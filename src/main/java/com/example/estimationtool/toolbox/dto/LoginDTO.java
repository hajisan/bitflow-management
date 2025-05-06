package com.example.estimationtool.toolbox.dto;

public class LoginDTO {

    private String email, password;

    public LoginDTO(String email, String password) {
        setEmail(email);
        setPassword(password);
    }

    // Getter-metoder

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    // Setter-metoder

    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }




}
