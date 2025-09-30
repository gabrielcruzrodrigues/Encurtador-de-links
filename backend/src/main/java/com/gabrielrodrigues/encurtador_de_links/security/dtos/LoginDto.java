package com.gabrielrodrigues.encurtador_de_links.security.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDto (
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password
) {}
