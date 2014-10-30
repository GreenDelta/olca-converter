package org.openlca.olcatdb;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.openlca.olcatdb.swing.ConversionWindow;

/**
 * The entry point of the application.
 * 
 * @author Michael Srocka
 * @author Imo Graf
 * 
 */
public class Main {

	/**
	 * Run a conversion or open the application window.
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {

			// set the look and feel
			// if the Nimbus look and feel is available it is selected
			// otherwise the platforms look and feel is selected
			try {
				String lookAndFeel = null;
				for (LookAndFeelInfo info : UIManager
						.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						lookAndFeel = info.getClassName();
						break;
					}
				}
				if (lookAndFeel == null) {
					lookAndFeel = UIManager.getSystemLookAndFeelClassName();
				}
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (Exception e) {
				e.printStackTrace();
			}

			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					ConversionWindow.getInstance().setVisible(true);
				}
			});

		} else {
			// interpret the command line arguments
			if (args[0] != null
					&& (args[0].equals("-h") || args[0].equals("-help"))) {
				printHelp();

			}
		}

	}

	/**
	 * Prints the allowed arguments for the command line control.
	 */
	private static void printHelp() {
		System.out.println("Converter options:");
		System.out.println("-h | -help : prints this message");
		System.out.println("-c -from <path> -to <path> -format <format> "
				+ ": converts an XML or ZIP file from the given "
				+ "path to the give directory using one of the "
				+ "allowed formats: EcoSpold1, EcoSpold2, ILCD");
	}

}
