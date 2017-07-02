package com.greenorbs.tagassist.visualproxy;

import java.util.HashSet;

import org.apache.log4j.xml.DOMConfigurator;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.message.VisualProxyMessages.VSyncRequest;
import com.greenorbs.tagassist.messagebus.util.AbstractHardware;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class VisualProxy extends AbstractHardware {
	
	public static void main(String[] args) {
		DOMConfigurator.configure("log4j.xml");
		
		VisualProxy.instance().start();
	}
	
	private static VisualProxy _instance;
	private boolean _started;
	private HashSet<BusMessageHandler> _messageHandlers;
	
	public VisualProxy() {
		super();
		
		_started = false;
		
		_messageHandlers = new HashSet<BusMessageHandler>();
		this.addBusMessageHandler(new DeviceAdapter());
		this.addBusMessageHandler(new FlightAdapter());
		this.addBusMessageHandler(new BaggageAdapter());
		this.addBusMessageHandler(new NotificationAdapter());
	}
	
	public static VisualProxy instance() {
		// Double-checked locking
		if (null == _instance) {
			synchronized (VisualProxy.class) {
				if (null == _instance) {
					_instance = new VisualProxy();
				}
			}
		}
		return _instance;
	}
	
	private void addBusMessageHandler(BusMessageHandler handler) {
		handler.setPublisher(_publisher);
		handler.setSubscriber(_subscriber);
		_messageHandlers.add(handler);
	}
	
	public void start() {
		if (!_started) {
			_internalStart();
		}
	}

	private void _internalStart() {
		try {
			_logger.info("Visual proxy is starting up.");
			
			super.startup();
			
			for (BusMessageHandler handler : _messageHandlers) {
				handler.initialize();
			}
		
			_started = true;
			
			_logger.info("Visual proxy started.");
		} catch (Exception e) {
			_logger.error(e.getMessage());
		}
	}
	
	public void close() {
		if (_started) {
			try {
				_publisher.stop();
				_publisher.close();
				
				_subscriber.stop();
				_subscriber.close();
				
				_started = false;
			} catch (Exception e) {
				_logger.error(e.getMessage());
			}
		}
	}
	
	@Override
	protected void initSubscriber() {
		super.initSubscriber();
		this.subscribe(VSyncRequest.class);
	}
	
	@Override
	protected MessageBase handleMessage(MessageBase message) {
		if (message instanceof VSyncRequest) {
			for (BusMessageHandler handler : _messageHandlers) {
				if (handler instanceof ISyncRequestHandler) {
					((ISyncRequestHandler) handler).sync();
				}
			}
			return null;
		} else {
			return super.handleMessage(message);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (_started) {
			this.close();
		}
		super.finalize();
	}
	
	@Override
	public void startup() throws HardwareException {
		this.start();
	}

	@Override
	public int getStatus() {
		return IHardware.STATUS_ON;
	}

	@Override
	public int getComponent() {
		return Identifiable.COMPONENT_VISUAL_PROXY;
	}

	@Override
	public String getUUID() {
		return VisualProxyConfiguration.uuid();
	}

	@Override
	public String getName() {
		return VisualProxyConfiguration.name();
	}

	@Override
	public Result rename(String name) {
		return VisualProxyConfiguration.name(name);
	}
	
	@Override
	public String getSoftwareVersion() {
		// TODO Auto-generated method stub
		return "3.0.0-alpha";
	}

}
