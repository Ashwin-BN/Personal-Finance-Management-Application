package com.example.apd545_final_project.controller;

import com.example.apd545_final_project.model.*;

import java.sql.SQLException;

import com.example.apd545_final_project.database.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransactionController {
    private DatabaseHandler dbHandler;

    public TransactionController() {
        dbHandler = new DatabaseHandler();
    }

    public void addTransaction(Transaction tx) throws SQLException {
        dbHandler.insertTransaction(tx);
    }

    public ObservableList<Transaction> getAllTransactions() throws SQLException {
        return FXCollections.observableArrayList(dbHandler.fetchTransactions());
    }

    // update/delete methods...
}
