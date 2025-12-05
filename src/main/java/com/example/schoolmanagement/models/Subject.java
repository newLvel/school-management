package com.example.schoolmanagement.models;

public class Subject {
    private int id;
    private String subjectName;
    private String code;

    public Subject(int id, String subjectName, String code) {
        this.id = id;
        this.subjectName = subjectName;
        this.code = code;
    }

    public Subject(String subjectName, String code) {
        this.subjectName = subjectName;
        this.code = code;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    @Override
    public String toString() { return subjectName + " (" + code + ")"; }
}
