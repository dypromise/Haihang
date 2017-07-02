package com.greenorbs.tagassist.visualproxy;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.messagebus.message.CheckTunnelMessages.BaggageCheckedIn;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.BaggageRemoval;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.BaggageTracked;
import com.greenorbs.tagassist.messagebus.message.VisualProxyMessages.VBaggageRemoved;
import com.greenorbs.tagassist.messagebus.message.VisualProxyMessages.VBaggageTracked;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;
import com.greenorbs.tagassist.storage.query.StorageFactory;

public class BaggageAdapter extends BusMessageHandler {
	
	@Override
	protected void initSubscriber() {
		this.subscribe(BaggageTracked.class);
		this.subscribe(BaggageCheckedIn.class);
		this.subscribe(BaggageRemoval.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {

		if (message instanceof BaggageTracked) {
			
			BaggageTracked m = (BaggageTracked) message;
			BaggageInfo baggageInfo = this.getBaggageInfo(m.getEPC());
			
			if (baggageInfo != null) {
//			if (true ) {
				VBaggageTracked vBaggageTracked = new VBaggageTracked();
				vBaggageTracked.setEPC(m.getEPC());
				vBaggageTracked.setFlightId(baggageInfo.getFlightId());
				vBaggageTracked.setDistance(m.getDistance());
				
				this.publish(vBaggageTracked);
				
				
			}
			
		} else if (message instanceof BaggageCheckedIn) {
			
			BaggageCheckedIn m = (BaggageCheckedIn) message;
			BaggageInfo baggageInfo = this.getBaggageInfo(m.getEPC());
			
			if (baggageInfo != null) {
				VBaggageRemoved vRemoved = new VBaggageRemoved();
				vRemoved.setEPC(m.getEPC());
				vRemoved.setFlightId(baggageInfo.getFlightId());
				
				this.publish(vRemoved);
				_logger.info("Message published, "
						+ "This massage is transvated from BaggageTracked from class TrackTunnel. " + vRemoved);
			}
			
		} else if (message instanceof BaggageRemoval) {
			
			BaggageRemoval m = (BaggageRemoval) message;
			BaggageInfo baggageInfo = m.getBaggageInfo();
			
			if (baggageInfo != null) {
				VBaggageRemoved vRemoved = new VBaggageRemoved();
				vRemoved.setEPC(baggageInfo.getEPC());
				vRemoved.setFlightId(baggageInfo.getFlightId());
				
				this.publish(vRemoved);
				_logger.info("Message published, "
						+ "This massage is transvated from BaggageTracked from class TrackTunnel. " + vRemoved);
			}
			
		}
		
		return null;
	}
	
	private BaggageInfo getBaggageInfo(String epc) {
		return StorageFactory.getBaggageStorage().findByEPC(epc);
	}

}
