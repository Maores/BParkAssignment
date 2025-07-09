package gui;

import client.ChatClient;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ShowCodeScreenDeliverController {
	
	private ChatClient client;
	private Stage window;
	
	public ShowCodeScreenDeliverController(Stage window,ChatClient client) {
		this.client = client;
		this.window = window;
	}

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
