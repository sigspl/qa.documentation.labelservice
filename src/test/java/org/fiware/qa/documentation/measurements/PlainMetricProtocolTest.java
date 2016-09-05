package org.fiware.qa.documentation.measurements;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlainMetricProtocolTest {

	@Test
	public void test() {
		
		PlainMetricProtocol p = new PlainMetricProtocol();
		
		p.storeEntry("enabler1", "enabler1 -> statement1");
		p.storeEntry("enabler1", "enabler1 -> statement2");
		
		p.storeEntry("enabler2", "enabler2 -> statement1");
		p.storeEntry("enabler2", "enabler2 -> statement2");
		
		p.storeEntry("enabler1", "enabler1 ->statement3");
				
		
		System.out.println(p.getLog("enabler1"));
		System.out.println(p.getLog("enabler2"));
		
		
	}

}
