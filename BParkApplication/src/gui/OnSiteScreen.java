package gui;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OnSiteScreen implements ChatIF{

    public OnSiteScreen() {
    	client = sg.getInstance(this);
	}

	@FXML
    private Button OrdersMenu;

    @FXML
    private Button btnDeliverCar;

    @FXML
    private Button btnGetCar;
    @FXML
    private TextArea Terminal;
    @FXML
    private StackPane pane;
    
	private ChatClient client;
	private singletoneClient sg = new singletoneClient();
	
    private Stage primaryStage;
    private MainApp mainApp;
    private String code;
    

    
    @FXML
    void Delivery(ActionEvent event) {
    	Terminal.appendText("Car is about to be taken by the lift, dont step over the line!\n");
    	client.handleMessageFromClientUI(
				"CAR_INSERT " + code);
    }

    @FXML
    void GetCar(ActionEvent event) {
    	Terminal.appendText("Car is being delivered, it may take up to 3 minutes..\n");
    }

    @FXML
    void Orders(ActionEvent event) {
    	RemoteScreen OffSite = new RemoteScreen();
    	OffSite.setMain(mainApp);
		Parent root = OffSite.buildRoot();
		Scene s = new Scene(root, 800, 600);
        s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
		Platform.runLater(() -> {
			primaryStage.setScene(s);
			primaryStage.setTitle("BPark - Customer: "+mainApp.getUserName());
			primaryStage.show();
		});
    }
    public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
    public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

    @SuppressWarnings("unchecked")
	public void displayMessage(String message) {
    	
    }
    
    @Override
	public void handleMessageFromServer(String message) {
		displayMessage(message);
	}
}
