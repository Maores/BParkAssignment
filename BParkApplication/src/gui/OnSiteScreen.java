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
/**
 * Controller class for the On-Site screen in the BPark system.
 * <p>
 * Handles user interaction for delivering and retrieving cars on site,
 * as well as navigating to the off-site orders screen.
 * Communicates with the server using {@link ChatClient}.
 * </p>
 */
public class OnSiteScreen implements ChatIF{

	@FXML
    private Button OrdersMenu; //Button for navigating to the Orders (off-site) screen

    @FXML
    private Button btnDeliverCar; //Button for initiating car delivery process

    @FXML
    private Button btnGetCar; //Button for initiating car retrieval process
    @FXML
    private TextArea Terminal; //Text area displaying terminal messages and feedback to the user
    @FXML
    private StackPane pane; //StackPane layout container for the screen
	
    private Stage primaryStage; //Primary stage of the application window
    private MainApp mainApp; //Reference to the main application instance
    private Stage checkCode; //Secondary stage used for entering codes

    private ChatClient client; //Chat client instance used for server communication
	private singletoneClient sg = new singletoneClient(); //Singleton wrapper for managing the ChatClient instance

	//Constructor initializes the ChatClient instance using the singleton wrapper.
	public OnSiteScreen() {
		client = sg.getInstance(this);
	}
    /**
     * Handles the delivery button action.
     * Opens a pop-up window for the user to enter the delivery code.
     *
     * @param event the action event triggered by the delivery button
     */
    @FXML
    void Delivery(ActionEvent event) {
        	ShowCodeScreen();
    }

    /**
     * Displays the popup window for entering the delivery code.
     */
    void ShowCodeScreen() {
    	checkCode = new Stage();
    	ShowCodeScreenDeliverController obj = new ShowCodeScreenDeliverController(checkCode,client);
    	Parent root = obj.buildRoot();
		checkCode.setScene(new Scene(root,300, 100));
		checkCode.setAlwaysOnTop(true);
		checkCode.setResizable(false);
		checkCode.setTitle("Enter Code");
		checkCode.show();
    }

    /**
     * Handles the get car button action.
     * Opens a popup window for the user to enter the retrieval code.
     *
     * @param event the action event triggered by the get car button
     */
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
    /**
     * Handles the orders button action.
     * Navigates the user to the remote/off-site orders screen.
     *
     * @param event the action event triggered by the orders button
     */
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
    /**
     * Sets the primary stage for this screen.
     *
     * @param primaryStage the main application window
     */
    public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
    /**
     * Sets the reference to the main application instance.
     *
     * @param mainApp the main application controller
     */
    public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}


    /**
     * Handles incoming messages from the server.
     * Appends relevant messages to the terminal display.
     *
     * @param message the message received from the server
     */
    @Override
	public void handleMessageFromServer(String message) {
    	if(message.equals("CAR_INSERTED")) {
    		Terminal.appendText("Car is being delivered, it may take up to 3 minutes..\n");
    	}
    	if(message.equals("CAR_NOT_INSERTED")) {
    		Terminal.appendText("Car is being delivered, it may take up to 3 minutes..\n");
    	}
    	
	}
}
