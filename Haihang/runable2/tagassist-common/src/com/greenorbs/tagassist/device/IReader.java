/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Haoxiang Liu, 2012-2-21
 */


package com.greenorbs.tagassist.device;

public interface IReader extends IHardware{
	
	public void setIP(String ip);
	
	public String getIP();
	
	public void setPort(int port);
	
	public int getPort();

	/**
	 * Get the power of the read point
	 * @param readpoint
	 * @return
	 */
	public int getReadPointPower(String readpoint)throws HardwareException, ReadPointNotFoundException;
	/**
	 * Set the power of the antenna
	 * @param readpoint
	 * @param value
	 * @return
	 */
	public boolean setReadPointPower(String readpoint, int value)throws HardwareException, ReadPointNotFoundException;
	
	/**
	 * Get all names of the antennas.
	 * @return
	 */
	public String[] getReadPoints()throws HardwareException;

	/**
	 * Actively identify the tags
	 * @param antennas
	 * @return
	 */
	public ObservationReport identify(String[] antennas)throws HardwareException, ReadPointNotFoundException;
	
	/**
	 * Add a identify listeners to the reader
	 * @param listener
	 */
	public void addIdentifyListener(IdentifyListener listener);
	
	/**
	 * Remove a identify listeners from the reader
	 * @param listener
	 */
	
	public void removeIdentifyListener(IdentifyListener listener);

}
