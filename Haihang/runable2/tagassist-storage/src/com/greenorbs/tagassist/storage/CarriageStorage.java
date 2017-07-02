package com.greenorbs.tagassist.storage;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.CarriageInfo;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.util.querier.CarriageQuerier;

public class CarriageStorage extends AbstractStorage<CarriageInfo> {

	public CarriageInfo findByCarriageId(String id) {
		// TODO Auto-generated method stub

		return null;
	}

	public CarriageInfo findByCarriageNumber(String carriageNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<CarriageInfo> getAllCarriages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<CarriageInfo> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMessage(AbstractMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void initSubscriber() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Collection<CarriageInfo> crawl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean addWithoutFireEvent(CarriageInfo item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean removeWithoutFireEvent(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

}
