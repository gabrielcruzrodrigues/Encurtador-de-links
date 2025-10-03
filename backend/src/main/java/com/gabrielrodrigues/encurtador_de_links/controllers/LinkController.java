package com.gabrielrodrigues.encurtador_de_links.controllers;

import com.gabrielrodrigues.encurtador_de_links.dtos.ResponseListAllLinks;
import com.gabrielrodrigues.encurtador_de_links.dtos.ResponseShortenLinkDto;
import com.gabrielrodrigues.encurtador_de_links.dtos.ShortenRequestDto;
import com.gabrielrodrigues.encurtador_de_links.enums.RoleEnum;
import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.ContentNotAvaliableToTheUserException;
import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.PathVariableNullableException;
import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.TokenNotFoundException;
import com.gabrielrodrigues.encurtador_de_links.models.Link;
import com.gabrielrodrigues.encurtador_de_links.models.User;
import com.gabrielrodrigues.encurtador_de_links.security.accessInterfaces.AdminAccess;
import com.gabrielrodrigues.encurtador_de_links.security.accessInterfaces.UserAccess;
import com.gabrielrodrigues.encurtador_de_links.services.LinkService;
import com.gabrielrodrigues.encurtador_de_links.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/link")
public class LinkController {
    private final LinkService linkService;
    private final UserService userService;

    public LinkController(LinkService linkService, UserService userService) {
        this.linkService = linkService;
        this.userService = userService;
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

//    @UserAccess
    @GetMapping("/links/{userId}")
    public ResponseEntity<List<ResponseListAllLinks>> GetLinksByUserId(@PathVariable Long userId, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof Jwt jwt))
            throw new TokenNotFoundException("O token jwt não foi encontrado na requisição!");

        Long userIdJwtToken = jwt.getClaim("id");
        User user = this.userService.getById(userId);

        if (!Objects.equals(user.getId(), userIdJwtToken))
            throw new ContentNotAvaliableToTheUserException("Esses dados não pertencem ao usuário logado!");

        Page<Link> links = this.linkService.getAllByUserId(userId, pageable);
        List<ResponseListAllLinks> response = links.stream()
                .map(link -> new ResponseListAllLinks(
                        link.getShortUrl(),
                        link.getOriginalUrl(),
                        link.getClicks()
                ))
                .toList();
        return ResponseEntity.ok().body(response);
    }

//    @AdminAccess
    @GetMapping("/links")
    public ResponseEntity<List<ResponseListAllLinks>> getAllLinks(Pageable pageable) {
        Page<Link> links = this.linkService.getAll(pageable);
        List<ResponseListAllLinks> response = links.stream()
                .map(link -> new ResponseListAllLinks(
                        link.getShortUrl(),
                        link.getOriginalUrl(),
                        link.getClicks()
                ))
                .toList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{shortUrl}")
    public void redirect(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        if (shortUrl.isEmpty())
            throw new PathVariableNullableException("A váriável da url é nula");

        String originalUlr = this.linkService.findOriginalUrlByShortUrl(shortUrl);
        response.sendRedirect(originalUlr);
    }
}
