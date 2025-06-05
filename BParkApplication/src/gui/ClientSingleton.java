package gui;

import gui.GUIParkingClient;
import common.ChatIF;

public class ClientSingleton {
    private static GUIParkingClient instance;

    public static GUIParkingClient getInstance(ChatIF gui) {
        if (instance == null) {
            instance = new GUIParkingClient("localhost", 5555, gui);
        } else {
            instance.setGui(gui);
        }
        return instance;
    }
} 