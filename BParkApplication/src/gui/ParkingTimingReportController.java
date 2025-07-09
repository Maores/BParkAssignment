package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import client.ChatClient;
import common.ChatIF;
import common.ParkingReportWrapper;
import common.ParkingTimingStats;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller for generating and exporting monthly parking timing reports.
 * <p>
 * Displays daily statistics for parking extensions in a bar chart,
 * and shows a pie chart of users who returned their cars late.
 * Users can also export the current report to a CSV file.
 * </p>
 */
public class ParkingTimingReportController implements ChatIF{


	@FXML
	private Button backBtn;
	
	@FXML
	private ComboBox<Integer> monthComboBox; //ComboBox for selecting the report month

	@FXML
	private ComboBox<Integer> yearComboBox; //ComboBox for selecting the report year.

	@FXML
	private BarChart<String, Number> barChart; //Bar chart for displaying daily parking extension data

	@FXML
	private PieChart latePieChart;//Pie chart for displaying late users' statistics
	
	private Dialog<Void> dialog;
	private Stage reportStage;
	private ChatClient chatClient;
	

	/** Holds the currently loaded report data mapped by day of month. */
	private Map<Integer, ParkingTimingStats> currentReportData = new TreeMap<>();

	
	public void setStage(Stage stage) {
		reportStage = stage;
	}
	public void setDialog(Dialog<Void> dialog) {
	 this.dialog = dialog;
	}


    /**
     * Initializes the report screen with default values and populates year/month combo boxes.
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

    public void setChatClient(ChatClient client) {
        this.chatClient = client;
    }

    @FXML
    private void onLoadReportClicked() {
        Integer month = monthComboBox.getValue();
        Integer year = yearComboBox.getValue();

        if (month == null || year == null) {
            showAlert("Selection Error", "Please select both month and year.");
            return;
        }

        String msg = "#GET_PARKING_TIMING_REPORT " + month + " " + year;
        chatClient.handleMessageFromClientUI(msg);
    }

    public void loadReportFromServer(ParkingReportWrapper wrapper) {
        Platform.runLater(() -> {
            currentReportData = wrapper.reportData;

            barChart.getData().clear();
            barChart.setAnimated(false);

            CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
            xAxis.getCategories().clear();

            XYChart.Series<String, Number> extendedSeries = new XYChart.Series<>();
            extendedSeries.setName("Extended");
            
            
            for (Map.Entry<Integer, ParkingTimingStats> entry : currentReportData.entrySet()) {
                String day = String.valueOf(entry.getKey());
                ParkingTimingStats stats = entry.getValue();
                
                xAxis.getCategories().add(day);
                
                extendedSeries.getData().add(new XYChart.Data<>(day, stats.extended));
            }
            
            barChart.getData().addAll(extendedSeries);

            latePieChart.getData().clear();
            for (Map.Entry<String, Integer> entry : wrapper.lateUsers.entrySet()) {
                PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
                latePieChart.getData().add(slice);
            }});
	}

    /**
     * Loads and displays the report data for the selected month and year.
     * Populates the bar chart with daily extension data and pie chart with late user stats.
     */

	@FXML
	private void backToDialog() {
		reportStage.close();
		dialog.showAndWait();
	}



    /**
     * Exports the currently displayed report to a CSV file.
     * Includes both the daily extension data and the monthly late user stats.
     */

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
                writer.println("User Name,Late Count");

                for (PieChart.Data slice : latePieChart.getData()) {
                    writer.printf("%s,%.0f%n", slice.getName(), slice.getPieValue());
                }

                showAlert("Success", "Report exported successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Export Error", "Could not export file.");
            }
        }
    }
	/**
     * Displays an informational alert dialog to the user.
     *
     * @param title   the title of the alert window
     * @param message the content message shown in the dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void handleMessageFromServer(String message) {
        System.out.println("String message from server: " + message);
    }


    public void handleMessageFromServer(Object msg) {
        if (msg instanceof ParkingReportWrapper) {
            Platform.runLater(() -> {
                loadReportFromServer((ParkingReportWrapper) msg);
            });
        }
    }

}
