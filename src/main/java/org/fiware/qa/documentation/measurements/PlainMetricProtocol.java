package org.fiware.qa.documentation.measurements;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

public class PlainMetricProtocol {

	
	HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>(); 
	
	public void storeEntry (String enabler, String statement)
	{

		ArrayList<String> log = map.get(enabler);
		if (log==null) log = new ArrayList<String>();
		
		log.add(statement);
		map.put(enabler, log);

	}
	
	public String getLog(String enabler)
	{
		ArrayList<String> log = map.get(enabler);
		
		if (log==null) return "no measurements recorded for " + enabler + " log was not initialized";
		if (log.size()==0) return "no measurements recorded for " + enabler;
		
		System.out.println("log entries: " +  log.size() + " for enabler " + enabler);
			
				
		return StringUtils.join(log,"\n");
		
		
	}
	
}
