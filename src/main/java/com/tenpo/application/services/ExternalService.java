package com.tenpo.application.services;

import com.tenpo.domain.ports.out.ExternalServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Slf4j
public class ExternalService implements ExternalServicePort {

    @Value("${external.service.percentage.url}")
    private String externalServiceUrl;

    private final RestTemplate restTemplate;

    public ExternalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Integer getPercentage() {
        String response = restTemplate.getForObject(externalServiceUrl, String.class);
        Integer number = Integer.valueOf(response.trim());
        log.info("Número del servicio externo: {}", number);

        if (number == null || number < 1 || number > 100) {
            throw new IllegalArgumentException("El porcentaje recibido está fuera del rango permitido (1-100)");
        }

        return number;
    }
}
