package com.greenorbs.tagassist.device.beiyang2;

public class BeiyangInfraredRayQueryCmd extends BeiyangSerialCmd {

	private byte[] _bytes = null;

	public static final BeiyangInfraredRayQueryCmd QUERY_COMMAND = new BeiyangInfraredRayQueryCmd();

	public BeiyangInfraredRayQueryCmd() {
		super(BeiyangSerials.COMMAND_QUERY_INFRARED_RAY);
	}

	/**
	 * Sinc the query command keeps constants, we use a Flyweight pattern.
	 */
	@Override
	public byte[] toBytes() {
		if (_bytes == null) {
			_bytes = super.toBytes();
		}
		return _bytes;
	}

	@Override
	public byte[] getContent() {
		return new byte[0];
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
