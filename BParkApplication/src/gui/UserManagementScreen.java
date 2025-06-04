package gui;

import db.DBController;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import server.EchoServer;

/**
 * Example screen showing how to access the database after the GUI/DB
 * separation. This screen doesn't need to receive database notifications, so it
 * passes null as listener.
 */
public class UserManagementScreen implements Initializable {

	@FXML
	private TextField userIdField;
	@FXML
	private Label resultLabel;
	@FXML
	private TextArea logArea;
	@FXML
	private TableView<ParkingRow> table;
	private EchoServer server;

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

		// Get DB instance without listener (null)
		// This screen doesn't need to receive DB notifications
		DBController db = DBController.getInstance(null);
		try {
			server = EchoServer.getServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logArea.appendText("Searching for user: " + userId + "\n");

		// Use DB methods normally
		String userRole = db.getUserRoleById(userId);

		if (userRole != null) {
			resultLabel.setText("User found! Role: " + userRole);
			logArea.appendText("User role: " + userRole + "\n");
		} else {
			resultLabel.setText("User not found");
			logArea.appendText("No user found with ID: " + userId + "\n");
		}
	}

	/**
	 * Check if an order exists Another example of DB access
	 */
	@FXML
	void viewOrder() {
		String orderId = userIdField.getText();

		// Access DB singleton
		DBController db = DBController.getInstance(null);

		String DataBaseAsString = db.getDatabaseAsString();
		Platform.runLater(() -> {
			String[] str = DataBaseAsString.split(" ");
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
				ParkingRow row = new ParkingRow(str[i], str[i + 1], str[i + 2], str[i + 3], str[i + 4], str[i + 5]);
				items.add(row);
			}

			table.setItems(items);
		});
	}

	/**
	 * Example of a more complex operation
	 */
	@FXML
	void generateReport() {
		DBController db = DBController.getInstance(null);

		logArea.appendText("Generating report...\n");

		// Get all database data
		String allData = db.getDatabaseAsString();

		// Process the data (count orders, etc.)
		String[] lines = allData.split("\n");
		int orderCount = lines.length - 1; // Minus header

		logArea.appendText("Total orders in database: " + orderCount + "\n");
		logArea.appendText("Report generated successfully\n");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Any initialization if needed
	}
}