package gui;

import common.ChatIF;

public class ClientUIController implements ChatIF {
    private static ClientUIController instance;
    private ChatIF activeScreen;

    private ClientUIController() {}

    public static ClientUIController getInstance() {
        if (instance == null) {
            instance = new ClientUIController();
        }
        return instance;
    }

    public void setActiveScreen(ChatIF screen) {
        this.activeScreen = screen;
    }

    @Override
    public void display(String message) {
        if (activeScreen != null) {
            activeScreen.display(message);
        } else {
            // Optionally log or buffer messages
            System.out.println("[ClientUIController] No active screen to display: " + message);
        }
    }
} 