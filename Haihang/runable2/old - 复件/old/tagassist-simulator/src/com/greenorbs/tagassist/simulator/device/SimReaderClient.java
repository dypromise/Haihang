package com.greenorbs.tagassist.simulator.device;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IReader;
import com.greenorbs.tagassist.device.IdentifyListener;
import com.greenorbs.tagassist.device.ObservationReport;
import com.greenorbs.tagassist.device.ReadPointNotFoundException;

public class SimReaderClient extends AbstractSimClient implements IReader {

	@Override
	public void startup() throws HardwareException {
		// TODO Auto-generated method stub

	}

	@Override
	public Result shutdown() throws HardwareException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result reset() throws HardwareException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getReadPointPower(String readpoint) throws HardwareException,
			ReadPointNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean setReadPointPower(String readpoint, int value)
			throws HardwareException, ReadPointNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getReadPoints() throws HardwareException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObservationReport identify(String[] antennas)
			throws HardwareException, ReadPointNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addIdentifyListener(IdentifyListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeIdentifyListener(IdentifyListener listener) {
		// TODO Auto-generated method stub

	}

}
