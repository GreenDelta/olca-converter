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

@Context(name = "review", parentName = "validation")
public class ILCDReview extends ContextObject {

	/**
	 * Type of review that has been performed regarding independency and type of
	 * review process.
	 * 
	 * @Attribute
	 */
	@ContextField(
			name = "review", 
			parentName = "validation",
			attributeName="type",
			isAttribute = true)
	public String type;
	
	@ContextField(
			name = "referenceToCompleteReviewReport",
			parentName = "review",
			type = Type.DataSetReference
	)
	public DataSetReference reviewReport;
	
	
	@SubContext(
			contextClass = ILCDReviewScope.class, 
			isMultiple = true)	
	private List<ILCDReviewScope> scopes 
		= new ArrayList<ILCDReviewScope>();

	public List<ILCDReviewScope> getScopes() {
		return scopes;
	}
	
	@SubContext(
			contextClass = ILCDQualityIndicator.class, 
			isMultiple = true)	
	private List<ILCDQualityIndicator> indicators
		= new ArrayList<ILCDQualityIndicator>();
	
	public List<ILCDQualityIndicator> getIndicators() {
		return indicators;
	}
	
	@ContextField(
			name = "reviewDetails",
			parentName = "review",
			isMultiple = true,
			type = Type.MultiLangText
	)
	private List<LangString> details 
		= new ArrayList<LangString>();
		
	public List<LangString> getDetails() {
		return details;
	}
	
	@ContextField(
			name = "referenceToNameOfReviewerAndInstitution",
			parentName = "review",	
			isMultiple = true,
			type = Type.DataSetReference
	)
	private List<DataSetReference> reviewers
		= new ArrayList<DataSetReference>();
	
	public List<DataSetReference> getReviewers() {
		return reviewers;
	}
	
	@ContextField(
			name = "otherReviewDetails",
			parentName = "review",
			isMultiple = true,
			type = Type.MultiLangText
	)
	private List<LangString> otherDetails
		= new ArrayList<LangString>();
	
	public List<LangString> getOtherDetails() {
		return otherDetails;
	}
	
	
}
