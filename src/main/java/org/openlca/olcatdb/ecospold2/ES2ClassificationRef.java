package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ClassificationSystemList;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextField.Type;
import org.openlca.olcatdb.parsing.ContextObject;

/**
 * A reference to a EcoSpold 02 classification in a master data file (used for
 * process and product classification).
 */
@Context(name = "classification", parentNonStrict = true)
public class ES2ClassificationRef extends ContextObject {

	@ContextField(name = "classification", parentNonStrict = true, attributeName = "classificationId", isAttribute = true, isMaster = true, classGroup = ES2ClassificationSystemList.class)
	public String classificationId;

	@ContextField(name = "classification", parentNonStrict = true, attributeName = "classificationContextId", isAttribute = true, isMaster = true)
	public String classificationContextId;

	@ContextField(name = "classificationSystem", parentName = "classification", isMultiple = true, type = Type.MultiLangText, classGroup = ES2ClassificationSystemList.class)
	private List<LangString> classificationSystem = new ArrayList<LangString>();

	public List<LangString> getClassificationSystem() {
		return classificationSystem;
	}

	@ContextField(name = "classificationValue", parentName = "classification", isMultiple = true, type = Type.MultiLangText, classGroup = ES2ClassificationSystemList.class)
	private List<LangString> classificationValue = new ArrayList<LangString>();

	public List<LangString> getClassificationValue() {
		return classificationValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ES2ClassificationRef && obj != null) {
			ES2ClassificationRef classi = ES2ClassificationRef.class.cast(obj);
			if (classi.classificationId != null
					&& this.classificationId != null
					&& classi.classificationId.equals(this.classificationId))
				return true;
			else
				return false;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return classificationId == null ? super.hashCode() : classificationId
				.hashCode();
	}
}
