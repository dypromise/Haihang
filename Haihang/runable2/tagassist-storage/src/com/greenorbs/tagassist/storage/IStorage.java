/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Author: Lei Yang, 2012-2-17
 */

package com.greenorbs.tagassist.storage;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.greenorbs.tagassist.IPropertySupport;
import com.greenorbs.tagassist.storage.query.CountResult;
import com.greenorbs.tagassist.storage.query.IQuery;
import com.greenorbs.tagassist.storage.query.IQueryResult;

public interface IStorage<E extends IPropertySupport> {

	public void initialize();

	public boolean synchronize();

	/* ========Query operations================ */

	public void query(IQuery<E> query, IQueryResult<E> result);

	public void query(IQuery<E> query, IQueryResult<E> result, boolean callback);

	public CountResult<E> count(IQuery<E> query);

	public CountResult<E> count(IQuery<E> query, boolean callback);

	public boolean containsQuery(IQuery<E> query);

	public int querySize();

	/* ======Collection operations=========== */

	public boolean add(E e);

	public boolean add(Object trigger, E e);

	public boolean addAll(Collection<? extends E> c);

	public boolean addAll(Object trigger, Collection<? extends E> c);

	public boolean remove(Object e);

	public boolean remove(Object trigger, Object e);

	public boolean removeAll(Collection<?> c);

	public boolean removeAll(Object trigger, Collection<?> c);

	public Iterator<E> iterator();

}
