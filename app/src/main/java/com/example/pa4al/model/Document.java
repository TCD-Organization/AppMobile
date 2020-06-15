package com.example.pa4al.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    public String id;
    public String name;
    public String hash;
    public String genre;
    public String content;
    public String owner;

}