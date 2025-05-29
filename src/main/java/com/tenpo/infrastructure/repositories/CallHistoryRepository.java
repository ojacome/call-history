package com.tenpo.infrastructure.repositories;

import com.tenpo.infrastructure.entities.CallHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallHistoryRepository extends JpaRepository<CallHistory, Long> {
}