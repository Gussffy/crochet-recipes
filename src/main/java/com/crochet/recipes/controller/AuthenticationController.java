package com.crochet.recipes.controller;

import com.crochet.recipes.dto.request.LoginRequestDTO;
import com.crochet.recipes.dto.response.ApiResponseDTO;
import com.crochet.recipes.dto.response.LoginResponseDTO;
import com.crochet.recipes.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de login e autenticação JWT")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Realiza login e retorna JWT token")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO requestDTO) {

        log.info("Requisição de login para: {}", requestDTO.email());
        LoginResponseDTO response = authenticationService.login(requestDTO);

        return ResponseEntity
                .ok(ApiResponseDTO.success(response, "Login realizado com sucesso!"));
    }

    @GetMapping("/demo-credentials")
    @Operation(summary = "Credenciais DEMO", description = "Retorna as credenciais do usuário DEMO (usado para testes)")
    public ResponseEntity<ApiResponseDTO<Object>> getDemoCredentials() {
        return ResponseEntity
                .ok(ApiResponseDTO.success(
                        new Object() {
                            public String email = "demo@crochet.com";
                            public String password = "demo123";
                            public String role = "DEMO";
                        },
                        "Credenciais do usuário DEMO"
                ));
    }
}

