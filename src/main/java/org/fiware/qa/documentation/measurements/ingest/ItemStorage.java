package org.fiware.qa.documentation.measurements.ingest;

import java.util.List;

import org.fiware.qa.documentation.measurements.models.ScrapedEnablerCataloguePage;

import java.util.HashMap;
import java.util.LinkedList;

public class ItemStorage  {

	public HashMap<String, List<ScrapedEnablerCataloguePage> > map = new HashMap<String, List<ScrapedEnablerCataloguePage> >();
	
	public void store(String name, ScrapedEnablerCataloguePage pageItem)
	{
		List<ScrapedEnablerCataloguePage> list;
		if (!map.containsKey(name))
		{
			list = new LinkedList<ScrapedEnablerCataloguePage>();
				
		}
		else
		{
			list = map.get(name);
				
		}
		
		list.add(pageItem);
		map.put(name, list);
		
		
	}
	
	
	
}
