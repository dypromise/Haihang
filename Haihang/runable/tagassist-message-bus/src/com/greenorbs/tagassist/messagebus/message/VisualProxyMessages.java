package com.greenorbs.tagassist.messagebus.message;

public class VisualProxyMessages {
	
	public static class VTrackTunnelHeartbeat extends MessageBase {
		
		private String _trackTunnelId;
		
		private String _ownerPool;
		
		private float _distance;

		public String getTrackTunnelId() {
			return _trackTunnelId;
		}

		public void setTrackTunnelId(String trackTunnelId) {
			_trackTunnelId = trackTunnelId;
		}

		public String getOwnerPool() {
			return _ownerPool;
		}

		public void setOwnerPool(String ownerPool) {
			_ownerPool = ownerPool;
		}

		public float getDistance() {
			return _distance;
		}

		public void setDistance(float distance) {
			_distance = distance;
		}
		
	}
	
	public static class VBaggageTracked extends MessageBase {
		
		private String _epc;
		
		private String _flightId;
		
		private float _distance;

		public String getEPC() {
			return _epc;
		}

		public void setEPC(String epc) {
			_epc = epc;
		}

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}

		public float getDistance() {
			return _distance;
		}

		public void setDistance(float distance) {
			_distance = distance;
		}
		
	}
	
	public static class VBaggageRemoved extends MessageBase {
		
		private String _epc;
		
		private String _flightId;

		public String getEPC() {
			return _epc;
		}

		public void setEPC(String epc) {
			_epc = epc;
		}

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}
		
	}
	
	public static class VFlightUpdated extends MessageBase {
		
		private String _flightId;
		
		private int _sorted;
		
		private int _unsorted;
		
		private int _missing;
		
		private int _total;

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}

		public int getSorted() {
			return _sorted;
		}

		public void setSorted(int sorted) {
			_sorted = sorted;
		}

		public int getUnsorted() {
			return _unsorted;
		}

		public void setUnsorted(int unsorted) {
			_unsorted = unsorted;
		}

		public int getMissing() {
			return _missing;
		}

		public void setMissing(int missing) {
			_missing = missing;
		}

		public int getTotal() {
			return _total;
		}

		public void setTotal(int total) {
			_total = total;
		}
		
	}
	
	public static class VFlightRemoved extends MessageBase {
		
		private String _flightId;

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}
		
	}

	public static class VNotificationUpdated extends MessageBase {
		
		private String _uuid;
		
		private String _content;
		
		private Long _time;
		
		private Long _expire;
		
		public String getUUID() {
			return _uuid;
		}

		public void setUUID(String uuid) {
			_uuid = uuid;
		}

		public String getContent() {
			return _content;
		}

		public void setContent(String content) {
			_content = content;
		}

		public Long getTime() {
			return _time;
		}

		public void setTime(Long time) {
			_time = time;
		}

		public Long getExpire() {
			return _expire;
		}

		public void setExpire(Long expire) {
			_expire = expire;
		}
		
	}
	
	public static class VNotificationRemoved extends MessageBase {
		
		private String _uuid;

		public String getUUID() {
			return _uuid;
		}

		public void setUUID(String uuid) {
			_uuid = uuid;
		}
		
	}
	
	public static class VSyncRequest extends MessageBase {}
	
}
