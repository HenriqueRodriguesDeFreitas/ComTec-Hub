package com.norteck.comtechub.dto.response;

import java.util.List;

public record ChatResponseDTO(List<MensagemResponseDTO> mensagens) {
}
