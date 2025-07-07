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
import javafx.stage.Stage;

public class UserUpdate implements ChatIF {
	@FXML
	private Button cancel;
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

	public void setPopWindow(Stage popWindow) {
		this.popWindow = popWindow;
	}

	@FXML
	void cancelUser(ActionEvent event) {
		if (popWindow != null) {
			popWindow.close();
		}
	}

	@FXML
	void updateUser(ActionEvent event) {
		if (email.getText().isEmpty() && phone.getText().isEmpty()) {
			Alert a = new Alert(Alert.AlertType.WARNING, "At least one field\n need to be filled.");
			a.show();
		} else {
			client.handleMessageFromClientUI("UPDATE_USER_INFO "+phone.getText()+" "+email.getText()+" "+id);
		}
	}

	@Override
	public void handleMessageFromServer(String message) {
		Platform.runLater(() -> {
			if(message.equals("ERROR_UPDATE_USER")) {
				Alert al = new Alert(Alert.AlertType.ERROR);
				al.show();
			}else {
				Alert al = new Alert(Alert.AlertType.INFORMATION,message);
				al.show();
				popWindow.close();
				
			}
		});

	}
}
