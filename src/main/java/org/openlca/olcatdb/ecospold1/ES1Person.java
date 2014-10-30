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
 * Used for the identification of members of the organisation / institute
 * co-operating within a quality network (e.g., ecoinvent) referred to in the
 * areas Validation, dataEntryBy and dataGeneratorAndPublication.
 */
@Context(name = "person", parentName = "administrativeInformation")
public class ES1Person extends ContextObject {

	/**  */
	@ContextField(name = "person", parentName = "administrativeInformation", isAttribute = true, attributeName = "address", isMultiple = false, type = Type.Text)
	public String address;

	/**  */
	@ContextField(name = "person", parentName = "administrativeInformation", isAttribute = true, attributeName = "companyCode", isMultiple = false, type = Type.Text)
	public String companyCode;

	/**  */
	@ContextField(name = "person", parentName = "administrativeInformation", isAttribute = true, attributeName = "countryCode", isMultiple = false, type = Type.Text)
	public String countryCode;

	/**  */
	@ContextField(name = "person", parentName = "administrativeInformation", isAttribute = true, attributeName = "email", isMultiple = false, type = Type.Text)
	public String email;

	/**  */
	@ContextField(name = "person", parentName = "administrativeInformation", isAttribute = true, attributeName = "name", isMultiple = false, type = Type.Text)
	public String name;

	/**  */
	@ContextField(name = "person", parentName = "administrativeInformation", isAttribute = true, attributeName = "number", isMultiple = false, type = Type.Integer)
	public Integer number;

	/**  */
	@ContextField(name = "person", parentName = "administrativeInformation", isAttribute = true, attributeName = "telefax", isMultiple = false, type = Type.Text)
	public String telefax;

	/**  */
	@ContextField(name = "person", parentName = "administrativeInformation", isAttribute = true, attributeName = "telephone", isMultiple = false, type = Type.Text)
	public String telephone;

}
