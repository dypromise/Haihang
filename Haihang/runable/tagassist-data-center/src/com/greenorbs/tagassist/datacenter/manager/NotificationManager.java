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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.greenorbs.tagassist.NotificationInfo;
import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.NotificationCreate;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.NotificationDelete;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.NotificationModify;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.NotificationCreated;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.NotificationDeleted;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.NotificationModified;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Confirmation;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class NotificationManager extends BusMessageHandler {
	
	@Override
	public void initSubscriber() {
		/* Administrator commands */
		this.subscribe(NotificationCreate.class);
		this.subscribe(NotificationModify.class);
		this.subscribe(NotificationDelete.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof NotificationCreate) {
			
			NotificationCreate m = (NotificationCreate) message;
			NotificationInfo notificationInfo = m.getNotificationInfo();
			Result result = this.createOrModifyNotification(notificationInfo);
			
			reply = new Confirmation(m.getMessageId(), result.getCode(), result.getDescription());
			
			if (result.getCode() == Result.CODE_SUCCESS) {
				this.publish(new NotificationCreated(notificationInfo));
			}
			
		} else if (message instanceof NotificationModify) {
			
			NotificationModify m = (NotificationModify) message;
			NotificationInfo notificationInfo = m.getNotificationInfo();
			Result result = this.createOrModifyNotification(notificationInfo);
			
			reply = new Confirmation(m.getMessageId(), result.getCode(), result.getDescription());
			
			if (result.getCode() == Result.CODE_SUCCESS) {
				this.publish(new NotificationModified(notificationInfo));
			}
			
		} else if (message instanceof NotificationDelete) {
			
			NotificationDelete m = (NotificationDelete) message;
			String notificationUUID = m.getNotificationUUID();
			Result result = this.deleteNotification(notificationUUID);
			
			reply = new Confirmation(m.getMessageId(), result.getCode(), result.getDescription());
			
			if (result.getCode() == Result.CODE_SUCCESS) {
				this.publish(new NotificationDeleted(notificationUUID));
			}
			
		}

		return reply;
	}
	
	private Result createOrModifyNotification(NotificationInfo notificationInfo) {
		if (null == notificationInfo || StringUtils.isEmpty(notificationInfo.getUUID())) {
			return new Result(Result.CODE_ERROR, "notificationInfo is null, or _uuid is empty");
		}
		
		Result result = new Result(Result.CODE_FAILURE);
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			session.saveOrUpdate(notificationInfo);
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
	
	private Result deleteNotification(String notificationUUID) {
		if (StringUtils.isEmpty(notificationUUID)) {
			return new Result(Result.CODE_ERROR, "notificationUUID is empty");
		}
		
		Result result = new Result(Result.CODE_FAILURE);
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			NotificationInfo storedNotification = (NotificationInfo) session.createQuery(
					"from NotificationInfo where uuid='" + notificationUUID + "'").uniqueResult();
			if (storedNotification != null && !storedNotification.getDeleted()) {
				storedNotification.setDeleted(true);
				session.update(storedNotification);
			}
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
