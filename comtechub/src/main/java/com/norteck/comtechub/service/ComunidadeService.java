package com.norteck.comtechub.service;

import com.norteck.comtechub.dto.response.ChatResponseDTO;
import com.norteck.comtechub.dto.response.ComunidadeComChatResponseDTO;
import com.norteck.comtechub.dto.response.ComunidadeResponseDTO;
import com.norteck.comtechub.dto.response.MensagemResponseDTO;
import com.norteck.comtechub.exceptions.custom.ConflictException;
import com.norteck.comtechub.exceptions.custom.EntityNotFoundException;
import com.norteck.comtechub.mapper.ComunidadeMapper;
import com.norteck.comtechub.mapper.MensagemMapper;
import com.norteck.comtechub.model.*;
import com.norteck.comtechub.model.enums.RoleNaComunidade;
import com.norteck.comtechub.model.enums.TipoComunidade;
import com.norteck.comtechub.repository.ComunidadeRepository;
import com.norteck.comtechub.repository.MensagemRepository;
import com.norteck.comtechub.repository.UsuarioComunidadeRepository;
import com.norteck.comtechub.repository.UsuarioRepository;
import com.norteck.comtechub.security.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ComunidadeService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioComunidadeRepository usuarioComunidadeRepository;
    private final ComunidadeRepository comunidadeRepository;
    private final MensagemRepository mensagemRepository;
    private final ComunidadeMapper comunidadeMapper;
    private final MensagemMapper mensagemMapper;
    private final SecurityService securityService;

    public ComunidadeService(UsuarioRepository usuarioRepository,
                             UsuarioComunidadeRepository usuarioComunidadeRepository,
                             ComunidadeRepository comunidadeRepository,
                             MensagemRepository mensagemRepository,
                             MensagemMapper mensagemMapper,
                             ComunidadeMapper comunidadeMapper,
                             SecurityService securityService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioComunidadeRepository = usuarioComunidadeRepository;
        this.comunidadeRepository = comunidadeRepository;
        this.mensagemRepository = mensagemRepository;
        this.mensagemMapper = mensagemMapper;
        this.comunidadeMapper = comunidadeMapper;
        this.securityService = securityService;
    }

    @Transactional
    public ComunidadeResponseDTO save(Comunidade comunidade) {
        var usuario = securityService.obterUsuarioAutenticado();

        Comunidade novaComunidade = new Comunidade();
        novaComunidade.setNome(comunidade.getNome());
        novaComunidade.setDescricao(comunidade.getDescricao());
        novaComunidade.setTipoComunidade(comunidade.getTipoComunidade());
        novaComunidade.setCodigoAcesso(comunidade.getCodigoAcesso());

        comunidadePrivada(novaComunidade);

        Chat chat = new Chat();
        chat.setComunidade(novaComunidade);
        novaComunidade.setChat(chat);

        UsuarioComunidade usuarioComunidade = new UsuarioComunidade();
        usuarioComunidade.setUsuario(usuario);
        usuarioComunidade.setComunidade(novaComunidade);
        usuarioComunidade.setRoleNaComunidade(RoleNaComunidade.ADMIN);


        List<UsuarioComunidade> usuarioComunidades = new ArrayList<>();
        usuarioComunidades.add(usuarioComunidade);

        novaComunidade.setUsuarioComunidade(usuarioComunidades);
        return convertObjectToDto(comunidadeRepository.save(novaComunidade));
    }

    @Transactional
    public ComunidadeResponseDTO update(UUID idUsuario, UUID idComunidade, Comunidade comunidadeUpdate) {
        var usuario = verificarUsuarioExiste(idUsuario);
        var comunidade = verificarComunidadeExiste(idComunidade);

        var usuarioComunidade = usuarioComunidadeRepository.findByUsuario(usuario)
                .stream().filter(uc -> uc.getComunidade().equals(comunidade))
                .findFirst()
                .orElseThrow(() -> new ConflictException("Usuario não faz parte da comunidade."));

        if (!usuarioComunidade.getRoleNaComunidade().equals(RoleNaComunidade.ADMIN)) {
            throw new ConflictException("Apenas administradores podem editar a comunidade");
        }

        comunidade.setNome(comunidadeUpdate.getNome());
        comunidade.setDescricao(comunidadeUpdate.getDescricao());
        comunidade.setTipoComunidade(comunidadeUpdate.getTipoComunidade());


        if (comunidade.getTipoComunidade().equals(TipoComunidade.PUBLICO)) {
            comunidade.setCodigoAcesso(null); // Limpa código de acesso se público
        } else {
            comunidade.setCodigoAcesso(comunidadeUpdate.getCodigoAcesso()); // Atualiza se privado
        }

        comunidadePrivada(comunidade);
        return convertObjectToDto(comunidadeRepository.save(comunidade));
    }

    public ComunidadeComChatResponseDTO findById(UUID idComunidade) {
        Comunidade comunidade = comunidadeRepository.findById(idComunidade)
                .orElseThrow(() -> new EntityNotFoundException("Comunidade não encontrada"));

        return comunidadeMapper.comunidadeToComunidadeComChatDto(comunidade);

    }

    public List<ComunidadeResponseDTO> findByNomeContaining(String nome) {
        return comunidadeRepository.findByNomeContainingIgnoreCase(nome)
                .stream().map(this::convertObjectToDto).collect(Collectors.toList());
    }

    public List<ComunidadeResponseDTO> findAll() {
        return comunidadeMapper.comunidadesToComunidadeDto(comunidadeRepository.findAll());
    }

    public List<ComunidadeResponseDTO> findByTipoComunidade(String tipo) {
        return comunidadeRepository.findByTipoComunidade(TipoComunidade.valueOf(tipo.toUpperCase()))
                .stream().map(this::convertObjectToDto).collect(Collectors.toList());
    }

    public List<ComunidadeResponseDTO> findByDescricao(String descricao) {
        return comunidadeRepository.findByDescricaoIgnoreCaseContaining(descricao)
                .stream().map(this::convertObjectToDto).collect(Collectors.toList());
    }

    public List<ComunidadeResponseDTO> findComunidadesDoUsuario(UUID id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        return usuarioComunidadeRepository.findByUsuario(usuario)
                .stream()
                .map(UsuarioComunidade::getComunidade)
                .map(this::convertObjectToDto).collect(Collectors.toList());

    }

    public void deleteByIdCascade(UUID idUsuario, UUID idComunidade) {
        var usuario =  verificarUsuarioExiste(idUsuario);
        var comunidade = verificarComunidadeExiste(idComunidade);

        var usuarioComunidade = usuarioComunidadeRepository.findByUsuario(usuario)
                .stream().filter(uc -> uc.getComunidade().equals(comunidade))
                .findFirst()
                .orElseThrow(() -> new ConflictException("Usuario não faz parte da comunidade."));


        if (!usuarioComunidade.getRoleNaComunidade().equals(RoleNaComunidade.ADMIN)) {
            throw new ConflictException("Apenas administradores podem excluir comunidades");
        }

        usuarioComunidadeRepository.deleteById(usuarioComunidade.getId());
    }

    private Comunidade verificarComunidadeExiste(UUID idComunidade) {
        return comunidadeRepository.findById(idComunidade)
                .orElseThrow(() -> new EntityNotFoundException("Comunidade não encontrado."));
    }

    private Usuario verificarUsuarioExiste(UUID idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado."));
    }

    private void comunidadePrivada(Comunidade comunidade) {
        if (comunidade.getTipoComunidade() == null) {
            throw new EntityNotFoundException("Defina a comunidade como PUBLICO ou PRIVADO");
        }
        if (comunidade.getTipoComunidade().equals(TipoComunidade.PRIVADO)
                && comunidade.getCodigoAcesso() == null) {
            throw new ConflictException("Campo senha precisa ser preenchido para comunidades privadas.");
        }
    }

    private ComunidadeResponseDTO convertObjectToDto(Comunidade comunidade) {
        List<Mensagem> mensagens = mensagemRepository.findAll();

        List<MensagemResponseDTO> mensagensResponse = mensagens
                .stream().map(m -> new MensagemResponseDTO(m.getUsuario().getLogin(),
                        m.getDataHoraMensagem(), m.getTexto())).collect(Collectors.toList());

        ChatResponseDTO chat = new ChatResponseDTO(mensagensResponse);

        return new ComunidadeResponseDTO(
                comunidade.getId(), comunidade.getNome(), comunidade.getDescricao(),
               comunidade.getTipoComunidade());

    }
}
