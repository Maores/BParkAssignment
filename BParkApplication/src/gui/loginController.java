package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import gui.ClientSingleton;
import gui.GUIParkingClient;
// import gui.MainApp;

public class loginController implements common.ChatIF {

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
		GUIParkingClient client = ClientSingleton.getInstance(this);
		client.sendMessage("LOGIN " + userId);
	}

	@Override
	public void display(String message) {
		if (message.startsWith("role ")) {
			String role = message.substring(5);
			if (mainApp != null) {
				try {
					mainApp.showRoleScreen(role, id.getText(), name.getText());
				} catch (Exception e) {
					Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load role screen: " + e.getMessage());
					alert.showAndWait();
				}
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR, message);
			alert.showAndWait();
		}
	}
}