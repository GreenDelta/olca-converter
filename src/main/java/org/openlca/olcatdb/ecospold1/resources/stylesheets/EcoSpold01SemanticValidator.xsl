<?xml version="1.0" encoding="UTF-8"?>
<!--
The contents of this file are subject to the EcoSpold Public License Version 1.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.ecoinvent.ch.
Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the specific language governing rights and limitations under the License.
The Original Code consists of the EcoSpold data format and EcoSpold Access.
The Original Code was created by the ecoinvent Centre, Switzerland (Swiss Centre for Life Cycle Inventories) and ifu Hamburg GmbH, Germany. Portions created by the ecoinvent Centre and ifu Hamburg GmbH are Copyright (C) ecoinvent Centre. All Rights Reserved.
-->
<!--
	errorNumbers:
	fatal errors:
		-1	dataGeneratorAndPublication: @dataPublishedIn=2 and not(@referenceToPublishedSource)
		-2	dataGeneratorAndPublication: @accessRestrictedTo=3 and not(@companyCode)
		-3 	dataGeneratorAndPublication: @accessRestrictedTo=3 and not(@countryCode)
		-4	exchange: (es:inputGroup = 4 or es:outputGroup = 4) and (not(@category) or not(@subCategory))
		-5	exchange: es:inputGroup = 5 and not(@infrastructureProcess)
		-6	exchange: (es:inputGroup != 4 or es:outputGroup != 4) and not(@location)
		-7	exchange: @uncertaintyType = 3 and (@standardDeviation95 or not(@minValue) or not(@maxValue) or (not(@mostLikelyValue) and not(@meanValue)) or (@mostLikelyValue and @meanValue))
		-8	invalid company code
		-9  exchange: (@uncertaintyType = 1 or @uncertaintyType = 2) and (not(@standardDeviation95) or (@minValue or @maxValue or @mostLikelyValue))
		-10	the file referenced by dataset/validCompanyCodes could not be found or is no valid CompanyCodes file.
		-11	the file referenced by dataset/validRegionalCodes could not be found or is no valid RegionalCodes file.
		-12	the file referenced by dataset/validCategories could not be found or is no valid Categories file.
		-13	the file referenced by dataset/validUnits could not be found or is no valid Units file.
		-14 dataSetInformation: @type is outside of valid range for the current namespace.
		-15 exchange: "(not(es:inputGroup) and not(es:outputGroup)) and ((../../es:metaInformation/es:processInformation/es:dataSetInformation/@type != 4) and (../../es:metaInformation/es:processInformation/es:dataSetInformation/@impactAssessmentResult = 'false'))
		-16 referenceFunction: every single output process must have an exchange for each referenceFunction
		-17 exchange: @uncertaintyType = 4 and (@standardDeviation95 or not(@minValue) or not(@maxValue) or @mostLikelyValue)
		-18 exchange: es:outputGroup and (es:outputGroup != 4) and @uncertaintyType: Non-ToNature output flows are not allowed to contain any uncertainty information.
		-19 es:outputGroup and (es:outputGroup = 0)  and @standardDeviation95: ReferenceProduct output flows are not allowed to contain standardDeviation95 information.
		
	warnings:
		1	dataset/@validCompanyCodes not set. Validation of company codes not possible.
		2	dataset/@validRegionalCode not set. Validation of regional codes not possible.
		3	dataset/@validCategories not set. Validation of Categories and SubCategories not possible.
		4	dataset/@validUnits not set. Validation of units not possible.
		5   technology/@text is required in the EcoInvent context.
		6	@accessRestrictedTo=0 and ((@companyCode != '') or (@countryCode != ''))
		7   dataSetInformation/@type = 5 and less than 1 allocation
		
	hints:
		1024	unknown Category and/or SubCategory
		1025	unknown local Category and/or local SubCategory
		1026	unknown regional code
		1027   unknown unit
		1028   Area allocation requires the presence of at least two exchanges with outputGroup = 2
		1029  dataSetInformation/@type = 5 and less than 2 exchanges with outputGroup = 2
		1030  dataSetInformation/@type != 5 and more than 0 exchanges with outputGroup = 2
-->
<xsl:stylesheet 
	version="1.0" 
	exclude-result-prefixes="es esi ese"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:es="http://www.EcoInvent.org/EcoSpold01"
	xmlns:esi="http://www.EcoInvent.org/EcoSpold01Impact"
	xmlns:ese="http://www.EcoInvent.org/EcoSpold01Elementary">

	<xsl:import href="EcoSpold01SV.i18n.xsl"/>
	<xsl:import href="EcoSpold01SV.xsl"/>
	<xsl:import href="EcoSpold01ImpactSV.xsl"/>
	<xsl:import href="EcoSpold01ElementarySV.xsl"/>

	<xsl:output 
		method="xml" 
		version="1.0" 
		encoding="UTF-8" 
		indent="yes"/>
	
	<xsl:template match="/*">
		<xsl:element name="semanticValidation">
			<xsl:call-template name="es:validateDatasets"/>
			<xsl:call-template name="esi:validateDatasets"/>
			<xsl:call-template name="ese:validateDatasets"/>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
