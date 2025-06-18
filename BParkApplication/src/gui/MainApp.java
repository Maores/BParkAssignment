package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;
    private static String userId;
    private static String userName;
  //getter and setter for userName
    public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		MainApp.userName = userName;
	}

	//getter and setter for ID
    public static void setUserId(String userId) {
		MainApp.userId = userId;
	}

	public static String getUserId() {
		return userId;
	}

	@Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showLoginScreen();
    }

    public void showLoginScreen() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/userLogin.fxml"));
        Parent root = loader.load();
        loginController controller = loader.getController();
        controller.setMainApp(this);
        Scene s = new Scene(root);
        s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        primaryStage.setScene(s);
        primaryStage.setTitle("BPark - Login");
        primaryStage.show();
        
    }

    public void showMainScreen() throws Exception {
        RemoteScreen parkingSystemGUI = new RemoteScreen();
        Parent root = parkingSystemGUI.buildRoot();
        
        primaryStage.setScene(new Scene(root, 675, 600));
        primaryStage.setTitle("BPark - Smart Parking System");
        primaryStage.show();
    }

    public void showRoleScreen(String role, String userId, String userName) throws Exception {
        String roleLower = role.toLowerCase();
        this.userId = userId;
        this.userName = userName;
       //USER
        if (roleLower.contains("user")) {
        	 
//            ParkingSystemGUI parkingSystemGUI = new ParkingSystemGUI();
//            Parent root = parkingSystemGUI.buildRoot();
        	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/userAccessOptionsGUI.fxml")); 	 
             Parent root = loader.load();
             UserLoginAccess controller = loader.getController();
             Scene s = new Scene(root, 550, 350);
             s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
             controller.setPrimaryStage(primaryStage);
             Platform.runLater(() -> {
	            primaryStage.setScene(s);
	            primaryStage.setTitle("BPark - Customer Access");
          
             });
        //STAFF
        } else if (roleLower.contains("staff")) {
            StaffGui Staff = new StaffGui();
            Parent root = Staff.buildRoot();
            Scene s = new Scene(root, 600, 400);
            s.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
            Staff.start(); 
            Platform.runLater(() -> {
	            primaryStage.setScene(s);
	            primaryStage.setTitle("BPark - Staff/Report");
            });
        //MANAGER
        } else if (roleLower.contains("manager")) {
            UserManagementScreen userManagementScreen = new UserManagementScreen();
            Parent root = userManagementScreen.buildRoot();
            Scene s = new Scene(root, 600, 400);
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
    
    public static void main(String[] args) {
        launch(args);
    }


} 