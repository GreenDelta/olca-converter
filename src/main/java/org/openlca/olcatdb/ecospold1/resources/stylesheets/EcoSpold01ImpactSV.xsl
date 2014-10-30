<?xml version="1.0" encoding="UTF-8"?>
<!--
The contents of this file are subject to the EcoSpold Public License Version 1.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.ecoinvent.ch.
Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the specific language governing rights and limitations under the License.
The Original Code consists of the EcoSpold data format and EcoSpold Access.
The Original Code was created by the ecoinvent Centre, Switzerland (Swiss Centre for Life Cycle Inventories) and ifu Hamburg GmbH, Germany. Portions created by the ecoinvent Centre and ifu Hamburg GmbH are Copyright (C) ecoinvent Centre. All Rights Reserved.
-->
<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
	xmlns:es="http://www.EcoInvent.org/EcoSpold01Impact"
	xmlns:ecc="http://www.EcoInvent.org/CompanyCodes"
	xmlns:eec="http://www.EcoInvent.org/Categories"
	xmlns:erc="http://www.EcoInvent.org/RegionalCodes"
	xmlns:eu="http://www.EcoInvent.org/Units">

	<xsl:output 
		method="xml" 
		version="1.0" 
		encoding="UTF-8" 
		indent="yes"/>
	
	<xsl:template name="es:validateDatasets">
		<xsl:for-each select="es:dataset">
			<xsl:element name="dataset">
				<xsl:attribute name="number"><xsl:value-of select="@number"/></xsl:attribute>
				<xsl:apply-templates select="es:metaInformation"/>
				<xsl:apply-templates select="es:flowData"/>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="es:metaInformation">
		<xsl:apply-templates select="es:administrativeInformation"/>
		<xsl:apply-templates select="es:processInformation"/>
	</xsl:template>

	<xsl:template match="es:administrativeInformation">
		<xsl:apply-templates select="es:dataEntryBy"/>
		<xsl:apply-templates select="es:dataGeneratorAndPublication"/>
		<xsl:apply-templates select="es:person"/>
	</xsl:template>

	<xsl:template match="es:processInformation">
		<xsl:apply-templates select="es:referenceFunction"/>
		<xsl:apply-templates select="es:geography"/>
		<xsl:apply-templates select="es:dataSetInformation"/>
	</xsl:template>
	
	<xsl:template match="es:dataEntryBy"/>
	
	<xsl:template match="es:dataGeneratorAndPublication">
		<xsl:if test="@companyCode">
			<xsl:call-template name="es:checkCompanyCode"/>
		</xsl:if>
		
		<xsl:if test="@dataPublishedIn=2 and not(@referenceToPublishedSource)">
			<xsl:call-template name="es:emitError">
				<xsl:with-param name="errorNumber" select="-1"/>
				<xsl:with-param name="attributeName">referenceToPublishedSource</xsl:with-param>
				<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error1</xsl:with-param></xsl:call-template></xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="@accessRestrictedTo=3 and not(@companyCode)">
			<xsl:call-template name="es:emitError">
				<xsl:with-param name="errorNumber" select="-2"/>
				<xsl:with-param name="attributeName">companyCode</xsl:with-param>
				<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error2</xsl:with-param></xsl:call-template></xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if test="@accessRestrictedTo=3 and not(@countryCode)">
			<xsl:call-template name="es:emitError">
				<xsl:with-param name="errorNumber" select="-3"/>
				<xsl:with-param name="attributeName">countryCode</xsl:with-param>
				<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error3</xsl:with-param></xsl:call-template></xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if test="@accessRestrictedTo=0 and ((@companyCode != '') or (@countryCode != ''))">
			<xsl:call-template name="es:emitWarning">
				<xsl:with-param name="warningNumber" select="6"/>
				<xsl:with-param name="attributeName">companyCode/countryCode</xsl:with-param>
				<xsl:with-param name="attributeValue"><xsl:value-of select="@companyCode"/>/<xsl:value-of select="@countryCode"/></xsl:with-param>
				<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">warning6</xsl:with-param></xsl:call-template></xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template match="es:person">
		<xsl:call-template name="es:checkCompanyCode"/>
	</xsl:template>

	<xsl:template match="es:geography">
		<xsl:call-template name="es:checkLocation"/>
	</xsl:template>

	<xsl:template match="es:referenceFunction">
		<xsl:call-template name="es:checkCategory"/>
		<xsl:call-template name="es:checkLocalCategory"/>
		<xsl:call-template name="es:checkUnit"/>
	</xsl:template>	

	<xsl:template match="es:dataSetInformation">
		<xsl:if test="@type != 4">
			<xsl:call-template name="es:emitError">
				<xsl:with-param name="errorNumber" select="-14"/>
				<xsl:with-param name="attributeName">type</xsl:with-param>
				<xsl:with-param name="attributeValue"><xsl:value-of select="@type"/></xsl:with-param>
				<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error14</xsl:with-param></xsl:call-template>Valid value: 4.</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="es:allocation"/>

	<xsl:template match="es:exchange">
<!--
		<xsl:if test="@location">
			<xsl:call-template name="es:checkLocation"/>
		</xsl:if>

		<xsl:if test="@category and @subCategory">
			<xsl:call-template name="es:checkCategory"/>
		</xsl:if>

		<xsl:if test="@localCategory and @localSubCategory">
			<xsl:call-template name="es:checkLocalCategory"/>
		</xsl:if>
		
		<xsl:call-template name="es:checkUnit"/>
		
		<xsl:if test="(es:inputGroup = 4 or es:outputGroup = 4) and ((@category = '') or (@subCategory = '') or not(@category) or not(@subCategory))">
			<xsl:call-template name="es:emitError">
				<xsl:with-param name="errorNumber" select="-4"/>
				<xsl:with-param name="attributeName">category/subCategory</xsl:with-param>
				<xsl:with-param name="attributeValue"><xsl:value-of select="@category"/>/<xsl:value-of select="@subCategory"/></xsl:with-param>
				<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error4</xsl:with-param></xsl:call-template></xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="es:inputGroup = 5 and not(@infrastructureProcess)">
			<xsl:call-template name="es:emitError">
				<xsl:with-param name="errorNumber" select="-5"/>
				<xsl:with-param name="attributeName">infrastructureProcess</xsl:with-param>
				<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error5</xsl:with-param></xsl:call-template></xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="(es:inputGroup != 4 or es:outputGroup != 4) and not(@location)">
			<xsl:call-template name="es:emitError">
				<xsl:with-param name="errorNumber" select="-6"/>
				<xsl:with-param name="attributeName">location</xsl:with-param>
				<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error6</xsl:with-param></xsl:call-template></xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if test="@uncertaintyType = 3 and (not(@minValue) or not(@maxValue) or (not(@mostLikelyValue) and not(@meanValue)) or (@mostLikelyValue and @meanValue))">
			<xsl:call-template name="es:emitError">
				<xsl:with-param name="errorNumber" select="-7"/>
				<xsl:with-param name="attributeName">minValue/maxValue/(mostLikelyValue/meanValue)</xsl:with-param>
				<xsl:with-param name="attributeValue"><xsl:value-of select="@minValue"/>/<xsl:value-of select="@maxValue"/>/(<xsl:value-of select="@mostLikelyValue"/>/<xsl:value-of select="@meanValue"/>)</xsl:with-param>
				<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error7</xsl:with-param></xsl:call-template></xsl:with-param>
			</xsl:call-template>
		</xsl:if>
-->
	</xsl:template>	
	
	<xsl:template name="es:checkCompanyCode">
		<xsl:variable name="countryCode">	
			<xsl:value-of select="@countryCode"/>
		</xsl:variable>
		<xsl:variable name="companyCode">	
			<xsl:value-of select="@companyCode"/>
		</xsl:variable>
		<xsl:variable name="validCompanyCodes">	
			<xsl:value-of select="ancestor::es:dataset/@validCompanyCodes"/>
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="$validCompanyCodes != ''">
				<xsl:choose>
					<xsl:when test="not(document($validCompanyCodes)/ecc:companyCodes)">
						<xsl:call-template name="es:emitError">
							<xsl:with-param name="errorNumber" select="-10"/>
							<xsl:with-param name="attributeName">validCompanyCodes</xsl:with-param>
							<xsl:with-param name="attributeValue"><xsl:value-of select="$validCompanyCodes"/></xsl:with-param>
							<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error10</xsl:with-param></xsl:call-template></xsl:with-param>
						</xsl:call-template>
					</xsl:when>
			
					<xsl:otherwise>
						<xsl:if test="not(document($validCompanyCodes)/ecc:companyCodes/ecc:countryCode[@code = $countryCode][ecc:companyCode = $companyCode])">
							<xsl:call-template name="es:emitError">
								<xsl:with-param name="errorNumber" select="-8"/>
								<xsl:with-param name="attributeName">companyCode</xsl:with-param>
								<xsl:with-param name="attributeValue"><xsl:value-of select="$companyCode"/></xsl:with-param>
								<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error8</xsl:with-param></xsl:call-template> (<xsl:value-of select="$countryCode"/>)</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:call-template name="es:emitWarning">
					<xsl:with-param name="warningNumber" select="1"/>
					<xsl:with-param name="attributeName">companyCode</xsl:with-param>
					<xsl:with-param name="attributeValue"><xsl:value-of select="$companyCode"/></xsl:with-param>
					<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">warning1</xsl:with-param></xsl:call-template></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

	<xsl:template name="es:checkCategory">
		<xsl:variable name="category">
			<xsl:value-of select="@category"/>
		</xsl:variable>
		<xsl:variable name="subCategory">
			<xsl:value-of select="@subCategory"/>
		</xsl:variable>
		<xsl:variable name="validCategories">
			<xsl:value-of select="ancestor::es:dataset/@validCategories"/>
		</xsl:variable>

		<xsl:choose>
			<xsl:when test="$validCategories != ''">
				<xsl:choose>
					<xsl:when test="not(document($validCategories)/eec:categories)">
						<xsl:call-template name="es:emitError">
							<xsl:with-param name="errorNumber" select="-12"/>
							<xsl:with-param name="attributeName">validCategories</xsl:with-param>
							<xsl:with-param name="attributeValue"><xsl:value-of select="$validCategories"/></xsl:with-param>
							<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error12</xsl:with-param></xsl:call-template></xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					
					<xsl:otherwise>
						<xsl:if test="not(document($validCategories)/eec:categories/eec:category[@name = $category]/eec:subCategory[@name = $subCategory])">
							<xsl:call-template name="es:emitHint">
								<xsl:with-param name="hintNumber" select="1024"/>
								<xsl:with-param name="attributeName">category/subCategory</xsl:with-param>
								<xsl:with-param name="attributeValue"><xsl:value-of select="$category"/>/<xsl:value-of select="$subCategory"/></xsl:with-param>
								<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">hint1024</xsl:with-param></xsl:call-template></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:call-template name="es:emitWarning">
					<xsl:with-param name="warningNumber" select="3"/>
					<xsl:with-param name="attributeName">category/subCategory</xsl:with-param>
					<xsl:with-param name="attributeValue"><xsl:value-of select="$category"/>/<xsl:value-of select="$subCategory"/></xsl:with-param>
					<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">warning3</xsl:with-param></xsl:call-template></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="es:checkLocalCategory">
		<xsl:variable name="localCategory">
			<xsl:value-of select="@localCategory"/>
		</xsl:variable>
		<xsl:variable name="localSubCategory">
			<xsl:value-of select="@localSubCategory"/>
		</xsl:variable>
		<xsl:variable name="validCategories">
			<xsl:value-of select="ancestor::es:dataset/@validCategories"/>
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="$validCategories != ''">
				<xsl:choose>
					<xsl:when test="not(document($validCategories)/eec:categories)">
						<xsl:call-template name="es:emitError">
							<xsl:with-param name="errorNumber" select="-12"/>
							<xsl:with-param name="attributeName">validCategories</xsl:with-param>
							<xsl:with-param name="attributeValue"><xsl:value-of select="$validCategories"/></xsl:with-param>
							<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error12</xsl:with-param></xsl:call-template></xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					
					<xsl:otherwise>
						<xsl:if test="not(document($validCategories)/eec:categories/eec:category[@localName = $localCategory]/eec:subCategory[@localName = $localSubCategory])">
							<xsl:call-template name="es:emitHint">
								<xsl:with-param name="hintNumber" select="1025"/>
								<xsl:with-param name="attributeName">localCategory/localSubCategory</xsl:with-param>
								<xsl:with-param name="attributeValue"><xsl:value-of select="$localCategory"/>/<xsl:value-of select="$localSubCategory"/></xsl:with-param>
								<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">hint1025</xsl:with-param></xsl:call-template></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:call-template name="es:emitWarning">
					<xsl:with-param name="warningNumber" select="3"/>
					<xsl:with-param name="attributeName">category/subCategory</xsl:with-param>
					<xsl:with-param name="attributeValue"><xsl:value-of select="$localCategory"/>/<xsl:value-of select="$localSubCategory"/></xsl:with-param>
					<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">warning3</xsl:with-param></xsl:call-template></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="es:checkLocation">
		<xsl:variable name="location">	
			<xsl:value-of select="@location"/>
		</xsl:variable>
		<xsl:variable name="validRegionalCodes">	
			<xsl:value-of select="ancestor::es:dataset/@validRegionalCodes"/>
		</xsl:variable>
		<xsl:variable name="xsdDataTypes">EcoSpold01DataTypes.xsd</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="$validRegionalCodes != ''">
				<xsl:choose>
					<xsl:when test="not(document($validRegionalCodes)/erc:regionalCodes)">
						<xsl:call-template name="es:emitError">
							<xsl:with-param name="errorNumber" select="-11"/>
							<xsl:with-param name="attributeName">validRegionalCodes</xsl:with-param>
							<xsl:with-param name="attributeValue"><xsl:value-of select="$validRegionalCodes"/></xsl:with-param>
							<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error11</xsl:with-param></xsl:call-template></xsl:with-param>
						</xsl:call-template>
					</xsl:when>
			
					<xsl:otherwise>
						<xsl:if test="not(document($xsdDataTypes)/descendant::*[@name = 'TISOCountryCode']/xsd:restriction/xsd:enumeration[@value = $location])">
							<xsl:if test="not(document($validRegionalCodes)/erc:regionalCodes[erc:regionalCode = $location])">
								<xsl:call-template name="es:emitHint">
									<xsl:with-param name="hintNumber" select="1026"/>
									<xsl:with-param name="attributeName">location</xsl:with-param>
									<xsl:with-param name="attributeValue"><xsl:value-of select="$location"/></xsl:with-param>
									<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">hint1026</xsl:with-param></xsl:call-template></xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>

			<xsl:otherwise>
				<xsl:call-template name="es:emitWarning">
					<xsl:with-param name="warningNumber" select="2"/>
					<xsl:with-param name="attributeName">location</xsl:with-param>
					<xsl:with-param name="attributeValue"><xsl:value-of select="$location"/></xsl:with-param>
					<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">warning2</xsl:with-param></xsl:call-template></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="es:checkUnit">
		<xsl:variable name="unit">
			<xsl:value-of select="@unit"/>
		</xsl:variable>
		<xsl:variable name="validUnits">
			<xsl:value-of select="ancestor::es:dataset/@validUnits"/>
		</xsl:variable>

		<xsl:choose>
			<xsl:when test="$validUnits != ''">
				<xsl:choose>
					<xsl:when test="not(document($validUnits)/eu:units)">
						<xsl:call-template name="es:emitError">
							<xsl:with-param name="errorNumber" select="-13"/>
							<xsl:with-param name="attributeName">validUnits</xsl:with-param>
							<xsl:with-param name="attributeValue"><xsl:value-of select="$validUnits"/></xsl:with-param>
							<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">error13</xsl:with-param></xsl:call-template></xsl:with-param>
						</xsl:call-template>
					</xsl:when>
			
					<xsl:otherwise>
						<xsl:if test="not(document($validUnits)/eu:units[eu:unit = $unit])">
								<xsl:call-template name="es:emitHint">
									<xsl:with-param name="hintNumber" select="1027"/>
									<xsl:with-param name="attributeName">unit</xsl:with-param>
									<xsl:with-param name="attributeValue"><xsl:value-of select="$unit"/></xsl:with-param>
									<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">hint1027</xsl:with-param></xsl:call-template></xsl:with-param>
								</xsl:call-template>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:call-template name="es:emitWarning">
					<xsl:with-param name="warningNumber" select="4"/>
					<xsl:with-param name="attributeName">unit</xsl:with-param>
					<xsl:with-param name="attributeValue"><xsl:value-of select="$unit"/></xsl:with-param>
					<xsl:with-param name="text"><xsl:call-template name="emitString"><xsl:with-param name="name">warning4</xsl:with-param></xsl:call-template></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="es:emitError">
		<xsl:param name="errorNumber"/>
		<xsl:param name="attributeName"/>
		<xsl:param name="attributeValue"/>
		<xsl:param name="text"/>
		
		<xsl:element name="error">
			<xsl:attribute name="number"><xsl:value-of select="$errorNumber"/></xsl:attribute>
			<xsl:attribute name="elementName"><xsl:value-of select="name(.)"/></xsl:attribute>

			<xsl:if test="@name">
				<xsl:attribute name="elementNumber"><xsl:value-of select="@number"/> (<xsl:value-of select="@name"/>)</xsl:attribute>
			</xsl:if>
			<xsl:if test="not(@name)">
				<xsl:attribute name="elementNumber"><xsl:value-of select="@number"/></xsl:attribute>
			</xsl:if>
			
			<xsl:attribute name="attributeName"><xsl:value-of select="$attributeName"/></xsl:attribute>
			<xsl:attribute name="attributeValue"><xsl:value-of select="$attributeValue"/></xsl:attribute>
			<xsl:value-of select="$text"/>
		</xsl:element>
	</xsl:template>

	<xsl:template name="es:emitWarning">
		<xsl:param name="warningNumber"/>
		<xsl:param name="attributeName"/>
		<xsl:param name="attributeValue"/>
		<xsl:param name="text"/>
		
		<xsl:element name="warning">
			<xsl:attribute name="number"><xsl:value-of select="$warningNumber"/></xsl:attribute>
			<xsl:attribute name="elementName"><xsl:value-of select="name(.)"/></xsl:attribute>
			<xsl:if test="@name">
				<xsl:attribute name="elementNumber"><xsl:value-of select="@number"/> (<xsl:value-of select="@name"/>)</xsl:attribute>
			</xsl:if>
			<xsl:if test="not(@name)">
				<xsl:attribute name="elementNumber"><xsl:value-of select="@number"/></xsl:attribute>
			</xsl:if>
			<xsl:attribute name="attributeName"><xsl:value-of select="$attributeName"/></xsl:attribute>
			<xsl:attribute name="attributeValue"><xsl:value-of select="$attributeValue"/></xsl:attribute>
			<xsl:value-of select="$text"/>
		</xsl:element>
	</xsl:template>

	<xsl:template name="es:emitHint">
		<xsl:param name="hintNumber"/>
		<xsl:param name="attributeName"/>
		<xsl:param name="attributeValue"/>
		<xsl:param name="text"/>
		
		<xsl:element name="hint">
			<xsl:attribute name="number"><xsl:value-of select="$hintNumber"/></xsl:attribute>
			<xsl:attribute name="elementName"><xsl:value-of select="name(.)"/></xsl:attribute>
			<xsl:if test="@name">
				<xsl:attribute name="elementNumber"><xsl:value-of select="@number"/> (<xsl:value-of select="@name"/>)</xsl:attribute>
			</xsl:if>
			<xsl:if test="not(@name)">
				<xsl:attribute name="elementNumber"><xsl:value-of select="@number"/></xsl:attribute>
			</xsl:if>
			<xsl:attribute name="attributeName"><xsl:value-of select="$attributeName"/></xsl:attribute>
			<xsl:attribute name="attributeValue"><xsl:value-of select="$attributeValue"/></xsl:attribute>
			<xsl:value-of select="$text"/>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
