package org.openlca.olcatdb.database;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.hsqldb.jdbcDriver;

public class Database {

	private static File folder;
	private static Database inst;
	private Connection con;

	public static Database getInstance() throws Exception {
		if (inst == null) {
			inst = new Database(getFolder());
		}
		return inst;
	}

	private Database(File folder) throws Exception {
		if (!folder.exists()) {
			folder.mkdirs();
		}
		String[] resources = getDatabaseFiles();
		initDatabaseDir(folder, resources);
		DriverManager.registerDriver(new jdbcDriver());
		String path = folder.getAbsolutePath().replace('\\', '/');
		String url = "jdbc:hsqldb:file:" + path + "/database";
		con = DriverManager.getConnection(url, "sa", "");
	}

	/**
	 * Returns the folder of the database. If no database location is set
	 * explicitly the default location is the folder 'database' in the directory
	 * where the converter is running (see also #setDatabase).
	 */
	public static File getFolder() {
		if (folder == null)
			folder = new File("database");
		return folder;
	}

	/**
	 * Set a new database location. If the database was already activated it
	 * will close the database and reactivate it at the new location.
	 */
	public static void setFolder(File newFolder) throws Exception {
		folder = newFolder;
		if(inst == null)
			return;
		close();
		inst = new Database(newFolder);
	}

	private void initDatabaseDir(File dbDir, String[] resources) throws IOException {
		for (String res : resources) {
			File f = new File(dbDir, res);
			if (!f.exists()) {
				f.createNewFile();
				InputStream is = Database.class.getResourceAsStream(res);
				BufferedInputStream in = new BufferedInputStream(is);
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(f));
				int b = -1;
				while ((b = in.read()) > -1) {
					out.write(b);
				}
				out.flush();
				out.close();
				in.close();
			}
		}
	}

	private String[] getDatabaseFiles() {
		return new String[]{
				"database.script",
				"database.properties",

				"ILCD_UNIT_GROUPS.csv",
				"ILCD_COMPARTMENTS.csv",
				"ILCD_FLOW_PROPERTIES.csv",
				"ILCD_ELEM_FLOWS.csv",

				"ES1_COMPARTMENTS.csv",
				"ES1_ELEM_FLOWS.csv",

				"ES2_UNITS.csv",
				"ES2_COMPARTMENTS.csv",
				"ES2_ELEM_FLOWS.csv",
				"ES2_GEOGRAPHIES.csv",

				"FLOW_MAP_ES1_TO_ILCD.csv",
				"FLOW_MAP_ES2_TO_ILCD.csv",
				"FLOW_MAP_ILCD_TO_ES1.csv",
				"FLOW_MAP_ILCD_TO_ES2.csv",

				"COMPARTMENT_MAP_ES1_TO_ILCD.csv",
				"COMPARTMENT_MAP_ES1_TO_ES2.csv",
				"COMPARTMENT_MAP_ES2_TO_ILCD.csv",
				"COMPARTMENT_MAP_ILCD_TO_ES2.csv",
				"COMPARTMENT_MAP_ILCD_TO_ES1.csv",

				"UNIT_MAP_ILCD_TO_ES2.csv",
				"UNIT_MAP_ES1_TO_ILCD.csv",
				"UNIT_MAP_ES2_TO_ILCD.csv",

				"ES2_TO_CSV_COMPARTMENT_MAP.csv",
				"ES2_TO_CSV_ELECTRICITY_UNITS.csv",
				"ES2_TO_CSV_GEOGRAPHY_MAP.csv"
		};
	}


	public ResultSet query(String sql) throws Exception {
		return con.createStatement().executeQuery(sql);
	}

	/**
	 * Creates a prepared statement on the database. The client calling this
	 * method is responsible for closing this statement.
	 */
	public PreparedStatement preparedStatement(String sql) throws Exception {
		return con.prepareStatement(sql);
	}

	/**
	 * Close the current instance of the database. After this queries on this
	 * database are not allowed any more. However, you can get a new database
	 * instance by calling the {@link #getInstance()} method.
	 */
	public static void close() throws Exception {
		if(inst == null)
			return;
		inst.con.createStatement().execute("SHUTDOWN");
	}
}
