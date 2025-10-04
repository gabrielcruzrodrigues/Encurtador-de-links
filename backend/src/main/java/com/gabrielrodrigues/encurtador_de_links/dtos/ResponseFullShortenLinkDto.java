package com.gabrielrodrigues.encurtador_de_links.dtos;

public record ResponseFullShortenLinkDto(
        String shortUrl,
        String originalUrl,
        int clicks
) {
}
