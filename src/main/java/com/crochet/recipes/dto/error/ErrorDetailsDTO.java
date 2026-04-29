package com.crochet.recipes.dto.error;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorDetailsDTO(
    String code,
    String message,
    Integer httpStatus,
    LocalDateTime timestamp,
    String traceId,
    String path,
    String hint
) {}

