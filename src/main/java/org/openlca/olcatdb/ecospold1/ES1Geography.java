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
 * Contains information about the geographic validity of the process. The region
 * described with regional code and free text is the market area of the product
 * / service at issue and not necessarily the place of production.
 */
@Context(name = "geography", parentName = "processInformation")
public class ES1Geography extends ContextObject {

	/**
	 * 7 letter regional code (capital letters). List of 2 letter ISO country
	 * codes extended by codes for regions, continents, market areas, and
	 * organisations and companies. The location code indicates the supply area
	 * of a product/service and the area of validity of impact assessment methods
	 * and impact categories, respectively. It does NOT necessarily coincide with
	 * the area/site of production or provenience. If supply and production area
	 * differ, production area is indicated in the name of the unit process.
	 */
	@ContextField(name = "geography", parentName = "processInformation", isAttribute = true, attributeName = "location", isMultiple = false, type = Type.Text)
	public String location;

	/** Free text for further explanation */
	@ContextField(name = "geography", parentName = "processInformation", isAttribute = true, attributeName = "text", isMultiple = false, type = Type.Text)
	public String text;

}
