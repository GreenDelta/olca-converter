package org.openlca.olcatdb.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * The text and image type used in the EcoSpold 02 format.
 * 
 * @author Michael Srocka
 * 
 */
public class TextAndImage {

	/**
	 * The list of text entries.
	 */
	private List<LangString> text = new ArrayList<LangString>();

	/**
	 * The list of image URLs.
	 */
	private List<ExtendedUrl> images = new ArrayList<ExtendedUrl>();

	/**
	 * The list of text variables.
	 */
	private List<NamedString> variables = new ArrayList<NamedString>();

	/**
	 * Creates a new text and image type.
	 */
	public TextAndImage() {
	}

	/**
	 * Creates a new text and image type.
	 */
	public TextAndImage(String text) {
		LangString langString = new LangString(text);
		langString.setIndex(0);
		this.text.add(langString);
	}

	/**
	 * The list of text entries.
	 */
	public List<LangString> getText() {
		return text;
	}

	/**
	 * The list of image URLs.
	 */
	public List<ExtendedUrl> getImages() {
		return images;
	}

	/**
	 * The list of text variables.
	 */
	public List<NamedString> getVariables() {
		return variables;
	}

	/**
	 * Returns the first text entry or <code>null</code> if the text list is
	 * empty.
	 */
	public String getFirstText() {
		String t = null;
		if (!text.isEmpty()) {
			t = text.get(0).getValue();
		}
		return t;
	}

	/**
	 * Returns the first language string from the text list, or
	 * <code>null</code> if the text list is empty.
	 */
	public LangString getFirstLangString() {
		LangString langString = null;
		if (!text.isEmpty()) {
			langString = text.get(0);
		}		
		return langString;
	}
	
	/**
	 * Returns <code>true</code> if there are text entries.
	 */
	public boolean hasText() {
		return !text.isEmpty();
	}
}
