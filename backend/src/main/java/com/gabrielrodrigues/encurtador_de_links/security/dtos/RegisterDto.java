package com.gabrielrodrigues.encurtador_de_links.security.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegisterDto (
        @NotBlank
        @Length(max = 50)
        String username,

        @NotBlank
        @Email
        @Length(max = 50)
        String email,

        @NotBlank
        String password
) {}
