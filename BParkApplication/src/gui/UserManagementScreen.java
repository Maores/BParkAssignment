package gui;

import db.DBController;
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
public class UserManagementScreen {
    
    @FXML private TextField userIdField;
    @FXML private Label resultLabel;
    @FXML private TextArea logArea;
    
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
        
        // Get DB instance without listener (null)
        // This screen doesn't need to receive DB notifications
        DBController db = DBController.getInstance(null);
        
        logArea.appendText("Searching for user: " + userId + "\n");
        
        // Use DB methods normally
        String userRole = db.getUserRoleById(userId);
        
        if (userRole != null) {
            resultLabel.setText("User found! Role: " + userRole);
            logArea.appendText("User role: " + userRole + "\n");
        } else {
            resultLabel.setText("User not found");
            logArea.appendText("No user found with ID: " + userId + "\n");
        }
    }
    
    /**
     * Check if an order exists
     * Another example of DB access
     */
    @FXML
    void checkOrder() {
        String orderId = userIdField.getText();
        
        // Access DB singleton
        DBController db = DBController.getInstance(null);
        
        boolean exists = db.checkDB(orderId);
        
        if (exists) {
            String orderData = db.SearchID(orderId);
            logArea.appendText("Order found:\n" + orderData + "\n");
        } else {
            logArea.appendText("Order " + orderId + " does not exist\n");
        }
    }
    
    /**
     * Example of a more complex operation
     */
    @FXML
    void generateReport() {
        DBController db = DBController.getInstance(null);
        
        logArea.appendText("Generating report...\n");
        
        // Get all database data
        String allData = db.getDatabaseAsString();
        
        // Process the data (count orders, etc.)
        String[] lines = allData.split("\n");
        int orderCount = lines.length - 1; // Minus header
        
        logArea.appendText("Total orders in database: " + orderCount + "\n");
        logArea.appendText("Report generated successfully\n");
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