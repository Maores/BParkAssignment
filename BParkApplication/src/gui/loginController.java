package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class loginController extends Application {

	@FXML
	private TextField id;

	@FXML
	private Button login;

	@FXML
	private TextField name;

	@FXML
	void loginUser(ActionEvent event) {
		ParkingSystemGUI system = new ParkingSystemGUI();
		Stage primaryStage = new Stage();
		system.start(primaryStage);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/userLogin.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("BPark system");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String args[]) throws Exception {
		launch(args);
	}
}