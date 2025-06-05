package gui;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Example screen showing how to access the database after the GUI/DB separation.
 * This screen doesn't need to receive database notifications, so it passes null as listener.
 */
public class UserManagementScreen implements ChatIF {
    
    @FXML private TextField userIdField;
    @FXML private Label resultLabel;
    @FXML private TextArea logArea;
    
    private ChatClient client;

    public UserManagementScreen() {
    	client = singletoneClient.getInstance(this);
    }

    /**
     * Search for a user by ID
     * Demonstrates accessing DB without being a listener
     */
    @FXML
    void searchUser() {
        String userId = userIdField.getText();
        
        if (userId.isEmpty()) {
            resultLabel.setText("Please enter a user ID");
            return;
        }
        
        client.handleMessageFromClientUI("SEARCH_USER " + userId);
        logArea.appendText("Searching for user: " + userId + "\n");
    }
    
    /**
     * Check if an order exists
     * Another example of DB access
     */
    @FXML
    void checkOrder() {
        String orderId = userIdField.getText();
        
        client.handleMessageFromClientUI("SEARCH_ORDER " + orderId);
        logArea.appendText("Checking order: " + orderId + "\n");
    }
    
    /**
     * Example of a more complex operation
     */
    @FXML
    void generateReport() {
    	client.handleMessageFromClientUI("GENERATE_REPORT DAILY_REPORT");
        logArea.appendText("Generating report...\n");
    }
    //Get message from the server
    @Override
    public void display(String message) {
        displayMessage(message);
    }

    private void displayMessage(String message) {
        Platform.runLater(() -> {
            logArea.appendText(message + "\n");
            if (message.startsWith("User found!")) {
                resultLabel.setText(message);
            } else if (message.startsWith("User not found")) {
                resultLabel.setText(message);
            } else if (message.startsWith("Order found:")) {
                resultLabel.setText("Order found");
            } else if (message.startsWith("Order") && message.contains("does not exist")) {
                resultLabel.setText(message);
            } else if (message.startsWith("=== DAILY PARKING REPORT ===")) {
                resultLabel.setText("Report generated");
            }
        });
    }

    public VBox buildRoot() {
        userIdField = new TextField();
        resultLabel = new Label();
        logArea = new TextArea();
        logArea.setPrefHeight(200);
        logArea.setEditable(false);
        Button searchBtn = new Button("Search User");
        searchBtn.setOnAction(e -> searchUser());
        Button checkOrderBtn = new Button("Check Order");
        checkOrderBtn.setOnAction(e -> checkOrder());
        Button reportBtn = new Button("Generate Report");
        reportBtn.setOnAction(e -> generateReport());
        VBox root = new VBox(10, new Label("User ID:"), userIdField, searchBtn, checkOrderBtn, reportBtn, resultLabel, logArea);
        root.setPrefWidth(400);
        root.setPrefHeight(350);
        return root;
    }
} 