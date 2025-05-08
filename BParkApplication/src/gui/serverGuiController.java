package gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class serverGuiController {

	@FXML
	private Button btnLstn;

	@FXML
	private TextField txtPort;
	@FXML
	private TextField txtArea;
	
	private String console;
	@FXML
	void listen(ActionEvent event) {
		String p;

		p = getport();
		if (p.trim().isEmpty()) {
			System.out.println("You must enter a port number");

		} else {
			serverUI.runServer(p);
			console += "\n"+"Server listening for connections on port " + p;
			txtArea.setText(console);
		}
	}

	private String getport() {
		return txtPort.getText();
	}

	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/serverGui.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();	

	}

}
