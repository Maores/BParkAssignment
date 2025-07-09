package server.common;

import java.io.Serializable;
import java.util.Map;

public class SubscriberReportWrapper implements Serializable {
	private Map<Integer, Integer> reportData;

	public SubscriberReportWrapper(Map<Integer, Integer> reportData) {
		this.reportData = reportData;
	}

	public Map<Integer, Integer> getReportData() {
		return reportData;
	}
}
