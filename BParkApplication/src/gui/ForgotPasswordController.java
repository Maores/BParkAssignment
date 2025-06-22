package gui;

import db.DBController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ForgotPasswordController {

	private Stage popWindow;
	
	public void setPopWindow(Stage popWindow) {
		this.popWindow = popWindow;
	}
	
    @FXML
    private TextField emailField;

    @FXML
    private void handleSubmit() {
        String email = emailField.getText();
        if (email == null || email.trim().isEmpty()) {
            showAlert(AlertType.WARNING, "Please enter an email address.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(AlertType.ERROR, "Invalid email format. Please enter a valid email address.");
            return;
        }

        // Check if email exists in the users table using DBController
        DBController db = DBController.getInstance(null);  // pass listener if needed
        boolean emailExists = db.emailExists(email);

        if (emailExists) {
            // Simulate sending reset email
            System.out.println("Password reset email sent to: " + email);
            showAlert(AlertType.INFORMATION, "A password reset email has been sent to your address.");
        } else {
            showAlert(AlertType.ERROR, "Email address not found. Please register first.");
        }
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
}
