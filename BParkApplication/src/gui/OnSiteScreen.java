package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class OnSiteScreen {

    @FXML
    private Button OrdersMenu;

    @FXML
    private Button btnDeliverCar;

    @FXML
    private Button btnGetCar;
    @FXML
    private TextArea Terminal;
    
    private Stage primaryStage;
    
    @FXML
    void Delivery(ActionEvent event) {
    	Terminal.appendText("Car is about to be taken by the lift, dont step over the line!\n");
    }

    @FXML
    void GetCar(ActionEvent event) {
    	Terminal.appendText("Car is being delivered, it may take up to 3 minutes..\n");
    }

    @FXML
    void Orders(ActionEvent event) {
    	OffSiteScreen OffSite = new OffSiteScreen();
		Parent root = OffSite.buildRoot();
		Platform.runLater(() -> {
			primaryStage.setScene(new Scene(root, 700, 550));
			primaryStage.setTitle("BPark - Customer");
			primaryStage.show();
		});
    }
    public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;;
	}
}
