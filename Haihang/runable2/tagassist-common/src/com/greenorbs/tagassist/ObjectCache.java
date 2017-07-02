package com.greenorbs.tagassist;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class provides a very simple implementation of an object cache.
 */
public class ObjectCache<T> {

	/** Objects are stored here */
	private final Map<String, T> _objects;
	/** Holds custom expiration dates */
	private final Map<String, Long> _expire;
	/** The default expiration time in seconds */
	private final long _defaultExpire;
	/** Is used to speed up some operations */
	private final ExecutorService _threads;

	/**
	 * Constructs the cache with a default expiration time for the 
	 * objects of 60 seconds.
	 */
	public ObjectCache() {
		this(60);
	}

	/**
	 * Construct a cache with a custom expiration time for the objects.
	 * 
	 * @param defaultExpire
	 *            default expiration time in seconds
	 */
	public ObjectCache(final long defaultExpire) {
		_objects = Collections.synchronizedMap(new HashMap<String, T>());
		_expire = Collections.synchronizedMap(new HashMap<String, Long>());

		_defaultExpire = defaultExpire;

		_threads = Executors.newFixedThreadPool(256);
		Executors.newScheduledThreadPool(2).scheduleWithFixedDelay(removeExpired(), 
				_defaultExpire / 2, _defaultExpire, TimeUnit.SECONDS);
	}

	/**
	 * This Runnable removes expired objects.
	 */
	private final Runnable removeExpired() {
		return new Runnable() {
			public void run() {
				for (final String key : _expire.keySet()) {
					if (System.currentTimeMillis() > _expire.get(key)) {
						_threads.execute(createRemoveRunnable(key));
					}
				}
			}
		};
	}

	/**
	 * Returns a runnable that removes a specific object from the cache.
	 * 
	 * @param key
	 *            the key of the object
	 */
	private final Runnable createRemoveRunnable(final String key) {
		return new Runnable() {
			public void run() {
				_objects.remove(key);
				_expire.remove(key);
			}
		};
	}

	/**
	 * Returns the default expiration time for the objects in the cache.
	 * 
	 * @return default expiration time in seconds
	 */
	public long getDefaultExpire() {
		return _defaultExpire;
	}

	/**
	 * Put an object into the cache.
	 * 
	 * @param key
	 *            the object will be referenced with this key in the cache
	 * @param obj
	 *            the object
	 */
	public void put(final String key, final T obj) {
		this.put(key, obj, _defaultExpire);
	}

	/**
	 * Put an object into the cache with a custom expiration time.
	 * 
	 * @param key
	 *            the object will be referenced with this key in the cache
	 * @param obj
	 *            the object
	 * @param expire
	 *            custom expiration time in seconds
	 */
	public void put(final String key, final T obj, final long expire) {
		_objects.put(key, obj);
		_expire.put(key, System.currentTimeMillis() + expire * 1000);
	}

	/**
	 * Returns an object from the cache.
	 * 
	 * @param key
	 *            the key of the object you'd like to get
	 * @return the object with the given key
	 */
	public T get(final String key) {
		final Long expire = _expire.get(key);
		if (expire == null) return null;
		if (System.currentTimeMillis() > expire) {
			_objects.remove(key);
			_expire.remove(key);
			return null;
		}
		return _objects.get(key);
	}

	/**
	 * Convenience method.
	 */
	@SuppressWarnings("unchecked")
	public <R extends T> R get(final String key, final Class<R> type) {
		return (R) this.get(key);
	}
	
}