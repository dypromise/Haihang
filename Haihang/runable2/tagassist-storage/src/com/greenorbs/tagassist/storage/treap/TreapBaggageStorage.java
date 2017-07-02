package com.greenorbs.tagassist.storage.treap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.CompositeMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.epc.EPC96;
import com.greenorbs.tagassist.epc.EPCException;
import com.greenorbs.tagassist.storage.BaggageStorage;
import com.greenorbs.tagassist.util.EPC96Utils;

public class TreapBaggageStorage extends BaggageStorage {

	protected static final String PREFIX = "B";

	private Collection<BaggageInfo> getAllBaggages() {

		Map<String, BaggageInfo> baggageMaps = TreapDB.instance().prefix(
				PREFIX, TreapDB.instance().length(), null, true);

		return baggageMaps.values();

	}

	@Override
	public synchronized Iterator<BaggageInfo> iterator() {

		return this.getAllBaggages().iterator();
	}

	@Override
	public synchronized int size() {
		return this.getAllBaggages().size();
	}

	@Override
	public synchronized boolean isEmpty() {
		return this.getAllBaggages().isEmpty();
	}

	@Override
	public synchronized boolean contains(Object o) {
		return this.getAllBaggages().contains(o);
	}

	@Override
	public synchronized Object[] toArray() {
		return this.getAllBaggages().toArray();
	}

	@Override
	public synchronized <T> T[] toArray(T[] a) {
		return this.getAllBaggages().toArray(a);
	}

	@Override
	protected boolean addWithoutFireEvent(BaggageInfo e) {

		if(e==null){
			return false;
		}
		
		if (e == null || StringUtils.isBlank(e.getFlightId())
				|| StringUtils.isBlank(e.getNumber())) {
			return false;
		}

		TreapDB.instance().put(
				PREFIX + "$" + e.getFlightId() + "$" + e.getNumber(), e);

		return true;
	}

	@Override
	protected boolean removeWithoutFireEvent(Object o) {

		if (o == null || (o instanceof BaggageInfo) == false) {
			return false;
		}

		BaggageInfo bag = (BaggageInfo) o;

		if (StringUtils.isBlank(bag.getFlightId())
				|| StringUtils.isBlank(bag.getNumber())) {
			return false;
		}

		TreapDB.instance().delete(
				PREFIX + "$" + bag.getFlightId() + "$" + bag.getNumber());

		return true;
	}

	@Override
	protected boolean updateWithoutFireEvent(BaggageInfo baggage) {

		return this.addWithoutFireEvent(baggage);
	}

	@Override
	public synchronized boolean containsAll(Collection<?> c) {

		return this.getAllBaggages().containsAll(c);
	}

	@Override
	public synchronized BaggageInfo findByNumber(String number) {

		Iterator<BaggageInfo> it = this.getAllBaggages().iterator();
		while (it.hasNext()) {
			BaggageInfo bag = it.next();
			if (bag.getNumber().equals(number)) {
				return bag;
			}
		}

		return null;

	}

	@Override
	public synchronized BaggageInfo findByEPC(String epcString) {
		EPC96 epc;
		try {
			epc = new EPC96(epcString);
			String number = EPC96Utils.parseBaggageNumber(epc);
			return findByNumber(number);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("cannot parse the epc");
		}

		return null;

	}

	@Override
	public synchronized BaggageInfo findByEPC(String flightId, String EPC) {

		try {
			String number = EPC96Utils.parseBaggageNumber(new EPC96(EPC));

			return (BaggageInfo) TreapDB.instance().get(
					PREFIX + "$" + flightId + "$" + number);

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("It fails to find baggage by EPC due to error parameter of EPC.");
		}
		return null;

	}

	@Override
	public synchronized List<BaggageInfo> findByFlightId(String flightId) {

		return new ArrayList<BaggageInfo>(TreapDB
				.instance()
				.prefix(PREFIX + "$" + flightId + "$",
						TreapDB.instance().length(), null, true).values());

	}

	@Override
	public synchronized List<BaggageInfo> findByStatus(String flightId,
			int status) {

		List<BaggageInfo> result = this.findByFlightId(flightId);

		Iterator<BaggageInfo> it = result.iterator();
		while (it.hasNext()) {
			BaggageInfo bag = it.next();
			if (bag.getStatus() != null && bag.getStatus() != status) {
				it.remove();
			}
		}
		return result;
	}

	@Override
	public synchronized boolean removeByFlightId(String flightId) {
		return TreapDB.instance().removePrefix(PREFIX + "$" + flightId + "$");
	}

	public static void main(String[] args) {

		TreapBaggageStorage storage = new TreapBaggageStorage();
		for (int i = 0; i < 100; i++) {
			BaggageInfo bag = new BaggageInfo();
			bag.setNumber(String.valueOf(i));
			bag.setFlightId(String.valueOf(i));
			storage.add(bag);
		}

		System.out.println(storage.findByFlightId("1"));
	}
}
