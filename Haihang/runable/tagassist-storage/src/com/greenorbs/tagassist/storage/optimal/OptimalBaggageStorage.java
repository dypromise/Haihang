/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Author: Lei Yang, 2012-2-22
 */

package com.greenorbs.tagassist.storage.optimal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.CompositeMap;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.messagebus.util.querier.BaggageQuerier;
import com.greenorbs.tagassist.storage.BaggageStorage;
import com.greenorbs.tagassist.storage.query.StorageFactory;

public class OptimalBaggageStorage extends BaggageStorage {

	// <FlightId, <BaggageNumber, BaggeInfo>>
	private Map<String, Map<String, BaggageInfo>> _baggages;

	private static Logger _log = Logger.getLogger(OptimalBaggageStorage.class);

	public OptimalBaggageStorage() {
		this._baggages = Collections
				.synchronizedMap(new HashMap<String, Map<String, BaggageInfo>>());
	}

	private synchronized Map<String, BaggageInfo> compositeMap() {
		CompositeMap maps = new CompositeMap();
		try {

			for (Map<String, BaggageInfo> map : this._baggages.values()) {
				maps.addComposited(map);
			}
		} catch (IllegalArgumentException e) {
			_log.error("The same baggage happens to be in two different flights. This violate the rule. "
					+ "The repeated baggage object will be discarded from the storage.");
		}
		return maps;
	}

	@Override
	public synchronized boolean addWithoutFireEvent(BaggageInfo item) {
		if (item == null) {
			return false;
		}

		try {
			if (this._baggages.containsKey(item.getFlightId()) == false) {
				this._baggages.put(item.getFlightId(),
						new HashMap<String, BaggageInfo>());
			}

			this._baggages.get(item.getFlightId()).put(item.getNumber(), item);

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("It fails to add bagage into MemoryBaggageStorage." + e);
			return false;
		}

		return true;
	}

	@Override
	public synchronized boolean removeWithoutFireEvent(Object item) {

		if (item == null) {
			return false;
		}

		try {
			if (item instanceof BaggageInfo) {
				BaggageInfo bag = (BaggageInfo) item;
				Map<String, BaggageInfo> map = this._baggages.get(bag
						.getFlightId());
				if (map != null) {
					map.remove(bag.getNumber());
					if (map.isEmpty()) {
						this._baggages.remove(bag.getFlightId());
					}
				}
			}
		} catch (Exception e) {
			_log.error("It fails to remove baggage from the MemoryBaggageStorage with exception:"
					+ e);
			return false;
		}

		return true;

	}

	@Override
	public synchronized int size() {

		return this.compositeMap().size();

	}

	@Override
	public synchronized Iterator<BaggageInfo> iterator() {

		return compositeMap().values().iterator();
	}

	@Override
	public synchronized boolean isEmpty() {
		return compositeMap().isEmpty();
	}

	@Override
	public synchronized boolean contains(Object o) {

		if (o instanceof String) {
			return compositeMap().containsKey(o);
		}

		return compositeMap().values().contains(o);
	}

	@Override
	public synchronized Object[] toArray() {
		return compositeMap().values().toArray();
	}

	@Override
	public synchronized <T> T[] toArray(T[] a) {

		return (T[]) compositeMap().values().toArray(a);
	}

	@Override
	public synchronized boolean containsAll(Collection<?> c) {
		return compositeMap().values().containsAll(c);
	}

	/**************** BaggageStorage interface ****************/

	@Override
	public synchronized List<BaggageInfo> findByFlightId(String flightId) {

		List<BaggageInfo> result = new ArrayList<BaggageInfo>();

		if (StringUtils.isBlank(flightId)) {
			return result;
		}

		if (this._baggages.containsKey(flightId)) {
			for (BaggageInfo bag : this._baggages.get(flightId).values()) {
				result.add(bag);
			}
		}

		return result;

	}

	@Override
	public synchronized BaggageInfo findByEPC(String EPC) {

		Iterator<BaggageInfo> it = this.iterator();
		BaggageInfo bag = null;

		while (it.hasNext()) {
			bag = it.next();
			if (bag.getEPC().equals(EPC)) {
				return bag;
			}
		}

		return null;
	}

	@Override
	public synchronized List<BaggageInfo> findByStatus(String flightId,
			int status) {

		List<BaggageInfo> result = new ArrayList<BaggageInfo>();

		if (this._baggages.containsKey(flightId)) {
			for (BaggageInfo bag : this._baggages.get(flightId).values()) {
				if (bag.getStatus() == status) {

					result.add(bag);

				}
			}
		}
		return result;
	}

	@Override
	public synchronized BaggageInfo findByNumber(String number) {
		if (StringUtils.isBlank(number)) {
			return null;
		}

		BaggageInfo bag = compositeMap().get(number);

		return bag;
	}

	@Override
	public synchronized BaggageInfo findByEPC(String flightId, String EPC) {

		if (this._baggages.containsKey(flightId)) {
			for (BaggageInfo bag : this._baggages.get(flightId).values()) {
				if (bag.getEPC().equals(EPC)) {
					return bag;
				}
			}
		}
		return null;
	}

	@Override
	public synchronized boolean removeByFlightId(String flightId) {

		if (StringUtils.isBlank(flightId)) {
			return false;
		}

		Iterator<BaggageInfo> bagIt = this.iterator();
		while (bagIt.hasNext()) {
			BaggageInfo item = bagIt.next();
			if (item.getFlightId().equals(flightId)) {
				bagIt.remove();
			}
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean synchronize() {
		//synchronize memory baggages 
		boolean result = this.synchronizeLocalWithCenter(this._baggages);
		
		//using Timer synchronize disk baggages every 30mins
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				OptimalBaggageStorage.this.synchronizeLocalWithCenter(
						Collections.synchronizedMap(new HashMap<String, Map<String, BaggageInfo>>()));
			}			
		}, 30*60*1000, 30*60*1000);
		
		return result;
	}

	@Override
	protected synchronized Collection<BaggageInfo> crawl() {
		List<BaggageInfo> baggageCollection = new ArrayList<BaggageInfo>();

		FlightInfo[] flightInfoList = this.getFlightQuerier().getFlightList();
		if (flightInfoList != null) {
			for (FlightInfo flightInfo : flightInfoList) {
				String flightId = flightInfo.getFlightId();
				BaggageInfo[] baggageInfoList = this.getBaggageQuerier()
						.getBaggageList(flightId);
				if (baggageInfoList != null) {
					baggageCollection.addAll(Arrays.asList(baggageInfoList));
				}
			}
		}

		return baggageCollection;
	}

	private synchronized boolean synchronizeLocalWithCenter(
			Map<String, Map<String, BaggageInfo>> newReplication) {
		// 1st: data from local file: flight list and baggage A
		Map<String, Map<String, BaggageInfo>> baggageA = this
				.retrieveFromDiskReplication();

		// 2st: flight list from data center B
		FlightInfo[] centerFlightList = this.getFlightQuerier().getFlightList();

		// 3st: compare flight list, A=> A1(timeout) & A2(reserve), B=>B1(new) &
		// B2(difference retrieve)
		Map<String, Map<String, BaggageInfo>> baggageC1 = Collections
				.synchronizedMap(new HashMap<String, Map<String, BaggageInfo>>());
		Map<String, Map<String, BaggageInfo>> baggageC2 = Collections
				.synchronizedMap(new HashMap<String, Map<String, BaggageInfo>>());
		for (FlightInfo fi : centerFlightList) {
			if (baggageA.containsKey(fi.getFlightId()))
				baggageC2.put(fi.getFlightId(), baggageA.get(fi.getFlightId()));
			else
				baggageC1.put(fi.getFlightId(),
						new HashMap<String, BaggageInfo>());
		}

		// new thread pool
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 20, 3,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100),
				new ThreadPoolExecutor.DiscardOldestPolicy());

		// 4st: fetch new flight and baggage => C1
		for (String fi : baggageC1.keySet())
			threadPool.execute(new CrawlBaggageThread(fi, baggageC1.get(fi),
					this.getBaggageQuerier()));

		// 5st: retrieve difference baggage for A2&B2 => C2
		for (String fi : baggageC2.keySet())
			threadPool.execute(new RetrieveBaggageThread(fi, baggageC2.get(fi),
					this.getBaggageQuerier()));

		// wait for thread pool over
		threadPool.shutdown();
		try {
			while (threadPool.getPoolSize() != 0)
				Thread.sleep(1000);
		} catch (InterruptedException e) {
			_log.warn("syncrhonize: main thread sleep error");
		}

		// 6st: composite C1 & C2 => _baggages
		newReplication.clear();
		newReplication.putAll(baggageC1);
		newReplication.putAll(baggageC2);

		// 7st: serializable to local disk
		this.dumpToDiskReplication(newReplication);

		return true;
	}

	private Map<String, Map<String, BaggageInfo>> retrieveFromDiskReplication() {
		Map<String, Map<String, BaggageInfo>> result = null;

		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					new File("baggage.cache")));
			result = (Map<String, Map<String, BaggageInfo>>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			result = new HashMap<String, Map<String, BaggageInfo>>();
			_log.warn("Failed to retrieve data from disk replication. " + e);
		}

		return result;

	}

	private boolean dumpToDiskReplication(
			Map<String, Map<String, BaggageInfo>> replication) {
		File _old = new File("baggage.cache.old");
		File _new = new File("baggage.cache");
		try {
			if (_new.exists())
				_new.renameTo(_old);
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(_new));
			oos.writeObject(replication);
			oos.close();
		} catch (Exception e) {
			_log.warn("Failed to update data to disk replication. " + e);
			if (_old.exists())
				_old.renameTo(_new);
			return false;
		} finally {
			File old = new File("baggage.cache.old");
			if (old.exists())
				old.delete();
		}

		return true;
	}

	// test speed
	public static void main(String[] args) {
		System.out.println("before get baggage storage");
		Long start = System.currentTimeMillis();
		BaggageStorage bs = StorageFactory.getBaggageStorage();
		// System.out.println("before baggage initialize");
		// bs.initialize();
		System.out.println("after baggage initialize");
		Long end = System.currentTimeMillis();
		System.out.println(end - start);
	}
}

// retrieve all baggages for a flight
class CrawlBaggageThread implements Runnable {
	String _flightId;
	Map<String, BaggageInfo> _baggage;
	BaggageQuerier _querier;

	public CrawlBaggageThread(String flightId,
			Map<String, BaggageInfo> baggage, BaggageQuerier querier) {
		_flightId = flightId;
		_baggage = baggage;
		_querier = querier;
	}

	@Override
	public void run() {
		BaggageInfo[] biList = this._querier.getBaggageList(_flightId);
		for (BaggageInfo bi : biList)
			_baggage.put(bi.getNumber(), bi);
	}
}

// retrieve different baggages for a flight
class RetrieveBaggageThread implements Runnable {
	String _flightId;
	Map<String, BaggageInfo> _baggage;
	BaggageQuerier _querier;

	public RetrieveBaggageThread(String flightId,
			Map<String, BaggageInfo> baggage, BaggageQuerier querier) {
		_flightId = flightId;
		_baggage = baggage;
		_querier = querier;
	}

	@Override
	public void run() {
		// generate query index
		Map<String, Long> baggageIndex = new HashMap<String, Long>();
		for (BaggageInfo bi : _baggage.values())
			// TODO, add count
			baggageIndex.put(bi.getNumber(), 1L);

		// query
		Map<String, BaggageInfo> queryResult = this._querier
				.getDifferentBaggageMap(_flightId, baggageIndex);

		// delete
		Collection<String> toDelete = CollectionUtils.subtract(
				this._baggage.keySet(), queryResult.keySet());
		for (String key : toDelete)
			this._baggage.remove(key);

		// insert and update
		for (String key : queryResult.keySet()) {
			BaggageInfo bi = queryResult.get(key);
			if (bi != null)
				this._baggage.put(key, bi);
		}
	}
}