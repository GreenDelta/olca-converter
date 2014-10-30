package org.openlca.olcatdb.datatypes;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;

/**
 * Some helper functions for formatting date and time types to XML strings.
 * 
 * @author Michael Srocka
 * 
 */
public class Time {

	/**
	 * Returns the current system time as XML formatted string.
	 */
	public static String now() {
		String now = null;
		GregorianCalendar calendar = new GregorianCalendar();
		try {
			now = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(calendar).toXMLFormat();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * Returns the time for the given date as XML formatted string.
	 */
	public static String forDate(Date date) {
		String time = null;
		try {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			time = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(calendar).toXMLFormat();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * Returns the date for the given XML formatted string.
	 */
	public static Date parseString(String xmlDate) {
		Date date = null;
		try {
			date = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(xmlDate).toGregorianCalendar()
					.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
}
