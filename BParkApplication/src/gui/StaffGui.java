package gui;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class StaffGui implements ChatIF {

    @FXML private TextField nameField;
    @FXML private TextField idField;
    @FXML private TextArea outputDisplay;
    
    private TableView<ParkingRow> table = new TableView<>();
    private singletoneClient sg = new singletoneClient();
    private ChatClient client;

    public void start() {
        client = sg.getInstance(this);
        display("Connected to server!");
    }

    @FXML
    void ViewDB() {
        String command = "VIEW_DATABASE";
        outputDisplay.appendText("Fetching data from DataBase...");
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
        	if(message.startsWith("order_number")) {
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


                table.getColumns().addAll(a, b, c, d, e);

                ObservableList<ParkingRow> items = FXCollections.observableArrayList();
                for (int i = 5; i + 4 < str.length; i += 5) {
                    ParkingRow row = new ParkingRow(
                        str[i], str[i + 1], str[i + 2],
                        str[i + 3], str[i + 4]
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

    public StackPane buildRoot() {
    	StackPane root = new StackPane();
    	Label name =  new Label("User name:");
        nameField = new TextField();
        nameField.setPromptText("Enter user name");
        VBox nameBox = new VBox(name,nameField);
        Label id =  new Label("User ID:");
        idField = new TextField();
        idField.setPromptText("Enter user ID");
        VBox idBox = new VBox(id ,idField);
        outputDisplay = new TextArea();
        outputDisplay.setPrefHeight(350);
        outputDisplay.setEditable(false);

        Button addUserBtn = new Button("Add New User");
        addUserBtn.setOnAction(e -> addNewUser());

        Button viewDBBtn = new Button("View Database");
        viewDBBtn.setOnAction(e -> ViewDB());
        
        //Btn styles
        addUserBtn.setStyle("-fx-background-color: #0b132b; -fx-text-fill: white; -fx-cursor: hand;");
        viewDBBtn.setStyle("-fx-background-color: #0b132b; -fx-text-fill: white; -fx-cursor: hand;");
        HBox btns = new HBox(viewDBBtn,addUserBtn);
        btns.setSpacing(10);
        VBox Interior = new VBox(10,
        	nameBox,
            idBox,
            btns,
            outputDisplay,
            table
        );
        root.setMargin(Interior, new Insets(15));
        root.getChildren().add(Interior);
        root.setPrefWidth(400);
        root.setPrefHeight(400);
        return root;
    }
}
