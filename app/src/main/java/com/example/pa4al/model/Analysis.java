package com.example.pa4al.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Analysis {
    public String id;
    public String name;
    public String type;
    public String status;
    public String document_id;
    public String document_name;
    public String owner;
    public String runner;
    public String step_number;
    public String total_steps;
    public String steps_name;
    public String start_time;
    public String lasting_time;
    public String end_time;
}
