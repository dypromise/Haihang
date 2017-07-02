/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Longfei Shangguan, Feb 6, 2012
 */


import javax.jms.JMSException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.beiyang2.BeiyangInfraredRay;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.HardwareCollect;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.HardwareControl;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.HardwareInfoGet;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.HardwareInfoSet;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.TrackTunnelConfig;
import com.greenorbs.tagassist.tracktunnel.TrackTunnel;

//
public class trackTunnelTest {
	
	public static void main(String[] args) {

		
		Publisher publisher = new Publisher();
		try {
			publisher.start();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//---------------------------------------------------------------------		
		//testing the subscribed messages.
		
		//Discovering the hardwares.
		
		AdministratorMessages.HardwareCollect hc = new AdministratorMessages.HardwareCollect();
		
		AdministratorMessages.TrackTunnelConfig ttc = new AdministratorMessages.TrackTunnelConfig();		
		ttc.setDistance(50);
		ttc.setMessageId("message001");
//		ttc.setPoolID("001");
		ttc.setName("001");
		ttc.setNeedConfirm(true);
		
		//rename the hardware
		AdministratorMessages.HardwareInfoSet hi = new AdministratorMessages.HardwareInfoSet();
		hi.setName("002");
		hi.setNeedConfirm(true);
				
		
		//Puase the hardware
		AdministratorMessages.HardwareControl hcontrol = new AdministratorMessages.HardwareControl();
		hcontrol.setTargetUUID("8ef11e48-a635-4655-94a6-85c0c1295250");
		hcontrol.setCommand(IHardware.COMMAND_PAUSE);
		
		
//		//reset the hardawre
		hcontrol.setCommand(IHardware.COMMAND_RESET);
		
//		//resume the hardware
		hcontrol.setCommand(IHardware.COMMAND_RESUME);
		
		
		//get the status of hardware.
		AdministratorMessages.HardwareInfoGet hg = new AdministratorMessages.HardwareInfoGet();
		hg.setTargetUUID("8ef11e48-a635-4655-94a6-85c0c1295250");
//---------------------------------------------------------------------		
	
		//testing the hardware discovery function.
		try {
			publisher.publish(hc);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//testing the hardware configuration function.
		try {
			publisher.publish(ttc);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				
		//testing the hardware rename function.
		try {
			publisher.publish(hi);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Get the status of the hardware.
		try {
			publisher.publish(hg);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//testing the pausing function.

		try {
			publisher.publish(hcontrol);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//testing the reset function.
		try {
			publisher.publish(hcontrol);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//testing the resume function.
		try {
			hcontrol.setCommand(IHardware.COMMAND_RESUME);
			publisher.publish(hcontrol);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
