package com.greenorbs.tagassist.visualproxy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.message.VisualProxyMessages.VFlightRemoved;
import com.greenorbs.tagassist.messagebus.message.VisualProxyMessages.VFlightUpdated;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;
import com.greenorbs.tagassist.storage.DefaultQueryResult;
import com.greenorbs.tagassist.storage.query.CountResult;
import com.greenorbs.tagassist.storage.query.IQuery;
import com.greenorbs.tagassist.storage.query.IQueryResult;
import com.greenorbs.tagassist.storage.query.StorageAddEvent;
import com.greenorbs.tagassist.storage.query.StorageFactory;
import com.greenorbs.tagassist.storage.query.StorageRemoveEvent;
import com.greenorbs.tagassist.storage.query.StorageUpdateEvent;

public class FlightAdapter extends BusMessageHandler implements ISyncRequestHandler {
	
	private class FlightUpdatedListener implements PropertyChangeListener {
		
		private FlightInfo _flightInfo;
		
		public FlightUpdatedListener(FlightInfo flightInfo) {
			_flightInfo = flightInfo;
		}

		@Override
		public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
			FlightAdapter.this.handleFlightUpdated(_flightInfo, false);
		}
		
	}
	
	private HashSet<String> _monitoredProperties;
	private IQueryResult<FlightInfo> _result;
	
	public FlightAdapter() {
		_monitoredProperties = new HashSet<String>();
		_monitoredProperties.add(FlightInfo.PROPERTY_UNSORTED_BAGGAGE_SIZE);
		_monitoredProperties.add(FlightInfo.PROPERTY_SORTED_BAGGAGE_SIZE);
		_monitoredProperties.add(FlightInfo.PROPERTY_MISSING_BAGGAGE_SIZE);
	}
	
	@Override
	public void initialize() {
		super.initialize();
		this.sync();
	}
	
	@Override
	public void sync() {
		this._result = new DefaultQueryResult<FlightInfo>() {

			@Override
			public void itemAdded(StorageAddEvent<FlightInfo> event) {
				super.itemAdded(event);
				
				FlightInfo flightInfo = event.getItem();
				FlightAdapter.this.handleFlightUpdated(flightInfo, true);
			}

			@Override
			public void itemUpdated(StorageUpdateEvent<FlightInfo> event) {
				super.itemUpdated(event);
				
				String propertyName = event.getPropertyName();
				if (_monitoredProperties.contains(propertyName)) {
					FlightInfo flightInfo = event.getItem();
					FlightAdapter.this.handleFlightUpdated(flightInfo, false);
				}
			}

			@Override
			public void itemRemoved(StorageRemoveEvent<FlightInfo> event) {
				super.itemRemoved(event);
				
				FlightInfo flightInfo = event.getItem();
				FlightAdapter.this.handleFlightRemoved(flightInfo);
			}

		};

		StorageFactory.getFlightStorage().query(new IQuery<FlightInfo>() {
			@Override
			public boolean accept(FlightInfo item) {
				return true;
			}
		}, this._result);
	}
	
	private synchronized void handleFlightUpdated(FlightInfo flightInfo, boolean added) {
		int unsorted = 0, sorted = 0, missing = 0, total = 0;
		
		CountResult<FlightInfo> unsortedCountResult = (CountResult<FlightInfo>) 
				flightInfo.getProperty(FlightInfo.PROPERTY_UNSORTED_BAGGAGE_SIZE);
		if (unsortedCountResult != null) {
			unsorted = (int) unsortedCountResult.getValue();
			if (added) {
				unsortedCountResult.addPropertyChangeListener(CountResult.PROPERTY_VALUE, 
						new FlightUpdatedListener(flightInfo));
			}
		} else if (!added) {
			// updated to null -> flight is being removed
			return;
		}
		
		CountResult<FlightInfo> sortedCountResult = (CountResult<FlightInfo>) 
				flightInfo.getProperty(FlightInfo.PROPERTY_SORTED_BAGGAGE_SIZE);
		if (sortedCountResult != null) {
			sorted = (int) sortedCountResult.getValue();
			if (added) {
				sortedCountResult.addPropertyChangeListener(CountResult.PROPERTY_VALUE, 
						new FlightUpdatedListener(flightInfo));
			}
		} else if (!added) {
			// updated to null -> flight is being removed
			return;
		}
		
		CountResult<FlightInfo> missingCountResult = (CountResult<FlightInfo>) 
				flightInfo.getProperty(FlightInfo.PROPERTY_MISSING_BAGGAGE_SIZE);
		if (missingCountResult != null) {
			missing = (int) missingCountResult.getValue();
			if (added) {
				missingCountResult.addPropertyChangeListener(CountResult.PROPERTY_VALUE, 
						new FlightUpdatedListener(flightInfo));
			}
		} else if (!added) {
			// updated to null -> flight is being removed
			return;
		}

		total = unsorted + sorted + missing;
		
		VFlightUpdated vUpdated = new VFlightUpdated();
		vUpdated.setFlightId(flightInfo.getFlightId());
		vUpdated.setUnsorted(unsorted);
		vUpdated.setSorted(sorted);
		vUpdated.setMissing(missing);
		vUpdated.setTotal(total);
		
		this.publish(vUpdated);
	}
	
	private void handleFlightRemoved(FlightInfo flightInfo) {
		String flightId = flightInfo.getFlightId();
		
		VFlightRemoved vRemoved = new VFlightRemoved();
		vRemoved.setFlightId(flightId);
		
		this.publish(vRemoved);
	}

	@Override
	protected void initSubscriber() {
		// do nothing
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		// do nothing
		return null;
	}
	
}
