package gui;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
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
 * GUI controller for the User Management screen in the BPark system.
 * Enables searching users or orders, viewing tables, and generating reports,
 * by communicating with the server through the ChatClient interface.
 */
public class UserManagementScreen implements ChatIF {

	@FXML
	private TextField orderF;
	private TextField userF;
	@FXML
	private TextArea logArea;
	
	private singletoneClient sg = new singletoneClient();
	private ChatClient client;

	private TableView<ParkingRow> table = new TableView<>();
	private Dialog<Void> dialog;
	private MainApp main;

	   /**
     * Constructor initializes the ChatClient instance with this screen as listener.
     */
	public UserManagementScreen() {
		client = sg.getInstance(this);
	}
    /**
     * Sets the main application instance for scene management.
     * @param main the MainApp instance
     */
	public void setMain(MainApp main) {
		this.main = main;
	}

    /**
     * Sends a request to search for a specific order.
     * Validates input and updates the UI log area.
     */
	void searchOrder() {
		String order = orderF.getText();

		if (order.isEmpty()) {
			logArea.setText("Please enter order number..\n");
			return;
		}

		client.handleMessageFromClientUI("SEARCH_ORDER " + order);
		logArea.appendText("Searching for order: " + order + "\n");
	}

    /**
     * Sends a request to search for a user by ID.
     * Validates input and updates the UI log area.
     */
	@FXML
	void searchUser() {
		String userId = userF.getText();

		if (userId.isEmpty()) {
			logArea.setText("Please enter a User ID..\n");
			return;
		}

		client.handleMessageFromClientUI("SEARCH_USER " + userId);
		logArea.appendText("Searching for user: " + userId + "\n");
	}
    /**
     * Opens a new window to display the Subscriber Status Report.
     */
	private void openSubscriberStatusReport() {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ReportScreen.fxml"));
	        Parent root = loader.load();
	        ReportController controller = loader.getController();
	        controller.setChatClient(client);
	    	client.setClientUI(controller);
	        controller.setDialog(dialog);
	        Stage stage = new Stage();
	        controller.setStage(stage);
	        stage.setTitle("Subscriber Status Report");
	        stage.setScene(new Scene(root));
	        stage.setResizable(false);
	        stage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

    /**
     * Opens a new window to display the Parking Duration Report.
     */
	private void openParkingDurationReport() {
		  try {
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/parking_timing_report.fxml"));
		        Parent root = loader.load();
		        ParkingTimingReportController controller = loader.getController();
		        controller.setDialog(dialog);
		        controller.setChatClient(client); 
		        client.setClientUI(controller);
		        Stage stage = new Stage();
		        controller.setStage(stage);
		        stage.setTitle("Subscriber Parking Duration Report");
		        stage.setScene(new Scene(root));
		        stage.setResizable(false);
		        stage.show();

		    } catch (Exception e) {
		        e.printStackTrace();
		    }
	}
	 /**
     * Prompts the user with a dialog to select between different types of reports.
     */
	@FXML
	private void generateReport() {
	    dialog = new Dialog<>();
	    dialog.setTitle("Select Report Type");
	    dialog.setHeaderText("Please choose a report to view:");
	    ButtonType usageButtonType = new ButtonType("Parking Duration Report");
	    ButtonType statusButtonType = new ButtonType("Subscriber Status Report");
	    ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

	    dialog.getDialogPane().getButtonTypes().addAll(usageButtonType, statusButtonType, cancelButtonType);

	    dialog.setResultConverter(dialogButton -> {
	        if (dialogButton == usageButtonType) {
	            openParkingDurationReport(); 
	        } else if (dialogButton == statusButtonType) {
	            openSubscriberStatusReport();
	        }
	        return null;
	    });

	    dialog.showAndWait();
	}
	
	
    /**
     * Handles messages received from the server.
     * @param message the message received
     */

	@Override
	public void handleMessageFromServer(String message) {
		displayMessage(message);
	}
    /**
     * Updates the UI based on the server message, populating tables or showing logs.
     * @param message the server message
     */
	private void displayMessage(String message) {
		Platform.runLater(() -> {

			if (message.startsWith("User found!")) {
				logArea.appendText(message);
			} else if (message.startsWith("User not found")) {
				logArea.appendText(message);
			} else if (message.startsWith("Order found:")) {
				logArea.appendText("Order found");
			} else if (message.startsWith("Order") && message.contains("does not exist")) {
				logArea.appendText(message);
			} else if (message.startsWith("=== DAILY PARKING REPORT ===")) {
				logArea.appendText("Report generated");
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
				logArea.appendText("Completed!" + "\n");
			} else if (message.startsWith("id")) {
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
						ParkingRow row = new ParkingRow(str[i], str[i + 1], str[i + 2], str[i + 3], str[i + 4], str[i + 5]);
						items.add(row);
					}

					table.setItems(items);
				});
				logArea.appendText("Completed!" + "\n");
			} else {
				logArea.appendText(message + "\n");
			}
		});

	}
    /**
     * Constructs and returns the root layout of the user management screen.
     * @return the root StackPane layout
     */
	public StackPane buildRoot() {
		orderF = new TextField();
		userF = new TextField();
		StackPane root = new StackPane();
		root.setId("pane");
		logArea = new TextArea();
		logArea.setPrefHeight(300);
		logArea.setEditable(false);
		Button searchBtn = new Button("Search order");
		searchBtn.setOnAction(e -> searchOrder());
		Button searchUserBtn = new Button("Search User");
		searchUserBtn.setOnAction(e -> searchUser());
		VBox userS = new VBox(10, new Label("User ID:"), userF);
		VBox orderS = new VBox(10, new Label("Order number:"), orderF);
		HBox searchBox = new HBox(orderS, userS);
		searchBox.setSpacing(5);
		Button viewOrderBtn = new Button("View orders");
		viewOrderBtn.setOnAction(e -> {
			logArea.setText("Showing orders..." + "\n");
			client.handleMessageFromClientUI("VIEW_DATABASE");
		});
		Button viewUserBtn = new Button("View users");
		viewUserBtn.setOnAction(e -> {
			logArea.setText("Showing users..." + "\n");
			client.handleMessageFromClientUI("VIEW_USERDATABASE");
		});
		Button reportBtn = new Button("Generate Report");
		reportBtn.setOnAction(e -> generateReport());
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
		Region spacer = new Region();                 // flexible blank area
		HBox.setHgrow(spacer, Priority.ALWAYS);       // let it absorb all free width

		HBox headerBar = new HBox(searchBox, spacer, logOutBtn);
		headerBar.setAlignment(Pos.TOP_LEFT);         // keeps everything on one line
		headerBar.setSpacing(10);

		HBox btns = new HBox(viewOrderBtn,viewUserBtn, searchBtn, searchUserBtn, reportBtn);
		btns.setSpacing(10);
		VBox Interior = new VBox(10, headerBar, btns, logArea, table);
		root.setMargin(Interior, new Insets(15));
		root.getChildren().add(Interior);
		root.setPrefWidth(400);
		root.setPrefHeight(350);
		return root;
	}
}