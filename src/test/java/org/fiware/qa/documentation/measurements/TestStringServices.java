package org.fiware.qa.documentation.measurements;

import static org.junit.Assert.*;

import org.fiware.qa.documentation.measurements.util.StringServices;
import org.junit.Test;

public class TestStringServices {

	public static String test ="Chapter:       Applications/Services and Data Delivery\n " +
			"Version:       4.4.5 \n" +      
				        "Updated:       2016-09-27\n" +      
				        "Contact Person: Flavio de la Vega      \n" +      
				            "fdelavega@conwet.com\n" +      
				            "Deprecated GEis\n" +      
				        "Feedback:       Send feedback\n"+
				"Chapter:       Data/Context Management  \n"+    
				        "Version:       6.6.0      \n" +
				        "Updated:       2016-10-21   \n   " +
				        "Contact Person:  Luis Lopez      \n" +      
				        "    luis.lopez@urjc.es \n" +
				      
				         "   FIWARE GEris \n " +      
				        "Feedback:       Send feedback \n";
	
	
	@Test
	public void test() {
			
		String[] test_version_correct = {"0.1.2", "3.11", "5.4.2.1.0", "4.4.3", "5.1.211"}; 
		String[] test_version_wrong = {"foo", "3.1.", "v.1.4"};
		
		for (int i = 0; i < test_version_correct.length; i++) {
			assertTrue(test_version_correct[i], StringServices.matchSoftwareVersion(test_version_correct[i]));
		}
		
		for (int i = 0; i < test_version_wrong.length; i++) {
			assertFalse(test_version_wrong[i], StringServices.matchSoftwareVersion(test_version_wrong[i]));
		}
		
		
		assertArrayEquals(new String [] {"Applications/Services and Data Delivery"}, new String[] {StringServices.extractValue(test, "Chapter:\\s")});;
		
	}

}
