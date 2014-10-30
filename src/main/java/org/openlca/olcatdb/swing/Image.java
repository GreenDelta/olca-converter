package org.openlca.olcatdb.swing;

import javax.swing.ImageIcon;

/**
 * A helper class for loading images.
 * 
 * @author Michael Srocka
 */
class Image {

	static ImageIcon makeIcon(String file) {
		return new ImageIcon(Image.class.getResource(file));
	}

}
