package com.example.pa4al.model;

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


    public Analysis (String id, String name, String type, String status, String document_id, String document_name,String owner){
        this.id=id;
        this.name=name;
        this.type=type;
        this.status=status;
        this.document_id=document_id;
        this.document_name=document_name;
        this.owner=owner;
    }
    public Analysis (String id, String name, String type, String status, String document_id, String document_name,String owner,String runner, String step_number, String total_steps, String steps_name, String start_time, String lasting_time, String end_time){
        this.id=id;
        this.name=name;
        this.type=type;
        this.status=status;
        this.document_id=document_id;
        this.document_name=document_name;
        this.owner=owner;
        this.runner=runner;
        this.step_number=step_number;
        this.steps_name=steps_name;
        this.end_time=end_time;
        this.total_steps=total_steps;
        this.start_time=start_time;
        this.lasting_time=lasting_time;
    }

}
