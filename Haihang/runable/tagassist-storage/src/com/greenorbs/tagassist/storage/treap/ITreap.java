package com.greenorbs.tagassist.storage.treap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public interface ITreap<K extends Comparable<K>, V> {

	public abstract void put(K key, V value);

	public abstract V get(K key);
	
	public abstract Map<K,V> bulkGet(List<K> keys);
	
	public abstract void bulkPut(Map<K,V> pairs);
	
	public abstract Map<K,V> range(K start, K end,int limit);

	public abstract int length();

	public abstract boolean delete(K key);

	public abstract boolean removePrefix(K prefixString);
	
	public abstract Map<K,V> prefix(K prefixString,int limit,K startK, boolean asc);
	
	public abstract Map<K,V> bulkPrefix(List<String> prefixList, int limit,K startK, boolean asc);
	
	public abstract Map<K,V> before(K key,int limit);

	public abstract Map<K,V> after(K key,int limit);
	
	public Map<K,V> kmin(int k);
	
	public Map<K,V> kmax(int k);
	
	public Entry<K, V> kth(int k,boolean asc);

	public int rank(K key,boolean asc);
}
