/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 7, 2012
 */

package com.greenorbs.tagassist.datacenter.manager;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.datacenter.logger.BaggageLogger;
import com.greenorbs.tagassist.datacenter.replier.BaggageReplier;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.epc.EPC96;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.BaggageStatusSet;
import com.greenorbs.tagassist.messagebus.message.CheckTunnelMessages.BaggageCheckedIn;
import com.greenorbs.tagassist.messagebus.message.CheckTunnelMessages.BaggageCheckedOut;
import com.greenorbs.tagassist.messagebus.message.CheckTunnelMessages.BaggageDamaged;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.BaggageStatusChanged;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.BaggageArrival;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.BaggageRemoval;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Confirmation;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.message.MobileReaderMessages.BoardingReport;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.BaggageTracked;
import com.greenorbs.tagassist.messagebus.message.WristbandProxyMessages.BaggageIdentified;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;
import com.greenorbs.tagassist.util.EPC96Utils;

public class BaggageManager extends BusMessageHandler {
	
	protected static Logger _logger = Logger.getLogger(BaggageManager.class);
	
	@Override
	public void initSubscriber() {
		/* Flight proxy messages */ 
		this.subscribe(BaggageArrival.class);
		this.subscribe(BaggageRemoval.class);
		
		/* Administrator commands */
		this.subscribe(BaggageStatusSet.class);
		
		/* Check-tunnel messages */
		this.subscribe(BaggageCheckedIn.class);
		this.subscribe(BaggageCheckedOut.class);
		this.subscribe(BaggageDamaged.class);
		
		/* Track-tunnel messages */
		this.subscribe(BaggageTracked.class);
		
		/* Wristband messages */
		this.subscribe(BaggageIdentified.class);
		
		/* Mobile reader messages */
		this.subscribe(BoardingReport.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof BaggageArrival) {
			
			BaggageArrival m = (BaggageArrival) message;
			BaggageInfo baggageInfo = m.getBaggageInfo();
			String source = m.getSource();
			
			this.handleBaggageArrival(baggageInfo, source);
			
		} else if (message instanceof BaggageRemoval) {
			
			BaggageRemoval m = (BaggageRemoval) message;
			BaggageInfo baggageInfo = m.getBaggageInfo();
			String source = m.getSource();
			
			this.handleBaggageRemoval(baggageInfo, source);
			
		} else if (message instanceof BaggageStatusSet) {
			
			BaggageStatusSet m = (BaggageStatusSet) message;
			String baggageNumber = m.getBaggageNumber();
			int status = m.getStatus();
			String source = m.getSource();
			String operator = m.getOperator();
			Result result = this.handleBaggageStatusSet(baggageNumber, status, source, operator);
			
			reply = new Confirmation(m.getMessageId(), result.getCode(), result.getDescription());
			
			if (result.getCode() == Result.CODE_SUCCESS) {
				BaggageStatusChanged changed = new BaggageStatusChanged();
				
				changed.setBaggageNumber(baggageNumber);
				changed.setStatus(status);
				changed.setDevice(source);
				changed.setOperator(operator);
				changed.setTime((new Date()).getTime());
				
				this.publish(changed);
			}
			
		} else if (message instanceof BaggageCheckedIn) {
			
			BaggageCheckedIn m = (BaggageCheckedIn) message;
			String epc = m.getEPC();
			int status = m.getWasCheckedInRight() ? BaggageInfo.STATUS_READ_BY_CHECKTUNNEL_RIGHT :
				BaggageInfo.STATUS_READ_BY_CHECKTUNNEL_WRONG;
			String tracedDevice = m.getSource();
			String operator = m.getOperator();
			
			this.updateBaggageStatus(epc, status, tracedDevice, operator);
			
			BaggageLogger.logBaggageChecked(m);
			
		} else if (message instanceof BaggageCheckedOut) {
			
			BaggageCheckedOut m = (BaggageCheckedOut) message;
			String epc = m.getEPC();
			int status = BaggageInfo.STATUS_IN_POOL;
			String tracedDevice = m.getSource();
			String operator = m.getOperator();
			
			this.updateBaggageStatus(epc, status, tracedDevice, operator);
			
			BaggageLogger.logBaggageChecked(m);
			
		} else if (message instanceof BaggageDamaged) {
			
			BaggageDamaged m = (BaggageDamaged) message;
			String baggageNumber = m.getBaggageNumber();
			int damageCode = m.getDamageCode();
			String source = m.getSource();
			String operator = m.getOperator();
			
			this.handleBaggageDamaged(baggageNumber, damageCode, source, operator);
			
		} else if (message instanceof BaggageTracked) {
			
			BaggageTracked m = (BaggageTracked) message;
			String epc = m.getEPC();
			int status = BaggageInfo.STATUS_IN_POOL;
			String tracedDevice = m.getSource();
			String operator = null;
			
			this.updateBaggageStatus(epc, status, tracedDevice, operator);
			
			BaggageLogger.logBaggageTracked(m);
			
		} else if (message instanceof BaggageIdentified) {
			
			BaggageIdentified m = (BaggageIdentified) message;
			String epc = m.getEPC();
			int status = BaggageInfo.STATUS_READ_BY_WRISTBAND;
			String tracedDevice = m.getSource();
			String operator = m.getOperator();
			
			this.updateBaggageStatus(epc, status, tracedDevice, operator);
			
			BaggageLogger.logBaggageIdentified(m);
			
		} else if (message instanceof BoardingReport) {
			
			this.handleBoardingReport((BoardingReport) message);
			
		}
		
		return reply;
	}

	private void handleBaggageArrival(BaggageInfo baggageInfo, String source) {
		if (null == baggageInfo || StringUtils.isEmpty(baggageInfo.getNumber())) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			BaggageInfo storedBaggage = (BaggageInfo) session.createQuery(
					"from BaggageInfo where number='" + baggageInfo.getNumber() + "'").uniqueResult();
			if (null == storedBaggage) {
				baggageInfo.setStatus(BaggageInfo.STATUS_ARRIVED);
				baggageInfo.setLastTracedDevice(source);
				baggageInfo.setLastTracedTime((new Date()).getTime());
				baggageInfo.setRevision(0);
				session.save(baggageInfo);
			} else if (!storedBaggage.getFlightId().equals(baggageInfo.getFlightId())) {
				_logger.warn(String.format("Overwriting baggage info, " +
						"old: number=%s, flightId=%s; new: number=%s, flightId=%s",
						storedBaggage.getNumber(), storedBaggage.getFlightId(),
						baggageInfo.getNumber(), baggageInfo.getFlightId()));
				
				// FIXME
				storedBaggage.setEPC(baggageInfo.getEPC());
				storedBaggage.setFlightId(baggageInfo.getFlightId());
				storedBaggage.setDestination(baggageInfo.getDestination());
				storedBaggage.setPassenger(baggageInfo.getPassenger());
				storedBaggage.setWeight(baggageInfo.getWeight());
				storedBaggage.setBClass(baggageInfo.getBClass());
				storedBaggage.setDamageCode(baggageInfo.getDamageCode());
				storedBaggage.setStatus(BaggageInfo.STATUS_ARRIVED);
				storedBaggage.setLastTracedDevice(source);
				storedBaggage.setLastOperator(null);
				storedBaggage.setLastTracedTime((new Date()).getTime());
				storedBaggage.setRevision(0);
				session.update(storedBaggage);
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			_logger.error(e);
		} finally {
			session.close();
		}
	}

	private void handleBaggageRemoval(BaggageInfo baggageInfo, String source) {
		if (null == baggageInfo || StringUtils.isEmpty(baggageInfo.getNumber())) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			BaggageInfo storedBaggage = (BaggageInfo) session.createQuery(
					"from BaggageInfo where number='" + baggageInfo.getNumber() + "'").uniqueResult();
			if (storedBaggage != null) {
				storedBaggage.setStatus(BaggageInfo.STATUS_REMOVED);
				storedBaggage.setLastTracedDevice(source);
				storedBaggage.setLastOperator(null);
				storedBaggage.setLastTracedTime((new Date()).getTime());
				storedBaggage.setRevision(storedBaggage.getRevision() + 1);
				session.update(storedBaggage);
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			_logger.error(e);
		} finally {
			session.close();
		}
	}
	
	private Result handleBaggageStatusSet(String baggageNumber, int status, String source, String operator) {
		if (StringUtils.isEmpty(baggageNumber)) {
			return new Result(Result.CODE_ERROR, "baggageNumber is empty");
		}
		
		Result result = new Result(Result.CODE_FAILURE);
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			BaggageInfo storedBaggage = (BaggageInfo) session.createQuery(
					"from BaggageInfo where number='" + baggageNumber + "'").uniqueResult();
			if (storedBaggage != null && storedBaggage.getStatus() != status) {
				storedBaggage.setStatus(status);
				storedBaggage.setLastTracedDevice(source);
				storedBaggage.setLastOperator(operator);
				storedBaggage.setLastTracedTime((new Date()).getTime());
				storedBaggage.setRevision(storedBaggage.getRevision() + 1);
				session.update(storedBaggage);
				
				result.setCode(Result.CODE_SUCCESS);
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			result.setCode(Result.CODE_ERROR);
			result.setDescription(e.getMessage());
			
			e.printStackTrace();
			_logger.error(e);
		} finally {
			session.close();
		}
		
		return result;
	}
	
	private void updateBaggageStatus(String epc, int status, String tracedDevice, String operator) {
		if (StringUtils.isEmpty(epc)) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			BaggageInfo storedBaggage = (BaggageInfo) session.createQuery(
					"from BaggageInfo where epc='" + epc + "'").uniqueResult();
			if (storedBaggage != null && storedBaggage.getStatus() != BaggageInfo.STATUS_REMOVED) {
				storedBaggage.setStatus(status);
				if (tracedDevice != null) {
					storedBaggage.setLastTracedDevice(tracedDevice);
					storedBaggage.setLastOperator(operator);
					storedBaggage.setLastTracedTime((new Date()).getTime());
				}
				storedBaggage.setRevision(storedBaggage.getRevision() + 1);
				session.update(storedBaggage);
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			_logger.error(e);
		} finally {
			session.close();
		}
	}
	
	private void handleBaggageDamaged(String baggageNumber, int damageCode, String source, String operator) {
		if (StringUtils.isEmpty(baggageNumber)) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			BaggageInfo storedBaggage = (BaggageInfo) session.createQuery(
					"from BaggageInfo where number='" + baggageNumber + "'").uniqueResult();
			if (storedBaggage != null) {
				storedBaggage.setDamageCode(damageCode);
				storedBaggage.setLastTracedDevice(source);
				storedBaggage.setLastOperator(operator);
				storedBaggage.setLastTracedTime((new Date()).getTime());
				storedBaggage.setRevision(storedBaggage.getRevision() + 1);
				session.update(storedBaggage);
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			_logger.error(e);
		} finally {
			session.close();
		}
	}

	private void handleBoardingReport(BoardingReport boardingReport) {
		if (null == boardingReport || StringUtils.isEmpty(boardingReport.getFlightId())) {
			return;
		}
		
		// Load expected baggage list from database
		BaggageInfo[] expectedBaggageInfoList = BaggageReplier.getBaggageInfoListByFlightId(
				boardingReport.getFlightId());
		
		// Extract boarded and unexpected baggage list from boarding report
		HashSet<String> boardedBaggageNumberList = new HashSet<String>(Arrays.asList(
				boardingReport.getBoardedBaggageNumberList()));
		
		HashSet<String> unexpectedBaggageNumberList = new HashSet<String>();
		for (String epc : boardingReport.getUnexpectedBaggageEPCList()) {
			try {
				String baggageNumber = EPC96Utils.parseBaggageNumber(new EPC96(epc));
				unexpectedBaggageNumberList.add(baggageNumber);
			} catch (Exception e) {
				e.printStackTrace();
				_logger.error(e);
			}
		}
		
		// Update database
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			String tracedDevice = boardingReport.getSource();
			String operator = boardingReport.getOperator();
			long tracedTime = (new Date()).getTime();
			
			for (BaggageInfo baggageInfo : expectedBaggageInfoList) {
				if (boardedBaggageNumberList.contains(baggageInfo.getNumber())) {
					baggageInfo.setStatus(BaggageInfo.STATUS_READ_BY_MOBILEREADER_RIGHT);
					baggageInfo.setLastTracedDevice(tracedDevice);
					baggageInfo.setLastOperator(operator);
					baggageInfo.setLastTracedTime(tracedTime);
					baggageInfo.setRevision(baggageInfo.getRevision() + 1);
					session.update(baggageInfo);
				} else if (!baggageInfo.isRemoved()) {
					baggageInfo.setStatus(BaggageInfo.STATUS_MISSING);
					baggageInfo.setLastTracedDevice(tracedDevice);
					baggageInfo.setLastOperator(operator);
					baggageInfo.setLastTracedTime(tracedTime);
					baggageInfo.setRevision(baggageInfo.getRevision() + 1);
					session.update(baggageInfo);
				}
			}
			
			for (String baggageNumber : unexpectedBaggageNumberList) {
				String queryString = "from BaggageInfo where number='" + baggageNumber + "'";
				BaggageInfo baggageInfo = (BaggageInfo) session.createQuery(queryString).uniqueResult();
				if (baggageInfo != null) {
					baggageInfo.setStatus(BaggageInfo.STATUS_READ_BY_MOBILEREADER_WRONG);
					baggageInfo.setLastTracedDevice(tracedDevice);
					baggageInfo.setLastOperator(operator);
					baggageInfo.setLastTracedTime(tracedTime);
					baggageInfo.setRevision(baggageInfo.getRevision() + 1);
					session.update(baggageInfo);
				}
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			_logger.error(e);
		} finally {
			session.close();
		}
	}

	public static void handleSortationTerminated(String flightId, String source) {
//		if (StringUtils.isEmpty(flightId)) {
//			return;
//		}
//		
//		Session session = HibernateUtils.getSessionFactory().openSession();
//		Transaction transaction = null;
//		try {
//			transaction = session.beginTransaction();
//			
//			String queryString = String.format("update BaggageInfo set status=%s where flightId='%s' and status!=%s",
//					BaggageInfo.STATUS_MISSING, flightId, BaggageInfo.STATUS_READ_BY_CHECKTUNNEL_RIGHT);
//			session.createQuery(queryString).executeUpdate();
//			
//			transaction.commit();
//		} catch (Exception e) {
//			if (transaction != null) {
//				transaction.rollback();
//			}
//			
//			e.printStackTrace();
//			_logger.error(e);
//		} finally {
//			session.close();
//		}
//		
//		/* Refresh the associated flight's baggage statistics. */
//		FlightManager.refreshBaggageStatistics(flightId);
	}
	
	public static void handleFlightTerminated(String flightId, String source) {
		if (StringUtils.isEmpty(flightId)) {
			return;
		}
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			String queryString = String.format(
					"update BaggageInfo set status=%s, lastTracedDevice='%s', lastTracedTime=%s" +
					" where flightId='%s' and status=%s",
					BaggageInfo.STATUS_BOARDED, source, (new Date()).getTime(), 
					flightId, BaggageInfo.STATUS_READ_BY_MOBILEREADER_RIGHT);
			session.createQuery(queryString).executeUpdate();
			
			queryString = String.format(
					"update BaggageInfo set status=%s, lastTracedDevice='%s', lastTracedTime=%s" +
					" where flightId='%s' and status!=%s and status!=%s",
					BaggageInfo.STATUS_MISSING, source, (new Date()).getTime(),
					flightId, BaggageInfo.STATUS_BOARDED, BaggageInfo.STATUS_REMOVED);
			session.createQuery(queryString).executeUpdate();
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			_logger.error(e);
		} finally {
			session.close();
		}
	}
	
}
