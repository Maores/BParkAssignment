package gui;

import db.DBController;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class ReportController {

    @FXML
    private ComboBox<Integer> monthComboBox;

    @FXML
    private ComboBox<Integer> yearComboBox;

    @FXML
    private BarChart<String, Number> barChart;
    

    private DBController db;
    private Map<Integer, Integer> currentReportData = new TreeMap<>();

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
            currentReportData = db.getDailyParkingUsage(month, year);
            barChart.getData().clear();

            CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
            xAxis.getCategories().clear();

            for (int day = 1; day <= 31; day++) {
                xAxis.getCategories().add(String.valueOf(day));
            }

            xAxis.setTickLabelRotation(0);
            xAxis.setTickLabelGap(10);
            barChart.setAnimated(false); 
            
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Parking Usage for " + month + "/" + year);

            for (Map.Entry<Integer, Integer> entry : currentReportData.entrySet()) {
                series.getData().add(new XYChart.Data<>(String.valueOf(entry.getKey()), entry.getValue()));
            }

            barChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load report.");
        }
    }
    


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



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
