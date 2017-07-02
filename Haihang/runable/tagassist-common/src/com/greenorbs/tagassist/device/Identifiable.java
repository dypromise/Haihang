package com.greenorbs.tagassist.device;

import com.greenorbs.tagassist.Result;

public interface Identifiable {
	
	/* These constants indicating the component type */
	public static final int COMPONENT_FLIGHT_PROXY 		= 1;
	public static final int COMPONENT_DATA_CENTER 		= 2;
	public static final int COMPONENT_WRISTBAND_PROXY 	= 3;
	public static final int COMPONENT_ADMINISTRATOR 	= 4;
	public static final int COMPONENT_TRACK_TUNNEL 		= 5;
	public static final int COMPONENT_CHECK_TUNNEL 		= 6;
	public static final int COMPONENT_MOBILE_READER 	= 7;
	public static final int COMPONENT_VISUALIZATION 	= 8;
	public static final int COMPONENT_SINK 				= 9;
	public static final int COMPONENT_AODB_PROXY		= 10;
	public static final int COMPONENT_VISUAL_PROXY		= 11;

	/* These constants indicating the facility type */
	public static final int FACILITY_SORTPOOL = 101;
	public static final int FACILITY_CARRIAGE = 102;
	
	public int getComponent();

	public String getUUID();
	
	public String getName();
	
	public Result rename(String name);
	
	public String getSoftwareVersion();
	
}
