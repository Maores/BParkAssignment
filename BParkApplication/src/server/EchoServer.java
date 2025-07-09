// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import common.DatabaseListener;
import common.MailSender;
import common.ParkingReportWrapper;
import common.ParkingTimingStats;
import common.SubscriberReportWrapper;
import db.DBController;
import gui.serverGuiController;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer implements DatabaseListener {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	private serverGuiController guiController;
	private DBController dbController = null; // Store reference to singleton
	private String roleConnected;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port, serverGuiController guiController) {
		super(port);
		this.guiController = guiController;

	}

	// Instance methods ************************************************

	/**
	 * Implementation of DatabaseListener interface
	 */
	@Override
	public void onDatabaseMessage(String message) {
		System.out.println("[DB] " + message);
		if (guiController != null) {
			guiController.appendMessage("[DB] " + message);
		}
	}

	@Override
	public void onDatabaseError(String error) {
		System.err.println("[DB ERROR] " + error);
		if (guiController != null) {
			guiController.appendMessage("[DB ERROR] " + error);
		}
	}

	@Override
	public void onDatabaseConnectionChange(boolean connected) {
		String status = connected ? "CONNECTED" : "DISCONNECTED";
		System.out.println("[DB Connection] " + status);
		if (guiController != null) {
			guiController.appendMessage("[DB Connection] " + status);
		}
	}

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
//		if (guiController != null) {
//			guiController.appendMessage("Message received: " + msg.toString());
//		}
		String message = msg.toString();
		String log;// Log message from DB
		// System.out.println("Message received: " + msg + " from " + client);

		// Get singleton instance - Server passes itself as listener
		DBController db = DBController.getInstance(this);
		try {
			// Handling "View database"
			if (message.equals("VIEW_DATABASE")) {
				guiController.appendMessage("[" + roleConnected + "] Fetching data from database..");
				// gather data from DB
				String data = db.getDatabaseAsString();
				client.sendToClient(data);
			} else if (message.equals("VIEW_USERDATABASE")) {
				guiController.appendMessage("[" + roleConnected + "] Fetching user data from database..");
				// gather data from DB
				String userData = db.getUserDatabaseAsString();
				client.sendToClient(userData);
				// gather mail and pass info from DB
			} else if (message.startsWith("GET_USER_INFO")) {
				String[] parts = message.split(" ");
				String id = parts[1];
				String userInfo = db.getMailPhoneDatabaseAsString(id);
				client.sendToClient("INFO "+userInfo);
				
			} else if (message.startsWith("UPDATE_ORDER")) {
				guiController.appendMessage("[" + roleConnected + "] Updating order database..");
				// getting data from DB into parts
				String[] parts = message.split(" ");
				String orderNumber = parts[1];
				if (db.checkDB(orderNumber)) {
					// updates the DB
					if ((log = db.updateDB(orderNumber)) == "true") {
						client.sendToClient("order number: " + orderNumber + " - Your request for an extension has been approved.");
					} else {
						client.sendToClient("order number: " + orderNumber + " - Your request for an extension has been rejected.");
					}
				} else {
					client.sendToClient("Invalid order number Try again...");
				}
			} else if (message.startsWith("UPDATE_USER_INFO")) {
				guiController.appendMessage("[" + roleConnected + "] Updating user database..");
				// getting data from DB into parts
				String[] parts = message.split(" ");
				String phone = parts[1];
				String email = parts[2];
				String id = parts[3];
				// updates the DB
				if ((log = db.updateUserInfoDB(phone, email, id)) == "true") {
					client.sendToClient("Update user information succeded");
				} else {
					client.sendToClient(log);
				}

			}
			/// SEARCH parking orders by id
			else if (message.startsWith("SEARCH_ORDER")) {
				guiController.appendMessage("[" + roleConnected + "] Searching order in database..");
				// get id from the message
				String[] parts = message.split(" ");
				String orderNumber = parts[1];
				// check if ID exists in DB
				if (db.checkDB(orderNumber)) {
					String data = db.SearchOrder(orderNumber);
					client.sendToClient(data);
				}
				// ID not found in DB
				else {
					client.sendToClient("Order not found in database.");
				}
			}
			/// SEARCH parking orders by id
			else if (message.startsWith("SEARCH_USER")) {
				guiController.appendMessage("[" + roleConnected + "] Searching user in database..");
				// get id from the message
				String[] parts = message.split(" ");
				String userID = parts[1];
				// check if ID exists in DB
				if (db.checkUserDB(userID)) {
					String data = db.SearchID(userID);
					client.sendToClient(data);
				}
				// ID not found in DB
				else {
					client.sendToClient("User not found in database.\n");
				}
			} else if (message.startsWith("LOGIN")) {

				// get id from the message
				String[] parts = message.split(" ");
				String id = parts[1];
				String name = parts[2];
				String role = db.getUserRoleById(id, name);

				if (role.equals("null")) {
					client.sendToClient("ERROR:UserName/Password is wrong!");
				} else {
					String roleS = role.split(" ")[0];
					guiController.appendMessage("[" + roleS + "] Login to the system");
					client.sendToClient("role " + role);// Send the role to client
				}

			} else if (message.startsWith("RESET_PASSWORD")) {
				String[] parts = message.split(" ");
				String email = parts[1];
				String mailMessage = "";
				if (db.emailExists(email)) {
					// Simulate sending reset email
					System.out.println("Password reset email sent to: " + email);
					String password = db.getPassword(email);
					if (!password.equals("")) {
						mailMessage = MailSender.sendEmail(email, "BPark Password", "The Password: " + password);
					}
				}
				client.sendToClient(mailMessage);
			}
			// Handle new report generation command
			else if (message.startsWith("GENERATE_REPORT")) {
				String[] parts = message.split(" ");
				String reportType = parts.length > 1 ? parts[1] : "DEFAULT";

				// Log the request
				if (guiController != null) {
					guiController.appendMessage("Report requested: " + reportType + " from " + client.getInetAddress());
				}

				// Generate report based on type
				String reportData = "";
				int totalSpaces = db.Count();
				reportData = "=== PARKING USAGE REPORT ===\n";
				reportData += "Total Orderes Created: " + totalSpaces + "\n";
				reportData += "Available Spaces for today: " + db.AvailableSpaces(LocalDate.now().toString()) + "\n";
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formattedDateTime = LocalDateTime.now().format(formatter);
				reportData += "Report generated at: " + formattedDateTime;

				client.sendToClient(reportData);
			} else if (message.startsWith("ADD_USER")) {
				guiController.appendMessage("[" + roleConnected + "] Add new user");
				String[] parts = message.split(" ");
				String id = parts[1];
				String name = parts[2];
				String phone = parts[3];
				String email = parts[4];
				String succes = db.insertUserToDB(name, id, phone, email);
				client.sendToClient(succes);// Send the role to client
			} else if (message.startsWith("ADD_ORDER")) {
				guiController.appendMessage("[" + roleConnected + "] Add new order");
				String[] parts = message.split(" ");
				String id = parts[1];
				String orderDate = parts[2];
				String orderHour = parts[3];
				String succes = db.insertResToDB(orderDate, id, orderHour);

				client.sendToClient(succes);// Send the role to client
			} else if (message.startsWith("VIEW_DATABASE_ID")) {
				guiController.appendMessage("[" + roleConnected + "] View user order");
				// gather data from DB
				String[] parts = message.split(" ");
				String id = parts[1];
				// Connection is already established in singleton
				String data = db.getDatabaseByIDAsString(id);
				client.sendToClient(data);
			}else if (message.startsWith("AVAILABLE_SPOTS")) {
				// gather data from DB
				String[] parts = message.split(" ");
				String date = parts[1];
				// Connection is already established in singleton
				String data =db.checkSpaceAvailability(date);
				client.sendToClient(data);
			}
			// CONNECTION host_name role
			else if (message.startsWith("CONNECTION")) {
				String[] parts = message.split(" ");
				if (guiController != null) {
					roleConnected = parts[2];
					// sends the message to the gui
					guiController.appendMessage("+++++++++++++++++++++++++++++++++++");
					guiController.appendMessage(
							"Host: " + parts[1] + "\nIP: " + client + "\nRole: " + parts[2] + "\nStatus: Connected");
					guiController.appendMessage("+++++++++++++++++++++++++++++++++++");
					System.out.println(
							"Host: " + parts[1] + "\nIP: " + client + "\nRole: " + parts[2] + "\nStatus: Connected");
				}
			} else if (message.startsWith("#CheckEmailReset#")) {
				guiController.appendMessage("[SERVER] Checking if email exists in DB..");
				String email = message.replace("#CheckEmailReset#", "").trim();
				boolean exists = db.emailExists(email);

				try {
					if (exists) {
						client.sendToClient("EMAIL_EXISTS");
					} else {
						client.sendToClient("EMAIL_NOT_FOUND");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (message.startsWith("CAR_INSERT")) {
				String[] parts = message.split(" ");
				String ConfirmationCode = parts[1];
				boolean inserted = db.CarInserted(ConfirmationCode);
				
				try {
					if (inserted) {
						client.sendToClient("CAR_INSERTED");
					} else {
						client.sendToClient("CAR_NOT_INSERTED");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			else if (message.startsWith("#GET_PARKING_TIMING_REPORT")) {
			    String[] parts = message.split(" ");
			    int month = Integer.parseInt(parts[1]);
			    int year = Integer.parseInt(parts[2]);

			    Map<Integer, ParkingTimingStats> reportData = db.getDailyParkingTimingReport(month, year);
			    Map<String, Integer> lateUsers = db.getLateUsersByName(month, year);

			    ParkingReportWrapper wrapper = new ParkingReportWrapper(reportData, lateUsers);

			    client.sendToClient(wrapper);
			}
			
			else if (msg instanceof String && ((String) msg).startsWith("GET_SUBSCRIBER_REPORT")) {
			    String[] parts = ((String) msg).split(" ");
			    int month = Integer.parseInt(parts[1]);
			    int year = Integer.parseInt(parts[2]);
			    
			    Map<Integer, Integer> report = DBController.getInstance(null).getDailyParkingUsage(month, year);
			    SubscriberReportWrapper wrapper = new SubscriberReportWrapper(report);
			    
			    client.sendToClient(wrapper);
			}



		} catch (Exception e) {
			e.printStackTrace();
		}
		// No need to close connection - it stays open in singleton
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
		if (guiController != null) {
			guiController.appendMessage("[Server] Server listening for connections on port " + getPort());
		}

		// Initialize database connection once when server starts
		// Pass 'this' as the listener to receive database notifications
		dbController = DBController.getInstance(this);
		String result = dbController.connectToDB();
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
		if (guiController != null) {
			guiController.appendMessage("[Server] Server has stopped listening for connections.");
		}
		// Close database connection only when server stops
		DBController.closeAndReset();
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
//	public static void main(String[] args) {
//		int port = 0; // Port to listen on
//
//		try {
//			port = Integer.parseInt(args[0]); // Get port from command line
//		} catch (Throwable t) {
//			port = DEFAULT_PORT; // Set port to 5555
//		}
//
//		EchoServer sv = new EchoServer(port);
//
//		try {
//			sv.listen(); // Start listening for connections
//		} catch (Exception ex) {
//			System.out.println("ERROR - Could not listen for clients!");
//		}
//	}
}
//End of EchoServer class
