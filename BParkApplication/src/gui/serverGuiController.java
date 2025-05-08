package gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.EchoServer;

public class serverGuiController extends Application{

	@FXML
	private Button btnLstn;

	@FXML
	private TextField txtPort;
	
	@FXML
	private TextArea txtArea;
	
	private static String console="";
	@FXML
	void listen(ActionEvent event) {
		String p;

		p = getport();
		if (p.trim().isEmpty()) {
			System.out.println("You must enter a port number");

		} else {
			runServer(p);
		}
	}
	public  void runServer(String p)
	{
		int port = 0; //Port to listen on

        try
        {
        		port = Integer.parseInt(p); //Set port to 5555
          
        }
        catch(Throwable t)
        {
        		System.out.println("ERROR - Could not connect!");
        }
    	
        EchoServer sv = new EchoServer(port,this);
        
        try 
        {
          sv.listen(); //Start listening for connections

        } 
        catch (Exception ex) 
        {
          System.out.println("ERROR - Could not listen for clients!");

        }
        	
		
	}
	public void message(String msg) {
		String str = txtArea.getText() +"\n" +msg;
		System.out.println("pass");
		txtArea.setText(str);
	}
	private String getport() {
		return txtPort.getText();
	}

	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/serverGui.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();	

	}
	
	public void Initializable() {
		console = txtArea.getText();
	}
	public static void main( String args[] ) throws Exception
	   {   
		 launch(args);
	  }
	public void appendMessage(String msg) {
		javafx.application.Platform.runLater(() -> {
	        txtArea.appendText(msg + "\n");
	    });
	} 
}
