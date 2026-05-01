package com.crochet.recipes.service;

import com.crochet.recipes.dto.request.LoginRequestDTO;
import com.crochet.recipes.dto.request.RegisterRequestDTO;
import com.crochet.recipes.dto.response.LoginResponseDTO;
import com.crochet.recipes.exception.JwtAuthenticationException;
import com.crochet.recipes.exception.UserAlreadyExistsException;
import com.crochet.recipes.model.User;
import com.crochet.recipes.repository.UserRepository;
import com.crochet.recipes.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
                    "USER",
                    "Bearer"
            );
        } catch (AuthenticationException ex) {
            log.error("Falha na autenticação: {}", ex.getMessage());
            throw new JwtAuthenticationException("Email ou senha inválidos");
        }
    }

    public LoginResponseDTO register(RegisterRequestDTO requestDTO) {
        log.info("Tentativa de registro para: {}", requestDTO.email());

        // Validar se senhas conferem
        if (!requestDTO.password().equals(requestDTO.passwordConfirm())) {
            throw new JwtAuthenticationException("As senhas não conferem");
        }

        // Validar se usuário já existe
        if (userRepository.findByEmail(requestDTO.email()).isPresent()) {
            log.warn("Tentativa de registrar email duplicado: {}", requestDTO.email());
            throw new UserAlreadyExistsException("Email já cadastrado: " + requestDTO.email());
        }

        // Criar novo usuário
        User newUser = User.builder()
                .email(requestDTO.email())
                .password(passwordEncoder.encode(requestDTO.password()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(newUser);
        log.info("Novo usuário registrado: {}", requestDTO.email());

        // Fazer login automático
        return login(new LoginRequestDTO(requestDTO.email(), requestDTO.password()));
    }
}

