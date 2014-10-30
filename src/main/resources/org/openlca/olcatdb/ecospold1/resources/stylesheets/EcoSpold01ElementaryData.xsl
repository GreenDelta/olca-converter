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
	xmlns:es="http://www.EcoInvent.org/EcoSpold01Elementary">
	
	<xsl:output 
		method="html" 
		version="1.0" 
		encoding="UTF-8" 
		omit-xml-declaration="no" 
		indent="no" 
		media-type="text/html"/>

	<xsl:template match="es:dataset" mode="TOC">
		<tr>
			<td class="ttl1" width="100%">
				<a><xsl:attribute name="href">#dataset<xsl:value-of select="@number"/>dataset</xsl:attribute><xsl:call-template name="es:emitCompleteDatasetDesignation"/>
				</a>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template match="es:dataset" mode="complete">
		<xsl:call-template name="es:emitAnchor"/>
		<br/>
		<div align="center">
			<a href="#toc"><xsl:call-template name="emitString"><xsl:with-param name="name">tableOfContent</xsl:with-param></xsl:call-template></a>
		</div>
		<br/>
		<xsl:apply-templates/>

		<xsl:call-template name="es:emitDatasetInformation"/>
	</xsl:template>
	
	<xsl:template name="es:emitDatasetInformation">
		<table width="100%">
			<tr><td><xsl:call-template name="emitString"><xsl:with-param name="name">indexnumber</xsl:with-param></xsl:call-template>: <xsl:value-of select="ancestor-or-self::es:dataset/@number"/></td></tr>
			<tr><td><xsl:call-template name="emitString"><xsl:with-param name="name">generator</xsl:with-param></xsl:call-template>: <xsl:value-of select="ancestor-or-self::es:dataset/@generator"/></td></tr>
			<tr><td><xsl:call-template name="emitString"><xsl:with-param name="name">timestamp</xsl:with-param></xsl:call-template>: <xsl:value-of select="ancestor-or-self::es:dataset/@timestamp"/></td></tr>
			<tr><td><xsl:call-template name="emitString"><xsl:with-param name="name">validCompanyCodes</xsl:with-param></xsl:call-template>: <xsl:value-of select="ancestor-or-self::es:dataset/@validCompanyCodes"/></td></tr>
			<tr><td><xsl:call-template name="emitString"><xsl:with-param name="name">validRegionalCodes</xsl:with-param></xsl:call-template>: <xsl:value-of select="ancestor-or-self::es:dataset/@validRegionalCodes"/></td></tr>
			<tr><td><xsl:call-template name="emitString"><xsl:with-param name="name">validCategories</xsl:with-param></xsl:call-template>: <xsl:value-of select="ancestor-or-self::es:dataset/@validCategories"/></td></tr>
			<tr><td><xsl:call-template name="emitString"><xsl:with-param name="name">validUnits</xsl:with-param></xsl:call-template>: <xsl:value-of select="ancestor-or-self::es:dataset/@validUnits"/></td></tr>
		</table>
	</xsl:template>
	
	<xsl:template match="es:metaInformation">
		<xsl:call-template name="es:emitNode">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">metaInformation</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="ttlClass">ttl1</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="es:processInformation">
		<xsl:call-template name="es:emitNode">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">processInformation</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="es:modellingAndValidation">
		<xsl:call-template name="es:emitNode">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">modellingAndValidation</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="es:administrativeInformation">
		<xsl:call-template name="es:emitNode">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">administrativeInformation</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="es:flowData"/>

	<xsl:template match="es:referenceFunction">
		<xsl:call-template name="es:emitLeafChilds">	
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">referenceFunction</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="elementTitle"><xsl:call-template name="emitString"><xsl:with-param name="name">synonyms</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="es:dataSetInformation">
		<xsl:call-template name="es:emitLeaf">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">datasetInformation</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>

		<table width="100%">
			<tr>
				<td class="ttl4" width="30%"><xsl:call-template name="emitString"><xsl:with-param name="name">number</xsl:with-param></xsl:call-template></td>
				<td class="ttl4" width="70%"><xsl:value-of select="ancestor::es:dataset/@number"/></td>
			</tr>
		</table>
	</xsl:template>

	<xsl:template match="es:validation">
		<xsl:call-template name="es:emitLeaf">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">validation</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="es:dataEntryBy">
		<xsl:call-template name="es:emitLeaf">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">dataEntryBy</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="es:person">
		<xsl:call-template name="es:emitLeaf">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">person</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="appendValue">true</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="es:emitLeaf">
		<xsl:param name="title"/>
		<xsl:param name="appendValue"/>

		<xsl:call-template name="es:emitAnchor">
			<xsl:with-param name="appendValue"><xsl:value-of select="$appendValue"/></xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="es:emitTitle">
			<xsl:with-param name="title">	
				<xsl:value-of select="$title"/>
			</xsl:with-param>
			<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="es:emitAllAttributes"/>
	</xsl:template>

	<xsl:template name="es:emitLeafChilds">
		<xsl:param name="title"/>
		<xsl:param name="element"/>
		<xsl:param name="elementTitle"/>

		<xsl:call-template name="es:emitAnchor"/>

		<xsl:call-template name="es:emitTitle">
			<xsl:with-param name="title">	
				<xsl:value-of select="$title"/>
			</xsl:with-param>
			<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="es:emitAllAttributes"/>
		
		<xsl:if test="*">
			<table width="100%">
				<xsl:for-each select="*">
					<tr>
						<xsl:if test="$elementTitle">
							<xsl:if test="position()=1">
								<td class="ttl4" width="30%"><xsl:value-of select="$elementTitle"/></td>
							</xsl:if>
							<xsl:if test="position()>1">
								<td class="ttl4" width="30%"/>
							</xsl:if>
						</xsl:if>
						<xsl:if test="not($elementTitle)">
							<td class="ttl4" width="30%"><xsl:call-template name="emitString"><xsl:with-param name="name"><xsl:value-of select="name(.)"/></xsl:with-param></xsl:call-template></td>
						</xsl:if>
						<td class="ttl4" width="70%"><xsl:value-of select="."/></td>
					</tr>
				</xsl:for-each>
			</table>
		</xsl:if>
	</xsl:template>

	<xsl:template name="es:emitLeafRepetitiveChilds">
		<xsl:param name="title"/>
		<xsl:param name="element"/>
		<xsl:param name="elementTitle"/>
		<xsl:param name="appendValue"/>

		<xsl:call-template name="es:emitAnchor">
			<xsl:with-param name="appendValue"><xsl:value-of select="$appendValue"/></xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="es:emitTitle">
			<xsl:with-param name="title">	
				<xsl:value-of select="$title"/>
			</xsl:with-param>
			<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="es:emitAllAttributes"/>
		
		<xsl:if test="*">
			<table width="100%">
				<tr>
					<td class="ttl4" width="30%"><xsl:value-of select="$elementTitle"/></td>
					<td class="ttl4" width="70%">
						<xsl:for-each select="*">
							<xsl:value-of select="."/>
							<xsl:if test="position()&lt;last()">, </xsl:if>
						</xsl:for-each>
					</td>
				</tr>
			</table>
		</xsl:if>
	</xsl:template>

	<xsl:template name="es:emitNode">
		<xsl:param name="title"/>
		<xsl:param name="ttlClass">ttl2</xsl:param>

		<xsl:call-template name="es:emitAnchor"/>
		
		<xsl:call-template name="es:emitTitle">
			<xsl:with-param name="title">	
				<xsl:value-of select="$title"/>
			</xsl:with-param>
			<xsl:with-param name="ttlClass">
				<xsl:value-of select="$ttlClass"/>
			</xsl:with-param>
		</xsl:call-template>
		
		<xsl:apply-templates/>
	</xsl:template>	

	<xsl:template name="es:emitAllAttributes">
		<table width="100%">
			<xsl:for-each select="@*[name(.) != 'xsi:type']">
				<tr>
					<td class="ttl4" width="30%">
						<xsl:call-template name="emitString"><xsl:with-param name="name"><xsl:value-of select="name(.)"/></xsl:with-param></xsl:call-template>
					</td>
					<td class="ttl4" width="70%">
						<xsl:choose>
							<xsl:when test=". = 'true'"><xsl:call-template name="emitString"><xsl:with-param name="name">true</xsl:with-param></xsl:call-template></xsl:when>
							<xsl:when test=". = 'false'"><xsl:call-template name="emitString"><xsl:with-param name="name">false</xsl:with-param></xsl:call-template></xsl:when>
							<xsl:when test="name(.) = 'person'">
								<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>person<xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></a>
							</xsl:when>
							<xsl:when test="name(.) = 'referenceToPublishedSource'">
								<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>source<xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></a>
							</xsl:when>
							<xsl:when test="name(.) = 'proofReadingValidator'">
								<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>person<xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></a>
							</xsl:when>
							<xsl:otherwise><xsl:value-of select="."/></xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>	

	<xsl:template name="es:emitTitle">
		<xsl:param name="title"/>
		<xsl:param name="ttlClass">ttl2</xsl:param>

		<table width="100%">
			<tr>
				<td width="100%" >
					<xsl:attribute name="class">
						<xsl:value-of select="$ttlClass"/>
					</xsl:attribute>
					<xsl:value-of select="$title"/>
				</td>
				<td style="text-align:right;" nowrap="nowrap">
					<xsl:attribute name="class">
						<xsl:value-of select="$ttlClass"/>
					</xsl:attribute>
					<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>dataset</xsl:attribute><xsl:call-template name="es:emitCompleteDatasetDesignation"><xsl:with-param name="ancestorRequired">true</xsl:with-param></xsl:call-template></a>
				</td>
			</tr>
		</table>
	</xsl:template>
		
	<xsl:template name="es:emitAnchor">
		<xsl:param name="appendValue"/>
		
		<xsl:if test="$appendValue != ''">
			<a><xsl:attribute name="name">dataset<xsl:value-of select="ancestor-or-self::es:dataset/@number"/><xsl:value-of select="name(.)"/><xsl:value-of select="@number"/></xsl:attribute></a>
		</xsl:if>

		<xsl:if test="$appendValue = ''">
			<a><xsl:attribute name="name">dataset<xsl:value-of select="ancestor-or-self::es:dataset/@number"/><xsl:value-of select="name(.)"/></xsl:attribute></a>
		</xsl:if>
	</xsl:template>

	<xsl:template name="es:emitCompleteDatasetDesignation">
		<xsl:param name="ancestorRequired">false</xsl:param>
		
		<xsl:choose>
			<xsl:when test="$lang = 'ja'">
				<xsl:if test="$ancestorRequired = 'true'">
					<xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@localName"/>, <xsl:value-of 		select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@localCategory"/>, <xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/		@localSubCategory"/>, [<xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]
				</xsl:if>
		
				<xsl:if test="$ancestorRequired = 'false'">
					<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@localName"/>, <xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@localCategory"/>, <xsl:value-of 	select="es:metaInformation/es:processInformation/es:referenceFunction/@localSubCategory"/>, [<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]
				</xsl:if>	
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="$ancestorRequired = 'true'">
					<xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@name"/>, <xsl:value-of 		select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@category"/>, <xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/		@subCategory"/>, [<xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]
				</xsl:if>
		
				<xsl:if test="$ancestorRequired = 'false'">
					<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@name"/>, <xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@category"/>, <xsl:value-of 		select="es:metaInformation/es:processInformation/es:referenceFunction/@subCategory"/>, [<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]
				</xsl:if>	
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
