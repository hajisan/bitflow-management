package com.example.estimationtool.enums;

public enum Status {
    ACTIVE("Aktiv"),
    INACTIVE("Inaktiv"),
    DONE("Done");


    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
