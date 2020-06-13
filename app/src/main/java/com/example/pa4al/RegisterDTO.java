package com.example.pa4al.ui;

public class RegisterDTO {

    final String username;
    final String password;
    final String email;

    public RegisterDTO(String email , String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
