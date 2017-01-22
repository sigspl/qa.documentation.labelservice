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
import org.sigspl.analysis.commons.labeling.SimpleLabeler;
import org.sigspl.analysis.commons.util.MathUtil;
import org.sigspl.analysis.commons.util.StringUtil;



public class MeasurementExperiment {

	

	private EnablerStorage enablers;
	private HashMap<String, Double> measurementResults = new HashMap<String, Double>();
	private PlainMetricProtocol protocol = new PlainMetricProtocol();

	public void setStorage(EnablerStorage d) {
		enablers = d;
	}

	/*
	 * public void execute (String enablerName) { EnablerDescription e =
	 * enablers.map.get(enablerName); CatalogueComplianceMeasurement c = new
	 * CatalogueComplianceMeasurement(); c.setEnabler(e);
	 * c.setLogCollector(protocol); double score = c.measureCompliance();
	 * 
	 * String entry = e.name+ ";" +
	 * round(score,Configuration.DECIMAL_PRECISION); System.out.println(entry);
	 * System.out.println("++++++++");
	 * 
	 * System.out.println(protocol.getLog(enablerName));
	 * 
	 * System.out.println("\n+++\n\n");
	 * 
	 * 
	 * 
	 * }
	 */

	public void executeAll() {
		String out = "";
		for (Iterator<String> iterator = enablers.assets.keySet()
				.iterator(); iterator.hasNext();) {
			String n = (String) iterator.next();
			EnablerDescription e = enablers.assets.get(n);

			CatalogueComplianceMeasurement c = new CatalogueComplianceMeasurement();
			c.setEnabler(e);
			c.setLogCollector(protocol);
			double score = c.measureCompliance();
			/*
			 * String v = formatDouble
			 * (round(score,Configuration.DECIMAL_PRECISION)); String entry =
			 * e.name+ ";" + v; System.out.println(entry); out = out + entry +
			 * "\n";
			 */
			measurementResults.put(e.name,
					MathUtil.round(score, Configuration.DECIMAL_PRECISION));

		}

		// sort collected values
		ValueComparator bvc = new ValueComparator(measurementResults);
		TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(
				bvc);
		sorted_map.putAll(measurementResults);

		SimpleLabeler sl = new SimpleLabeler();
		String protocolTranscript = "Catalogue compliance guide check transcript \nDetails: https://forge.fiware.org/plugins/mediawiki/wiki/fiware/index.php/Working_with_the_FIWARE_catalogue#Guidelines_on_what_to_write\n";
		protocolTranscript += "\n\n *** ATTENTION ***: Strikingly low scores might be affected by https://jira.fiware.org/browse/CAT-346 or some other error either in Catalogue or this software! Your feedback is greatly appreciated - just post an issue in this GitHub repo!.\n\n";
		protocolTranscript+="Date of measurement procedure: " + StringUtil.getTodayIsoDate() + "\n";
		protocolTranscript+="Used Catalogue online data scraped as of: " + StringUtil.getFileIsoDateLastModified(Configuration.INPUT_CATALOGUE_DATA)+"\n\n\n\n";
		
		
		for (Map.Entry<String, Double> sample : sorted_map.entrySet()) {
			String key = sample.getKey();
			Double value = sample.getValue();

			// output of labels as CSV table
			String entry = StringUtil.dquote(key) + Configuration.CSV_SEPARATOR + StringUtil.dquote(MathUtil.formatDouble(value))
					+ Configuration.CSV_SEPARATOR + StringUtil.dquote( sl.produceLinearLabel(value));
			out = out + entry + "\n";

			protocolTranscript += "\n++++++++\n\n";

			protocolTranscript += (protocol.getLog(key));
			/**
			 * HTMLReport html = new HTMLReport(); html.setProtocol(protocol);
			 * html.generate();
			 */

			//System.out.println(protocolTranscript);

		} // end for
		try {
			FileUtils.writeStringToFile(
					new File(Configuration.ENABLERS_TRANSCRIPT_TXT),
					protocolTranscript);
		} catch (IOException e) {
			// TODO add log entry
			e.printStackTrace();
		}

		System.out.println(out);

		// write a file; TODO: make a configuration option or implement further
		// data flow
		try {
			FileUtils.writeStringToFile(
					new File(Configuration.ENABLERS_DATA_FILENAME), out);
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	

}
