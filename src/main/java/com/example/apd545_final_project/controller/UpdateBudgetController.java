package com.example.apd545_final_project.controller;

import com.example.apd545_final_project.database.DatabaseHandler;
import com.example.apd545_final_project.model.Budget;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UpdateBudgetController {

    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField budgetLimitField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private final DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    public void initialize() {
        // Load categories from the database or a predefined list
        categoryComboBox.getItems().addAll("Food", "Transport", "Entertainment", "Bills", "Healthcare", "Rent", "Other");
    }

    @FXML
    private void handleSave() {
        try {
            // Get values from input fields
            String category = categoryComboBox.getValue();
            double newLimit = Double.parseDouble(budgetLimitField.getText());

            // Update the budget for the selected category
            dbHandler.insertOrUpdateBudget(category, newLimit);

            // Close window
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            // Show error if the limit is not a valid number
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number for the budget limit.");
            alert.showAndWait();
        } catch (SQLException e) {
            // Handle any SQL errors
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update budget. Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        // Close the window without saving
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
