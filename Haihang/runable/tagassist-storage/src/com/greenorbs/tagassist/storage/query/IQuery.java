/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Yi Guo, 2012-2-17
 */


package com.greenorbs.tagassist.storage.query;


public interface IQuery<E> {

	/**
	 * To determine whether the item is accepted.
	 * @param item
	 * @return 
	 */
	public boolean accept(E item);
	
}

