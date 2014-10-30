package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The simplified approach includes a qualitative assessment of data quality
 * indicators based on a pedigree matrix. The pedigree matrix takes pattern from
 * work published by (Pedersen Weidema and Wesnaes 1996). The pedigreeMatrix
 * element groups the 5 data quality indicators and contains no data itself.
 * 
 * @Element pedigreeMatrix
 */
@Context(name = "pedigreeMatrix", parentName = "uncertainty")
public class ES2PedigreeMatrix extends ContextObject {

	/**
	 * 1=Representative data from all sites relevant for the market considered
	 * over an adequate period to even out normal fluctuations 2=Representative
	 * data from >50% of the sites relevant for the market considered over an
	 * adequate period to even out normal fluctuations 3=Representative data
	 * from only some sites (50% of sites but from shorter periods
	 * 4=Representative data from only one site relevant for the market
	 * considered OR some sites but from shorter periods 5=Representativeness
	 * unknown or data from a small number of sites AND from shorter periods
	 * 
	 * @Attribute completeness
	 * @DataType string
	 */
	@ContextField(name = "pedigreeMatrix", parentName = "uncertainty", attributeName = "completeness", isAttribute = true, type = Type.Integer)
	public Integer completeness;

	/**
	 * 1=Data from enterprises, processes and materials under study 2=Data from
	 * processes and materials under study (i.e. identical technology) but from
	 * different enterprises 3=Data from processes and materials under study but
	 * from different technology 4=Data on related processes or materials 5=Data
	 * on related processes on laboratory scale or from different technology
	 * 
	 * @Attribute furtherTechnologyCorrelation
	 * @DataType string
	 */
	@ContextField(name = "pedigreeMatrix", parentName = "uncertainty", attributeName = "furtherTechnologyCorrelation", isAttribute = true, type = Type.Integer)
	public Integer furtherTechnologyCorrelation;

	/**
	 * 1=Data from area under study 2=Average data from larger area in which the
	 * area under study is included 3=Data from area with similar production
	 * conditions 4=Data from are with slightly similar production conditions
	 * 5=Data from unknown OR distinctly different area (north america instead
	 * of middle east, OECD- Europe instead of Russia)
	 * 
	 * @Attribute geographicalCorrelation
	 * @DataType string
	 */
	@ContextField(name = "pedigreeMatrix", parentName = "uncertainty", attributeName = "geographicalCorrelation", isAttribute = true, type = Type.Integer)
	public Integer geographicalCorrelation;

	/**
	 * 1=Verified data based on measurements 2=Verified data partly based on
	 * assumptions OR nonverified data based on measurements 3=Non-verified data
	 * partly based on qualified estimates 4=Qualified estimate (e.g. by
	 * industrial expert) 5=Non-qualified estimate (default)
	 * 
	 * @Attribute reliability
	 * @DataType string
	 */
	@ContextField(name = "pedigreeMatrix", parentName = "uncertainty", attributeName = "reliability", isAttribute = true, type = Type.Integer)
	public Integer reliability;

	/**
	 * 1=Less than 3 years of difference to the time period of the dataset
	 * (fields 600-610) 2=Less than 6 years of difference to the time period of
	 * the dataset (fields 600-610) 3=Less than 10 years of difference to the
	 * time period of the dataset (fields 600-610) 4=Less than 15 years of
	 * difference to the time period of the dataset (fields 600-610) 5=Age of
	 * data unknown or more than 15 years of difference to the time period of
	 * the dataset (fields 600-610)
	 * 
	 * @Attribute temporalCorrelation
	 * @DataType string
	 */
	@ContextField(name = "pedigreeMatrix", parentName = "uncertainty", attributeName = "temporalCorrelation", isAttribute = true, type = Type.Integer)
	public Integer temporalCorrelation;
}
