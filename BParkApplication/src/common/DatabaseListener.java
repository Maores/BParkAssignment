package common;

/**
 * Interface for receiving database-related notifications. This allows
 * separation between the database layer and the GUI layer.
 */
public interface DatabaseListener {
	/**
	 * Called when a general database message needs to be logged
	 * 
	 * @param message The message to log
	 */
	void onDatabaseMessage(String message);

	/**
	 * Called when a database error occurs
	 * 
	 * @param error The error message
	 */
	void onDatabaseError(String error);

	/**
	 * Called when the database connection status changes
	 * 
	 * @param connected true if connected, false if disconnected
	 */
	void onDatabaseConnectionChange(boolean connected);
}