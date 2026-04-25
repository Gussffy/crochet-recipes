package com.crochet.recipes.service;

import com.crochet.recipes.exception.InvalidImageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class ImageValidationService {

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final List<String> ALLOWED_TYPES =
        List.of("image/jpeg", "image/png", "image/webp", "image/gif");

    public void validateBase64Image(String base64, String contentType) {
        if (base64 == null || base64.isBlank()) {
            return;
        }

        log.debug("Validando imagem Base64 com contentType: {}", contentType);

        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new InvalidImageException(
                "Tipo de imagem não suportado: " + contentType +
                ". Tipos permitidos: " + String.join(", ", ALLOWED_TYPES)
            );
        }

        long decodedSize = (long) (base64.length() * 0.75);
        if (decodedSize > MAX_IMAGE_SIZE) {
            throw new InvalidImageException(
                "Imagem muito grande: " + formatBytes(decodedSize) +
                ". Máximo permitido: " + formatBytes(MAX_IMAGE_SIZE)
            );
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            verifyImageSignature(decodedBytes, contentType);
            log.debug("Imagem validada com sucesso. Tamanho: {} bytes", decodedBytes.length);
        } catch (IllegalArgumentException ex) {
            throw new InvalidImageException("Base64 inválido ou corrompido");
        }
    }

    private void verifyImageSignature(byte[] bytes, String contentType) {
        if (bytes.length < 4) {
            throw new InvalidImageException("Arquivo de imagem muito pequeno (< 4 bytes)");
        }

        switch (contentType) {
            case "image/jpeg":
                if (bytes[0] != (byte) 0xFF || bytes[1] != (byte) 0xD8) {
                    throw new InvalidImageException("Assinatura JPEG inválida. Arquivo pode estar corrompido");
                }
                break;

            case "image/png":
                if (bytes[0] != (byte) 0x89 || bytes[1] != 0x50 ||
                    bytes[2] != 0x4E || bytes[3] != 0x47) {
                    throw new InvalidImageException("Assinatura PNG inválida. Arquivo pode estar corrompido");
                }
                break;

            case "image/gif":
                if (bytes[0] != 0x47 || bytes[1] != 0x49 || bytes[2] != 0x46) {
                    throw new InvalidImageException("Assinatura GIF inválida. Arquivo pode estar corrompido");
                }
                break;

            case "image/webp":
                // WebP sempre começa com "RIFF" e contém "WEBP"
                if (bytes[0] != 0x52 || bytes[1] != 0x49 ||
                    bytes[2] != 0x46 || bytes[3] != 0x46) {
                    throw new InvalidImageException("Assinatura WebP inválida. Arquivo pode estar corrompido");
                }
                break;
        }
    }

    private String formatBytes(long bytes) {
        if (bytes <= 0) return "0 B";
        final String[] units = new String[] { "B", "KB", "MB", "GB" };
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return String.format("%.1f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}

