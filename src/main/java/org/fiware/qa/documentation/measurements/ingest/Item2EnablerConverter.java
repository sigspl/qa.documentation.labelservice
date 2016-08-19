package org.fiware.qa.documentation.measurements.ingest;

import java.util.Iterator;
import java.util.List;



import org.apache.log4j.Logger;
import org.fiware.qa.documentation.measurements.models.EnablerDescription;
import org.fiware.qa.documentation.measurements.models.ScrapedEnablerCataloguePage;
import org.fiware.qa.documentation.measurements.util.LevenshteinDistance;




public class Item2EnablerConverter {

	private int acceptableStringDistance_D1=10;

	final static Logger logger = Logger.getLogger(Item2EnablerConverter.class);
	
	public EnablerDescription convert (List<ScrapedEnablerCataloguePage> collection)
	{
		
		EnablerDescription enabler = new EnablerDescription();
		String shortestUrl=collection.get(0).url;
		enabler.name=collection.get(0).enabler.trim();
		String accumulatedText = "";
		
		for (Iterator<ScrapedEnablerCataloguePage> iterator = collection.iterator(); iterator.hasNext();) {
			ScrapedEnablerCataloguePage page = (ScrapedEnablerCataloguePage) iterator.next();
			
			if (page.url.length()<shortestUrl.length())
				shortestUrl=page.url;
			
			accumulatedText = accumulatedText + page.text;
			
			String[] urlSplit = page.url.split("/");
			String token = urlSplit[urlSplit.length-1];
			
			/* if exact matching => wait for clarification in http://jira.fiware.org/browse/HELP-7098
			if (token.equalsIgnoreCase("terms-and-conditions"))
				enabler.terms_conditions = token;
			else if (token.equalsIgnoreCase("instances"))
				enabler.instances = token;
			else if (token.equalsIgnoreCase("downloads"))
				enabler.downloads=token;
			else if (token.equalsIgnoreCase("documentation"))
				enabler.documentation = token;
			else if (token.equalsIgnoreCase("creating-instances"))
				enabler.creating_instances = token;
			
			*/
			
			if (token.startsWith("terms-and-conditions"))
				enabler.terms_conditions = page.text;
			else if (token.startsWith("instances"))
				enabler.instances = page.text;
			else if (token.startsWith("downloads"))
				enabler.downloads=page.text;
			else if (token.startsWith("documentation"))
				enabler.documentation = page.text;
			else if (token.startsWith("creating-instances"))
				enabler.creating_instances = page.text;
			
			
			
			
			else 
			{
				
				int distance = LevenshteinDistance.computeLevenshteinDistance(token, enabler.name.toLowerCase());
				
				if (distance < acceptableStringDistance_D1)
				{
					//logger.info("*INFO* fuzzy matching for " + token + " as overview page for enabler " + enabler.name);
					enabler.overview = page.text;
				}
				else
				{
					logger.warn("*WARN* unhandled case match /error: Enabler [" + enabler.name+ "] url ends with: " + token + " ; url=" + page.url + "; lev.distance=" + distance);
				}
				
				
			}
				
			
			
		}
		
		enabler.url=shortestUrl;
		enabler.text = accumulatedText;
		
		return enabler;
	}
	
}
