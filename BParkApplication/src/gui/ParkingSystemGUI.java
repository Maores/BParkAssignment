package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

public class ParkingSystemGUI extends Application {
	/**
	 * The default port and host to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;
	final public static String DEFAULT_HOST = "localhost";
	private TextArea dbDisplay;
	private TextField idField, dateField, spotField, srcField;
	private GUIParkingClient guiClient;
	private TableView<ParkingRow> table = new TableView<>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("BPark - Smart Parking System");
		Image icon = new Image(getClass().getResourceAsStream("logo.png"));
		primaryStage.getIcons().add(icon);
		ImageView iv1 = new ImageView();
		iv1.setImage(icon);
		iv1.setFitHeight(icon.getHeight()/10);
		iv1.setFitWidth(icon.getWidth()/10);	
		
		// Initialize GUI <-> Server client

		idField = new TextField();
		idField.setPromptText("Enter Order Number");
		VBox orderNumber = new VBox(new Label("Order Number:"),idField);
		
		dateField = new TextField();
		dateField.setPromptText("Enter Order Date (YYYY-MM-DD)");
		VBox orderDate = new VBox(new Label("Order Date:"),dateField);
		
		spotField = new TextField();
		spotField.setPromptText("Enter Parking Spot Number");
		VBox orderSpot = new VBox(new Label("Parking Space:"),spotField);
		
		srcField = new TextField();
		srcField.setPromptText("Search order number");
		
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
		//Button to reconnect to the server
		Button tryBtn = new Button("Reconnect");
		tryBtn.setStyle("-fx-background-color: #5a6f7d; -fx-text-fill: white; -fx-cursor: hand;");
		tryBtn.setOnAction(e -> guiClient.connect(DEFAULT_HOST, DEFAULT_PORT));
		
		Button srcBtn = new Button("Search");
		srcBtn.setStyle("-fx-background-color: #5c5a5a; -fx-text-fill: white; -fx-cursor: hand;");
		srcBtn.setOnAction(e -> guiClient.search(srcField.getText()));
		
		// Buttons
		HBox buttons = new HBox(viewBtn, updateBtn,tryBtn,srcField,srcBtn);
		buttons.setAlignment(Pos.CENTER_LEFT);
		buttons.setPadding(new Insets(5));
		buttons.setSpacing(10);

		dbDisplay = new TextArea();
		dbDisplay.setPrefHeight(200);
		dbDisplay.setEditable(false);
		dbDisplay.setStyle(
				"-fx-border-color: gray; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-family: monospace;");
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.TOP_CENTER);
		VBox fields = new VBox(10, orderNumber, orderDate, orderSpot, buttons);
		root.getChildren().addAll(iv1, fields, dbDisplay, table);
		//Create new client
		guiClient = new GUIParkingClient(DEFAULT_HOST,DEFAULT_PORT,this);
		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(root, 675, 600));
		primaryStage.show();
	}
	
	@SuppressWarnings("unchecked")
	public void displayMessage(String message) {   
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
        else {
        	dbDisplay.setText(message);
        	
        }
    }

}
