package com.norteck.comtechub.service;

import com.norteck.comtechub.dto.request.MensagemRequestDTO;
import com.norteck.comtechub.dto.response.ChatResponseDTO;
import com.norteck.comtechub.exceptions.custom.ConflictException;
import com.norteck.comtechub.exceptions.custom.EntityNotFoundException;
import com.norteck.comtechub.mapper.ChatMapper;
import com.norteck.comtechub.model.Comunidade;
import com.norteck.comtechub.model.Mensagem;
import com.norteck.comtechub.model.Usuario;
import com.norteck.comtechub.model.enums.TipoComunidade;
import com.norteck.comtechub.repository.ComunidadeRepository;
import com.norteck.comtechub.repository.MensagemRepository;
import com.norteck.comtechub.repository.UsuarioComunidadeRepository;
import com.norteck.comtechub.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ChatService {

    private final ComunidadeRepository comunidadeRepository;
    private final MensagemRepository mensagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioComunidadeRepository usuarioComunidadeRepository;
    private final ChatMapper chatMapper;

    public ChatService(
            ComunidadeRepository comunidadeRepository,
            MensagemRepository mensagemRepository,
            UsuarioRepository usuarioRepository,
            UsuarioComunidadeRepository usuarioComunidadeRepository,
            ChatMapper chatMapper) {
        this.comunidadeRepository = comunidadeRepository;
        this.mensagemRepository = mensagemRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioComunidadeRepository = usuarioComunidadeRepository;
        this.chatMapper = chatMapper;
    }

    public ChatResponseDTO findChatComunidade(UUID idUsuario, UUID idComunidade) {
        var usuario = verificarUsuarioExiste(idUsuario);
        var comunidade = verificarComunidadeExiste(idComunidade);

        verificarUsuarioPertenceComunidade(comunidade, usuario);

        if (comunidade.getChat() == null) {
            throw new ConflictException("Essa comunidade ainda não possui um chat.");
        }

        var mensagens = mensagemRepository.findByChat(comunidade.getChat());

        return chatMapper.toChatResponse(comunidade.getChat(), mensagens);
    }

    public ChatResponseDTO findByData(UUID idUsuario, UUID idComunidade, String dataInicio) {
        var usuario = verificarUsuarioExiste(idUsuario);
        var comunidade = verificarComunidadeExiste(idComunidade);

        verificarUsuarioPertenceComunidade(comunidade, usuario);

        if (comunidade.getChat() == null) {
            throw new ConflictException("Essa comunidade ainda não possui um chat.");
        }
        LocalDateTime inicio;
        try {
            inicio = LocalDateTime.parse(dataInicio + "T00:00:00");
        } catch (DateTimeException e) {
            throw new ConflictException("Formato de data invalido. Use yyyy-MM-dd, ex: 2024-08-01");
        }
        LocalDateTime fim = LocalDateTime.now();

        var mensagens = mensagemRepository.findByDataHoraMensagemBetween(inicio, fim)
                .stream()
                .filter(m -> m.getChat().equals(comunidade.getChat()))
                .toList();

        return chatMapper.toChatResponse(comunidade.getChat(), mensagens);
    }

    public ChatResponseDTO salvarMensagemNoChat(UUID idUsuario, UUID idComunidade, MensagemRequestDTO request) {
        var usuario = verificarUsuarioExiste(idUsuario);
        var comunidade = verificarComunidadeExiste(idComunidade);

        if (comunidade.getTipoComunidade().equals(TipoComunidade.PRIVADO)) {
            boolean pertence = usuarioComunidadeRepository.findByUsuario(usuario)
                    .stream()
                    .anyMatch(uc -> uc.getComunidade().getId().equals(comunidade.getId()));

            if (!pertence) {
                throw new ConflictException("Usuário não pertence a essa comunidade privada.");
            }
        }

        Mensagem mensagem = new Mensagem();
        mensagem.setTexto(request.texto());
        mensagem.setDataHoraMensagem(LocalDateTime.now());
        mensagem.setUsuario(usuario);
        mensagem.setChat(comunidade.getChat());
        mensagemRepository.save(mensagem);

        return chatMapper.toChatResponse(comunidade.getChat(), mensagemRepository.findByChat(comunidade.getChat()));
    }

    public ChatResponseDTO findByTexto(UUID idUsuario, UUID idComunidade, MensagemRequestDTO request) {
        var usuario = verificarUsuarioExiste(idUsuario);
        var comunidade = verificarComunidadeExiste(idComunidade);

        verificarUsuarioPertenceComunidade(comunidade, usuario);

        if (comunidade.getChat() == null) {
            throw new ConflictException("Essa comunidade ainda não possui um chat.");
        }

        var mensagens = mensagemRepository.findByTextoContaining(request.texto());
        return chatMapper.toChatResponse(comunidade.getChat(), mensagens);
    }

    private void verificarUsuarioPertenceComunidade(Comunidade comunidade, Usuario usuario) {
        if (comunidade.getTipoComunidade().equals(TipoComunidade.PRIVADO)) {
            boolean pertence = usuarioComunidadeRepository.findByUsuario(usuario)
                    .stream()
                    .anyMatch(uc -> uc.getComunidade().getId().equals(comunidade.getId()));

            if (!pertence) {
                throw new ConflictException("Usuário não pertence a essa comunidade privada.");
            }
        }
    }

    private Comunidade verificarComunidadeExiste(UUID idComunidade) {
        return comunidadeRepository.findById(idComunidade)
                .orElseThrow(() -> new EntityNotFoundException("Comunidade não encontrado."));
    }

    private Usuario verificarUsuarioExiste(UUID idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado."));
    }

}
