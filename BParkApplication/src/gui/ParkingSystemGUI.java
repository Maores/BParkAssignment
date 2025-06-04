package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ParkingSystemGUI implements Initializable {
	/**
	 * The default port and host to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;
	final public static String DEFAULT_HOST = "localhost";
	@FXML private TextArea dbDisplay;
	@FXML private TextField idField, dateField, spotField, srcField;
	@FXML private TableView<ParkingRow> table;
	@FXML private javafx.scene.control.Button viewBtn, updateBtn, tryBtn, srcBtn;
	private GUIParkingClient guiClient;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		guiClient = new GUIParkingClient(DEFAULT_HOST, DEFAULT_PORT, this);
		viewBtn.setOnAction(e -> guiClient.sendMessage("VIEW_DATABASE"));
		updateBtn.setOnAction(e -> {
			String id = idField.getText();
			String date = dateField.getText();
			String spot = spotField.getText();
			if (!id.isEmpty() && !date.isEmpty() && !spot.isEmpty()) {
				String updateMsg = "UPDATE_ORDER " + id + " " + spot + " " + date;
				guiClient.sendMessage(updateMsg);
			} else {
				displayMessage("Please fill all fields.");
			}
		});
		tryBtn.setOnAction(e -> guiClient.connect(DEFAULT_HOST, DEFAULT_PORT));
		srcBtn.setOnAction(e -> guiClient.search(srcField.getText()));
	}

	@SuppressWarnings("unchecked")
	public void displayMessage(String message) {   
    	if(message.startsWith("parking_space")) {
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
                ParkingRow row = new ParkingRow(
                    str[i], str[i + 1], str[i + 2],
                    str[i + 3], str[i + 4], str[i + 5]
                );
                items.add(row);
            }

            table.setItems(items);  
    		});
        }
        else {
        	dbDisplay.setText(message);
        	
        }
    }

}
