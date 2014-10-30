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
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Contains information about one individual unit process (or terminated
 * system). Information is divided into metaInformation and flowData.
 */
@Context(name = "dataset", parentName = "ecoSpold")
public class ES1Dataset extends ContextObject {

	// element attributes

	/**
	 * The generator of the data set.
	 */
	@ContextField(name = "dataset", parentName = "ecoSpold", isAttribute = true, attributeName = "generator", isMultiple = false, type = Type.Text)
	public String generator;

	/** The data set number. */
	@ContextField(name = "dataset", parentName = "ecoSpold", isAttribute = true, attributeName = "number", isMultiple = false, type = Type.Integer)
	public Integer number;

	/** The creation time as XML calender. */
	@ContextField(name = "dataset", parentName = "ecoSpold", isAttribute = true, attributeName = "timestamp", isMultiple = false, type = Type.Text)
	public String timestamp;

	/** A link to a category file. */
	@ContextField(name = "dataset", parentName = "ecoSpold", isAttribute = true, attributeName = "validCategories", isMultiple = false, type = Type.Text)
	public String validCategories;

	/** A link to a file with company codes. */
	@ContextField(name = "dataset", parentName = "ecoSpold", isAttribute = true, attributeName = "validCompanyCodes", isMultiple = false, type = Type.Text)
	public String validCompanyCodes;

	/** A link to a file with location codes. */
	@ContextField(name = "dataset", parentName = "ecoSpold", isAttribute = true, attributeName = "validRegionalCodes", isMultiple = false, type = Type.Text)
	public String validRegionalCodes;

	/** A link to a file with units. */
	@ContextField(name = "dataset", parentName = "ecoSpold", isAttribute = true, attributeName = "validUnits", isMultiple = false, type = Type.Text)
	public String validUnits;

	// sub-elements

	@SubContext(isMultiple = true, contextClass = ES1AllocationFactor.class)
	private List<ES1AllocationFactor> allocations = new ArrayList<ES1AllocationFactor>();

	/** Get the allocation factors of the data set. */
	public List<ES1AllocationFactor> getAllocationFactors() {
		return allocations;
	}

	/** The data entry by element. */
	@SubContext(isMultiple = false, contextClass = ES1DataEntryBy.class)
	public ES1DataEntryBy dataEntryBy;

	/**  */
	@SubContext(isMultiple = false, contextClass = ES1DataGeneratorAndPublication.class)
	public ES1DataGeneratorAndPublication dataGeneratorAndPublication;

	/**
	 * Contains the administrative information about the dataset at issue: type
	 * of dataset (unit process, elementary flow, impact category, multi-output
	 * process) timestamp, version and internalVersion number as well as
	 * language and localLanguage code.
	 */
	@SubContext(isMultiple = false, contextClass = ES1DataSetInformation.class)
	public ES1DataSetInformation dataSetInformation;

	@SubContext(isMultiple = true, contextClass = ES1Exchange.class)
	private List<ES1Exchange> exchanges = new ArrayList<ES1Exchange>();

	public List<ES1Exchange> getExchanges() {
		return exchanges;
	}

	/**
	 * Contains information about the geographic validity of the process. The
	 * region described with regional code and free text is the market area of
	 * the product / service at issue and not necessarily the place of
	 * production.
	 */
	@SubContext(isMultiple = false, contextClass = ES1Geography.class)
	public ES1Geography geography;

	/**
	 * Used for the identification of members of the organisation / institute
	 * co-operating within a quality network (e.g., ecoinvent) referred to in
	 * the areas Validation, dataEntryBy and dataGeneratorAndPublication.
	 */
	@SubContext(isMultiple = true, contextClass = ES1Person.class)
	private List<ES1Person> persons = new ArrayList<ES1Person>();

	/**
	 * Used for the identification of members of the organisation / institute
	 * co-operating within a quality network (e.g., ecoinvent) referred to in
	 * the areas Validation, dataEntryBy and dataGeneratorAndPublication.
	 */

	public List<ES1Person> getPersons() {
		return persons;
	}

	/**
	 * Contains the identifying information of a dataset including name (english
	 * and german), unit, classification (category, subCategory), etc.
	 */
	@SubContext(isMultiple = false, contextClass = ES1ReferenceFunction.class)
	public ES1ReferenceFunction referenceFunction;

	/**
	 * Contains information about the fraction of the relevant market supplied
	 * by the product/service described in the dataset. Information about market
	 * share, production volume (in the ecoinvent quality network: also
	 * consumption volume in the market area) and information about how data
	 * have been sampled.
	 */
	@SubContext(isMultiple = false, contextClass = ES1Representativeness.class)
	public ES1Representativeness representativeness;

	/**
	 * Contains information about author(s), title, kind of publication, place
	 * of publication, name of editors (if any), etc..
	 */
	@SubContext(isMultiple = true, contextClass = ES1Source.class)
	private List<ES1Source> sources = new ArrayList<ES1Source>();

	/**
	 * Contains information about author(s), title, kind of publication, place
	 * of publication, name of editors (if any), etc..
	 */

	public List<ES1Source> getSources() {
		return sources;
	}

	/**
	 * Contains a description of the technology for which flow data have been
	 * collected. Free text can be used. Pictures, graphs and tables are not
	 * allowed. The text should cover information necessary to identify the
	 * properties and particularities of the technology(ies) underlying the
	 * process data.
	 */
	@SubContext(isMultiple = false, contextClass = ES1Technology.class)
	public ES1Technology technology;

	/**
	 * Contains all possible date-formats applicable to describe start and end
	 * date of the time period for which the dataset is valid.
	 */
	@SubContext(isMultiple = false, contextClass = ES1TimePeriod.class)
	public ES1TimePeriod timePeriod;

	/**
	 * Contains information about who carried out the critical review and about
	 * the main results and conclusions of the review and the recommendations
	 * made.
	 */
	@SubContext(isMultiple = false, contextClass = ES1Validation.class)
	public ES1Validation validation;

	/**
	 * Returns true if this data set is a process and not an elementary flow or
	 * LCIA method data set (see data set information /type).
	 */
	public boolean isProcess() {
		return dataSetInformation != null
				&& dataSetInformation.type != null
				&& (dataSetInformation.type == 1
						|| dataSetInformation.type == 2 || dataSetInformation.type == 5);
	}
	
	/**
	 * Returns the person for the given id, or <code>null</code> if no
	 * such person exists.
	 */
	public ES1Person getPerson(Integer id) {
		ES1Person p = null;
		for(ES1Person pers : this.getPersons()) {
			if(pers.number != null && pers.number.equals(id)){
				p = pers;
				break;
			}
		}
		return p;
	}
	
	/**
	 * Returns the source for the given id, or <code>null</code> 
	 * if no such source exists.
	 */
	public ES1Source getSource(Integer id) {
		ES1Source s = null;
		for(ES1Source source : this.getSources()) {
			if(source.number != null && source.number.equals(id)) {
				s = source;
				break;
			}
		}		
		return s;
	}
}
