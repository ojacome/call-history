package com.tenpo.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.tenpo.application.services.CacheService;
import com.tenpo.application.services.ExternalService;
import com.tenpo.application.services.OperationsService;
import com.tenpo.domain.ports.out.CacheServicePort;
import com.tenpo.infrastructure.repositories.CallHistoryRepository;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

import java.util.concurrent.TimeUnit;

import static com.tenpo.application.shared.CacheConstants.CACHE_APP;

@Configuration
public class ApplicationConfig {

    @Value("${cache.percentage.expire-minutes}")
    private int expireAfterWriteMinutes;

    @Bean
    public OperationsService taskService(CacheServicePort cacheServicePort) {
        return new OperationsService(cacheServicePort);
    }

    @Bean
    public ExternalService externalService() {
        return new ExternalService();
    }

    @Bean
    public CacheService cacheService(ExternalService externalService, CacheManager cacheManager) {
        return new CacheService(externalService, cacheManager);
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(CACHE_APP);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(expireAfterWriteMinutes, TimeUnit.MINUTES));
        return cacheManager;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mi API de Historial de Llamadas (Tenpo)")
                        .version("1.0.0")
                        .description("Esta API permite consultar y guardar el historial de llamadas realizadas en el sistema."));
    }

}
