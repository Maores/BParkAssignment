
package client;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ParkingSystemGUI extends Application {
	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;
	private TextArea dbDisplay;
	private TextField idField, dateField, spotField;
	private GUIParkingClient guiClient;
	private TableView<ParkingRow> table = new TableView<>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("BPark System");

		// Initialize GUI <-> Server client
		guiClient = new GUIParkingClient("localhost", DEFAULT_PORT, this);

		Label title = new Label("Smart Parking System");
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
		// Buttons
		HBox buttons = new HBox(viewBtn, updateBtn);
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
		VBox fields = new VBox(10, idField, dateField, spotField, buttons);
		root.getChildren().addAll(title, fields, dbDisplay, table);

		primaryStage.setScene(new Scene(root, 420, 500));
		primaryStage.show();
	}

	@SuppressWarnings("unchecked")
	public void displayMessage(String message) {       
    	if(message.startsWith("parking")) {
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
