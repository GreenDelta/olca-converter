package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextField.Type;
import org.openlca.olcatdb.parsing.ContextObject;

/**
 * 
 * @author Imo Graf
 * 
 */

@Context(name = "language", parentName = "validLanguages")
public class ES2Language extends ContextObject {

	@ContextField(name = "language", parentName = "validLanguages", isAttribute = true, attributeName = "code")
	public String code;

	@ContextField(name = "comment", parentName = "language", isMultiple = true, length = 32000, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	public List<LangString> getComment() {
		return comment;
	}
}
