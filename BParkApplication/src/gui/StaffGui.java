package gui;

import client.ChatClient;
import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class StaffGui implements ChatIF {

    @FXML private TextField nameField;
    @FXML private TextField idField;
    private TextField phoneField;
    private TextField emailField;
    @FXML private TextArea outputDisplay;
    
    private TableView<ParkingRow> table = new TableView<>();
    private singletoneClient sg = new singletoneClient();
    private ChatClient client;
    private MainApp main;
    
    public void start() {
        client = sg.getInstance(this);
    }
    public void setMain(MainApp main) {
		this.main = main;
	}

    @FXML
    void ViewDB() {
        String command = "VIEW_DATABASE";
        outputDisplay.appendText("Fetching data from DataBase...\n");
        try {
            client.handleMessageFromClientUI(command);
            handleMessageFromServer("Requesting database contents...");
        } catch (Exception e) {
            handleMessageFromServer("Error sending request: " + e.getMessage());
        }
    }

    @FXML
    void addNewUser() {
        String name = nameField.getText();
        String id = idField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        if (name == null || name.isEmpty() || id == null || id.isEmpty() || email == null || email.isEmpty() || phone == null || phone.isEmpty()) {
            handleMessageFromServer("Please fill all fields.");
            return;
        }

        String command = "ADD_USER " + id + " " + name + " " + phone + " " + email;
        try {
            client.handleMessageFromClientUI(command);
            handleMessageFromServer("Request sent to add user: " + name + " (" + id + ")");
        } catch (Exception e) {
            handleMessageFromServer("Error: " + e.getMessage());
        }
    }

    @Override
    public void handleMessageFromServer(String message) {
        Platform.runLater(() -> {
            //outputDisplay.appendText(message + "\n");
        	if(message.startsWith("order_number")) {
        		Platform.runLater(() -> {
            	String[] str = message.split(" ");
                table.setEditable(true);
                
                table.getColumns().clear();

                TableColumn<ParkingRow, String> a = new TableColumn<>(str[0]);
                a.setCellValueFactory(new PropertyValueFactory<>("col1"));
                TableColumn<ParkingRow, String> b = new TableColumn<>(str[1]);
                b.setCellValueFactory(new PropertyValueFactory<>("col2"));
                TableColumn<ParkingRow, String> c = new TableColumn<>(str[2]);
                c.setCellValueFactory(new PropertyValueFactory<>("col3"));
                TableColumn<ParkingRow, String> d = new TableColumn<>(str[3]);
                d.setCellValueFactory(new PropertyValueFactory<>("col4"));
                TableColumn<ParkingRow, String> e = new TableColumn<>(str[4]);
                e.setCellValueFactory(new PropertyValueFactory<>("col5"));
                TableColumn<ParkingRow, String> f = new TableColumn<>(str[5]);
				f.setCellValueFactory(new PropertyValueFactory<>("col6"));
				table.getColumns().addAll(a, b, c, d, e, f);
				ObservableList<ParkingRow> items = FXCollections.observableArrayList();
				for (int i = 6; i + 5 < str.length; i += 6) {
					ParkingRow row = new ParkingRow(str[i], str[i + 1], str[i + 2], str[i + 3], str[i + 4], str[i + 5]);
					items.add(row);
				}

                table.setItems(items);  
        		});
            }
      
        	else {
        		outputDisplay.appendText(message+"\n");
        	}
        });
    }

    public StackPane buildRoot() {
    	StackPane root = new StackPane();
    	root.setId("pane");
    	
    	Label name =  new Label("User name:");
        nameField = new TextField();
        nameField.setPromptText("Enter user name");
        VBox nameBox = new VBox(name,nameField);
        
        Label id =  new Label("User ID:");
        idField = new TextField();
        idField.setPromptText("Enter user ID");
        VBox idBox = new VBox(id ,idField);
        
        HBox nameIdBox = new HBox(nameBox,idBox);
        nameIdBox.setSpacing(10);
        
        Label email =  new Label("E-Mail:");
        emailField = new TextField();
        VBox emailBox = new VBox(email,emailField);
        
        Label phone =  new Label("Phone:");
        phoneField = new TextField();
        VBox phoneBox = new VBox(phone,phoneField);
        
        
        
        outputDisplay = new TextArea();
        outputDisplay.setPrefHeight(350);
        outputDisplay.setEditable(false);

        Button addUserBtn = new Button("Add New User");
        addUserBtn.setOnAction(e -> addNewUser());

        Button viewDBBtn = new Button("View Database");
        viewDBBtn.setOnAction(e -> ViewDB());
        Button logOutBtn = new Button("LogOut");
		logOutBtn.setId("logOutBtn");
		//.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-cursor: hand;");
		logOutBtn.setOnAction(e -> {
			try {
				main.showLoginScreen();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        VBox emptyAddBox = new VBox(new Label(""),addUserBtn);
        HBox phoneEmailAddBox = new HBox(emailBox,phoneBox,emptyAddBox);
        HBox viewlogBox = new HBox(viewDBBtn,logOutBtn);
        phoneEmailAddBox.setSpacing(10);
        viewlogBox.setSpacing(10);
        //Btn styles
//        addUserBtn.setStyle("-fx-background-color: #0b132b; -fx-text-fill: white; -fx-cursor: hand;");
//        viewDBBtn.setStyle("-fx-background-color: #0b132b; -fx-text-fill: white; -fx-cursor: hand;");
        VBox Interior = new VBox(10,
        		nameIdBox ,
        		phoneEmailAddBox,
        		viewlogBox,
            outputDisplay,
            table
        );
        root.setMargin(Interior, new Insets(15));
        root.getChildren().add(Interior);
        root.setPrefWidth(400);
        root.setPrefHeight(400);
        return root;
    }
}
