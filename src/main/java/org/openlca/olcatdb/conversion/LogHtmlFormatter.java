package org.openlca.olcatdb.conversion;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * The HTML formatter for the conversion logs.
 * 
 */
public class LogHtmlFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {

		StringBuffer buffer = new StringBuffer();

		// create a new table row
		buffer.append("<tr>");

		// the level column
		buffer.append("<td>");
		String color = null;
		if (record.getLevel() == Level.INFO) {
			color = "green";
		} else if (record.getLevel() == Level.WARNING) {
			color = "#e3bf14";
		} else if (record.getLevel() == Level.SEVERE) {
			color = "red";
		} else {
			color = "black";
		}
		buffer.append("<span style='color:" + color + ";'><b>");
		buffer.append(record.getLevel().getName());
		buffer.append("</b></span>");
		buffer.append("</td>");

		// the message column
		buffer.append("<td>");
		buffer.append(record.getMessage());
		buffer.append("</td>");

		buffer.append("</tr>\n");

		return buffer.toString();
	}

	@Override
	public String getHead(Handler h) {
		// create the html-header
		String header = "<html>\n<head>\n" + (new Date()).toString()
				+ "\n</head>\n<body style='font-family:sans-serif'>\n<table width='100%'>\n"
				+ "<tr><th align='left'>Level</th><th align='left'>Message</th></tr>\n";
		return header;
	}

	@Override
	public String getTail(Handler h) {
		// create the html-footer
		String footer = "</table>\n</body>\n</html>\n";
		return footer;
	}
}
