package com.norteck.comtechub.controller;

import com.norteck.comtechub.dto.request.MensagemRequestDTO;
import com.norteck.comtechub.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("chats")
@PreAuthorize("hasRole('CADASTRADO')")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }


    @GetMapping
    public ResponseEntity<?> buscarChatDaComunidade(@RequestParam("idComunidade") UUID idComunidade){
        return ResponseEntity.ok(chatService.findChatComunidade(idComunidade));
    }

    @PostMapping
    public ResponseEntity<?> addNovaMensagemNoChat(@RequestParam("idComunidade") UUID idComunidade,
                                                   @RequestBody MensagemRequestDTO dto){
        return ResponseEntity.ok(chatService.salvarMensagemNoChat(idComunidade, dto));
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> findByTexto(@RequestParam("idComunidade") UUID idComunidade,
                                         @RequestBody MensagemRequestDTO dto){
        return ResponseEntity.ok(chatService.findByTexto(idComunidade, dto));
    }

    @GetMapping("/data")
    public ResponseEntity<?> findByData(@RequestParam("idComunidade") UUID idComunidade,
                                        @RequestParam("dataInicio") String dataInicio){
        return ResponseEntity.ok(chatService.findByData(idComunidade, dataInicio));
    }
}
