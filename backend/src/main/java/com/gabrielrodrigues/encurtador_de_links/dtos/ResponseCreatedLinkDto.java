package com.gabrielrodrigues.encurtador_de_links.dtos;

import jakarta.validation.constraints.NotBlank;

public record ResponseCreatedLinkDto(
        @NotBlank
        String short_url,

        @NotBlank
        String original_url
) {}
