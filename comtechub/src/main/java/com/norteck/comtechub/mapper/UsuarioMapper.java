package com.norteck.comtechub.mapper;

import com.norteck.comtechub.dto.request.RequestUsuarioDTO;
import com.norteck.comtechub.dto.response.ResponseUsuarioDTO;
import com.norteck.comtechub.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario usuarioDtoToUsuario(RequestUsuarioDTO usuarioDTO);
    ResponseUsuarioDTO usuarioToUsuarioDto(Usuario usuario);
}
