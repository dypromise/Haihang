/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 7, 2012
 */

package com.greenorbs.tagassist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "notification")
@Entity
public class NotificationInfo extends AbstractPropertySupport implements Comparable<NotificationInfo>, Cloneable {
		
	private static final long serialVersionUID = -3702168375058069123L;
	
	public static final String PROPERTY_UUID = "uuid";
	
	public static final String PROPERTY_CONTENT = "content";
	
	public static final String PROPERTY_TIME = "time";
	
	public static final String PROPERTY_EXPIRE = "expire";
	
	public static final String PROPERTY_DELETED = "deleted";
	
	public NotificationInfo() {}
	
	public NotificationInfo(String uuid, String content, long time, long expire) {
		this.setUUID(uuid);
		this.setContent(content);
		this.setTime(time);
		this.setExpire(expire);
	}
	
	@Id
	@Column(name = "uuid")
	public String getUUID() {
		return (String)this.getProperty(PROPERTY_UUID);
	}

	public void setUUID(String uuid) {
		this.setProperty(PROPERTY_UUID, uuid);
	}

	@Column(name = "content")
	public String getContent() {
		return (String)this.getProperty(PROPERTY_CONTENT);
	}

	public void setContent(String content) {
		this.setProperty(PROPERTY_CONTENT, content);
	}
	
	@Column(name = "time")
	public Long getTime() {
		return (Long)this.getProperty(PROPERTY_TIME);
	}

	public void setTime(Long time) {
		this.setProperty(PROPERTY_TIME, time);
		
	}

	@Column(name = "expire")
	public Long getExpire() {
		return (Long)this.getProperty(PROPERTY_EXPIRE);
	}

	public void setExpire(Long expire) {
		this.setProperty(PROPERTY_EXPIRE, expire);
	}
	
	@Column(name = "deleted")
	public Boolean getDeleted() {
		return (Boolean)this.getProperty(PROPERTY_DELETED);
	}

	public void setDeleted(Boolean deleted) {
		this.setProperty(PROPERTY_DELETED, deleted);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NotificationInfo) {
			NotificationInfo n = (NotificationInfo) obj;
			return this.getUUID().equals(n.getUUID());
		}
		return false;
	}

	@Override
	public int compareTo(NotificationInfo notification) {
		return (int) (this.getTime() - notification.getTime());
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
