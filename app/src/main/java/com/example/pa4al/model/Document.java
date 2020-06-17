package com.example.pa4al.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Document {
    final String id;
    final String name;
    final String hash;
    final String genre;
    final String content;
    final String owner;
}