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
 * Contains a description of the technology for which flow data have been
 * collected. Free text can be used. Pictures, graphs and tables are not
 * allowed. The text should cover information necessary to identify the
 * properties and particularities of the technology(ies) underlying the process
 * data.
 */
@Context(name = "technology", parentName = "processInformation")
public class ES1Technology extends ContextObject {

	/**
	 * Describes the technological properties of the unit process. If the
	 * process comprises several subprocesses, the corresponding technologies
	 * should be reported as well. Professional nomenclature should be used for
	 * the description.
	 */
	@ContextField(name = "technology", parentName = "processInformation", isAttribute = true, attributeName = "text", isMultiple = false, type = Type.Text)
	public String text;

}
