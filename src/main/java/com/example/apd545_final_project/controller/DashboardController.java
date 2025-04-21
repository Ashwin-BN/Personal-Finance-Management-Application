package com.example.apd545_final_project.controller;

import com.example.apd545_final_project.model.*;
import com.example.apd545_final_project.controller.TransactionController;
import com.example.apd545_final_project.database.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashboardController {

    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> colType;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, Double> colAmount;
    @FXML private TableColumn<Transaction, String> colCategory;
    @FXML private TableColumn<Transaction, String> colDescription;
    @FXML private BarChart<String, Number> expenseChart;
    @FXML private CategoryAxis categoryAxis;
    @FXML private NumberAxis amountAxis;

    private final DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    public void initialize() {
        // Set up table columns
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        loadTransactions();
    }

    @FXML
    private void loadTransactions() {
        Task<List<Transaction>> task = new Task<>() {
            @Override
            protected List<Transaction> call() throws SQLException {
                return dbHandler.fetchTransactions();
            }
        };

        task.setOnSucceeded(event -> {
            transactionTable.setItems(FXCollections.observableArrayList(task.getValue()));
        });

        new Thread(task).start();
    }


    @FXML
    private void openAddTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/apd545_final_project/addTransaction.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Transaction");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadTransactions(); // Refresh table after adding
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    @FXML
    private void openEditTransaction() {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/apd545_final_project/editTransaction.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Edit Transaction");
                stage.setScene(new Scene(loader.load()));
                stage.initModality(Modality.APPLICATION_MODAL);

                EditTransactionController controller = loader.getController();
                controller.setTransaction(selectedTransaction);
                stage.showAndWait();
                loadTransactions(); // Refresh the table after editing
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Transaction Selected", "Please select a transaction to edit.");
        }
    }
    
    @FXML
    private void deleteTransaction() {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            try {
                dbHandler.deleteTransaction(selectedTransaction.getId());
                loadTransactions(); // Refresh the table after deletion
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete transaction.");
            }
        } else {
            showAlert("No Transaction Selected", "Please select a transaction to delete.");
        }
    }
    
    @FXML
    private void viewSummary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/apd545_final_project/categorizationSummary.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Transaction Summary");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            // Optional: Show it in a dialog for user feedback
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error occurred: " + e.getMessage());
            alert.show();
        }
    }
    
    @FXML
    private void openUpdateBudget() {
    	 try {
    	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/apd545_final_project/updateBudget.fxml"));
    	        Parent root = loader.load();

    	        Stage stage = new Stage();
    	        stage.setTitle("Update Budget");
    	        stage.setScene(new Scene(root));
    	        stage.initModality(Modality.APPLICATION_MODAL);
    	        stage.showAndWait();
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	        showAlert("Error", "Unable to open the Update Budget window.");
    	    }
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
