package com.greenorbs.tagassist.device.beiyang2.test;

import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.greenorbs.tagassist.device.BarcodeScanListener;
import com.greenorbs.tagassist.device.IBarcodeScanner;

public class BeiyangBarcodeScannerTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testSetScanListener() {
		
		try {
			ApplicationContext ctx = new FileSystemXmlApplicationContext("bean.xml");

			IBarcodeScanner barcodeScanner = (IBarcodeScanner) ctx
					.getBean("barcodeScanner");
			barcodeScanner.setScanListener(new BarcodeScanListener() {

				@Override
				public void barcodeScanned(String barcode) {
					System.out.println(barcode);
					
				}
				
			});
			barcodeScanner.startup();
			Thread.sleep(100000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		

	}

	@Test
	public void testGetScanListener() {
//		fail("Not yet implemented");
	}

}
