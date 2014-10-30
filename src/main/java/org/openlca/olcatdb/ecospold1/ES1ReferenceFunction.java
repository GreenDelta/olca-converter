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
 * Contains the identifying information of a dataset including name (english and
 * german), unit, classification (category, subCategory), etc.
 */
@Context(name = "referenceFunction", parentName = "processInformation")
public class ES1ReferenceFunction extends ContextObject {

	/**
	 * Indicates the amount of reference flow (product/service, elementary flow,
	 * impact category). Within the ecoinvent quality network the amount of the
	 * reference flow always equals 1.
	 */
	@ContextField(
			name = "referenceFunction", 
			parentName = "processInformation", 
			isAttribute = true, 
			attributeName = "amount", 
			type = Type.Double,
			isRequired = true)
	public Double amount;

	/**
	 * Indicates the number according to the Chemical Abstract Service (CAS).
	 * The Format of the CAS-number: 000000-00-0, where the first string of
	 * digits needs not to be complete (i.e. less than six digits are admitted).
	 * Not applicable for impact categories.
	 */
	@ContextField(
			name = "referenceFunction", 
			parentName = "processInformation", 
			isAttribute = true, 
			attributeName = "CASNumber", 
			type = Type.Text,
			length = 11)
	public String CASNumber;

	/**
	 * Category is used to structure the content of the database (together with
	 * SubCategory). It is not required for the identification of a process
	 * (processes in different categories/ subCategories may therefore not be
	 * named identically). But it is required for the identification of
	 * elementary flows and impact categories. Categories are administrated
	 * centrally. English is the default language in the ecoinvent quality
	 * network.
	 */
	@ContextField(
			name = "referenceFunction", 
			parentName = "processInformation", 
			isAttribute = true, 
			attributeName = "category",
			type = Type.Text,
			isRequired = true,
			length = 40)
	public String category;

	/** Indicates whether the dataset relates to a process/service or not. */
	@ContextField(
			name = "referenceFunction", 
			parentName = "processInformation", 
			isAttribute = true, 
			attributeName = "datasetRelatesToProduct",
			type = Type.Boolean, 
			isRequired = true)
	public Boolean datasetRelatesToProduct;

	/**
	 * Chemical formula (e.g. sum formula) may be entered. No graphs are allowed
	 * to represent chemical formulas.
	 */
	@ContextField(
			name = "referenceFunction", 
			parentName = "processInformation", 
			isAttribute = true, 
			attributeName = "formula",
			type = Type.Text, 
			length = 40)
	public String formula;

	/** Free text for general information about the dataset. */
	@ContextField(
			name = "referenceFunction", 
			parentName = "processInformation", 
			isAttribute = true, 
			attributeName = "generalComment", 
			type = Type.Text,
			length = 32000)
	public String generalComment;

	/**
	 * Contains a description of the (sub-)processes which are combined to form
	 * one unit process (e.g., 'operation of heating system' including operation
	 * of boiler unit, regulation unit and circulation pumps). Such combination
	 * may be necessary because of lack of detailedness in available data or
	 * because of data confidentiality. As far as possible and feasible, data
	 * should however be reported on the level of detail it has been received
	 */
	@ContextField(name = "referenceFunction", parentName = "processInformation", isAttribute = true, attributeName = "includedProcesses", isMultiple = false, type = Type.Text)
	public String includedProcesses;

	/**
	 * Indicates whether the unit process imported into the database on the
	 * basis of an LCI result (received as cumulative mass- and energy-flows,
	 * hence, no LCI results will be calculated for such processes) has included
	 * infrastructure processes or not. For all other unit process raw data data
	 * sets this data field is empty.
	 */
	@ContextField(name = "referenceFunction", parentName = "processInformation", isAttribute = true, attributeName = "infrastructureIncluded", isMultiple = false, type = Type.Boolean)
	public Boolean infrastructureIncluded;

	/**
	 * Indicates whether the process is an investment or an operation process.
	 * Investment processes are for instance building of a nuclear power plant,
	 * a road, docks, construction of production machinery which deliver as the
	 * output a nuclear power plant, a km road, one seaport, and production
	 * machinery respectively. It is used as a discriminating element for the
	 * identification of processes.
	 */
	@ContextField(name = "referenceFunction", parentName = "processInformation", isAttribute = true, attributeName = "infrastructureProcess", isMultiple = false, type = Type.Boolean)
	public Boolean infrastructureProcess;

	/**
	 * See category for explanations. German is the default local language in
	 * the ecoinvent quality network.
	 */
	@ContextField(name = "referenceFunction", parentName = "processInformation", isAttribute = true, attributeName = "localCategory", isMultiple = false, type = Type.Text)
	public String localCategory;

	/** see 'name' for explanations. */
	@ContextField(name = "referenceFunction", parentName = "processInformation", isAttribute = true, attributeName = "localName", isMultiple = false, type = Type.Text)
	public String localName;

	/**
	 * See subCategory for explanations. German is the default local language in
	 * the ecoinvent quality network.
	 */
	@ContextField(name = "referenceFunction", parentName = "processInformation", isAttribute = true, attributeName = "localSubCategory", isMultiple = false, type = Type.Text)
	public String localSubCategory;

	/** Name of the unit process, elementary flow or impact category. */
	@ContextField(name = "referenceFunction", parentName = "processInformation", isAttribute = true, attributeName = "name", isMultiple = false, type = Type.Text)
	public String name;

	/**
	 * Contains the EU-classification system (NACE code). For the first edition
	 * of the ecoinvent database this data field will not be used.
	 */
	@ContextField(name = "referenceFunction", parentName = "processInformation", isAttribute = true, attributeName = "statisticalClassification", isMultiple = false, type = Type.Text)
	public String statisticalClassification;

	/**
	 * SubCategory is used to further structure the content of the database
	 * (together with category). It is not required for the identification of a
	 * process (processes in different categories/subCategories may therefore not
	 * be named identically). But it is required for the identification of
	 * elementary flows and impact categories. SubCategories are administrated
	 * centrally.
	 */
	@ContextField(name = "referenceFunction", parentName = "processInformation", isAttribute = true, attributeName = "subCategory", isMultiple = false, type = Type.Text)
	public String subCategory;

	/** Synonyms for the name, localName. */
	@ContextField(name = "synonym", parentName = "referenceFunction",

	isMultiple = true, type = Type.Text)
	private List<String> synonyms = new ArrayList<String>();

	public List<String> getSynonyms() {
		return synonyms;
	}
	
	/**
	 * For unit processes (and systems terminated) it is the unit to which all
	 * inputs and outputs of the unit process are related to (functional unit).
	 * For elementary flows it is the unit in which exhanges are reported. For
	 * impact categories, it is the unit in which characterisation, damage or
	 * weighting factors are expressed.
	 */
	@ContextField(name = "referenceFunction", parentName = "processInformation", isAttribute = true, attributeName = "unit", isMultiple = false, type = Type.Text)
	public String unit;


}
