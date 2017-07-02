package com.greenorbs.tagassist.visualproxy;

import com.greenorbs.tagassist.NotificationInfo;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.message.VisualProxyMessages.VNotificationRemoved;
import com.greenorbs.tagassist.messagebus.message.VisualProxyMessages.VNotificationUpdated;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;
import com.greenorbs.tagassist.storage.DefaultQueryResult;
import com.greenorbs.tagassist.storage.query.IQuery;
import com.greenorbs.tagassist.storage.query.IQueryResult;
import com.greenorbs.tagassist.storage.query.StorageAddEvent;
import com.greenorbs.tagassist.storage.query.StorageFactory;
import com.greenorbs.tagassist.storage.query.StorageRemoveEvent;
import com.greenorbs.tagassist.storage.query.StorageUpdateEvent;

public class NotificationAdapter extends BusMessageHandler implements ISyncRequestHandler {

	private IQueryResult<NotificationInfo> _result;
	
	@Override
	public void initialize() {
		super.initialize();
		this.sync();
	}
	
	@Override
	public void sync() {
		this._result = new DefaultQueryResult<NotificationInfo>() {
			
			@Override
			public void itemAdded(StorageAddEvent<NotificationInfo> event) {
				super.itemAdded(event);
				
				NotificationInfo notificationInfo = event.getItem();
				NotificationAdapter.this.handleNotificationUpdated(notificationInfo);
			}
			
			@Override
			public void itemUpdated(StorageUpdateEvent<NotificationInfo> event) {
				super.itemUpdated(event);
				
				NotificationInfo notificationInfo = event.getItem();
				NotificationAdapter.this.handleNotificationUpdated(notificationInfo);
			}
			
			@Override
			public void itemRemoved(StorageRemoveEvent<NotificationInfo> event) {
				super.itemRemoved(event);
				
				NotificationInfo notificationInfo = event.getItem();
				NotificationAdapter.this.handleNotificationRemoved(notificationInfo);
			}
			
		};
		
		StorageFactory.getNotificationStorage().query(new IQuery<NotificationInfo>() {
			@Override
			public boolean accept(NotificationInfo item) {
				return true;
			}
		}, this._result);
	}
	
	private void handleNotificationUpdated(NotificationInfo notificationInfo) {
		VNotificationUpdated vUpdated = new VNotificationUpdated();
		vUpdated.setUUID(notificationInfo.getUUID());
		vUpdated.setContent(notificationInfo.getContent());
		vUpdated.setTime(notificationInfo.getTime());
		vUpdated.setExpire(notificationInfo.getExpire());
		
		this.publish(vUpdated);
	}
	
	private void handleNotificationRemoved(NotificationInfo notificationInfo) {
		VNotificationRemoved vRemoved = new VNotificationRemoved();
		vRemoved.setUUID(notificationInfo.getUUID());
		
		this.publish(vRemoved);
	}

	@Override
	protected void initSubscriber() {
		// do nothing
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		// do nothing
		return null;
	}

}
