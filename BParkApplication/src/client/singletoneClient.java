package client;

import common.ChatIF;
import javafx.scene.control.Alert;

/**
 * Provides a singleton access point for the {@link ChatClient} instance.
 * <p>
 * This class ensures that only one instance of {@code ChatClient} is created
 * during runtime. It allows connecting to the server using default or manually
 * specified host and port.
 * </p>
 */
public class singletoneClient {
	/** The default port number used to connect to the server. */
	final public static int DEFAULT_PORT = 5555;
	/** The default host address used to connect to the server. */
	final public static String DEFAULT_HOST = "localhost";
	/** Singleton instance of {@link ChatClient}. */
	private ChatClient instance;

	/**
	 * Returns the singleton {@link ChatClient} instance. If it doesn't exist, a new
	 * one is created using default host and port values.
	 * <p>
	 * If the instance already exists, it simply updates the client UI reference.
	 * </p>
	 *
	 * @param clientUI the UI handler that receives messages from the server
	 * @return the {@code ChatClient} instance
	 */
	public ChatClient getInstance(ChatIF clientUI) {
		if (instance == null) {
			try {
				instance = new ChatClient(DEFAULT_HOST, DEFAULT_PORT, clientUI);
			} catch (Exception e) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Server is OFFLINE!\n" + e.getMessage());
				alert.showAndWait();
			}
		} else {

			instance.setClientUI(clientUI);
		}
		return instance;
	}

	/**
	 * Manually creates and returns a new {@link ChatClient} instance using the
	 * given host and port.
	 * <p>
	 * This will override any existing client instance.
	 * </p>
	 *
	 * @param PORT     the port number to connect to
	 * @param HOST     the hostname or IP address of the server
	 * @param clientUI the UI handler that receives messages from the server
	 * @return a new {@code ChatClient} instance
	 */
	public ChatClient setManualConnection(int PORT, String HOST, ChatIF clientUI) {
		try {
			instance = new ChatClient(HOST, PORT, clientUI);
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Server is OFFLINE!\n" + e.getMessage());
			alert.showAndWait();
		}

		return instance;
	}
}
