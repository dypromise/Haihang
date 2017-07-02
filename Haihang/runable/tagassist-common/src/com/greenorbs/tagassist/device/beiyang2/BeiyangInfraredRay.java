package com.greenorbs.tagassist.device.beiyang2;

import org.apache.log4j.Logger;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.InfraredRayExt;
import com.greenorbs.tagassist.device.InfraredRayExtListener;

public class BeiyangInfraredRay extends Thread implements InfraredRayExt {

	private static Logger _logger = Logger.getLogger(BeiyangInfraredRay.class);

	private BeiyangController _controller;
	private InfraredRayExtListener _listener;

	private int _status = -1;
	private boolean _over;

	public BeiyangController getController() {
		return _controller;
	}

	public void setController(BeiyangController controller) {
		_controller = controller;
	}

	@Override
	public void startup() throws HardwareException {

		if (_status == IHardware.STATUS_ON) {
			return;
		}
		this._over = false;

		if (this._controller == null) {
			_logger.error("the controller is null.");
			throw new HardwareException("the controller cannot be empty.");
		}

		this._controller.open();

		this.start();

		_status = IHardware.STATUS_ON;

	}

	@Override
	public Result shutdown() throws HardwareException {
		try {
			this._over = true;
			this._controller.close();
		} catch (Exception e) {
			return Result.ERROR;
		}

		return Result.SUCCESS;

	}

	@Override
	public Result reset() throws HardwareException {
		return Result.ERROR;
	}

	@Override
	public int getStatus() {
		return 0;
	}

	@Override
	public void setDetectionListener(InfraredRayExtListener listener) {

		this._listener = listener;
	}

	@Override
	public InfraredRayExtListener getDetectionListener() {
		return this._listener;
	}

	@Override
	public void run() {
		while (_over == false) {

			try {
				Thread.sleep(_controller.getQueryRayPeriod());
			} catch (Exception e) {
			}

			try {

				this._controller
						.sendCommand(BeiyangInfraredRayQueryCmd.QUERY_COMMAND);

				BeiyangInfraredRayResponse response = BeiyangInfraredRayResponse
						.tryParse(_controller.getDataQueue());
				boolean valid = false;
				if (response != null) {
					valid = response.isValid();
					if (valid) {
						if (this._listener != null) {
							this._listener.objectDetected(response
									.getRayStatus());
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				_logger.error("it fails to send commands to beiyang controller.");
			}
		}

	}

}
