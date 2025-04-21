package com.example.apd545_final_project.controller;

import com.example.apd545_final_project.model.Transaction;
import com.example.apd545_final_project.database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class EditTransactionController {

    @FXML private ComboBox<String> typeComboBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField descriptionField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private Transaction transactionToEdit;
    private final DatabaseHandler dbHandler = new DatabaseHandler();

    public void setTransaction(Transaction transaction) {
        this.transactionToEdit = transaction;
        // Pre-fill fields with selected transaction data
        typeComboBox.setValue(transaction.getType());
        datePicker.setValue(transaction.getDate());
        amountField.setText(String.valueOf(transaction.getAmount()));
        categoryComboBox.setValue(transaction.getCategory());
        descriptionField.setText(transaction.getDescription());
    }

    @FXML
    public void initialize() {
        typeComboBox.getItems().addAll("Income", "Expense");
        categoryComboBox.getItems().addAll("Food", "Transport", "Entertainment", "Bills", "Healthcare", "Rent", "Other");
    }
    
    @FXML
    private void handleSave() throws SQLException {
        // Get values from input fields
        String type = typeComboBox.getValue();
        LocalDate date = datePicker.getValue();
        double amount = Double.parseDouble(amountField.getText());
        String category = categoryComboBox.getValue();
        String description = descriptionField.getText();

        // Update transaction object
        transactionToEdit.setType(type);
        transactionToEdit.setDate(date);
        transactionToEdit.setAmount(amount);
        transactionToEdit.setCategory(category);
        transactionToEdit.setDescription(description);

        // Update database
        dbHandler.updateTransaction(transactionToEdit.getId(), transactionToEdit);

        // Close window
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        // Close the window without saving
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
