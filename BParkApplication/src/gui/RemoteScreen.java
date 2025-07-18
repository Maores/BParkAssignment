package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * GUI screen for remote users to manage parking orders in the BPark system.
 * <p>
 * Provides features to:
 * <ul>
 * <li>View order history</li>
 * <li>Insert new orders within allowed date/time range</li>
 * <li>Extend existing reservations</li>
 * <li>Update user information</li>
 * <li>Logout</li>
 * </ul>
 */
public class RemoteScreen implements ChatIF {
	/**
	 * The default port and host to connect on.
	 */
	private TextArea dbDisplay;
	private TextField orderField;
	private ChatClient client;
	private singletoneClient sg = new singletoneClient();
	private TableView<ParkingRow> table = new TableView<>();

	private Spinner<String> hourSpinner;

	private DatePicker datePick;
	private MainApp main;
	private Stage primary;
	private String id = MainApp.getUserId();
	private String name = MainApp.getUserName();

	/**
	 * Initializes UI components and connects the client to the server.
	 */
	public RemoteScreen() {
		// Only initialize data fields here, not the scene or stage
		orderField = new TextField();
		orderField.setPromptText("Enter order number");
		orderField.setMaxWidth(170);

		dbDisplay = new TextArea();
		datePick = new DatePicker();
		datePick.setPromptText("Enter order date");
		datePick.setMaxWidth(170);
		dbDisplay.setPrefHeight(200);
		dbDisplay.setEditable(false);

		client = sg.getInstance(this);
	}

	/**
	 * Sets the reference to the main application.
	 * 
	 * @param main the main application instance
	 */
	public void setMain(MainApp main) {
		this.main = main;
	}

	/**
	 * Builds and returns the root layout for the remote screen UI.
	 * 
	 * @return the root layout for this screen
	 */
	public StackPane buildRoot() {
		StackPane root = new StackPane();

		Spinner<String> hourSpinner = new Spinner<>();
		hourSpinner.setEditable(false); // prevent manual input

		// Generate time values from 09:00 to 17:00 in 15-minute intervals
		List<String> timeOptions = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.of(9, 0);
		LocalTime endTime = LocalTime.of(17, 0);

		while (!startTime.isAfter(endTime)) {
			timeOptions.add(startTime.format(formatter));
			startTime = startTime.plusMinutes(15);
		}

		// Set value factory
		SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(
				FXCollections.observableArrayList(timeOptions));
		valueFactory.setValue(timeOptions.get(0)); // default to first time (09:00)
		hourSpinner.setValueFactory(valueFactory);
		Button viewBtn = new Button("View order History");

		viewBtn.setOnAction(e -> {
			dbDisplay.setText("Fetching data...\n");
			client.handleMessageFromClientUI("VIEW_DATABASE_ID " + id);
		});

		Button updateBtn = new Button("Extend Reservation");

		updateBtn.setOnAction(e -> {
			if (!orderField.getText().isEmpty()) {
				String updateMsg = "UPDATE_ORDER " + orderField.getText();
				client.handleMessageFromClientUI(updateMsg);
			} else {
				displayMessage("Please fill order number that you want to extend.");
			}
		});

		Button insertBtn = new Button("New Order");

		insertBtn.setOnAction(e -> {
			/*
			 * Implementing 7 days -> 24 days prior limit
			 */

			if (datePick.getValue() != null) {
				LocalDate selectedDate = datePick.getValue();
				LocalDate today = LocalDate.now();
				LocalDate minDate = today.plusDays(1);
				LocalDate maxDate = today.plusDays(7);
				if (selectedDate.isBefore(minDate) || selectedDate.isAfter(maxDate)) {
					displayMessage("Error! Reservation must be between 1 and 7 days from today.");
					return;
				}

				String date = datePick.getValue().toString();
				client.handleMessageFromClientUI(
						"ADD_ORDER " + id + " " + date + " " + hourSpinner.getValue().toString());
			} else {
				displayMessage("Please fill date field and set time.");
			}
		});
		Button updateUserBtn = new Button("Update user information");
		updateUserBtn.setId("updateUserBtn");
		updateUserBtn.setOnAction(e -> {
			Stage updateScreen = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/userInfoUpdate.fxml"));
			Parent pop;
			try {
				pop = loader.load();
				Scene s = new Scene(pop, 300, 210);
				s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
				UserUpdate controller = loader.getController();
				controller.setPopWindow(updateScreen);
				controller.name.setText(name);
				updateScreen.setScene(s);
				updateScreen.setResizable(false);
				updateScreen.setAlwaysOnTop(true);
				updateScreen.setTitle("Update Information");
				updateScreen.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		Button logOutBtn = new Button("LogOut");
		logOutBtn.setId("logOutBtn");
		logOutBtn.setOnAction(e -> {
			try {
				main.showLoginScreen();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		Separator verticalSeparator = new Separator();
		verticalSeparator.setOrientation(Orientation.VERTICAL);
		VBox orderNumber = new VBox(new Label("Order Number:"), orderField);
		VBox orderDate = new VBox(new Label("Order Date:"), datePick);
		VBox orderHour = new VBox(new Label("Order Hour:"), hourSpinner);
		VBox emptyBoxInsertBtn = new VBox(new Label(""), insertBtn);
		HBox buttons = new HBox(updateBtn, viewBtn, updateUserBtn);

		buttons.setAlignment(Pos.CENTER_LEFT);
		buttons.setPadding(new Insets(5));
		buttons.setSpacing(10);
		VBox interior = new VBox(10);
		root.setPadding(new Insets(15));
		root.setAlignment(Pos.TOP_CENTER);
		HBox fields = new HBox(orderNumber, verticalSeparator, orderDate, orderHour, emptyBoxInsertBtn);
		fields.setSpacing(10);

		// ---------- header bar (fields + spacer + LogOut) ----------
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS); // spacer takes all extra width

		HBox headerBar = new HBox(fields, spacer, logOutBtn);
		headerBar.setAlignment(Pos.TOP_LEFT);
		headerBar.setSpacing(10);

		VBox inter = new VBox(10, headerBar, buttons);
		dbDisplay.setText(
				"Welcome to the BPark!\n"
				+ "---------------------\n"
				+ "To extend reservation - enter order number.\n"
				+ "To add new order - enter date and set time.\n"
				+ "Good Luck!");
		interior.getChildren().addAll(inter, dbDisplay, table);
		root.getChildren().add(interior);
		return root;
	}

	/**
	 * Displays a server message in the text area or loads data into the table.
	 * 
	 * @param message the message received from the server
	 */
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
				TableColumn<ParkingRow, String> f = new TableColumn<>(str[5]);
				f.setCellValueFactory(new PropertyValueFactory<>("col6"));
				TableColumn<ParkingRow, String> g = new TableColumn<>(str[6]);
				g.setCellValueFactory(new PropertyValueFactory<>("col7"));
				table.getColumns().addAll(a, b, c, d, e, f, g);
				ObservableList<ParkingRow> items = FXCollections.observableArrayList();
				for (int i = 7; i + 6 < str.length; i += 7) {
					ParkingRow row = new ParkingRow(str[i], str[i + 1], str[i + 2], str[i + 3], str[i + 4], str[i + 5],
							str[i + 6]);
					items.add(row);
				}
				table.setItems(items);
			});
		} else {
			dbDisplay.setText(message);

		}
	}

	/**
	 * Receives messages from the server and displays them in the appropriate UI
	 * section.
	 * 
	 * @param message the message received from the server
	 */
	@Override
	public void handleMessageFromServer(String message) {
		displayMessage(message);
	}

}
