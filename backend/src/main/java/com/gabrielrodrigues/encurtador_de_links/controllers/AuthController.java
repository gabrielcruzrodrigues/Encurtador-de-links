package com.gabrielrodrigues.encurtador_de_links.controllers;

import com.gabrielrodrigues.encurtador_de_links.security.dtos.AuthenticatedResponseDto;
import com.gabrielrodrigues.encurtador_de_links.security.dtos.LoginDto;
import com.gabrielrodrigues.encurtador_de_links.security.dtos.RegisterDto;
import com.gabrielrodrigues.encurtador_de_links.security.service.AuthenticationService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticatedResponseDto> login (@RequestBody LoginDto request) throws Exception {
        return ResponseEntity.ok().body(this.authenticationService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticatedResponseDto> register(@RequestBody RegisterDto request) throws Exception {
        return ResponseEntity.ok().body(this.authenticationService.register(request));
    }
}
