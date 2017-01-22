package org.fiware.qa.documentation.measurements.ingest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fiware.qa.documentation.measurements.models.EnablerDescription;




public class EnablerStorage {

	
public HashMap<String, EnablerDescription > assets = new HashMap<String, EnablerDescription >();
	
	public void store(String name, EnablerDescription item)
	{
		
		assets.put(name, item);
		
		//System.out.println("stored enabler: " + item.toString());
	}
	
	public void listEnablers ()
	{
		List sortedKeys=new ArrayList(assets.keySet());
		Collections.sort(sortedKeys);
		for (Iterator iterator = sortedKeys.iterator(); iterator.hasNext();) {
			String enablerName = (String) iterator.next();
			EnablerDescription enabler = assets.get(enablerName);
			//System.out.println(enabler.name + ";" + enabler.overview.length());
			
		}

		
	}
	
	
}
