package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.datatypes.TextAndImage;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Characterises the temporal properties of the unit activity (or system
 * terminated) at issue. It helps the user to judge the temporal suitability of
 * the activity dataset for his or her application (purpose).
 * 
 * @Element timePeriod
 * @ContentModel ((startYear | startYearMonth | startDate), (endYear |
 *               endYearMonth | endDate), generalComment?,
 *               namespace:uri="##other")
 */
@Context(name = "timePeriod", parentName = "activityDescription")
public class ES2TimePeriod extends ContextObject {

	@ContextField(
			name = "timePeriod", 
			parentName = "activityDescription", 
			attributeName = "isDataValidForEntirePeriod", 
			isAttribute = true, 
			type = Type.Boolean)
	public Boolean dataValidForEntirePeriod;

	@ContextField(
			name = "timePeriod", 
			parentName = "activityDescription", 
			attributeName="startDate", 
			isAttribute=true)
	public String startDate;

	@ContextField(
			name = "timePeriod", 
			parentName = "activityDescription", 
			attributeName="endDate", 
			isAttribute=true)
	public String endDate;

	@ContextField(
			name = "comment", 
			parentName = "timePeriod", 
			type = Type.TextAndImage)
	public TextAndImage comment;

}
