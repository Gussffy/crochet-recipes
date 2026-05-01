package com.crochet.recipes.service;

import com.crochet.recipes.dto.request.LoginRequestDTO;
import com.crochet.recipes.dto.response.LoginResponseDTO;
import com.crochet.recipes.exception.JwtAuthenticationException;
import com.crochet.recipes.model.User;
import com.crochet.recipes.repository.UserRepository;
import com.crochet.recipes.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        log.info("Tentativa de login para: {}", requestDTO.email());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDTO.email(),
                            requestDTO.password()
                    )
            );

            String token = jwtProvider.generateToken(authentication);
            User user = userRepository.findByEmail(requestDTO.email())
                    .orElseThrow(() -> new JwtAuthenticationException("Usuário não encontrado"));

            log.info("Login bem-sucedido para: {}", requestDTO.email());

            return new LoginResponseDTO(
                    token,
                    user.getEmail(),
                    user.getRole(),
                    "Bearer"
            );
        } catch (AuthenticationException ex) {
            log.error("Falha na autenticação: {}", ex.getMessage());
            throw new JwtAuthenticationException("Email ou senha inválidos");
        }
    }

    public User createAdminUser(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Tentativa de criar admin com email duplicado: {}", email);
            return userRepository.findByEmail(email).get();
        }

        User admin = User.builder()
                .email(email)
                .password(password) // Deve estar codificado
                .role("ADMIN")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        log.info("Admin criado: {}", email);
        return userRepository.save(admin);
    }

    public User createDemoUser() {
        String demoEmail = "demo@crochet.com";

        if (userRepository.findByEmail(demoEmail).isPresent()) {
            return userRepository.findByEmail(demoEmail).get();
        }

        User demo = User.builder()
                .email(demoEmail)
                .password("demo123") // Deve estar codificado
                .role("DEMO")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        log.info("Usuário DEMO criado: {}", demoEmail);
        return userRepository.save(demo);
    }
}

