package com.greenorbs.tagassist.device.simulation;

import org.apache.log4j.xml.DOMConfigurator;

import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IReader;
import com.greenorbs.tagassist.device.Observation;
import com.greenorbs.tagassist.device.ObservationReport;
import com.greenorbs.tagassist.device.ReadPointNotFoundException;
import com.greenorbs.tagassist.device.beiyang.AbstractBeiyangReader;

public class SimulatedReader extends AbstractBeiyangReader implements IReader {
	/**
	 * ¶¡Ñï¸Ä
	 * @param string
	 * @param i
	 */
	public SimulatedReader(String string, int i) {
		// TODO Auto-generated constructor stub
		super(string,i);
	}
	/**
	 * ¶¡Ñî¸Ä
	 * @param args
	 * @throws HardwareException
	 */
	public static void main(String[] args) throws HardwareException{
		//´ýÌí¼Ó
		DOMConfigurator.configure("log4j.xml");
		SimulatedReader simulatedReader = new SimulatedReader("1",1);
		simulatedReader.startup();
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
		System.out.println("simureader Tread run.");
		while (true) {
			try {
				System.out.println("jinru try.");
				ObservationReport report = this.identify(null);
				System.out.println(report);
				
				this.fireIdentifyEvent(report);
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
