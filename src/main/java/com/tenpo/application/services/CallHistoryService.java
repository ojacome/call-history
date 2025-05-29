package com.tenpo.application.services;

import com.tenpo.infrastructure.entities.CallHistory;
import com.tenpo.infrastructure.repositories.CallHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CallHistoryService {

    private final CallHistoryRepository repository;

    public CallHistoryService(CallHistoryRepository repository) {
        this.repository = repository;
    }

    @Async
    public void saveCallHistory(String endpoint, String parameters, Object response, String error) {
        try {
            CallHistory history = new CallHistory();
            history.setEndpoint(endpoint);
            history.setParameters(parameters);
            history.setResponse(response != null ? response.toString() : null);
            history.setError(error);
            history.setTimestamp(LocalDateTime.now());
            
            repository.save(history);
            log.debug("Historial de llamada guardado para endpoint: {}", endpoint);
        } catch (Exception e) {
            log.error("Error al guardar historial de llamada", e);
        }
    }

    public Page<CallHistory> getCallHistory(Pageable pageable) {
        return repository.findAll(pageable);
    }
}