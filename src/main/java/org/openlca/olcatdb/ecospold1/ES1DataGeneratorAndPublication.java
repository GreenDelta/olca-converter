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
@Context(name = "dataGeneratorAndPublication", parentName = "administrativeInformation")
public class ES1DataGeneratorAndPublication extends ContextObject {

	/**
	 * The EcoSpold codes for this field are: 0=Public, 1=ETH Domain,
	 * 2=ecoinvent 2000, 3=Institute
	 */
	@ContextField(
			name = "dataGeneratorAndPublication", 
			parentName = "administrativeInformation", 
			isAttribute = true, 
			attributeName = "accessRestrictedTo", 
			isMultiple = false, 
			type = Type.Integer)
	public Integer accessRestrictedTo;

	/**  */
	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "companyCode", isMultiple = false, type = Type.Text)
	public String companyCode;

	/**  */
	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "copyright", isMultiple = false, type = Type.Boolean)
	public Boolean copyright;

	/**  */
	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "countryCode", isMultiple = false, type = Type.Text)
	public String countryCode;

	/**
	 * The EcoSpold codes for this field are: 0= Data as such not published
	 * elsewhere, 1= The data of some unit processes or subsystems are
	 * published, 2= Data has been published entirely in (refers to field 757)
	 * (default)
	 */
	@ContextField(
			name = "dataGeneratorAndPublication", 
			parentName = "administrativeInformation", 
			isAttribute = true, 
			attributeName = "dataPublishedIn", 
			isMultiple = false, 
			type = Type.Integer)
	public Integer dataPublishedIn;

	/**  */
	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "pageNumbers", isMultiple = false, type = Type.Text)
	public String pageNumbers;

	/**  */
	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "person", isMultiple = false, type = Type.Integer)
	public Integer person;

	/**  */
	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "referenceToPublishedSource", isMultiple = false, type = Type.Integer)
	public Integer referenceToPublishedSource;

}
