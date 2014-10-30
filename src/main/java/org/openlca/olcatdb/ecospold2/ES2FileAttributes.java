package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.masterdata.ES2Context;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The file attributes element of EcoSpold 02.
 * 
 * @author Michael Srocka
 * 
 */
@Context(name = "fileAttributes", parentName = "administrativeInformation")
public class ES2FileAttributes extends ContextObject {

	public ES2FileAttributes() {
		
	}

	
	public ES2FileAttributes(boolean setDefaults) {
		internalSchemaVersion = "2.0.8";
	}

	@ContextField(name = "fileAttributes", parentName = "administrativeInformation", isAttribute = true, attributeName = "contextId", isMaster = true, classGroup = ES2Context.class)
	public String contextId;

	@ContextField(name = "fileAttributes", parentName = "administrativeInformation", isAttribute = true, attributeName = "creationTimestamp")
	public String creationTimestamp;

	@ContextField(name = "fileAttributes", parentName = "administrativeInformation", isAttribute = true, attributeName = "defaultLanguage")
	public String defaultLanguage = "en";

	@ContextField(name = "fileAttributes", parentName = "administrativeInformation", isAttribute = true, attributeName = "fileGenerator")
	public String fileGenerator;

	@ContextField(name = "fileAttributes", parentName = "administrativeInformation", isAttribute = true, attributeName = "fileTimestamp")
	public String fileTimestamp;

	@ContextField(name = "fileAttributes", parentName = "administrativeInformation", isAttribute = true, attributeName = "internalSchemaVersion")
	public String internalSchemaVersion;

	@ContextField(name = "fileAttributes", parentName = "administrativeInformation", isAttribute = true, attributeName = "lastEditTimestamp")
	public String lastEditTimestamp;

	@ContextField(name = "fileAttributes", parentName = "administrativeInformation", isAttribute = true, attributeName = "majorRelease", type = Type.Integer)
	public int majorRelease;

	@ContextField(name = "fileAttributes", parentName = "administrativeInformation", isAttribute = true, attributeName = "minorRelease", type = Type.Integer)
	public int minorRelease;

	@ContextField(name = "fileAttributes", parentName = "administrativeInformation", isAttribute = true, attributeName = "majorRevision", type = Type.Integer)
	public Integer majorRevision;

	@ContextField(name = "fileAttributes", parentName = "administrativeInformation", isAttribute = true, attributeName = "majorRevision", type = Type.Integer)
	public Integer minorRevision;

	@ContextField(name = "contextName", parentName = "fileAttributes", isMultiple = true, type = Type.MultiLangText, classGroup = ES2Context.class)
	private List<LangString> contextNames = new ArrayList<LangString>();

	public List<LangString> getContextNames() {
		return contextNames;
	}

	@SubContext(contextClass = ES2RequiredContext.class, isMultiple = true)
	private List<ES2RequiredContext> RequiredContexts = new ArrayList<ES2RequiredContext>();

	public List<ES2RequiredContext> getRequiredContexts() {
		return RequiredContexts;
	}

}
