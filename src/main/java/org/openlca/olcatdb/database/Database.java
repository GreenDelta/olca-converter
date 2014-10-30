package org.openlca.olcatdb.database;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.hsqldb.jdbcDriver;

/**
 * 
 * The embedded database for semantic mapping.
 * 
 * @author Michael Srocka
 * 
 */
public class Database {

	/**
	 * The singleton instance of this class.
	 */
	private static Database inst;

	/**
	 * The database connection.
	 */
	private Connection con;

	private Database() throws Exception {

		// initialize tables etc.
		File dbDir = new File("database");
		if (!dbDir.exists()) {
			dbDir.mkdirs();
		}
		String[] resources = new String[] {
				// the DB script
				"database.script",
				// the DB properties
				"database.properties",

				// the ILCD unit groups
				"ILCD_UNIT_GROUPS.csv",
				// the ILCD compartments
				"ILCD_COMPARTMENTS.csv",
				// the ILCD flow properties
				"ILCD_FLOW_PROPERTIES.csv",
				// the ILCD elementary flows
				"ILCD_ELEM_FLOWS.csv",

				// the EcoSpold 01 compartments
				"ES1_COMPARTMENTS.csv",
				// the EcoSpold 01 elementary flows
				"ES1_ELEM_FLOWS.csv",

				// the EcoSpold 02 units
				"ES2_UNITS.csv",
				// the EcoSpold 02 compartments
				"ES2_COMPARTMENTS.csv",
				// the EcoSpold 02 elementary flows
				"ES2_ELEM_FLOWS.csv",
				// the EcoSpold 02 geographies
				"ES2_GEOGRAPHIES.csv",

				// flow map: EcoSpold 01 -> ILCD
				"FLOW_MAP_ES1_TO_ILCD.csv",
				// flow map: EcoSpold 02 -> ILCD
				"FLOW_MAP_ES2_TO_ILCD.csv",
				// flow map: ILCD -> EcoSpold 01
				"FLOW_MAP_ILCD_TO_ES1.csv",
				// flow map: ILCD -> EcoSpold 02
				"FLOW_MAP_ILCD_TO_ES2.csv",

				// compartment map: EcoSpold 01 -> ILCD
				"COMPARTMENT_MAP_ES1_TO_ILCD.csv",
				// compartment map: EcoSpold 01 -> EcoSpold 02
				"COMPARTMENT_MAP_ES1_TO_ES2.csv",
				// compartment map: EcoSpold 02 -> ILCD
				"COMPARTMENT_MAP_ES2_TO_ILCD.csv",
				// compartment map: ILCD -> EcoSpold 02
				"COMPARTMENT_MAP_ILCD_TO_ES2.csv",
				// compartment map: ILCD -> EcoSpold 01
				"COMPARTMENT_MAP_ILCD_TO_ES1.csv",

				// unit map: ILCD -> EcoSpold 02
				"UNIT_MAP_ILCD_TO_ES2.csv",
				// unit map: EcoSpold 01 -> ILCD
				"UNIT_MAP_ES1_TO_ILCD.csv",
				// unit map: EcoSpold 02 -> ILCD
				"UNIT_MAP_ES2_TO_ILCD.csv",

				"ES2_TO_CSV_COMPARTMENT_MAP.csv",
				"ES2_TO_CSV_ELECTRICITY_UNITS.csv",
				"ES2_TO_CSV_GEOGRAPHY_MAP.csv", };
		// copy required resources
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

		// create the connection
		DriverManager.registerDriver(new jdbcDriver());
		con = DriverManager.getConnection("jdbc:hsqldb:file:database/database",
				"sa", "");
	}

	/**
	 * Get the singleton instance from the database.
	 */
	public static Database getInstance() throws Exception {
		if (inst == null) {
			inst = new Database();
		}
		return inst;
	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
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
	public void close() throws Exception {
		con.createStatement().execute("SHUTDOWN");
	}
}
