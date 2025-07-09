package gui;

import client.ChatClient;

import client.singletoneClient;
import common.ChatIF;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos; 

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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
/**
 * GUI controller for staff users in the BPark system.
 * <p>
 * Allows staff to add users, view user and parking databases, and log out.
 * </p>
 */
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
    /**
     * Initializes the ChatClient instance using singleton pattern.
     */
    public void start() {
        client = sg.getInstance(this);
    }
    /**
     * Sets the main application reference.
     * @param main the MainApp instance
     */
    public void setMain(MainApp main) {
		this.main = main;
	}
    
    /**
     * Handles the action for viewing the parking database.
     * Sends a request to the server and appends status to the UI.
     */
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
    /**
     * Sends a command to the server to add a new user based on entered data.
     * Validates the input fields before sending.
     */
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
    /**
     * Receives messages from the server and handles how data is displayed.
     *
     * @param message the message received from the server
     */
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
				TableColumn<ParkingRow, String> g = new TableColumn<>(str[6]);
				g.setCellValueFactory(new PropertyValueFactory<>("col7"));
				table.getColumns().addAll(a, b, c, d, e, f, g);
				ObservableList<ParkingRow> items = FXCollections.observableArrayList();
				for (int i = 7; i + 6 < str.length; i += 7) {
					ParkingRow row = new ParkingRow(str[i], str[i + 1], str[i + 2], str[i + 3], str[i + 4], str[i + 5],
							str[i + 6]);
					items.add(row);
				}

                table.setItems(items);  
        		});
            }else if (message.startsWith("id")) {
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
				outputDisplay.appendText("Completed!" + "\n");
			}
        	else {
        		outputDisplay.appendText(message+"\n");
        	}
        });
    }
    /**
     * Builds and returns the root layout for the staff GUI screen.
     *
     * @return a fully initialized StackPane layout
     */
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
        
        Region spacer = new Region();             
        HBox.setHgrow(spacer, Priority.ALWAYS);    

        
//        HBox nameIdBox = new HBox(nameBox,idBox);
//        nameIdBox.setSpacing(10);
        
        Label email =  new Label("E-Mail:");
        emailField = new TextField();
        VBox emailBox = new VBox(email,emailField);
        
        Label phone =  new Label("Phone:");
        phoneField = new TextField();
        VBox phoneBox = new VBox(phone,phoneField);
        
        
        
        outputDisplay = new TextArea();
        outputDisplay.setPrefHeight(300);
        outputDisplay.setEditable(false);

        Button addUserBtn = new Button("Add New User");
        addUserBtn.setOnAction(e -> addNewUser());

        Button viewDBBtn = new Button("View Database");
        viewDBBtn.setOnAction(e -> ViewDB());
        Button viewUserBtn = new Button("View users");
		viewUserBtn.setOnAction(e -> {
			outputDisplay.setText("Showing users..." + "\n");
			client.handleMessageFromClientUI("VIEW_USERDATABASE");
		});
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
		
		HBox nameIdBox = new HBox(
		        10,               
		        nameBox,
		        idBox,
		        spacer,           
		        logOutBtn         
		);
		nameIdBox.setAlignment(Pos.TOP_LEFT);     

		
        VBox emptyAddBox = new VBox(new Label(""),addUserBtn);
        HBox phoneEmailAddBox = new HBox(emailBox,phoneBox,emptyAddBox);
        HBox viewlogBox = new HBox(viewDBBtn,viewUserBtn);
        phoneEmailAddBox.setSpacing(10);
        viewlogBox.setSpacing(10);

        VBox Interior = new VBox(10,nameIdBox ,phoneEmailAddBox,viewlogBox,outputDisplay,table);
        root.setMargin(Interior, new Insets(15));
        root.getChildren().add(Interior);
        root.setPrefWidth(400);
        root.setPrefHeight(400);
        return root;
    }
}
