package gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import client.ChatClient;
import common.ChatIF;

/**
 * Example of a client-side GUI that connects to the server through the network.
 * This demonstrates how multiple client applications can access the database
 * through the server without direct DB access.
 */
public class ReportClientGUI extends Application implements ChatIF {
    
    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private TextArea reportDisplay;
    
    private ChatClient client;
    private static final String HOST = "localhost";
    private static final int PORT = 5555;
    
    @Override
    public void start(Stage primaryStage) {
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
            client.sendToServer(command);
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
            client.sendToServer("GET_ALL_PARKING_STATUS");
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
    @Override
    public void stop() {
        if (client != null) {
            client.quit();
        }
    }
} 