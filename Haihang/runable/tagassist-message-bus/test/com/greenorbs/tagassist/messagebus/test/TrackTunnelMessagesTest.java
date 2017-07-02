package com.greenorbs.tagassist.messagebus.test;

import org.junit.Test;

import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.BaggageTracked;

public class TrackTunnelMessagesTest {

	@Test
	public void test() throws MessageBusException {
		Publisher publisher = new Publisher();
		publisher.start();
		
		BaggageTracked tracked = new BaggageTracked();
		//tracked.setEPC("B0880483342FFF45EFD247FF");
		tracked.setEPC("B3880533374FFF45EFD247FF");
		publisher.publish(tracked);
		
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tracked.setEPC("B3880684909FFF45EFDE55FF");
		publisher.publish(tracked);
		
//		BaggageTrackingLost lost = new BaggageTrackingLost();
//		lost.setEpc("B0880483342FFF45EFD247FF");
//		publisher.publish(lost);
	}

}
