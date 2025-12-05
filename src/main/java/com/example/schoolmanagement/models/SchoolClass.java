package com.example.schoolmanagement.models;

public class SchoolClass {
    private int id;
    private String className;
    private String section;
    private int teacherId;
    private String teacherName; // Helper for display purposes

    public SchoolClass(int id, String className, String section, int teacherId, String teacherName) {
        this.id = id;
        this.className = className;
        this.section = section;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    // Constructor for creating new class (without ID)
    public SchoolClass(String className, String section, int teacherId) {
        this.className = className;
        this.section = section;
        this.teacherId = teacherId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    
    public String getFullClassName() { return className + " - " + section; }
}
