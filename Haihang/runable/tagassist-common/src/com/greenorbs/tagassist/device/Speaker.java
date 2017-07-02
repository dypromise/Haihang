package com.greenorbs.tagassist.device;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.greenorbs.tagassist.Result;

public class Speaker implements ISpeaker {

	protected static Logger _logger = Logger.getLogger(Speaker.class);

	private String _shortSound;

	private String _longSound;

	private String _midSound;

	private AudioClip _shortClip;

	private AudioClip _midClip;

	private AudioClip _longClip;

	public String getShortSound() {
		return _shortSound;
	}

	public void setShortSound(String shortSound) {
		_shortSound = shortSound;
	}

	public String getLongSound() {
		return _longSound;
	}

	public void setLongSound(String longSound) {
		_longSound = longSound;
	}

	public String getMidSound() {
		return _midSound;
	}

	public void setMidSound(String midSound) {
		_midSound = midSound;
	}

	@Override
	public void startup() throws HardwareException {

		try {
			this._longClip = Applet.newAudioClip(new File(this._longSound)
					.toURI().toURL());
			this._midClip = Applet.newAudioClip(new File(this._midSound)
					.toURI().toURL());
			this._shortClip = Applet.newAudioClip(new File(this._shortSound)
					.toURI().toURL());

		} catch (MalformedURLException e) {
			e.printStackTrace();
			_logger.error("it fails to load the round file.", e);
		}
	}

	@Override
	public Result shutdown() throws HardwareException {
		this.mute();
		return Result.SUCCESS;
	}

	@Override
	public Result reset() throws HardwareException {
		return Result.SUCCESS;
	}

	@Override
	public int getStatus() {
		return IHardware.STATUS_ON;
	}

	@Override
	public void speak(int mode) throws HardwareException {
		switch (mode) {
		case LONG_MODE:
			if (this._longClip != null) {
				this._longClip.play();
			}
			break;
		case SHORT_MODE:
			if (this._shortClip != null) {
				this._shortClip.play();
			}
			break;
		case MIDDLE_MODE:
			if (this._midClip != null) {
				this._midClip.play();
			}
			break;
		}

	}

	@Override
	public void mute() throws HardwareException {
		if (this._longClip != null) {
			this._longClip.stop();
		}
		if (this._midClip != null) {
			this._midClip.stop();
		}
		if (this._shortClip != null) {
			this._shortClip.stop();
		}
	}

	public static void main(String[] args) throws Exception {
		Speaker speaker = new Speaker();
		speaker.startup();
		speaker.setShortSound("error.wav");
		speaker.speak(ISpeaker.SHORT_MODE);

		// Applet.newAudioClip(new File("error.wav").toURI().toURL()).play();

		Thread.sleep(10000);
	}

}
