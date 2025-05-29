package com.tenpo.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "call_history")
@Setter
@Getter
public class CallHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String endpoint;
    
    @Column(columnDefinition = "TEXT")
    private String parameters;
    
    @Column(columnDefinition = "TEXT")
    private String response;
    
    @Column
    private String error;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
}