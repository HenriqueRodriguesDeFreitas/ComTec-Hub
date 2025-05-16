package com.norteck.comtechub.dto.response;

import com.norteck.comtechub.model.enums.TipoComunidade;

import java.util.UUID;

public record ComunidadeResponseDTO(UUID id, String nome, String descricao, Integer codigoAcesso,
                                    TipoComunidade tipoComunidade, ChatResponseDTO chat) {
}
