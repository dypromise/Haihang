/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 10, 2012
 */

package com.greenorbs.tagassist.datacenter.manager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.greenorbs.tagassist.TrackTunnelInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class TrackTunnelManager extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Track-tunnel messages */
		this.subscribe(TrackTunnelMessages.LocationMoved.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof TrackTunnelMessages.LocationMoved) {
			
			TrackTunnelMessages.LocationMoved m = (TrackTunnelMessages.LocationMoved) message;
			String trackTunnelUUID = m.getSource();
			String param1 = m.getLocationParam1();
			Float param2 = m.getLocationParam2();
			
			this.updateLocation(trackTunnelUUID, param1, param2);
		
		}
		
		return reply;
	}

	private void updateLocation(String trackTunnelUUID, String param1, Float param2) {
		if (StringUtils.isEmpty(trackTunnelUUID)) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			TrackTunnelInfo trackTunnelInfo = (TrackTunnelInfo) session.createQuery(
					"from TrackTunnelInfo where uuid='" + trackTunnelUUID + "'").uniqueResult();
			if (null == trackTunnelInfo) {
				trackTunnelInfo = new TrackTunnelInfo();
				trackTunnelInfo.setUUID(trackTunnelUUID);
			}
			trackTunnelInfo.setLocationParam1(param1);
			trackTunnelInfo.setLocationParam2(param2);
			session.saveOrUpdate(trackTunnelInfo);
			
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

}
