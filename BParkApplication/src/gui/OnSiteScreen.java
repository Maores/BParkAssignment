package gui;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private Stage checkCode;
    private String code;
    

    
    @FXML
    void Delivery(ActionEvent event) {
        	ShowCodeScreen();
    }
    void ShowCodeScreen() {
    	ShowCodeScreenController obj = new ShowCodeScreenController();
    	Parent root = obj.buildRoot();
    	checkCode = new Stage();
		checkCode.setScene(new Scene(root,300, 100));
		checkCode.setAlwaysOnTop(true);
		checkCode.setResizable(false);
		checkCode.setTitle("Enter Code");
		checkCode.show();
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


    
    @Override
	public void handleMessageFromServer(String message) {
	}
}
