package com.greenorbs.tagassist.datacenter.manager;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.greenorbs.tagassist.CarriageInfo;
import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.datacenter.util.HibernateUtils;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.CarriageRegister;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.CarriageUnregister;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.CarriageUpdate;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.CarriageRegistered;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.CarriageUnregistered;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.CarriageUpdated;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.Confirmation;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class CarriageManager extends BusMessageHandler {

	@Override
	protected void initSubscriber() {
		/* Administrator commands */
		this.subscribe(CarriageRegister.class);
		this.subscribe(CarriageUpdate.class);
		this.subscribe(CarriageUnregister.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof CarriageRegister) {
			
			CarriageRegister m = (CarriageRegister) message;
			CarriageInfo carriageInfo = m.getCarriageInfo();
			Result result = this.registerCarriage(carriageInfo);
			
			reply = new Confirmation(m.getMessageId(), result.getCode(), result.getDescription());
			
			if (result.getCode() == Result.CODE_SUCCESS) {
				this.publish(new CarriageRegistered(carriageInfo));
			}
			
		} else if (message instanceof CarriageUpdate) {
			
			CarriageUpdate m = (CarriageUpdate) message;
			CarriageInfo carriageInfo = m.getCarriageInfo();
			Result result = this.updateCarriage(carriageInfo);
			
			reply = new Confirmation(m.getMessageId(), result.getCode(), result.getDescription());
			
			if (result.getCode() == Result.CODE_SUCCESS) {
				this.publish(new CarriageUpdated(carriageInfo));
			}
			
		} else if (message instanceof CarriageUnregister) {
			
			CarriageUnregister m = (CarriageUnregister) message;
			String carriageId = m.getCarriageId();
			Result result = this.unregisterCarriage(carriageId);
			
			reply = new Confirmation(m.getMessageId(), result.getCode(), result.getDescription());
			
			if (result.getCode() == Result.CODE_SUCCESS) {
				this.publish(new CarriageUnregistered(carriageId));
			}
			
		}
		
		return reply;
	}
	
	private Result registerCarriage(CarriageInfo carriageInfo) {
		if (null == carriageInfo || StringUtils.isEmpty(carriageInfo.getUUID())) {
			return new Result(Result.CODE_ERROR, "carriageInfo is null, or _uuid is empty");
		}
		
		Result result = new Result(Result.CODE_FAILURE);
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			carriageInfo.setRegistTime((new Date()).getTime());
			session.save(carriageInfo);
			result.setCode(Result.CODE_SUCCESS);

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

	private Result updateCarriage(CarriageInfo carriageInfo) {
		if (null == carriageInfo || StringUtils.isEmpty(carriageInfo.getUUID())) {
			return new Result(Result.CODE_ERROR, "carriageInfo is null, or _uuid is empty");
		}
		
		Result result = new Result(Result.CODE_FAILURE);
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			session.update(carriageInfo);
			result.setCode(Result.CODE_SUCCESS);
			
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

	private Result unregisterCarriage(String carriageUUID) {
		if (StringUtils.isEmpty(carriageUUID)) {
			return new Result(Result.CODE_ERROR, "_uuid is empty");
		}
		
		Result result = new Result(Result.CODE_FAILURE);
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			session.createQuery("delete CarriageInfo where uuid='" + 
					carriageUUID + "'").executeUpdate();
			result.setCode(Result.CODE_SUCCESS);
			
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

}
