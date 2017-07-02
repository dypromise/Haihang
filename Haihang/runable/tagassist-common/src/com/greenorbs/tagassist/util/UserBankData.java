package com.greenorbs.tagassist.util;

public class UserBankData {
	private String flight_code;
	private String flight_number;
	private String takeoff_date;
	private String takeoff_city;
	private String desination_city;
	private String passeger_name;

	public UserBankData(String data) {
		parsing_data(data);
	}

	public String getFlightcode() {
		return this.flight_code;
	}

	public String getFlightnumber() {
		return this.flight_number;
	}

	public String getTakeoffdate() {
		return this.takeoff_date;
	}

	public String getTakeoffcity() {
		return this.takeoff_city;
	}

	public String getDestoantioncity() {
		return this.desination_city;
	}

	public String getpassengername() {
		return this.passeger_name;
	}

	private void parsing_data(String data) {
		StringBuffer stringBuffer = new StringBuffer();
		String aaa = data.substring(8, 10);
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(aaa));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(10, 12)));
		this.flight_code = stringBuffer.toString();
		stringBuffer.setLength(0);

		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(16, 18)));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(18, 20)));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(20, 22)));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(22, 24)));
		this.flight_number = stringBuffer.toString();
		stringBuffer.setLength(0);

		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(28, 30)));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(30, 32)));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(32, 34)));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(34, 36)));
		this.takeoff_date = stringBuffer.toString();
		stringBuffer.setLength(0);

		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(40, 42)));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(42, 44)));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(44, 46)));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(46, 48)));
		this.takeoff_city = stringBuffer.toString();
		stringBuffer.setLength(0);

		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(52, 54)));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(54, 56)));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(56, 58)));
		stringBuffer.append(com.greenorbs.tagassist.util.ASCII_table.ASCII_table.get(data.substring(58, 60)));
		this.desination_city = stringBuffer.toString();
		stringBuffer.setLength(0);

	}

}
