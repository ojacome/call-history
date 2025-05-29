package com.tenpo.application.services;

import com.tenpo.domain.model.OperationResponse;
import com.tenpo.domain.ports.out.ExternalServicePort;
import com.tenpo.infrastructure.repositories.CallHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static com.tenpo.application.shared.CacheConstants.CACHE_APP;
import static com.tenpo.application.shared.CacheConstants.KEY_PERCENTAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheServiceTest {

    private static final Integer PERCENTAGE = 10;
    private static final RuntimeException ANY_ERROR = new RuntimeException("Fooerror");
    @Mock
    private CacheManager cacheManager;
    @Mock
    private ExternalServicePort externalServicePort;
    @Mock
    private Cache cache;

    @InjectMocks
    private CacheService cacheService;

    @Test
    void getPercentage_success_WhenExternalServiceResponse() {
        when(externalServicePort.getPercentage()).thenReturn(PERCENTAGE);
        when(cacheManager.getCache(CACHE_APP)).thenReturn(cache);

        Integer result = cacheService.getPercentage();

        verify(externalServicePort).getPercentage();
        verify(cache).put(KEY_PERCENTAGE, PERCENTAGE);
        assertEquals(PERCENTAGE, result);
    }

    @Test
    void getPercentage_shouldUseCachedValue_WhenExternalServiceFails() {
        when(externalServicePort.getPercentage()).thenThrow(ANY_ERROR);
        when(cacheManager.getCache(CACHE_APP)).thenReturn(cache);
        when(cache.get(KEY_PERCENTAGE, Integer.class)).thenReturn(PERCENTAGE);

        Integer result = cacheService.getPercentage();

        verify(externalServicePort).getPercentage();
        verify(cache).get(KEY_PERCENTAGE, Integer.class);
        assertEquals(PERCENTAGE, result);
    }

    @Test
    void getPercentage_shouldThrowException_WhenBothServicesFail() {
        when(externalServicePort.getPercentage()).thenThrow(ANY_ERROR);
        when(cacheManager.getCache(CACHE_APP)).thenReturn(cache);
        when(cache.get(KEY_PERCENTAGE, Integer.class)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cacheService.getPercentage();
        });

        verify(externalServicePort).getPercentage();
        verify(cache).get(KEY_PERCENTAGE, Integer.class);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}