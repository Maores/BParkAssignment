package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

import common.DatabaseListener;

public class DBController {
	// Singleton instance
	private static DBController instance = null;
	private Connection conn;
	private DatabaseListener listener;
	private int orderNumber; 
	private int maxSpace = 10;
	// Private constructor to prevent direct instantiation
	private DBController() {
		// Connection will be initialized when needed
	}

	// Public method to get the singleton instance
	public static synchronized DBController getInstance(DatabaseListener listener) {
		if (instance == null) {
			instance = new DBController();
		}
		// Update listener reference (can be null for screens that don't need notifications)
		instance.listener = listener;
		return instance;
	}

	/*
	 * Get data base result as string
	 */
	public String getDatabaseAsString() {

		StringBuilder str = new StringBuilder();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM table_order;");
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnSize = rsmd.getColumnCount(); 
			// Build string for database - column names
			for (int i = 1; i <= columnSize; i++) {
				String s = new String();
				s = String.format("%s ", rsmd.getColumnName(i));
				str.append(s);
			}

			// Build string for database - rows
			while (rs.next()) {
				for (int i = 1; i <= columnSize; i++) {
					String columnValue = String.format("%s ", rs.getString(i));
					str.append(columnValue);

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// returns the string
		return str.toString();

	}
	public String getDatabaseByIDAsString(String id) {

		StringBuilder str = new StringBuilder();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM table_order WHERE subscriber_id ="+id+";");
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnSize = rsmd.getColumnCount(); 
			// Build string for database - column names
			for (int i = 1; i <= columnSize; i++) {
				String s = new String();
				s = String.format("%s ", rsmd.getColumnName(i));
				str.append(s);
			}

			// Build string for database - rows
			while (rs.next()) {
				for (int i = 1; i <= columnSize; i++) {
					String columnValue = String.format("%s ", rs.getString(i));
					str.append(columnValue);

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// returns the string
		return str.toString();

	}

	// Search Specific ID
	public String SearchOrder(String order) {

		StringBuilder str = new StringBuilder();

		try {
//			Statement stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM table_order WHERE order_number = ?;");
//			ResultSetMetaData rsmd = rs.getMetaData();

			String sql = "SELECT * FROM `table_order` WHERE order_number = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(order));
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnSize = rsmd.getColumnCount(); 
			// Build string for database - column names
			for (int i = 1; i <= columnSize; i++) {
				String s = new String();
				s = String.format("%s ", rsmd.getColumnName(i));
				str.append(s);
			}

			// Build string for database - rows
			while (rs.next()) {
				for (int i = 1; i <= columnSize; i++) {
					String columnValue = String.format("%s ", rs.getString(i));
					str.append(columnValue);

				}
			}
			if(str.length()==columnSize) {
				return "Order number not exist!";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// returns the string
		return str.toString();

	}
	// Search Specific ID
		public String SearchID(String id) {

			StringBuilder str = new StringBuilder();

			try {
//				Statement stmt = conn.createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT * FROM table_order WHERE order_number = ?;");
//				ResultSetMetaData rsmd = rs.getMetaData();

				String sql = "SELECT * FROM `users` WHERE id = ?;";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(id));
				ResultSet rs = ps.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount(); 

				// Build string for database - column names
				for (int i = 1; i <= columnCount; i++) {
					String s = new String();
					s = String.format("%s ", rsmd.getColumnName(i));
					str.append(s);
				}

				// Build string for database - rows
				while (rs.next()) {
					for (int i = 1; i <= columnCount; i++) {
						String columnValue = String.format("%s ", rs.getString(i));
						str.append(columnValue);

					}
				}
				if(str.length()==columnCount) {
					return "User ID not exist!";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// returns the string
			return str.toString();

		}

	/**
	 * Creating a connection to the database.
	 */
	public String connectToDB() {
		try {
			// Check if connection already exists and is valid
			if (conn != null && !conn.isClosed() && conn.isValid(2)) {
				// Connection exists and is valid - just return success
				return "Database connection already established.";
			}
			
			// Create new connection only if needed
			if (listener != null) {
				listener.onDatabaseMessage("Establishing database connection...");
			}
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/bparkprototype?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true", "root", // MySql //
																											// username
					"Aa12345" // MySql password
					
			);
			
			if (listener != null) {
				listener.onDatabaseMessage("Database connection established successfully.");
				listener.onDatabaseConnectionChange(true);
			}
			return "Database connection established successfully.";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			if (listener != null) {
				listener.onDatabaseError(e.getMessage());
				listener.onDatabaseConnectionChange(false);
			}
			return "Failed to connect to database!";
		}
	}

	/**
	 * Validates if information exists in DB.
	 */

	public boolean checkDB(String order) {
		String sql = "SELECT order_number FROM `table_order` WHERE order_number = ?";

		try {
			// puts the id inside the "?" that is at the end of the query above
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(order));
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				// The order_number exists
				return true;
			} else {
				// No result found -> the order_number does not exist
				return false;
			}

		} catch (Exception e) {

			return false;
		}
	}
	public boolean checkUserDB(String id) {
		String sql = "SELECT id FROM `users` WHERE id = ?";

		try {
			// puts the id inside the "?" that is at the end of the query above
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(id));
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				// The order_number exists
				return true;
			} else {
				// No result found -> the order_number does not exist
				return false;
			}

		} catch (Exception e) {

			return false;
		}
	}
	

	/**
	 * Update a specific order (change the date)
	 */
	public String updateDB( String order_date, String id) {
		String sql = "UPDATE `table_order` SET  order_date = ? WHERE order_number = ?;";
		if(!availableSpots(order_date)) {
			return "Not enough space at this date!";
		}
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, order_date);
			ps.setInt(2, Integer.parseInt(id));

			ps.executeUpdate();
			System.out.println("Database updated successfully.");
			if (listener != null) {
				listener.onDatabaseMessage("Database updated successfully.");
			}
			return "true";
		} catch (Exception e) {
			System.out.println("Database updated Failed.");
			System.out.println(e.getMessage());
			if (listener != null) {
				listener.onDatabaseError("Database update failed: " + e.getMessage());
			}
			return e.getMessage();
		}

	}
	/**
	 * Insert user to the database.
	 */
	public String insertUserToDB(String name, String id) {
		if(checkDB(id)) {
			return "Insert_User false";
		}
		String sql = "INSERT INTO `users` (id, name, role) VALUES (?, ?, 'user');";
		try {
		    PreparedStatement ps = conn.prepareStatement(sql);
		    ps.setInt(1, Integer.parseInt(id));
		    ps.setString(2, name);
		    ps.executeUpdate();
		    return "User added Succsussfully";
		} catch (SQLIntegrityConstraintViolationException e1) {
		  // Or handle error properly
			return "User name/id alreay exists!" ;
		}catch(SQLException e2) {
			return "User couldnt be added!" ;
		}

	}
	/**
	 * Insert user to the database.(order_number, parking_space, order_date, confirmation_code, subscriber_id, date_of_placing_an_order)
	 */
	public String insertResToDB(String order_date, String id) {
		orderNumber = this.getMaxOrderNumber();
		orderNumber++;
		String sql = "INSERT INTO `table_order` (order_number, order_date, confirmation_code, subscriber_id, date_of_placing_an_order) "
				+ "VALUES (?,?,?,?,?);";
		if(!availableSpots(order_date)) {
			return "Not enough space at this date!";
		}
		Random rand = new Random();
        int confirCode = rand.nextInt(9000) + 1000; // 0–8999 + 1000 = 1000–9999
        LocalDate date = LocalDate.now(); 
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, orderNumber);
			ps.setString(2, order_date);
			ps.setInt(3, confirCode);
			ps.setInt(4, Integer.parseInt(id));
			ps.setString(5, date.toString());
			ps.executeUpdate();
			System.out.println("Database updated successfully.");
			if (listener != null) {
				listener.onDatabaseMessage("Database updated successfully.");
			}
			return "Order added Succsussfully"+"\nYour confirmation code is: " + confirCode;
		} catch (Exception e) {
			e.printStackTrace();
			return "Order couldnt be added!" ;
		}

	}
	//true -> theres spaces available (more than 40%)
	public boolean availableSpots(String order_date) {
		String sql = "SELECT COUNT(*) FROM table_order WHERE order_date = ?;";
		int count=0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, order_date);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
			    count = rs.getInt(1);
			}
			
			if(((float)(maxSpace-count)/maxSpace) >= 0.4 ) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false ;
		}

	}
	/**
	 * Close the database connection.
	 */
	public void close() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
				if (listener != null) {
					listener.onDatabaseMessage("Database connection closed.");
					listener.onDatabaseConnectionChange(false);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			if (listener != null) {
				listener.onDatabaseError("Error closing connection: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Get user role by id from the users table
	 */
	public String getUserRoleById(String id , String name) {
		String sql = "SELECT role FROM users WHERE id = ? AND name = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("role");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return "null";
	}
	//get the maximum order number so the numbers are going up 
	public int getMaxOrderNumber() {
		String orderNum;
		String sql = "SELECT MAX(order_number) FROM table_order;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				 orderNum = rs.getString("MAX(order_number)");
				 return Integer.parseInt(orderNum);
			}
			else return 1000;
		} catch (SQLException e) {
			e.printStackTrace();
			return 1000;
		}
	}
	/**
	 * Close connection and reset singleton instance
	 */
	public static void closeAndReset() {
		if (instance != null) {
			instance.close();
			instance = null;
		}
	}
}
