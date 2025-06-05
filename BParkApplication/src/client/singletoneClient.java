package client;

import java.io.IOException;

import common.ChatIF;

public class singletoneClient {
	private static ChatClient instance;

    public static ChatClient getInstance(ChatIF gui) {
        if (instance == null) {
            try {
				instance = new ChatClient("localhost", 5555,  gui);
			} catch (IOException e) {
				System.out.println("SingletoneClient Error "+ e.getMessage());
				e.printStackTrace();
			}
        }
        else {
        	try {
				instance = new ChatClient("localhost", 5555,  gui);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//instance.setClientUI(gui);
        }
        return instance;
    }
}
