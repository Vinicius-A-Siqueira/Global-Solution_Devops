package com.wellmind.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Configuração de Internacionalização (i18n)
 *
 * Funcionalidades:
 * - Suporte a múltiplos idiomas (PT-BR, EN)
 * - Mudança de idioma via parâmetro ?lang=pt_BR
 * - Mensagens de validação localizadas
 * - Locale padrão: Português Brasil
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    /**
     * Define o Locale padrão e estratégia de resolução
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();

        // Locale padrão: Português Brasil
        localeResolver.setDefaultLocale(new Locale("pt", "BR"));

        return localeResolver;
    }

    /**
     * Configura o MessageSource para carregar mensagens i18n
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();

        // Localização dos arquivos de mensagem
        messageSource.setBasename("classpath:messages");

        // Encoding UTF-8 para suportar caracteres especiais
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());

        // Cache de 1 hora em produção
        messageSource.setCacheSeconds(3600);

        // Fallback para locale padrão se mensagem não encontrada
        messageSource.setFallbackToSystemLocale(false);

        return messageSource;
    }

    /**
     * Interceptor para permitir mudança de idioma via parâmetro
     * Exemplo: /api/v1/usuario?lang=en
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();

        // Nome do parâmetro para mudar idioma
        interceptor.setParamName("lang");

        return interceptor;
    }

    /**
     * Registra o interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
