package com.greenorbs.tagassist.device;

public interface IReaderExt extends IHardware {

	void setIP(String ip);

	String getIP();

	void setPort(int port);

	int getPort();

	void setIdentifyListener(IdentifyListener listener);

	IdentifyListener getIdentifyListener();
}
