package com.example.estimationtool.roleCheck;

import com.example.estimationtool.enums.Role;

public class RoleCheck {

    public static void ensureAdmin(Role role) {

        if (role != Role.ADMIN) {
            throw new SecurityException("Kun Admin har adgang til denne funktion");
        }
    }

    public static void ensureAdminOrProjectManager(Role role) {
        if (role != Role.ADMIN && role != Role.PROJECT_MANAGER) {
            throw new SecurityException("Kun Admin eller Project Manager har adgang til denne funktion");
        }
    }
}
