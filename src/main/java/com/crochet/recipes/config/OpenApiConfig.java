package com.crochet.recipes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI crochetRecipesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🧶 API de Receitas de Crochê")
                        .description("""
                                Sistema para publicação e gerenciamento de receitas de crochê.
                                
                                Funcionalidades:
                                - Publicar receitas com nome, materiais e partes
                                - Cada parte possui título, instruções em texto e imagem (Base64)
                                - Imagem de capa para a receita
                                - Filtros por autor, dificuldade, tags e pesquisa por keyword
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Crochet Recipes API")
                                .email("gsf34062@gmail.com"))
                        .license(new License()
                                .name("MIT License")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Desenvolvimento")
                ));
    }
}
