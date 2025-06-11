package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class loginAccess  {

	@FXML
	private Button onsite;

	@FXML
	private Button remote;

	private Stage primaryStage;

	private MainApp mainApp;
	
	@FXML
	void loadOnSiteScreen(ActionEvent event) {
		
	}
	

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;;
	}
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}


	@FXML
	void loadRemoteScreen(ActionEvent event) {
		ParkingSystemGUI parkingSystemGUI = new ParkingSystemGUI();
		Parent root = parkingSystemGUI.buildRoot();
		Platform.runLater(() -> {
			primaryStage.setScene(new Scene(root, 700, 550));
			primaryStage.setTitle("BPark - Customer");
			primaryStage.show();
		});
	}


}
