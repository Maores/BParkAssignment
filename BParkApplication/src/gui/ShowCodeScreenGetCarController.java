package gui;

import client.ChatClient;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ShowCodeScreenGetCarController {
	private ChatClient client;
	private Stage window;
	
	public ShowCodeScreenGetCarController(Stage window,ChatClient client) {
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
	    			client.handleMessageFromClientUI("CAR_GET " + code.getText());
	    			window.close();
	    		}
	    	});
			VBox root = new VBox(new Label("Scan your tag or Enter confirmation code:"),code,codeBtn);
			root.setAlignment(Pos.CENTER);
			return root;
	}

}
