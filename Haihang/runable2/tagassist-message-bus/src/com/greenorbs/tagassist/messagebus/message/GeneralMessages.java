/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 6, 2012
 */

package com.greenorbs.tagassist.messagebus.message;

import com.greenorbs.tagassist.Result;

public class GeneralMessages {
	
	public static class Heartbeat extends MessageBase {}
	
	/**
	 * For use of either
	 *  
	 * 1) passively replying to a HardwareInfoGet message; or
	 * 
	 * 2) passively publishing one's info when a HardwareCollect message is received; or
	 * 
	 * 3) actively publishing one's info when appropriate (e.g., started up, info changed).
	 *
	 */
	public static class HardwareInfoReport extends MessageBase {
		
		private int _status;
		
		private String _softwareVersion;
		
		private String _hostname;
		
		private String _macAddress;
		
		private String _ipAddress;
		
		public HardwareInfoReport() {}
		
		public int getStatus() {
			return _status;
		}

		public void setStatus(int status) {
			_status = status;
		}

		public String getSoftwareVersion() {
			return _softwareVersion;
		}

		public void setSoftwareVersion(String softwareVersion) {
			_softwareVersion = softwareVersion;
		}

		public String getHostname() {
			return _hostname;
		}

		public void setHostname(String hostname) {
			_hostname = hostname;
		}

		public String getMacAddress() {
			return _macAddress;
		}

		public void setMacAddress(String macAddress) {
			_macAddress = macAddress;
		}

		public String getIpAddress() {
			return _ipAddress;
		}

		public void setIpAddress(String ipAddress) {
			_ipAddress = ipAddress;
		}
		
	}

	/**
	 * @param confirmTo 	the UUID of the message to confirm to
	 * @param result 		either RESULT_SUCCESS, RESULT_FAILURE, RESULT_ERROR or RESULT_UNKNOWN
	 * @param description 	description of the result (if not successful)
	 */
	public static class Confirmation extends MessageBase {
		
		public static final int RESULT_UNKNOWN	= Result.CODE_UNKNOWN;
		public static final int RESULT_SUCCESS	= Result.CODE_SUCCESS;
		public static final int RESULT_FAILURE	= Result.CODE_FAILURE;
		public static final int RESULT_ERROR	= Result.CODE_ERROR;
		
		private String _confirmTo;
		
		private int _result;
		
		private String _description;
		
		public Confirmation() {}
		
		public Confirmation(String confirmTo, int result) {
			this(confirmTo, result, null);
		}
		
		public Confirmation(String confirmTo, int result, String description) {
			_confirmTo = confirmTo;
			_result = result;
			_description = description; 
		}

		public String getConfirmTo() {
			return _confirmTo;
		}

		public void setConfirmTo(String confirmTo) {
			_confirmTo = confirmTo;
		}
		
		public int getResult() {
			return _result;
		}

		public void setResult(int result) {
			_result = result;
		}

		public String getDescription() {
			return _description;
		}

		public void setDescription(String description) {
			_description = description;
		}
		
	}
	
}
