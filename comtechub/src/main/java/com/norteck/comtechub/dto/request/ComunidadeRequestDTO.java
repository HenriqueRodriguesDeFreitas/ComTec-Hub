package com.norteck.comtechub.dto.request;

import com.norteck.comtechub.model.enums.TipoComunidade;
import jakarta.validation.constraints.NotBlank;

public record ComunidadeRequestDTO(@NotBlank String nome,
                                   String descricao,
                                   Integer codigoAcesso,
                                   TipoComunidade tipoComunidade) {
}
