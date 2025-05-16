package com.norteck.comtechub.mapper;

import com.norteck.comtechub.dto.request.UsuarioRequestDTO;
import com.norteck.comtechub.dto.response.UsuarioResponseDTO;
import com.norteck.comtechub.model.Usuario;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario usuarioDtoToUsuario(UsuarioRequestDTO usuarioDTO);
    UsuarioResponseDTO usuarioToUsuarioDto(Usuario usuario);
    List<UsuarioResponseDTO> usuariosToUsuariosDto(List<Usuario> usuarios);

}
