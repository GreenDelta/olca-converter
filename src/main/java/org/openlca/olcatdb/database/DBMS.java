package org.openlca.olcatdb.database;

public class DBMS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		org.hsqldb.util.DatabaseManager.main(new String[]{
				"--driver", "org.hsqldb.jdbcDriver",
				"--url", "jdbc:hsqldb:file:database/database", 
				"--user", "sa"});

	}

}
