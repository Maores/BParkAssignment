package gui;

/**
 * A simple data holder class for storing parking timing statistics.
 * <p>
 * Represents the number of extended parking sessions and late returns for a
 * given day.
 * </p>
 */
public class ParkingTimingStats {
	public int extended; // Number of extended parking sessions
	public int late; // Number of late car returns

	/**
	 * Constructs a ParkingTimingStats object with the specified values.
	 *
	 * @param extended the number of extended parking sessions
	 * @param late     the number of late returns
	 */
	public ParkingTimingStats(int extended, int late) {
		this.extended = extended;
		this.late = late;
	}
}
