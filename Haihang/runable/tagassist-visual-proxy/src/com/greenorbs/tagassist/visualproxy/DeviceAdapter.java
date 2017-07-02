package com.greenorbs.tagassist.visualproxy;

import com.greenorbs.tagassist.TrackTunnelInfo;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Heartbeat;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.message.VisualProxyMessages.VTrackTunnelHeartbeat;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class DeviceAdapter extends BusMessageHandler {

	@Override
	protected void initSubscriber() {
		this.subscribe(Heartbeat.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {

		if (message instanceof Heartbeat) {
			
			Heartbeat m = (Heartbeat) message;
			
			if (m.getComponent() == Identifiable.COMPONENT_TRACK_TUNNEL) {
				VTrackTunnelHeartbeat vHeartbeat = new VTrackTunnelHeartbeat();
				vHeartbeat.setTrackTunnelId(m.getSource());
				
				if (m.getCustom() != null && m.getCustom() instanceof TrackTunnelInfo) {
					TrackTunnelInfo trackTunnelInfo = (TrackTunnelInfo) m.getCustom();
					vHeartbeat.setOwnerPool(trackTunnelInfo.getLocationParam1());
					vHeartbeat.setDistance(trackTunnelInfo.getLocationParam2());
				}
				
				this.publish(vHeartbeat);
			}
			
		}
		
		return null;
	}

}
