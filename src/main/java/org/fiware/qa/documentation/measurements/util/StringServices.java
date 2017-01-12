package org.fiware.qa.documentation.measurements.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fiware.qa.documentation.measurements.Configuration;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

public class StringServices {

	public static String extractValue(String subject, String searchKey) {

		String[] lines = subject.split("\n");
		for (int i = 0; i < lines.length; i++) {

			if (matchRegex(lines[i], searchKey)) {
				// System.out.println(lines[i]);
				String value = lines[i].replaceAll(searchKey, "").trim();
				return value;
			}
		}
		return "";
	}

	public static String findRegex(String subject, String regex) {

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(subject);
		while (m.find()) {
			return m.group();
		}
		return "";

	}

	public static boolean matchRegex(String subject, String regex) {

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(subject);
		return m.find();

	}

	public static boolean matchSoftwareVersion(String v) {

		v = v.trim();
		String regex = "^(\\d+.)+(\\d)+$";
		boolean result = matchRegex(v, regex);

		return result;

	}

	public static String getTodayIsoDate() {

		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd");
		Date today = new Date();
		return dateFormatter.format(today);

	}

	public static String getDataFileDate() {
		long lastModified = new File(Configuration.INPUT_CATALOGUE_DATA)
				.lastModified();
		SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date(lastModified);
		return iso.format(d);

	}

}
