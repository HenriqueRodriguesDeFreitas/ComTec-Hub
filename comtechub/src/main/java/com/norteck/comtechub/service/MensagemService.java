package com.norteck.comtechub.service;

import com.norteck.comtechub.repository.ChatRepository;
import com.norteck.comtechub.repository.ComunidadeRepository;
import com.norteck.comtechub.repository.MensagemRepository;
import org.springframework.stereotype.Service;

@Service
public class MensagemService {

    private final ComunidadeRepository comunidadeRepository;
    private final ChatRepository chatRepository;
    private final MensagemRepository mensagemRepository;

    public MensagemService(ComunidadeRepository comunidadeRepository,
                           ChatRepository chatRepository,
                           MensagemRepository mensagemRepository) {
        this.comunidadeRepository = comunidadeRepository;
        this.chatRepository = chatRepository;
        this.mensagemRepository = mensagemRepository;
    }


}
