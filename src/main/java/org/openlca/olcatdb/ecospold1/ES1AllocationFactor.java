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

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 *	An EcoSpold 01 allocation factor
 */
@Context(name = "allocation", parentName = "flowData")
public class ES1AllocationFactor extends ContextObject {

	/**
	 * The code for the allocation method: 
	 * <ul>
	 * <li>-1=Undefined (default)</li>
	 * <li>0=Physical causality</li>
	 * <li>1=Economic causality</li>
	 * <li>2=Other method.</li>
	 * </ul>
	 */
	@ContextField(
			name = "allocation", 
			parentName = "flowData", 
			isAttribute = true, 
			attributeName = "allocationMethod", 
			isMultiple = false, 
			type = Type.Integer)
	public int allocationMethod = -1;
	
	@ContextField(
			name = "allocation", 
			parentName = "flowData", 
			isAttribute = true, 
			attributeName = "explanations", 
			isMultiple = false, 
			type = Type.Text)
	public String explanations;

	/**
	 * The value of the allocation factor, expressed as a fraction (in %),
	 * applied on one particular exchange for one particular co-product. The sum
	 * of the allocation factors applied on one particular exchange must add up
	 * to 100%.
	 */
	@ContextField(
			name = "allocation", 
			parentName = "flowData", 
			isAttribute = true, 
			attributeName = "fraction", 
			isMultiple = false, 
			type = Type.Double)
	public double fraction;

	/**
	 * Indicates the co-product output for which a particular allocation factor
	 * is valid. Additional information is required about the exchange on which
	 * the allocation factor is applied (see 'referenceToInputOutput').
	 */
	@ContextField(
			name = "allocation", 
			parentName = "flowData", 
			isAttribute = true, 
			attributeName = "referenceToCoProduct", 
			isMultiple = false, 
			type = Type.Integer)
	public Integer referenceToCoProduct;
	
	@ContextField(
			name = "referenceToInputOutput", 
			parentName = "allocation",
			isMultiple = true, 
			type = Type.Integer)
	private List<Integer> inputOutputReferences 
		= new ArrayList<Integer>();

	
	public List<Integer> getInputOutputReferences() {
		return this.inputOutputReferences;
	}

}
