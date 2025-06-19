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
	    Scene s = new Scene(root, 500, 300);
        s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
	    Platform.runLater(() -> {
	        primaryStage.setScene(s);
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
		Scene s = new Scene(root, 700, 600);
        s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());

		Platform.runLater(() -> {
			primaryStage.setScene(s);
			primaryStage.setTitle("BPark - Customer: " + mainApp.getUserName());
			primaryStage.show();
		});
	}


}
