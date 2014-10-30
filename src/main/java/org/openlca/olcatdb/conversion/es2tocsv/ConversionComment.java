package org.openlca.olcatdb.conversion.es2tocsv;

import org.openlca.olcatdb.ecospold2.ES2ClassificationRef;
import org.openlca.olcatdb.ecospold2.ES2Dataset;
import org.openlca.olcatdb.ecospold2.ES2FileAttributes;
import org.openlca.olcatdb.ecospold2.ES2RequiredContext;
import org.openlca.olcatdb.ecospold2.ES2Review;

import com.greendeltatc.simapro.csv.model.SPDataEntry;
import com.greendeltatc.simapro.csv.model.SPDocumentation;
import com.greendeltatc.simapro.csv.model.SPLiteratureReference;
import com.greendeltatc.simapro.csv.model.SPLiteratureReferenceEntry;

/**
 * 
 * @author Imo Graf
 * 
 */
public final class ConversionComment {

	static void literatureReference(SPDataEntry dataEntry, ES2Dataset dataset) {
		SPLiteratureReference literatureReference = new SPLiteratureReference(
				"EcoSpold 2 data generator and publication", "", "EcoSpold");

		SPLiteratureReferenceEntry entry = new SPLiteratureReferenceEntry(
				literatureReference);

		dataEntry.getDocumentation().add(entry);

		StringBuilder builder = new StringBuilder();

		builder.append("#### Data generator and publication ###\n");

		// @dataPublishedIn
		if (dataset.publication.dataPublishedIn != null)
			builder.append("data published in: "
					+ dataset.publication.dataPublishedIn + "\n");

		// @publishedSourceYear
		if (dataset.publication.publishedSourceYear != null)
			builder.append("published source year:  "
					+ dataset.publication.publishedSourceYear + "\n");

		// @publishedSourceFirstAuthor
		if (dataset.publication.publishedSourceFirstAuthor != null)
			builder.append("published source first author: "
					+ dataset.publication.publishedSourceFirstAuthor + "\n");

		// @pageNumbers
		if (dataset.publication.pageNumbers != null)
			builder.append("page numbers: " + dataset.publication.pageNumbers
					+ "\n");

		// @isCopyrightProtected
		if (dataset.publication.isCopyrightProtected)
			builder.append("is copyright protected: "
					+ dataset.publication.isCopyrightProtected + "\n");

		// @accessRestrictedTo
		if (dataset.publication.accessRestrictedTo != null)
			builder.append("access restricted to: "
					+ dataset.publication.accessRestrictedTo + "\n");

		// @companyCode
		if (dataset.publication.companyCode != null)
			builder.append("company code: " + dataset.publication.companyCode);

		entry.setComment(builder.toString());
	}

	static void record(SPDataEntry dataEntry, ES2Dataset dataset) {
		StringBuilder record = new StringBuilder();

		// Administrative information

		if (!(dataset.dataEntryBy == null
				&& !dataset.dataEntryBy.isActiveAuthor
				&& dataset.dataEntryBy.personName == null && dataset.dataEntryBy.personEmail == null))
			record.append("### Administrative information ###\n");

		// @personName
		if (dataset.dataEntryBy.personName != null)
			record.append("person name: " + dataset.dataEntryBy.personName
					+ "\n");

		// @isActiveAuthor
		if (dataset.dataEntryBy.isActiveAuthor)
			record.append("is active author: "
					+ dataset.dataEntryBy.isActiveAuthor + "\n");

		// @personEmail
		if (dataset.dataEntryBy.personEmail != null)
			record.append("person email: " + dataset.dataEntryBy.personEmail
					+ "\n");

		// Required Context Reference
		for (ES2RequiredContext context : dataset.fileAttributes
				.getRequiredContexts()) {
			record.append("\n### Context Reference ###\n");

			// @majorRelease
			record.append("major release: " + context.majorRelease + "\n");

			// @minorRelease
			record.append("minor release: " + context.minorRelease + "\n");

			// @majorRevision
			record.append("major revision: " + context.majorRevision + "\n");

			// @minorRevision
			record.append("minor revision: " + context.minorRevision + "\n");

			// @requiredContextFileLocation
			if (context.requiredContextFileLocation != null)
				record.append("required context file location: "
						+ context.requiredContextFileLocation + "\n");

			// requiredContextName
			String requiredContextName = ConversionUtils.getLangEntry(context
					.getRequiredContextName());
			if (requiredContextName != null)
				record.append("required context name: " + requiredContextName
						+ "\n");
		}

		if (record.length() != 0)
			dataEntry.getDocumentation().setRecord(record.toString());
	}

	static void generator(SPDataEntry dataEntry, ES2Dataset dataset) {

		StringBuilder generator = new StringBuilder();

		if (dataset.publication.personName != null
				|| dataset.publication.personEmail != null) {
			generator.append("#### Data generator and publication ###\n");
			// Data generator and publication

			// @personName
			if (dataset.publication.personName != null)
				generator.append("person name: "
						+ dataset.publication.personName + "\n");

			// @personEmail
			if (dataset.publication.personEmail != null)
				generator.append("person email: "
						+ dataset.publication.personEmail);
		}

		if (generator.length() != 0)
			dataEntry.getDocumentation().setGenerator(generator.toString());
	}

	static void collectionMethodANDDataTreatment(SPDataEntry dataEntry,
			ES2Dataset dataset) {

		// samplingProcedure
		String samplingProcedure = ConversionUtils
				.getLangEntry(dataset.representativeness.getSamplingProcedure());
		if (samplingProcedure != null)
			dataEntry.getDocumentation().setCollectionMethod(
					"### Sampling Procedure ###\n" + samplingProcedure);

		// extrapolations
		String extrapolations = ConversionUtils
				.getLangEntry(dataset.representativeness.getExtrapolations());
		if (extrapolations != null)
			dataEntry.getDocumentation().setDataTreatment(
					"### Extrapolations ###\n" + extrapolations);
	}

	static void allocationRules(SPDataEntry dataEntry, ES2Dataset dataset) {
		if (dataset.description != null
				&& dataset.description.allocationComment != null) {
			String alloc = ConversionUtils
					.getLangEntry(dataset.description.allocationComment
							.getText());
			// allocation comment
			if (alloc != null)
				dataEntry.getDocumentation().setAllocationRules(
						"### Allocation comment ###\n" + alloc);
		}
		if (dataEntry.getDocumentation().getAllocationRules() == null)
			dataEntry.getDocumentation().setAllocationRules("");
	}

	static void verification(SPDataEntry dataEntry, ES2Dataset dataset) {

		StringBuilder verification = new StringBuilder();
		for (ES2Review review : dataset.getReviews()) {
			if (!verification.toString().equals(""))
				verification.append("\n\n");

			verification.append("### Review ###\n");

			// @reviewerName
			if (review.reviewerName != null)
				verification.append("reviewer name: " + review.reviewerName
						+ "\n");

			// @reviewerEmail
			if (review.reviewerEmail != null)
				verification.append("reviewer email: " + review.reviewerEmail
						+ "\n");

			// @reviewDate
			if (review.reviewDate != null)
				verification.append("reviewer date: " + review.reviewDate
						+ "\n");

			// @reviewedMajorRelease
			verification.append("reviewed major release:"
					+ review.reviewedMajorRelease + "\n");

			// @reviewedMinorRelease
			verification.append("reviewed Minor Release: "
					+ review.reviewedMinorRelease + "\n");

			// @reviewedMajorRevision
			verification.append("reviewed major revision: "
					+ review.reviewedMajorRevision + "\n");

			// @reviewedMinorRevision
			verification.append("reviewed minor revision: "
					+ review.reviewedMinorRevision + "\n");

			// details
			String details = ConversionUtils.getLangEntry(review.details
					.getText());
			if (details != null)
				verification.append("details: " + details + "\n");

			// otherDetails
			String otherDetails = ConversionUtils.getLangEntry(review
					.getOtherDetails());
			if (otherDetails != null)
				verification.append("other details: " + otherDetails);
		}

		dataEntry.getDocumentation().setVerification(verification.toString());

	}

	static void documentationComment(SPDataEntry dataEntry, ES2Dataset dataset) {
		SPDocumentation doc = dataEntry.getDocumentation();
		StringBuilder comment = new StringBuilder();

		comment.append("\n### Activity Description ###\n");

		if (dataset.description.generalComment != null)
			comment.append(ConversionUtils
					.getLangEntry(dataset.description.generalComment.getText())
					+ "\n");

		// @inheritanceDepth
		if (dataset.description.inheritanceDepth != null)
			comment.append("Inheritance depth: "
					+ dataset.description.inheritanceDepth + "\n");

		// @tag
		for (String tag : dataset.description.getTags()) {
			comment.append("tag: " + tag + "\n");
		}

		// @synonyms
		String synonym = ConversionUtils.getLangEntry(dataset.description
				.getSynonyms());
		if (synonym != null) {
			comment.append("synonym: " + synonym + "\n");
		}

		// @specialActivityType
		comment.append("special activity type: "
				+ dataset.description.specialActivityType + "\n");

		// includedActivitiesStart
		String includedActivitiesStart = ConversionUtils
				.getLangEntry(dataset.description.getIncludedActivitiesStart());
		if (includedActivitiesStart != null)
			comment.append("included activitie start: "
					+ includedActivitiesStart + "\n");

		// includedActivitiesEnd
		String includedActivitiesEnd = ConversionUtils
				.getLangEntry(dataset.description.getIncludedActivitiesEnd());
		if (includedActivitiesEnd != null) {
			comment.append("included activitie end: " + includedActivitiesEnd
					+ "\n");

		}

		// @energyValues
		comment.append("energy value: " + dataset.description.energyValues
				+ "\n");

		// @datasetIcon
		if (dataset.description.datasetIcon != null)
			comment.append("dataset icon: " + dataset.description.datasetIcon
					+ "\n");

		// classifications
		for (ES2ClassificationRef classification : dataset.getClassifications()) {
			// classificationSystem
			String classificationSystem = ConversionUtils
					.getLangEntry(classification.getClassificationSystem());
			if (classificationSystem != null)
				comment.append("classification system: " + classificationSystem
						+ "\n");

			// classificationValue
			String classificationValue = ConversionUtils
					.getLangEntry(classification.getClassificationValue());
			if (classificationValue != null)
				comment.append("classification value: " + classificationValue
						+ "\n");

		}

		// geography
		String geoComment = ConversionUtils
				.getLangEntry(dataset.geography.comment.getText());
		if (geoComment != null) {
			comment.append("geography: " + geoComment + "\n");
		}

		// technology
		String technology = ConversionUtils
				.getLangEntry(dataset.technology.comment.getText());
		if (technology != null)
			comment.append("technology: " + technology + "\n");

		// time period
		String timePeriod = ConversionUtils
				.getLangEntry(dataset.timePeriod.comment.getText());
		if (timePeriod != null)
			comment.append("time period: " + timePeriod + "\n");
		comment.append("data valid for entire period: "
				+ dataset.timePeriod.dataValidForEntirePeriod + "\n");

		// Macro-economic Scenario
		String macroName = ConversionUtils
				.getLangEntry(dataset.macroEconomicScenario.getName());
		if (macroName != null)
			comment.append("macro-economic Scenario name: " + macroName + "\n");

		String macroComment = ConversionUtils
				.getLangEntry(dataset.macroEconomicScenario.getComment());
		if (macroComment != null)
			comment.append("macro-economic Scenario comment: " + macroComment
					+ "\n");

		// File attributes
		comment.append("\n### File attrubutes ###\n");

		ES2FileAttributes fileAttributes = dataset.fileAttributes;

		// @majorRelease
		comment.append("major release: " + fileAttributes.majorRelease + "\n");

		// @minorRelease
		comment.append("minor release: " + fileAttributes.minorRelease + "\n");

		// @majorRevision
		comment.append("major revision: " + fileAttributes.majorRevision + "\n");

		// @minorRevision
		comment.append("minor revision: " + fileAttributes.minorRevision + "\n");

		// @defaultLanguage
		if (fileAttributes.defaultLanguage != null)
			comment.append("default language: "
					+ fileAttributes.defaultLanguage + "\n");

		// @creationTimestamp
		if (fileAttributes.creationTimestamp != null)
			comment.append("creation timestamp: "
					+ fileAttributes.creationTimestamp + "\n");

		// @lastEditTimestamp
		if (fileAttributes.lastEditTimestamp != null)
			comment.append("last edit timestamp: "
					+ fileAttributes.lastEditTimestamp + "\n");

		// @internalSchemaVersion
		if (fileAttributes.internalSchemaVersion != null)
			comment.append("internal schema version: "
					+ fileAttributes.internalSchemaVersion + "\n");

		// @fileGenerator
		if (fileAttributes.fileGenerator != null)
			comment.append("file generator: " + fileAttributes.fileGenerator
					+ "\n");

		// @fileTimestamp
		if (fileAttributes.fileTimestamp != null)
			comment.append("file timestamp: " + fileAttributes.fileTimestamp
					+ "\n");

		// contextName
		String contextName = ConversionUtils.getLangEntry(fileAttributes
				.getContextNames());
		if (contextName != null)
			comment.append("context name: " + contextName + "\n");

		// representativeness
		comment.append("\n### Representativeness ###\n");

		// systemModelName
		String systemModelName = ConversionUtils
				.getLangEntry(dataset.representativeness.getSystemModelName());
		if (systemModelName != null)
			comment.append("system model name: " + systemModelName + "\n");

		// @percent
		comment.append("percent: " + dataset.representativeness.percent + "\n");

		doc.setComment(comment.toString());
	}

}
