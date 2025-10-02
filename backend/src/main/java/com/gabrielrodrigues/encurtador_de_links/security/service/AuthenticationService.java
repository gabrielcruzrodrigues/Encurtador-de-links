package com.gabrielrodrigues.encurtador_de_links.security.service;

import com.gabrielrodrigues.encurtador_de_links.enums.RoleEnum;
import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.EntityAlreadyExist;
import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.EntityNotFoundException;
import com.gabrielrodrigues.encurtador_de_links.models.User;
import com.gabrielrodrigues.encurtador_de_links.repositories.UserRepository;
import com.gabrielrodrigues.encurtador_de_links.security.dtos.AuthenticatedResponseDto;
import com.gabrielrodrigues.encurtador_de_links.security.dtos.LoginDto;
import com.gabrielrodrigues.encurtador_de_links.security.dtos.RegisterDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            JwtTokenService jwtTokenService,
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticatedResponseDto login(LoginDto loginCredentials) throws Exception {
        try {
            return this.findAndAuthenticateUser(loginCredentials);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public AuthenticatedResponseDto register(RegisterDto registerCredentials) throws Exception {
        boolean usernameVerify = this.userRepository.findByUsername(registerCredentials.username()).isPresent();
        if (usernameVerify)
            throw new EntityAlreadyExist("O username: " + registerCredentials.username() + " já foi cadastrado no banco de dados!");

        boolean emailVerify = this.userRepository.findByEmail(registerCredentials.email()).isPresent();
        if (emailVerify)
            throw new EntityAlreadyExist("O email: " + registerCredentials.email() + " já foi cadastrado no banco de dados!");

        User user = new User(
                registerCredentials.username(),
                registerCredentials.email(),
                passwordEncoder.encode(registerCredentials.password()),
                RoleEnum.USER
        );

        try {
            this.userRepository.save(user);
            return this.findAndAuthenticateUser(new LoginDto(user.getEmail(), registerCredentials.password()));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private AuthenticatedResponseDto findAndAuthenticateUser(LoginDto loginCredentials) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginCredentials.email(), loginCredentials.password()));

        User user = userRepository.findByEmail(loginCredentials.email()).orElseThrow(
                () -> new EntityNotFoundException("O usuário com o email: " + loginCredentials.email() + " não foi encontrado!"));
        String token = jwtTokenService.generateToken(auth, user.getId());

        return new AuthenticatedResponseDto(
                user.getId(),
                user.getUsername(),
                token,
                user.getRole().toString()
        );
    }
}
