/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-2-28
 */

package com.greenorbs.tagassist.storage.memory;

import java.util.Collection;
import java.util.Iterator;

import com.greenorbs.tagassist.FacilityInfo;
import com.greenorbs.tagassist.storage.FacilityStorage;

public class MemoryFacilityStorage extends FacilityStorage {
	

	@Override
	public  FacilityInfo findById(String id) {
		return null;
	}

	@Override
	public FacilityInfo getFacilities(Integer type) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean contains(Object o) {
		if(o instanceof String){
			for(FacilityInfo f: this){
				if(f.getUUID().equals(o)){
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public Iterator<FacilityInfo> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return null;
	}


	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public int getCarriageSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean addWithoutFireEvent(FacilityInfo item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean removeWithoutFireEvent(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

}
