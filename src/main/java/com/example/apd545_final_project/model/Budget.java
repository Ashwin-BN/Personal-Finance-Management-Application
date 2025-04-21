package com.example.apd545_final_project.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Budget {
    private StringProperty category;
    private DoubleProperty limit;
    private DoubleProperty currentSpending;

    public Budget(String category, double limit) {
        this.category = new SimpleStringProperty(category);
        this.limit = new SimpleDoubleProperty(limit);
        this.currentSpending = new SimpleDoubleProperty(0.0);  // Default to 0
    }

    public String getCategory() { return category.get(); }
    public void setCategory(String category) { this.category.set(category); }

    public double getLimit() { return limit.get(); }
    public void setLimit(double limit) { this.limit.set(limit); }

    public double getCurrentSpending() { return currentSpending.get(); }
    public void setCurrentSpending(double currentSpending) { this.currentSpending.set(currentSpending); }

    public StringProperty categoryProperty() { return category; }
    public DoubleProperty limitProperty() { return limit; }
    public DoubleProperty currentSpendingProperty() { return currentSpending; }
}
