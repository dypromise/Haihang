/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 7, 2012
 */

package com.greenorbs.tagassist.datacenter.manager;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.greenorbs.tagassist.CheckTunnelInfo;
import com.greenorbs.tagassist.DeviceInfo;
import com.greenorbs.tagassist.MobileReaderInfo;
import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.TrackTunnelInfo;
import com.greenorbs.tagassist.WristbandInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.DeviceRegister;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.DeviceUnregister;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.DeviceRegistered;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.DeviceUnregistered;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Confirmation;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.HardwareInfoReport;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Heartbeat;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.TrackTunnelInfoReport;
import com.greenorbs.tagassist.messagebus.message.WristbandProxyMessages.WristbandInfoReport;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class DeviceManager extends BusMessageHandler {
	
	@Override
	public void initSubscriber() {
		/* Hardware heartbeat */
		this.subscribe(Heartbeat.class);
		
		/* Hardware info reports */
		this.subscribe(HardwareInfoReport.class);
		this.subscribe(TrackTunnelInfoReport.class);
		this.subscribe(WristbandInfoReport.class);
		
		/* Administrator commands */
		this.subscribe(DeviceRegister.class);
		this.subscribe(DeviceUnregister.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof Heartbeat) {
			
			this.handleHeartbeat((Heartbeat) message);
			
		} else if (message instanceof HardwareInfoReport) {
			
			// NOTE TrackTunnelInfoReport and WristbandInfoReport are also handled here
			
			this.handleHardwareInfoReport((HardwareInfoReport) message);
			
		} else if (message instanceof DeviceRegister) {
			
			DeviceRegister m = (DeviceRegister) message;
			String deviceUUID = m.getDeviceUUID();
			Result result = this.registerDevice(deviceUUID);
			
			reply = new Confirmation(m.getMessageId(), result.getCode(), result.getDescription());
			
		} else if (message instanceof DeviceUnregister) {
			
			DeviceUnregister m = (DeviceUnregister) message;
			String deviceUUID = m.getDeviceUUID();
			Result result = this.unregisterDevice(deviceUUID);
			
			reply = new Confirmation(m.getMessageId(), result.getCode(), result.getDescription());
			
		}
			
		return reply;
	}

	private void handleHeartbeat(Heartbeat heartbeat) {
		if (null == heartbeat || StringUtils.isEmpty(heartbeat.getSource())) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		
		try {
			transaction = session.beginTransaction();
			
			DeviceInfo deviceInfo = (DeviceInfo) session.createQuery(
					"from DeviceInfo where uuid='" + heartbeat.getSource() + "'").uniqueResult();
			if (null == deviceInfo) {
				deviceInfo = new DeviceInfo();
				deviceInfo.setUUID(heartbeat.getSource());
				deviceInfo.setComponent(heartbeat.getComponent());
				deviceInfo.setName(heartbeat.getName());
				deviceInfo.setStatus(IHardware.STATUS_UNKNOWN);
				deviceInfo.setRegistered(false);
				deviceInfo.setRegistTime(null);
			}
			deviceInfo.setLastActiveTime((new Date()).getTime());
			session.saveOrUpdate(deviceInfo);
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			_logger.error(e);
		} finally {
			session.close();
		}
	}

	private void handleHardwareInfoReport(HardwareInfoReport report) {
		if (null == report || StringUtils.isEmpty(report.getSource())) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		
		try {
			transaction = session.beginTransaction();
			
			DeviceInfo deviceInfo = (DeviceInfo) session.createQuery(
					"from DeviceInfo where uuid='" + report.getSource() + "'").uniqueResult();
			if (null == deviceInfo) {
				deviceInfo = new DeviceInfo();
				deviceInfo.setUUID(report.getSource());
				deviceInfo.setComponent(report.getComponent());
				deviceInfo.setRegistered(false);
				deviceInfo.setRegistTime(null);
			}
			deviceInfo.setName(report.getName());
			deviceInfo.setStatus(report.getStatus());
			deviceInfo.setSoftwareVersion(report.getSoftwareVersion());
			deviceInfo.setHostname(report.getHostname());
			deviceInfo.setMacAddress(report.getMacAddress());
			deviceInfo.setLastActiveIp(report.getIpAddress());
			session.saveOrUpdate(deviceInfo);
			
			if (deviceInfo.getRegistered()) {
				if (report instanceof TrackTunnelInfoReport) {
					String locationParam1 = ((TrackTunnelInfoReport) report).getLocationParam1();
					Float locationParam2 = ((TrackTunnelInfoReport) report).getLocationParam2();
					TrackTunnelInfo storedTrackTunnel = (TrackTunnelInfo) session.createQuery(
							"from TrackTunnelInfo where uuid='" + report.getSource() + "'").uniqueResult();
					if (storedTrackTunnel != null) {
						storedTrackTunnel.setLocationParam1(locationParam1);
						storedTrackTunnel.setLocationParam2(locationParam2);
						session.update(storedTrackTunnel);
					}
				} else if (report instanceof WristbandInfoReport) {
					WristbandInfo wristbandInfo = ((WristbandInfoReport) report).getWristbandInfo();
					if (wristbandInfo != null && StringUtils.isNotEmpty(wristbandInfo.getUUID())) {
						WristbandInfo storedWristband = (WristbandInfo) session.createQuery(
								"from WristbandInfo where uuid='" + wristbandInfo.getUUID() + "'").uniqueResult();
						if (storedWristband != null) {
							storedWristband.setSubStatus(wristbandInfo.getSubStatus());
							storedWristband.setBattery(wristbandInfo.getBattery());
							storedWristband.setRFIDStatus(wristbandInfo.getRFIDStatus());
							session.update(storedWristband);
						}
					}
				}
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			_logger.error(e);
		} finally {
			session.close();
		}
	}

	private Result registerDevice(String deviceUUID) {
		if (StringUtils.isEmpty(deviceUUID)) {
			return new Result(Result.CODE_ERROR, "_uuid is empty");
		}
		
		Result result = new Result(Result.CODE_FAILURE);
		DeviceInfo registeredDeviceInfo = null;
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			DeviceInfo deviceInfo = (DeviceInfo) session.createQuery(
					"from DeviceInfo where uuid='" + deviceUUID + "'").uniqueResult();
			
			if (null == deviceInfo) {
				registeredDeviceInfo = null;
				result.setCode(Result.CODE_ERROR);
				result.setDescription("device does not exist");
				
			} else if (deviceInfo.getRegistered()) {
				registeredDeviceInfo = deviceInfo;
				result.setCode(Result.CODE_SUCCESS);
				
			} else {
				deviceInfo.setRegistered(true);
				deviceInfo.setRegistTime((new Date()).getTime());
				session.update(deviceInfo);
				
				this.registerDeviceEx(session, deviceInfo);
				
				registeredDeviceInfo = deviceInfo;
				result.setCode(Result.CODE_SUCCESS);
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			result.setCode(Result.CODE_ERROR);
			result.setDescription(e.getMessage());
			
			e.printStackTrace();
			_logger.error(e);
		} finally {
			session.close();
		}
		
		if (result.getCode() == Result.CODE_SUCCESS) {
			this.publish(new DeviceRegistered(registeredDeviceInfo));
		}
		
		return result;
	}

	private Result unregisterDevice(String deviceUUID) {
		if (StringUtils.isEmpty(deviceUUID)) {
			return new Result(Result.CODE_ERROR, "_uuid is empty");
		}
		
		Result result = new Result(Result.CODE_FAILURE);
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			DeviceInfo deviceInfo = (DeviceInfo) session.createQuery(
					"from DeviceInfo where uuid='" + deviceUUID + "'").uniqueResult();
			
			if (null == deviceInfo) {
				result.setCode(Result.CODE_ERROR);
				result.setDescription("device does not exist");
				
			} else if (!deviceInfo.getRegistered()) {
				result.setCode(Result.CODE_SUCCESS);
				
			} else {
				deviceInfo.setRegistered(false);
				deviceInfo.setRegistTime(null);
				session.update(deviceInfo);
				
				this.unregisterDeviceEx(session, deviceInfo);

				result.setCode(Result.CODE_SUCCESS);
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			result.setCode(Result.CODE_ERROR);
			result.setDescription(e.getMessage());
			
			e.printStackTrace();
			_logger.error(e);
		} finally {
			session.close();
		}
		
		if (result.getCode() == Result.CODE_SUCCESS) {
			this.publish(new DeviceUnregistered(deviceUUID));
		}
		
		return result;
	}
	
	private void registerDeviceEx(Session session, DeviceInfo deviceInfo) {
		switch (deviceInfo.getComponent()) {
		case Identifiable.COMPONENT_CHECK_TUNNEL:
		{
			CheckTunnelInfo checkTunnelInfo = new CheckTunnelInfo();
			checkTunnelInfo.setUUID(deviceInfo.getUUID());
			session.saveOrUpdate(checkTunnelInfo);
			break;
		}
		case Identifiable.COMPONENT_TRACK_TUNNEL:
		{
			TrackTunnelInfo trackTunnelInfo = new TrackTunnelInfo();
			trackTunnelInfo.setUUID(deviceInfo.getUUID());
			session.saveOrUpdate(trackTunnelInfo);
			break;
		}
		case Identifiable.COMPONENT_WRISTBAND_PROXY:
		{
			WristbandInfo wristbandInfo = new WristbandInfo();
			wristbandInfo.setUUID(deviceInfo.getUUID());
			session.saveOrUpdate(wristbandInfo);
			break;
		}
		case Identifiable.COMPONENT_MOBILE_READER:
		{
			MobileReaderInfo mobileReaderInfo = new MobileReaderInfo();
			mobileReaderInfo.setUUID(deviceInfo.getUUID());
			session.saveOrUpdate(mobileReaderInfo);
		}
		default:
			break;
		}
	}

	private void unregisterDeviceEx(Session session, DeviceInfo deviceInfo) {
		String queryString = null;
		
		switch (deviceInfo.getComponent()) {
		case Identifiable.COMPONENT_CHECK_TUNNEL:
		{
			queryString = "delete CheckTunnelInfo where uuid='" + deviceInfo.getUUID() + "'";
			break;
		}
		case Identifiable.COMPONENT_TRACK_TUNNEL:
		{
			queryString = "delete TrackTunnelInfo where uuid='" + deviceInfo.getUUID() + "'";
			break;
		}
		case Identifiable.COMPONENT_WRISTBAND_PROXY:
		{
			queryString = "delete WristbandBindingInfo where uuid='" + deviceInfo.getUUID() + "'";
			break;
		}
		case Identifiable.COMPONENT_MOBILE_READER:
		{
			queryString = "delete MobileReaderInfo where uuid='" + deviceInfo.getUUID() + "'";
			break;
		}
		default:
			break;
		}
		
		if (queryString != null) {
			session.createQuery(queryString).executeUpdate();
		}
	}
	
}
