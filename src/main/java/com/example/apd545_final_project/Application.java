package com.example.apd545_final_project;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
      Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
      primaryStage.setTitle("Personal Finance Manager");
      primaryStage.setScene(new Scene(root));
      primaryStage.show();
  }

  public static void main(String[] args) {
      launch(args);
  }
}
