package com.norteck.comtechub.repository;

import com.norteck.comtechub.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Client findByClientId(String clientId);
}
