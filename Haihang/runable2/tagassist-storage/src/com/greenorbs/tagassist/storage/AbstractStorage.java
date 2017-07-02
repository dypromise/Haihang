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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.greenorbs.tagassist.IPropertySupport;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.IMessageHandler;
import com.greenorbs.tagassist.messagebus.io.MessageHelper;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.io.Subscriber;
import com.greenorbs.tagassist.storage.query.CountResult;
import com.greenorbs.tagassist.storage.query.IQuery;
import com.greenorbs.tagassist.storage.query.IQueryResult;
import com.greenorbs.tagassist.storage.query.StorageAddEvent;
import com.greenorbs.tagassist.storage.query.StorageRemoveEvent;
import com.greenorbs.tagassist.storage.query.StorageUpdateEvent;
//import com.sun.org.apache.xml.internal.security.encryption.Reference;

public abstract class AbstractStorage<E extends IPropertySupport> implements
		IStorage<E>, IMessageHandler, PropertyChangeListener, Collection<E> {

	protected Logger _log = Logger.getLogger(this.getClass());

	protected Map<IQuery<E>, IQueryResult<E>> _queries;

	protected Publisher _publisher;
	protected Subscriber _subscriber;

	@SuppressWarnings("unchecked")
	public AbstractStorage(Identifiable identity) {
		this._publisher = new Publisher(identity);
		this._subscriber = new Subscriber(identity);

		this._queries = (Map<IQuery<E>, IQueryResult<E>>) Collections
				.synchronizedMap(new ReferenceMap(ReferenceMap.HARD,
						ReferenceMap.WEAK));
	}

	public AbstractStorage() {
		this(null);
	}

	protected abstract void initSubscriber();

	protected abstract Collection<E> crawl();

	@Override
	public synchronized void initialize() {
		this.initSubscriber();

		try {
			this._publisher.start();
			this._subscriber.start();
		} catch (Exception e) {
			e.printStackTrace();
			_log.error(e.getMessage());
		}

		this.synchronize();
	}

	protected void subscribe(Class<?> topicClass) {
		try {
			this._subscriber.subscribe(topicClass, this);
		} catch (MessageBusException e) {
			e.printStackTrace();
			_log.error("Failed to subscribe "
					+ MessageHelper.getTopicName(topicClass));
		}
	}

	@Override
	public synchronized void query(IQuery<E> query, IQueryResult<E> result) {
		this.query(query, result, true);
	}

	@Override
	public synchronized void query(IQuery<E> query, IQueryResult<E> result,
			boolean callback) {

		try {
			_log.debug("add new query:" + query);

			Validate.notNull(query);
			Validate.notNull(result);

			if (callback == true) {
				this._queries.put(query, result);
			}

			Iterator<E> it = this.iterator();

			while (it.hasNext()) {
				E e = it.next();
				if (query.accept(e)) {
					PropertyChangeListener[] listeners = e
							.getPropertyChangeListeners();
					if (ArrayUtils.contains(listeners, this) == false) {
						e.addPropertyChangeListener(this);
					}
					result.itemAdded(new StorageAddEvent<E>(this, this, e));
				}
			}

			_log.debug("after query:" + this.snapshot());

			int size = this._queries.size();

			if (size != 0 && size % 200 == 0) {
				_log.warn("The queries from the storage is too large. The size is: "
						+ this._queries.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("It fails to add the query: [" + query + "]", e);
		}

	}

	@Override
	public synchronized CountResult<E> count(IQuery<E> query, boolean callback) {

		CountResult<E> countResult = new CountResult<E>();

		this.query(query, countResult, callback);

		_log.debug(this.snapshot());

		return countResult;
	}

	@Override
	public synchronized CountResult<E> count(IQuery<E> query) {
		return count(query, true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean synchronize() {

		Collection<E> result = null;

		try {
			_log.debug("start to synchronize the storage with data center.");

			result = this.crawl();

			if (result == null) {
				return false;
			}

			Collection<E> needRemoved = CollectionUtils.subtract(this, result);
			for (E o : needRemoved) {
				this.remove(null, o);
			}

			Collection<E> needAdded = CollectionUtils.subtract(result, this);
			for (E o : needAdded) {
				this.add(null, o);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			this._log.error("It fails to crawl the data from the data center."
					+ e);
			return false;
		}

	}

	private synchronized void fireItemAdded(Object trigger, E item) {

		Validate.notNull(item);

		for (IQuery<E> q : this._queries.keySet()) {
			try {
				if (q.accept(item) && this._queries.get(q) != null) {
					this._queries.get(q).itemAdded(
							new StorageAddEvent<E>(this, trigger, item));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				this._log.error("It fails to fire the item added event." + ex);
			}
		}
	}

	private synchronized void fireItemRemoved(Object trigger, E item) {

		Validate.notNull(item);

		for (IQuery<E> q : this._queries.keySet()) {
			try {

				if (q.accept(item) && this._queries.get(q) != null) {
					this._queries.get(q).itemRemoved(
							new StorageRemoveEvent<E>(this, trigger, item));
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				this._log
						.error("It fails to fire the item removed event." + ex);
			}

		}

	}

	@SuppressWarnings("unchecked")
	public synchronized void propertyChange(PropertyChangeEvent evt) {

		E e = (E) evt.getSource();

		for (IQuery<E> q : this._queries.keySet()) {
			try {
				IQueryResult<E> result = this._queries.get(q);

				if (result.contains(e)) {

					if (q.accept(e)) {
						this.updateWithoutFireEvent(e);
						result.itemUpdated(new StorageUpdateEvent<E>(this,
								null, e, evt.getPropertyName(), evt
										.getOldValue(), evt.getNewValue()));
					} else {
						result.itemRemoved(new StorageRemoveEvent<E>(this,
								null, e));
					}
				} else {
					if (q.accept(e)) {
						result.itemAdded(new StorageAddEvent<E>(this, null, e));
					} else {
						// Keep nothing
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				this._log.error("It fails to fire the item update event." + ex);
			}

		}
	}

	@Override
	public synchronized boolean containsQuery(IQuery<E> query) {
		return this._queries.containsKey(query);
	}

	@Override
	public synchronized int querySize() {
		return this._queries.size();
	}

	protected abstract boolean addWithoutFireEvent(E item);

	protected abstract boolean removeWithoutFireEvent(Object o);

	protected boolean updateWithoutFireEvent(E item) {
		// keep blank for memory storage in default
		return true;
	}

	@Override
	public synchronized final boolean add(E item) {
		return add(this, item);
	}

	public synchronized final boolean add(Object trigger, E item) {
		item.addPropertyChangeListener(this);
		this.fireItemAdded(trigger, item);
		return this.addWithoutFireEvent(item);
	}

	public synchronized final boolean addAll(Collection<? extends E> c) {
		return this.addAll(this, c);
	}

	public synchronized final boolean addAll(Object trigger,
			Collection<? extends E> c) {

		if (c == null) {
			return false;
		}

		for (E e : c) {
			this.add(trigger, e);
		}

		return true;
	}

	public synchronized final boolean remove(Object o) {
		return remove(this, o);
	}

	@SuppressWarnings("unchecked")
	public synchronized final boolean remove(Object trigger, Object object) {
		IPropertySupport item = (IPropertySupport) object;
		item.removePropertyChangeListener(this);
		this.fireItemRemoved(trigger, (E) item);
		return this.removeWithoutFireEvent(object);
	}

	public synchronized final boolean removeAll(Collection<?> c) {
		return this.removeAll(this, c);
	}

	public synchronized final boolean removeAll(Object trigger, Collection<?> c) {
		if (c == null) {
			return false;
		}
		for (Object e : c) {
			this.remove(trigger, e);
		}

		return true;
	}

	public synchronized final void clear() {
		this.removeAll(this);
	}

	public synchronized final boolean retainAll(Collection<?> c) {

		if (c == null) {
			return false;
		}

		List list = new ArrayList();
		for (Object o : this) {
			if (c.contains(o) == false) {
				list.add(o);
			}
		}

		return this.removeAll(list);
	}

	public synchronized String snapshot() {
		StringBuffer sb = new StringBuffer();
		sb.append("snapshot(" + this + ")");
		sb.append("[");
		sb.append("{query size:" + this._queries.size() + "},");

		sb.append("{object size:" + this.size() + "}");
		sb.append("]");
		return sb.toString();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public Map<IQuery<E>, IQueryResult<E>> queries() {
		return this._queries;
	}

}
