package org.openlca.olcatdb.conversion;

import org.openlca.olcatdb.ecospold1.ES1Dataset;
import org.openlca.olcatdb.ecospold1.ES1Exchange;
import org.openlca.olcatdb.ecospold1.ES1Person;
import org.openlca.olcatdb.ecospold1.ES1Validation;

public class ES1SimaProValidConversion {

	private SPMappingFile mappingFile = new SPMappingFile();

	public SPMappingFile getMappingFile() {
		return mappingFile;
	}

	public void validate(ES1Dataset dataSet) {
		checkRequiredElements(dataSet);
		checkRefFlowName(dataSet);
		checkRequiredExchangeAttributes(dataSet);
		correctSubCategory(dataSet);
	}

	private void checkRequiredElements(ES1Dataset dataSet) {
		// Check element validation
		if (dataSet.validation == null) {
			ES1Validation validation = new ES1Validation();

			// Add person unknown
			int personId = 0;
			if (dataSet.getPerson(personId) == null) {
				ES1Person person = new ES1Person();
				person.number = personId;
				person.name = "unknown";
				dataSet.getPersons().add(person);
			}

			validation.proofReadingDetails = "unknown";
			validation.proofReadingValidator = personId;
			dataSet.validation = validation;
		}

	}

	/**
	 * Check if the process name and the RefFlow name are equals
	 * 
	 * @param dataSet
	 */
	private void checkRefFlowName(ES1Dataset dataSet) {
		for (ES1Exchange exchange : dataSet.getExchanges()) {
			if (exchange.outputGroup != null && exchange.outputGroup == 0) {
				if (!exchange.name.equals(dataSet.referenceFunction.name)) {
					exchange.name = dataSet.referenceFunction.name;
				}
			}
		}
	}

	private void checkRequiredExchangeAttributes(ES1Dataset dataSet) {
		for (ES1Exchange exchange : dataSet.getExchanges()) {
			// Location
			if (exchange.location == null) {
				exchange.location = "";
			}

			// UncertaintyType
			if (exchange.uncertaintyType == null) {
				exchange.uncertaintyType = 0;
			}

			// Categories for elementary flows
			if (exchange.inputGroup != null && exchange.inputGroup == 4) {
				if (exchange.category == null) {
					exchange.category = "unspecified";
				}

				if (exchange.subCategory == null) {
					exchange.subCategory = "unspecified";
				}
			}
		}
	}

	private void correctSubCategory(ES1Dataset dataSet) {
		for (ES1Exchange exchange : dataSet.getExchanges()) {
			if (exchange.category != null) {
				if (exchange.category.equals("air")) {
					if (exchange.subCategory.equals("high population density")) {
						exchange.subCategory = "high. pop.";
					}
				}

				if (exchange.category.equals("air")) {
					if (exchange.subCategory.equals("low population density")) {
						exchange.subCategory = "low. pop.";
					}
				}

				if (exchange.category.equals("air")) {
					if (exchange.subCategory
							.equals("low population density, long-term")) {
						exchange.subCategory = "low. pop., long-term";
					}
				}

				if (exchange.category.equals("air")) {
					if (exchange.subCategory
							.equals("lower stratosphere + upper tropos")) {
						exchange.subCategory = "stratosphere";
					}
				}

				if (exchange.category.equals("water")) {
					if (exchange.subCategory.equals("fossil-")) {
						exchange.subCategory = "fossilwater";
					}
				}

				if (exchange.category.equals("water")) {
					if (exchange.subCategory.equals("ground-")) {
						exchange.subCategory = "groundwater";
					}
				}

				if (exchange.category.equals("water")) {
					if (exchange.subCategory.equals("ground-, long-term")) {
						exchange.subCategory = "groundwater, long";
					}
				}
			}
		}
	}

	public void processForMappingFile(ES1Dataset dataSet) {
		for (ES1Exchange exchange : dataSet.getExchanges()) {
			SPCategory category = SPCategory.getCategory(exchange.category,
					exchange.subCategory, null);
			mappingFile.add(category);
		}
	}

}
