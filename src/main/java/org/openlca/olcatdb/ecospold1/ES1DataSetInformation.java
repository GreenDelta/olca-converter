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
 * Contains the administrative information about the dataset at issue: type of
 * dataset (unit process, elementary flow, impact category, multi-output
 * process) timestamp, version and internalVersion number as well as language
 * and localLanguage code.
 */
@Context(name = "dataSetInformation", parentName = "processInformation")
public class ES1DataSetInformation extends ContextObject {

	/**
	 * Indicates the way energy values are applied in the dataset. The codes
	 * are: 0=Undefined (default). 1=Net (lower) heating value. 2=Gross (higher)
	 * heating value.
	 */
	@ContextField(name = "dataSetInformation", parentName = "processInformation", isAttribute = true, attributeName = "energyValues", isMultiple = false, type = Type.Integer)
	public Integer energyValues;

	/**
	 * Indicates whether or not (yes/no) the dataset contains the results of an
	 * impact assessment applied on unit processes (unit process raw data) or
	 * terminated systems (LCI results).
	 */
	@ContextField(name = "dataSetInformation", parentName = "processInformation", isAttribute = true, attributeName = "impactAssessmentResult", isMultiple = false, type = Type.Boolean)
	public Boolean impactAssessmentResult;

	/**
	 * The internalVersion number is used to discern different versions during
	 * the working period until the dataset is entered into the database). The
	 * internalVersion is generated automatically with each change made in the
	 * dataset or related file.
	 */
	@ContextField(name = "dataSetInformation", parentName = "processInformation", isAttribute = true, attributeName = "internalVersion", isMultiple = false, type = Type.Double)
	public Double internalVersion;

	/**
	 * 2 letter ISO language codes are used. Default language is English. Lower
	 * case letters are used.
	 */
	@ContextField(name = "dataSetInformation", parentName = "processInformation", isAttribute = true, attributeName = "languageCode", isMultiple = false, type = Type.Text)
	public String languageCode;

	/**
	 * 2 letter ISO language codes are used. Default localLanguage is German.
	 * Lower case letters are used.
	 */
	@ContextField(name = "dataSetInformation", parentName = "processInformation", isAttribute = true, attributeName = "localLanguageCode", isMultiple = false, type = Type.Text)
	public String localLanguageCode;

	/** Automatically generated date when dataset is created. */
	@ContextField(name = "dataSetInformation", parentName = "processInformation", isAttribute = true, attributeName = "timestamp", isMultiple = false, type = Type.Text)
	public String timestamp;

	/**
	 * Indicates the kind of data that is represented by this dataset. The code
	 * is: 0=System non-terminated. 1=Unit process. 2=System terminated.
	 * 3=Elementary flow. 4=Impact category.5=Multioutput process.
	 */
	@ContextField(name = "dataSetInformation", parentName = "processInformation", isAttribute = true, attributeName = "type", isMultiple = false, type = Type.Integer)
	public Integer type;

	/**
	 * The ecoinvent version number is used as follows: with a major update
	 * (e.g. every second year) the version number is increased by one (1.00,
	 * 2.00, etc.). The digits after the decimal point (e.g., 1.01, 1.02, etc.)
	 * are used for minor updates (corrected errors) within the period of two
	 * major updates. The version number is placed manually.
	 */
	@ContextField(name = "dataSetInformation", parentName = "processInformation", isAttribute = true, attributeName = "version", isMultiple = false, type = Type.Double)
	public Double version;

}
