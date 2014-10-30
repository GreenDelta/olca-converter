package org.openlca.olcatdb.conversion.es2tocsv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.greendeltatc.simapro.csv.model.types.Geography;

public final class ES2MappingFileUtil {

	/**
	 * 
	 * @param file
	 * @return
	 * @throws java.io.IOException
	 */
	private static List<List<String>> readCSVFile(File file) throws IOException {
		List<List<String>> list = new ArrayList<List<String>>();

		CsvListReader csvReader = new CsvListReader(new BufferedReader(
				new InputStreamReader(new FileInputStream(file),
						Charset.forName("UTF-8"))),
				CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		List<String> line;
		while (null != (line = csvReader.read())) {
			List<String> temp = new ArrayList<String>();
			temp.addAll(line);
			list.add(temp);
		}
		csvReader.close();

		return list;
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws java.io.IOException
	 */
	public static Map<String, Geography> readGeography(File file)
			throws IOException {
		List<List<String>> lines = readCSVFile(file);
		return readGeography(lines);
	}

	/**
	 * 
	 * @param lines
	 * @return
	 */
	public static Map<String, Geography> readGeography(List<List<String>> lines) {
		Map<String, Geography> map = new HashMap<String, Geography>();
		for (List<String> line : lines) {
			map.put(line.get(0), Geography.getInstances().get(line.get(1)));
		}
		return map;
	}

	/**
	 * 
	 * @return
	 * @throws java.io.IOException
	 */
	public static List<CompartmentModel> readCompartments(File file)
			throws IOException {
		List<List<String>> lines = readCSVFile(file);
		return readCompartments(lines);
	}

	public static List<CompartmentModel> readCompartments(
			List<List<String>> lines) {
		List<CompartmentModel> list = new ArrayList<CompartmentModel>();
		for (List<String> line : lines) {
			CompartmentModel model = new CompartmentModel();
			model.setEs2Compartment(line.get(0));
			model.setEs2Subcompartment(line.get(1));
			model.setSimaProElemType(line.get(2));
			model.setSimaProSubcompartment(line.get(3));
			list.add(model);
		}

		return list;
	}

	/**
	 * Read the electricity unit file.
	 * 
	 * @param file
	 * @throws java.io.IOException
	 */
	public static List<String> readElecricityUnits(File file)
			throws IOException {
		List<List<String>> lines = readCSVFile(file);
		return readElecricityUnits(lines);
	}

	public static List<String> readElecricityUnits(List<List<String>> lines) {
		List<String> list = new ArrayList<String>();

		for (List<String> line : lines) {
			list.add(line.get(0));
		}

		return list;
	}

	public static void writeCSVFile(File file, List<List<String>> rows)
			throws IOException {
		StringWriter stringWriter = new StringWriter();
		CsvListWriter csvWriter = new CsvListWriter(stringWriter,
				CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		for (List<String> list : rows) {
			csvWriter.write(list);
		}
		csvWriter.close();

		PrintWriter printWriter = new PrintWriter(file);
		printWriter.println(stringWriter.toString());
		stringWriter.close();
		printWriter.close();
	}

}
