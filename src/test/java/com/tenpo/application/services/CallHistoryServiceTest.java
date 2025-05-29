package com.tenpo.application.services;

import com.tenpo.infrastructure.entities.CallHistory;
import com.tenpo.infrastructure.repositories.CallHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.mockito.ArgumentMatchers.any;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CallHistoryServiceTest {

    private static final String ENDPOINT_MOCK = "/api/task";
    private static final String PARAMETERS_MOCK = "mock=2";
    private static final Object RESPONSE_MOCK = "fakeBody";
    private static final String ERROR_MOCK = "fakeError";
    private static final Pageable PAGE_REQUEST = PageRequest.of(0, 10);
    private static final Page<CallHistory> PAGE_RESPONSE = new PageImpl<>(List.of(new CallHistory()));
    private static final RuntimeException DB_ERROR = new RuntimeException("DB Error");
    @Mock
    private CallHistoryRepository callHistoryRepository;

    @InjectMocks
    private CallHistoryService callHistoryService;

    @Test
    void saveCallHistory_success() {
        callHistoryService.saveCallHistory(ENDPOINT_MOCK, PARAMETERS_MOCK, RESPONSE_MOCK, ERROR_MOCK);

        verify(callHistoryRepository, timeout(1000).times(1)).save(argThat(history ->
                history.getEndpoint().equals(ENDPOINT_MOCK) &&
                        history.getParameters().equals(PARAMETERS_MOCK) &&
                        history.getResponse().equals(RESPONSE_MOCK) &&
                        history.getError() != null &&
                        history.getTimestamp() != null
        ));
    }

    @Test
    void saveCallHistory_whenSaveFails_shouldHandleExceptionGracefully() {
        doThrow(DB_ERROR)
                .when(callHistoryRepository)
                .save(any(CallHistory.class));

        assertDoesNotThrow(() ->
                callHistoryService.saveCallHistory(ENDPOINT_MOCK, PARAMETERS_MOCK, RESPONSE_MOCK, ERROR_MOCK)
        );

        verify(callHistoryRepository, timeout(1000).times(1)).save(any(CallHistory.class));
    }

    @Test
    void getCallHistory_shouldReturnPageFromRepository() {
        when(callHistoryRepository.findAll(PAGE_REQUEST)).thenReturn(PAGE_RESPONSE);

        Page<CallHistory> result = callHistoryService.getCallHistory(PAGE_REQUEST);

        assertEquals(PAGE_RESPONSE, result);
        verify(callHistoryRepository).findAll(PAGE_REQUEST);
    }

    @Test
    void getCallHistory_whenRepositoryFails_shouldThrowException() {
        when(callHistoryRepository.findAll(PAGE_REQUEST))
                .thenThrow(DB_ERROR);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            callHistoryService.getCallHistory(PAGE_REQUEST);
        });

        assertEquals(DB_ERROR.getMessage(), exception.getMessage());
        verify(callHistoryRepository).findAll(PAGE_REQUEST);
    }

}