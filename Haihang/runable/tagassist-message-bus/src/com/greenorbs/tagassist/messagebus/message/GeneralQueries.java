/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.messagebus.message;

public class GeneralQueries {

	/**
	 * 
	 * Function: Get the specified device's info from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseDeviceInfo
	 *
	 */
	public static class QueryDeviceInfo extends MessageBase {
		
		private String _targetUUID;
		
		public QueryDeviceInfo() {}
		
		public QueryDeviceInfo(String targetUUID) {
			_targetUUID = targetUUID;
		}

		public String getTargetUUID() {
			return _targetUUID;
		}

		public void setTargetUUID(String targetUUID) {
			_targetUUID = targetUUID;
		}

	}
	
	/**
	 * 
	 * Function: Get the list of devices from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseDeviceList
	 *
	 */
	public static class QueryDeviceList extends MessageBase {}
	
	/**
	 * 
	 * Function: Get the specified facility's info from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseFacilityInfo
	 *
	 */
	public static class QueryFacilityInfo extends MessageBase {
		
		private String _targetUUID;
		
		public QueryFacilityInfo() {}
		
		public QueryFacilityInfo(String targetUUID) {
			_targetUUID = targetUUID;
		}

		public String getTargetUUID() {
			return _targetUUID;
		}

		public void setTargetUUID(String targetUUID) {
			_targetUUID = targetUUID;
		}

	}
	
	/**
	 * 
	 * Function: Get the list of facilities from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseFacilityList
	 *
	 */
	public static class QueryFacilityList extends MessageBase {}
	
	/**
	 * 
	 * Function: Get the specified carriage's info from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseCarriageInfo
	 *
	 */
	public static class QueryCarriageInfo extends MessageBase {
		
		private String _carriageId;
		
		public QueryCarriageInfo() {}
		
		public QueryCarriageInfo(String carriageId) {
			_carriageId = carriageId;
		}

		public String getCarriageId() {
			return _carriageId;
		}

		public void setCarriageId(String carriageId) {
			_carriageId = carriageId;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the list of carriages from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseCarriageList
	 *
	 */
	public static class QueryCarriageList extends MessageBase {}
	
	/**
	 * 
	 * Function: Get the specified check-tunnel's info from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseCheckTunnelInfo
	 *
	 */
	public static class QueryCheckTunnelInfo extends MessageBase {
		
		private String _checkTunnelId;
		
		public QueryCheckTunnelInfo() {}
		
		public QueryCheckTunnelInfo(String checkTunnelId) {
			_checkTunnelId = checkTunnelId;
		}

		public String getCheckTunnelId() {
			return _checkTunnelId;
		}

		public void setCheckTunnelId(String checkTunnelId) {
			_checkTunnelId = checkTunnelId;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the list of check-tunnels within the specified pool (if specified) from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseCheckTunnelList
	 *
	 */
	public static class QueryCheckTunnelList extends MessageBase {
		
		private String _poolId;
		
		public QueryCheckTunnelList() {}
		
		public QueryCheckTunnelList(String poolId) {
			_poolId = poolId;
		}

		public String getPoolId() {
			return _poolId;
		}

		public void setPoolId(String poolId) {
			_poolId = poolId;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the specified track-tunnel's info from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseTrackTunnelInfo
	 *
	 */
	public static class QueryTrackTunnelInfo extends MessageBase {
		
		private String _trackTunnelId;

		public QueryTrackTunnelInfo() {}
		
		public QueryTrackTunnelInfo(String trackTunnelId) {
			_trackTunnelId = trackTunnelId;
		}
		
		public String getTrackTunnelId() {
			return _trackTunnelId;
		}

		public void setTrackTunnelId(String trackTunnelId) {
			_trackTunnelId = trackTunnelId;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the list of track-tunnels within the specified pool (if specified) from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseTrackTunnelList
	 *
	 */
	public static class QueryTrackTunnelList extends MessageBase {
		
		private String _poolId;
		
		public QueryTrackTunnelList() {}
		
		public QueryTrackTunnelList(String poolId) {
			_poolId = poolId;
		}

		public String getPoolId() {
			return _poolId;
		}

		public void setPoolId(String poolId) {
			_poolId = poolId;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the specified wristband's info from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseWristbandInfo
	 *
	 */
	public static class QueryWristbandInfo extends MessageBase {
		
		private String _wristbandId;
		
		public QueryWristbandInfo() {}
		
		public QueryWristbandInfo(String wristbandId) {
			_wristbandId = wristbandId;
		}

		public String getWristbandId() {
			return _wristbandId;
		}

		public void setWristbandId(String wristbandId) {
			_wristbandId = wristbandId;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the list of wristbands within the specified pool (if specified) from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseWristbandList
	 *
	 */
	public static class QueryWristbandList extends MessageBase {
		
		private String _poolId;
		
		public QueryWristbandList() {}
		
		public QueryWristbandList(String poolId) {
			_poolId = poolId;
		}

		public String getPoolId() {
			return _poolId;
		}

		public void setPoolId(String poolId) {
			_poolId = poolId;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the specified mobile reader's info from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseMobileReaderInfo
	 *
	 */
	public static class QueryMobileReaderInfo extends MessageBase {

		private String _mobileReaderId;

		public QueryMobileReaderInfo() {}
		
		public QueryMobileReaderInfo(String mobileReaderId) {
			_mobileReaderId = mobileReaderId;
		}
		
		public String getMobileReaderId() {
			return _mobileReaderId;
		}

		public void setMobileReaderId(String mobileReaderId) {
			_mobileReaderId = mobileReaderId;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the list of mobile readers from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseMobileReaderList
	 *
	 */
	public static class QueryMobileReaderList extends MessageBase {}
	
	/**
	 * 
	 * Function: Get the correct check-tunnel for a wrongly sorted baggage from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseBaggageCheckTunnel
	 *
	 */
	public static class QueryBaggageCheckTunnel extends MessageBase {
		
		private String _baggageNumber;
		
		public QueryBaggageCheckTunnel() {}
		
		public QueryBaggageCheckTunnel(String baggageNumber) {
			_baggageNumber = baggageNumber;
		}
		
		public String getBaggageNumber() {
			return _baggageNumber;
		}

		public void setBaggageNumber(String baggageNumber) {
			_baggageNumber = baggageNumber;
		}
		
	}

	/**
	 * 
	 * Function: Get the specified baggage's info from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseBaggageInfo
	 *
	 */
	public static class QueryBaggageInfo extends MessageBase {
		
		private String _baggageNumber;
		
		public QueryBaggageInfo() {}
		
		public QueryBaggageInfo(String baggageNumber) {
			_baggageNumber = baggageNumber;
		}
		
		public String getBaggageNumber() {
			return _baggageNumber;
		}

		public void setBaggageNumber(String baggageNumber) {
			_baggageNumber = baggageNumber;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the specified baggage's info from the data center by EPC
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseBaggageInfo
	 *
	 */
	public static class QueryBaggageInfoByEPC extends MessageBase {
		
		private String _epc;

		public QueryBaggageInfoByEPC() {}
		
		public QueryBaggageInfoByEPC(String epc) {
			_epc = epc;
		}
		
		public String getEPC() {
			return _epc;
		}

		public void setEPC(String epc) {
			_epc = epc;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the list of baggage for a specified flight from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseBaggageList
	 *
	 */
	public static class QueryBaggageList extends MessageBase {
		
		private String _flightId;
		
		public QueryBaggageList() {}
		
		public QueryBaggageList(String flightId) {
			_flightId = flightId;
		}

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the list of baggage numbers for a specified flight from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseBaggageNumberList
	 *
	 */
	public static class QueryBaggageNumberList extends MessageBase {
		
		private String _flightId;
		
		public QueryBaggageNumberList() {}
		
		public QueryBaggageNumberList(String flightId) {
			_flightId = flightId;
		}

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the specified baggage's trace from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseBaggageTrace
	 *
	 */
	public static class QueryBaggageTrace extends MessageBase {
		
		private String _baggageNumber;
		
		public QueryBaggageTrace() {}
		
		public QueryBaggageTrace(String baggageNumber) {
			_baggageNumber = baggageNumber;
		}

		public String getBaggageNumber() {
			return _baggageNumber;
		}

		public void setBaggageNumber(String baggageNumber) {
			_baggageNumber = baggageNumber;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the specified flight's info from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseFlightInfo
	 *
	 */
	public static class QueryFlightInfo extends MessageBase {
		
		private String _flightId;
		
		public QueryFlightInfo() {}
		
		public QueryFlightInfo(String flightId) {
			_flightId = flightId;
		}

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the list of flights from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseFlightList
	 *
	 */
	public static class QueryFlightList extends MessageBase {}
	
	/**
	 * 
	 * Function: Get the specified notification's info from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseNotificationInfo
	 *
	 */
	public static class QueryNotificationInfo extends MessageBase {
		
		private String _notificationUUID;
		
		public QueryNotificationInfo() {}
		
		public QueryNotificationInfo(String notificationUUID) {
			_notificationUUID = notificationUUID;
		}

		public String getNotificationUUID() {
			return _notificationUUID;
		}

		public void setNotificationUUID(String notificationUUID) {
			_notificationUUID = notificationUUID;
		}
		
	}
	
	/**
	 * 
	 * Function: Get the list of notifications from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseNotificationList
	 *
	 */
	public static class QueryNotificationList extends MessageBase {}
	
	/**
	 * 
	 * Function: Get the specified user's info from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: DataCenterMessages.ResponseUserInfo
	 *
	 */
	public static class QueryUserInfo extends MessageBase {
		
		private String _username;
		
		private String _password;
		
		public QueryUserInfo() {}

		public QueryUserInfo(String username, String password) {
			_username = username;
			_password = password;
		}
		
		public String getUsername() {
			return _username;
		}

		public void setUsername(String username) {
			_username = username;
		}

		public String getPassword() {
			return _password;
		}

		public void setPassword(String password) {
			_password = password;
		}
		
	}
	
}
