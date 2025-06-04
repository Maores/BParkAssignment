package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
		// Add your login validation logic here
		if (mainApp != null) {
			try {
				mainApp.showMainScreen();
			} catch (Exception e) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load main screen: " + e.getMessage());
				alert.showAndWait();
			}
		}
	}
}