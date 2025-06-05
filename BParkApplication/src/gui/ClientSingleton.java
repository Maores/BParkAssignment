package gui;

import java.io.IOException;

import client.ChatClient;
import common.ChatIF;

public class ClientSingleton {
    private static ChatClient instance;

    public static ChatClient getInstance(ChatIF clientUI) throws IOException {
        if (instance == null) {
            instance = new ChatClient("localhost", 5555, clientUI);
        } else {
            // Rebind UI interface when switching screens
            instance.setClientUI(clientUI);
        }
        return instance;
    }
}
