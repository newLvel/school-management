package com.example.schoolmanagement.models;

import java.time.LocalDate;

public class Exam {
    private int id;
    private String examName;
    private LocalDate examDate;
    private int classId;
    private String className; // Helper

    public Exam(int id, String examName, LocalDate examDate, int classId, String className) {
        this.id = id;
        this.examName = examName;
        this.examDate = examDate;
        this.classId = classId;
        this.className = className;
    }

    public Exam(String examName, LocalDate examDate, int classId) {
        this.examName = examName;
        this.examDate = examDate;
        this.classId = classId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getExamName() { return examName; }
    public void setExamName(String examName) { this.examName = examName; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}
