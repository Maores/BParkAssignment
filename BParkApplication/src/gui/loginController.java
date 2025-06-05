package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import db.DBController;
// import gui.MainApp;

public class loginController {

	@FXML
	private TextField id;

	@FXML
	private Button login;

	@FXML
	private TextField name;

	private MainApp mainApp;

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	void loginUser(ActionEvent event) {
		String userId = id.getText();
		String userName = name.getText();
		if (userId == null || userId.isEmpty() || userName == null || userName.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter both ID and Name.");
			alert.showAndWait();
			return;
		}
		DBController db = DBController.getInstance(null);
		db.connectToDB();
		String role = db.getUserRoleById(userId);
		if (role == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "User not found. Please try again.");
			alert.showAndWait();
			id.clear();
			name.clear();
			return;
		}
		if (mainApp != null) {
			try {
				mainApp.showRoleScreen(role, userId, userName);
			} catch (Exception e) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load role screen: " + e.getMessage());
				alert.showAndWait();
			}
		}
	}
}