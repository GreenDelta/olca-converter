package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * 
 * @author Imo Graf
 * 
 */
@Context(name = "requiredContext", parentName = "validProperties")
public class ES2PropertyRequiredContext extends ContextObject{

	@ContextField(name = "requiredContext", attributeName = "majorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int majorRelease = 0;

	@ContextField(name = "requiredContext", attributeName = "minorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int minorRelease = 0;

	@ContextField(name = "requiredContext", attributeName = "majorRevision", isAttribute = true, isRequired = false, type = Type.Integer)
	public int majorRevision = 0;

	@ContextField(name = "requiredContext", attributeName = "miniorRevision", isAttribute = true, isRequired = false, type = Type.Integer)
	public int miniorRevision = 0;

	@ContextField(name = "requiredContext", attributeName = "requiredContextFileLocation", isAttribute = true)
	public String requiredContextFileLocation;

	@ContextField(name = "requiredContext", attributeName = "requiredContextId", isAttribute = true, isRequired = true, length = 36)
	public String requiredContextId;

	@ContextField(name = "requiredContextName", parentName = "requiredContext", isMultiple = true, isRequired = true, length = 80, type = Type.MultiLangText)
	private List<LangString> requiredContextName = new ArrayList<LangString>();

	public List<LangString> getNames() {
		return requiredContextName;
	}
}
