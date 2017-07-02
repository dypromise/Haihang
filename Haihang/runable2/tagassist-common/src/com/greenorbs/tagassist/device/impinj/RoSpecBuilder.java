package com.greenorbs.tagassist.device.impinj;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.llrp.ltk.generated.enumerations.AISpecStopTriggerType;
import org.llrp.ltk.generated.enumerations.AirProtocols;
import org.llrp.ltk.generated.enumerations.ROReportTriggerType;
import org.llrp.ltk.generated.enumerations.ROSpecStartTriggerType;
import org.llrp.ltk.generated.enumerations.ROSpecState;
import org.llrp.ltk.generated.enumerations.ROSpecStopTriggerType;
import org.llrp.ltk.generated.interfaces.AirProtocolInventoryCommandSettings;
import org.llrp.ltk.generated.interfaces.SpecParameter;
import org.llrp.ltk.generated.parameters.AISpec;
import org.llrp.ltk.generated.parameters.AISpecStopTrigger;
import org.llrp.ltk.generated.parameters.AntennaConfiguration;
import org.llrp.ltk.generated.parameters.C1G2InventoryCommand;
import org.llrp.ltk.generated.parameters.C1G2RFControl;
import org.llrp.ltk.generated.parameters.C1G2SingulationControl;
import org.llrp.ltk.generated.parameters.InventoryParameterSpec;
import org.llrp.ltk.generated.parameters.RFTransmitter;
import org.llrp.ltk.generated.parameters.ROBoundarySpec;
import org.llrp.ltk.generated.parameters.ROReportSpec;
import org.llrp.ltk.generated.parameters.ROSpec;
import org.llrp.ltk.generated.parameters.ROSpecStartTrigger;
import org.llrp.ltk.generated.parameters.ROSpecStopTrigger;
import org.llrp.ltk.generated.parameters.TagReportContentSelector;
import org.llrp.ltk.types.Bit;
import org.llrp.ltk.types.TwoBitField;
import org.llrp.ltk.types.UnsignedByte;
import org.llrp.ltk.types.UnsignedInteger;
import org.llrp.ltk.types.UnsignedShort;
import org.llrp.ltk.types.UnsignedShortArray;

public class RoSpecBuilder {

	static Logger log = Logger.getLogger(RoSpecBuilder.class);

	protected ROSpec _roSpec;
	
	public static final int POWER91 = 91;
	public static final int POWER87 = 87;
	public static final int POWER61 = 61;
	public static final int POWER41 = 41;
	public static final int POWER35 = 35;

	public RoSpecBuilder(int roSpecId) {
		log.debug("Building default ROSpec.");

		_roSpec = new ROSpec();

		// Create a Reader Operation Spec (ROSpec).
		_roSpec.setPriority(new UnsignedByte(0));
		_roSpec.setCurrentState(new ROSpecState(ROSpecState.Disabled));
		_roSpec.setROSpecID(new UnsignedInteger(roSpecId));

		// Set up the ROBoundarySpec
		// This defines the start and stop triggers.
		ROBoundarySpec roBoundarySpec = new ROBoundarySpec();

		// Set the start trigger to null.
		// This means the ROSpec will start as soon as it is enabled.
		ROSpecStartTrigger startTrig = new ROSpecStartTrigger();
		// startTrig.setROSpecStartTriggerType(new ROSpecStartTriggerType(
		// ROSpecStartTriggerType.Null));
		startTrig.setROSpecStartTriggerType(new ROSpecStartTriggerType(
				ROSpecStartTriggerType.Immediate));
		roBoundarySpec.setROSpecStartTrigger(startTrig);

		// Set the stop trigger is null. This means the ROSpec
		// will keep running until an STOP_ROSPEC message is sent.
		ROSpecStopTrigger stopTrig = new ROSpecStopTrigger();
		stopTrig.setDurationTriggerValue(new UnsignedInteger(0));
		stopTrig.setROSpecStopTriggerType(new ROSpecStopTriggerType(
				ROSpecStopTriggerType.Null));
		roBoundarySpec.setROSpecStopTrigger(stopTrig);

		_roSpec.setROBoundarySpec(roBoundarySpec);

		// Add an Antenna Inventory Spec (AISpec).
		AISpec aispec = new AISpec();

		// Set the AI stop trigger to null. This means that
		// the AI spec will run until the ROSpec stops.
		AISpecStopTrigger aiStopTrigger = new AISpecStopTrigger();
		aiStopTrigger.setAISpecStopTriggerType(new AISpecStopTriggerType(
				AISpecStopTriggerType.Null));
		aiStopTrigger.setDurationTrigger(new UnsignedInteger(0));
		aispec.setAISpecStopTrigger(aiStopTrigger);
		// AntennaConfiguration antennaConfig = new AntennaConfiguration();
		// RFReceiver rfReceiver = new RFReceiver();
		// rfReceiver.setReceiverSensitivity(-70);
		// antennaConfig.setRFReceiver(rFReceiver)

		// Select which antenna ports we want to use.
		// Setting this property to zero means all antenna ports.
		UnsignedShortArray antennaIDs = new UnsignedShortArray();
		antennaIDs.add(new UnsignedShort(0));

		aispec.setAntennaIDs(antennaIDs);

		// Tell the reader that we're reading Gen2 tags.
		InventoryParameterSpec inventoryParam = new InventoryParameterSpec();
		inventoryParam.setProtocolID(new AirProtocols(
				AirProtocols.EPCGlobalClass1Gen2));
		inventoryParam.setInventoryParameterSpecID(new UnsignedShort(1234));

		AntennaConfiguration antennaConfig = new AntennaConfiguration();
		antennaConfig.setAntennaID(new UnsignedShort(0));
		RFTransmitter rfTransmit = new RFTransmitter();
		rfTransmit.setChannelIndex(new UnsignedShort(1));
		rfTransmit.setHopTableID(new UnsignedShort(0));
		rfTransmit.setTransmitPower(new UnsignedShort(91));
		antennaConfig.setRFTransmitter(rfTransmit);
		C1G2InventoryCommand command = new C1G2InventoryCommand();
		command.setTagInventoryStateAware(new Bit(0));
		C1G2RFControl control = new C1G2RFControl();
		control.setModeIndex(new UnsignedShort(0));
		control.setTari(new UnsignedShort(0));
		command.setC1G2RFControl(control);
		C1G2SingulationControl singulationControl = new C1G2SingulationControl();
		singulationControl.setSession(new TwoBitField(new Bit[] { new Bit(1),
				new Bit(0) }));
		singulationControl.setTagPopulation(new UnsignedShort(32));
		singulationControl.setTagTransitTime(new UnsignedInteger(0));
		command.setC1G2SingulationControl(singulationControl);
		antennaConfig.setAirProtocolInventoryCommandSettingsList(Arrays
				.asList((AirProtocolInventoryCommandSettings) command));

		inventoryParam.addToAntennaConfigurationList(antennaConfig);

		aispec.addToInventoryParameterSpecList(inventoryParam);

		_roSpec.addToSpecParameterList(aispec);

		// Specify what type of tag reports we want
		// to receive and when we want to receive them.
		ROReportSpec roReportSpec = new ROReportSpec();
		// Receive a report every time a tag is read.
		roReportSpec.setROReportTrigger(new ROReportTriggerType(
				ROReportTriggerType.Upon_N_Tags_Or_End_Of_ROSpec));
		roReportSpec.setN(new UnsignedShort(1));
		TagReportContentSelector reportContent = new TagReportContentSelector();
		// Select which fields we want in the report.
		reportContent.setEnableAccessSpecID(new Bit(1));
		reportContent.setEnableAntennaID(new Bit(1));
		reportContent.setEnableChannelIndex(new Bit(1));
		reportContent.setEnableFirstSeenTimestamp(new Bit(1));
		reportContent.setEnableInventoryParameterSpecID(new Bit(1));
		reportContent.setEnableLastSeenTimestamp(new Bit(1));
		reportContent.setEnablePeakRSSI(new Bit(1));
		reportContent.setEnableROSpecID(new Bit(1));
		reportContent.setEnableSpecIndex(new Bit(1));
		reportContent.setEnableTagSeenCount(new Bit(1));
		roReportSpec.setTagReportContentSelector(reportContent);
		_roSpec.setROReportSpec(roReportSpec);

	}

	public RoSpecBuilder enableReportAll() {
		this.enableReportAccessSpecId(true);
		this.enableReportAntennaId(true);
		this.enableReportChannelIndex(true);
		this.enableReportFirstSeenTimestamp(true);
		this.enableReportInventoryParameterSpecId(true);
		this.enableReportLastSeenTimestamp(true);
		this.enableReportPeakRSS(true);
		this.enableReportRoSpecId(true);
		this.enableReportSpecIndex(true);
		this.enableReportTagSeenCount(true);
		return this;
	}

	public RoSpecBuilder enableReportInventoryParameterSpecId(boolean b) {
		this._roSpec.getROReportSpec().getTagReportContentSelector()
				.setEnableInventoryParameterSpecID(new Bit(b ? 1 : 0));
		return this;
	}

	public RoSpecBuilder enableReportRoSpecId(boolean b) {

		this._roSpec.getROReportSpec().getTagReportContentSelector()
				.setEnableROSpecID(new Bit(b ? 1 : 0));
		return this;
	}

	public RoSpecBuilder enableReportSpecIndex(boolean b) {
		this._roSpec.getROReportSpec().getTagReportContentSelector()
				.setEnableSpecIndex(new Bit(b ? 1 : 0));
		return this;
	}

	public RoSpecBuilder enableReportAccessSpecId(boolean b) {
		this._roSpec.getROReportSpec().getTagReportContentSelector()
				.setEnableAccessSpecID(new Bit(b ? 1 : 0));
		return this;
	}

	public RoSpecBuilder enableReportAntennaId(boolean b) {
		this._roSpec.getROReportSpec().getTagReportContentSelector()
				.setEnableAntennaID(new Bit(b ? 1 : 0));
		return this;
	}

	public RoSpecBuilder enableReportChannelIndex(boolean b) {
		this._roSpec.getROReportSpec().getTagReportContentSelector()
				.setEnableChannelIndex(new Bit(b ? 1 : 0));
		return this;
	}

	public RoSpecBuilder enableReportFirstSeenTimestamp(boolean b) {
		this._roSpec.getROReportSpec().getTagReportContentSelector()
				.setEnableFirstSeenTimestamp(new Bit(b ? 1 : 0));
		return this;
	}

	public RoSpecBuilder enableReportLastSeenTimestamp(boolean b) {
		this._roSpec.getROReportSpec().getTagReportContentSelector()
				.setEnableLastSeenTimestamp(new Bit(b ? 1 : 0));
		return this;
	}

	public RoSpecBuilder enableReportPeakRSS(boolean b) {
		this._roSpec.getROReportSpec().getTagReportContentSelector()
				.setEnablePeakRSSI(new Bit(b ? 1 : 0));
		return this;
	}

	public RoSpecBuilder enableReportTagSeenCount(boolean b) {
		this._roSpec.getROReportSpec().getTagReportContentSelector()
				.setEnableTagSeenCount(new Bit(b ? 1 : 0));
		return this;
	}

	public RoSpecBuilder buildPower(int power) {

		for (SpecParameter param : this._roSpec.getSpecParameterList()) {
			if (param instanceof AISpec) {
				AISpec aiSpec = (AISpec) param;
				for (InventoryParameterSpec iparam : aiSpec
						.getInventoryParameterSpecList()) {
					for (AntennaConfiguration param3 : iparam
							.getAntennaConfigurationList()) {
						AntennaConfiguration antennaConfig = (AntennaConfiguration) param3;
						antennaConfig.getRFTransmitter().setTransmitPower(
								new UnsignedShort(power));

					}
				}

			}
		}

		return this;
	}
	
//	public RoSpecBuilder buildImpinjPeekRSSI(){
//		this._roSpec.a
//	}

	public ROSpec toRoSpec() {
		return this._roSpec;
	}

}
