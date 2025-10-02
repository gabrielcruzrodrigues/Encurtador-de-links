package com.gabrielrodrigues.encurtador_de_links.controllers;

import com.gabrielrodrigues.encurtador_de_links.dtos.ResponseShortenLinkDto;
import com.gabrielrodrigues.encurtador_de_links.dtos.ShortenRequestDto;
import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.TokenNotFoundException;
import com.gabrielrodrigues.encurtador_de_links.services.LinkService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/link")
public class LinkController {
    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/test")
    public String test() {
        return "pong";
    }

    @PostMapping("/shorten")
    public ResponseEntity<ResponseShortenLinkDto> shortenLink(@RequestBody ShortenRequestDto shortenRequest, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof Jwt jwt))
            throw new TokenNotFoundException("O token jwt não foi encontrado na requisição!");

        Long userId = jwt.getClaim("id");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.linkService.createShortUrl(shortenRequest.url(), request, userId));

    }
}
