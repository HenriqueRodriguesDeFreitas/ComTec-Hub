package com.norteck.comtechub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(@Email String email, @NotBlank String login, @NotBlank String senha) {
}
