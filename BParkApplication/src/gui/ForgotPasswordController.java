package gui;

import java.io.IOException;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for the "Forgot Password" popup screen.
 * <p>
 * Handles user input to submit an email address for password reset,
 * validates the input, and communicates with the server to trigger
 * the password reset process.
 * </p>
 */
public class ForgotPasswordController implements ChatIF {

	 /** Reference to the popup window stage. */
	private Stage popWindow;
	/** Singleton client instance used for communication with the server. */
	private ChatClient client = (new singletoneClient()).getInstance(this);
	/**
     * Sets the pop-up window stage reference.
     * @param popWindow the stage representing the forgot password window
     */
	public void setPopWindow(Stage popWindow) {
		this.popWindow = popWindow;
	}

	/** Text field where the user enters their email address. */
	@FXML
	private TextField emailField;

	 /**
     * Handles the submit button action.
     * <p>
     * Validates the email address and, if valid, sends a password reset
     * request to the server.
     * </p>
     *
     * @throws IOException if there's an error sending data to the server
     */
	@FXML
	private void handleSubmit() throws IOException {
		String email = emailField.getText();
		if (email == null || email.trim().isEmpty()) {
			showAlert(AlertType.WARNING, "Please enter an email address.");
			return;
		}

		if (!isValidEmail(email)) {
			showAlert(AlertType.ERROR, "Invalid email format. Please enter a valid email address.");
			return;
		}
		showAlert(AlertType.INFORMATION, "A password reset email has been sent to your address.");
		client.sendToServer("RESET_PASSWORD " + email);
//        if (emailExists) {
//            // Simulate sending reset email
//            System.out.println("Password reset email sent to: " + email);
//            
//            
//        } else {
//            showAlert(AlertType.ERROR, "Email address not found. Please register first.");
//        }
	}
	
    /**
     * Validates the format of the given email address using a regular expression.
     *
     * @param email the email address to validate
     * @return true if the email format is valid, false otherwise
     */
	private boolean isValidEmail(String email) {
		return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
	}

    /**
     * Displays an alert dialog with the given type and message.
     *
     * @param type    the type of alert (e.g., ERROR, INFORMATION)
     * @param message the message to display
     */
	private void showAlert(AlertType type, String message) {
		Alert alert = new Alert(type, message);
		alert.showAndWait();
	}

    /**
     * Handles messages received from the server.
     * <p>
     * If the message indicates a successful email process, an information
     * alert is shown and the popup window is closed. If the message indicates
     * failure, an error alert is shown.
     * </p>
     *
     * @param message the message received from the server
     */
	@Override
	public void handleMessageFromServer(String message) {
		if(message.startsWith("Email")) {
			showAlert(AlertType.INFORMATION, message);
			popWindow.close();
		}
		else if(message.startsWith("Failed")) {
			showAlert(AlertType.ERROR, message);
		}
		popWindow.close();
	}
}
