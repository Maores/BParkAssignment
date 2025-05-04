
package client;

import common.ChatIF;

public class GUIParkingClient implements ChatIF {

    private ChatClient client;
    private ParkingSystemGUI gui;

    public GUIParkingClient(String host, int port, ParkingSystemGUI gui) {
        this.gui = gui;
        try {
            client = new ChatClient(host, port, this);
            client.sendToServer(host);
        } catch (Exception e) {
            display("Error: Can't setup connection! Terminating client.");
            System.exit(1);
        }
    }

    public void sendMessage(String msg) {
        client.handleMessageFromClientUI(msg);
    }

    public void display(String message) {
        gui.displayMessage(message);
    }

    public String getLastServerResponse() {
        return client.getLastServerResponse();
    }
    public void quit() {
        client.quit();
    }
}
