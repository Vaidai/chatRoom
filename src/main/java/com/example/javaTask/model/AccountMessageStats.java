package com.example.javaTask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMessageStats {
    private String name;
    private Long messageCount;
    private Timestamp firstMessageTimestamp;
    private Timestamp lastMessageTimestamp;
    private Integer avgMessageLength;
    private String lastMessageText;
}
