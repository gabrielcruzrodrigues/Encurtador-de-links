package com.gabrielrodrigues.encurtador_de_links.dtos;

public record ResponseListAllLinks(
        String shortUrl,
        String originalUrl,
        int clicks
) {
}
