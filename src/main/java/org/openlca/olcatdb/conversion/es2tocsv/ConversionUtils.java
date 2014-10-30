package org.openlca.olcatdb.conversion.es2tocsv;

import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.ES2PedigreeMatrix;
import org.openlca.olcatdb.ecospold2.ES2Property;
import org.openlca.olcatdb.ecospold2.ES2TransferCoefficient;
import org.openlca.olcatdb.ecospold2.ES2Uncertainty;

import com.greendeltatc.simapro.csv.model.IDistribution;
import com.greendeltatc.simapro.csv.model.SPLogNormalDistribution;
import com.greendeltatc.simapro.csv.model.SPNormalDistribution;
import com.greendeltatc.simapro.csv.model.SPPedigreeMatrix;
import com.greendeltatc.simapro.csv.model.SPTriangleDistribution;
import com.greendeltatc.simapro.csv.model.SPUniformDistribution;
import com.greendeltatc.simapro.csv.model.pedigreetypes.SampleSize;

/**
 * 
 * @author Imo Graf
 * 
 */
public final class ConversionUtils {

	/**
	 * The language
	 */
	static String lang = null;

	/**
	 * 
	 * @param list
	 * @return
	 */
	static String getLangEntry(List<LangString> list) {
		String result = null;

		if (lang != null && !lang.equals("")) {
			for (LangString s : list) {
				if (s.getLangCode().equals(lang)) {
					result = s.getValue();
					break;
				}
			}
		}

		if (result == null && list.size() > 0)
			result = list.get(0).getValue();

		return result;
	}

	/**
	 * 
	 * @param transferCoefficients
	 * @return
	 */
	static String getTransferCoefficientAsString(
			List<ES2TransferCoefficient> transferCoefficients) {

		StringBuilder builder = new StringBuilder();
		for (ES2TransferCoefficient transferCoefficient : transferCoefficients) {

			builder.append("\n### Transfer Coefficient ###\n");

			// @amount
			if (transferCoefficient.amount != null)
				builder.append("amount: " + transferCoefficient.amount + "\n");

			// @mathematicalRelation
			if (transferCoefficient.mathematicalRelation != null)
				builder.append("mathematical realtion: "
						+ transferCoefficient.mathematicalRelation + "\n");

			// @isCalculatedAmount
			builder.append("is calculated amount: "
					+ transferCoefficient.isCalculatedAmount + "\n");

			// uncertainty
			String uncertainty = null;
			if (transferCoefficient.uncertainty != null)
				uncertainty = getUncertaintyAsString(transferCoefficient.uncertainty);
			if (uncertainty != null)
				builder.append(uncertainty);

			// @sourceYear
			if (transferCoefficient.sourceYear != null)
				builder.append("source year: " + transferCoefficient.sourceYear
						+ "\n");

			// @sourceFirstAuthor
			if (transferCoefficient.sourceFirstAuthor != null)
				builder.append("source first author:_"
						+ transferCoefficient.sourceFirstAuthor + "\n");

			// comment
			String comment = getLangEntry(transferCoefficient
					.getGeneralComment());
			if (comment != null)
				builder.append("comment: " + comment + "\n");
		}

		if (builder.length() != 0)
			return builder.toString();
		else
			return null;
	}

	/**
	 * 
	 * @param uncertainty
	 * @return
	 */
	static IDistribution getDistribution(ES2Uncertainty uncertainty) {
		IDistribution distribution = null;
		String massage = " : not implemented yet.";

		SPPedigreeMatrix pedigreeMatrix = new SPPedigreeMatrix();
		ES2PedigreeMatrix es2PedigreeMatrix = uncertainty.pedigreeMatrix;
		if (es2PedigreeMatrix != null) {

			pedigreeMatrix.setCompleteness(String
					.valueOf(es2PedigreeMatrix.completeness));

			pedigreeMatrix.setFurtherTechnologicalCorrelation(String
					.valueOf(es2PedigreeMatrix.furtherTechnologyCorrelation));

			pedigreeMatrix.setGeographicalCorrelation(String
					.valueOf(es2PedigreeMatrix.geographicalCorrelation));

			pedigreeMatrix.setReliability(String
					.valueOf(es2PedigreeMatrix.reliability));

			pedigreeMatrix.setTemporalCorrelation(String
					.valueOf(es2PedigreeMatrix.temporalCorrelation));

			// NA because this field no longer exist in ES2 schema
			pedigreeMatrix.sampleSize = SampleSize.NA;

		}

		if (uncertainty.logNormalDistribution != null) {
			double variance = 0;
			variance = uncertainty.logNormalDistribution.variance;

			if (variance <= 0)
				variance = pedigreeMatrix.getCalculatedUncertainty();
			else
				variance = calcSquaredStandardDeviation(variance);

			variance = Math.rint(variance * 100) / 100.;

			distribution = new SPLogNormalDistribution(variance, pedigreeMatrix);

		} else if (uncertainty.normalDistribution != null) {
			distribution = new SPNormalDistribution(
					uncertainty.normalDistribution.variance);

		} else if (uncertainty.triangularDistribution != null) {
			distribution = new SPTriangleDistribution(
					uncertainty.triangularDistribution.minValue,
					uncertainty.triangularDistribution.maxValue);

		} else if (uncertainty.uniformDistribution != null) {
			distribution = new SPUniformDistribution(
					uncertainty.uniformDistribution.minValue,
					uncertainty.uniformDistribution.maxValue);

		} else if (uncertainty.betaDistribution != null) {
			throw new IllegalArgumentException("beta uncertainty" + massage);

		} else if (uncertainty.erlangDistribution != null) {
			throw new IllegalArgumentException("erlang uncertainty" + massage);

		} else if (uncertainty.gammaDistribution != null) {
			throw new IllegalArgumentException("gamma uncertainty" + massage);
		}

		return distribution;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	static String getPropertiesAsString(List<ES2Property> properties) {
		StringBuilder comment = new StringBuilder();

		for (ES2Property property : properties) {
			comment.append("\n#### Property ###\n");
			// name
			String name = getLangEntry(property.getName());
			if (name != null)
				comment.append("name: " + name + "\n");

			// unitName
			String unitName = getLangEntry(property.getUnitName());
			if (unitName != null)
				comment.append("unit name: " + unitName + "\n");

			// @amount
			comment.append("amount: " + property.amount + "\n");

			// @isCalculatedAmount
			comment.append("is calculated amount: "
					+ property.isCalculatedAmount + "\n");

			// @isDefiningValue
			comment.append("is defining value: " + property.isDefiningValue
					+ "\n");

			// @mathematicalRelation
			if (property.mathematicalRelation != null)
				comment.append("mathematical relation: "
						+ property.mathematicalRelation + "\n");

			// @variableName
			if (property.variableName != null)
				comment.append("variabel name: " + property.variableName + "\n");

			// uncertainty
			ES2Uncertainty uncertainty = property.uncertainty;
			String unc = getUncertaintyAsString(uncertainty);
			if (unc != null)
				comment.append(unc);

			// @sourceYear
			if (property.sourceYear != null)
				comment.append("source year: " + property.sourceYear + "\n");

			// @sourceFirstAuthor
			if (property.sourceFirstAuthor != null)
				comment.append("source first author: "
						+ property.sourceFirstAuthor + "\n");

			// comment
			String com = getLangEntry(property.getComment());
			if (com != null)
				comment.append("comment: " + com + "\n");
		}

		if (comment.length() != 0)
			return comment.toString();
		else
			return null;
	}

	/**
	 * 
	 * @param uncertainty
	 * @return
	 */
	static String getUncertaintyAsString(ES2Uncertainty uncertainty) {
		StringBuilder builder = new StringBuilder();

		if (uncertainty != null) {
			if (uncertainty.betaDistribution != null) {
				builder.append("\n### Beta Distribution ###\n");
				builder.append("max: " + uncertainty.betaDistribution.maxValue
						+ "\n");
				builder.append("min: " + uncertainty.betaDistribution.minValue
						+ "\n");
				builder.append("most frequent: "
						+ uncertainty.betaDistribution.mostFrequent + "\n");

			} else if (uncertainty.erlangDistribution != null) {
				builder.append("\n### Erlang Distribution ###\n");
				builder.append("mean value: "
						+ uncertainty.erlangDistribution.meanValue + "\n");
				builder.append("oder: " + uncertainty.erlangDistribution.order
						+ "\n");

			} else if (uncertainty.gammaDistribution != null) {
				builder.append("\n### Gamma Distribution ###\n");
				builder.append("min: " + uncertainty.gammaDistribution.minValue
						+ "\n");
				builder.append("scale: " + uncertainty.gammaDistribution.scale
						+ "\n");
				builder.append("shape: " + uncertainty.gammaDistribution.shape
						+ "\n");

			} else if (uncertainty.logNormalDistribution != null) {
				builder.append("\n### Log Normal Distribution ###\n");
				builder.append("mean value: "
						+ uncertainty.logNormalDistribution.meanValue + "\n");
				builder.append("mu: " + uncertainty.logNormalDistribution.mu
						+ "\n");
				builder.append("standard deviation 95: "
						+ uncertainty.logNormalDistribution.standardDeviation95
						+ "\n");
				builder.append("variance: "
						+ uncertainty.logNormalDistribution.variance + "\n");
				builder.append("variance with pedigree uncertainty: "
						+ uncertainty.logNormalDistribution.varianceWithPedigreeUncertainty
						+ "\n");

			} else if (uncertainty.normalDistribution != null) {
				builder.append("\n### Normal Distribution ###\n");
				builder.append("mean value: "
						+ uncertainty.normalDistribution.meanValue + "\n");
				builder.append("standard deviation 95: "
						+ uncertainty.normalDistribution.standardDeviation95
						+ "\n");
				builder.append("variance: "
						+ uncertainty.normalDistribution.variance + "\n");

			} else if (uncertainty.triangularDistribution != null) {
				builder.append("\n### Triangular Distribution ###\n");
				builder.append("min: "
						+ uncertainty.triangularDistribution.minValue + "\n");
				builder.append("max: "
						+ uncertainty.triangularDistribution.maxValue + "\n");
				builder.append("most likely value: "
						+ uncertainty.triangularDistribution.mostLikelyValue
						+ "\n");

			} else if (uncertainty.uniformDistribution != null) {
				builder.append("\n### Uniform Distribution ###\n");
				builder.append("min: "
						+ uncertainty.uniformDistribution.minValue + "\n");
				builder.append("max: "
						+ uncertainty.uniformDistribution.maxValue + "\n");

			} else if (uncertainty.undefinedDistribution != null) {
				builder.append("\n### Undefined Distribution ###\n");
				builder.append("min: "
						+ uncertainty.undefinedDistribution.minValue + "\n");
				builder.append("max: "
						+ uncertainty.undefinedDistribution.maxValue + "\n");
				builder.append("standard deviation 95: "
						+ uncertainty.undefinedDistribution.standardDeviation95
						+ "\n");
			}

			builder.append("pedigree uncertainty included: "
					+ uncertainty.pedigreeUncertaintyIncluded + "\n");

			ES2PedigreeMatrix matrix = uncertainty.pedigreeMatrix;
			if (matrix != null) {
				builder.append("\n ### Pedigree matrix ###\n");
				builder.append("completeness: " + matrix.completeness + "\n");
				builder.append("further technology correlation:"
						+ matrix.furtherTechnologyCorrelation + "\n");
				builder.append("geographical correlation: "
						+ matrix.geographicalCorrelation + "\n");
				builder.append("reliability: " + matrix.reliability + "\n");
				builder.append("temporal correlation: "
						+ matrix.temporalCorrelation + "\n");
			}
		}

		if (builder.length() != 0)
			return builder.toString();
		else
			return null;
	}

	/**
	 * 
	 * @param variance
	 * @return
	 */
	static double calcSquaredStandardDeviation(Double variance) {
		double result = 0;

		if (variance > 0)
			result = Math.pow((Math.exp((Math.sqrt(variance)))), 2);

		return result;
	}

}
