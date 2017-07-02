package com.greenorbs.tagassist.device.beiyang2;

import java.util.Arrays;
import java.util.Queue;
import java.util.Random;

public class BeiyangInfraredRayResponse extends BeiyangSerialResponse {

	public static final int RAY_RECORD_LENGTH = 7;

	public BeiyangInfraredRayResponse(byte[] data) {
		super(data);
	}

	public boolean[] getRayStatus() {

		byte[] data = this.getContent();
		int[] message = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			if (data[i] < 0) {
				message[i] = data[i] + 256;
			} else {
				message[i] = data[i];
			}
		}

		int num = this.getContentLength();
		int count = 0;
		int[] lightscount = new int[16];
		for (int i = 0; i < num / 7; i++) {
			count++;
			for (int k = 0; k < 2; k++) {
				int temp = message[1 + k];
				for (int j = 0; j < 8; j++) {
					if ((temp & (int) Math.pow(2, j)) != 0) {
						lightscount[8 * k + j]++;
					}
				}
			}
		}

		// System.out.println("count:" + count);
		// System.out.println("lightcount:" + Arrays.toString(lightscount));
		boolean[] rtn = new boolean[16];
		for (int i = 0; i < 16; i++) {
			rtn[i] = lightscount[i] > 5;

		}

		return rtn;

	}

	public static BeiyangInfraredRayResponse tryParse(Queue<Byte> queue) {

		if (queue == null || queue.size() == 0) {
			return null;
		}
		if(queue.peek().intValue() > queue.size()
				|| queue.peek().intValue() <= 0){
			return null;
		}

		int frameLength = queue.peek().intValue();

		byte[] data = new byte[frameLength];

		for (int i = 0; i < frameLength; i++) {
			data[i] = queue.poll();
		}

		return new BeiyangInfraredRayResponse(data);

	}

}
