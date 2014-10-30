package org.openlca.olcatdb.datatypes;

import java.util.Iterator;
import java.util.List;

public class LangString {

	private String langCode;

	private String value;

	private int index = 0;

	public LangString() {
		this.langCode = "en";
		this.value = "";
	}

	public LangString(String value) {
		this.langCode = "en";
		this.value = value;
	}

	public LangString(String langCode, String value) {
		this.langCode = langCode;
		this.value = value;
	}

	public String getLangCode() {
		return langCode;
	}

	public LangString setLangCode(String langCode) {
		this.langCode = langCode;
		return this;
	}

	public String getValue() {
		return value;
	}

	public LangString setValue(String value) {
		this.value = value;
		return this;
	}

	/**
	 * Returns the first multiple language string from the given list with the
	 * given language code. If there is no element with the given language code
	 * in the list, null is returned.
	 */
	public static LangString getFirst(List<LangString> langStrings,
			String langCode) {
		LangString first = null;
		Iterator<LangString> it = langStrings.iterator();
		while (first == null && it.hasNext()) {
			LangString langString = it.next();
			if ((langCode == null && langString.langCode == null)
					|| (langCode != null && langCode
							.equals(langString.langCode))) {
				first = langString;
			}
		}
		return first;
	}

	/**
	 * Returns the first multiple language string from the given list or
	 * <code>null</code> if this list is empty.
	 */
	public static LangString getFirst(List<LangString> langStrings) {
		LangString first = null;
		if (!langStrings.isEmpty()) {
			first = langStrings.get(0);
		}
		return first;
	}

	/**
	 * Returns the value of the first multiple language string from the given
	 * list with the given language code. If there is no element with the given
	 * language code in the list, <code>null</code> is returned.
	 */
	public static String getFirstValue(List<LangString> langStrings,
			String langCode) {
		String v = null;
		LangString lString = getFirst(langStrings, langCode);
		if (lString != null)
			v = lString.getValue();
		return v;
	}

	/**
	 * Returns the value of the first multiple language string from the given
	 * list. If the list is empty, <code>null</code> is returned.
	 */
	public static String getFirstValue(List<LangString> langStrings) {
		String val = null;
		if (!langStrings.isEmpty()) {
			val = langStrings.get(0).value;
		}
		return val;
	}

	/**
	 * Returns true if the given list of language strings contains at least one
	 * language string with the given value.
	 */
	public static boolean contains(List<LangString> langStrings, String val) {
		boolean b = false;
		for (LangString lStr : langStrings) {
			if (val == null && lStr.value == null) {
				b = true;
				break;
			} else if (lStr.value != null && lStr.value.equals(val)) {
				b = true;
				break;
			}
		}
		return b;
	}

	/**
	 * Adds the values of the source list to the target list. If there is no
	 * value for a language code from the source list the value is added to the
	 * target list. Otherwise the values are concatenated using the given
	 * separator.
	 */
	public static void concat(List<LangString> source, List<LangString> target,
			String separator) {
		for (LangString sourceString : source) {
			LangString targetString = getFirst(target, sourceString.langCode);
			if (targetString == null) {
				target.add(sourceString);
			} else if (targetString.value != null) {
				targetString.value = targetString.value.concat(separator)
						.concat(sourceString.value);
			} else {
				targetString.value = sourceString.value;
			}
		}
	}

	public int getIndex() {
		return index;
	}

	public LangString setIndex(int index) {
		this.index = index;
		return this;
	}

	@Override
	public String toString() {
		return "MultiLangString [index=" + index + ", "
				+ (langCode != null ? "langCode=" + langCode + ", " : "")
				+ (value != null ? "value=" + value : "") + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LangString && obj != null) {
			LangString ls = LangString.class.cast(obj);
			boolean valueIsEqual = (ls.value != null && this.value != null
					&& this.value.equals(ls.value) || (this.value == null && ls.value == null));
			boolean codeIsEqual = (ls.langCode != null && this.langCode != null
					&& this.langCode.equals(ls.langCode) || (this.langCode == null && ls.langCode == null));

			if (valueIsEqual && codeIsEqual)
				return true;
			else
				return false;

		} else {
			return false;
		}
	}
}
