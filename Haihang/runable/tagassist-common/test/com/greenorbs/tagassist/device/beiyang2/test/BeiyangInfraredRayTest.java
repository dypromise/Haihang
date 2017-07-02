package com.greenorbs.tagassist.device.beiyang2.test;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.greenorbs.tagassist.device.InfraredRayExtListener;
import com.greenorbs.tagassist.device.beiyang2.BeiyangInfraredRay;

public class BeiyangInfraredRayTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		try {
			ApplicationContext ctx = new FileSystemXmlApplicationContext(
					"bean.xml");

			BeiyangInfraredRay infraredray = (BeiyangInfraredRay) ctx
					.getBean("infraredRay");
			infraredray.setDetectionListener(new InfraredRayExtListener() {

				@Override
				public void objectDetected(boolean[] rays) {
					for (int i = 0; i < rays.length; i++) {
						System.out.print(rays[i] ? 1 : 0);
					}
					System.out.println();

				}

			});
			infraredray.startup();
			Thread.sleep(100000);

			infraredray.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
