package client;

import java.io.IOException;

import common.ChatIF;

public class singletoneClient {
    private static ChatClient instance;

    public static ChatClient getInstance(ChatIF clientUI) {
        if (instance == null) {
            try {
				instance = new ChatClient("localhost", 5555, clientUI);
			} catch (IOException e) {
				e.printStackTrace();
			}
        } else { 	
        	  try {
  				instance = new ChatClient("localhost", 5555, clientUI);
  			} catch (IOException e) {
  				e.printStackTrace();
  			}
        		//instance.setClientUI(clientUI);

        }
        return instance;
    }
}
