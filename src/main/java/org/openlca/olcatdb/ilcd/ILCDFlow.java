package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The ILCD flow data set definition.
 * 
 * @author Michael Srocka
 * 
 */
@Context(name = "flowDataSet")
public class ILCDFlow extends ContextObject {

	/**
	 * The flow description.
	 * 
	 * @Element dataSetInformation
	 */
	@SubContext(contextClass = ILCDFlowDescription.class)
	public ILCDFlowDescription description;

	/**
	 * Pointer to the reference flow property.
	 * 
	 * @Element referenceToReferenceFlowProperty
	 */
	@ContextField(name = "referenceToReferenceFlowProperty", parentName = "quantitativeReference", type = Type.Integer)
	public Integer referenceFlowProperty;

	@ContextField(name = "locationOfSupply", parentName = "geography", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> location = new ArrayList<LangString>();

	/**
	 * The location of supply if the flow is a product.
	 * 
	 * @Element geography/locationOfSupply
	 */
	public List<LangString> getLocation() {
		return location;
	}

	/**
	 * The technology.
	 * 
	 * @Element technology
	 */
	@SubContext(contextClass = ILCDFlowTechnology.class)
	public ILCDFlowTechnology technology;

	/**
	 * Names the basic type of the flow.
	 * 
	 * @Element typeOfDataSet
	 */
	@ContextField(name = "typeOfDataSet", parentName = "LCIMethod")
	public String type;

	@SubContext(contextClass = ILCDCompliance.class, isMultiple = true)
	private List<ILCDCompliance> complianceDeclarations = new ArrayList<ILCDCompliance>();

	/**
	 * The compliance declarations.
	 * 
	 * @Element complianceDeclarations
	 */
	public List<ILCDCompliance> getComplianceDeclarations() {
		return complianceDeclarations;
	}

	/**
	 * The data entry by section.
	 * 
	 * @Element dataEntryBy
	 */
	@SubContext(contextClass = ILCDFlowEntry.class)
	public ILCDFlowEntry entry;

	/**
	 * The publication and ownership section.
	 * 
	 * @Element publicationAndOwnership
	 */
	@SubContext(contextClass = ILCDPublication.class)
	public ILCDPublication publication;

	/**
	 * List of flow properties with values.
	 * 
	 * @Element flowProperties
	 */
	@SubContext(contextClass = ILCDFlowPropertyValue.class, isMultiple = true)
	private List<ILCDFlowPropertyValue> flowPropertyValues = new ArrayList<ILCDFlowPropertyValue>();

	/**
	 * List of flow properties with values.
	 * 
	 * @Element flowProperties
	 */
	public List<ILCDFlowPropertyValue> getFlowPropertyValues() {
		return flowPropertyValues;
	}

	/**
	 * Returns the data set reference to the reference flow property of this
	 * data set. If there is no reference flow property defined this function
	 * returns <code>null</code>.
	 */
	public DataSetReference getReferenceProperty() {

		DataSetReference ref = null;

		if (referenceFlowProperty != null) {
			for (ILCDFlowPropertyValue v : this.flowPropertyValues) {
				if (v.id != null && v.id.equals(referenceFlowProperty)) {
					ref = v.flowPropertyDataSet;
				}
			}
		}

		return ref;
	}

	/**
	 * Returns <code>true</code> if the type of the flow is "Elementary flow"
	 * (element <code>typeOfDataSet</code>).
	 * 
	 */
	public boolean isElementary() {
		boolean b = false;
		if (type != null) {
			b = type.equals("Elementary flow");
		}
		return b;
	}
	
	/**
	 * Returns <code>true</code> if the type of the flow is "Waste flow"
	 * (element <code>typeOfDataSet</code>).
	 * 
	 */
	public boolean isWaste() {
		boolean b = false;
		if (type != null) {
			b = type.equals("Waste flow");
		}
		return b;
	}
	
	/**
	 * Returns <code>true</code> if the type of the flow is "Product flow"
	 * (element <code>typeOfDataSet</code>).
	 * 
	 */
	public boolean isProduct() {
		boolean b = false;
		if (type != null) {
			b = type.equals("Product flow");
		}
		return b;
	}

}
