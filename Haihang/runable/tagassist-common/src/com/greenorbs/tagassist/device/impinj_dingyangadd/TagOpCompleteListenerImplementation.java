package com.greenorbs.tagassist.device.impinj_dingyangadd;

import java.util.List;

import org.apache.log4j.Logger;
import org.llrp.ltk.generated.custom.parameters.ImpinjPeakRSSI;
import org.llrp.ltk.generated.custom.parameters.ImpinjRFDopplerFrequency;
import org.llrp.ltk.generated.custom.parameters.ImpinjRFPhaseAngle;
import org.llrp.ltk.generated.parameters.Custom;
import org.llrp.ltk.generated.parameters.EPC_96;
import org.llrp.ltk.generated.parameters.TagReportData;

import com.greenorbs.tagassist.device.IdentifyListener;
import com.greenorbs.tagassist.device.Observation;
import com.greenorbs.tagassist.device.ObservationReport;
import com.greenorbs.tagassist.device.impinj.ImpinJReader;
import com.impinj.octane.*;

public class TagOpCompleteListenerImplementation implements TagOpCompleteListener {

	private ObservationReport _report = new ObservationReport();
	protected static Logger _logger = Logger.getLogger(ImpinJReader.class);
	protected IdentifyListener _identifyListener;
	public int count = 0;

	public void setIndentifyListener(IdentifyListener identifyListener) {
		this._identifyListener = identifyListener;
	}

	public void onTagOpComplete(ImpinjReader reader, TagOpReport results) {
		System.out.println("TagOpComplete: ");	
		this._report.clear();
		for (TagOpResult t : results.getResults()) {

			Observation obs = new Observation();

			String epc_ = t.getTag().getEpc().toHexString();
			_logger.info("EPC: " + epc_);
			obs.setEPC(t.getTag().getEpc().toHexString());

			if (t instanceof TagReadOpResult) {
				TagReadOpResult tr = (TagReadOpResult) t;
				_logger.info("READ:  id: " + tr.getOpId()+" sequence: " 
				+ tr.getSequenceId()+"	result: " + tr.getResult().toString());
				if (tr.getResult() == ReadResultStatus.Success) {
					String data_ = tr.getData().toHexString();
					obs.setData(data_);
					_logger.info("data: " + data_ );
				}
			}
			// if (tag.getAntennaID() != null) {
			// obs.setReadPoint(tag.getAntennaID().getAntennaID()
			// .toString());
			// }
			//
			// if (tag.getPeakRSSI() != null) {
			// obs.setRssi(tag.getPeakRSSI().getPeakRSSI().intValue());
			// }
			//
			// if (tag.getChannelIndex() != null) {
			// obs.setChannelIndex(tag.getChannelIndex().getChannelIndex()
			// .intValue());
			// }
			//
			// if (tag.getFirstSeenTimestampUTC() != null) {
			// obs.setTimeStamp(System.currentTimeMillis());
			// }
			//
			// if (tag.getTagSeenCount() != null) {
			// obs.setCount(tag.getTagSeenCount().getTagCount().intValue());
			// }
			// List<Custom> clist = tag.getCustomList();
			//
			// for (Custom cd : clist) {
			// if (cd.getClass() == ImpinjRFPhaseAngle.class) {
			// obs.setPhaseAngle(((ImpinjRFPhaseAngle) cd)
			// .getPhaseAngle().toInteger());
			// }
			// if (cd.getClass() == ImpinjPeakRSSI.class) {
			// obs.setPeekRssi(((ImpinjPeakRSSI) cd).getRSSI()
			// .intValue());
			// }
			//
			// if (cd.getClass() == ImpinjRFDopplerFrequency.class) {
			// obs.setDopperFrequency(((ImpinjRFDopplerFrequency) cd)
			// .getDopplerFrequency().intValue());
			// }
			//
			// }

			this._report.add(obs);
			_logger.info(obs);
			

			
		}
		
		if (this._identifyListener != null) {
			
			try {
				
				this._identifyListener.identifyPerformed(_report);
			} catch (Exception e) {
				e.printStackTrace();
				_logger.error("fail to revoke the IdentifyListener", e);
			}
		}
	}

}
