package com.greenorbs.tagassist.test;

import org.junit.Before;
import org.junit.Test;

import com.greenorbs.tagassist.ObjectCache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ObjectCacheTest {

	private ObjectCache<Object> cache;

	@Before
	public void setup() {
		this.cache = new ObjectCache<Object>(3);
	}

	@Test
	public void perf() throws InterruptedException {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			this.cache.put("" + i, new Integer(i));
		}
		long end = System.currentTimeMillis() - start;
		System.out.println("Putting took: " + end + "ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			assertEquals(new Integer(i), this.cache.get("" + i, Integer.class));
		}
		end = System.currentTimeMillis() - start;
		System.out.println("Getting took: " + end + "ms");
	}

	@Test
	public void putAndGet() throws InterruptedException {
		this.cache.put("one", 1);
		assertEquals(1, this.cache.get("one", Integer.class).intValue());
		this.cache.put("hurz", "hurz");
		assertEquals("hurz", this.cache.get("hurz", String.class));
		this.cache.put("long", new Long(2));
		assertEquals(2, this.cache.get("long", Long.class).longValue());

		// overwrite
		this.cache.put("test", 1);
		this.cache.put("test", 2);
		assertEquals(2, this.cache.get("test", Integer.class).intValue());
	}

	@Test
	public void expire() throws InterruptedException {
		this.cache.put("test", "test-string");
		assertEquals("test-string", this.cache.get("test", String.class));
		// wait until this object is expired
		Thread.sleep(this.cache.getDefaultExpire() * 1010);
		assertNull(this.cache.get("test", String.class));

		// check if a custom time works
		assertTrue("Expiration time should be greater than or equal to 2", this.cache.getDefaultExpire() >= 2);
		this.cache.put("test", "test-string", this.cache.getDefaultExpire() / 2);
		assertEquals("test-string", this.cache.get("test", String.class));
		Thread.sleep((this.cache.getDefaultExpire() / 2) * 1100);
		assertNull(this.cache.get("test", String.class));
	}

	@Test
	public void typeSafe() {
		final ObjectCache<Integer> intCache = new ObjectCache<Integer>();
		intCache.put("one", 1);
		assertEquals(new Integer(1), intCache.get("one"));

		// this doesn't work
		// intCache.get("one", String.class);

		// this seems to be okay during compile time, but ...
		final ObjectCache<Object> objectCache = new ObjectCache<Object>();
		objectCache.put("int", 1);
		try {
			@SuppressWarnings("unused")
			final String str = objectCache.get("int", String.class);
			fail("Should throw exception");
		} catch (ClassCastException ignore) {
		}
	}
}