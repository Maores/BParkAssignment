package client;

import common.ChatIF;
import javafx.scene.control.Alert;

public class singletoneClient {
	final public static int DEFAULT_PORT = 5555;
	final public static String DEFAULT_HOST = "localhost";
	private ChatClient instance;

	public ChatClient getInstance(ChatIF clientUI) {
		if (instance == null) {
			try {
				instance = new ChatClient(DEFAULT_HOST, DEFAULT_PORT, clientUI);
			} catch (Exception e) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Server is OFFLINE!\n" + e.getMessage());
				alert.showAndWait();
			}
		} else {

			instance.setClientUI(clientUI);
		}
		return instance;
	}

	public ChatClient setManualConnection(int PORT, String HOST, ChatIF clientUI) {
		try {
			instance = new ChatClient(HOST, PORT, clientUI);
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Server is OFFLINE!\n" + e.getMessage());
			alert.showAndWait();
		}

		return instance;
	}
}
