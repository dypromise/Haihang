package com.greenorbs.tagassist.webservice.util;

import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.Publisher;

public class BusUtils {
	
	private static Publisher _publisher = null;
	
	public static Publisher getPublisher() {
		// Double-checked locking
		if (null == _publisher) {
			synchronized (Publisher.class) {
				if (null == _publisher) {
					_publisher = new Publisher();
				}
			}
		}
		
		if (!_publisher.isStarted()) {
			try {
				_publisher.start();
			} catch (MessageBusException e) {
				e.printStackTrace();
			}
		}
		
		return _publisher;
	}

}
