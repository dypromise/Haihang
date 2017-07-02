/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 15, 2012
 */

package com.greenorbs.tagassist.messagebus.util.querier;

import org.apache.log4j.Logger;

import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.io.Publisher;

public class BusQuerier {
	
	protected static Logger _logger = Logger.getLogger(BusQuerier.class);

	protected Publisher _publisher;
	protected static final int MAX_TRIES = 3;
	
	protected BusQuerier(Publisher publisher) {
		_publisher = publisher;
	}
	
	protected AbstractMessage queryOne(AbstractMessage message, long timeout) {
		AbstractMessage reply = null;
		
		try {
			for (int n = 1; null == reply && n <= MAX_TRIES; n++) {
				if (n > 1) {
					_logger.info("Query timed out. Will try again: " + message);
				}
				reply = _publisher.queryOne(message, timeout * n);
				_logger.info("Query sent: " + message);
			}
		} catch (MessageBusException e) {
			e.printStackTrace();
			_logger.error(e);
		}
		
		if (reply != null) {
			_logger.info("Reply received: " + reply);
		} else {
			_logger.warn(String.format("Query failed after %s tries. Will give up.", MAX_TRIES));
		}
		
		return reply;
	}
	
	protected AbstractMessage queryOne(AbstractMessage message) {
		return this.queryOne(message, _publisher.getDefaultQueryTimeout());
	}
	
}
