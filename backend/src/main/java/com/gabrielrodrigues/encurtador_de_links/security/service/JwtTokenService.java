package com.gabrielrodrigues.encurtador_de_links.security.service;

import com.gabrielrodrigues.encurtador_de_links.models.User;
import com.gabrielrodrigues.encurtador_de_links.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {
    private final JwtEncoder jwtEncoder;
    private final UserService userService;

    public JwtTokenService(JwtEncoder jwtEncoder, UserService userService) {
        this.jwtEncoder = jwtEncoder;
        this.userService = userService;
    }

    public String generateToken(Authentication authentication, Long userId) {
        Instant now = Instant.now();
        long expiry = 86400;

        String scopes = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        User user = this.userService.getById(userId);

        var claims = JwtClaimsSet.builder()
                .issuer("encurta-link")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scopes)
                .claim("id", userId)
                .claim("role", user.getRole())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
