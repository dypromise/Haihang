/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-4
 */

package com.greenorbs.tagassist.storage.query;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.greenorbs.tagassist.IPropertySupport;
import com.greenorbs.tagassist.storage.StorageException;

public class CountResult<E extends IPropertySupport> extends AbstractAggregateResult<E> {
	
	List<E> _objects = new Vector<E>();

	public CountResult(){
		super(new Integer(0));
	}
	
	public CountResult(Integer count){
		super(count);
	}
	
	@Override
	public synchronized void itemAdded(StorageAddEvent<E> event) {
		
		if(_objects.contains(event.getItem())==true){
			return;
		}
		
		_objects.add(event.getItem());
		
		if (this.getValue() == null) {
			this.setValue(new Integer(1));
		} else {
			this.setValue((Integer) this.getValue() + 1);
		}

	}

	@Override
	public synchronized void itemRemoved(StorageRemoveEvent<E> event) {

		
		if(_objects.contains(event.getItem())==false){
			return;
		}
		
		_objects.remove(event.getItem());
		
		if(this.getValue()==null){
			throw new StorageException("The wrong state of storage");
		}
		
		this.setValue((Integer)this.getValue()-1);
	}

	@Override
	public synchronized void itemUpdated(StorageUpdateEvent<E> event) {
		
		//keep nothing since it not affects the count aggregation.
		
	}

	@Override
	public String toString(){
		return this.getValue().toString();
	}

	@Override
	public boolean contains(E item) {
		return this._objects.contains(item);
	}

	@Override
	public Iterator<E> iterator() {
		return null;
	}

	

}
