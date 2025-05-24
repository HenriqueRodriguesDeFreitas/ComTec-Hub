package com.norteck.comtechub.service;

import com.norteck.comtechub.model.Client;
import com.norteck.comtechub.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository repository;
    private final PasswordEncoder encoder;
    public ClientService(ClientRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public Client salvar(Client client) {
        client.setClientSecret(encoder.encode(client.getClientSecret()));
        return repository.save(client);
    }

    public Client obterPorClientId(String clientId){

    return repository.findByClientId(clientId);
    }
}
