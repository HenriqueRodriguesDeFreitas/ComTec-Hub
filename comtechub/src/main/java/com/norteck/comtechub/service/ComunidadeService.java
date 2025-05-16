package com.norteck.comtechub.service;

import com.norteck.comtechub.dto.response.ComunidadeResponseDTO;
import com.norteck.comtechub.exceptions.custom.ConflictException;
import com.norteck.comtechub.exceptions.custom.EntityNotFoundException;
import com.norteck.comtechub.model.Chat;
import com.norteck.comtechub.model.Comunidade;
import com.norteck.comtechub.model.UsuarioComunidade;
import com.norteck.comtechub.model.enums.RoleNaComunidade;
import com.norteck.comtechub.model.enums.TipoComunidade;
import com.norteck.comtechub.repository.ComunidadeRepository;
import com.norteck.comtechub.repository.UsuarioComunidadeRepository;
import com.norteck.comtechub.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ComunidadeService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioComunidadeRepository usuarioComunidadeRepository;
    private final ComunidadeRepository comunidadeRepository;

    public ComunidadeService(UsuarioRepository usuarioRepository,
                             UsuarioComunidadeRepository usuarioComunidadeRepository,
                             ComunidadeRepository comunidadeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioComunidadeRepository = usuarioComunidadeRepository;
        this.comunidadeRepository = comunidadeRepository;
    }

    @Transactional
    public Comunidade save(UUID idUsuario, Comunidade comunidade) {
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

        UsuarioComunidade usuarioComunidade = new UsuarioComunidade(usuario, novaComunidade, RoleNaComunidade.ADMIN);
        novaComunidade.setChat(chat);
        novaComunidade.setUsuarioComunidade(List.of(usuarioComunidade));
        return comunidadeRepository.save(novaComunidade);
    }

    public Comunidade findById(UUID id){
        return comunidadeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Comunidade não encontrada"));
    }
    private void comunidadePrivada(Comunidade comunidade) {
        if (comunidade.getTipoComunidade().equals(TipoComunidade.PRIVADO)
                && comunidade.getCodigoAcesso() == null) {
            throw new ConflictException("Campo senha precisa ser preenchido para comunidades privadas.");
        }
    }
}
