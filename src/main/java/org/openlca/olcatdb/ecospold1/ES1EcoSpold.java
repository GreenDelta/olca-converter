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
 * The container element of EcoSpold data sets.
 */
@Context(name = "ecoSpold", parentName = "")
public class ES1EcoSpold extends ContextObject {

	/**  */
	@ContextField(name = "ecoSpold", parentName = "", isAttribute = true, attributeName = "schemaLocation", isMultiple = false, type = Type.Text)
	public String schemaLocation;

	/**  */
	@ContextField(name = "ecoSpold", parentName = "", isAttribute = true, attributeName = "validationId", isMultiple = false, type = Type.Integer)
	public Integer validationId;

	/**  */
	@ContextField(name = "ecoSpold", parentName = "", isAttribute = true, attributeName = "validationStatus", isMultiple = false, type = Type.Text)
	public String validationStatus;

	/**  */
	@ContextField(name = "ecoSpold", parentName = "", isAttribute = true, attributeName = "schema", isMultiple = false, type = Type.Text)
	public String schema;

	/**
	 * Contains information about one individual unit process (or terminated
	 * system). Information is divided into metaInformation and flowData.
	 */
	@SubContext(isMultiple = true, contextClass = ES1Dataset.class)
	private List<ES1Dataset> datasets = new ArrayList<ES1Dataset>();

	/**
	 * Contains information about one individual unit process (or terminated
	 * system). Information is divided into metaInformation and flowData.
	 */

	public List<ES1Dataset> getDatasets() {
		return datasets;
	}

}
