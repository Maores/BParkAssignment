package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import common.DatabaseListener;

public class DBController {
	// Singleton instance
	private static DBController instance = null;
	private Connection conn;
	private DatabaseListener listener;
	
	// Private constructor to prevent direct instantiation
	private DBController() {
		// Connection will be initialized when needed
	}

	// Public method to get the singleton instance
	public static synchronized DBController getInstance(DatabaseListener listener) {
		if (instance == null) {
			instance = new DBController();
			System.out.println("Creating DBController singleton instance.");
			if (listener != null) {
				listener.onDatabaseMessage("Creating DBController singleton instance.");
			}
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
			// Build string for database - column names
			for (int i = 1; i <= 6; i++) {
				String s = new String();
				s = String.format("%s ", rsmd.getColumnName(i));
				str.append(s);
			}

			// Build string for database - rows
			while (rs.next()) {
				for (int i = 1; i <= 6; i++) {
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
	public String SearchID(String id) {

		StringBuilder str = new StringBuilder();

		try {
//			Statement stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM table_order WHERE order_number = ?;");
//			ResultSetMetaData rsmd = rs.getMetaData();

			String sql = "SELECT * FROM `table_order` WHERE order_number = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(id));
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			// Build string for database - column names
			for (int i = 1; i <= 6; i++) {
				String s = new String();
				s = String.format("%s ", rsmd.getColumnName(i));
				str.append(s);
			}

			// Build string for database - rows
			while (rs.next()) {
				for (int i = 1; i <= 6; i++) {
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
			System.out.println("Establishing database connection...");
			if (listener != null) {
				listener.onDatabaseMessage("Establishing database connection...");
			}
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/bparkprototype?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true", "root", // MySql //
																											// username
					"Aa123456" // MySql password
			);

			System.out.println("Database connection established successfully.");
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

	public boolean checkDB(String id) {
		String sql = "SELECT order_number FROM `table_order` WHERE order_number = ?";

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
	 * Update the database.
	 */
	public String updateDB(String parking_space, String order_date, String id) {
		String sql = "UPDATE `table_order` SET parking_space = ?, order_date = ? WHERE order_number = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(parking_space));
			ps.setString(2, order_date);
			ps.setInt(3, Integer.parseInt(id));

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
	 * Close the database connection.
	 */
	public void close() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
				System.out.println("Database connection closed.");
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
	public String getUserRoleById(String id) {
		String sql = "SELECT role FROM users WHERE id = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("role");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Close connection and reset singleton instance
	 */
	public static void closeAndReset() {
		if (instance != null) {
			System.out.println("Closing database connection and resetting singleton.");
			if (instance.listener != null) {
				instance.listener.onDatabaseMessage("Closing database connection and resetting singleton.");
			}
			instance.close();
			instance = null;
		}
	}
}
