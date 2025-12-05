package com.example.schoolmanagement.models;

import java.time.LocalDate;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String address;
    private int parentId;
    private String parentName; // Helper for display
    private int classId;
    private String className; // Helper for display
    private int transportId;

    public Student() {
        // Default constructor
    }

    public Student(int id, String firstName, String lastName, LocalDate dob, String gender, String address, 
                   int parentId, String parentName, int classId, String className, int transportId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.parentId = parentId;
        this.parentName = parentName;
        this.classId = classId;
        this.className = className;
        this.transportId = transportId;
    }

    // Constructor for new student
    public Student(String firstName, String lastName, LocalDate dob, String gender, String address, 
                   int parentId, int classId, int transportId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.parentId = parentId;
        this.classId = classId;
        this.transportId = transportId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getParentId() { return parentId; }
    public void setParentId(int parentId) { this.parentId = parentId; }

    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }

    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public int getTransportId() { return transportId; }
    public void setTransportId(int transportId) { this.transportId = transportId; }
    
    public String getFullName() { return firstName + " " + lastName; }
}