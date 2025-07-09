package common;

import java.io.Serializable;
import java.util.Map;

public class ParkingReportWrapper implements Serializable {
	public Map<Integer, ParkingTimingStats> reportData;
	public Map<String, Integer> lateUsers;

	public ParkingReportWrapper(Map<Integer, ParkingTimingStats> reportData, Map<String, Integer> lateUsers) {
		this.reportData = reportData;
		this.lateUsers = lateUsers;
	}

	public Map<Integer, ParkingTimingStats> getReportData() {
		return reportData;
	}

	public Map<String, Integer> getLateUsers() {
		return lateUsers;
	}
}
