package client;

import java.io.IOException;

import client.ChatClient;
import common.ChatIF;

public class singletoneClient {
    private static ChatClient instance;

    public static ChatClient getInstance(ChatIF clientUI) {
        if (instance == null) {
            try {
				instance = new ChatClient("localhost", 5555, clientUI);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            // Rebind UI interface when switching screens
            instance.setClientUI(clientUI);
        }
        return instance;
    }
}
