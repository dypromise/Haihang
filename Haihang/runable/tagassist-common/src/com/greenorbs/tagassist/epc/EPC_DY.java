package com.greenorbs.tagassist.epc;

import java.text.ParseException;

import com.greenorbs.tagassist.util.EPC96Utils;

public class EPC_DY extends EPC {
	private String epc_string;


	public EPC_DY(String hex) throws EPCException {
		super(hex);
		epc_string = hex;
	
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getRaw() {
		// TODO Auto-generated method stub
		return epc_string;
	}

	@Override
	protected int sizeInHex() {
		// TODO Auto-generated method stub
		return 24;
	}

	// dingyang add
	public String getBaggageNumber() {

		StringBuffer baggagenumber = new StringBuffer();
		try {
			for (int i = 3; i < epc_string.length() - 2; i += 2) {
				baggagenumber.append(epc_string.charAt(i));
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("EPC_DY getting baggagenumber ERROR");
		}
		return baggagenumber.toString();

	}
}
