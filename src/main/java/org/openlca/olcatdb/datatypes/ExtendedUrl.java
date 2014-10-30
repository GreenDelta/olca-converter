package org.openlca.olcatdb.datatypes;

/**
 * A wrapped URL string used for the {@link TextAndImage} type in the EcoSpold
 * 02 format. 
 */
public class ExtendedUrl {

	/**
	 * The index of the URL in the text and image context.
	 */
	private int index;

	/**
	 * The wrapped URL.
	 */
	private String url;

	public ExtendedUrl() {

	}

	public ExtendedUrl(int index, String url) {
		this.index = index;
		this.url = url;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
