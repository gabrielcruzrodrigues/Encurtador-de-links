package com.gabrielrodrigues.encurtador_de_links.controllers;

import com.gabrielrodrigues.encurtador_de_links.security.dtos.AuthenticatedResponseDto;
import com.gabrielrodrigues.encurtador_de_links.security.dtos.LoginDto;
import com.gabrielrodrigues.encurtador_de_links.security.dtos.RegisterDto;
import com.gabrielrodrigues.encurtador_de_links.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authenticação", description = "Gerenciamento da conta do usuário | SEM O REDIRECIONAMENTO DO PROXY REVERSO")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Fazer login na conta do usuário | roles permitidas: USER, ADMIN")
    @PostMapping("/login")
    public ResponseEntity<AuthenticatedResponseDto> login (@RequestBody LoginDto request) throws Exception {
        return ResponseEntity.ok().body(this.authenticationService.login(request));
    }

    @Operation(summary = "Criar uma conta para o usuário | roles permitidas: USER, ADMIN")
    @PostMapping("/register")
    public ResponseEntity<AuthenticatedResponseDto> register(@RequestBody RegisterDto request) throws Exception {
        return ResponseEntity.ok().body(this.authenticationService.register(request));
    }
}
