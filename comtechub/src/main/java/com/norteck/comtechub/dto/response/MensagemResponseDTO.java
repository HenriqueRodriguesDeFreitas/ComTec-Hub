package com.norteck.comtechub.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record MensagemResponseDTO(String usuario,
                                  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime dataHora,
                                  String texto) {
}
