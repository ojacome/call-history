package com.tenpo.application.services;

import com.tenpo.domain.ports.out.CacheServicePort;
import com.tenpo.domain.ports.out.ExternalServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.tenpo.application.shared.CacheConstants.CACHE_APP;
import static com.tenpo.application.shared.CacheConstants.KEY_PERCENTAGE;

@Service
public class CacheService implements CacheServicePort {
    private final ExternalServicePort externalService;
    private final CacheManager cacheManager;

    public CacheService(ExternalServicePort externalService, CacheManager cacheManager) {
        this.externalService = externalService;
        this.cacheManager = cacheManager;
    }

    @Override
    public Integer getPercentage() {
        try {
            Integer percentage = externalService.getPercentage();
            cacheManager.getCache(CACHE_APP).put(KEY_PERCENTAGE, percentage);
            return percentage;
        } catch (Exception ex) {
            Cache cache = cacheManager.getCache(CACHE_APP);
            Integer cachedValue = cache.get(KEY_PERCENTAGE, Integer.class);
            if (cachedValue != null) {
                return cachedValue;
            }
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se pudo obtener el porcentaje, ni existe uno en caché."
            );
        }
    }
}
