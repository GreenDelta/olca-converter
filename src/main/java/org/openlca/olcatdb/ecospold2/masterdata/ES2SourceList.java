package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The EcoSpold 2 master data file for valid sources.
 * 
 * @author Michael Srocka 
 *
 */
@Context(name="validSources")
public class ES2SourceList extends ContextObject {

	@ContextField(
		name="validSources",
		attributeName="contextId",
		isAttribute=true,
		isRequired=true,
		length=36)
	public String contextId;	
	
	@ContextField(
		name="validSources",
		attributeName="majorRelease",
		isAttribute=true,
		isRequired=true,
		type=Type.Integer)
	public int majorRelease = 1;
	
	@ContextField(
		name="validSources",
		attributeName="minorRelease",
		isAttribute=true,
		isRequired=true,
		type=Type.Integer)
	public int minorRelease = 0;
	
	@ContextField(
		name="contextName",
		parentName="validSources",
		isRequired=true,
		length=80,
		isMultiple=true,
		type=Type.MultiLangText)
	private List<LangString> contextNames 
		= new ArrayList<LangString>(); 
	
	public List<LangString> getContextNames() {
		return contextNames;
	}

	@SubContext(
			contextClass = ES2Source.class, 
			isMultiple = true)
	private List<ES2Source> sources 
		= new ArrayList<ES2Source>();

	public List<ES2Source> getSources() {
		return sources;
	}
	
	/**
	 * Returns true if this list contains a source with the
	 * given ID.
	 */
	public boolean contains(String id) {
		boolean b = false;
		for(ES2Source s : sources) {
			if(s.id != null && s.id.equals(id)) {
				b = true;
				break;
			}
		}		
		return b;
	}
	
	/**
	 * Returns the source for the given ID, or <code>null</code> if no
	 * such source exists in this list. 
	 */
	public ES2Source getSource(String id) {
		ES2Source source = null;
		for(ES2Source s : sources) {
			if(id == null && s.id == null) {
				source = s;
				break;
			} else if(id != null && s.id != null && id.equals(s.id)) {
				source = s;
				break;
			}
		}
		return source;
	}
	
	
}
