package gui;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import client.ChatClient;
import common.ChatIF;
import common.SubscriberReportWrapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller for the monthly parking usage report view.
 * <p>
 * This class provides functionality to:
 * <ul>
 * <li>Load and visualize daily parking usage data in a bar chart</li>
 * <li>Export the data to a CSV file</li>
 * </ul>
 */
public class ReportController implements ChatIF {

	@FXML
	private ComboBox<Integer> monthComboBox;

	@FXML
	private ComboBox<Integer> yearComboBox;

	@FXML
	private BarChart<String, Number> barChart;

	@FXML
	private Button backBtn;

	private Dialog<Void> dialog;

	private ChatClient chatClient;

	/** Stores the currently loaded report data (day → number of parkings). */

	private Map<Integer, Integer> currentReportData = new TreeMap<>();

	private Stage reportStage;

	public void setStage(Stage stage) {
		reportStage = stage;
	}

	public void setDialog(Dialog<Void> dialog) {
		this.dialog = dialog;
	}

	public void setChatClient(ChatClient client) {
		this.chatClient = client;
	}

	/**
	 * Initializes the report screen with month/year dropdowns and default values.
	 * Called automatically by JavaFX after FXML loading.
	 */

	@FXML
	public void initialize() {
		for (int i = 1; i <= 12; i++) {
			monthComboBox.getItems().add(i);
		}

		int currentYear = LocalDate.now().getYear();
		for (int y = currentYear - 5; y <= currentYear + 2; y++) {
			yearComboBox.getItems().add(y);
		}

		monthComboBox.setValue(LocalDate.now().getMonthValue());
		yearComboBox.setValue(currentYear);
	}

	/**
	 * Loads parking usage data from the database and displays it in a bar chart.
	 */
	@FXML
	private void onLoadReportClicked() {
		Integer month = monthComboBox.getValue();
		Integer year = yearComboBox.getValue();

		if (month == null || year == null) {
			showAlert("Selection Error", "Please select both month and year.");
			return;
		}

		String request = "GET_SUBSCRIBER_REPORT " + month + " " + year;
		chatClient.handleMessageFromClientUI(request);
	}

	public void loadReportFromServer(SubscriberReportWrapper wrapper) {
		Platform.runLater(() -> {
			currentReportData = wrapper.getReportData();

			barChart.getData().clear();
			CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
			xAxis.getCategories().clear();

			for (int day = 1; day <= 31; day++) {
				xAxis.getCategories().add(String.valueOf(day));
			}

			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName("Parking Usage");

			for (Map.Entry<Integer, Integer> entry : currentReportData.entrySet()) {
				series.getData().add(new XYChart.Data<>(String.valueOf(entry.getKey()), entry.getValue()));
			}

			barChart.getData().add(series);
		});
	}

	@FXML
	private void backToDialog() {
		reportStage.close();
		dialog.showAndWait();
	}

	/**
	 * Exports the currently loaded report data to a CSV file.
	 */
	@FXML
	private void onExportCsvClicked() {
		if (currentReportData.isEmpty()) {
			showAlert("No Data", "Please load a report first.");
			return;
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Report As CSV");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
		fileChooser.setInitialFileName("parking_report.csv");

		try {
			Stage stage = new Stage();
			java.io.File file = fileChooser.showSaveDialog(stage);

			if (file != null) {
				try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
					writer.println("Date,Number of Parkings");

					int month = monthComboBox.getValue();
					int year = yearComboBox.getValue();

					for (Map.Entry<Integer, Integer> entry : currentReportData.entrySet()) {
						int day = entry.getKey();
						String fullDate = String.format("%04d-%02d-%02d", year, month, day);
						writer.println(fullDate + "," + entry.getValue());
					}

					showAlert("Success", "Report exported successfully.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Export Error", "Could not export file.");
		}
	}

	/**
	 * Utility method to show informational alerts.
	 *
	 * @param title   the title of the alert dialog
	 * @param message the message to be shown
	 */

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@Override
	public void handleMessageFromServer(String message) {
		// TODO Auto-generated method stub

	}
}
