package com.crochet.recipes.dto.response;

public record LoginResponseDTO(
    String token,
    String email,
    String role,
    String type
) {}

