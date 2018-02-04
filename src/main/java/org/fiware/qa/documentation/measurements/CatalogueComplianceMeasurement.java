package org.fiware.qa.documentation.measurements;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import org.fiware.qa.documentation.measurements.models.EnablerDescription;

//import com.joestelmach.natty.*;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import de.fhg.iais.re.analysis.commons.util.StringUtil;


/**
 * 
 * @author pmuryshkin Qualify Enabler descriptions available from the Catalogue
 *         for compliance with the Guide. Guide:
 *         https://forge.fiware.org/plugins/mediawiki/wiki/fiware/index.php/Working_with_the_FIWARE_catalogue#Guidelines_on_what_to_write
 */
// TODO: add scan of page links as URL array or similar for additional layer of
// evaluation = will add more precision and provide data to DEPLOYMENT
// evaluation layer
public class CatalogueComplianceMeasurement {

	public int maximumPoints = 0;
	private EnablerDescription enabler;
	public HashMap<String, Integer> attributes = new HashMap<String, Integer>();
	private Table<String, Integer, Integer> feature_metrics = HashBasedTable.create();
	
	public Table<String, Integer, Integer> getFeature_metrics() {
		return feature_metrics;
	}

	private PlainMetricProtocol protocol;

	final static Logger logger = Logger
			.getLogger(CatalogueComplianceMeasurement.class);

	public void setFeature_metrics(
			Table<String, Integer, Integer> feature_metrics) {
		this.feature_metrics = feature_metrics;
	}

	public void setEnabler(EnablerDescription e) {
		enabler = e;
	}

	public double measureCompliance() {

		double score = 0;
		
		int meta = measureMeta();  
		
	    int score_01 = ft01_measureOverview(); 
	    int score_02 = ft02_measureCreatingInstances();
		int score_03 = ft03_measureDocumentation();
		int score_04 = ft04_measureDownloads();
		int score_05 = ft05_measureInstances();
		int score_06 = ft06_measureTermsConditions();

		score = meta + score_01 + score_02 + score_03 + score_04 + score_05 + score_06;
		
		
		calculateMaximumPoints(); // !requires all methods to register points!
		printAttributes();
		return score / maximumPoints;

	}

	private int ft01_measureOverview() {
		int localScore = 0;

		if (enabler.overview.length() < 10)
			logger.warn("unassigned text for " + enabler.name);

		// attributable quality: overview contains string "What you get"
		attributes.put("catalogue.overview.what", 10);
		// System.out.println(enabler.overview);
		if (enabler.overview.contains("What you get")) {
			localScore += attributes.get("catalogue.overview.what");
			protocol.storeEntry(enabler.name,
					"10/10 points for mentioning 'What you get'");
		} else
			protocol.storeEntry(enabler.name,
					"0/10 points for missing 'What you get'");

		// attributable quality: overview contains string "Why to get it"
		attributes.put("catalogue.overview.why", 10);
		if (enabler.overview.contains("Why to get it")) {
			localScore += attributes.get("catalogue.overview.why");
			protocol.storeEntry(enabler.name,
					"10/10 points for mentioning 'Why to get it'");
		} else
			protocol.storeEntry(enabler.name,
					"0/10 points for missing 'Why to get  it'");

		// attributable quality: text length should be in certain range;
		// too short text is non-informative, or indicates that something might
		// be
		// missing; too long reduces readability and in this context marketing
		// effect.
		// as of August 2016, measured values lay between (789,20541)
		// characters,
		// with a median value 1500 (the closest Enablers is "3D-UI-XML3D".
		// we could assume 1500 characters to be a good average length to
		// present an enabler,
		// and give 10 points for this. Deviation from this
		// ideal value means subtraction of points following some adequate
		// formula.

		// formula: 10-10*(ABS(1-(ABS(1-(ABS(LENGTH-MEDIAN)/MEDIAN)))))

		float median = 1500;
		float L = enabler.overview.length();
		float p = 10;
		int x = Math.round(p - p * (Math.abs(
				1 - (Math.abs(1 - (Math.abs(L - median) / median))))));
		attributes.put("catalogue.overview.optimal_length", 10);
		protocol.storeEntry(enabler.name,
				x + "/10 points for matching recommended median-based text length 1500 chars: length="
						+ (int) L);

		localScore += x; // could be 10 points for a text close to median, or
							// 2-3 points for too long or too short text

		// System.out.println("local score: " + localScore);

		feature_metrics.put(enabler.name, 1, localScore);
		
		return localScore;
	}

	private int ft02_measureCreatingInstances() {
		int localScore = 0;

		// score sections following the template
		String m, e = "";
		int instancesScore = 0;

		m = "Deploying a dedicated GE instance based on an image";
		e = "There are no images created for this GE implementation yet.";
		attributes.put("catalogue.creating_instances.section1", 10);
		if (StringUtil.matchRegex(enabler.creating_instances, m)) {
			instancesScore += 10;

		} else if (StringUtil.matchRegex(enabler.creating_instances, e))
			instancesScore += 2;

		m = "Deploying a dedicated GE instance in your own virtual infrastructure";
		e = "There are no recipe created for this GE implementation yet.";
		attributes.put("catalogue.creating_instances.section2", 10);
		if (StringUtil.matchRegex(enabler.creating_instances, m))
			instancesScore += 10;
		else if (StringUtil.matchRegex(enabler.creating_instances, e))
			instancesScore += 2;

		m = "Deploying a dedicated GE instance based on blueprint templates for this GE";
		attributes.put("catalogue.creating_instances.section3", 10);
		if (StringUtil.matchRegex(enabler.creating_instances, m))
			instancesScore += 10;

		// Docker references. Additionally, this section must include references
		// to the Docker containers and the recipes available (if any).
		// give 1 point for mentioning "Docker", 4 for mentioning
		// "DockerHub"/"Docker Hub" and 5 for "Dockerfile"
		attributes.put("catalogue.creating_instances.docker", 10);
		if (StringUtil.matchRegex(enabler.creating_instances,
				"\\sDocker\\s"))
			instancesScore += 1;
		if (StringUtil.matchRegex(enabler.creating_instances,
				"\\sDocker Hub\\s")
				|| StringUtil.matchRegex(enabler.creating_instances,
						"\\sDockerHub\\s"))
			instancesScore += 4;
		if (StringUtil.matchRegex(enabler.creating_instances,
				"\\sDockerfile\\s"))
			instancesScore += 5;

		protocol.storeEntry(enabler.name, +instancesScore
				+ "/40 points for page creating_instances");
		localScore = localScore + instancesScore;
		// if (localScore==0)
		// logger.warn("0 (zero) points for creating instances tab: \n" );
		feature_metrics.put(enabler.name, 2, localScore);
		return localScore;
	}

	private int ft03_measureDocumentation() // TODO: next incremental level of
										// precision would be to count/score
										// links
	{
		int localScore = 0;
		String s[] = { "User and Programmer guides",
				"User's and Programmer's guides",
				"Installation and Administration guides", "Tutorials" };

		attributes.put("catalogue.documentation", 9);

		for (int i = 0; i < s.length; i++) {
			if (StringUtil.matchRegex(enabler.documentation, (s[i])))
				localScore += 3;
		}

		protocol.storeEntry(enabler.name,
				+localScore + "/9 points for page documentation");

		feature_metrics.put(enabler.name, 3, localScore);
		return localScore;
	}

	private int ft04_measureDownloads() {
		int localScore = 0;
		// simplest measurable value is here to check the must have GitHub
		// reference. (with more precision, for links to GitHub)
		attributes.put("catalogue.downloads.github_mentioned", 10);
		if (StringUtil.matchRegex(enabler.downloads.toLowerCase(),
				"github"))
			localScore += 10;

		protocol.storeEntry(enabler.name,
				+localScore + "/10 points for page downloads");
		feature_metrics.put(enabler.name, 4, localScore);
		return localScore;
	}

	private int ft05_measureInstances() {

		// 0p This Catalogue entry intentionally does not list any instance.
		// 2 p for linking at least one instance (future: +5 per working); max. 10p
		// instance, e.g. -10 p for not available instance)
		//
		//
		int localScore = 0;
		
		String r ="(https?|http|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		Pattern pattern=Pattern.compile(r);
		Matcher  matcher = pattern.matcher(enabler.instances);

	    int count = 0;
	    while (matcher.find())
	        count++;

	    //System.out.println("FT 05 - found urls for instances: " + count);
	    localScore=2*count;
	    if (localScore>10)
	    	localScore=10;
		
		//System.out.println(enabler);
		attributes.put("instances.listed", 10);

		feature_metrics.put(enabler.name, 5, localScore);
		return localScore;
	}

	private int ft06_measureTermsConditions() {

		// System.out.println(enabler.name + " : " + new String(new
		// char[60-enabler.name.length()]).replace("\0", " ")
		// +enabler.terms_conditions.replaceAll("\n", " "));
		String[] relevantNGrams = {
				"PPP Projects being part of the FIWARE PPP program can use",
				"Experimentation/testing within the scope of the FIWARE PPP Projects",
				"under the conditions established in the FIWARE PPP Collaboration Agreement that they should have signed as beneficiaries of the program",
				"testing and experimentation of applications using: experimental instances deployed",
				"versions of the software downloaded from resources",
				"open to negotiate bi-lateral commercial",
				"External Availability Software associated",
				"product software in order to fix a bug or incorporate enhancements",
				"considered a derivative work of the product",
				"Please check the specific terms and conditions linked",
				"Please note that software derived as a result of modifying the source code",
				"Experimental instances deployed on the FIWARE Open Innovation Lab",
				"open source license",
				"entitled to offer support services to third parties",
				"versions of the software downloaded from resources",
				"otherwise unmodified version of existing software",
				"current strategy plans contemplate the commercialization",
				"provided as open source", "within the scope",
				"allows maximum reuse, contribution"

		};
		int localScore = 0;
		for (int i = 0; i < relevantNGrams.length; i++) {
			if (StringUtil.matchRegex(enabler.terms_conditions,
					relevantNGrams[i])) {
				localScore++;
			}
		}

		if (localScore < 3)
			logger.warn("ver low ngram score for Terms & Conditions:"
					+ localScore + " for " + enabler.name);

		attributes.put("catalogue.terms_conditions.relevant_ngrams",
				relevantNGrams.length);
		protocol.storeEntry(enabler.name, +localScore + "/"
				+ relevantNGrams.length
				+ " points for matching relevant N-Grams on the Terms&Conditions page");
		
		feature_metrics.put(enabler.name, 6, localScore);
		return localScore;
	}

	private void calculateMaximumPoints() {
		int sum = 0;
		// logger.debug("print attributes and points");
		for (int f : attributes.values()) {
			// logger.debug("adding " +f);
			sum += f;
		}
		// logger.info("registered max points for Catalogue: " + sum);
		maximumPoints = sum;

	}

	/** Writes report with metrics QA_DOCS_METRICS_FILENAME 
	 */
	public void printAttributes() {
		String out = "";
		TreeSet<String> keys = new TreeSet<String>(attributes.keySet());

		for (Iterator<String> iterator = keys.iterator(); iterator
				.hasNext();) {
			String n = (String) iterator.next();
			Integer v = attributes.get(n);

			String entry = n + Configuration.CSV_SEPARATOR + v;
			out = out + entry + "\n";
			
			

		}

		// write a file; TODO: make a configuration option or implement further
		// data flow
		try {
			FileUtils.writeStringToFile(
					new File(Configuration.QA_DOCS_METRICS_FILENAME), out);
		} catch (IOException e) {
			// TODO add log entry
			e.printStackTrace();
		}
	}

	public void setLogCollector(PlainMetricProtocol protocol) {
		this.protocol = protocol;

	}

	public int measureMeta() {
		int localScore = 0;
		int minimalAcceptedContent=0;
		attributes.put("catalogue.meta.top_menu_bar_ok", 5);
		int topBarScore = 0;
		
		if (enabler.overview.length() > minimalAcceptedContent) {
			topBarScore++;
		}
		if (enabler.creating_instances.length() > minimalAcceptedContent) {
			topBarScore++;
		}
		if (enabler.documentation.length() > minimalAcceptedContent) {
			topBarScore++;
		}
		
		if (enabler.instances.length() > minimalAcceptedContent) {
			topBarScore++;
		}
		if (enabler.terms_conditions.length() > minimalAcceptedContent) {
			topBarScore++;
		}
		
		
		protocol.storeEntry(enabler.name, topBarScore
				+ "/5 points for providing items in the top bar (not scoring 'Downloads'.");
		localScore = localScore + topBarScore;

		attributes.put("catalogue.meta.chapter_mentioned", 5);
		attributes.put("catalogue.meta.version_mentioned", 5);
		attributes.put("catalogue.meta.valid_date", 10); // "recently updated
															// e.g. not later
															// than one year
															// before from today
		attributes.put("catalogue.meta.valid_contact", 5); // contact person is
															// mentioned

		attributes.put("catalogue.meta.valid_email", 10); // there is a valid
															// email address

		// System.out.println(enabler.meta);

		/* check chapter */
		String chapter = StringUtil.extractValue(enabler.meta,
				"Chapter:\\s");
		if (chapter == null || chapter == "") {
			protocol.storeEntry(enabler.name,
					"0/5 points for providing a valid chapter name (no chapter provided).");
		} else if (Configuration.FIWARE_CHAPTERS.contains(chapter)) {
			protocol.storeEntry(enabler.name,
					"5/5 points for providing a valid chapter name.");
		} else {
			protocol.storeEntry(enabler.name,
					"0/5 points for providing a valid chapter name. -> unknown chapter: "
							+ chapter);

		}

		/* check version */

		String version = StringUtil.extractValue(enabler.meta,
				"Version:\\s");
		if (version == null || version == "") {
			protocol.storeEntry(enabler.name,
					"0/5 points for providing a valid software release version (no version at all could be identified).");
		} else if (StringUtil.matchSoftwareVersion(version)) {
			protocol.storeEntry(enabler.name,
					"5/5 points for providing a valid software release version.");
		} else {
			protocol.storeEntry(enabler.name,
					"1/5 points for providing a valid software release version (some version seems to be provided but not in valid format \"A.B.C\": "
							+ version + ")");

		}

		// logger.info(" MATCHED:" + version + "/ " +
		// StringServices.matchSoftwareVersion(version));

		/* check whether valid recent date is provided */
		if (measureMeta_checkDate(enabler.meta))
			localScore += 10; // this subroutine does the protocol record

		/* check for valid email */

		int valid_emails = 0;

		Matcher m = Pattern
				.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+")
				.matcher(enabler.meta);
		while (m.find()) {
			// System.out.println(m.group());
			valid_emails++;
		}

		if (valid_emails > 0) {
			protocol.storeEntry(enabler.name,
					"10/10 points for a valid contact email");
			localScore += 10;

		} else
			protocol.storeEntry(enabler.name,
					"0/10 points for providing a valid contact email");

		/* check for valid contact person mentioning */
		boolean match_name = false;
		String pattern = "Contact Person:\\s";
		String[] lines = enabler.meta.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (StringUtil.matchRegex(lines[i], pattern)) {
				// System.out.println(lines[i]);
				String name = lines[i].replaceAll(pattern, "").trim();
				if (name.length() > 2)
					match_name = true;
			}
		}
		if (match_name) {
			protocol.storeEntry(enabler.name,
					"10/10 points for mentioning a contact person");

			localScore += 10;
		} else
			protocol.storeEntry(enabler.name,
					"0/10 points for mentioning a contact person");

		feature_metrics.put(enabler.name, 0, localScore);
		return localScore;
	}

	private boolean measureMeta_checkDate(String meta) {

		boolean match = false;
		Matcher m1 = Pattern.compile("Updated:\\s+\\d{4}-\\d{2}-\\d{2}")
				.matcher(meta);
		while (m1.find()) {
			match = true;

		}

		if (!match) {

			protocol.storeEntry(enabler.name,
					"0/10 points for providing a valid recent date. "
							+ "(no 'Updated: YYYY-MM-DD' string provided/identified) "
							+ "\n" + enabler.meta);
			return false;
		}

		Matcher m2 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(meta);
		DateTime today = new DateTime();
		int dateCounter = 0;
		while (m2.find()) {

			dateCounter++;
			if (dateCounter > 1)
				logger.warn("");

			DateTime enablerDate = new DateTime(m2.group());
			int diff = Days.daysBetween(enablerDate.toLocalDate(),
					today.toLocalDate()).getDays();

			if (diff > 0 && diff <= 365) {
				protocol.storeEntry(enabler.name,
						"10/10 points for providing a valid recent date: "
								+ enablerDate.toString() + " - " + diff
								+ " days since last update.");

			} else {
				String msg = "enabler not updated since more than 1 year: "
						+ diff + " days since last update ("
						+ enablerDate.toString() + ")";

				protocol.storeEntry(enabler.name,
						"0/10 points for providing a valid recent date. "
								+ msg);
				return false;
			}

		}

		return false;

	}

}
