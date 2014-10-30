/*******************************************************************************
 * Copyright (c) 2007, 2008 GreenDeltaTC. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Mozilla
 * Public License v1.1 which accompanies this distribution, and is available at
 * http://www.openlca.org/uploads/media/MPL-1.1.html
 * 
 * Contributors: 
 *			GreenDeltaTC - initial API and implementation
 * 			www.greendeltatc.com 
 *			tel.: +49 30 4849 6030 
 *			mail: gdtc@greendeltatc.com
 ******************************************************************************/

package org.openlca.olcatdb.ecospold1;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Contains all possible date-formats applicable to describe start and end date
 * of the time period for which the dataset is valid.
 */
@Context(name = "timePeriod", parentName = "processInformation")
public class ES1TimePeriod extends ContextObject {

	/**
	 * Indicates whether or not the process data (elementary and intermediate
	 * product flows reported under flow data) are valid for the entire time
	 * period stated. If not, explanations may be given under 'text'.
	 */
	@ContextField(name = "timePeriod", parentName = "processInformation", isAttribute = true, attributeName = "dataValidForEntirePeriod", isMultiple = false, type = Type.Boolean)
	public Boolean dataValidForEntirePeriod;

	/**
	 * End date of the time period for which the dataset is valid, presented as
	 * a complete date (year-month-day).
	 */
	@ContextField(name = "endDate", parentName = "timePeriod", isMultiple = false, type = Type.Text)
	public String endDate;

	/** End date of the time period for which the dataset is valid. */
	@ContextField(name = "endYear", parentName = "timePeriod", isMultiple = false, type = Type.Integer)
	public Integer endYear;

	/** End date of the time period for which the dataset is valid. */
	@ContextField(name = "endYearMonth", parentName = "timePeriod", isMultiple = false, type = Type.Text)
	public String endYearMonth;

	/**
	 * Start date of the time period for which the dataset is valid, presented
	 * as a complete date (year-month-day).
	 */
	@ContextField(name = "startDate", parentName = "timePeriod", isMultiple = false, type = Type.Text)
	public String startDate;

	/** Start date of the time period for which the dataset is valid. */
	@ContextField(name = "startYear", parentName = "timePeriod", isMultiple = false, type = Type.Integer)
	public Integer startYear;

	/** Start date of the time period for which the dataset is valid. */
	@ContextField(name = "startYearMonth", parentName = "timePeriod", isMultiple = false, type = Type.Text)
	public String startYearMonth;

	/**
	 * Additional explanations concerning the temporal validity of the flow data
	 * reported.
	 */
	@ContextField(name = "timePeriod", parentName = "processInformation", isAttribute = true, attributeName = "text", isMultiple = false, type = Type.Text)
	public String text;

}
