package com.norteck.comtechub.repository;

import com.norteck.comtechub.model.Chat;
import com.norteck.comtechub.model.Mensagem;
import com.norteck.comtechub.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MensagemRepository extends JpaRepository<Mensagem, UUID> {
    List<Mensagem> findByDataHoraMensagemBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Mensagem> findByTextoContaining(String texto);
    List<Mensagem> findByUsuario(Usuario usuario);
    List<Mensagem> findByChat(Chat chat);
}
