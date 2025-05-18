package com.norteck.comtechub.mapper;

import com.norteck.comtechub.dto.response.ChatResponseDTO;
import com.norteck.comtechub.dto.response.MensagemResponseDTO;
import com.norteck.comtechub.model.Chat;
import com.norteck.comtechub.model.Mensagem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "mensagens", source = "mensagens") // precisa criar método auxiliar para isso
    ChatResponseDTO toChatResponse(Chat chat, List<Mensagem> mensagens);

    // Mapeia Mensagem para MensagemResponseDTO
    @Mapping(target = "usuario", source = "usuario.login")
    @Mapping(target = "dataHora", source = "dataHoraMensagem")
    @Mapping(target = "texto", source = "texto")
    MensagemResponseDTO toMensagemResponse(Mensagem mensagem);

    List<MensagemResponseDTO> toMensagemResponseList(List<Mensagem> mensagens);
}
