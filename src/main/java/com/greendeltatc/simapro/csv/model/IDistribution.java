package com.greendeltatc.simapro.csv.model;

import com.greendeltatc.simapro.csv.model.types.DistributionParameterType;
import com.greendeltatc.simapro.csv.model.types.DistributionType;

/**
 * This class represents a SimaPro uncertainty distribution
 * 
 * @author Sebastian Greve
 * 
 */
public interface IDistribution {

	/**
	 * Getter of the distribution parameter for the given type
	 * 
	 * @param type
	 *            The type of distribution parameter which is requested
	 * @return The value of the distribution parameter with the given type, if
	 *         defined in the distribution, null otherwise
	 */
	double getDistributionParameter(
			DistributionParameterType type);

	/**
	 * Getter of the distribution type
	 * 
	 * @see com.greendeltatc.simapro.csv.model.types.DistributionType
	 * @return The type of the distribution
	 */
	DistributionType getType();

}
