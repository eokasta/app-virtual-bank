package me.eokasta.appvirtualbank.user;

import lombok.Getter;

public enum UserRole {

    ADMIN("ADMIN"),
    USER("user");

    @Getter
    private String role;

    UserRole(String role) {
        this.role = role;
    }

}
