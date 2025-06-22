package gui;

import java.io.IOException;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RemoteScreen implements ChatIF {
	/**
	 * The default port and host to connect on.
	 */
	private TextArea dbDisplay;
	private TextField orderField;
	private ChatClient client;
	private singletoneClient sg = new singletoneClient();
	private TableView<ParkingRow> table = new TableView<>();
	private DatePicker datepic;
	private MainApp main;
	private Stage primary;
	private String id = MainApp.getUserId();
	private String name = MainApp.getUserName();

	public RemoteScreen() {
		// Only initialize data fields here, not the scene or stage
		orderField = new TextField();
		orderField.setPromptText("Enter order number");
		orderField.setMaxWidth(170);
//		srcField = new TextField();
//		srcField.setPromptText("Search order number");
		dbDisplay = new TextArea();
		datepic = new DatePicker();
		datepic.setPromptText("Enter order date");
		datepic.setMaxWidth(170);
		dbDisplay.setPrefHeight(200);
		dbDisplay.setEditable(false);
//		dbDisplay.setStyle(
//				"-fx-border-color: gray; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-family: monospace;");
		client = sg.getInstance(this);
	}
	public void setMain(MainApp main) {
		this.main = main;
	}

	public StackPane buildRoot() {
//		Image icon = new Image(getClass().getResourceAsStream("logo.png"));
//		ImageView iv1 = new ImageView();
//		iv1.setImage(icon);
//		iv1.setFitHeight(icon.getHeight() / 10);
//		iv1.setFitWidth(icon.getWidth() / 10);
		StackPane root = new StackPane();
		// root.setId("pane");

		Button viewBtn = new Button("View order History");

		viewBtn.setOnAction(e -> {
			dbDisplay.setText("Fetching data...\n");
			client.handleMessageFromClientUI("VIEW_DATABASE_ID " + id);
		});

		Button updateBtn = new Button("Update Reservation");

		updateBtn.setOnAction(e -> {
			String orderNum = orderField.getText();
			String date = datepic.getValue().toString();
			if (!orderNum.isEmpty() && !date.isEmpty()) {
				String updateMsg = "UPDATE_ORDER " + orderNum + " " + date;
				client.handleMessageFromClientUI(updateMsg);
			} else {
				displayMessage("Please fill order number AND date fields.");
			}
		});

		Button insertBtn = new Button("New Order");
		insertBtn.setOnAction(e -> {
			
			if (datepic.getValue() != null) {
				String date = datepic.getValue().toString();
				client.handleMessageFromClientUI("ADD_ORDER " + id + " " + date);
			} else {
				displayMessage("Please fill date field.");
			}

		});

		Button updateUserBtn = new Button("Update user information");
		updateUserBtn.setOnAction(e -> {
			Stage updateScreen = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/userInfoUpdate.fxml"));
			Parent pop;
			try {
				pop = loader.load();
				Scene s = new Scene(pop,300,210);
				s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
				UserUpdate controller = loader.getController();
				controller.setPopWindow(updateScreen);
				controller.name.setText(name);
				updateScreen.setScene(s);
				updateScreen.setResizable(false);
				updateScreen.setTitle("Update Information");	
				updateScreen.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			

		});
		Button logOutBtn = new Button("LogOut");
		logOutBtn.setId("logOutBtn");
		//.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-cursor: hand;");
		logOutBtn.setOnAction(e -> {
			try {
				main.showLoginScreen();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
//		// Styles
//		updateBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
//		viewBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-cursor: hand;");
//		insertBtn.setStyle("-fx-background-color: #5a6f7d; -fx-text-fill: white; -fx-cursor: hand;");
		VBox orderNumber = new VBox(new Label("Order Number:"), orderField);
		VBox orderDate = new VBox(new Label("Order Date:"), datepic);
		HBox buttons = new HBox(viewBtn, updateBtn, insertBtn,updateUserBtn,logOutBtn);
		buttons.setAlignment(Pos.CENTER_LEFT);
		buttons.setPadding(new Insets(5));
		buttons.setSpacing(10);
		VBox interior = new VBox(10);
		root.setPadding(new Insets(15));
		root.setAlignment(Pos.TOP_CENTER);
		HBox fields = new HBox(orderNumber, orderDate);
		fields.setSpacing(10);
		VBox inter = new VBox(10, fields, buttons);
		interior.getChildren().addAll(inter, dbDisplay, table);
		root.getChildren().add(interior);
		return root;
		// not available for now/at all
//		Button tryBtn = new Button("Reconnect");
//		tryBtn.setStyle("-fx-background-color: #5a6f7d; -fx-text-fill: white; -fx-cursor: hand;");
//		tryBtn.setOnAction(e -> guiClient.connect(DEFAULT_HOST, DEFAULT_PORT));
//		Button srcBtn = new Button("Search");
//		srcBtn.setStyle("-fx-background-color: #5c5a5a; -fx-text-fill: white; -fx-cursor: hand;");
//		srcBtn.setOnAction(e -> guiClient.search(srcField.getText()));
	}

	@SuppressWarnings("unchecked")
	public void displayMessage(String message) {
		if (message.startsWith("order_number")) {
			dbDisplay.appendText("succeded!\n");
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
					ParkingRow row = new ParkingRow(str[i], str[i + 1], str[i + 2], str[i + 3], str[i + 4]);
					items.add(row);
				}
				table.setItems(items);
			});
		} else {
			dbDisplay.setText(message);

		}
	}

	@Override
	public void handleMessageFromServer(String message) {
		displayMessage(message);
	}

}
