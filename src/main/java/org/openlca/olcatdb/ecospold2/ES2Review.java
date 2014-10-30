package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.TextAndImage;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Contains information about the reviewers' comments on the dataset content.
 * 
 * @Element validation
 * @ContentModel (details?, otherDetails*, namespace:uri="##other")
 * 
 */
@Context(name = "review", parentName = "modellingAndValidation")
public class ES2Review extends ContextObject {

	@ContextField(name = "review", parentName = "modellingAndValidation", isAttribute = true, attributeName = "reviewDate")
	public String reviewDate;

	@ContextField(name = "review", parentName = "modellingAndValidation", isAttribute = true, attributeName = "reviewedMajorRelease", type = Type.Integer)
	public int reviewedMajorRelease;

	@ContextField(name = "review", parentName = "modellingAndValidation", isAttribute = true, attributeName = "reviewedMajorRevision", type = Type.Integer)
	public int reviewedMajorRevision;

	@ContextField(name = "review", parentName = "modellingAndValidation", isAttribute = true, attributeName = "reviewedMinorRelease", type = Type.Integer)
	public int reviewedMinorRelease;

	@ContextField(name = "review", parentName = "modellingAndValidation", isAttribute = true, attributeName = "reviewedMinorRelease", type = Type.Integer)
	public int reviewedMinorRevision;

	@ContextField(name = "review", parentName = "modellingAndValidation", isAttribute = true, attributeName = "reviewerContextId")
	public String reviewerContextId;

	@ContextField(name = "review", parentName = "modellingAndValidation", isAttribute = true, attributeName = "reviewerEmail")
	public String reviewerEmail;

	@ContextField(name = "review", parentName = "modellingAndValidation", isAttribute = true, attributeName = "reviewerId")
	public String reviewerId;

	@ContextField(name = "review", parentName = "modellingAndValidation", isAttribute = true, attributeName = "reviewerName")
	public String reviewerName;

	@ContextField(name = "details", parentName = "review", isMultiple = true, type = Type.TextAndImage)
	public TextAndImage details;

	@ContextField(name = "otherDetails", parentName = "review", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> otherDetails = new ArrayList<LangString>();

	public List<LangString> getOtherDetails() {
		return otherDetails;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ES2Review && obj != null) {
			ES2Review review = ES2Review.class.cast(obj);
			if (review.reviewerId != null && this.reviewerId != null
					&& review.reviewerId.equals(this.reviewerId))
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
}
