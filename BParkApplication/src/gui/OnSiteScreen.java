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
	
    private Stage primaryStage;
    private MainApp mainApp;
    private Stage checkCode;

    private ChatClient client;
	private singletoneClient sg = new singletoneClient();

	public OnSiteScreen() {
		client = sg.getInstance(this);
	}
	 
    @FXML
    void Delivery(ActionEvent event) {
    	checkCode = new Stage();
    	ShowCodeScreenDeliverController obj = new ShowCodeScreenDeliverController(checkCode,client);
    	Parent root = obj.buildRoot();
		checkCode.setScene(new Scene(root,300, 100));
		checkCode.setAlwaysOnTop(true);
		checkCode.setResizable(false);
		checkCode.setTitle("Enter Code");
		checkCode.show();
    }

   
	@FXML
    void GetCar(ActionEvent event) {
    	checkCode = new Stage();
    	ShowCodeScreenGetCarController obj = new ShowCodeScreenGetCarController(checkCode,client);
    	Parent root = obj.buildRoot();
		checkCode.setScene(new Scene(root,300, 100));
		checkCode.setAlwaysOnTop(true);
		checkCode.setResizable(false);
		checkCode.setTitle("Enter Code");
		checkCode.show();
    	
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
    	if(message.equals("CAR_INSERTED")) {
    		Terminal.setText("Please place the car on the carrier. Have a great day!\n");
    	}else if(message.equals("CAR_NOT_INSERTED")) {
    		Terminal.appendText("Oops! It looks like the code is incorrect, or the order isn’t scheduled for this time.\n");
    	}else if(message.equals("CAR_GET_FAIELD")) {
    		Terminal.appendText("Oops! That code doesn’t seem to work. Please reach out to our staff for help.\n");
    	}else if(message.equals("CAR_GET_SUCCES")) {
    		Terminal.setText("Your car is on its way — this should take no more than 3 minutes.\n");
    	}
    	
	}
}
