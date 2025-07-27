package com.example.javaTask.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountRole {
    ACCOUNT("Account"),
    ADMIN("Admin");

    private final String description;
}
