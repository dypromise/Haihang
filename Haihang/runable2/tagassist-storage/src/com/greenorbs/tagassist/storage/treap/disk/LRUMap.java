package com.greenorbs.tagassist.storage.treap.disk;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class LRUMap<K, V> extends LinkedHashMap<K, V> {

	/**
         * 
         */
	private static final long serialVersionUID = -6791021890755690917L;
	private int maxCapacity;
	private ReentrantLock lock = new ReentrantLock();

	// ------------------------------------------------------------ Constructors

	public LRUMap(int maxCapacity) {
		super(maxCapacity, 0.75f, true);
		this.maxCapacity = maxCapacity;
	}

	// ---------------------------------------------- Methods from LinkedHashMap

	protected boolean removeEldestEntry(
			@SuppressWarnings("rawtypes") Map.Entry eldest) {
		return (size() > maxCapacity);
	}

	@Override
	// ���ö�д������ΪLRUԭ����readҲ�������޸Ĳ���
	public V get(Object key) {
		try {
			lock.lock();
			return super.get(key);
		} finally {
			lock.unlock();
		}
	}

	public V put(K key, V value) {
		try {
			lock.lock();
			return super.put(key, value);
		} finally {
			lock.unlock();
		}
	};
}
