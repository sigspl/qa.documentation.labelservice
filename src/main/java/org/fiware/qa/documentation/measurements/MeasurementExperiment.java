package org.fiware.qa.documentation.measurements;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.fiware.qa.documentation.measurements.ingest.EnablerStorage;
import org.fiware.qa.documentation.measurements.models.EnablerDescription;
import org.fiware.qa.documentation.measurements.util.ValueComparator;



public class MeasurementExperiment {

	
	private EnablerStorage enablers;
	private HashMap<String, Double> measurementResults = new HashMap<String, Double>();
	
	public void setStorage(EnablerStorage d)
	{
		enablers = d;
	}
	
	public void execute (String enablerName)
	{
		EnablerDescription e = enablers.map.get(enablerName);
		CatalogueComplianceMeasurement c = new CatalogueComplianceMeasurement();
		c.setEnabler(e);
		double score = c.measureCompliance();
		
		String entry = e.name+  ";" + round(score,2);
		System.out.println(entry);

		
	}
	
	public void executeAll()
	{
		String out = "";
		for (Iterator<String> iterator = enablers.map.keySet().iterator(); iterator.hasNext();) {
			String n = (String) iterator.next();
			EnablerDescription e = enablers.map.get(n);
			
			CatalogueComplianceMeasurement c = new CatalogueComplianceMeasurement();
			c.setEnabler(e);
			double score = c.measureCompliance();
			/*
			String v = formatDouble (round(score,2));
			String entry = e.name+  ";" + v;
			System.out.println(entry);
			out = out + entry + "\n";
			*/
			measurementResults.put(e.name, round(score,2));
			
		}

		ValueComparator bvc = new ValueComparator(measurementResults);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
		sorted_map.putAll(measurementResults);
        
        
        for(Map.Entry<String,Double> sample : sorted_map.entrySet()) {
        	  String key = sample.getKey();
        	  Double value = sample.getValue();
        	  String entry = key+  ";" + formatDouble(value);
        	  out = out + entry + "\n";
        	  
        	}
        
        System.out.println(out);
        
		// write a file; TODO: make a configuration option or implement further data flow
		try {
			FileUtils.writeStringToFile(new File("enablers_data.csv"), out);
		} catch (IOException e) {
			// TODO add log entry
			e.printStackTrace();
		}
		
	}
	
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	      
	    return bd.doubleValue();
	}
	
	public static String formatDouble (double d)
	{
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
		DecimalFormat df = (DecimalFormat)nf;
		return df.format(d);
	}
	
	
}
