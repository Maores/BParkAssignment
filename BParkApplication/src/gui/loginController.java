package gui;

import java.io.IOException;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller class for the login screen.
 * <p>
 * Handles user authentication, forgot password functionality, QR code login
 * view, and availability check for guest users.
 * </p>
 */
public class loginController implements ChatIF {

	@FXML
	private TextField password;

	@FXML
	private Button login;

	@FXML
	private TextField name;

	@FXML
	private Button fgBtn;

	@FXML
	private DatePicker date;

	@FXML
	private Button checkAvailBtn;

	private singletoneClient sg = new singletoneClient();
	/** Singleton wrapper to access the ChatClient instance. */
	private static ChatClient client;
	/** Shared ChatClient instance for communication with the server. */

	private MainApp mainApp; // Reference to the main application instance

	// Constructs the controller and initializes the ChatClient instance.
	public loginController() {
		client = sg.getInstance(this);
	}

	/**
	 * Sets the reference to the main application, allowing this controller to
	 * switch scenes.
	 * 
	 * @param mainApp the main application instance
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	}

	/**
	 * Handles the login button action. Validates user input and sends login
	 * credentials to the server.
	 *
	 * @param event the ActionEvent triggered by the button
	 */
	@FXML
	void loginUser(ActionEvent event) {
		String userPass = password.getText();
		String userName = name.getText();
		if (userPass == null || userPass.isEmpty() || userName == null || userName.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter both Name and Password.");
			alert.showAndWait();
			return;
		}
		try {
			client = sg.getInstance(this);
			client.handleMessageFromClientUI("LOGIN " + userPass + " " + userName);

		} catch (Exception e) {
		} // Silent fail – could log
	}

	/**
	 * Returns the ChatClient instance.
	 * 
	 * @return the ChatClient used by this controller
	 */
	public ChatClient getClient() {
		return client;
	}

	/**
	 * Handles messages received from the server. Responds to login result and
	 * navigates to the appropriate role screen.
	 * 
	 * @param message the message received from the server
	 */
	@Override
	public void handleMessageFromServer(String message) {
		if (message.startsWith("role ")) {
			String[] parts = message.split(" ");
			String role = parts[1];
			String id = parts[2];
			if (mainApp != null) {
				try {
					if (client != null) {
						client.handleMessageFromClientUI("CONNECTION " + client.getHost() + " " + role);
					}
					mainApp.showRoleScreen(role, id, name.getText());
				} catch (Exception e) {
					Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load role screen: " + e.getMessage());
					alert.showAndWait();
				}
			}
		} else if (message.startsWith("No")) {
			Platform.runLater(() -> {
				Alert alert = new Alert(Alert.AlertType.ERROR, message);
				alert.showAndWait();
			});
		} else {
			Platform.runLater(() -> {
				Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
				alert.showAndWait();
			});
		}
	}

	// Opens the Forgot Password pop-up window and loads its controller.
	@FXML
	private void forgotPass() {
		Stage forgotPasswordStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ForgotPassword.fxml"));
		Parent pop;
		try {
			pop = loader.load();
			Scene s = new Scene(pop, 300, 150);
			s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
			ForgotPasswordController controller = loader.getController();
			controller.setPopWindow(forgotPasswordStage);
			forgotPasswordStage.setScene(s);
			forgotPasswordStage.setResizable(false);
			forgotPasswordStage.setTitle("Reset Password");
			forgotPasswordStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Displays a popup window with a QR code for alternative login method.
	 */
	@FXML
	public void showQR() {
		Stage qrStage = new Stage();
		StackPane root = new StackPane();
		ImageView view = new ImageView();
		Image image = new Image(getClass().getResource("/gui/QR_code.png").toExternalForm());
		view.setImage(image);
		view.setFitWidth(150);
		view.setFitHeight(150);
		view.setPreserveRatio(true);
		VBox inside = new VBox(new Label("Scan QR to login:"), view);
		inside.setAlignment(Pos.CENTER);
		root.getChildren().add(inside);
		qrStage.setScene(new Scene(root, 200, 200));
		qrStage.setAlwaysOnTop(true);
		qrStage.setResizable(false);
		qrStage.setTitle("Login via QR code");
		qrStage.show();
	}

	/**
	 * Sends a request to check parking availability for the selected date. Requires
	 * that a date is selected in the DatePicker.
	 */
	@FXML
	public void checkAvailability() {
		if (date.getValue() == null || date.getValue().toString().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Fill the date field.");
			alert.show();
		} else {
			client = sg.getInstance(this);
			if (client != null) {
				client.handleMessageFromClientUI("CONNECTION " + client.getHost() + " Guest");
			}
			client.handleMessageFromClientUI("AVAILABLE_SPOTS " + date.getValue().toString());
		}
	}

}