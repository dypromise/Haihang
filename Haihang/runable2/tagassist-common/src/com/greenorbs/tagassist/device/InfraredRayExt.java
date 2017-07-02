package com.greenorbs.tagassist.device;

public interface InfraredRayExt extends IHardware {

	void setDetectionListener(InfraredRayExtListener listener);

	InfraredRayExtListener getDetectionListener();
}
