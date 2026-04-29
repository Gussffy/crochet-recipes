package com.crochet.recipes.dto.error;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ValidationErrorDTO(
    String code,
    String message,
    Integer httpStatus,
    LocalDateTime timestamp,
    String traceId,
    String path,
    Map<String, String> fieldErrors
) {}

