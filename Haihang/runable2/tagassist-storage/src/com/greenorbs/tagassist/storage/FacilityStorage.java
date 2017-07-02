/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-2-27
 */

package com.greenorbs.tagassist.storage;

import java.util.Collection;
import java.util.List;

import com.greenorbs.tagassist.CarriageInfo;
import com.greenorbs.tagassist.FacilityInfo;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;

public abstract class FacilityStorage extends AbstractStorage<FacilityInfo> {
	
	public abstract int getCarriageSize();
	
	/*
	 * uuid
	 */
	public abstract FacilityInfo findById(String id);
	
	public abstract FacilityInfo getFacilities(Integer type);
	
	@Override
	protected void initSubscriber() {
	
	}
	
	@Override
	public void onMessage(AbstractMessage message) {
		
	}
	
	@Override
	protected Collection<FacilityInfo> crawl(){
		
		return null;
	}
}
