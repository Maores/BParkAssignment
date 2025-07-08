package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import db.DBController;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ParkingTimingReportController {

	@FXML
	private ComboBox<Integer> monthComboBox;

	@FXML
	private ComboBox<Integer> yearComboBox;

	@FXML
	private BarChart<String, Number> barChart;

	@FXML
	private PieChart latePieChart;

	private DBController db;
	private Map<Integer, ParkingTimingStats> currentReportData = new TreeMap<>();

	@FXML
	public void initialize() {
		db = DBController.getInstance(null);
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

	@FXML
	private void onLoadReportClicked() {
	    Integer month = monthComboBox.getValue();
	    Integer year = yearComboBox.getValue();

	    if (month == null || year == null) {
	        showAlert("Selection Error", "Please select both month and year.");
	        return;
	    }

	    try {
	        Map<Integer, ParkingTimingStats> reportData = db.getDailyParkingTimingReport(month, year);
	        Map<String, Integer> lateUsersByName = db.getLateUsersByName(month, year);
	        currentReportData = reportData;

	        barChart.getData().clear();
	        barChart.setAnimated(false);

	        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
	        xAxis.getCategories().clear();
	        for (Integer day : reportData.keySet()) {
	            xAxis.getCategories().add(String.valueOf(day));
	        }

	        XYChart.Series<String, Number> extendedSeries = new XYChart.Series<>();
	        extendedSeries.setName("Extended");

	        for (Map.Entry<Integer, ParkingTimingStats> entry : reportData.entrySet()) {
	            String day = String.valueOf(entry.getKey());
	            ParkingTimingStats stats = entry.getValue();
	            extendedSeries.getData().add(new XYChart.Data<>(day, stats.extended));
	        }

	        barChart.getData().add(extendedSeries);

	        latePieChart.getData().clear();
	        for (Map.Entry<String, Integer> entry : lateUsersByName.entrySet()) {
	            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
	            latePieChart.getData().add(slice);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        showAlert("Error", "Could not load report.");
	    }
	}





	@FXML
	private void onExportCsvClicked() {
	    if (currentReportData == null || currentReportData.isEmpty()) {
	        showAlert("No Data", "Please load a report first.");
	        return;
	    }

	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save Report As CSV");
	    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
	    fileChooser.setInitialFileName("parking_timing_report.csv");

	    Stage stage = new Stage();
	    File file = fileChooser.showSaveDialog(stage);

	    if (file != null) {
	        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
	            int month = monthComboBox.getValue();
	            int year = yearComboBox.getValue();

	            writer.println("Date,Extensions");
	            for (Map.Entry<Integer, ParkingTimingStats> entry : currentReportData.entrySet()) {
	                int day = entry.getKey();
	                ParkingTimingStats stats = entry.getValue();
	                String fullDate = String.format("%02d/%02d/%d", day, month, year);
	                writer.printf("%s,%d%n", fullDate, stats.extended);
	            }

	            writer.println();  
	            writer.println("Late Users This Month:");

	            Map<String, Integer> lateUsers = db.getLateUsersForMonth(month, year);
	            writer.println("User Name,Late Count");

	            for (Map.Entry<String, Integer> entry : lateUsers.entrySet()) {
	                writer.printf("%s,%d%n", entry.getKey(), entry.getValue());
	            }

	            showAlert("Success", "Report exported successfully.");
	        } catch (Exception e) {
	            e.printStackTrace();
	            showAlert("Export Error", "Could not export file.");
	        }
	    }
	}



	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
