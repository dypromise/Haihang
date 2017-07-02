/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 9, 2012
 */

package com.greenorbs.tagassist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "facility")
@Entity
public class FacilityInfo extends AbstractPropertySupport implements Cloneable {
	
	private static final long serialVersionUID = -4163906568272267376L;
	
	public static final int STATUS_NORMAL	= 1;
	
	public static final int STATUS_UNUSABLE	= 2;
	
	public static final String PROPERTY_UUID = "uuid";
	
	public static final String PROPERTY_TYPE = "type";
	
	public static final String PROPERTY_NAME = "name";
	
	public static final String PROPERTY_STATUS = "status";
	
	public static final String PROPERTY_REGIST_TIME = "registTime";
	
	public static final String PROPERTY_REMARK = "remark";
	
	private String _uuid;
	
	private int _type;
	
	private String _name;
	
	private int _status;
	
	private Long _registTime;
	
	private String _remark;
	
	private String _locationParam1;
	
	private Float _locationParam2;
	
	private Float _locationParam3;

	@Id
	@Column(name = "uuid")
	public String getUUID() {
		return _uuid;
	}

	public void setUUID(String uuid) {
		_uuid = uuid;
	}

	@Column(name = "type")
	public int getType() {
		return _type;
	}

	public void setType(int type) {
		_type = type;
	}

	@Column(name = "name")
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}
	
	@Column(name = "status")
	public int getStatus() {
		return _status;
	}

	public void setStatus(int status) {
		_status = status;
	}
	
	@Column(name = "registTime", nullable = true)
	public Long getRegistTime() {
		return _registTime;
	}

	public void setRegistTime(Long registTime) {
		_registTime = registTime;
	}
	
	@Column(name = "remark", nullable = true)
	public String getRemark() {
		return _remark;
	}

	public void setRemark(String remark) {
		_remark = remark;
	}

	@Column(name = "locationParam1", nullable = true)
	public String getLocationParam1() {
		return _locationParam1;
	}

	public void setLocationParam1(String locationParam1) {
		_locationParam1 = locationParam1;
	}

	@Column(name = "locationParam2", nullable = true)
	public Float getLocationParam2() {
		return _locationParam2;
	}

	public void setLocationParam2(Float locationParam2) {
		_locationParam2 = locationParam2;
	}

	@Column(name = "locationParam3", nullable = true)
	public Float getLocationParam3() {
		return _locationParam3;
	}

	public void setLocationParam3(Float locationParam3) {
		_locationParam3 = locationParam3;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FacilityInfo) {
			FacilityInfo f = (FacilityInfo) obj;
			return this._uuid.equals(f.getUUID());
		}
		return false;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
