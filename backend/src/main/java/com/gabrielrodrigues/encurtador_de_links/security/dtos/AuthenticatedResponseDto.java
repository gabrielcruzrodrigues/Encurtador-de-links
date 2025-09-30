package com.gabrielrodrigues.encurtador_de_links.security.dtos;

public record AuthenticatedResponseDto (
        Long id,
        String username,
        String accessToken,
        String role
) {}
