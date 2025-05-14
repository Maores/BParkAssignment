// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

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
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	private serverGuiController guiController;
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
		boolean flag = false;
		String log;
		// System.out.println("Message received: " + msg + " from " + client);
		DBController db = new DBController(guiController);
		try {
			// Handling "View database"
			if (message.equals("VIEW_DATABASE")) {
				//gather data from DB
				if ((log = db.connectToDB()).startsWith("Database")) {
					client.sendToClient(log);
					String data = db.getDatabaseAsString();
					client.sendToClient(data);
				} else {
					client.sendToClient(log);
				}
			} else if (message.startsWith("UPDATE_ORDER")) {
				//getting data from DB into parts
				if ((log = db.connectToDB()).startsWith("Database")) {
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
				} else {
					client.sendToClient(log);
				}
			//if message from client is not ViewDB or UpdateDB - flag so it wont close the server in finally
			} else {
				flag = true;
				if (guiController != null) {
					//sends the message to the gui
					guiController.appendMessage("Host: " + message + "\nIP: " + client + "\nStatus: Connected");
					System.out.println("Host: " + message + "\nIP: " + client + "\nStatus: Connected");
				}
				
			}
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			if (!flag) {
				db.close();
			}
		}
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
