// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import db.DBController;
import gui.serverGuiController;
import common.DatabaseListener;
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
	private DBController dbController = null;  // Store reference to singleton

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
		String log;//Log message from DB 
		// System.out.println("Message received: " + msg + " from " + client);
		
		// Get singleton instance - Server passes itself as listener
		DBController db = DBController.getInstance(this);
		try {
			// Handling "View database"
			if (message.equals("VIEW_DATABASE")) {
				//gather data from DB
				// Connection is already established in singleton
				String data = db.getDatabaseAsString();
				client.sendToClient(data);
			} else if (message.startsWith("UPDATE_ORDER")) {
				//getting data from DB into parts
				String[] parts = message.split(" ");
				String orderNumber = parts[1];
				String parkingSpace = parts[2];
				String orderDate = parts[3];
				if (db.checkDB(orderNumber)) {
					//updates the DB
					if ((log = db.updateDB(parkingSpace, orderDate, orderNumber)) == "true") {
						client.sendToClient("Update successful for order number " + orderNumber);
					} else {
						client.sendToClient(log.substring(0, log.length() - 9) + ".");
					}
				} else {
					client.sendToClient("Invalid order number Try again...");
				}
			} 
			///SEARCH specific parking by id
			else if (message.startsWith("SEARCH_ORDER")) {
				//get id from the message
				String[] parts = message.split(" ");
				String orderNumber = parts[1];
				//check if ID exists in DB
				if (db.checkDB(orderNumber)) {
					String data = db.SearchID(orderNumber);
					client.sendToClient(data);
				}
				//ID not found in DB
				else {
					client.sendToClient("Order not found in database.");
				}
			}
			else if (message.startsWith("LOGIN")) {
				//get id from the message
				String[] parts = message.split(" ");
				String id  = parts[1];
				String role = "role " + db.getUserRoleById(id);
				client.sendToClient(role);//Send the role to client
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
				switch (reportType) {
					case "DAILY_REPORT":
						reportData = "=== DAILY PARKING REPORT ===\n";
						reportData += db.getDatabaseAsString();
						reportData += "\nGenerated at: " + new java.util.Date();
						break;
					case "PARKING_USAGE_REPORT":
						// Count occupied spaces
						String allData = db.getDatabaseAsString();
						int totalSpaces = allData.split("\n").length - 1;
						reportData = "=== PARKING USAGE REPORT ===\n";
						reportData += "Total occupied spaces: " + totalSpaces + "\n";
						reportData += "Report generated at: " + new java.util.Date();
						break;
					default:
						reportData = "Report type not implemented: " + reportType;
				}
				
				client.sendToClient(reportData);
			}
			// Handle parking status request
			else if (message.equals("GET_ALL_PARKING_STATUS")) {
				if (guiController != null) {
					guiController.appendMessage("Parking status requested from " + client.getInetAddress());
				}
				
				// Get all parking data
				String allParkingData = db.getDatabaseAsString();
				client.sendToClient("=== PARKING STATUS ===\n" + allParkingData);
			}
			//if message from client is not ViewDB or UpdateDB
			else {
				if (guiController != null) {
					//sends the message to the gui
					guiController.appendMessage("Host: " + message + "\nIP: " + client + "\nStatus: Connected");
					System.out.println("Host: " + message + "\nIP: " + client + "\nStatus: Connected");
				}
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
			guiController.appendMessage("Server listening for connections on port " + getPort());
		}
		
		// Initialize database connection once when server starts
		// Pass 'this' as the listener to receive database notifications
		dbController = DBController.getInstance(this);
		String result = dbController.connectToDB();
		System.out.println("Database initialization: " + result);
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
		if (guiController != null) {
			guiController.appendMessage("Server has stopped listening for connections.");
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
