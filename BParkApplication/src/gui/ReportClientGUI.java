package gui;

import client.ChatClient;
import common.ChatIF;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * Example of a client-side GUI that connects to the server through the network.
 * This demonstrates how multiple client applications can access the database
 * through the server without direct DB access.
 */
public class ReportClientGUI implements ChatIF {
    
    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private TextArea reportDisplay;
    
    private ChatClient client;
    private static final String HOST = "localhost";
    private static final int PORT = 5555;
    
    public void start() {
        // Initialize connection to server
        try {
            client = new ChatClient(HOST, PORT, this);
            display("Connected to server!");
        } catch (Exception e) {
            display("Error: Cannot connect to server");
        }
        
        // Setup UI...
        initializeReportTypes();
    }
    
    private void initializeReportTypes() {
        reportTypeCombo.getItems().addAll(
            "Daily Report",
            "Weekly Report", 
            "Monthly Report",
            "Parking Usage Report"
        );
    }
    
    /**
     * Generate report based on selected type
     */
    @FXML
    void generateReport() {
        String reportType = reportTypeCombo.getValue();
        
        if (reportType == null) {
            display("Please select a report type");
            return;
        }
        
        // Send request to server with custom command
        String command = "GENERATE_REPORT " + reportType.replace(" ", "_");
        
        try {
            client.handleMessageFromClientUI(command);
            display("Requesting " + reportType + "...");
        } catch (Exception e) {
            display("Error sending request: " + e.getMessage());
        }
    }
    
    /**
     * Request all parking spaces status
     */
    @FXML
    void getAllParkingStatus() {
        try {
            // Custom command for this specific screen
            client.handleMessageFromClientUI("GET_ALL_PARKING_STATUS");
            display("Fetching parking status...");
        } catch (Exception e) {
            display("Error: " + e.getMessage());
        }
    }
    
    /**
     * Implementation of ChatIF interface
     */
    @Override
    public void display(String message) {
        javafx.application.Platform.runLater(() -> {
            reportDisplay.appendText(message + "\n");
        });
    }
    
    /**
     * Clean up on close
     */
    public void stop() {
        if (client != null) {
            client.quit();
        }
    }

    public VBox buildRoot() {
        reportTypeCombo = new ComboBox<>();
        reportDisplay = new TextArea();
        reportDisplay.setPrefHeight(200);
        reportDisplay.setEditable(false);
        initializeReportTypes();
        Button generateBtn = new Button("Generate Report");
        generateBtn.setOnAction(e -> generateReport());
        Button statusBtn = new Button("Get All Parking Status");
        statusBtn.setOnAction(e -> getAllParkingStatus());
        VBox root = new VBox(10, reportTypeCombo, generateBtn, statusBtn, reportDisplay);
        root.setPrefWidth(400);
        root.setPrefHeight(300);
        return root;
    }
} 