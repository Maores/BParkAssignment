// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import java.io.IOException;

import common.ChatIF;
import common.ParkingReportWrapper;
import common.SubscriberReportWrapper;
import gui.ParkingTimingReportController;
import gui.ReportController;
import ocsf.client.AbstractClient;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;

	private String lastServerResponse = "";
	public static boolean awaitResponse = false;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */

	@Override
	public void handleMessageFromServer(Object msg) {
		lastServerResponse = msg.toString();

		if (msg instanceof ParkingReportWrapper) {
			ParkingReportWrapper wrapper = (ParkingReportWrapper) msg;
			if (clientUI instanceof ParkingTimingReportController) {
				((ParkingTimingReportController) clientUI).loadReportFromServer(wrapper);
			}
			return;
		}

		if (msg instanceof SubscriberReportWrapper) {
			if (clientUI instanceof ReportController) {
				((ReportController) clientUI).loadReportFromServer((SubscriberReportWrapper) msg);
				return;
			}
		}

		clientUI.handleMessageFromServer(lastServerResponse);
	}

	public String getLastServerResponse() {
		return lastServerResponse;
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {
		try {
			sendToServer(message);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			clientUI.handleMessageFromServer("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	public void requestParkingTimingReport(int month, int year) {
		String msg = "#GET_PARKING_TIMING_REPORT " + month + " " + year;
		try {
			sendToServer(msg);
		} catch (IOException e) {
			clientUI.handleMessageFromServer("Failed to request report.");
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	public ChatIF getClientUI() {
		return clientUI;
	}

	public void setClientUI(ChatIF clientUI) {
		this.clientUI = clientUI;
	}
}
//End of ChatClient class
