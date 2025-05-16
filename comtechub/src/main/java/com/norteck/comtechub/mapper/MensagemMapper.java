package com.norteck.comtechub.mapper;

import com.norteck.comtechub.dto.request.MensagemRequestDTO;
import com.norteck.comtechub.dto.response.MensagemResponseDTO;
import com.norteck.comtechub.model.Mensagem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MensagemMapper {

    Mensagem mensagemDtoToMensagem(MensagemRequestDTO dto);

    @Mapping(source = "usuario.login", target = "usuario")
    MensagemResponseDTO mensagemToMensagemDto(Mensagem mensagem);
}
