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
        ParkingSystemGUI parkingSystemGUI = new ParkingSystemGUI();
        Parent root = parkingSystemGUI.buildRoot();
        primaryStage.setScene(new Scene(root, 675, 600));
        primaryStage.setTitle("BPark - Smart Parking System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 