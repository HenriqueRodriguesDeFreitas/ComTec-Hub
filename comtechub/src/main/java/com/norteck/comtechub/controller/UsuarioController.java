package com.norteck.comtechub.controller;

import com.norteck.comtechub.dto.request.UsuarioRequestDTO;
import com.norteck.comtechub.dto.response.UsuarioResponseDTO;
import com.norteck.comtechub.mapper.UsuarioMapper;
import com.norteck.comtechub.model.Usuario;
import com.norteck.comtechub.service.UsuarioService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @PermitAll
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid UsuarioRequestDTO requestDTO) {
        var usuario = usuarioMapper.usuarioDtoToUsuario(requestDTO);
        return ResponseEntity.ok(usuarioMapper.usuarioToUsuarioDto(usuarioService.save(usuario)));
    }

    @GetMapping
    public ResponseEntity<List<?>> findAll() {
        List<Usuario> usuarios = usuarioService.findAll();
        List<UsuarioResponseDTO> response = usuarioMapper.usuariosToUsuariosDto(usuarios);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id")UUID id){
       return ResponseEntity.ok(usuarioService.findById(id));
    }

    @PreAuthorize("hasRole('CADASTRADO')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody UsuarioRequestDTO requestDTO){
        var usuarioAtualizado = usuarioService.update(usuarioMapper.usuarioDtoToUsuario(requestDTO));
        return ResponseEntity.ok(usuarioMapper.usuarioToUsuarioDto(usuarioAtualizado));
    }

    @PreAuthorize("hasRole('CADASTRADO')")
    @GetMapping("/Usuario")
    public ResponseEntity<List<?>> findByComunidadesDoUsuario(){
        return ResponseEntity.ok(usuarioService.findComunidadesDoUsuario());
    }
}
