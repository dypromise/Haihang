package com.greenorbs.tagassist.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext _context;

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		_context = context;

	}

	public static ApplicationContext getContext() {
		return _context;
	}

	public static void publish(ApplicationEvent event) {
		_context.publishEvent(event);
	}

}
