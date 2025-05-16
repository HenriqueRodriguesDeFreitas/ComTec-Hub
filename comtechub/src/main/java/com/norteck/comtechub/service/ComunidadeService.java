package com.norteck.comtechub.service;

import com.norteck.comtechub.dto.response.ChatResponseDTO;
import com.norteck.comtechub.dto.response.ComunidadeResponseDTO;
import com.norteck.comtechub.dto.response.MensagemResponseDTO;
import com.norteck.comtechub.exceptions.custom.ConflictException;
import com.norteck.comtechub.exceptions.custom.EntityNotFoundException;
import com.norteck.comtechub.mapper.MensagemMapper;
import com.norteck.comtechub.model.Chat;
import com.norteck.comtechub.model.Comunidade;
import com.norteck.comtechub.model.Mensagem;
import com.norteck.comtechub.model.UsuarioComunidade;
import com.norteck.comtechub.model.enums.RoleNaComunidade;
import com.norteck.comtechub.model.enums.TipoComunidade;
import com.norteck.comtechub.repository.ComunidadeRepository;
import com.norteck.comtechub.repository.MensagemRepository;
import com.norteck.comtechub.repository.UsuarioComunidadeRepository;
import com.norteck.comtechub.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ComunidadeService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioComunidadeRepository usuarioComunidadeRepository;
    private final ComunidadeRepository comunidadeRepository;
    private final MensagemRepository mensagemRepository;

    private final MensagemMapper mensagemMapper;

    public ComunidadeService(UsuarioRepository usuarioRepository,
                             UsuarioComunidadeRepository usuarioComunidadeRepository,
                             ComunidadeRepository comunidadeRepository,
                             MensagemRepository mensagemRepository,
                             MensagemMapper mensagemMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioComunidadeRepository = usuarioComunidadeRepository;
        this.comunidadeRepository = comunidadeRepository;
        this.mensagemRepository = mensagemRepository;
        this.mensagemMapper = mensagemMapper;
    }

    @Transactional
    public ComunidadeResponseDTO save(UUID idUsuario, Comunidade comunidade) {
        var usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));

        Comunidade novaComunidade = new Comunidade();
        novaComunidade.setNome(comunidade.getNome());
        novaComunidade.setDescricao(comunidade.getDescricao());
        novaComunidade.setTipoComunidade(comunidade.getTipoComunidade());
        novaComunidade.setCodigoAcesso(comunidade.getCodigoAcesso());

        comunidadePrivada(novaComunidade);

        Chat chat = new Chat();
        chat.setComunidade(novaComunidade);

        UsuarioComunidade usuarioComunidade = new UsuarioComunidade(
                usuario, novaComunidade, RoleNaComunidade.ADMIN);
        novaComunidade.setChat(chat);
        novaComunidade.setUsuarioComunidade(List.of(usuarioComunidade));
        return convertObjectToDto(comunidadeRepository.save(novaComunidade));
    }

    public ComunidadeResponseDTO findById(UUID id){
        return convertObjectToDto(comunidadeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Comunidade não encontrada")));
    }

    public List<ComunidadeResponseDTO> findByNomeContaining(String nome){
        return comunidadeRepository.findByNomeContainingIgnoreCase(nome)
                .stream().map(this::convertObjectToDto).collect(Collectors.toList());
    }

    private void comunidadePrivada(Comunidade comunidade) {
        if (comunidade.getTipoComunidade().equals(TipoComunidade.PRIVADO)
                && comunidade.getCodigoAcesso() == null) {
            throw new ConflictException("Campo senha precisa ser preenchido para comunidades privadas.");
        }
    }

    private ComunidadeResponseDTO convertObjectToDto(Comunidade comunidade){

        List<Mensagem> mensagens = mensagemRepository.findAll();

        List<MensagemResponseDTO> mensagensResponse =  mensagens
                .stream().map(m -> new MensagemResponseDTO(m.getUsuario().getLogin(),
                        m.getDataHoraMensagem(), m.getTexto())).collect(Collectors.toList());

        ChatResponseDTO chat = new ChatResponseDTO(mensagensResponse);

        ComunidadeResponseDTO response = new ComunidadeResponseDTO(
                comunidade.getId(), comunidade.getNome(), comunidade.getDescricao(),
                comunidade.getCodigoAcesso(), comunidade.getTipoComunidade(), chat
        );
        return  response;
    }
}
