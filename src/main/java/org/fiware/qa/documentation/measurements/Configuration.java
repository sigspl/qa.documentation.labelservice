package org.fiware.qa.documentation.measurements;

import java.util.Arrays;
import java.util.List;

public class Configuration {

	
	public static final String ENABLERS_TRANSCRIPT_TXT = "output/2018-02-measurement_transcript.txt";
	public static final String ENABLERS_TRANSCRIPT_HTML = "output/2018-02-measurement_transcript.html";
	public static final String CSV_SEPARATOR = ",";
	public static String ENABLERS_DATA_FILENAME="output/2018-02-enablers_data.csv";
	public static String QA_DOCS_METRICS_FILENAME="output/2018-02-qa_docs_metrics.csv";
	public static int DECIMAL_PRECISION=4;
	
	public static final String INPUT_CATALOGUE_DATA="input/2018-02-items.json";
	
	public static List<String> FIWARE_CHAPTERS = Arrays.asList(
			"Cloud Hosting",
			"Data/Context Management",
			"Internet of Things Services Enablement",
			"Applications/Services and Data Delivery",
			"Advanced Web-based User Interface",
			"Security",
			"Interface to Networks and Devices");
	
	
}
