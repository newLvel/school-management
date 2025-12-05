package com.example.schoolmanagement.models;

public class Parent {
    private int id;
    private String fatherName;
    private String motherName;
    private String email;
    private String phone;
    private String address;

    public Parent(int id, String fatherName, String motherName, String email, String phone, String address) {
        this.id = id;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Parent(String fatherName, String motherName, String email, String phone, String address) {
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    // Helper to show a representative name (e.g. "John Doe (Father)")
    public String getDisplayName() {
        if (fatherName != null && !fatherName.isEmpty()) return fatherName;
        if (motherName != null && !motherName.isEmpty()) return motherName;
        return "Unknown Parent";
    }
}
