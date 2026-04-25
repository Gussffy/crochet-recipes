package com.crochet.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetailsDTO {

    private String code;
    private String message;
    private Integer httpStatus;
    private LocalDateTime timestamp;
    private String traceId;
    private String path;
    private String hint;
}

