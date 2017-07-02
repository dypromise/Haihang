package com.greenorbs.tagassist.device.beiyang2.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.greenorbs.tagassist.device.ICautionLight;
import com.greenorbs.tagassist.device.IStatusLight;

public class BeiyangCautionLightTest {

	@Test
	public void test() {
		try {
			ApplicationContext ctx = new FileSystemXmlApplicationContext("bean.xml");

			ICautionLight light = (ICautionLight) ctx
					.getBean("cautionLight");
			light.startup();
			light.turnOn(ICautionLight.RED);
			Thread.sleep(2000);
			light.turnOn(ICautionLight.YELLOW);
			Thread.sleep(2000);
			light.turnOn(ICautionLight.BLUE);
			Thread.sleep(2000);
			light.turnOn(ICautionLight.PURPLE);
			Thread.sleep(2000);
			light.turnOn(ICautionLight.WHITE);
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
