package com.example.pa4al;

public class DocumentDTO {
    final String name;
    final String genre;
    final String contentType;
    final String content;


    public DocumentDTO(String name, String genre, String contentType, String content) {
        this.name = name;
        this.genre = genre;
        this.contentType = contentType;
        this.content = content;

    }
}
