package gui;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class StaffGui implements ChatIF {

    @FXML private TextField nameField;
    @FXML private TextField idField;
    @FXML private TextArea outputDisplay;
    private TableView<ParkingRow> table = new TableView<>();
    private ChatClient client;

    public void start() {
        client = singletoneClient.getInstance(this);
        display("Connected to server!");
    }

    @FXML
    void ViewDB() {
        String command = "VIEW_DATABASE";
        try {
            client.handleMessageFromClientUI(command);
            display("Requesting database contents...");
        } catch (Exception e) {
            display("Error sending request: " + e.getMessage());
        }
    }

    @FXML
    void addNewUser() {
        String name = nameField.getText();
        String id = idField.getText();

        if (name == null || name.isEmpty() || id == null || id.isEmpty()) {
            display("Please enter both name and ID.");
            return;
        }

        String command = "ADD_USER " + id + " " + name;
        try {
            client.handleMessageFromClientUI(command);
            display("Request sent to add user: " + name + " (" + id + ")");
        } catch (Exception e) {
            display("Error: " + e.getMessage());
        }
    }

    @Override
    public void display(String message) {
        Platform.runLater(() -> {
            //outputDisplay.appendText(message + "\n");
        	if(message.startsWith("parking_space")) {
        		Platform.runLater(() -> {
            	String[] str = message.split(" ");
                table.setEditable(true);
                
                
                table.getColumns().clear();

                TableColumn<ParkingRow, String> a = new TableColumn<>(str[0]);
                a.setCellValueFactory(new PropertyValueFactory<>("col1"));
                TableColumn<ParkingRow, String> b = new TableColumn<>(str[1]);
                b.setCellValueFactory(new PropertyValueFactory<>("col2"));
                TableColumn<ParkingRow, String> c = new TableColumn<>(str[2]);
                c.setCellValueFactory(new PropertyValueFactory<>("col3"));
                TableColumn<ParkingRow, String> d = new TableColumn<>(str[3]);
                d.setCellValueFactory(new PropertyValueFactory<>("col4"));
                TableColumn<ParkingRow, String> e = new TableColumn<>(str[4]);
                e.setCellValueFactory(new PropertyValueFactory<>("col5"));
                TableColumn<ParkingRow, String> f = new TableColumn<>(str[5]);
                f.setCellValueFactory(new PropertyValueFactory<>("col6"));

                table.getColumns().addAll(a, b, c, d, e, f);

                ObservableList<ParkingRow> items = FXCollections.observableArrayList();
                for (int i = 6; i + 5 < str.length; i += 6) {
                    ParkingRow row = new ParkingRow(
                        str[i], str[i + 1], str[i + 2],
                        str[i + 3], str[i + 4], str[i + 5]
                    );
                    items.add(row);
                }

                table.setItems(items);  
        		});
            }
        	else if (message.startsWith("User")) {
        		outputDisplay.setText(message);
        	}
        });
    }

    public VBox buildRoot() {
        nameField = new TextField();
        nameField.setPromptText("Enter user name");

        idField = new TextField();
        idField.setPromptText("Enter user ID");

        outputDisplay = new TextArea();
        outputDisplay.setPrefHeight(200);
        outputDisplay.setEditable(false);

        Button addUserBtn = new Button("Add New User");
        addUserBtn.setOnAction(e -> addNewUser());

        Button viewDBBtn = new Button("View Database");
        viewDBBtn.setOnAction(e -> ViewDB());

        VBox root = new VBox(10,
            nameField,
            idField,
            addUserBtn,
            viewDBBtn,
            outputDisplay,
            table
        );
        root.setPrefWidth(400);
        root.setPrefHeight(400);
        return root;
    }
}
