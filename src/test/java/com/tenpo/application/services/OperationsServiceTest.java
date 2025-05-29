package com.tenpo.application.services;

import com.tenpo.domain.model.OperationResponse;
import com.tenpo.domain.ports.out.CacheServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationsServiceTest {

    private static final double NUM1 = 50;
    private static final double NUM2 = 50;
    private static final Integer PERCENTAGE = 10;
    private static final RuntimeException porcentajeError = new RuntimeException("Error al obtener porcentaje");
    @Mock
    private CacheServicePort cacheServicePort;

    @InjectMocks
    private OperationsService operationsService;
    
    @Test
    void calculateDynamicPercentage_success() {
        when(cacheServicePort.getPercentage()).thenReturn(PERCENTAGE);

        OperationResponse result = operationsService.calculateDynamicPercentage(NUM1, NUM2);

        verify(cacheServicePort).getPercentage();

        assertEquals(10, result.getPercentage());
        assertEquals(110, result.getTotal());
    }

    @Test
    void calculateDynamicPercentage_whenCacheFails_shouldThrowException() {
        when(cacheServicePort.getPercentage())
                .thenThrow(porcentajeError);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            operationsService.calculateDynamicPercentage(NUM1, NUM2);
        });

        verify(cacheServicePort).getPercentage();
        assertEquals(porcentajeError.getMessage(), exception.getMessage());
    }
}