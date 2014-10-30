package org.openlca.olcatdb.swing;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.net.URI;

public class Browser {

	public static boolean open(URI uri) {
		boolean opened = false;
		try {
			if(Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if(desktop.isSupported(Action.BROWSE)) {
					desktop.browse(uri);
					opened = true;
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return opened;
	}	
}
