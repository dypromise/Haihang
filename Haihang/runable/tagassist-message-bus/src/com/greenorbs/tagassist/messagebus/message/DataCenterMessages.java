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

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.CarriageInfo;
import com.greenorbs.tagassist.CheckTunnelInfo;
import com.greenorbs.tagassist.DeviceInfo;
import com.greenorbs.tagassist.FacilityInfo;
import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.MobileReaderInfo;
import com.greenorbs.tagassist.NotificationInfo;
import com.greenorbs.tagassist.TracingInfo;
import com.greenorbs.tagassist.TrackTunnelInfo;
import com.greenorbs.tagassist.UserInfo;
import com.greenorbs.tagassist.WristbandInfo;

public class DataCenterMessages {
	
	public static class DeviceRegistered extends MessageBase {
		
		private DeviceInfo _deviceInfo;
		
		public DeviceRegistered() {}
		
		public DeviceRegistered(DeviceInfo deviceInfo) {
			_deviceInfo = deviceInfo;
		}

		public DeviceInfo getDeviceInfo() {
			return _deviceInfo;
		}

		public void setDeviceInfo(DeviceInfo deviceInfo) {
			_deviceInfo = deviceInfo;
		}
		
	}
	
	public static class DeviceUnregistered extends MessageBase {
		
		private String _deviceUUID;
		
		public DeviceUnregistered() {}
		
		public DeviceUnregistered(String deviceUUID) {
			_deviceUUID = deviceUUID;
		}

		public String getDeviceUUID() {
			return _deviceUUID;
		}

		public void setDeviceUUID(String deviceUUID) {
			_deviceUUID = deviceUUID;
		}
		
	}
	
	public static class FacilityRegistered extends MessageBase {
		
		private FacilityInfo _facilityInfo;
		
		public FacilityRegistered() {}
		
		public FacilityRegistered(FacilityInfo facilityInfo) {
			_facilityInfo = facilityInfo;
		}

		public FacilityInfo getFacilityInfo() {
			return _facilityInfo;
		}

		public void setFacilityInfo(FacilityInfo facilityInfo) {
			_facilityInfo = facilityInfo;
		}
		
	}
	
	public static class FacilityUpdated extends MessageBase {
		
		private FacilityInfo _facilityInfo;
		
		public FacilityUpdated() {}
		
		public FacilityUpdated(FacilityInfo facilityInfo) {
			_facilityInfo = facilityInfo;
		}

		public FacilityInfo getFacilityInfo() {
			return _facilityInfo;
		}

		public void setFacilityInfo(FacilityInfo facilityInfo) {
			_facilityInfo = facilityInfo;
		}
		
	}
	
	public static class FacilityUnregistered extends MessageBase {
		
		private String _facilityUUID;
		
		public FacilityUnregistered() {}
		
		public FacilityUnregistered(String facilityUUID) {
			_facilityUUID = facilityUUID;
		}

		public String getFacilityUUID() {
			return _facilityUUID;
		}

		public void setFacilityUUID(String facilityUUID) {
			_facilityUUID = facilityUUID;
		}
		
	}
	
	public static class CarriageRegistered extends MessageBase {
		
		private CarriageInfo _carriageInfo;
		
		public CarriageRegistered() {}
		
		public CarriageRegistered(CarriageInfo carriageInfo) {
			_carriageInfo = carriageInfo;
		}

		public CarriageInfo getCarriageInfo() {
			return _carriageInfo;
		}

		public void setCarriageInfo(CarriageInfo carriageInfo) {
			_carriageInfo = carriageInfo;
		}

	}
	
	public static class CarriageUpdated extends MessageBase {
		
		private CarriageInfo _carriageInfo;
		
		public CarriageUpdated() {}
		
		public CarriageUpdated(CarriageInfo carriageInfo) {
			_carriageInfo = carriageInfo;
		}

		public CarriageInfo getCarriageInfo() {
			return _carriageInfo;
		}

		public void setCarriageInfo(CarriageInfo carriageInfo) {
			_carriageInfo = carriageInfo;
		}

	}
	
	public static class CarriageUnregistered extends MessageBase {
		
		private String _carriageId;
		
		public CarriageUnregistered() {}
		
		public CarriageUnregistered(String carriageId) {
			_carriageId = carriageId;
		}

		public String getCarriageId() {
			return _carriageId;
		}

		public void setCarriageId(String carriageId) {
			_carriageId = carriageId;
		}

	}
	
	public static class NotificationCreated extends MessageBase {
		
		private NotificationInfo _notificationInfo;
		
		public NotificationCreated() {}
		
		public NotificationCreated(NotificationInfo notificationInfo) {
			_notificationInfo = notificationInfo;
		}

		public NotificationInfo getNotificationInfo() {
			return _notificationInfo;
		}

		public void setNotificationInfo(NotificationInfo notificationInfo) {
			_notificationInfo = notificationInfo;
		}
		
	}
	
	public static class NotificationModified extends MessageBase {
		
		private NotificationInfo _notificationInfo;
		
		public NotificationModified() {}
		
		public NotificationModified(NotificationInfo notificationInfo) {
			_notificationInfo = notificationInfo;
		}

		public NotificationInfo getNotificationInfo() {
			return _notificationInfo;
		}

		public void setNotificationInfo(NotificationInfo notificationInfo) {
			_notificationInfo = notificationInfo;
		}
		
	}
	
	public static class NotificationDeleted extends MessageBase {
		
		private String _notificationUUID;
		
		public NotificationDeleted() {}
		
		public NotificationDeleted(String notificationUUID) {
			_notificationUUID = notificationUUID;
		}
		
		public String getNotificationUUID() {
			return _notificationUUID;
		}

		public void setNotificationUUID(String notificationUUID) {
			_notificationUUID = notificationUUID;
		}

	}

	public static class BaggageStatusChanged extends MessageBase {
		
		private String _baggageNumber;
		
		// Changed to
		private int _status;
		
		// Changed by
		private String _device;
		
		// Changed by
		private String _operator;
		
		// Changed at
		private long _time;
		
		public String getBaggageNumber() {
			return _baggageNumber;
		}

		public void setBaggageNumber(String baggageNumber) {
			_baggageNumber = baggageNumber;
		}

		public int getStatus() {
			return _status;
		}

		public void setStatus(int status) {
			_status = status;
		}

		public String getDevice() {
			return _device;
		}

		public void setDevice(String device) {
			_device = device;
		}

		public String getOperator() {
			return _operator;
		}

		public void setOperator(String operator) {
			_operator = operator;
		}

		public long getTime() {
			return _time;
		}

		public void setTime(long time) {
			_time = time;
		}
		
	}
	
	public static class ResponseDeviceInfo extends MessageBase {
		
		private DeviceInfo _deviceInfo;
		
		public ResponseDeviceInfo() {}
		
		public ResponseDeviceInfo(DeviceInfo deviceInfo) {
			_deviceInfo = deviceInfo;
		}

		public DeviceInfo getDeviceInfo() {
			return _deviceInfo;
		}

		public void setDeviceInfo(DeviceInfo deviceInfo) {
			_deviceInfo = deviceInfo;
		}
		
	}
	
	public static class ResponseDeviceList extends MessageBase {
		
		private DeviceInfo[] _deviceInfoList;
		
		public ResponseDeviceList() {}
		
		public ResponseDeviceList(DeviceInfo[] deviceInfoList) {
			_deviceInfoList = deviceInfoList;
		}

		public DeviceInfo[] getDeviceInfoList() {
			return _deviceInfoList;
		}

		public void setDeviceInfoList(DeviceInfo[] deviceInfoList) {
			_deviceInfoList = deviceInfoList;
		}
		
	}
	
	public static class ResponseFacilityInfo extends MessageBase {
		
		private FacilityInfo _facilityInfo;

		public ResponseFacilityInfo() {}
		
		public ResponseFacilityInfo(FacilityInfo facilityInfo) {
			_facilityInfo = facilityInfo;
		}

		public FacilityInfo getFacilityInfo() {
			return _facilityInfo;
		}

		public void setFacilityInfo(FacilityInfo facilityInfo) {
			_facilityInfo = facilityInfo;
		}
		
	}
	
	public static class ResponseFacilityList extends MessageBase {
		
		private FacilityInfo[] _facilityInfoList;
		
		public ResponseFacilityList() {}
		
		public ResponseFacilityList(FacilityInfo[] facilityInfoList) {
			_facilityInfoList = facilityInfoList;
		}

		public FacilityInfo[] getFacilityInfoList() {
			return _facilityInfoList;
		}

		public void setFacilityInfoList(FacilityInfo[] facilityInfoList) {
			_facilityInfoList = facilityInfoList;
		}
		
	}
	
	public static class ResponseCarriageInfo extends MessageBase {
		
		private CarriageInfo _carriageInfo;

		public ResponseCarriageInfo() {}
		
		public ResponseCarriageInfo(CarriageInfo carriageInfo) {
			_carriageInfo = carriageInfo;
		}

		public CarriageInfo getCarriageInfo() {
			return _carriageInfo;
		}

		public void setCarriageInfo(CarriageInfo carriageInfo) {
			_carriageInfo = carriageInfo;
		}

	}
	
	public static class ResponseCarriageList extends MessageBase {
		
		private CarriageInfo[] _carriageInfoList;
		
		public ResponseCarriageList() {}
		
		public ResponseCarriageList(CarriageInfo[] carriageInfoList) {
			_carriageInfoList = carriageInfoList;
		}

		public CarriageInfo[] getCarriageInfoList() {
			return _carriageInfoList;
		}

		public void setCarriageInfoList(CarriageInfo[] carriageInfoList) {
			_carriageInfoList = carriageInfoList;
		}

	}
	
	public static class ResponseCheckTunnelInfo extends MessageBase {
		
		private CheckTunnelInfo _checkTunnelInfo;

		public ResponseCheckTunnelInfo() {}
		
		public ResponseCheckTunnelInfo(CheckTunnelInfo checkTunnelInfo) {
			_checkTunnelInfo = checkTunnelInfo;
		}
		
		public CheckTunnelInfo getCheckTunnelInfo() {
			return _checkTunnelInfo;
		}

		public void setCheckTunnelInfo(CheckTunnelInfo checkTunnelInfo) {
			_checkTunnelInfo = checkTunnelInfo;
		}
		
	}
	
	public static class ResponseCheckTunnelList extends MessageBase {
		
		private CheckTunnelInfo[] _checkTunnelInfoList;
		
		public ResponseCheckTunnelList() {}
		
		public ResponseCheckTunnelList(CheckTunnelInfo[] checkTunnelInfoList) {
			_checkTunnelInfoList = checkTunnelInfoList;
		}

		public CheckTunnelInfo[] getCheckTunnelInfoList() {
			return _checkTunnelInfoList;
		}

		public void setCheckTunnelInfoList(CheckTunnelInfo[] checkTunnelInfoList) {
			_checkTunnelInfoList = checkTunnelInfoList;
		}
		
	}
	
	public static class ResponseTrackTunnelInfo extends MessageBase {
		
		private TrackTunnelInfo _trackTunnelInfo;

		public ResponseTrackTunnelInfo() {}
		
		public ResponseTrackTunnelInfo(TrackTunnelInfo trackTunnelInfo) {
			_trackTunnelInfo = trackTunnelInfo;
		}
		
		public TrackTunnelInfo getTrackTunnelInfo() {
			return _trackTunnelInfo;
		}

		public void setTrackTunnelInfo(TrackTunnelInfo trackTunnelInfo) {
			_trackTunnelInfo = trackTunnelInfo;
		}
		
	}
	
	public static class ResponseTrackTunnelList extends MessageBase {
		
		private TrackTunnelInfo[] _trackTunnelInfoList;
		
		public ResponseTrackTunnelList() {}
		
		public ResponseTrackTunnelList(TrackTunnelInfo[] trackTunnelInfoList) {
			_trackTunnelInfoList = trackTunnelInfoList;
		}

		public TrackTunnelInfo[] getTrackTunnelInfoList() {
			return _trackTunnelInfoList;
		}

		public void setTrackTunnelInfoList(TrackTunnelInfo[] trackTunnelInfoList) {
			_trackTunnelInfoList = trackTunnelInfoList;
		}
		
	}
	
	public static class ResponseWristbandInfo extends MessageBase {
		
		private WristbandInfo _wristbandInfo;
		
		public ResponseWristbandInfo() {}
		
		public ResponseWristbandInfo(WristbandInfo wristbandInfo) {
			_wristbandInfo = wristbandInfo;
		}

		public WristbandInfo getWristbandInfo() {
			return _wristbandInfo;
		}

		public void setWristbandInfo(WristbandInfo wristbandInfo) {
			_wristbandInfo = wristbandInfo;
		}
		
	}
	
	public static class ResponseWristbandList extends MessageBase {
		
		private WristbandInfo[] _wristbandInfoList;
		
		public ResponseWristbandList() {}
		
		public ResponseWristbandList(WristbandInfo[] wristbandInfoList) {
			_wristbandInfoList = wristbandInfoList;
		}

		public WristbandInfo[] getWristbandInfoList() {
			return _wristbandInfoList;
		}

		public void setWristbandInfoList(WristbandInfo[] wristbandInfoList) {
			_wristbandInfoList = wristbandInfoList;
		}
		
	}
	
	public static class ResponseMobileReaderInfo extends MessageBase {
		
		private MobileReaderInfo _mobileReaderInfo;

		public ResponseMobileReaderInfo() {}
		
		public ResponseMobileReaderInfo(MobileReaderInfo mobileReaderInfo) {
			_mobileReaderInfo = mobileReaderInfo;
		}
		
		public MobileReaderInfo getMobileReaderInfo() {
			return _mobileReaderInfo;
		}

		public void setMobileReaderInfo(MobileReaderInfo mobileReaderInfo) {
			_mobileReaderInfo = mobileReaderInfo;
		}
		
	}
	
	public static class ResponseMobileReaderList extends MessageBase {
		
		private MobileReaderInfo[] _mobileReaderInfoList;

		public ResponseMobileReaderList() {}
		
		public ResponseMobileReaderList(MobileReaderInfo[] mobileReaderInfoList) {
			_mobileReaderInfoList = mobileReaderInfoList;
		}
		
		public MobileReaderInfo[] getMobileReaderInfoList() {
			return _mobileReaderInfoList;
		}

		public void setMobileReaderInfoList(MobileReaderInfo[] mobileReaderInfoList) {
			_mobileReaderInfoList = mobileReaderInfoList;
		}
		
	}
	
	public static class ResponseBaggageCheckTunnel extends MessageBase {
		
		private String _checkTunnelId;
		
		private String _checkTunnelName;
		
		public ResponseBaggageCheckTunnel() {}
		
		public ResponseBaggageCheckTunnel(String checkTunnelId, String checkTunnelName) {
			_checkTunnelId = checkTunnelId;
			_checkTunnelName = checkTunnelName;
		}
		
		public String getCheckTunnelId() {
			return _checkTunnelId;
		}

		public void setCheckTunnelId(String checkTunnelId) {
			_checkTunnelId = checkTunnelId;
		}

		public String getCheckTunnelName() {
			return _checkTunnelName;
		}

		public void setCheckTunnelName(String checkTunnelName) {
			_checkTunnelName = checkTunnelName;
		}
		
	}
	
	public static class ResponseBaggageInfo extends MessageBase {
	
		private BaggageInfo _baggageInfo;
		
		public ResponseBaggageInfo() {}
		
		public ResponseBaggageInfo(BaggageInfo baggageInfo) {
			_baggageInfo = baggageInfo;
		}
		
		public BaggageInfo getBaggageInfo() {
			return _baggageInfo;
		}

		public void setBaggageInfo(BaggageInfo baggageInfo) {
			_baggageInfo = baggageInfo;
		}

	}
	
	public static class ResponseBaggageList extends MessageBase {
		
		private BaggageInfo[] _baggageInfoList;
		
		public ResponseBaggageList() {}
		
		public ResponseBaggageList(BaggageInfo[] baggageInfoList) {
			_baggageInfoList = baggageInfoList;
		}
		
		public BaggageInfo[] getBaggageInfoList() {
			return _baggageInfoList;
		}

		public void setBaggageInfoList(BaggageInfo[] baggageInfoList) {
			_baggageInfoList = baggageInfoList;
		}
		
	}
	
	public static class ResponseBaggageNumberList extends MessageBase {
		
		private String _flightId;
		
		private String[] _baggageNumberList;
		
		public ResponseBaggageNumberList() {}
		
		public ResponseBaggageNumberList(String flightId, String[] baggageNumberList) {
			_flightId = flightId;
			_baggageNumberList = baggageNumberList;
		}

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}

		public String[] getBaggageNumberList() {
			return _baggageNumberList;
		}

		public void setBaggageNumberList(String[] baggageNumberList) {
			_baggageNumberList = baggageNumberList;
		}

	}
	
	public static class ResponseBaggageTrace extends MessageBase {
		
		private TracingInfo[] _tracingInfoList;
		
		public ResponseBaggageTrace() {}
		
		public ResponseBaggageTrace(TracingInfo[] tracingInfoList) {
			_tracingInfoList = tracingInfoList;
		}

		public TracingInfo[] getTracingInfoList() {
			return _tracingInfoList;
		}

		public void setTracingInfoList(TracingInfo[] tracingInfoList) {
			_tracingInfoList = tracingInfoList;
		}
		
	}
	
	public static class ResponseFlightInfo extends MessageBase {
		
		private FlightInfo _flightInfo;
		
		public ResponseFlightInfo() {}
		
		public ResponseFlightInfo(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}

		public FlightInfo getFlightInfo() {
			return _flightInfo;
		}

		public void setFlightInfo(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}
		
	}
	
	public static class ResponseFlightList extends MessageBase {
		
		private FlightInfo[] _flightInfoList;
		
		public ResponseFlightList() {}
		
		public ResponseFlightList(FlightInfo[] flightInfoList) {
			_flightInfoList = flightInfoList;
		}

		public FlightInfo[] getFlightInfoList() {
			return _flightInfoList;
		}

		public void setFlightInfoList(FlightInfo[] flightInfoList) {
			_flightInfoList = flightInfoList;
		}
		
	}
	
	public static class ResponseNotificationInfo extends MessageBase {
		
		private NotificationInfo _notificationInfo;
		
		public ResponseNotificationInfo() {}
		
		public ResponseNotificationInfo(NotificationInfo notificationInfo) {
			_notificationInfo = notificationInfo;
		}

		public NotificationInfo getNotificationInfo() {
			return _notificationInfo;
		}

		public void setNotificationInfo(NotificationInfo notificationInfo) {
			_notificationInfo = notificationInfo;
		}
		
	}
	
	public static class ResponseNotificationList extends MessageBase {
		
		private NotificationInfo[] _notificationInfoList;
		
		public ResponseNotificationList() {}
		
		public ResponseNotificationList(NotificationInfo[] notificationInfoList) {
			_notificationInfoList = notificationInfoList;
		}

		public NotificationInfo[] getNotificationInfoList() {
			return _notificationInfoList;
		}

		public void setNotificationInfoList(NotificationInfo[] notificationInfoList) {
			_notificationInfoList = notificationInfoList;
		}
		
	}
	
	public static class ResponseUserInfo extends MessageBase {
		
		private UserInfo _userInfo;
		
		public ResponseUserInfo() {}
		
		public ResponseUserInfo(UserInfo userInfo) {
			_userInfo = userInfo;
		}

		public UserInfo getUserInfo() {
			return _userInfo;
		}

		public void setUserInfo(UserInfo userInfo) {
			_userInfo = userInfo;
		}
		
	}
	
}
