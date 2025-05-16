package com.norteck.comtechub.mapper;

import com.norteck.comtechub.dto.request.ComunidadeRequestDTO;
import com.norteck.comtechub.dto.response.ComunidadeResponseDTO;
import com.norteck.comtechub.model.Comunidade;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComunidadeMapper {

    Comunidade comunidadeDtoToComunidade(ComunidadeRequestDTO dto);
    ComunidadeResponseDTO comunidadeToComunidadeDto(Comunidade comunidade);
}
