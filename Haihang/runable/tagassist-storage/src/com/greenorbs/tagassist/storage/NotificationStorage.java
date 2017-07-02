/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-2-22
 */

package com.greenorbs.tagassist.storage;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.greenorbs.tagassist.NotificationInfo;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.NotificationCreated;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.NotificationDeleted;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.NotificationModified;
import com.greenorbs.tagassist.messagebus.util.querier.NotificationQuerier;

public abstract class NotificationStorage extends AbstractStorage<NotificationInfo> {

	private NotificationQuerier _notificationQuerier;
	
	public abstract NotificationInfo findByNotificationId(String id);

	public NotificationStorage(Identifiable identity) {
		super(identity);
		this._notificationQuerier = new NotificationQuerier(this._publisher);
	}

	public NotificationStorage() {
		this(null);
	}

	@Override
	protected void initSubscriber() {
		this.subscribe(NotificationCreated.class);
		this.subscribe(NotificationModified.class);
		this.subscribe(NotificationDeleted.class);
	}

	@Override
	public void onMessage(AbstractMessage message) {
		// capture the message of notification creation.
		if (message instanceof NotificationCreated) {

			NotificationCreated m = (NotificationCreated) message;
			NotificationInfo notificationInfo = m.getNotificationInfo();

			this.handleNotificationCreated(message, notificationInfo);
		}

		// capture the message of notification modification.
		else if (message instanceof NotificationModified) {

			NotificationModified m = (NotificationModified) message;
			NotificationInfo notificationInfo = m.getNotificationInfo();

			this.handleNotificationModified(message, notificationInfo);
		}

		// capture the message of notification deletion.
		else if (message instanceof NotificationDeleted) {

			NotificationDeleted m = (NotificationDeleted) message;
			String notificationUUID = m.getNotificationUUID();

			this.handleNotificationDeleted(message, notificationUUID);
		}
	}

	private void handleNotificationCreated(AbstractMessage message,
			NotificationInfo notificationInfo) {
		if (null == notificationInfo || StringUtils.isEmpty(notificationInfo.getUUID())) {
			return;
		}

		if (!this.contains(notificationInfo.getUUID())) {
			this.add(message, notificationInfo);
		}
	}

	private void handleNotificationModified(AbstractMessage message,
			NotificationInfo notificationInfo) {
		if (null == notificationInfo || StringUtils.isEmpty(notificationInfo.getUUID())) {
			return;
		}

		NotificationInfo storedNotification = this.findByNotificationId(notificationInfo.getUUID());
		if (storedNotification != null) {
			storedNotification.setContent(notificationInfo.getContent());
			storedNotification.setExpire(notificationInfo.getExpire());
//			NotificationInfo old = this.itemUpdated(storedNotification);
//			this.fireItemUpdated(message, old, storedNotification);
		} else {
			this.add(message, notificationInfo);
		}
	}

	private void handleNotificationDeleted(AbstractMessage message,String notificationUUID) {
		if (StringUtils.isEmpty(notificationUUID)) {
			return;
		}

		NotificationInfo storedNotification = this.findByNotificationId(notificationUUID);
		if (storedNotification != null && !storedNotification.getDeleted()) {
			storedNotification.setDeleted(true);
//			NotificationInfo old = this.itemUpdated(storedNotification);
//			this.fireItemUpdated(message, old, storedNotification);
		}
	}

	@Override
	protected Collection<NotificationInfo> crawl() {
		NotificationInfo[] notificationInfoList = _notificationQuerier.getNotificationList();

		if (notificationInfoList != null) {
			return Arrays.asList(notificationInfoList);
		} else {
			return null;
		}
	}

}
