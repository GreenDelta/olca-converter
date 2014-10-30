package com.greendeltatc.simapro.csv.model;

import com.greendeltatc.simapro.csv.model.types.DistributionParameterType;
import com.greendeltatc.simapro.csv.model.types.DistributionType;


/**
 * Simple implementation of {@link com.greendeltatc.simapro.csv.model.IDistribution} for normal distribution
 * 
 * @author Sebastian Greve
 * 
 */
public class SPNormalDistribution implements IDistribution {

	/**
	 * The doubled standard deviation
	 */
	private double doubledStandardDeviation = 0;

	/**
	 * Creates a new normal distribution
	 * 
	 * @param doubledStandardDeviation
	 *            The double standard deviation
	 */
	public SPNormalDistribution(double doubledStandardDeviation) {
		this.doubledStandardDeviation = doubledStandardDeviation;
	}

	@Override
	public double getDistributionParameter(DistributionParameterType type) {
		double parameter = 0;
		if (type == DistributionParameterType.DOUBLED_STANDARD_DEVIATION) {
			parameter = doubledStandardDeviation;
		}
		return parameter;
	}

	@Override
	public DistributionType getType() {
		return DistributionType.NORMAL;
	}

	/**
	 * Setter of the doubled standard deviation
	 * 
	 * @param doubledStandardDeviation
	 *            The new doubled standard deviation
	 */
	public void setDoubledStandardDeviation(double doubledStandardDeviation) {
		this.doubledStandardDeviation = doubledStandardDeviation;
	}

}
