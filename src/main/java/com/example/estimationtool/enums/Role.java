package com.example.estimationtool.enums;

public enum Role {
    ADMIN("Administrator"),
    PROJECT_MANAGER("Project manager"),
    DEVELOPER("Developer");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


