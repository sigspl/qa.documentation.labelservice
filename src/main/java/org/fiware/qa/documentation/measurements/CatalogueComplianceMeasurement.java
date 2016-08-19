package org.fiware.qa.documentation.measurements;

import java.util.HashMap;
import java.util.Iterator;

import org.fiware.qa.documentation.measurements.models.EnablerDescription;



/**
 * 
 * @author pmuryshkin
 * Qualify Enabler descriptions available from the Catalogue for compliance with
 * the Guide.
 * Guide: https://forge.fiware.org/plugins/mediawiki/wiki/fiware/index.php/Working_with_the_FIWARE_catalogue#Guidelines_on_what_to_write
 */
// TODO: add scan of page links as URL array or similar for additional layer of evaluation = will add more precision and provide data to DEPLOYMENT evaluation layer
public class CatalogueComplianceMeasurement {

	public int maximumPoints=0;
	private EnablerDescription enabler;
	public HashMap<String, Integer> attributes = new HashMap<String, Integer> ();
	
	
	public void setEnabler(EnablerDescription e)
	{
		enabler = e;
	}
	
	public double measureCompliance()
	{
		
		double score = 0;
		score = measureOverview() +
				measureCreatingInstances() +
				measureDownloads() +
				measureDocumentation() +
				measureInstances() +
				measureTermsConditions();
		
		calculateMaximumPoints(); // !requires all methods to register points!
		return score/maximumPoints;
				
				
	}
	
	private int measureOverview()
	{
		int localScore = 0;
		
		// attributable quality: overview contains string "What you get"
		attributes.put("catalogue.overview.what", 10);
		//System.out.println(enabler.overview);
		if (enabler.overview.contains("What you get"))
			localScore+=attributes.get("catalogue.overview.what");
		
		// attributable quality: overview contains string "Why to get it"
		attributes.put("catalogue.overview.why", 10);
		if (enabler.overview.contains("Why to get it"))
			localScore+=attributes.get("catalogue.overview.why");
			
	
		// attributable quality: text length should be in certain range;
		// too short text is non-informative, or indicates that something might be 
		// missing; too long reduces readability and in this context marketing effect.
		// as of August 2016, measured values lay between (789,20541) characters,
		// with a median value 1500 (the closest Enablers is  "3D-UI-XML3D". 
		// we could assume 1500 characters to be a good average length to present an enabler,
		// and give 10 points for this. Deviation from this
		// ideal value means subtraction of points following some adequate formula.
		
		// formula: 10-10*(ABS(1-(ABS(1-(ABS(LENGTH-MEDIAN)/MEDIAN)))))
		
		float median = 1500;
		float L = enabler.overview.length();
		float p = 10;
		int x = Math.round(p-p*(Math.abs(1-(Math.abs(1-(Math.abs(L-median)/median)))))); 
		attributes.put("catalogue.overview.length", 10);
		localScore+=x; // could be 10 points for a text close to median, or 2-3 points for too long or too short text
		
		//System.out.println("local score: " +  localScore);
		
		return localScore;
	}
	

	private int measureCreatingInstances()
	{
		int localScore=0;

		// score sections following the template
		String m="";
		
		m="Deploying a dedicated GE instance based on an image";
		attributes.put("catalogue.creating_instances.section1", 10);
		if (enabler.creating_instances.matches(m))
			localScore+=10;
		
		
		m="Deploying a dedicated GE instance in your own virtual infrastructure";
		attributes.put("catalogue.creating_instances.section2", 10);
		if (enabler.creating_instances.matches(m))
			localScore+=10;
		
		
		m="Deploying a dedicated GE instance based on blueprint templates for this GE";
		attributes.put("catalogue.creating_instances.section3", 10);
		if (enabler.creating_instances.matches(m))
			localScore+=10;
		
		
		// "If there are no such image, the section must be filled in with "There are no images created for this GE implementation yet."
		// -> so far, we assume that community members honestly follow the Guide in this case: we search for quality, not for fraud.
		// attributable quality: overview contains specified string 
		attributes.put("catalogue.creating_instances.image", 10);
		String image_statement = "There are no images created for this GE implementation yet.";
		if (!enabler.creating_instances.matches(image_statement))
			localScore+=10; // otherwise we score here 10 points also in cases with another wording or fraud.
		
		
		// Docker references. Additionally, this section must include references to the Docker containers and the recipes available (if any). 
		// give 1 point for mentioning "Docker", 4 for mentioning "DockerHub"/"Docker Hub" and 5 for "Dockerfile"
		attributes.put("catalogue.creating_instances.docker", 10);
		if (enabler.creating_instances.matches("Docker"))
			localScore+=1;
		if (enabler.creating_instances.matches("Docker Hub") || enabler.creating_instances.matches("DockerHub"))
			localScore+=4;
		if (enabler.creating_instances.matches("Dockerfile"))
			localScore+=5;
		
		
		return localScore;
	}

	private int measureDownloads()
	{
		return 0;
	}
	
	private int measureDocumentation()
	{
		return 0;
	}
	
	private int measureTermsConditions()
	{
		return 0;
	}
	
	private int measureInstances()
	{
		return 0;
	}
	
	
	private void calculateMaximumPoints()
	{
		int sum = 0;
		for (int f : attributes.values()) {
		    sum += f;
		}
		//System.out.println("registered max points for Catalogue: " + sum);
		maximumPoints = sum;
		
	}
	
	
}
