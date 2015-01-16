package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextField.Type;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;

/**
 * The EcoSpold 02 master-data list for elementary flows.
 */
@Context(name = "validElementaryExchanges")
public class ES2ElemFlowList extends ContextObject {

	@ContextField(name = "validElementaryExchanges", attributeName = "contextId", isAttribute = true, isRequired = true, length = 36)
	public String contextId;

	@ContextField(name = "validElementaryExchanges", attributeName = "majorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int majorRelease = 1;

	@ContextField(name = "validElementaryExchanges", attributeName = "majorRevision", isAttribute = true, type = Type.Integer)
	public Integer majorRevision;

	@ContextField(name = "validElementaryExchanges", attributeName = "minorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int minorRelease = 0;

	@ContextField(name = "validElementaryExchanges", attributeName = "minorRevision", isAttribute = true, type = Type.Integer)
	public Integer minorRevision;

	@ContextField(name = "validElementaryExchanges", attributeName = "originalSource", isAttribute = true)
	public String originalSource;

	@ContextField(name = "contextName", parentName = "validElementaryExchanges", isRequired = true, length = 80, isMultiple = true, type = Type.MultiLangText)
	private List<LangString> contextName = new ArrayList<LangString>();

	public List<LangString> getContextName() {
		return contextName;
	}

	@SubContext(contextClass = ES2ElemFlow.class, isMultiple = true)
	private List<ES2ElemFlow> elemFlows = new ArrayList<ES2ElemFlow>();

	public List<ES2ElemFlow> getElemFlows() {
		return elemFlows;
	}

	/**
	 * Returns true if this list contains an elementary flow with the given ID,
	 * otherwise false.
	 */
	public boolean contains(String id) {
		boolean b = false;
		if (id != null)
			for (ES2ElemFlow f : this.elemFlows) {
				if (id.equals(f.id)) {
					b = true;
					break;
				}
			}
		return b;
	}

	/**
	 * Returns true if this list contains an elementary name with the given
	 * name, otherwise false.
	 */
	public boolean containsName(List<LangString> names) {
		boolean result = false;
		Set<String> contains = new HashSet<String>();
		for (LangString langString : names) {
			contains.add(langString.getValue());
		}

		for (ES2ElemFlow exchange : elemFlows) {
			List<LangString> list = exchange.getName();
			if (list != null && names != null) {
				for (LangString langString : list) {
					result = contains.contains(langString.getValue());
				}

			}

			if (result)
				break;
		}

		return result;
	}

}
