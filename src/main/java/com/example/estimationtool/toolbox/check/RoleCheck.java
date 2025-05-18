package com.example.estimationtool.toolbox.check;

import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.toolbox.controllerAdvice.UserFriendlyException;

public class RoleCheck {

    public static void ensureAdmin(Role role) {

        if (role != Role.ADMIN) {
            throw new UserFriendlyException("Kun Admin har adgang til denne funktion", "/users/profile");
        }
    }

    public static void ensureAdminOrProjectManager(Role role) {
        if (role != Role.ADMIN && role != Role.PROJECT_MANAGER) {
            throw new UserFriendlyException("Kun Admin eller Projektleder har adgang til denne funktion", "/users/profile");
        }
    }


}
