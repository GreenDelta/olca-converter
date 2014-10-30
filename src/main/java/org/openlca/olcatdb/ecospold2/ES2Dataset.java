package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextField.Type;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;

/**
 * @Element activityDataset
 * @ContentModel (activityDescription, flowData, modellingAndValidation,
 *               administrativeInformation, namespace:uri="##other")
 */
@Context(name = "activityDataset", parentName = "ecoSpold")
public class ES2Dataset extends ContextObject {

	@ContextField(name = "isChildDataSet", isAttribute = true, type = Type.Boolean)
	public boolean isChildDataSet = false;

	/**
	 * Comprises information which identifies and characterises one particular
	 * dataset (=unit process or system terminated).
	 * 
	 * @Element activityDescription
	 * @ContentModel (activityName+, synonym*, includedActivitiesStart*,
	 *               includedActivitiesEnd*, allocationComment?,
	 *               generalComment?, tag*, namespace:uri="##other")
	 */
	@SubContext(contextClass = ES2Description.class)
	public ES2Description description;

	/**
	 * Contains classification pairs to specify the activity.
	 * 
	 * @Element classification
	 */
	@SubContext(contextClass = ES2ClassificationRef.class, isMultiple = true)
	protected List<ES2ClassificationRef> classifications = new ArrayList<ES2ClassificationRef>();

	/**
	 * Contains classification pairs to specify the activity.
	 * 
	 * @Element classification
	 */
	public List<ES2ClassificationRef> getClassifications() {
		return classifications;
	}

	/**
	 * Describes the geographic area for which the dataset is supposed to be
	 * valid. It is the supply region (not the production region) of the product
	 * service at issue. It helps the user to judge the geographical suitability
	 * of the activity (or impact compartment) dataset for his or her
	 * application (purpose).
	 * 
	 * @Element geography
	 * @ContentModel (locationShortname*, generalComment?,
	 *               namespace:uri="##other")
	 */
	@SubContext(contextClass = ES2GeographyRef.class)
	public ES2GeographyRef geography;

	/**
	 * Describes the technological properties of the unit process. It helps the
	 * user to judge the technical suitability of the activity dataset for his
	 * or her application (purpose).
	 * 
	 * @Element technology
	 * @ContentModel (generalComment?, namespace:uri="##other")
	 */
	@SubContext(contextClass = ES2Technology.class)
	public ES2Technology technology;

	/**
	 * Characterises the temporal properties of the unit activity (or system
	 * terminated) at issue. It helps the user to judge the temporal suitability
	 * of the activity dataset for his or her application (purpose).
	 * 
	 * @Element timePeriod
	 * @ContentModel (startDate, endDate, generalComment?,
	 *               namespace:uri="##other")
	 */
	@SubContext(isMultiple = false, contextClass = ES2TimePeriod.class)
	public ES2TimePeriod timePeriod;

	/**
	 * @Element macroEconomicScenario
	 * @ContentModel (name+, generalComment*)
	 */
	@SubContext(contextClass = ES2MacroEconomicScenario.class)
	public ES2MacroEconomicScenario macroEconomicScenario;

	@SubContext(contextClass = ES2IntermediateExchange.class, isMultiple = true)
	protected List<ES2IntermediateExchange> intermediateExchanges = new ArrayList<ES2IntermediateExchange>();

	public List<ES2IntermediateExchange> getIntermediateExchanges() {
		return intermediateExchanges;
	}

	@SubContext(contextClass = ES2ElementaryExchange.class, isMultiple = true)
	private List<ES2ElementaryExchange> elementaryExchanges = new ArrayList<ES2ElementaryExchange>();

	public List<ES2ElementaryExchange> getElementaryExchanges() {
		return elementaryExchanges;
	}

	/**
	 * comprises all referenceToInputOutput.
	 * 
	 * @Element parameter
	 * @ContentModel (name*, uncertainty?, generalComment*,
	 *               namespace:uri="##other")
	 */
	@SubContext(contextClass = ES2Parameter.class, isMultiple = true)
	protected List<ES2Parameter> parameters = new ArrayList<ES2Parameter>();

	/**
	 * comprises all referenceToInputOutput.
	 * 
	 * @Element parameter
	 * @ContentModel (name*, uncertainty?, generalComment*,
	 *               namespace:uri="##other")
	 * 
	 */
	public List<ES2Parameter> getParameters() {
		return parameters;
	}

	/**
	 * Contains information about the representativeness of the unit process
	 * data (meta information and flow data).
	 * 
	 * @Element representativeness
	 * @ContentModel (productionVolumeText*, samplingProcedure*,
	 *               extrapolations*, namespace:uri="##other")
	 */
	@SubContext(contextClass = ES2Representativeness.class)
	public ES2Representativeness representativeness;

	@SubContext(contextClass = ES2Review.class, isMultiple = true)
	protected List<ES2Review> reviews = new ArrayList<ES2Review>();

	public List<ES2Review> getReviews() {
		return reviews;
	}

	/**
	 * Contains information about the person and the quality network the person
	 * belongs to.
	 * 
	 * @Element dataEntryBy
	 */
	@SubContext(contextClass = ES2Entry.class)
	public ES2Entry dataEntryBy;

	/**
	 * Contains information about the generator of the dataset in the database,
	 * whether the dataset has been published (and how) and about copyright and
	 * the accessibility of the dataset (public or restricted to ETH domain,
	 * ECOINVENT, or a particular institute of ECOINVENT).
	 * 
	 * @Element dataGeneratorAndPublication
	 * @ContentModel (pageNumbers?, namespace:uri="##other")
	 */
	@SubContext(contextClass = ES2Publication.class)
	public ES2Publication publication;

	@SubContext(contextClass = ES2FileAttributes.class)
	public ES2FileAttributes fileAttributes;

	/**
	 * Returns true if this activity has more than one output products,
	 * otherwise false.
	 */
	public boolean isMultiFunctional() {
		int outputCount = 0;
		for (ES2IntermediateExchange exchange : getIntermediateExchanges()) {
			if (exchange.outputGroup != null) {
				outputCount++;
			}
		}
		return outputCount > 1;
	}
}
