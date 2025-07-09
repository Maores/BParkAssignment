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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import common.DatabaseListener;
import gui.ParkingTimingStats;

public class DBController {
	// Singleton instance
	private static DBController instance = null;
	private Connection conn;
	private DatabaseListener listener;
	private int orderNumber;
	private int maxSpace = 100;

	// Private constructor to prevent direct instantiation
	private DBController() {
		// Connection will be initialized when needed
	}

	// Public method to get the singleton instance
	public static synchronized DBController getInstance(DatabaseListener listener) {
		if (instance == null) {
			instance = new DBController();
		}
		// Update listener reference (can be null for screens that don't need
		// notifications)
		instance.listener = listener;
		return instance;
	}

	/**
	 * Get phone + email for the requested user-id. Returned format: "<phone>
	 * <email>"
	 */
	public String getMailPhoneDatabaseAsString(String id) {
		// Make sure we have a DB connection
		if (conn == null) {
			connectToDB();
		}

		String sql = "SELECT phone, email FROM users WHERE id = ?;";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, Integer.parseInt(id));
			ResultSet rs = ps.executeQuery();

			if (rs.next()) { // user found
				String phone = rs.getString("phone");
				String email = rs.getString("email");
				// Avoid nulls so they won't break the split() בצד-הלקוח
				if (phone == null)
					phone = "";
				if (email == null)
					email = "";
				return phone + " " + email; // same order the GUI מצפה
			} else {
				return ""; // user not found
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return ""; // error case
		}
	}

	/*
	 * Get data base result as string
	 */
	public String getDatabaseAsString() {

		StringBuilder str = new StringBuilder();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT order_number, order_date, order_time, finish_time, confirmation_code, subscriber_id, date_of_placing_an_order FROM table_order;");
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
			ResultSet rs = stmt.executeQuery("SELECT order_number, order_date, order_time, finish_time, confirmation_code, subscriber_id, date_of_placing_an_order FROM table_order WHERE subscriber_id =" + id + ";");
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

	/*
	 * Get data base result as string
	 */
	public String getUserDatabaseAsString() {

		StringBuilder str = new StringBuilder();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id, name, role, phone, email, password FROM users WHERE role = 'user';");
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

			String sql = "SELECT order_number, order_date, order_time, finish_time, confirmation_code, subscriber_id, date_of_placing_an_order FROM `table_order` WHERE order_number = ?;";
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
			if (str.length() == columnSize) {
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

			String sql = "SELECT id, name, role, phone, email, password FROM `users` WHERE id = ?;";
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
			if (str.length() == columnCount) {
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
					"jdbc:mysql://127.0.0.1:3306/bparkprototype?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true",
					"root", // MySql //
					// username
					"Aa123456" // MySql password

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
    
	Returns either the number of free parking spots for the given date
	or the string "No space available" when less than 40 % of the lot is free.*
	@param orderDate date in format "yyyy-MM-dd" – must match the DB column format
	@return String  – number of free spots, or "No space available"*/
	public String checkSpaceAvailability(String orderDate) {// 1. How many spots are still free on this date?
	    int freeSpots = AvailableSpaces(orderDate);      // existing helper

	        // If DB error occurred – propagate a generic failure message
	        if (freeSpots < 0) {
	            return "Database error – please try again";
	        }

	        // 2. Decide according to 40 % rule
	        float ratio = (float) freeSpots / maxSpace;      // maxSpace == 100
	        if (ratio >= 0.40f) {
	            // Enough space – tell the GUI exactly how many are free
	            return "Available spots: " + freeSpots;
	        } else {
	            // Less than 40 % free – tell the GUI there is no space
	            return "No space available";
	        }
	    }
	/**
	 * Update a specific order (change the date)
	 */
	public String updateDB(String order_number) {
        String sqlGet = "SELECT finish_time FROM table_order WHERE order_number = ?;";
        String hour_date = "";
        try {
            PreparedStatement ps = conn.prepareStatement(sqlGet);
            ps.setString(1, order_number);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                hour_date = rs.getString(1);
            }

        } catch (Exception e) {
        }
        String[] time = hour_date.split(":");
        int newTime = Integer.parseInt(time[0]) + 4;
        if (newTime >= 21) {
            hour_date = "21:00";
        } else {
            hour_date = String.format("%d:%s", newTime, time[1]);
        }

        String sql =
                "UPDATE table_order " +
                "SET finish_time = ?, was_extended = TRUE " +
                "WHERE order_number = ? AND car_inserted = 1;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, hour_date);
            ps.setInt(2, Integer.parseInt(order_number));

            int rows = ps.executeUpdate();
            if (rows == 0) {
                // nothing was updated , either wrong number or the car isn’t inside
                return "Car is not in the parking lot – cannot extend";
            }
            return "true";
//            ps.executeUpdate();
//            System.out.println("Database updated successfully.");
//            if (listener != null) {
//                listener.onDatabaseMessage("Database updated successfully.");
//            }
//            return "true";
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
	 * Update a user info (change the date)
	 */
	public String updateUserInfoDB(String phone, String email, String id) {
		if (email.isEmpty())
			email = null;
		if (phone.isEmpty())
			phone = null;
		String sql = "UPDATE `users` SET  phone = COALESCE(?, phone),email = COALESCE(?, email) WHERE id = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, phone);
			ps.setString(2, email);
			ps.setInt(3, Integer.parseInt(id));

			ps.executeUpdate();
			System.out.println("Database user information updated successfully.");
			if (listener != null) {
				listener.onDatabaseMessage("Database user information updated successfully.");
			}
			return "true";
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Database user information updated Failed.");
			if (listener != null) {
				listener.onDatabaseError("Database update failed: " + e.getMessage());
			}
			return "ERROR_UPDATE_USER";
		}

	}

	/**
	 * Insert user to the database.
	 */
	public String insertUserToDB(String name, String id, String phone, String email) {
		if (checkUserDB(id)) {
			return "Insert_User false";
		}
		String password = generatePassword();
		String sql = "INSERT INTO `users` (id, name, role,phone,email,password) VALUES (?, ?, 'user',?,?,?);";
		if (password.equals("ERROR")) {
			return "User couldnt be added!";
		}
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(id));// id
			ps.setString(2, name);// name
			ps.setString(3, phone);// phone
			ps.setString(4, email);// email
			ps.setString(5, password);// password
			ps.executeUpdate();
			return "User added Succsussfully \nYour Password:" + password + "\nKeep it!!!";
		} catch (SQLIntegrityConstraintViolationException e1) {
			// Or handle error properly
			return "User name/id alreay exists!";
		} catch (SQLException e2) {
			e2.printStackTrace();
			return "User couldnt be added!";
		}

	}

	private String generatePassword() {

		Random rand = new Random();
		int password, count = 0;
		while (count < 10) {
			password = rand.nextInt(9000) + 1000; // 0–8999 + 1000 = 1000–9999
			String sql = "SELECT password FROM `users` WHERE password = ?";
			try {
				// puts the id inside the "?" that is at the end of the query above
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, password);
				ResultSet rs = ps.executeQuery();
				if (!rs.next()) {
					return String.valueOf(password);
				}

			} catch (Exception e) {
			} finally {
				count++;
			}

		}
		return "ERROR";
	}

	/**
	 * Insert user to the database.
	 */
	public String insertResToDB(String order_date, String id, String order_hour) {

		orderNumber = this.getMaxOrderNumber();
		orderNumber++;
		String[] time = order_hour.split(":");
		int newTime = Integer.parseInt(time[0]) + 4;
		String finish_time = String.format("%d:%s", newTime, time[1]);
		String sql = "INSERT INTO `table_order` (order_number, order_date, order_time,finish_time, confirmation_code, subscriber_id, date_of_placing_an_order) "
				+ "VALUES (?,?,?,?,?,?,?);";
		// checks if an order was already placed in the same date by the given user

		if (orderByDateExists(id, order_date))
			return "Order Already exists for the given date, choose another date.";

		if (!AvailableSpots(order_date)) {
			return "Not enough space at this date!";
		}
		Random rand = new Random();
		int confirCode = rand.nextInt(9000) + 1000; // 0–8999 + 1000 = 1000–9999
		LocalDate date = LocalDate.now();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, orderNumber);
			ps.setString(2, order_date);
			ps.setString(3, order_hour);
			ps.setString(4, finish_time);
			ps.setInt(5, confirCode);
			ps.setInt(6, Integer.parseInt(id));
			ps.setString(7, date.toString());
			ps.executeUpdate();
			System.out.println("Database updated successfully.");
			if (listener != null) {
				listener.onDatabaseMessage("Database updated successfully.");
			}
			return "New order added Succsussfully" + "\nYour order number: " + orderNumber
					+ "\nYour confirmation code: " + confirCode;
		} catch (Exception e) {
			e.printStackTrace();
			return "Order couldnt be added!";
		}

	}

	// available spaces for a specific
	public int AvailableSpaces(String order_date) {
		String sql = "SELECT COUNT(*) FROM table_order WHERE order_date = ?;";
		int count = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, order_date);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
			return maxSpace - count;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	public boolean AvailableSpots(String date) {
		int count = AvailableSpaces(date);
		if (((float) (count) / maxSpace) >= 0.4) {
			return true;
		}
		return false;
	}

	// checks if an order for a user has already been set in a certain date
	public boolean orderByDateExists(String id, String date) {
		String sql = "SELECT order_number FROM table_order WHERE subscriber_id = ? AND order_date = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(id));
			ps.setString(2, date);

			ResultSet rs = ps.executeQuery();
			if (rs.next())
				// The order_number exists at the given date for the given user
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// get password of user by email
	public String getPassword(String email) {
		String sql = "SELECT password FROM users WHERE email = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, email);

			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getString(1);
			else
				return "";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
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
	public String getUserRoleById(String password, String name) {
		String sql = "SELECT role,id FROM users WHERE password = ? AND name = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, password);
			ps.setString(2, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("role") + " " + rs.getString("id");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return "null";
	}

	// get the maximum order number so the numbers are going up
	public int getMaxOrderNumber() {
		String orderNum;
		String sql = "SELECT MAX(order_number) FROM table_order;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				orderNum = rs.getString("MAX(order_number)");
				return Integer.parseInt(orderNum);
			} else
				return 1000;
		} catch (SQLException e) {
			e.printStackTrace();
			return 1000;
		}
	}

	public int Count() {
		String sql = "SELECT COUNT(*) FROM table_order;";
		int count = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
			return count;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//Car inserted, change boolean car_inserted for specific code order
	public boolean CarInserted(String Code) {
	    String selectSQL = "SELECT car_inserted FROM table_order WHERE confirmation_code = ? ";
	    String updateSQL = "UPDATE table_order SET car_inserted = 1 WHERE confirmation_code = ? AND order_date = ? AND order_time BETWEEN ? AND ? ";

	    try {
	        int codeInt = Integer.parseInt(Code); // convert string to int
	        PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
	        selectStmt.setInt(1, codeInt);
	        ResultSet rs = selectStmt.executeQuery();
	        if (rs.next()) {
	            int carInserted = rs.getInt("car_inserted");
	            if (carInserted == 0) {
	                // Set car_inserted to 1
	                try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
	                	LocalTime now = LocalTime.now();
	                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	                    String formattedTime = now.format(formatter);
	                    String timeBefore = null;
	                    String timeAfter = null;
	                    String Time[] = formattedTime.split(":");
	                    if(Integer.parseInt(Time[1])==0) {
	                    	if(Integer.parseInt(Time[0])>9)
	                    		timeBefore = String.format("%d:%s", Integer.parseInt(Time[0])-1,"45");
	                    	else
	                    		timeBefore = String.format("09:00");
	                    	timeAfter =String.format("%s:%s", Time[0],"15");
	                    }else if(Integer.parseInt(Time[1])==45) {
	                    	timeBefore =String.format("%s:%s", Time[0],"30");
	                    	if(Integer.parseInt(Time[0])<21)
	                    		timeAfter =String.format("%d:%s", Integer.parseInt(Time[0])+1,"00");
	                    	else
	                    		timeAfter = String.format("21:00");
	                    }else {
	                    	if(Integer.parseInt(Time[1])==15)
	                    		timeBefore =String.format("%s:00", Time[0]);
	                    	else
	                    		timeBefore =String.format("%s:%d", Time[0],Integer.parseInt(Time[1])-15);
	                    	timeAfter = String.format("%s:%d",Time[0],Integer.parseInt(Time[1])+15);
	                    }
	                    
	                    updateStmt.setInt(1, codeInt);
	                    updateStmt.setString(2, LocalDate.now().toString());
	                    updateStmt.setString(3, timeBefore);
	                    updateStmt.setString(4, timeAfter);
	                    int g = updateStmt.executeUpdate();
	                    if(g==0)
	                    	return false;
	                    return true;
	                } catch(Exception e) {
	                    e.printStackTrace();
	                    return false;
	                }
	            } else {
	                // Already inserted
	                return false;
	            }
	        } else {
	            // No such confirmation_code found
	            return false;
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	
	
	public boolean emailExists(String email) {
		try {
			if (conn == null || conn.isClosed()) {
				connectToDB();
			}

			String sql = "SELECT 1 FROM users WHERE email = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, email.trim().toLowerCase());
			ResultSet rs = stmt.executeQuery();
			return rs.next(); // true if a match is found
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Map<Integer, Integer> getDailyParkingUsage(int month, int year) {
		Map<Integer, Integer> dailyData = new TreeMap<>();

		try {
			if (conn == null || conn.isClosed()) {
				connectToDB();
			}

			String sql = "SELECT DAY(order_date) AS day_of_month, COUNT(*) AS total_orders "
					+ "FROM table_order t JOIN users u ON t.subscriber_id = u.id "
					+ "WHERE u.role = 'user' AND MONTH(order_date) = ? AND YEAR(order_date) = ? "
					+ "GROUP BY DAY(order_date) ORDER BY DAY(order_date)";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, month);
			stmt.setInt(2, year);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				dailyData.put(rs.getInt("day_of_month"), rs.getInt("total_orders"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dailyData;
	}
	
	
	public Map<Integer, ParkingTimingStats> getDailyParkingTimingReport(int month, int year) {
	    Map<Integer, ParkingTimingStats> result = new TreeMap<>();
	    try {
	        if (conn == null || conn.isClosed()) {
	            connectToDB();
	        }

	        String sql = "SELECT " +
	                     "  DAY(STR_TO_DATE(o.order_date, '%Y-%m-%d')) AS day, " +
	                     "  COUNT(CASE WHEN o.was_extended = 1 THEN 1 ELSE NULL END) AS extensions, " +
	                     "  COUNT(CASE WHEN u.was_late = 1 THEN 1 ELSE NULL END) AS late_users " +
	                     "FROM table_order o " +
	                     "JOIN users u ON o.subscriber_id = u.id " +
	                     "WHERE MONTH(STR_TO_DATE(o.order_date, '%Y-%m-%d')) = ? " +
	                     "  AND YEAR(STR_TO_DATE(o.order_date, '%Y-%m-%d')) = ? " +
	                     "GROUP BY day " +
	                     "ORDER BY day;";

	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, month);
	        ps.setInt(2, year);

	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            int day = rs.getInt("day");
	            int extensions = rs.getInt("extensions");
	            int lates = rs.getInt("late_users");

	            result.put(day, new ParkingTimingStats(extensions, lates));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}



	public Map<String, Integer> getLateUsersByName(int month, int year) {
	    Map<String, Integer> result = new HashMap<>();

	    try {
	        if (conn == null || conn.isClosed()) {
	            connectToDB();
	        }

	        String sql = "SELECT u.name, u.was_late " +
	                     "FROM users u " +
	                     "WHERE u.role = 'user' AND u.was_late > 0";

	        PreparedStatement ps = conn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            String name = rs.getString("name");
	            int count = rs.getInt("was_late");
	            result.put(name, count);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}

	public Map<String, Integer> getLateUsersForMonth(int month, int year) {
	    Map<String, Integer> lateMap = new LinkedHashMap<>();
	    try {
	        if (conn == null || conn.isClosed()) {
	            connectToDB();
	        }

	        String sql = "SELECT name, was_late " +
	                     "FROM users " +
	                     "WHERE role = 'user' AND was_late > 0";

	        PreparedStatement ps = conn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            String name = rs.getString("name");
	            int lateCount = rs.getInt("was_late");
	            lateMap.put(name, lateCount);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return lateMap;
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
