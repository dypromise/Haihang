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
				VBaggageTracked vTracked = new VBaggageTracked();
				vTracked.setEPC(m.getEPC());
				vTracked.setFlightId(baggageInfo.getFlightId());
				vTracked.setDistance(m.getDistance());
				
				this.publish(vTracked);
			}
			
		} else if (message instanceof BaggageCheckedIn) {
			
			BaggageCheckedIn m = (BaggageCheckedIn) message;
			BaggageInfo baggageInfo = this.getBaggageInfo(m.getEPC());
			
			if (baggageInfo != null) {
				VBaggageRemoved vRemoved = new VBaggageRemoved();
				vRemoved.setEPC(m.getEPC());
				vRemoved.setFlightId(baggageInfo.getFlightId());
				
				this.publish(vRemoved);
			}
			
		} else if (message instanceof BaggageRemoval) {
			
			BaggageRemoval m = (BaggageRemoval) message;
			BaggageInfo baggageInfo = m.getBaggageInfo();
			
			if (baggageInfo != null) {
				VBaggageRemoved vRemoved = new VBaggageRemoved();
				vRemoved.setEPC(baggageInfo.getEPC());
				vRemoved.setFlightId(baggageInfo.getFlightId());
				
				this.publish(vRemoved);
			}
		}
		
		return null;
	}
	
	private BaggageInfo getBaggageInfo(String epc) {
		return StorageFactory.getBaggageStorage().findByEPC(epc);
	}

}
