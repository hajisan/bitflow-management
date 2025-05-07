package com.example.estimationtool.toolbox.dto;

import com.example.estimationtool.model.enums.Role;

public class UserViewDTO {

    private int userId;
    private String firstName, lastName, email;
    private Role role;

    public UserViewDTO(int userId, String firstName, String lastName, String email, Role role) {
        setUserId(userId);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setRole(role);
    }

    // Getter-metoder

    public int getUserId() {
        return userId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public Role getRole() {
        return role;
    }

    // Setter-metoder

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserViewDTO{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }




}
