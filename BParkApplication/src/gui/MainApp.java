package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * The main entry point for the BPark application.
 * <p>
 * This class is responsible for initializing the primary stage, loading the
 * initial login screen, and navigating to different GUI scenes based on the
 * user's role.
 * </p>
 */
public class MainApp extends Application {
	private Stage primaryStage;
	/** Primary application stage. */
	private static String userId;
	/** Currently logged-in user's ID. */
	private static String userName;

	/** Currently logged-in user's name. */

	/**
	 * Returns the currently logged-in user's name.
	 * 
	 * @return the username
	 */
	public static String getUserName() {
		return userName;
	}

	/**
	 * Sets the logged-in user's name.
	 * 
	 * @param userName the username to set
	 */
	public static void setUserName(String userName) {
		MainApp.userName = userName;
	}

	/**
	 * Sets the logged-in user's ID.
	 * 
	 * @param userId the user ID to set
	 */
	public static void setUserId(String userId) {
		MainApp.userId = userId;
	}

	/**
	 * Returns the currently logged-in user's ID.
	 * 
	 * @return the user ID
	 */
	public static String getUserId() {
		return userId;
	}

	/**
	 * Initializes the application by launching the login screen.
	 * 
	 * @param primaryStage the primary stage for this application
	 * @throws Exception if an error occurs during FXML loading
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		showLoginScreen();
	}

	/**
	 * Loads and displays the login screen (userLogin.fxml).
	 * 
	 * @throws Exception if the FXML file fails to load
	 */
	public void showLoginScreen() throws Exception {
		this.userId = null;
		this.userName = null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/userLogin.fxml"));
		Parent root = loader.load();
		loginController controller = loader.getController();
		controller.setMainApp(this);
		Scene s = new Scene(root);
		primaryStage.setResizable(false);
		s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
		primaryStage.setScene(s);
		primaryStage.setTitle("BPark - Login");
		primaryStage.show();

	}

	/**
	 * Displays the main parking system screen for users with general access.
	 * 
	 * @throws Exception if there is a failure building the UI
	 */
	public void showMainScreen() throws Exception {
		RemoteScreen parkingSystemGUI = new RemoteScreen();
		Parent root = parkingSystemGUI.buildRoot();

		primaryStage.setScene(new Scene(root, 675, 600));
		primaryStage.setTitle("BPark - Smart Parking System");
		primaryStage.show();
	}

	/**
	 * Displays the appropriate screen based on the user's role.
	 * 
	 * @param role     the role of the logged-in user (e.g., user, staff, manager)
	 * @param userId   the user's ID
	 * @param userName the user's name
	 * @throws Exception if the appropriate screen cannot be loaded
	 */
	public void showRoleScreen(String role, String userId, String userName) throws Exception {
		String roleLower = role.toLowerCase();
		this.userId = userId;
		this.userName = userName;
		// USER
		if (roleLower.contains("user")) {
//            ParkingSystemGUI parkingSystemGUI = new ParkingSystemGUI();
//            Parent root = parkingSystemGUI.buildRoot();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/userAccessOptionsGUI.fxml"));
			Parent root = loader.load();
			UserLoginAccess controller = loader.getController();
			Scene s = new Scene(root, 550, 350);
			s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
			controller.setPrimaryStage(primaryStage);
			controller.setMainApp(this);
			Platform.runLater(() -> {
				primaryStage.setScene(s);
				primaryStage.setTitle("BPark - Customer Access");

			});
			// STAFF
		} else if (roleLower.contains("staff")) {
			StaffGui Staff = new StaffGui();
			Staff.setMain(this);
			Parent root = Staff.buildRoot();
			Scene s = new Scene(root, 800, 500);
			s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
			Staff.start();
			Platform.runLater(() -> {
				primaryStage.setScene(s);
				primaryStage.setTitle("BPark - Staff/Report");
			});
			// MANAGER
		} else if (roleLower.contains("manager")) {
			UserManagementScreen userManagementScreen = new UserManagementScreen();
			userManagementScreen.setMain(this);
			Parent root = userManagementScreen.buildRoot();
			Scene s = new Scene(root, 800, 400);
			s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
			Platform.runLater(() -> {
				primaryStage.setScene(s);
				primaryStage.setTitle("BPark - Manager/Admin");
			});
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Unknown role: " + role);
			alert.showAndWait();
			showLoginScreen();
		}
		primaryStage.show();
	}

	/**
	 * Main method to launch the application.
	 * 
	 * @param args command-line arguments (not used)
	 */
	public static void main(String[] args) {
		launch(args);
	}

}