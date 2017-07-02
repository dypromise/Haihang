/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-2-17
 */

package com.greenorbs.tagassist.storage.query;

import java.util.Iterator;
import com.greenorbs.tagassist.IPropertySupport;

public interface IQueryResult<E extends IPropertySupport> {

	public void itemAdded(StorageAddEvent<E> event);

	public void itemRemoved(StorageRemoveEvent<E> event);

	public void itemUpdated(StorageUpdateEvent<E> event);

	public boolean contains(E item);

	public Iterator<E> iterator();

}
