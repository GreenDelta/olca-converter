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
 *	
 */
@Context(name = "exchange", parentName = "flowData")
public class ES1Exchange extends ContextObject {

	/**
	 * Indicates the number according to the Chemical Abstract Service (CAS).
	 * The Format of the CAS- number: 000000-00-0, where the first string of
	 * digits needs not to be complete (i.e. less than six digits are admitted).
	 * Not applicable for impact categories.
	 * 
	 * @Attribute CASNumber
	 * @DataType string
	 */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "CASNumber", isMultiple = false, type = Type.Text)
	public String CASNumber;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "category", isMultiple = false, type = Type.Text)
	public String category;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "formula", isMultiple = false, type = Type.Text)
	public String formula;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "generalComment", isMultiple = false, type = Type.Text)
	public String generalComment;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "infrastructureProcess", isMultiple = false, type = Type.Boolean)
	public Boolean infrastructureProcess;

	/**  */
	@ContextField(name = "inputGroup", parentName = "exchange",

	isMultiple = false, type = Type.Integer)
	public Integer inputGroup;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "localCategory", isMultiple = false, type = Type.Text)
	public String localCategory;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "localName", isMultiple = false, type = Type.Text)
	public String localName;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "localSubCategory", isMultiple = false, type = Type.Text)
	public String localSubCategory;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "location", isMultiple = false, type = Type.Text)
	public String location;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "maxValue", isMultiple = false, type = Type.Double)
	public Double maxValue;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "meanValue", isMultiple = false, type = Type.Double)
	public Double meanValue;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "minValue", isMultiple = false, type = Type.Double)
	public Double minValue;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "mostLikelyValue", isMultiple = false, type = Type.Double)
	public Double mostLikelyValue;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "name", isMultiple = false, type = Type.Text)
	public String name;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "number", isMultiple = false, type = Type.Integer)
	public Integer number;

	/**  */
	@ContextField(name = "outputGroup", parentName = "exchange",

	isMultiple = false, type = Type.Integer)
	public Integer outputGroup;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "pageNumbers", isMultiple = false, type = Type.Text)
	public String pageNumbers;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "referenceToSource", isMultiple = false, type = Type.Integer)
	public Integer referenceToSource;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "standardDeviation95", isMultiple = false, type = Type.Double)
	public Double standardDeviation95;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "subCategory", isMultiple = false, type = Type.Text)
	public String subCategory;

	/**
	 * Defines the kind of uncertainty distribution applied on one particular
	 * exchange. Log-normal distribution is default, normal, triangular or
	 * uniform distribution may be chosen if appropriate. The EcoSpold codes
	 * are: 0=undefined, 1=lognormal (default), 2=normal, 3=triang, 4=uniform
	 */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "uncertaintyType", isMultiple = false, type = Type.Integer)
	public Integer uncertaintyType;

	/**  */
	@ContextField(name = "exchange", parentName = "flowData", isAttribute = true, attributeName = "unit", isMultiple = false, type = Type.Text)
	public String unit;

	/**
	 * Returns true if the flow described by this exchange is an elementary
	 * flow.
	 */
	public boolean isElementaryFlow() {
		if (this.inputGroup != null)
			return this.inputGroup.equals(4);
		else if (this.outputGroup != null)
			return this.outputGroup.equals(4);
		else
			return true;
	}

	@Override
	public String toString() {
		return "ES1Exchange [category=" + category + ", name=" + name
				+ ", subCategory=" + subCategory + ", unit=" + unit + "]";
	}

}
