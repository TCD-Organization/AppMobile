package com.example.pa4al.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Analysis implements Serializable {
    private String id;
    private String name;
    private String status;
    private String document_id;
    private String document_name;
    private String owner;
    private String runner;
    private int step_number;
    private int total_steps;
    private String step_name;
    private Date start_time;
    private Long lasting_time;
    private Date end_time;
    private String result;
}
