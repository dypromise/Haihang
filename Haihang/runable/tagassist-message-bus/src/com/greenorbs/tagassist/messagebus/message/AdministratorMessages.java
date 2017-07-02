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

import com.greenorbs.tagassist.CarriageInfo;
import com.greenorbs.tagassist.FacilityInfo;
import com.greenorbs.tagassist.NotificationInfo;

public class AdministratorMessages {
	
	public static class HardwareControl extends MessageBase {
		
		private String _targetUUID;
		
		private int _command;
		
		public HardwareControl() {
			_needConfirm = true;
		}

		public String getTargetUUID() {
			return _targetUUID;
		}

		public void setTargetUUID(String targetUUID) {
			_targetUUID = targetUUID;
		}

		public int getCommand() {
			return _command;
		}

		public void setCommand(int command) {
			_command = command;
		}
		
	}
	
	public static class HardwareInfoSet extends MessageBase {
		
		private String _targetUUID;
		
		private String _name;
		
		public HardwareInfoSet() {
			_needConfirm = true;
		}

		public String getTargetUUID() {
			return _targetUUID;
		}

		public void setTargetUUID(String targetUUID) {
			_targetUUID = targetUUID;
		}

		public String getName() {
			return _name;
		}

		public void setName(String name) {
			_name = name;
		}
		
	}
	
	public static class HardwareInfoGet extends MessageBase {
		
		private String _targetUUID;
		
		public HardwareInfoGet() {}
		
		public HardwareInfoGet(String targetUUID) {
			_targetUUID = targetUUID;
		}

		public String getTargetUUID() {
			return _targetUUID;
		}

		public void setTargetUUID(String targetUUID) {
			_targetUUID = targetUUID;
		}
		
	}
	
	public static class HardwareCollect extends MessageBase {
		
		private int _targetComponent; /* 0 for all */
		
		public HardwareCollect() {
			this(0);
		}
		
		public HardwareCollect(int targetComponent) {
			_targetComponent = targetComponent;
		}

		public int getTargetComponent() {
			return _targetComponent;
		}

		public void setTargetComponent(int targetComponent) {
			_targetComponent = targetComponent;
		}
		
	}
	
//	public static class SortationTerminate extends MessageBase {
//		
//		private String _flightId;
//		
//		public SortationTerminate() {}
//		
//		public SortationTerminate(String flightId) {
//			_flightId = flightId;
//		}
//	
//		public String getFlightId() {
//			return _flightId;
//		}
//	
//		public void setFlightId(String flightId) {
//			_flightId = flightId;
//		}
//		
//	}

	public static class FlightTerminate extends MessageBase {
		
		private String _flightId;
		
		public FlightTerminate() {}
		
		public FlightTerminate(String flightId) {
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
	 * Function: Register a device to the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: GeneralMessages.Confirmation
	 *
	 */
	public static class DeviceRegister extends MessageBase {
		
		private String _deviceUUID;
		
		public DeviceRegister() {
			_needConfirm = true;
		}
		
		public DeviceRegister(String deviceUUID) {
			_deviceUUID = deviceUUID;
		}

		public String getDeviceUUID() {
			return _deviceUUID;
		}

		public void setDeviceUUID(String deviceUUID) {
			_deviceUUID = deviceUUID;
		}
		
	}
	
	/**
	 * 
	 * Function: Unregister a device from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: GeneralMessages.Confirmation
	 *
	 */
	public static class DeviceUnregister extends MessageBase {
		
		private String _deviceUUID;
		
		public DeviceUnregister() {
			_needConfirm = true;
		}
		
		public DeviceUnregister(String deviceUUID) {
			this();
			_deviceUUID = deviceUUID;
		}

		public String getDeviceUUID() {
			return _deviceUUID;
		}

		public void setDeviceUUID(String deviceUUID) {
			_deviceUUID = deviceUUID;
		}
		
	}
	
	/**
	 * 
	 * Function: Register a facility to the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: GeneralMessages.Confirmation
	 *
	 */
	public static class FacilityRegister extends MessageBase {
		
		private FacilityInfo _facilityInfo;
		
		public FacilityRegister() {
			_needConfirm = true;
		}
		
		public FacilityRegister(FacilityInfo facilityInfo) {
			this();
			_facilityInfo = facilityInfo;
		}

		public FacilityInfo getFacilityInfo() {
			return _facilityInfo;
		}

		public void setFacilityInfo(FacilityInfo facilityInfo) {
			_facilityInfo = facilityInfo;
		}
		
	}
	
	/**
	 * 
	 * Function: Update a facility in the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: GeneralMessages.Confirmation
	 *
	 */
	public static class FacilityUpdate extends MessageBase {

		private FacilityInfo _facilityInfo;
		
		public FacilityUpdate() {
			_needConfirm = true;
		}
		
		public FacilityUpdate(FacilityInfo facilityInfo) {
			this();
			_facilityInfo = facilityInfo;
		}

		public FacilityInfo getFacilityInfo() {
			return _facilityInfo;
		}

		public void setFacilityInfo(FacilityInfo facilityInfo) {
			_facilityInfo = facilityInfo;
		}
		
	}
	
	/**
	 * 
	 * Function: Unregister a facility from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: GeneralMessages.Confirmation
	 *
	 */
	public static class FacilityUnregister extends MessageBase {
		
		private String _facilityUUID;
		
		public FacilityUnregister() {
			_needConfirm = true;
		}
		
		public FacilityUnregister(String facilityUUID) {
			this();
			_facilityUUID = facilityUUID;
		}

		public String getFacilityUUID() {
			return _facilityUUID;
		}

		public void setFacilityUUID(String facilityUUID) {
			_facilityUUID = facilityUUID;
		}
		
	}
	
	/**
	 * 
	 * Function: Register a carriage to the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: GeneralMessages.Confirmation
	 *
	 */
	public static class CarriageRegister extends MessageBase {
		
		private CarriageInfo _carriageInfo;
		
		public CarriageRegister() {
			_needConfirm = true;
		}
		
		public CarriageRegister(CarriageInfo carriageInfo) {
			this();
			_carriageInfo = carriageInfo;
		}

		public CarriageInfo getCarriageInfo() {
			return _carriageInfo;
		}

		public void setCarriageInfo(CarriageInfo carriageInfo) {
			_carriageInfo = carriageInfo;
		}

	}
	
	/**
	 * 
	 * Function: Update a carriage in the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: GeneralMessages.Confirmation
	 *
	 */
	public static class CarriageUpdate extends MessageBase {

		private CarriageInfo _carriageInfo;
		
		public CarriageUpdate() {
			_needConfirm = true;
		}
		
		public CarriageUpdate(CarriageInfo carriageInfo) {
			this();
			_carriageInfo = carriageInfo;
		}

		public CarriageInfo getCarriageInfo() {
			return _carriageInfo;
		}

		public void setCarriageInfo(CarriageInfo carriageInfo) {
			_carriageInfo = carriageInfo;
		}
		
	}
	
	/**
	 * 
	 * Function: Unregister a carriage from the data center
	 * 
	 * Receiver: Data center
	 * 
	 * Response: GeneralMessages.Confirmation
	 *
	 */
	public static class CarriageUnregister extends MessageBase {
		
		private String _carriageId;
		
		public CarriageUnregister() {
			_needConfirm = true;
		}
		
		public CarriageUnregister(String carriageId) {
			this();
			_carriageId = carriageId;
		}

		public String getCarriageId() {
			return _carriageId;
		}

		public void setCarriageId(String carriageId) {
			_carriageId = carriageId;
		}

	}
	
	public static class BaggageStatusSet extends MessageBase {
		
		private String _baggageNumber;
		
		private int _status;
		
		private String _operator;
		
		public BaggageStatusSet() {
			_needConfirm = true;
		}
		
		public BaggageStatusSet(String baggageNumber, int status) {
			this();
			_baggageNumber = baggageNumber;
			_status = status;
		}

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

		public String getOperator() {
			return _operator;
		}

		public void setOperator(String operator) {
			_operator = operator;
		}
		
	}
	
	public static class NotificationCreate extends MessageBase {
		
		private NotificationInfo _notificationInfo;
		
		public NotificationCreate() {
			_needConfirm = true;
		}
		
		public NotificationCreate(NotificationInfo notificationInfo) {
			this();
			_notificationInfo = notificationInfo;
		}

		public NotificationInfo getNotificationInfo() {
			return _notificationInfo;
		}

		public void setNotificationInfo(NotificationInfo notificationInfo) {
			_notificationInfo = notificationInfo;
		}
		
	}
	
	public static class NotificationModify extends MessageBase {
		
		private NotificationInfo _notificationInfo;
		
		public NotificationModify() {
			_needConfirm = true;
		}
		
		public NotificationModify(NotificationInfo notificationInfo) {
			this();
			_notificationInfo = notificationInfo;
		}

		public NotificationInfo getNotificationInfo() {
			return _notificationInfo;
		}

		public void setNotificationInfo(NotificationInfo notificationInfo) {
			_notificationInfo = notificationInfo;
		}
		
	}
	
	public static class NotificationDelete extends MessageBase {
		
		private String _notificationUUID;
		
		public NotificationDelete() {
			_needConfirm = true;
		}
		
		public NotificationDelete(String notificationUUID) {
			this();
			_notificationUUID = notificationUUID;
		}
		
		public String getNotificationUUID() {
			return _notificationUUID;
		}

		public void setNotificationUUID(String notificationUUID) {
			_notificationUUID = notificationUUID;
		}

	}
	
	public static class CheckTunnelConfig extends MessageBase {
		
		private String _checkTunnelId;
		
		private String _flightId;
		
		private String _carriageId;
		
		public CheckTunnelConfig() {
			_needConfirm = true;
		}
		
		public String getCheckTunnelId() {
			return _checkTunnelId;
		}

		public void setCheckTunnelId(String checkTunnelId) {
			_checkTunnelId = checkTunnelId;
		}	

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}

		public String getCarriageId() {
			return _carriageId;
		}

		public void setCarriageId(String carriageId) {
			_carriageId = carriageId;
		}
		
	}

	public static class CheckTunnelRelease extends MessageBase {
		
		private String _checkTunnelId;
		
		public CheckTunnelRelease() {
			_needConfirm = true;
		}

		public String getCheckTunnelId() {
			return _checkTunnelId;
		}

		public void setCheckTunnelId(String checkTunnelId) {
			_checkTunnelId = checkTunnelId;
		}
		
	}
	
	public static class TrackTunnelConfig extends MessageBase {
		
		private String _trackTunnelId;
		
		private String _name;
		
		private String _poolId;
		
		private float _distance;
		
		public TrackTunnelConfig() {
			_needConfirm = true;
		}

		public String getTrackTunnelId() {
			return _trackTunnelId;
		}

		public void setTrackTunnelId(String trackTunnelId) {
			_trackTunnelId = trackTunnelId;
		}

		public String getName() {
			return _name;
		}
	
		public void setName(String name) {
			_name = name;
		}
	
		public String getPoolId() {
			return _poolId;
		}
	
		public void setPoolId(String poolId) {
			_poolId = poolId;
		}
	
		public float getDistance() {
			return _distance;
		}
	
		public void setDistance(float distance) {
			_distance = distance;
		}
	
	}
	
	/**
	 * 
	 * Function: Register a wristband to the sink
	 * 
	 * Receiver: Sink
	 * 
	 * Response: GeneralMessages.Confirmation
	 *
	 */
	public static class SinkRegisterWristband extends MessageBase {
		
		private String _sinkId;
		
		private String _wristbandId;
		
		private String _name;
		
		public SinkRegisterWristband() {
			_needConfirm = true;
		}
		
		public String getSinkId() {
			return _sinkId;
		}

		public void setSinkId(String sinkId) {
			_sinkId = sinkId;
		}

		public String getWristbandId() {
			return _wristbandId;
		}

		public void setWristbandId(String wristbandId) {
			_wristbandId = wristbandId;
		}

		public String getName() {
			return _name;
		}

		public void setName(String name) {
			_name = name;
		}
		
	}
	
	/**
	 * 
	 * Function: Unregister a wristband from the sink
	 * 
	 * Receiver: Sink
	 * 
	 * Response: GeneralMessages.Confirmation
	 *
	 */
	public static class SinkUnregisterWristband extends MessageBase {
		
		private String _sinkId;
		
		private String _wristbandId;
		
		public SinkUnregisterWristband() {
			_needConfirm = true;
		}
		
		public String getSinkId() {
			return _sinkId;
		}

		public void setSinkId(String sinkId) {
			_sinkId = sinkId;
		}

		public String getWristbandId() {
			return _wristbandId;
		}

		public void setWristbandId(String wristbandId) {
			_wristbandId = wristbandId;
		}
		
	}
	
	public static class WristbandBind extends MessageBase {
		
		private String _wristbandId;
		
		private String _flightId;
		
		public WristbandBind() {
			_needConfirm = true;
		}

		public String getWristbandId() {
			return _wristbandId;
		}

		public void setWristbandId(String wristbandId) {
			_wristbandId = wristbandId;
		}

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}
		
	}
	
	public static class WristbandUnbind extends MessageBase {
		
		private String _wristbandId;

		private String _flightId;
		
		public WristbandUnbind() {
			_needConfirm = true;
		}

		public String getWristbandId() {
			return _wristbandId;
		}

		public void setWristbandId(String wristbandId) {
			_wristbandId = wristbandId;
		}
		
		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}
		
	}
	
	public static class WristbandSearch extends MessageBase {
		
		private String _wristbandId;
		
		public WristbandSearch() {
			_needConfirm = true;
		}

		public String getWristbandId() {
			return _wristbandId;
		}

		public void setWristbandId(String wristbandId) {
			_wristbandId = wristbandId;
		}
		
	}
	
	public static class WristbandLightOn extends MessageBase {
		
		private String _wristbandId;
		
		private int _light;
		
		public WristbandLightOn() {
			_needConfirm = true;
		}

		public int getLight() {
			return _light;
		}

		public void setLight(int _light) {
			this._light = _light;
		}

		public String getWristbandId() {
			return _wristbandId;
		}

		public void setWristbandId(String _wristbandId) {
			this._wristbandId = _wristbandId;
		}
		
	}
	
	public static class WristbandLightOff extends MessageBase {
		
		private String _wristbandId;
		
		private int _light;
		
		public WristbandLightOff() {
			_needConfirm = true;
		}

		public int getLight() {
			return _light;
		}

		public void setLight(int _light) {
			this._light = _light;
		}

		public String getWristbandId() {
			return _wristbandId;
		}

		public void setWristbandId(String _wristbandId) {
			this._wristbandId = _wristbandId;
		}
		
	}
	
	public static class WristbandCollect extends MessageBase {}
	
	public static class WristbandRFIDSwitch extends MessageBase {
		
		private String _wristbandId;
		
		private boolean _turnedOn;
		
		public WristbandRFIDSwitch() {
			_needConfirm = true;
		}
		
		public String getWristbandId() {
			return _wristbandId;
		}

		public void setWristbandId(String wristbandId) {
			_wristbandId = wristbandId;
		}

		public boolean isTurnedOn() {
			return _turnedOn;
		}

		public void setTurnedOn(boolean turnedOn) {
			_turnedOn = turnedOn;
		}
		
	}
	
	public static class MobileReaderConfig extends MessageBase {
		
		private String _mobileReaderId;
		
		private String _flightId;
		
		public MobileReaderConfig() {
			_needConfirm = true;
		}
		
		public String getMobileReaderId() {
			return _mobileReaderId;
		}

		public void setMobileReaderId(String mobileReaderId) {
			_mobileReaderId = mobileReaderId;
		}

		public String getFlightId() {
			return _flightId;
		}

		public void setFlightId(String flightId) {
			_flightId = flightId;
		}
		
	}

}
