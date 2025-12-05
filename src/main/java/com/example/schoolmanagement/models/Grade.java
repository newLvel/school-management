package com.example.schoolmanagement.models;

public class Grade {
    private int id;
    private int examId;
    private int studentId;
    private int subjectId;
    private double score;
    private String remarks;
    
    // Helpers
    private String studentName;
    private String subjectName;

    public Grade(int id, int examId, int studentId, int subjectId, double score, String remarks) {
        this.id = id;
        this.examId = examId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.score = score;
        this.remarks = remarks;
    }

    // Constructor for UI Table (includes student name)
    public Grade(int studentId, String studentName, double score, String remarks) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.score = score;
        this.remarks = remarks;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
}
