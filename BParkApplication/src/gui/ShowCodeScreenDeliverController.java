package gui;

import client.ChatClient;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * GUI controller for entering a confirmation code
 * to deliver a car on-site in the BPark system.
 */

public class ShowCodeScreenDeliverController {
	
	private ChatClient client; //The client used to communicate with the server
	private Stage window; //The popup window (stage) for the code input
    /**
     * Constructs the controller for the code entry window.
     * @param window the stage to display
     * @param client the ChatClient instance used for communication
     */
	public ShowCodeScreenDeliverController(Stage window,ChatClient client) {
		this.client = client;
		this.window = window;
	}
    /**
     * Builds the root layout for the confirmation code input screen.
     * Includes a label, input field, and a button to submit the code.
     * @return a VBox layout containing the UI components
     */
	public VBox buildRoot() {
	    	TextField code = new TextField();
	    	Button codeBtn = new Button("check");
	    	codeBtn.setOnAction(e -> {
	    		if(code.getText().isEmpty()) {
	    			Alert alert = new Alert(Alert.AlertType.ERROR,"Fill the field!");
	    			alert.show();
	    		}
	    		else {
	    			client.handleMessageFromClientUI("CAR_INSERT " + code.getText());
	    			window.close();
	    		}
	    	});
			VBox root = new VBox(new Label("Enter confirmation code:"),code,codeBtn);
			root.setAlignment(Pos.CENTER);
			return root;
	}

}
