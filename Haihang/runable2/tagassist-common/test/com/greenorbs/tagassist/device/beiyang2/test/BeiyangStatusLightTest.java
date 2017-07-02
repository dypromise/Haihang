package com.greenorbs.tagassist.device.beiyang2.test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.greenorbs.tagassist.device.IBarcodeScanner;
import com.greenorbs.tagassist.device.IStatusLight;

public class BeiyangStatusLightTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		try {
			ApplicationContext ctx = new FileSystemXmlApplicationContext("bean.xml");

			IStatusLight statusLight = (IStatusLight) ctx
					.getBean("statusLight");
			statusLight.startup();
			statusLight.turnOn(IStatusLight.YELLOW);
			Thread.sleep(2000);
			statusLight.blink(IStatusLight.YELLOW);

			statusLight.turnOn(IStatusLight.RED);
			Thread.sleep(2000);
			statusLight.turnOff(IStatusLight.RED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
