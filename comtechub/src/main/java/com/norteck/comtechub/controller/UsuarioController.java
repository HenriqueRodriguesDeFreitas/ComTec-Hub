package com.norteck.comtechub.controller;

import com.norteck.comtechub.dto.request.UsuarioRequestDTO;
import com.norteck.comtechub.mapper.UsuarioMapper;
import com.norteck.comtechub.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid UsuarioRequestDTO requestDTO){
        var usuario = usuarioMapper.usuarioDtoToUsuario(requestDTO);
        return ResponseEntity.ok(usuarioMapper.usuarioToUsuarioDto(usuarioService.save(usuario)));
    }
}
