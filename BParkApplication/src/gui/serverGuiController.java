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
/**
 * JavaFX GUI controller for starting and stopping the BPark server.
 * <p>
 * Provides buttons to listen on a user-specified port and shut down the server,
 * as well as a text area to display server messages and status.
 * </p>
 */
public class serverGuiController extends Application {

	@FXML
	private Button btnLstn;
	@FXML
	private Button btnStop;
	@FXML
	private TextField txtPort;

	@FXML
	private TextArea txtArea;
	
	 /** The server instance being managed through the GUI. */
	private EchoServer sv;

    /**
     * Called when the "Listen" button is pressed.
     * Starts the server if a valid port is provided.
     *
     * @param event the action event from the button
     */
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
    /**
     * Initializes and starts the EchoServer on the given port.
     *
     * @param p the port number as a string
     */
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
    /**
     * Retrieves the port entered in the GUI text field.
     *
     * @return the port string entered by the user
     */
	private String getport() {
		return txtPort.getText();
	}
    /**
     * Called when the "Stop" button is pressed or window is closed.
     * Shuts down the server safely.
     */
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
    /**
     * JavaFX entry point to launch the server GUI.
     * @param primaryStage the main window stage
     * @throws IOException if the FXML loading fails
     */
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
    /**
     * Launches the JavaFX application.
     * @param args command-line arguments (not used)
     * @throws Exception if launch fails
     */
	public static void main(String args[]) throws Exception {
		launch(args);
	}

    /**
     * Appends a message to the GUI text area from the server.
     * @param msg the message to display
     */
	public void appendMessage(String msg) {
		javafx.application.Platform.runLater(() -> {
			txtArea.appendText(msg + "\n");
		});
	}
}
