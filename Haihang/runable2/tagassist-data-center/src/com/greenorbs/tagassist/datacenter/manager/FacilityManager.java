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

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.greenorbs.tagassist.FacilityInfo;
import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.FacilityRegister;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.FacilityUnregister;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.FacilityUpdate;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.FacilityRegistered;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.FacilityUnregistered;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.FacilityUpdated;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Confirmation;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class FacilityManager extends BusMessageHandler {

	@Override
	public void initSubscriber() {
		/* Administrator commands */
		this.subscribe(FacilityRegister.class);
		this.subscribe(FacilityUpdate.class);
		this.subscribe(FacilityUnregister.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof FacilityRegister) {
			
			FacilityRegister m = (FacilityRegister) message;
			FacilityInfo facilityInfo = m.getFacilityInfo();
			Result result = this.registerFacility(facilityInfo);
			
			reply = new Confirmation(m.getMessageId(), result.getCode(), result.getDescription());
			
			if (result.getCode() == Result.CODE_SUCCESS) {
				this.publish(new FacilityRegistered(facilityInfo));
			}
			
		} else if (message instanceof FacilityUpdate) {
			
			FacilityUpdate m = (FacilityUpdate) message;
			FacilityInfo facilityInfo = m.getFacilityInfo();
			Result result = this.updateFacility(facilityInfo);
			
			reply = new Confirmation(m.getMessageId(), result.getCode(), result.getDescription());
			
			if (result.getCode() == Result.CODE_SUCCESS) {
				this.publish(new FacilityUpdated(facilityInfo));
			}
			
		} else if (message instanceof FacilityUnregister) {
			
			FacilityUnregister m = (FacilityUnregister) message;
			String facilityUUID = m.getFacilityUUID();
			Result result = this.unregisterFacility(facilityUUID);
			
			reply = new Confirmation(m.getMessageId(), result.getCode(), result.getDescription());
			
			if (result.getCode() == Result.CODE_SUCCESS) {
				this.publish(new FacilityUnregistered(facilityUUID));
			}
			
		}
		
		return reply;
	}

	private Result registerFacility(FacilityInfo facilityInfo) {
		if (null == facilityInfo || StringUtils.isEmpty(facilityInfo.getUUID())) {
			return new Result(Result.CODE_ERROR, "facilityInfo is null, or _uuid is empty");
		}
		
		Result result = new Result(Result.CODE_FAILURE);
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			facilityInfo.setRegistTime((new Date()).getTime());
			session.save(facilityInfo);
			result.setCode(Result.CODE_SUCCESS);

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
		
		return result;
	}

	private Result updateFacility(FacilityInfo facilityInfo) {
		if (null == facilityInfo || StringUtils.isEmpty(facilityInfo.getUUID())) {
			return new Result(Result.CODE_ERROR, "facilityInfo is null, or _uuid is empty");
		}
		
		Result result = new Result(Result.CODE_FAILURE);
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			session.update(facilityInfo);
			result.setCode(Result.CODE_SUCCESS);
			
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
		
		return result;
	}

	private Result unregisterFacility(String facilityUUID) {
		if (StringUtils.isEmpty(facilityUUID)) {
			return new Result(Result.CODE_ERROR, "_uuid is empty");
		}
		
		Result result = new Result(Result.CODE_FAILURE);
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			session.createQuery("delete FacilityInfo where uuid='" + 
					facilityUUID + "'").executeUpdate();
			result.setCode(Result.CODE_SUCCESS);
			
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
		
		return result;
	}
	
}
