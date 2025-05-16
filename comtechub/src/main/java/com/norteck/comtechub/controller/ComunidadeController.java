package com.norteck.comtechub.controller;

import com.norteck.comtechub.dto.request.ComunidadeRequestDTO;
import com.norteck.comtechub.mapper.ComunidadeMapper;
import com.norteck.comtechub.service.ComunidadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("comunidades")
public class ComunidadeController {

    private final ComunidadeService comunidadeService;
    private final ComunidadeMapper comunidadeMapper;

    public ComunidadeController(ComunidadeService comunidadeService, ComunidadeMapper comunidadeMapper) {
        this.comunidadeService = comunidadeService;
        this.comunidadeMapper = comunidadeMapper;
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> save(@PathVariable("id") UUID id, @RequestBody ComunidadeRequestDTO dto) {
        var request = comunidadeMapper.comunidadeDtoToComunidade(dto);
        var response = comunidadeMapper.comunidadeToComunidadeDto(comunidadeService.save(id, request));
        return ResponseEntity.ok(response);
    }
}
