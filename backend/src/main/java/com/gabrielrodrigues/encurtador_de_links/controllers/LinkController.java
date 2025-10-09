package com.gabrielrodrigues.encurtador_de_links.controllers;

import com.gabrielrodrigues.encurtador_de_links.dtos.ResponseFullShortenLinkDto;
import com.gabrielrodrigues.encurtador_de_links.dtos.ResponseShortShortenLinkDto;
import com.gabrielrodrigues.encurtador_de_links.dtos.ShortenRequestDto;
import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.ContentNotAvaliableToTheUserException;
import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.PathVariableNullableException;
import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.TokenNotFoundException;
import com.gabrielrodrigues.encurtador_de_links.models.Link;
import com.gabrielrodrigues.encurtador_de_links.models.User;
import com.gabrielrodrigues.encurtador_de_links.security.accessInterfaces.AdminAccess;
import com.gabrielrodrigues.encurtador_de_links.security.accessInterfaces.UserAccess;
import com.gabrielrodrigues.encurtador_de_links.services.LinkService;
import com.gabrielrodrigues.encurtador_de_links.services.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.parser.Authorization;
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
@RequestMapping("/api/link")
public class LinkController {
    private final LinkService linkService;
    private final UserService userService;

    public LinkController(LinkService linkService, UserService userService) {
        this.linkService = linkService;
        this.userService = userService;
    }

    @AdminAccess
    @GetMapping("/ping")
    public String pong() {
        return "pong";
    }

    @AdminAccess
    @GetMapping("/links/{userId}")
    public ResponseEntity<List<ResponseFullShortenLinkDto>> GetLinksByUserId(@PathVariable Long userId, Pageable pageable) {
        Page<Link> links = this.linkService.getAllByUserId(userId, pageable);
        List<ResponseFullShortenLinkDto> response = links.stream()
                .map(link -> new ResponseFullShortenLinkDto(
                        link.getShortUrl(),
                        link.getOriginalUrl(),
                        link.getClicks()
                ))
                .toList();
        return ResponseEntity.ok().body(response);
    }

    @AdminAccess
    @GetMapping("/links")
    public ResponseEntity<List<ResponseFullShortenLinkDto>> getAllLinks(Pageable pageable) {
        Page<Link> links = this.linkService.getAll(pageable);
        List<ResponseFullShortenLinkDto> response = links.stream()
                .map(link -> new ResponseFullShortenLinkDto(
                        link.getShortUrl(),
                        link.getOriginalUrl(),
                        link.getClicks()
                ))
                .toList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/sh/{shortUrl}")
    public void redirectByShortUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        if (shortUrl.isEmpty())
            throw new PathVariableNullableException("A váriável da url é nula");

        String originalUlr = this.linkService.findOriginalUrlByShortUrl(shortUrl);
        response.sendRedirect(originalUlr);
    }

    @AdminAccess
    @GetMapping("/short/{shortUrl}")
    public ResponseEntity<ResponseFullShortenLinkDto> getByShortUrl(@PathVariable String shortUrl) {
        Link link = this.linkService.getByShortUrl(shortUrl);
        ResponseFullShortenLinkDto fullShortenLInkDto = new ResponseFullShortenLinkDto(
                link.getShortUrl(),
                link.getOriginalUrl(),
                link.getClicks()
        );
        return ResponseEntity.ok().body(fullShortenLInkDto);
    }

    @UserAccess
    @PostMapping("/shorten")
    public ResponseEntity<ResponseShortShortenLinkDto> shortenLink(@RequestBody ShortenRequestDto shortenRequest, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof Jwt jwt))
            throw new TokenNotFoundException("O token jwt não foi encontrado na requisição!");

        Long userId = jwt.getClaim("id");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.linkService.createShortUrl(shortenRequest.url(), request, userId));
    }

    @UserAccess
    @DeleteMapping("/{shortUrl}")
    public ResponseEntity<?> deleteByShortUrl(@PathVariable String shortUrl) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof Jwt jwt))
            throw new TokenNotFoundException("O token jwt não foi encontrado na requisição!");

        String role = jwt.getClaim("role");
        if (!role.equals("ADMIN")) {
            //Terminar validação
        }

        this.linkService.deleteByShortUrl(shortUrl);
        return ResponseEntity.noContent().build();
    }

//    private void validateUserToken(Authentication authentication) {
//        if (!(authentication.getPrincipal() instanceof Jwt jwt))
//            throw new TokenNotFoundException("O token jwt não foi encontrado na requisição!");
//
//        Long userIdJwtToken = jwt.getClaim("id");
//        User user = this.userService.getById(userIdJwtToken);
//
//        if (!Objects.equals(authentication.getName(), user.getUsername()))
//            throw new ContentNotAvaliableToTheUserException("Esses dados não pertencem ao usuário logado!");
//    }
}
