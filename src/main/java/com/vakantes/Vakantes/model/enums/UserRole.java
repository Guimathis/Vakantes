package com.vakantes.Vakantes.model.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private final String role;

    private UserRole(String role) {
        this.role = role;
    }

}
