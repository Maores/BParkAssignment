// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import common.ChatIF;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF {
	// Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;

	// Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	ChatClient client;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the ClientConsole UI.
	 *
	 * @param host The host to connect to.
	 * @param port The port to connect on.
	 */
	public ClientConsole(String host, int port) {
		try {
			client = new ChatClient(host, port, this);
			client.sendToServer(host);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
	}

	// Instance methods ************************************************

	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to the client's message handler.
	 */
	public void accept() {

		try {
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
			String message;
			while (true) {
				
				// BPark menu
				message = fromConsole.readLine();
				System.out.println();
				if (message.equals("1")) {
					client.handleMessageFromClientUI("VIEW_DATABASE");
				}
				else if (message.equals("2")) {
					while (true) {
						System.out.print("Enter order number: ");
						String orderNumber = fromConsole.readLine();

						System.out.print("Enter new parking space: ");
						String parkingSpace = fromConsole.readLine();

						System.out.print("Enter new order date (YYYY-MM-DD): ");
						String orderDate = fromConsole.readLine();

						String updateMessage = "UPDATE_ORDER " + orderNumber + " " + parkingSpace + " " + orderDate;
						client.handleMessageFromClientUI(updateMessage);
						//Thread.sleep(100); 
						if (!client.getLastServerResponse().equals("Invalid order number Try again...")) {
							break;
						}
					}
				}
				else if(message.equals("3")) {
					client.closeConnection();
					break;
				}
				//Thread.sleep(900); 
			}

		} catch (Exception ex) {
			System.out.println("Unexpected error while reading from console!");
		}
	}

	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the screen.
	 *
	 * @param message The string to be displayed.
	 */
	public void display(String message) {
		System.out.println(message); // Rafi - Delete "> "
	}

	/*
	 * 
	 * 
	 */
	public void handleTable() {

	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the Client UI.
	 *
	 * @param args[0] The host to connect to.
	 */
	public static void main(String[] args) {
		String host = "";
		int port = 0; // The port number

		try {
			host = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "localhost";
		}
		ClientConsole chat = new ClientConsole(host, DEFAULT_PORT);
		chat.accept(); // Wait for console data

	}
}
//End of ConsoleChat class
