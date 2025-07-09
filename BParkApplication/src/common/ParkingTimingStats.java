package common;

import java.io.Serializable;

public class ParkingTimingStats implements Serializable {
	public int extended;
	public int late;

	public ParkingTimingStats(int extended, int late) {
		this.extended = extended;
		this.late = late;
	}
}
