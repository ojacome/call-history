package com.tenpo.infrastructure.controllers;

import com.tenpo.application.services.OperationsService;
import com.tenpo.domain.model.OperationResponse;
import com.tenpo.infrastructure.entities.CallHistory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operations")
@Validated
public class OperationsController {

    private final OperationsService operationsService;

    public OperationsController(OperationsService operationsService) {

        this.operationsService = operationsService;
    }


    @GetMapping("/dynamic-percentage")
    public ResponseEntity<OperationResponse> calcularTotal(
            @RequestParam @NotNull @Positive(message = "num1 debe ser positivo") Double num1,
            @RequestParam @NotNull @Positive(message = "num2 debe ser positivo") Double num2) {
        OperationResponse result = operationsService.calculateDynamicPercentage(num1, num2);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
