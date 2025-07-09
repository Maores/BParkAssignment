package gui;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for the user login access screen that allows navigation between
 * on-site and remote user interfaces.
 */
public class UserLoginAccess {

	/** Button to navigate to the on-site user interface */

	@FXML
	private Button onsite;
	/** Button to navigate to the remote user interface */
	@FXML
	private Button remote;
	/** The primary stage used to switch scenes */
	private Stage primaryStage;
	/** Reference to the main application instance */
	private MainApp mainApp;

	/**
	 * Handles the event triggered when the user clicks the "On-Site" button. Loads
	 * the OnSiteScreen FXML and sets the stage and main application reference.
	 *
	 * @param event the button click event
	 * @throws IOException if the FXML file cannot be loaded
	 */
	@FXML
	void loadOnSiteScreen(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/OnSiteScreen.fxml"));
		Parent root = loader.load();
		OnSiteScreen controller = loader.getController();
		controller.setPrimaryStage(primaryStage);
		controller.setMainApp(mainApp);
		Scene s = new Scene(root, 550, 300);
		s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
		Platform.runLater(() -> {
			primaryStage.setScene(s);
			primaryStage.setTitle("BPark - On-Site");

		});

	}

	/**
	 * Sets the primary stage reference.
	 *
	 * @param primaryStage the main application stage
	 */

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
		;
	}

	/**
	 * Sets the reference to the main application instance.
	 *
	 * @param mainApp the MainApp instance
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Handles the event triggered when the user clicks the "Remote" button. Loads
	 * and displays the remote customer interface.
	 *
	 * @param event the button click event
	 */
	@FXML
	void loadRemoteScreen(ActionEvent event) {
		RemoteScreen OffSite = new RemoteScreen();
		OffSite.setMain(mainApp);
		Parent root = OffSite.buildRoot();
		Scene s = new Scene(root, 800, 600);
		s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());

		Platform.runLater(() -> {
			primaryStage.setScene(s);
			primaryStage.setTitle("BPark - Customer: " + mainApp.getUserName());
			primaryStage.show();
		});
	}

}
