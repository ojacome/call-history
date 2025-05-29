package com.tenpo.application.services;

import com.tenpo.domain.ports.out.ExternalServicePort;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class ExternalService implements ExternalServicePort {

    @Override
    public Integer getPercentage() {
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1;  // 0..99 + 1 => 1..100
        log.info("number external service: {}", randomNumber);
        return randomNumber;
    }
}
