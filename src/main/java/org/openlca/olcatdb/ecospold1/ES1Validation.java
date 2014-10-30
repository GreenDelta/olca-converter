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
 * Contains information about who carried out the critical review and about the
 * main results and conclusions of the review and the recommendations made.
 */
@Context(name = "validation", parentName = "modellingAndValidation")
public class ES1Validation extends ContextObject {

	/**  */
	@ContextField(
			name = "validation", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "otherDetails", 
			isMultiple = false, 
			type = Type.Text)
	public String otherDetails;

	/**  */
	@ContextField(
			name = "validation", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "proofReadingDetails", 
			isMultiple = false, 
			type = Type.Text)
	public String proofReadingDetails;

	/**  */
	@ContextField(
			name = "validation", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "proofReadingValidator", 
			isMultiple = false, 
			type = Type.Integer)
	public Integer proofReadingValidator;

}
