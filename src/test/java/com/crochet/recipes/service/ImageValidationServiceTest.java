package com.crochet.recipes.service;

import com.crochet.recipes.exception.InvalidImageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("ImageValidationService - Testes Unitários")
class ImageValidationServiceTest {

    private ImageValidationService imageValidationService;

    @BeforeEach
    void setUp() {
        imageValidationService = new ImageValidationService();
    }

    @Nested
    @DisplayName("Validações de Base64 Válido")
    class ValidImageTests {

        @Test
        @DisplayName("Deve aceitar imagem PNG válida")
        void shouldAcceptValidPngImage() {
            // Magic bytes PNG: 89 50 4E 47
            byte[] pngBytes = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x00, 0x00, 0x00, 0x00};
            String base64 = Base64.getEncoder().encodeToString(pngBytes);

            assertThatNoException()
                .isThrownBy(() -> imageValidationService.validateBase64Image(base64, "image/png"));
        }

        @Test
        @DisplayName("Deve aceitar imagem JPEG válida")
        void shouldAcceptValidJpegImage() {
            // Magic bytes JPEG: FF D8
            byte[] jpegBytes = {(byte) 0xFF, (byte) 0xD8, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            String base64 = Base64.getEncoder().encodeToString(jpegBytes);

            assertThatNoException()
                .isThrownBy(() -> imageValidationService.validateBase64Image(base64, "image/jpeg"));
        }

        @Test
        @DisplayName("Deve aceitar imagem GIF válida")
        void shouldAcceptValidGifImage() {
            // Magic bytes GIF: 47 49 46
            byte[] gifBytes = {0x47, 0x49, 0x46, 0x00, 0x00, 0x00, 0x00, 0x00};
            String base64 = Base64.getEncoder().encodeToString(gifBytes);

            assertThatNoException()
                .isThrownBy(() -> imageValidationService.validateBase64Image(base64, "image/gif"));
        }

        @Test
        @DisplayName("Deve aceitar imagem WebP válida")
        void shouldAcceptValidWebPImage() {
            // Magic bytes WebP: 52 49 46 46 (RIFF)
            byte[] webpBytes = {0x52, 0x49, 0x46, 0x46, 0x00, 0x00, 0x00, 0x00};
            String base64 = Base64.getEncoder().encodeToString(webpBytes);

            assertThatNoException()
                .isThrownBy(() -> imageValidationService.validateBase64Image(base64, "image/webp"));
        }

        @Test
        @DisplayName("Deve aceitar imagem nula (opcional)")
        void shouldAcceptNullImage() {
            assertThatNoException()
                .isThrownBy(() -> imageValidationService.validateBase64Image(null, "image/png"));
        }

        @Test
        @DisplayName("Deve aceitar imagem vazia (opcional)")
        void shouldAcceptEmptyImage() {
            assertThatNoException()
                .isThrownBy(() -> imageValidationService.validateBase64Image("", "image/png"));
        }
    }

    @Nested
    @DisplayName("Validações de Tipo MIME")
    class ContentTypeValidationTests {

        @Test
        @DisplayName("Deve rejeitar tipo MIME não suportado")
        void shouldRejectUnsupportedContentType() {
            byte[] pngBytes = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x00, 0x00, 0x00, 0x00};
            String base64 = Base64.getEncoder().encodeToString(pngBytes);

            assertThatThrownBy(() -> imageValidationService.validateBase64Image(base64, "image/bmp"))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("Tipo de imagem não suportado");
        }

        @Test
        @DisplayName("Deve rejeitar SVG (não suportado)")
        void shouldRejectSvg() {
            String svgBase64 = Base64.getEncoder().encodeToString("<svg></svg>".getBytes());

            assertThatThrownBy(() -> imageValidationService.validateBase64Image(svgBase64, "image/svg+xml"))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("Tipo de imagem não suportado");
        }

        @Test
        @DisplayName("Deve rejeitar type text/plain")
        void shouldRejectTextPlain() {
            String textBase64 = Base64.getEncoder().encodeToString("texto".getBytes());

            assertThatThrownBy(() -> imageValidationService.validateBase64Image(textBase64, "text/plain"))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("Tipo de imagem não suportado");
        }
    }

    @Nested
    @DisplayName("Validações de Tamanho")
    class FileSizeValidationTests {

        @Test
        @DisplayName("Deve rejeitar imagem maior que 5MB")
        void shouldRejectImageLargerThan5MB() {
            // Criar array de 6MB (5242880 bytes + 1MB)
            byte[] largeImage = new byte[6 * 1024 * 1024];
            largeImage[0] = (byte) 0x89;
            largeImage[1] = 0x50;
            largeImage[2] = 0x4E;
            largeImage[3] = 0x47;

            String base64 = Base64.getEncoder().encodeToString(largeImage);

            assertThatThrownBy(() -> imageValidationService.validateBase64Image(base64, "image/png"))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("Imagem muito grande");
        }

        @Test
        @DisplayName("Deve aceitar imagem exatamente em 5MB")
        void shouldAcceptImageAt5MBLimit() {
            // Criar array de exatamente 5MB com assinatura PNG válida
            byte[] image5MB = new byte[5 * 1024 * 1024];
            image5MB[0] = (byte) 0x89;
            image5MB[1] = 0x50;
            image5MB[2] = 0x4E;
            image5MB[3] = 0x47;

            String base64 = Base64.getEncoder().encodeToString(image5MB);

            assertThatNoException()
                .isThrownBy(() -> imageValidationService.validateBase64Image(base64, "image/png"));
        }
    }

    @Nested
    @DisplayName("Validações de Assinatura (Magic Bytes)")
    class SignatureValidationTests {

        @Test
        @DisplayName("Deve rejeitar PNG com assinatura inválida")
        void shouldRejectInvalidPngSignature() {
            // Começar com bytes incorretos para PNG
            byte[] invalidPng = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            String base64 = Base64.getEncoder().encodeToString(invalidPng);

            assertThatThrownBy(() -> imageValidationService.validateBase64Image(base64, "image/png"))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("Assinatura PNG inválida");
        }

        @Test
        @DisplayName("Deve rejeitar JPEG com assinatura inválida")
        void shouldRejectInvalidJpegSignature() {
            // PNG bytes mas informar como JPEG
            byte[] pngBytes = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x00, 0x00, 0x00, 0x00};
            String base64 = Base64.getEncoder().encodeToString(pngBytes);

            assertThatThrownBy(() -> imageValidationService.validateBase64Image(base64, "image/jpeg"))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("Assinatura JPEG inválida");
        }

        @Test
        @DisplayName("Deve rejeitar GIF com assinatura inválida")
        void shouldRejectInvalidGifSignature() {
            byte[] invalidGif = {(byte) 0xFF, (byte) 0xD8, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            String base64 = Base64.getEncoder().encodeToString(invalidGif);

            assertThatThrownBy(() -> imageValidationService.validateBase64Image(base64, "image/gif"))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("Assinatura GIF inválida");
        }

        @Test
        @DisplayName("Deve rejeitar WebP com assinatura inválida")
        void shouldRejectInvalidWebPSignature() {
            byte[] invalidWebp = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x00, 0x00, 0x00, 0x00};
            String base64 = Base64.getEncoder().encodeToString(invalidWebp);

            assertThatThrownBy(() -> imageValidationService.validateBase64Image(base64, "image/webp"))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("Assinatura WebP inválida");
        }
    }

    @Nested
    @DisplayName("Validações de Base64 Inválido")
    class InvalidBase64Tests {

        @Test
        @DisplayName("Deve rejeitar Base64 corrompido")
        void shouldRejectCorruptedBase64() {
            String invalidBase64 = "!!!INVALID_BASE64!!!";

            assertThatThrownBy(() -> imageValidationService.validateBase64Image(invalidBase64, "image/png"))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("Base64 inválido ou corrompido");
        }

        @Test
        @DisplayName("Deve rejeitar arquivo muito pequeno")
        void shouldRejectTooSmallFile() {
            byte[] tinyFile = {0x01, 0x02}; // Menos de 4 bytes
            String base64 = Base64.getEncoder().encodeToString(tinyFile);

            assertThatThrownBy(() -> imageValidationService.validateBase64Image(base64, "image/png"))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("muito pequeno");
        }
    }
}

