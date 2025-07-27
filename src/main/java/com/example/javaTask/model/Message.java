package com.example.javaTask.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    private String content;
    private LocalDateTime timestamp;
}