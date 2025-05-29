package com.tenpo.application.services;

import com.tenpo.domain.model.OperationResponse;
import com.tenpo.domain.ports.out.CacheServicePort;

public class OperationsService {

    private final CacheServicePort cacheServicePort;
    public OperationsService(CacheServicePort externalServicePort) {
        this.cacheServicePort = externalServicePort;
    }

    public OperationResponse calculateDynamicPercentage(double num1, double num2) {
        Integer percentage = this.cacheServicePort.getPercentage();

        double suma = num1 + num2;
        double total = suma + (suma * percentage / 100);

        return new OperationResponse(percentage, total);
    }

}
