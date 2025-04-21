package com.example.apd545_final_project.controller;

import com.example.apd545_final_project.model.Transaction;
import com.example.apd545_final_project.database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class AddTransactionController {

	@FXML private ComboBox<String> typeComboBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField descriptionField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private final DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    private void initialize() {
    	typeComboBox.getItems().addAll("Income", "Expense");
        typeComboBox.getSelectionModel().selectFirst();
        
     // Populate the category ComboBox with possible categories
        categoryComboBox.getItems().addAll("Food", "Transport", "Entertainment", "Bills", "Healthcare", "Rent", "Other");
        categoryComboBox.getSelectionModel().selectFirst(); // Optionally select a default value
        
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    private void handleSave() throws SQLException {
        // Get values from input fields
        String type = typeComboBox.getValue();
        LocalDate date = datePicker.getValue();
        double amount;
        String category = categoryComboBox.getValue();
        String description = descriptionField.getText();
        
     // Data Validation
        if (date == null || category.isEmpty() || amountField.getText().isEmpty()) {
            showAlert("Missing Fields", "All fields are required.");
            return;
        }

        try {
            amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert("Invalid Amount", "Amount must be a positive number.");
            return;
        }

        
        // Create a new Transaction object
        Transaction newTransaction = new Transaction(type, date, amount, category, description);

        // Save the transaction to the database
        dbHandler.insertTransaction(newTransaction);

        // Close the window and refresh the transactions list in the Dashboard
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        // Close the window without saving
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close(); // Close the "Add Transaction" window
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}