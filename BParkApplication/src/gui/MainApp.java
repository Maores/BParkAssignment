package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/parkingSystem.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 675, 600));
        primaryStage.setTitle("BPark - Smart Parking System");
        primaryStage.show();
    }

    public void showRoleScreen(String role, String userId, String userName) throws Exception {
        String roleLower = role.toLowerCase();
        if (roleLower.contains("user")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/parkingSystem.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 675, 600));
            primaryStage.setTitle("BPark - Smart Parking System (Customer)");
        } else if (roleLower.contains("staff")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/reportClient.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.setTitle("BPark - Staff");
        } else if (roleLower.contains("manager")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/userManagement.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.setTitle("BPark - Manager");
        }
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 