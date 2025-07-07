package gui;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ShowCodeScreenController implements ChatIF {
	
	private ChatClient client;
	private singletoneClient sg = new singletoneClient();
	
	public ShowCodeScreenController() {
		client = sg.getInstance(this);
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
	    			client.handleMessageFromClientUI("CAR_INSERT" + " " + code.getText());
	    		}
	    	});
			VBox root = new VBox(new Label("Enter confirmation code:"),code,codeBtn);
			root.setAlignment(Pos.CENTER);
			return root;
	}

	@Override
	public void handleMessageFromServer(String message) {
		Platform.runLater(() -> {
			Alert alert = null;
	        if(message.equals("CAR_INSERTED")) {
	            alert = new Alert(Alert.AlertType.INFORMATION,"Car inserted successfully!");
	            alert.show();
	        }else if(message.equals("CAR_NOT_INSERTED")) {
	            alert = new Alert(Alert.AlertType.ERROR,"Failed Code is wrong!");
	            alert.show();
	        }
		});	
	}
}
