package com.greenorbs.tagassist.device.simulation;

import java.util.Date;
import java.text.SimpleDateFormat;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IReader;
import com.greenorbs.tagassist.device.Observation;
import com.greenorbs.tagassist.device.ObservationReport;
import com.greenorbs.tagassist.device.ReadPointNotFoundException;
import com.greenorbs.tagassist.device.beiyang.AbstractBeiyangReader;

public class SimulatedReader extends AbstractBeiyangReader implements IReader {
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	
	
	public static void main(String[] args) throws HardwareException{
		SimulatedReader sr=new SimulatedReader();
		sr.start();
	}
	
	public SimulatedReader(){
		
	}
	
	@Override
	public void startup() throws HardwareException {
		super.startup();
		this.start();
	}

	@Override
	public ObservationReport identify(String[] antennas) throws HardwareException,
			ReadPointNotFoundException {
		
		ObservationReport report = new ObservationReport();
		
		Observation observation = new Observation("B0001092723FFF45EFDD8DFF", "", 0, 0, 0);
		report.add(observation);
		
		return report;
	}

	@Override
	public void run() {
		while (true) {
			try {
				ObservationReport report = this.identify(null);
				this.fireIdentifyEvent(report);
				Thread.sleep(1000);
				System.out.println(df.format(new Date()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
