package client.controller;

import client.gui.MainApp;
import client.network.ChatClient;
import client.network.singletoneClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.ChatIF;

/**
 * Controller for the user information update screen.
 * <p>
 * Allows users to update their phone number and/or email address. Retrieves
 * current user information and communicates changes to the server.
 * </p>
 */
public class UserUpdate implements ChatIF {
	@FXML
	private Button cancelBtn;
	@FXML
	private TextField email;
	@FXML
	public TextField name;
	@FXML
	private TextField phone;
	@FXML
	private Button update;
	private String id = MainApp.getUserId();

	private ChatClient client = (new singletoneClient()).getInstance(this);
	private Stage popWindow;

	/**
	 * Default constructor. Requests current user information from the server.
	 */
	public UserUpdate() {
		client.handleMessageFromClientUI("GET_USER_INFO " + id);
	}

	/**
	 * Sets the reference to the pop-up window used to display this form.
	 * 
	 * @param popWindow the Stage instance representing the pop-up window
	 */
	public void setPopWindow(Stage popWindow) {
		this.popWindow = popWindow;
	}

	/**
	 * Closes the update form pop-up when the cancel button is clicked.
	 * 
	 * @param event the action event triggered by the cancel button
	 */
	@FXML
	void cancelUser(ActionEvent event) {
		if (popWindow != null) {
			popWindow.close();
		}
	}

	/**
	 * Sends an update request to the server with the new email and/or phone number.
	 * If both fields are empty, shows a warning alert.
	 * 
	 * @param event the action event triggered by the update button
	 */
	@FXML
	void updateUser(ActionEvent event) {
		if (email.getText().isEmpty() && phone.getText().isEmpty()) {
			Alert a = new Alert(Alert.AlertType.WARNING, "At least one field\n need to be filled.");
			a.show();
		} else {
			client.handleMessageFromClientUI("UPDATE_USER_INFO " + phone.getText() + " " + email.getText() + " " + id);
		}
	}

	/**
	 * Handles messages received from the server. Populates fields with current user
	 * data or shows alerts based on the response.
	 * 
	 * @param message the message received from the server
	 */
	@Override
	public void handleMessageFromServer(String message) {
		Platform.runLater(() -> {
			if (message.equals("ERROR_UPDATE_USER")) {
				Alert al = new Alert(Alert.AlertType.ERROR);
				al.show();
			} else if (message.startsWith("INFO")) {
				String[] parts = message.split(" ");

				if (parts.length >= 3) { // "INFO <phone> <email>"
					String phoneStr = parts[1];
					String emailStr = parts[2];
					phone.setText(phoneStr);
					email.setText(emailStr);
				}

			} else {
				Alert al = new Alert(Alert.AlertType.INFORMATION, message);
				al.show();
				popWindow.close();

			}

		});
	}

}
