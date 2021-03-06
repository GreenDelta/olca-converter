package com.greendeltatc.simapro.csv.model;

import com.greendeltatc.simapro.csv.model.types.DistributionParameterType;
import com.greendeltatc.simapro.csv.model.types.DistributionType;


/**
 * Simple implementation of {@link com.greendeltatc.simapro.csv.model.IDistribution} for triangle distribution
 * 
 * @author Sebastian Greve
 * 
 */
public class SPTriangleDistribution implements IDistribution {

	/**
	 * The minimum
	 */
	private double minimum = 0;

	/**
	 * The maximum
	 */
	private double maximum = 0;

	/**
	 * Creates a new triangle distribution
	 * 
	 * @param minimum
	 *            The minimum
	 * @param maximum
	 *            The maximum
	 */
	public SPTriangleDistribution(double minimum, double maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
	}

	@Override
	public double getDistributionParameter(DistributionParameterType type) {
		double parameter = 0;
		if (type == DistributionParameterType.MINIMUM) {
			parameter = minimum;
		} else if (type == DistributionParameterType.MAXIMUM) {
			parameter = maximum;
		}
		return parameter;
	}

	@Override
	public DistributionType getType() {
		return DistributionType.TRIANGLE;
	}

	/**
	 * Setter of the maximum
	 * 
	 * @param maximum
	 *            The new maximum
	 */
	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}

	/**
	 * Setter of the minimum
	 * 
	 * @param minimum
	 *            The new minimum
	 */
	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}

}
