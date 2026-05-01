package com.crochet.recipes.config;

import com.crochet.recipes.model.User;
import com.crochet.recipes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializationConfig {

    @Bean
    CommandLineRunner initializeUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            try {
            // Criar usuário ADMIN
            if (userRepository.findByEmail("admin@crochet.com").isEmpty()) {
                User admin = User.builder()
                        .email("admin@crochet.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role("ADMIN")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                userRepository.save(admin);
                log.info("✅ Usuário ADMIN criado: admin@crochet.com");
            }

            // Criar usuário DEMO
            if (userRepository.findByEmail("demo@crochet.com").isEmpty()) {
                User demo = User.builder()
                        .email("demo@crochet.com")
                        .password(passwordEncoder.encode("demo123"))
                        .role("DEMO")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                userRepository.save(demo);
                log.info("✅ Usuário DEMO criado: demo@crochet.com");
            }

            log.info("✅ Inicialização de usuários concluída");
            } catch (Exception ex) {
                log.warn("⚠️ MongoDB não disponível durante inicialização. Usuários serão criados na próxima execução com banco disponível: {}", ex.getMessage());
            }
        };
    }
}

