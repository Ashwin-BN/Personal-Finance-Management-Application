module com.example.apd545_final_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires javafx.graphics;
	requires java.sql;
	requires org.xerial.sqlitejdbc;


	opens com.example.apd545_final_project.controller to javafx.fxml;

    exports com.example.apd545_final_project;
    exports com.example.apd545_final_project.controller;
    exports com.example.apd545_final_project.model;
    exports com.example.apd545_final_project.database;
}