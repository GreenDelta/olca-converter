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
 * Contains information about the fraction of the relevant market supplied by
 * the product/service described in the dataset. Information about market share,
 * production volume (in the ecoinvent quality network: also consumption volume
 * in the market area) and information about how data have been sampled.
 */
@Context(name = "representativeness", parentName = "modellingAndValidation")
public class ES1Representativeness extends ContextObject {

	/**
	 * Describes extrapolations of data from another time period, another
	 * geographical area or another technology and the way these extrapolations
	 * have been carried out.
	 */
	@ContextField(name = "representativeness", parentName = "modellingAndValidation", isAttribute = true, attributeName = "extrapolations", isMultiple = false, type = Type.Text)
	public String extrapolations;

	/**
	 * Indicates the share in market supply in the geographical area indicated
	 * of the product/service at issue.
	 */
	@ContextField(name = "representativeness", parentName = "modellingAndValidation", isAttribute = true, attributeName = "percent", isMultiple = false, type = Type.Double)
	public Double percent;

	/**
	 * Indicates the market area consumption volume (NOT necessarily identical
	 * with the production volume) in the geographical area indicated of the
	 * product/service at issue
	 */
	@ContextField(name = "representativeness", parentName = "modellingAndValidation", isAttribute = true, attributeName = "productionVolume", isMultiple = false, type = Type.Text)
	public String productionVolume;

	/**
	 * Indicates the sampling procedure applied for quantifying the exchanges.
	 * It should be reported whether the sampling procedure for particular
	 * elementary and intermediate product flows differ from the general
	 * procedure. Possible problems in combining different sampling procedures
	 * should be mentioned.
	 */
	@ContextField(name = "representativeness", parentName = "modellingAndValidation", isAttribute = true, attributeName = "samplingProcedure", isMultiple = false, type = Type.Text)
	public String samplingProcedure;

	/**
	 * For datasets where the additional uncertainty from lacking
	 * representativeness has been included in the quantified uncertainty values
	 * ('minValue' and 'maxValue'), thus raising the value in 'percent' of the
	 * dataset to 100%, this field also reports the original representativeness,
	 * the additional uncertainty and the procedure by which it was assessed or
	 * calculated.
	 */
	@ContextField(name = "representativeness", parentName = "modellingAndValidation", isAttribute = true, attributeName = "uncertaintyAdjustments", isMultiple = false, type = Type.Text)
	public String uncertaintyAdjustments;

}
