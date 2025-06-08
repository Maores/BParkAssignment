package gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.EchoServer;

public class serverGuiController extends Application {

	@FXML
	private Button btnLstn;
	@FXML
	private Button btnStop;
	@FXML
	private TextField txtPort;

	@FXML
	private TextArea txtArea;

	private EchoServer sv;

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

	public void runServer(String p) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(p); // Set port to 5555

		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
			txtArea.appendText("ERROR - Could not connect!\n");
		}

		sv = new EchoServer(port, this);

		try {
			sv.listen(); // Start listening for connections

		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
			txtArea.appendText("ERROR - Could not listen for clients!\n");
		}

	}

	private String getport() {
		return txtPort.getText();
	}

	@FXML
	public void closeServer() {
		if (sv != null) {
			try {
				sv.close();
			} catch (IOException e) {
				 txtArea.appendText("Error closing server: " + e.getMessage() + "\n");
				e.printStackTrace();
			}
		}
		else {
			txtArea.appendText("Server is not running.\n");
		}
	}

	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/serverGui.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		serverGuiController controller = loader.getController();
		primaryStage.setTitle("BPark - Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(event -> {
			controller.closeServer();
		});
	}

	public static void main(String args[]) throws Exception {
		launch(args);
	}

	public void appendMessage(String msg) {
		javafx.application.Platform.runLater(() -> {
			txtArea.appendText(msg + "\n");
		});
	}
}
