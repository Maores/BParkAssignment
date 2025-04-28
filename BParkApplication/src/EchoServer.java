// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.ArrayList;

import ocsf.server.*;

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

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
		
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String message = msg.toString();	
		boolean flag = false;
		//System.out.println("Message received: " + msg + " from " + client);	
		DBController db = new DBController();
		try {
			// Handling "View database"
			if (message.equals("VIEW_DATABASE")) {
				db.connectToDB();
				String data = db.getDatabaseAsString();
				client.sendToClient(data);
			} else if (message.startsWith("UPDATE_ORDER")) {
				db.connectToDB();
				String[] parts = message.split(" ");
				String orderNumber = parts[1];
				String parkingSpace = parts[2];
				String orderDate = parts[3];
				if(db.checkDB(orderNumber)) {
					db.updateDB(parkingSpace, orderDate, orderNumber);
					client.sendToClient("Update successful for order number " + orderNumber);
				}
				else {
					client.sendToClient("Invalid order number Try again...");
				}
			}
			else {
				flag=true;
				System.out.println("Host: " + message +"\nIP: " + client + "\nStatus: connected");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(!flag) {
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

		
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
//End of EchoServer class
