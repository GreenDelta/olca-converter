package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Provides information about the time representativeness of the data set.
 * 
 * @Element time
 * @ContentModel (referenceYear?, dataSetValidUntil?,
 *               timeRepresentativenessDescription*, other?)
 */
@Context(name = "time", parentName = "processInformation")
public class ILCDTime extends ContextObject {

	/**
	 * Start year of the time period for which the data set is valid (until year
	 * of "Data set valid until:"). For data sets that combine data from
	 * different years, the most representative year is given regarding the
	 * overall environmental impact. In that case, the reference year is derived
	 * by expert judgement.
	 * 
	 * @Element referenceYear
	 * @DataType Year
	 */
	@ContextField(name = "referenceYear", parentName = "time", type = Type.Integer)
	public Integer referenceYear;

	/**
	 * End year of the time period for which the data set is still valid /
	 * sufficiently representative. This date also determines when a data set
	 * revision / remodelling is required or recommended due to expected
	 * relevant changes in environmentally or technically relevant inventory
	 * values, including in the background system.
	 * 
	 * @Element dataSetValidUntil
	 * @DataType Year
	 */
	@ContextField(name = "dataSetValidUntil", parentName = "time", type = Type.Integer)
	public Integer validUntil;

	/**
	 * Description of the valid time span of the data set including information
	 * on limited usability within sub- time spans (e.g. summer/winter).
	 * 
	 * @Element timeRepresentativenessDescription
	 * @DataType : FT
	 */
	@ContextField(name = "timeRepresentativenessDescription", parentName = "time", type = Type.MultiLangText, isMultiple = true)
	private List<LangString> description = new ArrayList<LangString>();

	/**
	 * Description of the valid time span of the data set including information
	 * on limited usability within sub- time spans (e.g. summer/winter).
	 * 
	 * @Element timeRepresentativenessDescription
	 * @DataType : FT
	 */
	public List<LangString> getDescription() {
		return description;
	}

}
