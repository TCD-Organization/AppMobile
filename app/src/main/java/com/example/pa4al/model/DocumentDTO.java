package com.example.pa4al.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentDTO {
    final String name;
    final String genre;
    final String content_type;
    final String content;
}
