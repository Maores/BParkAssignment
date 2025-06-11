package gui;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 * Example screen showing how to access the database after the GUI/DB
 * separation. This screen doesn't need to receive database notifications, so it
 * passes null as listener.
 */
public class UserManagementScreen implements ChatIF {

	@FXML
	private TextField userIdField;
	@FXML
	private Label resultLabel;
	@FXML
	private TextArea logArea;

	private singletoneClient sg = new singletoneClient();
	private ChatClient client;

	private TableView<ParkingRow> table = new TableView<>();

	public UserManagementScreen() {
		client = sg.getInstance(this);
	}

	/**
	 * Search for a user by ID Demonstrates accessing DB without being a listener
	 */
	@FXML
	void searchUser() {
		String userId = userIdField.getText();

		if (userId.isEmpty()) {
			resultLabel.setText("Please enter a user ID");
			return;
		}

		client.handleMessageFromClientUI("SEARCH_ORDER " + userId);
		logArea.appendText("Searching for user: " + userId + "\n");
	}

	/**
	 * Example of a more complex operation
	 */
	@FXML
	void generateReport() {
		client.handleMessageFromClientUI("GENERATE_REPORT DAILY_REPORT");
		logArea.appendText("Generating report...\n");
	}

	// Get message from the server
	@Override
	public void display(String message) {
		displayMessage(message);
	}

	private void displayMessage(String message) {
		Platform.runLater(() -> {

			if (message.startsWith("User found!")) {
				resultLabel.setText(message);
			} else if (message.startsWith("User not found")) {
				resultLabel.setText(message);
			} else if (message.startsWith("Order found:")) {
				resultLabel.setText("Order found");
			} else if (message.startsWith("Order") && message.contains("does not exist")) {
				resultLabel.setText(message);
			} else if (message.startsWith("=== DAILY PARKING REPORT ===")) {
				resultLabel.setText("Report generated");
			} else if (message.startsWith("order_number")) {
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
				logArea.appendText("Completed!"+ "\n");
			}
			else {
				logArea.appendText(message+"\n");
			}
		});

	}

	public VBox buildRoot() {
		userIdField = new TextField();
		resultLabel = new Label();
		logArea = new TextArea();
		logArea.setPrefHeight(200);
		logArea.setEditable(false);
		Button searchBtn = new Button("Search order");
		searchBtn.setOnAction(e -> searchUser());
		Button checkOrderBtn = new Button("View orders");
		checkOrderBtn.setOnAction(e -> {
			logArea.appendText("Showing orders..."+ "\n");
			client.handleMessageFromClientUI("VIEW_DATABASE");
			});
		Button reportBtn = new Button("Generate Report");
		reportBtn.setOnAction(e -> generateReport());
		VBox root = new VBox(10, new Label("Order number:"), userIdField, searchBtn, checkOrderBtn, reportBtn,
				resultLabel, logArea, table);
		root.setPrefWidth(400);
		root.setPrefHeight(350);
		return root;
	}
}