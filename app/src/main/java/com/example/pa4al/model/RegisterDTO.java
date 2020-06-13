package com.example.pa4al.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDTO {
    final String username;
    final String email;
    final String password;
}
