package com.tenpo.application.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ExternalServiceTest {

    private static final String mockUrl = "http://mock-service.com/percentage";
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExternalService externalService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(this.externalService, "externalServiceUrl", mockUrl);
    }

    @Test
    void getPercentage_shouldReturnValidPercentage_whenServiceReturnsValidNumber() {
        when(restTemplate.getForObject(mockUrl, String.class)).thenReturn("42");

        Integer result = externalService.getPercentage();

        assertEquals(42, result);
        verify(restTemplate).getForObject(mockUrl, String.class);
    }

    @Test
    void getPercentage_shouldThrowException_whenServiceReturnsNull() {
        when(restTemplate.getForObject(mockUrl, String.class)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            externalService.getPercentage();
        });

        assertTrue(exception.getMessage().contains("null"));
    }


    @Test
    void getPercentage_shouldThrowException_whenNumberBelow1() {
        when(restTemplate.getForObject(mockUrl, String.class)).thenReturn("0");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            externalService.getPercentage();
        });

        assertEquals("El porcentaje recibido está fuera del rango permitido (1-100)", exception.getMessage());
    }

    @Test
    void getPercentage_shouldThrowException_whenNumberAbove100() {
        when(restTemplate.getForObject(mockUrl, String.class)).thenReturn("101");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            externalService.getPercentage();
        });

        assertEquals("El porcentaje recibido está fuera del rango permitido (1-100)", exception.getMessage());
    }
}