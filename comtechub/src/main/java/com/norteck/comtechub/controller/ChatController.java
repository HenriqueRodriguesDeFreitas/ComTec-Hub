package com.norteck.comtechub.controller;

import com.norteck.comtechub.dto.request.MensagemRequestDTO;
import com.norteck.comtechub.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<?> buscarChatDaComunidade(@RequestParam("idUsuario") UUID idUsuario,
                                                    @RequestParam("idComunidade") UUID idComunidade){
        return ResponseEntity.ok(chatService.findChatComunidade(idUsuario,idComunidade));
    }

    @PostMapping
    public ResponseEntity<?> addNovaMensagemNoChat(@RequestParam("idUsuario") UUID idUsuario,
                                                   @RequestParam("idComunidade") UUID idComunidade,
                                                   @RequestBody MensagemRequestDTO dto){
        return ResponseEntity.ok(chatService.salvarMensagemNoChat(idUsuario, idComunidade, dto));
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> findByTexto(@RequestParam("idUsuario") UUID idUsuario,
                                               @RequestParam("idComunidade") UUID idComunidade,
                                               @RequestBody MensagemRequestDTO dto){
        return ResponseEntity.ok(chatService.findByTexto(idUsuario, idComunidade, dto));
    }

    @GetMapping("/data")
    public ResponseEntity<?> findByData(@RequestParam("idUsuario") UUID idUsuario,
                                        @RequestParam("idComunidade") UUID idComunidade,
                                        @RequestParam("dataInicio") String dataInicio){
        return ResponseEntity.ok(chatService.findByData(idUsuario, idComunidade, dataInicio));
    }
}
