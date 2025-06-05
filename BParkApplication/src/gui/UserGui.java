package gui;

import client.ChatClient;
import common.ChatIF;

public class UserGui implements ChatIF {

	private ChatClient client;
	private ChatIF gui;

	public UserGui(String host, int port, ChatIF gui) {
		this.gui = gui;
		try {
			client = new ChatClient(host, port, this);
			//display("Connected!");
		} catch (Exception e) {
			display("Error: Can't setup connection!");
		}
	}

	public void sendMessage(String msg) {
		try {
			
			client.handleMessageFromClientUI(msg);
		} catch (Exception e) {
			
			display("ERROR: Could not send message to server.");
		}
	}

	public void search(String msg) {
		if (!msg.isEmpty()) {
			sendMessage("SEARCH_ORDER " + msg);
		} else {
			display("Search bar is empty.");
		}
	}

	public void display(String message) {
		gui.display(message);
	}

	public void connect(String host, int port) {
		try {
			client = new ChatClient(host, port, this);
			System.out.println(client);
			display("Connected!");
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

	public void setGui(ChatIF gui) {
		this.gui = gui;
	}
}
