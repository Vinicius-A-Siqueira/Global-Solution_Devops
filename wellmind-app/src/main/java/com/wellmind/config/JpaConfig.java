package com.wellmind.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

/**
 * Configuração JPA e Hibernate
 *
 * Funcionalidades:
 * - JPA Repositories habilitados
 * - Auditoria automática (createdBy, modifiedBy)
 * - Gerenciamento de transações
 * - Naming strategy para Oracle
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.wellmind.repository")
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableTransactionManagement
public class JpaConfig {

    /**
     * Provider de auditoria para rastrear quem criou/modificou registros
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }

    /**
     * Implementação de AuditorAware usando Spring Security
     */
    static class SpringSecurityAuditorAware implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null ||
                    !authentication.isAuthenticated() ||
                    authentication.getPrincipal().equals("anonymousUser")) {
                return Optional.of("SYSTEM");
            }

            return Optional.of(authentication.getName());
        }
    }
}
