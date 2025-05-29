package com.tenpo.infrastructure.aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.application.services.CallHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class CallHistoryAspect {

    private final CallHistoryService callHistoryService;
    private final ObjectMapper objectMapper;

    public CallHistoryAspect(CallHistoryService callHistoryService, ObjectMapper objectMapper) {
        this.callHistoryService = callHistoryService;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object logCallHistory(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        
        String requestURI = request.getRequestURI();
        String parameters = request.getQueryString();

        // Excluir rutas específicas /v3/api-docs
        if (
                request.getRequestURI().startsWith("/api/history") ||
                request.getRequestURI().startsWith("/v3/api-docs")
        ) {
            return joinPoint.proceed();
        }

        try {
            Object result = joinPoint.proceed();
            String jsonResponse = serializeResponse(result);
            callHistoryService.saveCallHistory(requestURI, parameters, jsonResponse, null);
            return result;
        } catch (Exception e) {
            callHistoryService.saveCallHistory(requestURI, parameters, null, e.getMessage());
            throw e;
        }
    }
    private String serializeResponse(Object result) {
        if (result == null) {
            return null;
        }

        try {
            // Si es una ResponseEntity, serializamos solo el body
            if (result instanceof ResponseEntity) {
                Object responseBody = ((ResponseEntity<?>) result).getBody();
                return responseBody != null ? objectMapper.writeValueAsString(responseBody) : null;
            }
            // Para otros tipos de respuesta
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            log.warn("Error serializing response to JSON", e);
            // Fallback: usar toString() si no se puede serializar a JSON
            return result.toString();
        }
    }
}