package com.greenorbs.tagassist.storage.treapdb;

import static org.junit.Assert.*;

import java.util.Random;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.storage.BaggageStorage;
import com.greenorbs.tagassist.storage.DefaultQueryResult;
import com.greenorbs.tagassist.storage.query.CountResult;
import com.greenorbs.tagassist.storage.query.IQuery;
import com.greenorbs.tagassist.storage.query.StorageAddEvent;
import com.greenorbs.tagassist.storage.query.StorageRemoveEvent;
import com.greenorbs.tagassist.storage.query.StorageUpdateEvent;
import com.greenorbs.tagassist.storage.treap.TreapBaggageStorage;
import com.greenorbs.tagassist.storage.treap.TreapDB;
import com.greenorbs.tagassist.storage.treap.disk.DiskTreap;

public class TreapBaggageStorageTest {

	static BaggageStorage storage = new TreapBaggageStorage();

	static final String FLIGHT = "CP161/20NOV/YVR/Y";

	static final int COUNT = 1000;

	static BaggageInfo target = new BaggageInfo();

	static {
		target.setNumber(validNumber(500));
		target.setFlightId(FLIGHT);
	}

	private static String validNumber(int num) {
		String number = String.valueOf(num);
		while (number.length() < 10) {
			number = number + "0";
		}
		return number;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		try {
			for (int i = 0; i < COUNT; i++) {
				BaggageInfo bag = new BaggageInfo();
				bag.setNumber(validNumber(i));
				bag.setFlightId(FLIGHT);
				bag.setStatus(0);

				storage.add(bag);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testFindByNumber() {

		try {
			TreapDB.dump();
			
			BaggageInfo bag = storage.findByNumber(validNumber(500));
			assertNotNull(bag);
			assertEquals(bag.getFlightId(), FLIGHT);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	@Test
	public void testFindByEPCStringString() {

	}

	@Test
	public void testFindByFlightId() {

		assertNotNull(storage.findByFlightId(target.getFlightId()));

	}

	@Test
	public void testFindByStatus() {

		System.out.println(storage.findByStatus(FLIGHT, 0).size());
		assertTrue(storage.findByStatus(FLIGHT, 0).size() == storage.size());
	}

	int addCount = 0;
	int removeCount = 0;
	int updateCount = 0;

	@Test
	public void testQueryIQueryOfEIQueryResultOfE() {

		storage.query(new IQuery<BaggageInfo>() {

			@Override
			public boolean accept(BaggageInfo item) {
				return true;
			}

		}, new DefaultQueryResult<BaggageInfo>() {

			@Override
			public void itemAdded(StorageAddEvent<BaggageInfo> event) {
				addCount++;
			}

			@Override
			public void itemRemoved(StorageRemoveEvent<BaggageInfo> event) {
				removeCount++;
			}

			@Override
			public void itemUpdated(StorageUpdateEvent<BaggageInfo> event) {

				updateCount++;

			}

		});

		storage.remove(target);
		assertTrue(removeCount == 1);
		storage.add(target);
		assertTrue(addCount == storage.size()+1);
		target.setNumber("12345");
		assertTrue(updateCount == 1);
	}

	@Test
	public void testQueryIQueryOfEIQueryResultOfEBoolean() {

		storage.query(new IQuery<BaggageInfo>() {

			@Override
			public boolean accept(BaggageInfo item) {
				return true;
			}

		}, new DefaultQueryResult<BaggageInfo>() {

			@Override
			public void itemAdded(StorageAddEvent<BaggageInfo> event) {
				addCount++;
			}

			@Override
			public void itemRemoved(StorageRemoveEvent<BaggageInfo> event) {
				removeCount++;
			}

		}, false);

		int count = addCount;
		storage.remove(target);
		Assert.assertEquals(count, addCount);
		storage.add(target);
		Assert.assertEquals(count, addCount);

	}

	@Test
	public void testCountIQueryOfE() {
		CountResult<BaggageInfo> result = storage
				.count(new IQuery<BaggageInfo>() {

					@Override
					public boolean accept(BaggageInfo item) {
						return true;
					}

				});

		assertEquals(result.getValue(), storage.size());
	}

	@Test
	public void testContainsQuery() {
		
		IQuery<BaggageInfo> query = new IQuery<BaggageInfo>(){

			@Override
			public boolean accept(BaggageInfo item) {
				return true;
			}	
		};
		
		DefaultQueryResult<BaggageInfo> result = new DefaultQueryResult<BaggageInfo>();
		
		storage.query(query, result,true);
		
		assertTrue(storage.containsQuery(query));
		
		query = null;
		
		System.gc();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		
		System.gc();
		
		assertFalse(storage.containsQuery(query));
		
	}

	@Test
	public void testQuerySize() {
		
	}

	@Test
	public void testAddE() {
	}

	@Test
	public void testAddObjectE() {
		
	}

	@Test
	public void testAddAllCollectionOfQextendsE() {
		
	}

	@Test
	public void testAddAllObjectCollectionOfQextendsE() {
		
	}

	@Test
	public void testRemoveObject() {
	}

	@Test
	public void testRemoveObjectObject() {
	}

	@Test
	public void testRemoveAllCollectionOfQ() {
	}

	@Test
	public void testRemoveAllObjectCollectionOfQ() {
	}

	@Test
	public void testClear() {
		
	}

	@Test
	public void testRetainAll() {

	}

}
