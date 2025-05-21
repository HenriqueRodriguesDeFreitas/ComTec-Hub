package com.norteck.comtechub.controller;

import com.norteck.comtechub.dto.request.ComunidadeRequestDTO;
import com.norteck.comtechub.mapper.ComunidadeMapper;
import com.norteck.comtechub.service.ComunidadeService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("comunidades")
public class ComunidadeController {

    private final ComunidadeService comunidadeService;
    private final ComunidadeMapper comunidadeMapper;

    public ComunidadeController(ComunidadeService comunidadeService,
                                ComunidadeMapper comunidadeMapper) {
        this.comunidadeService = comunidadeService;
        this.comunidadeMapper = comunidadeMapper;
    }

    @PreAuthorize("hasRole('CADASTRADO')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid ComunidadeRequestDTO dto) {
       var comunidade = comunidadeMapper.comunidadeDtoToComunidade(dto);
        return ResponseEntity.ok(comunidadeService.save(comunidade));
    }

    @PermitAll
    @GetMapping
    public ResponseEntity<List<?>> findAll(){
        return ResponseEntity.ok(comunidadeService.findAll());
    }

    @PermitAll
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID id){
        return ResponseEntity.ok(comunidadeService.findById(id));
    }

    @PermitAll
    @GetMapping("/Nome-Containing")
    public ResponseEntity<?> findByNomeContaining(@RequestParam("nome") String nome){
        return ResponseEntity.ok(comunidadeService.findByNomeContaining(nome));
    }

    @PermitAll
    @GetMapping("/Tipo-Comunidade")
    public ResponseEntity<List<?>> findByTipoComunidade(@RequestParam("tipo")String tipo){
        return  ResponseEntity.ok(comunidadeService.findByTipoComunidade(tipo));
    }

    @PermitAll
    @GetMapping("/Descricao")
    public ResponseEntity<List<?>> findByDescricao(@RequestParam("descricao") String descricao){
        return ResponseEntity.ok(comunidadeService.findByDescricao(descricao));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> update(
                                    @RequestParam("idComunidade") UUID idCommunidade,
                                    @RequestBody @Valid ComunidadeRequestDTO dto){
        var comunidade = comunidadeMapper.comunidadeDtoToComunidade(dto);
        return ResponseEntity.ok(comunidadeService.update(idCommunidade, comunidade));
    }

    @PreAuthorize("hasRole('CADASTRADO')")
    @PostMapping("addUsuario")
    public ResponseEntity<?> addUsuarioNaComunidade(@RequestParam("idComunidade") UUID idComunidade,
                                                    @RequestParam("senha") Integer senha){
        return ResponseEntity.ok(comunidadeService.addUsuarioNaComunidade(idComunidade, senha));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteById(@RequestParam("idUsuario") UUID idUsuario,
                                        @RequestParam("idComunidade") UUID idComunidade){
        comunidadeService.deleteByIdCascade(idComunidade);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
