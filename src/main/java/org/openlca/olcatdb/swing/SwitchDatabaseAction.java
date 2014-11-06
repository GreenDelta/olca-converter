package org.openlca.olcatdb.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.openlca.olcatdb.database.Database;

class SwitchDatabaseAction implements ActionListener {

	private JTextField pathField;
	private JEditorPane outputPane;

	SwitchDatabaseAction(JTextField pathField, JEditorPane outputPane) {
		this.pathField = pathField;
		this.outputPane = outputPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(Database.getFolder());
		chooser.setDialogTitle("Select the database folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showOpenDialog(pathField);
		File newDir = chooser.getSelectedFile();
		if (newDir == null || newDir.equals(Database.getFolder()))
			return;
		else {
			outputPane.setText("");
			new Thread(new Switcher(newDir)).start();
		}
	}

	private void handleError(Exception e, String prefix) {
		e.printStackTrace();
		String message = prefix + ": " + e.getMessage();
		JOptionPane.showMessageDialog(pathField, message, "Error",
				JOptionPane.ERROR_MESSAGE);
		log(message);
	}

	private void log(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				String text = outputPane.getText();
				if (text == null)
					text = "";
				text += message;
				outputPane.setText(text);
			}
		});
	}

	private class Switcher implements Runnable {

		private File newDir;

		private Switcher(File newDir) {
			this.newDir = newDir;
		}

		@Override
		public void run() {
			log("Switching to new database; please wait .");
			File oldDir = Database.getFolder();
			Dotter dotter = new Dotter();
			dotter.start();
			try {
				Database.setFolder(newDir);
				pathField.setText(newDir.getAbsolutePath());
				dotter.quit();
				log("\nCheck database tables: \n");
				checkTables();
				log("\n\nAll done with no errors!");
			} catch (Exception e) {
				switchFailed(oldDir, e);
			} finally {
				dotter.quit();
			}
		}

		private void switchFailed(File oldDir, Exception e) {
			handleError(e, "Failed to switch to " + newDir.getName());
			try {
				if (oldDir != null)
					Database.setFolder(oldDir);
			} catch (Exception e2) {
				handleError(e2, "Failed to switch back to old database "
						+ oldDir.getName());
			}
		}

		private void checkTables() throws Exception {
			String[] tables = getTables();
			for (String table : tables) {
				Dotter dotter = new Dotter();
				try {
					log("\n\tCheck table " + table + " ..");
					dotter.start();
					String query = "SELECT count(*) FROM " + table;
					ResultSet result = Database.getInstance().query(query);
					result.next();
					int entries = result.getInt(1);
					dotter.quit();
					log(" " + entries + " entries");
				} catch (Exception e) {
					dotter.quit();
					throw e;
				}
			}
		}

		private String[] getTables() {
			return new String[]{"COMPARTMENT_MAP_ILCD_TO_ES1",
							"COMPARTMENT_MAP_ES2_TO_ILCD", "ES1_ELEM_FLOWS",
							"COMPARTMENT_MAP_ILCD_TO_ES2", "COMPARTMENT_MAP_ES1_TO_ILCD",
							"COMPARTMENT_MAP_ES1_TO_ES2", "ES1_COMPARTMENTS",
							"ES2_COMPARTMENTS", "ES2_ELEM_FLOWS",
							"ES2_GEOGRAPHIES", "ES2_UNITS", "FLOW_MAP_ES1_TO_ILCD",
							"FLOW_MAP_ES2_TO_ILCD", "FLOW_MAP_ILCD_TO_ES1",
							"FLOW_MAP_ILCD_TO_ES2", "ILCD_COMPARTMENTS",
							"ILCD_ELEM_FLOWS", "ILCD_FLOW_PROPERTIES",
							"ILCD_UNIT_GROUPS", "UNIT_MAP_ES1_TO_ILCD",
							"UNIT_MAP_ILCD_TO_ES2", "UNIT_MAP_ES2_TO_ILCD"};
		}
	}

	private class Dotter extends Thread {

		private boolean stopped = false;

		private void quit() {
			stopped = true;
		}

		@Override
		public void run() {
			while (!stopped) {
				log(".");
				try {
					sleep(1000);
				} catch (Exception e) {
					stopped = true;
				}
			}
		}
	}

}
