package org.openlca.olcatdb.conversion.es2tocsv;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.ES2ClassificationRef;
import org.openlca.olcatdb.ecospold2.ES2Dataset;
import org.openlca.olcatdb.ecospold2.ES2ElementaryExchange;
import org.openlca.olcatdb.ecospold2.ES2IntermediateExchange;
import org.openlca.olcatdb.ecospold2.ES2Parameter;
import org.openlca.olcatdb.ecospold2.ES2Property;
import org.openlca.olcatdb.ecospold2.ES2TransferCoefficient;
import org.openlca.olcatdb.ecospold2.ES2Uncertainty;

import com.greendeltatc.simapro.csv.model.SPCalculatedParameter;
import com.greendeltatc.simapro.csv.model.SPDataEntry;
import com.greendeltatc.simapro.csv.model.SPDocumentation;
import com.greendeltatc.simapro.csv.model.SPElementaryFlow;
import com.greendeltatc.simapro.csv.model.SPInputParameter;
import com.greendeltatc.simapro.csv.model.SPProcess;
import com.greendeltatc.simapro.csv.model.SPProductFlow;
import com.greendeltatc.simapro.csv.model.SPReferenceProduct;
import com.greendeltatc.simapro.csv.model.SPSubstance;
import com.greendeltatc.simapro.csv.model.SPUnit;
import com.greendeltatc.simapro.csv.model.types.ElementaryFlowType;
import com.greendeltatc.simapro.csv.model.types.Geography;
import com.greendeltatc.simapro.csv.model.types.ProcessCategory;
import com.greendeltatc.simapro.csv.model.types.ProcessType;
import com.greendeltatc.simapro.csv.model.types.ProductFlowType;
import com.greendeltatc.simapro.csv.model.types.SubCompartment;
import com.greendeltatc.simapro.csv.model.types.Technology;
import com.greendeltatc.simapro.csv.model.types.TimePeriod;

/**
 * 
 * @author Imo Graf
 * 
 */
public class Conversion {

	Logger logger;

	/**
	 * Index for the parameters
	 */
	private Map<String, SPInputParameter> paramterIndex;

	/**
	 * 
	 */
	private ActivityType activityType;

	public Conversion(Map<String, SPInputParameter> paramterIndex,
			ActivityType activityType, Logger logger) {
		this.paramterIndex = paramterIndex;
		this.activityType = activityType;
		this.logger = logger;
	}

	void description(SPDataEntry dataEntry, ES2Dataset dataset) {

		ProcessType processType = null;
		switch (dataset.description.type) {
		case 1:
			processType = ProcessType.SYSTEM;
			break;

		case 2:
			processType = ProcessType.UNIT_PROCESS;
			break;
		}

		ProcessCategory processCategory = null;
		switch (activityType) {
		case PROCESS:
			processCategory = ProcessCategory.MATERIAL;
			break;

		case WASTE_TREATMENT:
			processCategory = ProcessCategory.WASTE_TREATMENT;
			break;

		}

		SPDocumentation doc = new SPDocumentation(dataset.description.getName()
				.get(0).getValue(), processCategory, processType);
		dataEntry.setDocumentation(doc);

		// name
		if (dataset.description.getName() != null)
			doc.setName(ConversionUtils.getLangEntry(dataset.description
					.getName()));
		if (doc.getName() == null && doc.getName().equals(""))
			logger.severe("Null actvity name for activity id: "
					+ dataset.description.id);
	}

	void geography(SPDataEntry dataEntry, ES2Dataset dataset,
			Map<String, Geography> geoMap) {
		Geography geography = Geography.UNSPECIFIED;

		String geo = null;
		if (geography != null) {

			geo = ConversionUtils.getLangEntry(dataset.geography
					.getShortNames());
			Geography geoTemp = geoMap.get(geo);
			if (geoTemp != null)
				geography = geoTemp;
		}
		dataEntry.getDocumentation().setGeography(geography);
	}

	void timePeriod(SPDataEntry dataEntry, ES2Dataset dataset) {
		TimePeriod timePeriod = TimePeriod.UNSPECIFIED;
		String startDate = null;
		String endDate = null;
		int startYear = 0;
		int endYear = 0;

		if (dataset.timePeriod != null) {
			startDate = dataset.timePeriod.startDate;
			endDate = dataset.timePeriod.endDate;
		}

		if (startDate != null && startDate != "" && endDate != null
				&& endDate != "") {
			try {
				startYear = Integer.parseInt(startDate.toString().substring(0,
						startDate.toString().indexOf("-")));
				endYear = Integer.parseInt(endDate.toString().substring(0,
						endDate.toString().indexOf("-")));
			} catch (Exception e) {
			}

			timePeriod = TimePeriod.MIXED_DATA;
			if (startYear < 1980 || endYear < 1980) {
				timePeriod = TimePeriod.P_1980_AND_BEFORE;
			} else if (startYear >= 1980 && endYear <= 1984) {
				timePeriod = TimePeriod.P_1980_1984;
			} else if (startYear >= 1985 && endYear <= 1989) {
				timePeriod = TimePeriod.P_1985_1989;
			} else if (startYear >= 1990 && endYear <= 1994) {
				timePeriod = TimePeriod.P_1990_1994;
			} else if (startYear >= 1995 && endYear <= 1999) {
				timePeriod = TimePeriod.P_1995_1999;
			} else if (startYear >= 2000 && endYear <= 2004) {
				timePeriod = TimePeriod.P_2000_2004;
			} else if (startYear >= 2005 && endYear <= 2009) {
				timePeriod = TimePeriod.P_2005_2009;
			} else if (startYear > 2010 && endYear > 2010) {
				timePeriod = TimePeriod.P_2010_AND_AFTER;
			}
		}
		dataEntry.getDocumentation().setTimePeriod(timePeriod);
	}

	void technology(SPDataEntry dataEntry, ES2Dataset dataset) {
		Technology technology = Technology.UNKNOWN;
		if (dataset.technology != null) {
			switch (dataset.technology.technologyLevel) {

			// undefined
			case 0:
				technology = Technology.UNSPECIFIED;
				break;

			// New
			case 1:
				technology = Technology.BEST_AVAILABLE_TECHNOLOGY;
				break;

			// Modern
			case 2:
				technology = Technology.MODERN_TECHNOLOGY;
				break;

			// Current (default)
			case 3:
				technology = Technology.AVERAGE_TECHNOLOGY;
				break;

			// Old
			case 4:
				technology = Technology.WORST_CASE;
				break;

			// Outdated
			case 5:
				technology = Technology.OUTDATED_TECHNOLOGY;
				break;
			}
		}

		dataEntry.getDocumentation().setTechnology(technology);
	}

	void intermediateExchanges(SPDataEntry dataEntry, ES2Dataset dataset,
			ActivityEntry entry, Set<String> electricityUnits) {

		for (ES2IntermediateExchange exchange : dataset
				.getIntermediateExchanges()) {
			ProductFlowType productFlowType = null;
			boolean isByProduct = false;

			String name = ConversionUtils.getLangEntry(exchange.getName());

			SPUnit unit = new SPUnit(ConversionUtils.getLangEntry(exchange
					.getUnitNames()));

			boolean isCalculatedAmount = false;
			if (exchange.isCalculatedAmount != null)
				isCalculatedAmount = exchange.isCalculatedAmount;

			String amount = getExchangeAmount(dataEntry,
					exchange.mathematicalRelation, exchange.amount,
					isCalculatedAmount, exchange.uncertainty);

			if (exchange.inputGroup != null) {

				switch (exchange.inputGroup) {

				// Materials/Fuels
				case 1:
					productFlowType = ProductFlowType.MATERIAL_INPUT;
					break;

				// Electricity/Heat
				case 2:
					productFlowType = ProductFlowType.ELECTRICITY_INPUT;
					break;

				// Services
				case 3:
					productFlowType = ProductFlowType.MATERIAL_INPUT;
					break;

				// From Technosphere (unspecified)
				case 5:
					switch (activityType) {
					case PROCESS:
						if (electricityUnits.contains(unit.getName()))
							productFlowType = ProductFlowType.ELECTRICITY_INPUT;
						else
							productFlowType = ProductFlowType.MATERIAL_INPUT;
						break;

					case WASTE_TREATMENT:
						productFlowType = ProductFlowType.WASTE_TREATMENT;
						break;
					}
					break;

				default:
					logger.severe("Wrong inputGroup: Activity='"
							+ ConversionUtils.getLangEntry(dataset.description
									.getName()) + "' IntermediateExchange='"
							+ ConversionUtils.getLangEntry(exchange.getName())
							+ "': " + exchange.inputGroup);
				}

			} else if (exchange.outputGroup != null) {

				switch (exchange.outputGroup) {

				// ReferenceProduct
				case 0:
					if (!entry.refFlowId.equals(exchange.id)) {
						switch (activityType) {
						case PROCESS:
							isByProduct = true;
							break;

						case WASTE_TREATMENT:
							productFlowType = ProductFlowType.AVOIDED_PRODUCT;
							break;
						}
					}
					break;

				// By-product
				case 2:
					isByProduct = true;
					break;

				// Material for Treatment
				case 3:
					productFlowType = ProductFlowType.WASTE_TREATMENT;
					break;

				// Stock Additions
				case 5:
					// TODO
					logger.severe("outputGroup 5 is not implemnted yet: "
							+ ConversionUtils.getLangEntry(exchange.getName()));

				default:

				}

			} else {
				logger.severe("No input/ouputGroup found. Acitvity: "
						+ ConversionUtils.getLangEntry(dataset.description
								.getName()) + "' IntermediateExchange='"
						+ ConversionUtils.getLangEntry(exchange.getName()));
			}

			// comment
			StringBuilder comment = new StringBuilder();
			comment.append("");
			String exchangeComment = getExchangeComment(exchange.getComment(),
					exchange.getSynonym(), exchange.getTags(),
					exchange.getProperties(),
					exchange.getTransferCoefficients(), exchange.sourceYear,
					exchange.sourceFirstAuthor, exchange.pageNumbers);

			if (exchangeComment != null)
				comment.append(comment.toString() + "\n");

			// comment only for intermediate exchanges

			// @productionVolumeAmount
			if (exchange.productionVolumeAmount != null)
				comment.append("production volume amount: "
						+ exchange.productionVolumeAmount + "\n");

			// @productionVolumeVariableName
			if (exchange.productionVolumeVariableName != null)
				comment.append("production volume variable name: "
						+ exchange.productionVolumeVariableName + "\n");

			// @productionVolumeMathematicalRelation
			if (exchange.productionVolumeMathematicalRelation != null)
				comment.append("production volume mathematical relation: "
						+ exchange.productionVolumeMathematicalRelation + "\n");

			// @productionVolumeSourceYear
			if (exchange.productionVolumeSourceYear != null)
				comment.append("production volume source year: "
						+ exchange.productionVolumeSourceYear + "\n");

			// @productionVolumeSourceFirstAuthor
			if (exchange.productionVolumeSourceFirstAuthor != null)
				comment.append("production volume source first author: "
						+ exchange.productionVolumeSourceFirstAuthor + "\n");

			// productionVolumeComment
			String productionVolumeComment = ConversionUtils
					.getLangEntry(exchange.getProductionVolumeComment());
			if (productionVolumeComment != null)
				comment.append("productionVolumeComment: "
						+ productionVolumeComment + "\n");

			// classification
			for (ES2ClassificationRef classification : exchange
					.getProductClassifications()) {

				String classificationSystem = ConversionUtils
						.getLangEntry(classification.getClassificationSystem());
				if (classificationSystem != null)
					comment.append("classification system: "
							+ classificationSystem + "\n");

				String classificationValue = ConversionUtils
						.getLangEntry(classification.getClassificationValue());
				if (classificationValue != null)
					comment.append("classification value: "
							+ classificationValue + "\n");
			}

			// productionVolumeUncertainty
			if (exchange.productionVolumeUncertainty != null)
				comment.append("\nProduction Volume Uncertainty\n"
						+ ConversionUtils
								.getUncertaintyAsString(exchange.productionVolumeUncertainty));

			if (productFlowType != null) {
				SPProductFlow flow = new SPProductFlow(productFlowType, name,
						unit, amount);
				dataEntry.add(flow);

				if (comment.length() != 0)
					flow.setComment(comment.toString());

				// uncertainty
				if (exchange.uncertainty != null)
					flow.setDistribution(ConversionUtils
							.getDistribution(exchange.uncertainty));

			} else if (isByProduct) {
				if (activityType == ActivityType.PROCESS) {
					SPProcess process = (SPProcess) dataEntry;
					SPReferenceProduct product = new SPReferenceProduct(name,
							unit, amount);
					process.add(product);

					if (comment.length() != 0)
						product.setComment(comment.toString());

				} else {
					logger.severe("Intermediate exchange was detected as a by product but the activity not as a process.");
				}

			} else {
				if (!entry.refFlowId.equals(exchange.id))
					logger.severe("Intermediate exchange could not identified.");
			}

		}

	}

	void elementaryExchanges(SPDataEntry dataEntry, ES2Dataset dataset,
			Map<String, CompartmentModel> compartments) {

		for (ES2ElementaryExchange exchange : dataset.getElementaryExchanges()) {
			String name = ConversionUtils.getLangEntry(exchange.getName());
			SPUnit unit = new SPUnit(ConversionUtils.getLangEntry(exchange
					.getUnitNames()));

			boolean isCalculatedAmount = false;
			if (exchange.isCalculatedAmount != null)
				isCalculatedAmount = exchange.isCalculatedAmount;

			String amount = getExchangeAmount(dataEntry,
					exchange.mathematicalRelation, exchange.amount,
					isCalculatedAmount, exchange.uncertainty);

			SPSubstance substance = new SPSubstance(name, unit);
			if (exchange.casNumber != null && !exchange.casNumber.equals(""))
				substance.setCASNumber(exchange.casNumber);

			// compartment/subcompartment
			String comp = ConversionUtils.getLangEntry(exchange
					.getCompartment());
			String subCompName = ConversionUtils.getLangEntry(exchange
					.getSubCompartment());
			CompartmentModel model = compartments.get(comp + subCompName);
			ElementaryFlowType type = null;
			SubCompartment subComp = null;

			String errmsg = "Null or unknown compartment: '"
					+ ConversionUtils.getLangEntry(exchange.getName()) + "'";

			if (model == null)
				logger.severe(errmsg);
			else {
				model.getElemFlowType();
				type = model.getElemFlowType();

				// sub Compartment
				subComp = model.getSubCompartment();
			}

			if (type == null)
				logger.severe(errmsg);

			SPElementaryFlow flow = new SPElementaryFlow(type, substance, unit,
					amount);
			dataEntry.add(flow);

			if (subComp != null)
				flow.setSubCompartment(subComp);

			// name
			flow.setName(name);

			// uncertainty
			if (exchange.uncertainty != null)
				flow.setDistribution(ConversionUtils
						.getDistribution(exchange.uncertainty));

			// comment
			String comment = getExchangeComment(exchange.getComment(),
					exchange.getSynonym(), exchange.getTags(),
					exchange.getProperties(),
					exchange.getTransferCoefficients(), exchange.sourceYear,
					exchange.sourceFirstAuthor, exchange.pageNumbers);

			if (comment == null)
				comment = "";

			if (exchange.formula != null && !exchange.formula.equals(""))
				comment += "\nFormula: " + exchange.formula + "\n";

			flow.setComment(comment);

		}
	}

	void paramter(SPDataEntry dataEntry, ES2Dataset dataset) {
		for (ES2Parameter parameter : dataset.getParameters()) {
			// comment
			StringBuilder comment = new StringBuilder();
			if (parameter.getComment().size() > 0)
				comment.append(ConversionUtils.getLangEntry(parameter
						.getComment()) + "\n");
			if (parameter.getUnitName().size() > 0)
				comment.append(ConversionUtils.getLangEntry(parameter
						.getUnitName()) + "\n");

			if (parameter.isCalculatedAmount) {
				SPCalculatedParameter calcParameter = new SPCalculatedParameter(
						parameter.variableName, parameter.mathematicalRelation);

				if (comment.length() > 0)
					calcParameter.setComment(comment.toString());
			} else {
				SPInputParameter inputParameter = new SPInputParameter(
						parameter.variableName, parameter.amount);

				if (parameter.uncertainty != null)
					inputParameter.setDistribution(ConversionUtils
							.getDistribution(parameter.uncertainty));

				if (comment.length() > 0)
					inputParameter.setComment(comment.toString());
			}

		}
	}

	private String getExchangeComment(List<LangString> comments,
			List<LangString> synonyms, List<String> tags,
			List<ES2Property> properties,
			List<ES2TransferCoefficient> transferCoefficients,
			String sourceYear, String sourceFirstAuthor, String pageNumbers) {

		StringBuilder comment = new StringBuilder();
		String com = ConversionUtils.getLangEntry(comments);
		if (com != null)
			comment.append(com + "\n");

		// tag to comment
		for (String tag : tags) {
			comment.append("Tag: " + tag + "\n");
		}

		// synonym
		String synm = ConversionUtils.getLangEntry(synonyms);
		if (synm != null)
			comment.append("Synonym: " + synm + "\n");

		// @sourceYear
		if (sourceYear != null)
			comment.append("source year: " + sourceYear + "\n");

		// @sourceFirstAuthor
		if (sourceFirstAuthor != null)
			comment.append("source first author: " + sourceFirstAuthor + "\n");

		// @pageNumbers
		if (pageNumbers != null)
			comment.append("page numbers: " + pageNumbers + "\n");

		// transferCoefficient
		String tc = ConversionUtils
				.getTransferCoefficientAsString(transferCoefficients);
		if (tc != null)
			comment.append(tc);

		// property
		String property = ConversionUtils.getPropertiesAsString(properties);
		if (property != null)
			comment.append(property);

		if (comment.length() != 0)
			return null;
		else
			return comment.toString();

	}

	private String getExchangeAmount(SPDataEntry dataEntry,
			String mathematicalRelation, double amount,
			boolean isCalculatedAmount, ES2Uncertainty uncertainty) {
		String result = null;

		if (isCalculatedAmount && mathematicalRelation != null
				&& !mathematicalRelation.equals("")) {
			result = mathematicalRelation;
			for (SPInputParameter parameter : paramterIndex.values()) {
				if (mathematicalRelation.contains(parameter.getName()))
					dataEntry.add(parameter);
			}

		} else {
			if (uncertainty != null) {
				if (uncertainty.logNormalDistribution != null) {
					if (uncertainty.logNormalDistribution.meanValue != 0)
						result = String
								.valueOf(uncertainty.logNormalDistribution.meanValue);
				} else if (uncertainty.normalDistribution != null) {
					if (uncertainty.normalDistribution.meanValue != 0)
						result = String
								.valueOf(uncertainty.normalDistribution.meanValue);
				} else if (uncertainty.triangularDistribution != null) {
					if (uncertainty.triangularDistribution.mostLikelyValue != 0)
						result = String
								.valueOf(uncertainty.triangularDistribution.mostLikelyValue);
				} else if (uncertainty.uniformDistribution != null) {
					if (uncertainty.uniformDistribution.mostFrequentValue != 0)
						result = String
								.valueOf(uncertainty.uniformDistribution.mostFrequentValue);
				} else {
					result = String.valueOf(amount);
				}

			} else {
				result = String.valueOf(amount);
			}
		}

		return result;
	}

}
