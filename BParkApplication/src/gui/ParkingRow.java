package gui;

import javafx.beans.property.SimpleStringProperty;

public class ParkingRow {
    private final SimpleStringProperty col1;
    private final SimpleStringProperty col2;
    private final SimpleStringProperty col3;
    private final SimpleStringProperty col4;
    private final SimpleStringProperty col5;

    public ParkingRow(String c1, String c2, String c3, String c4, String c5) {
        this.col1 = new SimpleStringProperty(c1);
        this.col2 = new SimpleStringProperty(c2);
        this.col3 = new SimpleStringProperty(c3);
        this.col4 = new SimpleStringProperty(c4);
        this.col5 = new SimpleStringProperty(c5);
    }

    public String getCol1() { return col1.get(); }
    public String getCol2() { return col2.get(); }
    public String getCol3() { return col3.get(); }
    public String getCol4() { return col4.get(); }
    public String getCol5() { return col5.get(); }

}
