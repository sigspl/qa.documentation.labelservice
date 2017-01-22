package org.fiware.qa.documentation.measurements.ingest;

import java.util.Iterator;
import java.util.List;



import org.apache.log4j.Logger;
import org.fiware.qa.documentation.measurements.models.EnablerDescription;
import org.fiware.qa.documentation.measurements.models.ScrapedEnablerCataloguePage;
import org.fiware.qa.documentation.measurements.util.LevenshteinDistance;




public class EnablerPartsAssembler {

	private int acceptableStringDistance_D1=10;

	final static Logger logger = Logger.getLogger(EnablerPartsAssembler.class);
	
	public EnablerDescription assemble (List<ScrapedEnablerCataloguePage> collection)
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
			{
				enabler.terms_conditions = page.text;
				saveMeta(enabler, page);
			}
			else if (token.startsWith("instances"))
			{
				enabler.instances = page.text;
				saveMeta(enabler, page);
			}
			else if (token.startsWith("downloads"))
			{
				enabler.downloads=page.text;
				saveMeta(enabler, page);
			}
			else if (token.startsWith("documentation"))
			{
				enabler.documentation = page.text;
				saveMeta(enabler, page);
			}
			else if (token.startsWith("creating-instances"))
			{
				enabler.creating_instances = page.text;
				saveMeta(enabler, page);
			}
			
			
			
			// if none of the above cases applies, the page is main 'Overview' pages for the Enabler (if not too deviating title) 
			// or there is an error. Assumption: very high similarity between Enabler root URL token and Enabler name
			// - as other relevant pages have been identified as subpages
			else 
			{
				
				int distance = LevenshteinDistance.computeLevenshteinDistance(token, enabler.name.toLowerCase());
				
				if (distance < acceptableStringDistance_D1)
				{
					//logger.info("*INFO* fuzzy matching for " + token + " as overview page for enabler " + enabler.name);
					enabler.overview = page.text;
					//logger.debug(enabler.name + " " + enabler.url +" " + "text: " + enabler.overview.length());
					saveMeta(enabler, page);
									
				}
			
				else
				{
					logger.warn("*WARN* unhandled case match /error: Enabler [" + enabler.name+ "] url ends with: " + token + " ; url=" + page.url + "; lev.distance=" + distance);
				}
				
				if (enabler.overview.length()<10 )
					logger.error("short overview text for " + page.url);
				if (page.text.length()<10)
					logger.warn("please double-check overview assigning for url " + enabler);
				
			}
				
			
		} // end for
		
		enabler.url=shortestUrl;
		enabler.text = accumulatedText;
		
		return enabler;
	}
	
	
	private void saveMeta(EnablerDescription enabler, ScrapedEnablerCataloguePage page)
	{
		// In case the template mechanism  works well, all meta entries would be equal. We can test this here.
		page.meta = page.meta.trim();
		
		if (enabler.meta == null || enabler.meta.length()==0) 
		{
			enabler.meta = page.meta; // initial write-through
			// but meta should not be empty!
			if (page.meta ==null  || page.meta.length()==0)
				logger.error("Enabler meta data missing in this url? " + page.meta);

		}
		
		else if (enabler.meta.equalsIgnoreCase(page.meta))
		{
				// it is safe to overwrite
				enabler.meta = page.meta;
		
		}
		else
		{
				// something is wrong, print out error
				logger.error("Enabler meta data mismatch detected!");
				logger.error("ON ONE PAGE (need to manual-check: " + enabler.meta + "; length=" + enabler.meta.length());
				logger.error("ON ANOTHER PAGE ["+ page.url+ "]: " + page.meta + "; length=" + page.meta.length());
		}

	}
	
}
