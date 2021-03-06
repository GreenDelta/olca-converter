package com.greendeltatc.simapro.csv.model;

import com.greendeltatc.simapro.csv.model.types.DistributionParameterType;
import com.greendeltatc.simapro.csv.model.types.DistributionType;

/**
 * Simple implementation of {@link com.greendeltatc.simapro.csv.model.IDistribution} for logarithmic normal
 * distribution
 * 
 * @author Sebastian Greve
 * 
 */
public class SPLogNormalDistribution implements IDistribution {

	/**
	 * The squared standard deviation
	 */
	private double squaredStandardDeviation = 0;

	/**
	 * The pedigree matrix of the flow
	 */
	private SPPedigreeMatrix pedigreeMatrix;

	/**
	 * Creates a new logarithmic normal distribution
	 * 
	 * @param squaredStandardDeviation
	 *            The squared standard deviation
	 */
	public SPLogNormalDistribution(double squaredStandardDeviation,
			SPPedigreeMatrix pedigreeMatrix) {
		this.squaredStandardDeviation = squaredStandardDeviation;
		this.pedigreeMatrix = pedigreeMatrix;
	}

	@Override
	public double getDistributionParameter(DistributionParameterType type) {
		double parameter = 0;
		if (type == DistributionParameterType.SQUARED_STANDARD_DEVIATION) {
			parameter = squaredStandardDeviation;
		}
		return parameter;
	}

	/**
	 * 
	 * @return {@link com.greendeltatc.simapro.csv.model.SPPedigreeMatrix}
	 */
	public SPPedigreeMatrix getPedigreeMatrix() {
		return pedigreeMatrix;
	}

	/**
	 * Set the pedigree matrix
	 * 
	 * @param pedigreeMatrix
	 */
	public void setPedigreeMatrix(SPPedigreeMatrix pedigreeMatrix) {
		this.pedigreeMatrix = pedigreeMatrix;
	}

	@Override
	public DistributionType getType() {
		return DistributionType.LOG_NORMAL;
	}

	/**
	 * Setter of the squared standard deviation
	 * 
	 * @param squaredStandardDeviation
	 *            The new squared standard deviation
	 */
	public void setSquaredStandardDeviation(double squaredStandardDeviation) {
		this.squaredStandardDeviation = squaredStandardDeviation;
	}

}
