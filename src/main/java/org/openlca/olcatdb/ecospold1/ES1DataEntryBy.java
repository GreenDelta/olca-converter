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
@Context(name = "dataEntryBy", parentName = "administrativeInformation")
public class ES1DataEntryBy extends ContextObject {

	/**  */
	@ContextField(name = "dataEntryBy", parentName = "administrativeInformation", isAttribute = true, attributeName = "person", isMultiple = false, type = Type.Integer)
	public Integer person;

	/**  */
	@ContextField(name = "dataEntryBy", parentName = "administrativeInformation", isAttribute = true, attributeName = "qualityNetwork", isMultiple = false, type = Type.Integer)
	public Integer qualityNetwork;

}
