package com.greenorbs.tagassist.device;

public interface IBarcodeScanner extends IHardware {


	/**
	 * Add a identify listeners to the reader
	 * 
	 * @param listener
	 */
	public void setScanListener(BarcodeScanListener listener);

	public BarcodeScanListener getScanListener();

}
