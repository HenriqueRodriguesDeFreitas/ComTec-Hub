package com.norteck.comtechub.controller;

import com.norteck.comtechub.dto.request.ComunidadeRequestDTO;
import com.norteck.comtechub.mapper.ComunidadeMapper;
import com.norteck.comtechub.security.SecurityService;
import com.norteck.comtechub.service.ComunidadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("comunidades")
public class ComunidadeController {

    private final ComunidadeService comunidadeService;
    private final ComunidadeMapper comunidadeMapper;
    private final SecurityService securityService;
    public ComunidadeController(ComunidadeService comunidadeService,
                                ComunidadeMapper comunidadeMapper,
                                SecurityService securityService) {
        this.comunidadeService = comunidadeService;
        this.comunidadeMapper = comunidadeMapper;
        this.securityService = securityService;
    }

    @PreAuthorize("hasRole('CADASTRADO')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid ComunidadeRequestDTO dto,
                                  Authentication authentication) {
       var comunidade = comunidadeMapper.comunidadeDtoToComunidade(dto);
        return ResponseEntity.ok(comunidadeService.save(comunidade));
    }

    @GetMapping
    public ResponseEntity<List<?>> findAll(){
        return ResponseEntity.ok(comunidadeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID id){
        return ResponseEntity.ok(comunidadeService.findById(id));
    }

    @GetMapping("/Nome-Containing")
    public ResponseEntity<?> findByNomeContaining(@RequestParam("nome") String nome){
        return ResponseEntity.ok(comunidadeService.findByNomeContaining(nome));
    }

    @GetMapping("/Tipo-Comunidade")
    public ResponseEntity<List<?>> findByTipoComunidade(@RequestParam("tipo")String tipo){
        return  ResponseEntity.ok(comunidadeService.findByTipoComunidade(tipo));
    }

    @GetMapping("/Descricao")
    public ResponseEntity<List<?>> findByDescricao(@RequestParam("descricao") String descricao){
        return ResponseEntity.ok(comunidadeService.findByDescricao(descricao));
    }

    @GetMapping("/Usuario")
    public ResponseEntity<List<?>> findByComunidadesDoUsuario(@RequestParam("id") UUID id){
        return ResponseEntity.ok(comunidadeService.findComunidadesDoUsuario(id));
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestParam("idUsuario") UUID idUsuario,
                                    @RequestParam("idComunidade") UUID idCommunidade,
                                    @RequestBody ComunidadeRequestDTO dto){
        var comunidade = comunidadeMapper.comunidadeDtoToComunidade(dto);
        return ResponseEntity.ok(comunidadeService.update(idUsuario, idCommunidade, comunidade));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteById(@RequestParam("idUsuario") UUID idUsuario,
                                        @RequestParam("idComunidade") UUID idComunidade){
        comunidadeService.deleteByIdCascade(idUsuario, idComunidade);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
