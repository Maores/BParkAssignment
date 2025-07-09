package gui;

import client.ChatClient;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * GUI controller for retrieving a car by entering or scanning a confirmation
 * code.
 * <p>
 * This class handles user input and sends a "CAR_GET" command to the server via
 * the ChatClient.
 * </p>
 */
public class ShowCodeScreenGetCarController {
	/** The ChatClient used for communication with the server. */
	private ChatClient client;
	/** The stage (popup window) for code entry. */
	private Stage window;

	/**
	 * Constructs the controller with the given window and ChatClient.
	 *
	 * @param window the stage where the code input UI is displayed
	 * @param client the client used to send messages to the server
	 */
	public ShowCodeScreenGetCarController(Stage window, ChatClient client) {
		this.client = client;
		this.window = window;
	}

	/**
	 * Builds and returns the root layout containing the input field and check
	 * button.
	 * <p>
	 * The user is prompted to scan or enter a confirmation code, and upon clicking
	 * the button, the input is sent to the server. If the field is empty, an error
	 * alert is shown.
	 * </p>
	 *
	 * @return a VBox layout containing the label, input field, and button
	 */
	public VBox buildRoot() {
		TextField code = new TextField();
		Button codeBtn = new Button("check");
		codeBtn.setOnAction(e -> {
			if (code.getText().isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Fill the field!");
				Window window = alert.getDialogPane().getScene().getWindow();
				if (window instanceof Stage stage) {
					stage.setAlwaysOnTop(true);
				}
				alert.show();
			} else {
				client.handleMessageFromClientUI("CAR_GET " + code.getText());
				window.close();
			}
		});
		VBox root = new VBox(5, new Label("Scan your tag or Enter confirmation code:"), code, codeBtn);
		root.setAlignment(Pos.CENTER);
		return root;
	}

}
