package client.gui;

import javafx.beans.property.SimpleStringProperty;

/**
 * Represents a row of parking-related data for use in a JavaFX TableView.
 * <p>
 * Each column is stored as a {@link SimpleStringProperty} to support data
 * binding in the GUI. Handles null values gracefully by converting them to
 * empty strings.
 * </p>
 */
public class ParkingRow {
	private final SimpleStringProperty col1;
	private final SimpleStringProperty col2;
	private final SimpleStringProperty col3;
	private final SimpleStringProperty col4;
	private final SimpleStringProperty col5;
	private final SimpleStringProperty col6;
	private final SimpleStringProperty col7;

	/**
	 * Constructs a ParkingRow with seven columns. Null values represented as the
	 * string "null" are converted to empty strings.
	 *
	 * @param c1 value for column 1
	 * @param c2 value for column 2
	 * @param c3 value for column 3
	 * @param c4 value for column 4 (nullable)
	 * @param c5 value for column 5 (nullable)
	 * @param c6 value for column 6 (nullable)
	 * @param c7 value for column 7 (nullable)
	 */
	public ParkingRow(String c1, String c2, String c3, String c4, String c5, String c6, String c7) {
		this.col1 = new SimpleStringProperty(c1);
		this.col2 = new SimpleStringProperty(c2);
		this.col3 = new SimpleStringProperty(c3);
		if (c4.equals("null")) {
			this.col4 = new SimpleStringProperty("");
		} else {
			this.col4 = new SimpleStringProperty(c4);
		}
		if (c5.equals("null")) {
			this.col5 = new SimpleStringProperty("");
		} else {
			this.col5 = new SimpleStringProperty(c5);
		}
		if (c6.equals("null")) {
			this.col6 = new SimpleStringProperty("");
		} else {
			this.col6 = new SimpleStringProperty(c6);
		}
		if (c7.equals("null")) {
			this.col7 = new SimpleStringProperty("");
		} else {
			this.col7 = new SimpleStringProperty(c7);
		}
	}

	/**
	 * Constructs a ParkingRow with six columns. Column 7 is initialized as null.
	 * Null values represented as the string "null" are converted to empty strings.
	 *
	 * @param c1 value for column 1
	 * @param c2 value for column 2
	 * @param c3 value for column 3
	 * @param c4 value for column 4 (nullable)
	 * @param c5 value for column 5 (nullable)
	 * @param c6 value for column 6 (nullable)
	 */
	public ParkingRow(String c1, String c2, String c3, String c4, String c5, String c6) {
		this.col1 = new SimpleStringProperty(c1);
		this.col2 = new SimpleStringProperty(c2);
		this.col3 = new SimpleStringProperty(c3);
		if (c4.equals("null")) {
			this.col4 = new SimpleStringProperty("");
		} else {
			this.col4 = new SimpleStringProperty(c4);
		}
		if (c5.equals("null")) {
			this.col5 = new SimpleStringProperty("");
		} else {
			this.col5 = new SimpleStringProperty(c5);
		}
		if (c6.equals("null")) {
			this.col6 = new SimpleStringProperty("");
		} else {
			this.col6 = new SimpleStringProperty(c6);
		}
		this.col7 = new SimpleStringProperty(null);
	}

	/**
	 * Gets the value of each column .
	 *
	 * @return value of col_i
	 */
	public String getCol1() {
		return col1.get();
	}

	public String getCol2() {
		return col2.get();
	}

	public String getCol3() {
		return col3.get();
	}

	public String getCol4() {
		return col4.get();
	}

	public String getCol5() {
		return col5.get();
	}

	public String getCol6() {
		return col6.get();
	}

	public String getCol7() {
		return col7.get();
	}
}
