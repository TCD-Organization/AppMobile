package com.example.pa4al.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Document implements Serializable {
    public String id;
    public String name;
    public String hash;
    public String genre;
    public String content;
    public String owner;
}