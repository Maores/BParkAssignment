
package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ParkingSystemGUI extends Application {

    private TextArea dbDisplay;
    private TextField idField, dateField, spotField;
    private GUIParkingClient guiClient;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Smart Parking System");

        // Initialize GUI <-> Server client
        guiClient = new GUIParkingClient("localhost", 5555, this);

        Label title = new Label("Smart Parking GUI");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        idField = new TextField();
        idField.setPromptText("Enter Order Number");

        dateField = new TextField();
        dateField.setPromptText("Enter Order Date (YYYY-MM-DD)");

        spotField = new TextField();
        spotField.setPromptText("Enter Parking Spot Number");

        Button viewBtn = new Button("View DB");
        viewBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-cursor: hand;");
        viewBtn.setOnAction(e -> guiClient.sendMessage("VIEW_DATABASE"));

        Button updateBtn = new Button("Update Reservation");
        updateBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
        updateBtn.setOnAction(e -> {
            String id = idField.getText();
            String date = dateField.getText();
            String spot = spotField.getText();
            if (!id.isEmpty() && !date.isEmpty() && !spot.isEmpty()) {
                String updateMsg = "UPDATE_ORDER " + id + " " + spot + " " + date;
                guiClient.sendMessage(updateMsg);
            } else {
                displayMessage("Please fill all fields.");
            }
        });

        dbDisplay = new TextArea();
        dbDisplay.setPrefHeight(200);
        dbDisplay.setEditable(false);
        dbDisplay.setStyle("-fx-border-color: gray; -fx-border-radius: 5; -fx-background-radius: 5;");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        VBox fields = new VBox(10, idField, dateField, spotField, viewBtn, updateBtn);
        root.getChildren().addAll(title, fields, dbDisplay);

        primaryStage.setScene(new Scene(root, 420, 500));
        primaryStage.show();
    }

    public void displayMessage(String message) {
        dbDisplay.setText(message);
    }
}
