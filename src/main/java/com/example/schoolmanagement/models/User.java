package com.example.schoolmanagement.models;

public class User {
    private int id;
    private String username;
    private String passwordHash; // Add this field
    private String role;

    public User(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // Overloaded constructor for DAO to fetch password
    public User(int id, String username, String passwordHash, String role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; } // Add getter
    public String getRole() { return role; }
}
