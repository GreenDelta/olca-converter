package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Uncertainty information in the form of distribution functions and their
 * parameters and/or pedigree data. For the format definition see the complex
 * type section below.
 * 
 * @Element uncertainty
 * @ContentModel ((lognormal | normal | triangular | uniform | beta | gamma |
 *               erlang | undefined), pedigreeMatrix?, generalComment*,
 *               namespace:uri="##other")
 * 
 */
@Context(name = "uncertainty", parentNonStrict = true)
public class ES2Uncertainty extends ContextObject {

	/**
	 * Determines whether the uncertainty field is only basic uncertainty or
	 * includes the additional uncertainty calculated from the pedigree values.
	 * 
	 * @Attribute isPedigreeUncertaintyIncluded
	 * @DataType boolean
	 */
	@ContextField(name = "uncertainty", parentNonStrict = true, attributeName = "isPedigreeUncertaintyIncluded", isAttribute = true, type = Type.Boolean)
	public Boolean pedigreeUncertaintyIncluded;

	/**
	 * Beta distribution using minValue (a), maxValue (b) and mostFrequent (m)
	 * parameters to calculate the two shape parameters of the underlying Gamma
	 * distributions.'+' The parameters must follow this condition: ((a <= m)
	 * and (m <= b)) or (a = b). the shape values will be calculated by these
	 * formulas: shape1 = 1 + 4 * ((m-a) / (b-a)). shape2 = 6 - shape1.
	 * 
	 * @Element beta
	 */
	@SubContext(contextClass = ES2BetaDistribution.class)
	public ES2BetaDistribution betaDistribution;

	/**
	 * The Erlang-distribution is a continuos distribution often used in
	 * queueing theory. Mean parameter determines the average value, Order
	 * parameter is the order of the Erlang distribution and must be >= 1.
	 * 
	 * @Element erlang
	 */
	@SubContext(contextClass = ES2ErlangDistribution.class)
	public ES2ErlangDistribution erlangDistribution;

	/**
	 * Gamma distribution using scale and shape parameter. Absolute values of
	 * the values entered here will be used. The value of the minimum parameter
	 * will be added to all samples.
	 * 
	 * @Element gamma
	 */
	@SubContext(contextClass = ES2GammaDistribution.class)
	public ES2GammaDistribution gammaDistribution;

	/**
	 * The Lognormal-distribution with average value (MeanValue parameter) and
	 * variance s (Variance parameter) is a Normal-distribution, shaping the
	 * natural logarithm of the characteristic values ln(x) instead of x-values.
	 * 
	 * @Element lognormal
	 */
	@SubContext(contextClass = ES2LogNormalDistribution.class)
	public ES2LogNormalDistribution logNormalDistribution;

	/**
	 * Normal (also known as "Gaussian") distribution. It is a family of
	 * distributions of the same general form, differing in their location and
	 * scale parameters: the mean ("MeanValue") and standard deviation
	 * ("Deviation"), respectively.
	 * 
	 * @Element normal
	 * 
	 */
	@SubContext(contextClass = ES2NormalDistribution.class)
	public ES2NormalDistribution normalDistribution;

	/**
	 * #missing.# Parameter are minValue, mostLikelyValue, maxValue. In case of
	 * triangular uncertainty distribution, the meanValue shall be calculated
	 * from the mostLikelyValue. The field mostLikelyValue (#3797) shall not be
	 * used in the ecoinvent quality network.
	 * 
	 * @Element triangular
	 */
	@SubContext(contextClass = ES2TriangularDistribution.class)
	public ES2TriangularDistribution triangularDistribution;

	/**
	 * This "distribution" can be used to hold legacy data of the EcoSpold01
	 * format which reused the minValue, maxValue and standardDeviation95 fields
	 * to store undefined distribution data.
	 * 
	 * @Element undefined
	 */
	@SubContext(contextClass = ES2UndefinedDistribution.class)
	public ES2UndefinedDistribution undefinedDistribution;

	/**
	 * Uniform distribution of values between the minValue and the maxValue
	 * parameter. If the maxValue parameter is smaller than the minValue
	 * parameter their values will be swapped.
	 * 
	 * @Element uniform
	 * 
	 */
	@SubContext(contextClass = ES2UniformDistribution.class)
	public ES2UniformDistribution uniformDistribution;

	/**
	 * The simplified approach includes a qualitative assessment of data quality
	 * indicators based on a pedigree matrix. The pedigree matrix takes pattern
	 * from work published by (Pedersen Weidema and Wesnaes 1996). The
	 * pedigreeMatrix element groups the 5 data quality indicators and contains
	 * no data itself.
	 * 
	 * @Element pedigreeMatrix
	 */
	@SubContext(contextClass = ES2PedigreeMatrix.class)
	public ES2PedigreeMatrix pedigreeMatrix;

	/**
	 * A general comment can be made about each uncertainty information
	 * 
	 * @Element generalComment
	 * @DataType TBaseString32000
	 */
	@ContextField(name = "generalComment", parentName = "uncertainty", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> generalComment = new ArrayList<LangString>();

	/**
	 * A general comment can be made about each uncertainty information
	 * 
	 * @Element generalComment
	 * @DataType TBaseString32000
	 */
	public List<LangString> getGeneralComment() {
		return generalComment;
	}

}
