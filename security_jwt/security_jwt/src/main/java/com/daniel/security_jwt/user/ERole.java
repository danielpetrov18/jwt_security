package com.daniel.security_jwt.user;

public enum ERole {

    USER("User"),
    ADMIN("Admin");

    private final String name;

    ERole(final String newName) {
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }

}