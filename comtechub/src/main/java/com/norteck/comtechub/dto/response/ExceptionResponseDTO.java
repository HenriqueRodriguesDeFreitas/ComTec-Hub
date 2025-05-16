package com.norteck.comtechub.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ExceptionResponseDTO(@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime timestamp,
                                   int status, String erro, String mensagem) {
}
