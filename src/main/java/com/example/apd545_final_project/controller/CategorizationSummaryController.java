package com.example.apd545_final_project.controller;

import com.example.apd545_final_project.database.DatabaseHandler;
import com.example.apd545_final_project.model.Budget;
import com.example.apd545_final_project.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CategorizationSummaryController {

    @FXML private BarChart<String, Number> spendingBarChart;
    @FXML private PieChart categoryPieChart;
    @FXML private CategoryAxis categoryAxis;
    @FXML private NumberAxis amountAxis;
    @FXML private TableView<Budget> budgetTable;
    @FXML private TableColumn<Budget, String> colCategory;
    @FXML private TableColumn<Budget, Double> colLimit;
    @FXML private TableColumn<Budget, Double> colCurrentSpending;

    private final DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    public void initialize() {
        loadSpendingCharts();
        loadBudgetTable();
    }

    private void loadSpendingCharts() {
        // Load data for bar chart and pie chart
        try {
            List<String> categories = dbHandler.getCategories();
            ObservableList<BarChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            for (String category : categories) {
            	int currentMonth = java.time.LocalDate.now().getMonthValue();  // 1 to 12
            	int currentYear = java.time.LocalDate.now().getYear();
            	double spent = dbHandler.getSpentAmount(category, currentMonth, currentYear);

            	barChartData.add(new BarChart.Series<>(category, FXCollections.observableArrayList(new BarChart.Data<>(category, spent))));
                pieChartData.add(new PieChart.Data(category, spent));
            }
            
            spendingBarChart.setData(barChartData);
            categoryPieChart.setData(pieChartData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBudgetTable() {
        // Load budgets and current spending
    	try {
            List<Budget> budgets = dbHandler.fetchBudgets();
            
            for (Budget budget : budgets) {
                double spent = dbHandler.getSpentAmount(budget.getCategory(), java.time.LocalDate.now().getMonthValue(), java.time.LocalDate.now().getYear());
                budget.setCurrentSpending(spent);  // Set current spending for the category
            }

            ObservableList<Budget> budgetData = FXCollections.observableArrayList(budgets);
            budgetTable.setItems(budgetData);
            
         // Make sure the columns are correctly bound
            colCategory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
            colLimit.setCellValueFactory(cellData -> cellData.getValue().limitProperty().asObject());
            colCurrentSpending.setCellValueFactory(cellData -> cellData.getValue().currentSpendingProperty().asObject());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void openAddTransaction() {
        // Open Add Transaction window (similar to your current implementation)
    }

    @FXML
    private void openEditTransaction() {
        // Open Edit Transaction window (similar to your current implementation)
    }

    @FXML
    private void deleteTransaction() {
        // Handle transaction deletion (similar to your current implementation)
    }
    @FXML
    public void openUpdateBudget() {
        // Your code to open the update budget screen
        System.out.println("openUpdateBudget triggered");
    }

    @FXML
    private void viewSummary() {
        // Handle summary view (this will be called when the menu item is clicked)
        System.out.println("View Summary triggered");
    }

    @FXML
    private void viewBudget() {
        // Handle budget view (this will be called when the menu item is clicked)
        System.out.println("View Budget triggered");
    }
}