package com.greendeltatc.simapro.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import com.greendeltatc.simapro.csv.model.SPDataSet;

/**
 * Parse a SimaPro CSV and returned one {@link com.greendeltatc.simapro.csv.model.SPDataSet} for each process
 * 
 * @author Imo Graf
 * 
 */
public class CSVParser {
	private SPDataSet dataSet;
	private BufferedReader reader;
	private File csvfile;
	private boolean run;

	public CSVParser(File csvfile) {
		this.csvfile = csvfile;
	}

	/**
	 * Read a part from the csv file. Starts with the given String and stop with
	 * the the line equals 'End'
	 * 
	 * @param part
	 * @return
	 * @throws java.io.IOException
	 */
	private StringReader readPart(String part) throws IOException {
		boolean read = true;
		boolean start = false;
		String process = null;
		String line;

		while ((line = reader.readLine()) != null && read) {
			if (line.equals(part)) {
				start = true;
			}

			if (start)
				if (line.equals(part)) {
					process = line + "\n";
				} else {
					process += line + "\n";
				}

			if (line.equals("End")) {
				read = false;
			}
		}

		if (process != null) {
			return new StringReader(process);
		} else {
			reader.close();
			reader = null;
			run = false;
			return null;
		}
	}

	/**
	 * 
	 * @return {@link com.greendeltatc.simapro.csv.model.SPDataSet} which was read straight
	 */
	public SPDataSet getDataSet() {
		return dataSet;
	}

	/**
	 * Start the reading from the given file.
	 * 
	 * @throws java.io.IOException
	 */
	public void start() throws IOException {
		if (reader != null) {
			reader.close();
		}
		reader = new BufferedReader(new FileReader(csvfile));
		run = true;
	}

	/**
	 * Read the next process.
	 * 
	 * @return true if the new current process is valid; false if there are no
	 *         more processes
	 * @throws java.io.IOException
	 *             , IllegalArgumentException
	 */
	public boolean next() throws IOException, IllegalArgumentException {
		if (!run)
			throw new IllegalArgumentException("Reading was not started.");
		CSVReader csvReader = new CSVReader();
		StringReader reader = readPart("Process");
		if (reader != null) {
			try {
				dataSet = csvReader.read(reader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			dataSet = null;
		}

		if (dataSet != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Count the process from the given file.
	 * 
	 * @return
	 * @throws java.io.IOException
	 */
	public int count() throws IOException {
		int count = 0;
		start();
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.equals("Process")) {
				count++;
			}
		}
		run = false;
		return count;
	}

	/**
	 * Read only the general data from the csv file.
	 * 
	 * @return {@link com.greendeltatc.simapro.csv.model.SPDataSet}
	 * @throws java.io.IOException
	 * 
	 */
	public SPDataSet readGeneralData() throws IllegalArgumentException,
			IOException {
		start();

		StringReader reader;
		CSVReader csvReader = new CSVReader();
		SPDataSet dataSet = new SPDataSet("");
		
		// TODO

		return dataSet;
	}

}
