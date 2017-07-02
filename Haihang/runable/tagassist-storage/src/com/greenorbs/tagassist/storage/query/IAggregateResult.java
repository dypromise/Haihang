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

import com.greenorbs.tagassist.IPropertySupport;


public interface IAggregateResult<E extends IPropertySupport> extends IQueryResult<E>{
	
	public static final String PROPERTY_VALUE = "Value ";

	public Object getValue();

	public void setValue(Object value);
}
