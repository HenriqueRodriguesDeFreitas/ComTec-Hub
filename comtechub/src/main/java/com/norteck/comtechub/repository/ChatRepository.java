package com.norteck.comtechub.repository;

import com.norteck.comtechub.model.Chat;
import com.norteck.comtechub.model.Comunidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
  Optional<Chat> findByComunidade(Comunidade comunidade);

}
