package com.tenpo.infrastructure.controllers;

import com.tenpo.application.services.CallHistoryService;
import com.tenpo.infrastructure.entities.CallHistory;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/history")
@Validated
public class HistoryController {

    private final CallHistoryService service;

    public HistoryController(CallHistoryService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<Page<CallHistory>> getCallHistory(
            @Parameter(description = "Paginación (opcional)")
            @ParameterObject
            @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.getCallHistory(pageable));
    }


}
