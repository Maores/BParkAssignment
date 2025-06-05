package gui;

public class ClientSingleton {
    private static GUIParkingClient instance;

    public static GUIParkingClient getInstance() {
        if (instance == null) {
            instance = new GUIParkingClient("localhost", 5555, ClientUIController.getInstance());
        }
        return instance;
    }
} 