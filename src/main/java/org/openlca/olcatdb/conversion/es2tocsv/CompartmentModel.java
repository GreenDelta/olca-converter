package org.openlca.olcatdb.conversion.es2tocsv;

import com.greendeltatc.simapro.csv.model.types.ElementaryFlowType;
import com.greendeltatc.simapro.csv.model.types.SubCompartment;

public class CompartmentModel {
	private String simaProElemType = "";
	private String simaProSubcompartment = "";
	private String es2Compartment = "";
	private String es2Subcompartment = "";

	public String getSimaProElemType() {
		return simaProElemType;
	}

	public void setSimaProElemType(String simaProElemType) {
		this.simaProElemType = simaProElemType;
	}

	public String getSimaProSubcompartment() {
		return simaProSubcompartment;
	}

	public void setSimaProSubcompartment(String simaProSubcompartment) {
		this.simaProSubcompartment = simaProSubcompartment;
	}

	public String getEs2Compartment() {
		return es2Compartment;
	}

	public void setEs2Compartment(String es2Compartment) {
		this.es2Compartment = es2Compartment;
	}

	public String getEs2Subcompartment() {
		return es2Subcompartment;
	}

	public void setEs2Subcompartment(String es2Subcompartment) {
		this.es2Subcompartment = es2Subcompartment;
	}

	public ElementaryFlowType getElemFlowType() {
			return ElementaryFlowType.forValue(simaProElemType);
	}

	public SubCompartment getSubCompartment() {
		return SubCompartment.forValue(simaProSubcompartment);
	}
}
