
package gui;

import client.ChatClient;
import common.ChatIF;

public class GUIParkingClient implements ChatIF {

	private ChatClient client;
	private ParkingSystemGUI gui;

	public GUIParkingClient(String host, int port, ParkingSystemGUI gui) {
		
		this.gui = gui;
		try {
			client = new ChatClient(host, port, this);
			display("Connected!");
			client.sendToServer(host);

		} catch (Exception e) {
			display("Error: Can't setup connection!");

		}

	}

	public void sendMessage(String msg) {
		if (client != null && client.isConnected()) {
			client.handleMessageFromClientUI(msg);

		} else {
			display("ERROR:The server is offline.");
		}

	}
	public void search(String msg) {
		if (client != null && client.isConnected()) {
			client.handleMessageFromClientUI(msg);
			if(!msg.isEmpty()) {
				client.handleMessageFromClientUI("SEARCH_ORDER "+msg);
			}
			else {
				display("Search bar is empty.");
			}
			
		} else {
			display("ERROR:The server is offline.");
		}

	}

	public void display(String message) {
		gui.displayMessage(message);
	}

	public void connect(String host, int port) {
		try {
			
			client = new ChatClient(host, port, this);
			System.out.println(client);
			display("Connected!");
			client.sendToServer(host);
		} catch (Exception e) {
			display("Error: Can't setup connection!");
		}

	}

	public String getLastServerResponse() {
		return client.getLastServerResponse();
	}

	public void quit() {
		client.quit();
	}
}
