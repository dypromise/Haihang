/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-4
 */

package com.greenorbs.tagassist.storage.memory;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import junit.framework.Assert;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.storage.BaggageStorage;
import com.greenorbs.tagassist.storage.DefaultQueryResult;
import com.greenorbs.tagassist.storage.IStorage;
import com.greenorbs.tagassist.storage.query.CountResult;
import com.greenorbs.tagassist.storage.query.IQuery;
import com.greenorbs.tagassist.storage.query.IQueryResult;
import com.greenorbs.tagassist.storage.query.StorageAddEvent;
import com.greenorbs.tagassist.storage.query.StorageFactory;
import com.greenorbs.tagassist.storage.query.StorageRemoveEvent;
import com.greenorbs.tagassist.storage.query.StorageUpdateEvent;

public class MemoryBaggageStorageTest2 {

	boolean updated = false;

	static int updateCount;
	static int addCount;
	static int removeCount;

	static {
		// DOMConfigurator.configure("log4j.xml");

	}

	static BaggageStorage storage = new MemoryBaggageStorage();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		for (int i = 0; i < 10; i++) {
			BaggageInfo bag = new BaggageInfo();
			bag.setNumber("n-" + String.valueOf(i));
			bag.setFlightId("id-" + String.valueOf(i));
			storage.add(null, bag);
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAutomaticQueryRelease() {
		for (int i = 0; i < 100; i++) {
			storage.query(new IQuery<BaggageInfo>() {

				@Override
				public boolean accept(BaggageInfo item) {
					return true;
				}

			}, new DefaultQueryResult<BaggageInfo>());
			System.gc();
		}

		assertTrue(storage.querySize() < 100);
	}

	@Test
	public void testAddEvent() {

		DefaultQueryResult<BaggageInfo> result = new DefaultQueryResult<BaggageInfo>() {
			@Override
			public void itemAdded(StorageAddEvent<BaggageInfo> event) {
				addCount++;
				super.itemAdded(event);
			}
		};

		storage.query(new IQuery<BaggageInfo>() {

			@Override
			public boolean accept(BaggageInfo item) {
				return true;
			}

		}, result);

		storage.add(null, new BaggageInfo());

		System.out.println(addCount);
		Assert.assertTrue(addCount == storage.size());

	}

	@Test
	public void testUpdateEvent() {

		DefaultQueryResult<BaggageInfo> result = new DefaultQueryResult<BaggageInfo>() {

			@Override
			public void itemUpdated(StorageUpdateEvent<BaggageInfo> event) {
				updateCount++;
				super.itemUpdated(event);
			}
		};

		storage.query(new IQuery<BaggageInfo>() {

			@Override
			public boolean accept(BaggageInfo item) {
				return true;
			}

		}, result);

		result.get(0).setBClass("BC");

		assertTrue(updateCount == 1);

	}

	@Test
	public void testDeleteEvent() {

		DefaultQueryResult<BaggageInfo> result = new DefaultQueryResult<BaggageInfo>() {

			@Override
			public void itemRemoved(StorageRemoveEvent<BaggageInfo> event) {
				removeCount++;
				super.itemRemoved(event);
			}
		};

		storage.query(new IQuery<BaggageInfo>() {

			@Override
			public boolean accept(BaggageInfo item) {
				return true;
			}

		}, result);

		int size = storage.size();

		BaggageInfo b = result.get(0);

		storage.remove(null, b);

		assertTrue(removeCount == 1);

	}

	@Test
	public void testQuery() {
		DefaultQueryResult<BaggageInfo> result = new DefaultQueryResult<BaggageInfo>();

		storage.query(new IQuery<BaggageInfo>() {

			@Override
			public boolean accept(BaggageInfo item) {
				return true;
			}

		}, result, false);

	}

	
	private Random random = new Random();
	

}
