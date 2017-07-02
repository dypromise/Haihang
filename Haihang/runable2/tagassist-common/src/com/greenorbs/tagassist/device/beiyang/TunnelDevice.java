package com.greenorbs.tagassist.device.beiyang;

import java.util.concurrent.TimeoutException;

import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.ISerialReader;
import com.greenorbs.tagassist.device.SerialComm;

public class TunnelDevice implements ISerialReader {

	public static final int BLACK = 0;
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	public static final int YELLOW = 4;
	public static final int PURPLE = 5;
	public static final int CYAN = 6;
	public static final int WHITE = 7;

	public static final int YELLOW_ON = 0;
	public static final int YELLOW_OFF = 1;
	public static final int YELLOW_FLASH = 2;
	public static final int RED_ON = 3;
	public static final int RED_OFF = 4;
	public static final int RED_FLASH = 5;

	public static final byte SHOW = 0;
	public static final byte MOVE = 1;
	public static final byte FLASH = 2;
	public static final byte INV_SHOW = 3;
	public static final byte INV_MOVE = 4;
	public static final byte INV_FLASH = 5;
	public static final byte LIE_SHOW = 6;
	public static final byte LIE_MOVE = 7;
	public static final byte LIE_FLASH = 8;

	private boolean[] tempInfraredState = null;

	private static TunnelDevice device;

	private SerialComm comm;
	private Thread current;

	private TunnelDevice() throws HardwareException {
		comm = new SerialComm("COM1", 38400, this, false);
	}

	public static TunnelDevice getInstance() throws HardwareException {
		if (device == null) {
			device = new TunnelDevice();
		}
		return device;
	}

	public boolean[] getInfraredStatus() throws InterruptedException,
			TimeoutException {
		byte ins = (byte) 0x80;
		byte[] data = new byte[0];
		tempInfraredState = null;
		current = Thread.currentThread();
		comm.write(encode(ins, data));
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			if (tempInfraredState == null)
				throw e;
		}
		if (tempInfraredState == null) {
			throw new TimeoutException();
		}
		return tempInfraredState;
	}

	public void setLightBar(int colorCode) {
		byte ins = (byte) 0x81;
		byte[] data = new byte[5];
		data[3] = data[4] = (byte) 0xFF;
		switch (colorCode) {
		case BLACK:
			data[0] = 0;
			data[1] = 0;
			data[2] = 0;
			break;
		case RED:
			data[0] = 1;
			data[1] = 0;
			data[2] = 0;
			break;
		case GREEN:
			data[0] = 0;
			data[1] = 1;
			data[2] = 0;
			break;
		case BLUE:
			data[0] = 0;
			data[1] = 0;
			data[2] = 1;
			break;
		case YELLOW:
			data[0] = 1;
			data[1] = 1;
			data[2] = 0;
			break;
		case PURPLE:
			data[0] = 1;
			data[1] = 0;
			data[2] = 1;
			break;
		case CYAN:
			data[0] = 0;
			data[1] = 1;
			data[2] = 1;
			break;
		case WHITE:
			data[0] = 1;
			data[1] = 1;
			data[2] = 1;
			break;
		default:
			return;
		}
		comm.write(encode(ins, data));
	}

	public void setStatusLight(int statusCode) {
		byte ins = (byte) 0x83;
		byte[] data = new byte[4];
		switch (statusCode) {
		case YELLOW_ON:
			data[0] = 0x11;
			break;
		case YELLOW_OFF:
			data[0] = 0x10;
			break;
		case YELLOW_FLASH:
			data[0] = 0x12;
			break;
		case RED_ON:
			data[1] = 0x11;
			break;
		case RED_OFF:
			data[1] = 0x10;
			break;
		case RED_FLASH:
			data[1] = 0x12;
			break;
		default:
			return;
		}
		comm.write(encode(ins, data));
	}

	public void setText(String str, byte mode) {
		if (mode >= 9)
			return;
		byte[] tmp = str.getBytes();
		byte[] data = new byte[tmp.length + 2];
		data[0] = 0x01;
		data[1] = mode;
		System.arraycopy(tmp, 0, data, 2, tmp.length);
		comm.write(encode((byte) 0x82, data));
	}

	private byte[] encode(byte ins, byte[] message) {
		byte[] data = new byte[message.length + 6];
		data[0] = (byte) (message.length + 6);
		data[1] = (byte) 0xFF;
		data[2] = ins;
		data[3] = (byte) message.length;
		System.arraycopy(message, 0, data, 4, message.length);
		byte x = 0;
		for (int i = 0; i != message.length + 4; ++i)
			x ^= data[i];
		data[message.length + 4] = (byte) ~x;
		data[message.length + 5] = 0x3;
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenorbs.tagassist.device.ISerialReader#process(byte[])
	 * 这个知识处理红外返回的结果，其他的设备不涉及返回值
	 */
	@Override
	public void process(byte[] data) {
		int[] message = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			if (data[i] < 0)
				message[i] = data[i] + 256;
			else
				message[i] = data[i];
		}
		if (!isFault(message[2])) {
			System.out.println("返回有错！");
			return;
		}
		int num = message[3];
		if (num % 7 != 0) {
			System.out.println("数据长度有错！");
			return;
		}
		int[] lightscount = new int[16];
		for (int i = 0; i < num / 7; i++) {
			int id1 = message[4 + i * 7];
			long time = 1;
			for (int k = 0; k < 4; k++) {
				time += message[4 + i * 7 + 3 + k]
						* (long) Math.pow(0xFF, 3 - k);
			}
			// boolean[] led = new boolean[16];
			for (int k = 0; k < 2; k++) {
				int temp = message[4 + 1 + k];
				for (int j = 0; j < 8; j++) {
					if ((temp & (int) Math.pow(2, j)) != 0) {
						lightscount[8 * k + j]++;
					}
				}
			}
		}
		boolean[] led = new boolean[16];
		for (int i = 0; i < 16; i++) {
			if (lightscount[i] > num / 14)
				led[i] = true;
		}
		tempInfraredState = led.clone();
		current.interrupt();
	}

	public boolean isFault(int no) {
		switch (no) {
		case 0x0:
			return true;
		case 0x1:
			System.out.println("发送命令中数据域长度错误");
		case 0x17:
			System.out.println("接收数据校验错误");
		case 0x84:
			System.out.println("参数错误");
		case 0x85:
			System.out.println("不支持的命令");
		}
		return false;
	}

	public static void main(String[] args) {
		while (true) {
			try {
				boolean[] lights = TunnelDevice.getInstance()
						.getInfraredStatus();
				for (int i = 0; i < lights.length; i++) {
					if (lights[i])
						System.out.print("1 ");
					else
						System.out.print("0 ");
				}
				System.out.println();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HardwareException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
