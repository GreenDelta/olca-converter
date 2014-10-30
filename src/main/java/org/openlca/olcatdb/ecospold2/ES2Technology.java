package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.datatypes.TextAndImage;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Describes the technological properties of the unit process. It helps the user
 * to judge the technical suitability of the activity dataset for his or her
 * application (purpose).
 * 
 * @Element technology
 * @ContentModel (generalComment?, namespace:uri="##other")
 */
@Context(name = "technology", parentName = "activityDescription")
public class ES2Technology extends ContextObject {
	
	@ContextField(
			name = "technology", 
			parentName = "activityDescription", 
			attributeName = "technologyLevel", 
			isAttribute = true, 
			type = Type.Integer)
	public int technologyLevel;

	@ContextField(
			name = "comment", 
			parentName = "technology", 
			type = Type.TextAndImage)
	public TextAndImage comment;
}
