package com.example.schoolmanagement.models;

import java.time.LocalDate;

public class Fee {
    private int id;
    private int studentId;
    private double amount;
    private LocalDate date;

    public Fee(int id, int studentId, double amount, LocalDate date) {
        this.id = id;
        this.studentId = studentId;
        this.amount = amount;
        this.date = date;
    }

    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
}
