package com.tenpo.application.services;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExternalServiceTest {

    @InjectMocks
    private ExternalService externalService;

    @RepeatedTest(100)
    void getPercentage_shouldAlwaysReturnValueInRange() {
        int result = externalService.getPercentage();
        assertTrue(result >= 1 && result <= 100,
                "El porcentaje debe estar entre 1 y 100, pero fue: " + result);
    }
}