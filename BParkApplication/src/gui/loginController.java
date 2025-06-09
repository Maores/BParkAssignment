package gui;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class loginController implements ChatIF {

	@FXML
	private TextField id;

	@FXML
	private Button login;

	@FXML
	private TextField name;
	
	private singletoneClient sg = new singletoneClient();
	private static ChatClient client;

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
		try {
			client = sg.getInstance(this);
			client.handleMessageFromClientUI("LOGIN " + userId +" " + userName);
			
		} catch (Exception e) {
		}
	}

	public ChatClient getClient() {
		return client;
	}

	// Get message from the server!!!
	@Override
	public void display(String message) {
		if (message.startsWith("role ")) {
			String role = message.substring(5);
			if (mainApp != null) {
				try {
					if (client != null) {
						client.handleMessageFromClientUI("CONNECTION " + client.getHost() +" " + role);
					}
					mainApp.showRoleScreen(role, id.getText(), name.getText());					
				} catch (Exception e) {
					Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load role screen: " + e.getMessage());
					alert.showAndWait();
				}
			}
		}
		else {
			Platform.runLater(()-> {
				Alert alert = new Alert(Alert.AlertType.ERROR, message);
				alert.showAndWait();
			});
			
		}
	}
}