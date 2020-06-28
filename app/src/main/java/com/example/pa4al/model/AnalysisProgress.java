package com.example.pa4al.model;

import java.util.Date;

public class AnalysisProgress {
    private String id;
    private String name;
    private String type;
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

    public AnalysisProgress(String id, String name, String type, String status, String document_id, String document_name, String owner, String runner, int step_number, int total_steps, String step_name, Date start_time, Long lasting_time, Date end_time, String result) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.document_id = document_id;
        this.document_name = document_name;
        this.owner = owner;
        this.runner = runner;
        this.step_number = step_number;
        this.total_steps = total_steps;
        this.step_name = step_name;
        this.start_time = start_time;
        this.lasting_time = lasting_time;
        this.end_time = end_time;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRunner() {
        return runner;
    }

    public void setRunner(String runner) {
        this.runner = runner;
    }

    public int getStep_number() {
        return step_number;
    }

    public void setStep_number(int step_number) {
        this.step_number = step_number;
    }

    public int getTotal_steps() {
        return total_steps;
    }

    public void setTotal_steps(int total_steps) {
        this.total_steps = total_steps;
    }

    public String getStep_name() {
        return step_name;
    }

    public void setStep_name(String step_name) {
        this.step_name = step_name;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Long getLasting_time() {
        return lasting_time;
    }

    public void setLasting_time(Long lasting_time) {
        this.lasting_time = lasting_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
