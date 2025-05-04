package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DBController {
	private Connection conn;

	/**
	 * Establish a connection to the MySQL database.
	 */

	/*
	 * 
	 */
	public String getDatabaseAsString() {
		
		StringBuilder str = new StringBuilder();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM table_order;");
			ResultSetMetaData rsmd = rs.getMetaData();

			// Print column names
			for (int i = 1; i <= 6; i++) {
				//System.out.print(rsmd.getColumnName(i) + "\t");
				String s = new String();
				s = String.format("%-20s", rsmd.getColumnName(i));
				str.append(s);
			}
			str.append("\n");
			
			// print rows
			while (rs.next()) {
				for (int i = 1; i <= 6; i++) {
					String columnValue = String.format("%-20s",rs.getString(i));
					str.append(columnValue);
					
				}
				str.append("\n");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//returns the string
		return str.toString();

	}


	/**
	 * Creating a connection to the database.
	 */
	public void connectToDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/bparkprototype?serverTimezone=UTC&useSSL=false", "root", // MySql username
					"Ra8420346" // MySql password
			);

			System.out.println("Database connection successful.");
		} catch (Exception e) {
			System.out.println("Failed to connect to database:");
			e.printStackTrace();
		}
	}


	/**
	 * Validates if information exists in DB.
	 */
	
	public boolean checkDB(String id) {
		String sql = "SELECT order_number FROM `table_order` WHERE order_number = ?";
	    
	    try {
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, Integer.parseInt(id));
	        
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) {
	            //The order_number exists
	            System.out.println("Order number " + id + " exists in the database.");
	            return true;
	        } else {
	            // No result found -> the order_number does not exist
	            System.out.println("Order number " + id + " does not exist in the database.");
	            return false;
	        }
	        
	    } catch (Exception e) {
	    	
	        return false;
	    }
	}
	
	
	/**
	 * Update the database.
	 */
	public void updateDB(String parking_space, String order_date, String id) {
		//String sql = "UPDATE table_order "
		//+ "SET parking_space= '"+parking_space+"', order_date= '"+order_date+"' WHERE order_number='"+id+"';" ;
		
		String sql = "UPDATE `table_order` SET parking_space = ?, order_date = ? WHERE order_number = ?;";
		try {
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, Integer.parseInt(parking_space)); 
	        ps.setString(2, order_date);
	        ps.setInt(3, Integer.parseInt(id));
	        

	        ps.executeUpdate();
	        System.out.println("Database updated successfully.");
	    } catch (Exception e) {
	    	System.out.println("Database updated Failed.");
	        e.printStackTrace();
	    }
	
	}

	/**
	 * Close the database connection.
	 */
	public void close() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
				System.out.println("Connection closed.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
