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
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    public void showMainScreen() throws Exception {
        ParkingSystemGUI parkingSystemGUI = new ParkingSystemGUI();
        Parent root = parkingSystemGUI.buildRoot();
        primaryStage.setScene(new Scene(root, 675, 600));
        primaryStage.setTitle("BPark - Smart Parking System");
        primaryStage.show();
    }

    public void showRoleScreen(String role, String userId, String userName) throws Exception {
        String roleLower = role.toLowerCase();
        if (roleLower.contains("user")) {
            ParkingSystemGUI parkingSystemGUI = new ParkingSystemGUI();
            Parent root = parkingSystemGUI.buildRoot();
            Platform.runLater(() -> {
	            primaryStage.setScene(new Scene(root, 675, 600));
	            primaryStage.setTitle("BPark - Smart Parking System (Customer)");
            });
        } else if (roleLower.contains("staff")) {
            StaffGui Staff = new StaffGui();
            
            Parent root = Staff.buildRoot();
            Staff.start(); 
            Platform.runLater(() -> {
	            primaryStage.setScene(new Scene(root, 600, 400));
	            primaryStage.setTitle("BPark - Staff/Report");
            });
        } else if (roleLower.contains("manager")) {
            UserManagementScreen userManagementScreen = new UserManagementScreen();
            Parent root = userManagementScreen.buildRoot();
            Platform.runLater(() -> {
	            primaryStage.setScene(new Scene(root, 600, 400));
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