package com.example.apd545_final_project.model;

import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
	private int id;
    private String type; // income or expense
    private LocalDate date;
    private double amount;
    private String category;
    private String description;

    
    public Transaction(int id, String type, LocalDate date, double amount, String category, String description) {
    	this.id = id;
    	this.type = type;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public Transaction(String type, LocalDate date, double amount, String category, String description) {
        this(-1, type, date, amount, category, description);
    }
    
    
 // Getters and setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
