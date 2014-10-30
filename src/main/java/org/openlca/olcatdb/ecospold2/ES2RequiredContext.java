package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * 
 * @author Imo Graf
 * 
 */
@Context(name = "requiredContext", parentName = "fileAttributes")
public class ES2RequiredContext {

	@ContextField(name = "requiredContext", parentName = "fileAttributes", isAttribute = true, attributeName = "majorRelease", type = Type.Integer)
	public int majorRelease;

	@ContextField(name = "requiredContext", parentName = "fileAttributes", isAttribute = true, attributeName = "minorRelease", type = Type.Integer)
	public int minorRelease;

	@ContextField(name = "requiredContext", parentName = "fileAttributes", isAttribute = true, attributeName = "majorRevision", type = Type.Integer)
	public int majorRevision;

	@ContextField(name = "requiredContext", parentName = "fileAttributes", isAttribute = true, attributeName = "minorRevision", type = Type.Integer)
	public int minorRevision;

	@ContextField(name = "requiredContext", parentName = "fileAttributes", isAttribute = true, attributeName = "requiredContextId")
	public String requiredContextId;

	@ContextField(name = "requiredContext", parentName = "fileAttributes", isAttribute = true, attributeName = "requiredContextFileLocation")
	public String requiredContextFileLocation;

	@ContextField(name = "requiredContext", parentName = "fileAttributes", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> requiredContextName = new ArrayList<LangString>();

	public List<LangString> getRequiredContextName() {
		return requiredContextName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof ES2RequiredContext) {
			ES2RequiredContext context = ES2RequiredContext.class.cast(obj);
			if (context.requiredContextId != null
					&& this.requiredContextId != null
					&& context.requiredContextId.equals(this.requiredContextId))
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
}
