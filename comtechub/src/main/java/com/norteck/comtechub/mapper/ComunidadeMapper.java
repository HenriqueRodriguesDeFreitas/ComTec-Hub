package com.norteck.comtechub.mapper;

import com.norteck.comtechub.dto.request.ComunidadeRequestDTO;
import com.norteck.comtechub.dto.response.ChatResponseDTO;
import com.norteck.comtechub.dto.response.ComunidadeComChatResponseDTO;
import com.norteck.comtechub.dto.response.ComunidadeResponseDTO;
import com.norteck.comtechub.dto.response.MensagemResponseDTO;
import com.norteck.comtechub.model.Chat;
import com.norteck.comtechub.model.Comunidade;
import com.norteck.comtechub.model.Mensagem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComunidadeMapper {

    Comunidade comunidadeDtoToComunidade(ComunidadeRequestDTO dto);
    List<ComunidadeResponseDTO> comunidadesToComunidadeDto(List<Comunidade> comunidade);
    ComunidadeResponseDTO comunidadeToComunidadeDto(Comunidade comunidade);

    default MensagemResponseDTO mensagemToMensagemDto(Mensagem mensagem) {
        return new MensagemResponseDTO(
                mensagem.getUsuario().getLogin(),
                mensagem.getDataHoraMensagem(),
                mensagem.getTexto()
        );
    }

    default ChatResponseDTO chatToChatDto(Chat chat) {
        if (chat == null) return new ChatResponseDTO(List.of());

        List<MensagemResponseDTO> mensagensDto = chat.getMensagens().stream()
                .map(this::mensagemToMensagemDto)
                .toList();

        return new ChatResponseDTO(mensagensDto);
    }

    default ComunidadeComChatResponseDTO comunidadeToComunidadeComChatDto(Comunidade comunidade) {
        return new ComunidadeComChatResponseDTO(
                comunidade.getId(),
                comunidade.getNome(),
                comunidade.getDescricao(),
                comunidade.getTipoComunidade(),
                List.of(chatToChatDto(comunidade.getChat())) // mesmo que seja 1, seu DTO espera uma lista
        );
    }
}
