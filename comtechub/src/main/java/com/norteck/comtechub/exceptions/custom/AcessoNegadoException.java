package com.norteck.comtechub.exceptions.custom;

import org.springframework.security.access.AccessDeniedException;

public class AcessoNegadoException extends AccessDeniedException {
    public AcessoNegadoException(String message) {
        super(message);
    }
}
