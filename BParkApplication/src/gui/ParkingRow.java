package gui;

import javafx.beans.property.SimpleStringProperty;

public class ParkingRow {
    private final SimpleStringProperty col1;
    private final SimpleStringProperty col2;
    private final SimpleStringProperty col3;
    private final SimpleStringProperty col4;
    private final SimpleStringProperty col5;
    private final SimpleStringProperty col6;
    private final SimpleStringProperty col7;
    
    public ParkingRow(String c1, String c2, String c3, String c4, String c5, String c6,String c7) {
        this.col1 = new SimpleStringProperty(c1);
        this.col2 = new SimpleStringProperty(c2);
        this.col3 = new SimpleStringProperty(c3);
        if(c4.equals("null")) {
        	this.col4 = new SimpleStringProperty("");
        }
        else {
        	this.col4 = new SimpleStringProperty(c4);
        }
        if(c5.equals("null")) {
        	this.col5 = new SimpleStringProperty("");
        }
        else {
        	this.col5 = new SimpleStringProperty(c5);
        }
        if(c6.equals("null")) {
        	this.col6 = new SimpleStringProperty("");
        }
        else {
        	this.col6 = new SimpleStringProperty(c6);
        }
        if(c7.equals("null")) {
        	this.col7 = new SimpleStringProperty("");
        }
        else {
        	this.col7 = new SimpleStringProperty(c7);
        }
    }
    public ParkingRow(String c1, String c2, String c3) {
        this.col1 = new SimpleStringProperty(c1);
        this.col2 = new SimpleStringProperty(c2);
        this.col3 = new SimpleStringProperty(c3);
        this.col4 = new SimpleStringProperty(null);
        this.col5 = new SimpleStringProperty(null);
        this.col6 = new SimpleStringProperty(null);
        this.col7 = new SimpleStringProperty(null);
    }
    public String getCol1() { return col1.get(); }
    public String getCol2() { return col2.get(); }
    public String getCol3() { return col3.get(); }
    public String getCol4() { return col4.get(); }
    public String getCol5() { return col5.get(); }
    public String getCol6() { return col6.get(); }
    public String getCol7() { return col7.get(); }
}
