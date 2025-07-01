package gui;

import java.io.IOException;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import common.MailSender;
import db.DBController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ForgotPasswordController implements ChatIF {

	private Stage popWindow;
	private ChatClient client = (new singletoneClient()).getInstance(this);

	public void setPopWindow(Stage popWindow) {
		this.popWindow = popWindow;
	}

	@FXML
	private TextField emailField;

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

	// Improved regex to validate email address format
	private boolean isValidEmail(String email) {
		return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
	}

	// Utility function to show alerts
	private void showAlert(AlertType type, String message) {
		Alert alert = new Alert(type, message);
		alert.showAndWait();
	}

	@Override
	public void handleMessageFromServer(String message) {
		if(message.startsWith("Email")) {
			showAlert(AlertType.INFORMATION, message);
		}
		else if(message.startsWith("Failed")) {
			showAlert(AlertType.ERROR, message);
		}
		popWindow.close();
	}
}
