package gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserLoginAccess  {
	
	


	@FXML
	private Button onsite;

	@FXML
	private Button remote;

	private Stage primaryStage;

	private MainApp mainApp;
	

	
	@FXML
	void loadOnSiteScreen(ActionEvent event) throws IOException {
	   	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/OnSiteScreen.fxml")); 	 
	   	Parent root = loader.load();
	   	OnSiteScreen controller = loader.getController();
	    controller.setPrimaryStage(primaryStage);
	    Platform.runLater(() -> {
	        primaryStage.setScene(new Scene(root, 550, 350));
	        primaryStage.setTitle("BPark - On-Site");
	
	     });
    
	}
	

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;;
	}
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}


	@FXML
	void loadRemoteScreen(ActionEvent event) {
		RemoteScreen OffSite = new RemoteScreen();
		Parent root = OffSite.buildRoot();
		Platform.runLater(() -> {
			primaryStage.setScene(new Scene(root, 700, 550));
			primaryStage.setTitle("BPark - Customer");
			primaryStage.show();
		});
	}


}
