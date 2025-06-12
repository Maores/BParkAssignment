package gui;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RemoteScreen implements ChatIF{
	/**
	 * The default port and host to connect on.
	 */
	private TextArea dbDisplay;
	private TextField idField, dateField, spotField, srcField;
	private ChatClient client;
	private singletoneClient sg = new singletoneClient();
	private TableView<ParkingRow> table = new TableView<>();
	
	private String id = MainApp.getUserId();
	private String name = MainApp.getUserName();
	
	public RemoteScreen() {
		// Only initialize data fields here, not the scene or stage
		idField = new TextField();
		idField.setPromptText("Enter Order Number");
		dateField = new TextField();
		dateField.setPromptText("Enter Order Date (YYYY-MM-DD)");
		spotField = new TextField();
		spotField.setPromptText("Enter Parking Spot Number");
		srcField = new TextField();
		srcField.setPromptText("Search order number");
		dbDisplay = new TextArea();
		dbDisplay.setPrefHeight(200);
		dbDisplay.setEditable(false);
		dbDisplay.setStyle("-fx-border-color: gray; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-family: monospace;");
		client = sg.getInstance(this);
	}

	public VBox buildRoot() {
		Image icon = new Image(getClass().getResourceAsStream("logo.png"));
		ImageView iv1 = new ImageView();
		iv1.setImage(icon);
		iv1.setFitHeight(icon.getHeight()/10);
		iv1.setFitWidth(icon.getWidth()/10);

		VBox orderNumber = new VBox(new Label("Order Number:"),idField);
		VBox orderDate = new VBox(new Label("Order Date:"),dateField);

		Button viewBtn = new Button("View order History");
		viewBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-cursor: hand;");
		viewBtn.setOnAction(e -> client.handleMessageFromClientUI("VIEW_DATABASE_ID "+id));

		Button updateBtn = new Button("Update Reservation");
		updateBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
		updateBtn.setOnAction(e -> {
			String id = idField.getText();
			String date = dateField.getText();
			String spot = spotField.getText();
			if (!id.isEmpty() && !date.isEmpty() ) {
				String updateMsg = "UPDATE_ORDER " + id  + " " + date;
				client.handleMessageFromClientUI(updateMsg);
			} else {
				displayMessage("Please fill all fields.");
			}
		});
		Button insertBtn = new Button("New Order");
		insertBtn.setStyle("-fx-background-color: #5a6f7d; -fx-text-fill: white; -fx-cursor: hand;");
		insertBtn.setOnAction(e -> client.handleMessageFromClientUI("ADD_ORDER "+id+ " "+ dateField.getText()));
		
//		Button tryBtn = new Button("Reconnect");
//		tryBtn.setStyle("-fx-background-color: #5a6f7d; -fx-text-fill: white; -fx-cursor: hand;");
//		tryBtn.setOnAction(e -> guiClient.connect(DEFAULT_HOST, DEFAULT_PORT));
//		Button srcBtn = new Button("Search");
//		srcBtn.setStyle("-fx-background-color: #5c5a5a; -fx-text-fill: white; -fx-cursor: hand;");
//		srcBtn.setOnAction(e -> guiClient.search(srcField.getText()));
		HBox buttons = new HBox(viewBtn, updateBtn,insertBtn);
		buttons.setAlignment(Pos.CENTER_LEFT);
		buttons.setPadding(new Insets(5));
		buttons.setSpacing(10);
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.TOP_CENTER);
		VBox fields = new VBox(10, orderNumber, orderDate, buttons);
		root.getChildren().addAll(iv1, fields, dbDisplay, table);
		return root;
	}
	
	@SuppressWarnings("unchecked")
	public void displayMessage(String message) {   
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
        else {
        	dbDisplay.setText(message);
        	
        }
    }

	@Override
	public void display(String message) {
		displayMessage(message);
	}

}
