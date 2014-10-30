package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.TextAndImage;
import org.openlca.olcatdb.ecospold2.masterdata.ES2GeographyList;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The reference to a EcoSpold 02 geography. In EcoSpold 02 the information of a
 * geography is stored in a master data file. The process only contains the
 * reference information to the respective master data information (see
 * {@link org.openlca.olcatdb.ecospold2.masterdata.ES2GeographyList}).
 * 
 * @author Michael Srocka
 * 
 */
@Context(name = "geography", parentName = "activityDescription")
public class ES2GeographyRef extends ContextObject {

	@ContextField(name = "geography", parentName = "activityDescription", attributeName = "geographyId", isAttribute = true, isMaster = true, classGroup = ES2GeographyList.class)
	public String geographyId;

	@ContextField(name = "geography", parentName = "activityDescription", attributeName = "geographyContextId", isAttribute = true, isMaster = true)
	public String geographyContextId;

	@ContextField(name = "shortname", parentName = "geography", isMultiple = true, type = Type.MultiLangText, classGroup = ES2GeographyList.class)
	private List<LangString> shortNames = new ArrayList<LangString>();

	public List<LangString> getShortNames() {
		return shortNames;
	}

	@ContextField(name = "comment", parentName = "geography", type = Type.TextAndImage, classGroup = ES2GeographyList.class)
	public TextAndImage comment;

}
