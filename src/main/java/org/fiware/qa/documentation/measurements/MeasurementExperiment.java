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
import org.fiware.qa.labels.SimpleLabeler;

import reporting.HTMLReport;

public class MeasurementExperiment {

	public static final String ESC_DQUOTE = "\"";

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
		for (Iterator<String> iterator = enablers.map.keySet()
				.iterator(); iterator.hasNext();) {
			String n = (String) iterator.next();
			EnablerDescription e = enablers.map.get(n);

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
					round(score, Configuration.DECIMAL_PRECISION));

		}

		// sort collected values
		ValueComparator bvc = new ValueComparator(measurementResults);
		TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(
				bvc);
		sorted_map.putAll(measurementResults);

		SimpleLabeler sl = new SimpleLabeler();
		String protocolTranscript = "";
		for (Map.Entry<String, Double> sample : sorted_map.entrySet()) {
			String key = sample.getKey();
			Double value = sample.getValue();

			String entry = dquote(key) + Configuration.CSV_SEPARATOR + dquote(formatDouble(value))
					+ ";" + sl.produceLinearLabel(value);
			out = out + entry + "\n";

			protocolTranscript += "\n++++++++\n\n";

			protocolTranscript += (protocol.getLog(key));
			/**
			 * HTMLReport html = new HTMLReport(); html.setProtocol(protocol);
			 * html.generate();
			 */

			System.out.println(protocolTranscript);

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
			// TODO add log entry
			e.printStackTrace();
		}

	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.CEILING);

		return bd.doubleValue();
	}

	public static String formatDouble(double d) {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
		DecimalFormat df = (DecimalFormat) nf;
		return df.format(d);
	}

	private String dquote(String x) {
		return ESC_DQUOTE + x + ESC_DQUOTE;
	}

}
