package com.greenorbs.tagassist.tracktunnel;
/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Longfei Shangguan, Feb 24, 2012
 */


import java.text.ParseException;
import java.util.Random;
import java.util.Timer;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Test;
import org.springframework.expression.spel.support.ReflectionHelper.ArgsMatchKind;

import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.BaggageTracked;
import com.greenorbs.tagassist.util.EPC96Utils;
import com.greenorbs.tagassist.util.FlightUtils;
public class Simulation {

	Publisher publisher = new Publisher();	
	static Timer timer = new Timer();
	//¶¡Ñî¼Ó
	protected Logger _logger = Logger.getLogger(this.getClass());
	
	/**
	 * ¶¡Ñî¼Ó
	 * @param maxTimeInterval
	 * @return
	 */
	public static void main(String[] args){
		DOMConfigurator.configure("log4j.xml");
		Simulation simulation  = new Simulation();
		simulation.test();
	}
	
	public int achieveTimeInterval ( int maxTimeInterval){
		
		int s = (int) Math.random()*maxTimeInterval*1000;
		System.out.println(s);
		return  s;
	}
	
	
	@Test
	public void test() {
		Random random = new Random();
		
		try {
			publisher.start();
			//¶¡Ñî¸Ä JMSException
		} catch ( MessageBusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		String[] number_prefix={"8181818181", "1818181818"};
		String[] flightid = {"CP161/20NOV2012", "NW028S/20NOV2012"};
		int[] track_distance = {140, 1333, 3140};
		String[] track_uuid = {"8ef11e48-a635-4655-94a6-85c0c1295250", 
				"28ef1782-b5d7-4027-842c-69fa39fd093c", "6b9c6e4e-595a-4337-8875-26d351861f19"};
		*/
		String[] number_prefix={"3880738237","0851357742","3880517152","3880617496","0079817402","3880617509"};
		String[] flightid = {"HU7147/01AUG2016","HU7975/01AUG2016","HU7189/01AUG2016","HU7245/01AUG2016", "HU489/01AUG2016","HU7771/01AUG2016"};
		int[] track_distance = {100 , 2000, 4000};
		String[] track_uuid = {"8ef11e48-a635-4655-94a6-85c0c1295250", 
				"28ef1782-b5d7-4027-842c-69fa39fd093c", "6b9c6e4e-595a-4337-8875-26d351861f19"};
		
//		//single
//		while(true){
//			BaggageTracked baggageTracked = new BaggageTracked();
//
//			int track_choice = random.nextInt(3);
//			try {
//				baggageTracked.setEPC(EPC96Utils.hexEncode(number_prefix[0]+"119", 
//						FlightUtils.parseFlightCode(flightid[0])));
//				baggageTracked.setDistance(track_distance[track_choice]);
//				baggageTracked.setSource(track_uuid[track_choice]);
//				publisher.publish(baggageTracked);
//				
//
//				Thread.sleep(30000);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
		
		//all
		while(true){
			BaggageTracked baggageTracked = new BaggageTracked();
			
			int flight_choice = random.nextInt(6);
			int track_choice = random.nextInt(3);
			
			//cp161
			int number_int = random.nextInt(250);
			String number_str = number_prefix[flight_choice];
			//¶¡Ñî¸Ä
			/*if(number_int < 10)
				number_str += "00"+number_int;
			else if(number_int < 100)
				number_str += "0"+number_int;
			else 
				number_str += number_int;
			*/
			try {
				baggageTracked.setEPC(EPC96Utils.hexEncode(number_str, FlightUtils.parseFlightCode(flightid[flight_choice])));
				baggageTracked.setDistance(track_distance[track_choice]);
				baggageTracked.setSource(track_uuid[track_choice]);
				publisher.publish(baggageTracked);
				_logger.info("Message Published£º "+ baggageTracked);

				Thread.sleep(random.nextInt(1000)+2000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
//
//		//baggage track
//		BaggageInfo baggage = new BaggageInfo();
//		baggage.setNumber("0018234501001");
//		baggage.setFlightId("CP161/20NOV/YVR/Y");		
//		baggage.setEPC("B0018234501001319FC587FF");
//		baggage.setStatus(BaggageInfo.STATUS_IN_POOL);
//
//		BaggageTracked baggageTracked = new BaggageTracked();
//		
//		try {
//			baggageTracked.setEPC(baggage.getEPC());
//			baggageTracked.setDistance(1333);
//			baggageTracked.setSource("28ef1782-b5d7-4027-842c-69fa39fd093c");
//			publisher.publish(baggageTracked);
//		} catch (JMSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		for (int i = 0; i < 250; i++) {
//			BaggageInfo bag = new BaggageInfo();
//			bag.setFlightId("NW028S/20NOV2012");
//			bag.setBClass("A");
//			bag.setDamageCode(0);
//			StringBuffer sb = new StringBuffer();
//			sb.append("1818181818");
//			if(i < 10)
//				sb.append("00"+i);
//			else if(i < 100)
//				sb.append("0"+i);
//			else
//				sb.append(i);
//
//			bag.setNumber(sb.toString());
//			bag.setStatus(BaggageInfo.STATUS_IN_POOL);
//			try {
//				bag.setEPC(EPC96Utils.hexEncode(bag.getNumber(),
//						FlightUtils.parseFlightCode(bag.getFlightId())));
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			bag.setPassenger("Young");
//		}
//		
//		 FlightInfo f = new FlightInfo();
//		 f.setFlightId("HTR124/14JAN/YVR/Y");
//		 f.setMissingBaggageSize(10);
//		 f.setUnsortedBaggageSize(5);
//		 f.setSortedBaggageSize(20);
//		 f.setDepartTime(new Date().getTime());
//		 f.setStatus(FlightInfo.STATUS_CHECKING_IN);
//		 FlightScheduled message = new FlightScheduled();
//		 message.setFlightInfo(f);
//		 try {
//			publisher.publish(message);
//		} catch (MessageBusException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
		
//		BaggageInfo baggageOne = new BaggageInfo();
//		BaggageInfo baggageTwo = new BaggageInfo();
//		BaggageInfo baggageThree = new BaggageInfo();
//		
//		baggageOne.setEPC("0018234567001319FC587FF0");
//		baggageOne.setFlightId("CP161/20NOV/YVR/Y");
//		baggageOne.setStatus(BaggageInfo.STATUS_ARRIVED);
//		baggageOne.setNumber("0018234567001");
//		
//		baggageTwo.setEPC("0018234567002319FC587FF0");
//		baggageTwo.setFlightId("CP161/20NOV/YVR/Y");
//		baggageTwo.setStatus(BaggageInfo.STATUS_ARRIVED);
//		baggageTwo.setNumber("0018234567002");
//		
//		baggageThree.setEPC("0018234567003319FC587FF0");
//		baggageThree.setFlightId("CP161/20NOV/YVR/Y");
//		baggageThree.setStatus(BaggageInfo.STATUS_ARRIVED);
//		baggageThree.setNumber("0018234567003");
		
		
//		float siteOne = 0f;
//		float siteTwo = 1333f;
//		float siteThree = 3102f;
//		float perimeter = 7115.16f;
//		float speed = 100;
//		
//		
//		String initNumber1 = "123123123";
//		String initNumber2 = "0";
//		int number = 111;
//		for ( int i = 0; i < 1; i++ ){
//			BaggageInfo baggage = new BaggageInfo();
//			baggage.setNumber("0018234501001");
//			baggage.setFlightId("CP161/20NOV/YVR/Y");
//			String flightCode=null;
//			try {
//				flightCode = FlightUtils.parseFlightCode("CP161/20NOV/YVR/Y");
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			if ( flightCode == null ){
//				return;
//			}
//			
//			baggage.setEPC("B0018234501001319FC587FF");
//			baggage.setStatus(BaggageInfo.STATUS_IN_POOL);
//			
//			BaggageArrival arrivalMessage = new BaggageArrival();			
//			
//			try {
//				arrivalMessage.setBaggageInfo(baggage);
//				publisher.publish(arrivalMessage);
//			} catch (JMSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}		

//			BaggageTracked baggageTracked = new BaggageTracked();
//			baggage.setStatus(BaggageInfo.STATUS_IN_POOL);
//			
//			try {
//				baggageTracked.setEPC(baggage.getEPC());
//				baggageTracked.setDistance(siteOne);
//			//			baggageTracked.setSource("0e5a195e-3943-46df-a8f6-cfb77dd7ef71");
//				publisher.publish(baggageTracked);
//			} catch (JMSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
////				e.printStackTrace();
//			}
			
		}
		//baggage arrival
//		{	
//			
//			BaggageArrival arrivalMessage = new BaggageArrival();			
//			
//			try {
//				arrivalMessage.setBaggageInfo(baggageOne);
//				publisher.publish(arrivalMessage);
//				System.out.println("baggage one arrival*************************");
//			} catch (JMSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				arrivalMessage.setBaggageInfo(baggageTwo);
//				publisher.publish(arrivalMessage);
//				System.out.println("baggage two arrival**************************");
//			} catch (JMSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			try {
//				arrivalMessage.setBaggageInfo(baggageThree);
//				publisher.publish(arrivalMessage);
//				System.out.println("baggage three arrival**************************");
//			} catch (JMSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		BaggageTracked baggageTracked = new BaggageTracked();
//		baggageOne.setStatus(BaggageInfo.STATUS_IN_POOL);
//		baggageTwo.setStatus(BaggageInfo.STATUS_IN_POOL);
//		baggageThree.setStatus(BaggageInfo.STATUS_IN_POOL);
//		
//		try {
//			baggageTracked.setEPC(baggageTwo.getEPC());
//			baggageTracked.setDistance(siteOne);
////			baggageTracked.setSource("0e5a195e-3943-46df-a8f6-cfb77dd7ef71");
//			publisher.publish(baggageTracked);
//		} catch (JMSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			Thread.sleep(5);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//		}
//		
//		try {
//			baggageTracked.setEPC(baggageThree.getEPC());
//			baggageTracked.setDistance(siteOne);
////			baggageTracked.setSource("0e5a195e-3943-46df-a8f6-cfb77dd7ef71");
//			publisher.publish(baggageTracked);
//		} catch (JMSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			Thread.sleep(6);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//		}
//		
//		while (true){
//			
//			try {
//				baggageTracked.setEPC(baggageOne.getEPC());
//				baggageTracked.setDistance(siteOne);
////				baggageTracked.setSource("0e5a195e-3943-46df-a8f6-cfb77dd7ef71");
//				publisher.publish(baggageTracked);
//			} catch (JMSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				Thread.sleep(achieveTimeInterval(5));
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//			}
//			
//			try {
//				baggageTracked.setEPC(baggageOne.getEPC());
//				baggageTracked.setDistance(siteTwo);
////				baggageTracked.setSource("2561bd99-b818-42c8-9313-fd44cc4b6e42");
//				publisher.publish(baggageTracked);
//			} catch (JMSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				Thread.sleep(7000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//			}
//			
//			try {
//				baggageTracked.setEPC(baggageOne.getEPC());
//				baggageTracked.setDistance(siteThree);
////				baggageTracked.setSource("34dad239-8a0b-4184-aca6-96951c47e378");
//				publisher.publish(baggageTracked);
//			} catch (JMSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//			}
//			
//			
//			
//		}
		
		
//		//baggage tracked
//		{
//			BaggageTracked baggageTracked = new BaggageTracked();
//			baggageOne.setStatus(BaggageInfo.STATUS_IN_POOL);
//			baggageTwo.setStatus(BaggageInfo.STATUS_IN_POOL);
//			baggageThree.setStatus(BaggageInfo.STATUS_IN_POOL);
//			
//			try {
//				baggageTracked.setEPC(baggageOne.getEPC());
//				baggageTracked.setDistance(siteOne);
//				baggageTracked.setSource("0e5a195e-3943-46df-a8f6-cfb77dd7ef71");
//				publisher.publish(baggageTracked);
//			} catch (JMSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//			try {
//				baggageTracked.setEPC(baggageTwo.getEPC());
//				baggageTracked.setDistance(siteOne);
//				baggageTracked.setSource("2561bd99-b818-42c8-9313-fd44cc4b6e42");
//				publisher.publish(baggageTracked);
//			} catch (JMSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//				
//			for ( int i = 0; i < 1000; i++ ){
//				
//				try {
//					baggageThree.setStatus(BaggageInfo.STATUS_IN_POOL);
//					baggageTracked.setDistance(siteOne);
//					baggageTracked.setEPC(baggageThree.getEPC());
//					publisher.publish(baggageTracked);
//				} catch (JMSException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				int duration = (int) ((siteTwo - siteOne)/speed)*1000 + 2000;
//				try {
//					Thread.sleep(duration);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				
//				try {
//					baggageThree.setStatus(BaggageInfo.STATUS_IN_POOL);
//					baggageTracked.setDistance(siteTwo);
//					baggageTracked.setEPC(baggageThree.getEPC());
//					publisher.publish(baggageTracked);
//				} catch (JMSException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				duration = (int) ((siteThree - siteTwo)/speed)*1000 + 3000;
//				try {
//					Thread.sleep(duration);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				
//				try {
//					baggageThree.setStatus(BaggageInfo.STATUS_IN_POOL);
//					baggageTracked.setDistance(siteThree);
//					baggageTracked.setEPC(baggageThree.getEPC());
//					publisher.publish(baggageTracked);
//				} catch (JMSException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				duration = (int) ((perimeter - siteThree + siteOne)/speed)*1000 + 1000;
//				try {
//					Thread.sleep(duration);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				
//			}
//		}
//		
//		}
		

}