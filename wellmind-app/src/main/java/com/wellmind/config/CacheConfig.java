package com.wellmind.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    // CacheManager para Redis — somente ativo no perfil "prod"
    @Bean
    @Profile("prod")
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        // sua configuração atual para Redis aqui...

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("usuarios", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("empresas", defaultConfig.entryTtl(Duration.ofHours(2)));
        cacheConfigurations.put("registros-bemestar", defaultConfig.entryTtl(Duration.ofMinutes(15)));
        cacheConfigurations.put("recomendacoes", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("categorias", defaultConfig.entryTtl(Duration.ofHours(4)));
        cacheConfigurations.put("profissionais", defaultConfig.entryTtl(Duration.ofHours(2)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }

    // CacheManager para cache simples — ativo em todos perfis exceto o "prod"
    @Bean
    @Profile({"!prod"})
    public CacheManager simpleCacheManager() {
        return new ConcurrentMapCacheManager(); // cache na memória local simples
    }

    // O ObjectMapper pode ficar comum para ambos perfis
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // mapper.activateDefaultTyping(
        //     LaissezFaireSubTypeValidator.instance,
        //     ObjectMapper.DefaultTyping.NON_FINAL,
        //     JsonTypeInfo.As.PROPERTY
        // );
        return mapper;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, org.springframework.cache.Cache cache, Object key) {
                System.err.println("Cache GET error: " + exception.getMessage());
                super.handleCacheGetError(exception, cache, key);
            }

            @Override
            public void handleCachePutError(RuntimeException exception, org.springframework.cache.Cache cache, Object key, Object value) {
                System.err.println("Cache PUT error: " + exception.getMessage());
                super.handleCachePutError(exception, cache, key, value);
            }
        };
    }
}
