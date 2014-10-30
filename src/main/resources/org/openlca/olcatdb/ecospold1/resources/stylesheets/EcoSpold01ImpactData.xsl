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
	xmlns:es="http://www.EcoInvent.org/EcoSpold01Impact">
	
	<xsl:output 
		method="html" 
		version="1.0" 
		encoding="UTF-8" 
		omit-xml-declaration="no" 
		indent="no" 
		media-type="text/html"/>

	<xsl:template match="es:dataset" mode="TOC">
		<tr>
			<td class="ttl1" width="80%">
				<a><xsl:attribute name="href">#dataset<xsl:value-of select="@number"/>dataset</xsl:attribute><xsl:call-template name="es:emitCompleteDatasetDesignation"/>
				</a>
			</td>
			<td class="ttl1" width="20%" style="text-align:center;">
				<a><xsl:attribute name="href">#dataset<xsl:value-of select="@number"/>flowData</xsl:attribute>&gt;&gt;&gt;</a>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template match="es:dataset" mode="TOCMeta">
		<tr>
			<td class="ttl1">
				<a><xsl:attribute name="href">#dataset<xsl:value-of select="@number"/>dataset</xsl:attribute><xsl:call-template name="es:emitCompleteDatasetDesignation"/>
				</a>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template match="es:dataset" mode="complete">
		<xsl:call-template name="es:emitAnchor"/>
		<a><xsl:attribute name="name">dataset<xsl:value-of select="@number"/>metaInformationEx</xsl:attribute></a>
		<br/>
		<div align="center">
			<a href="#toc"><xsl:call-template name="emitString"><xsl:with-param name="name">tableOfContent</xsl:with-param></xsl:call-template></a>&#160;&#160;-&#160;&#160;
			<a><xsl:attribute name="href">#dataset<xsl:value-of select="@number"/>flowData</xsl:attribute><xsl:call-template name="emitString"><xsl:with-param name="name">exchanges</xsl:with-param></xsl:call-template></a>
			<xsl:if test="count(es:flowData/es:allocation) &gt; 0">
				&#160;&#160;-&#160;&#160;<a><xsl:attribute name="href">#dataset<xsl:value-of select="@number"/>allocation</xsl:attribute><xsl:call-template name="emitString"><xsl:with-param name="name">allocations</xsl:with-param></xsl:call-template></a>
			</xsl:if>
		</div>
		<br/>
		<xsl:apply-templates/>

		<xsl:call-template name="es:emitDatasetInformation"/>
	</xsl:template>

	<xsl:template match="es:dataset" mode="metaOnly">
		<xsl:call-template name="es:emitAnchor"/>
		<a><xsl:attribute name="name">dataset<xsl:value-of select="@number"/>metaInformationEx</xsl:attribute></a>
		<br/>
		<div align="center">
			<a href="#toc"><xsl:call-template name="emitString"><xsl:with-param name="name">tableOfContent</xsl:with-param></xsl:call-template></a>
		</div>
		<br/>
		<xsl:apply-templates select="es:metaInformation"/>

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
	
	<xsl:template match="es:flowData">
		<a><xsl:attribute name="name">dataset<xsl:value-of select="ancestor::es:dataset/@number"/>flowData</xsl:attribute></a>

		<br/>
		<div align="center">
			<a href="#toc"><xsl:call-template name="emitString"><xsl:with-param name="name">tableOfContent</xsl:with-param></xsl:call-template></a>&#160;&#160;-&#160;&#160;
			<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>metaInformationEx</xsl:attribute>Meta information</a>
		</div>
		<br/>

		<xsl:call-template name="es:emitTitle">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">flowData</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="ttlClass">ttl1</xsl:with-param>
		</xsl:call-template>

		<a><xsl:attribute name="name">dataset<xsl:value-of select="ancestor::es:dataset/@number"/>exchange</xsl:attribute></a>

		<xsl:call-template name="es:emitTitle">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">characterisationFactors</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="ttlClass">ttl2</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="es:emitExchanges"/>
		
		<xsl:call-template name="es:emitTitle">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">characterisationFactorDetails</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="ttlClass">ttl2</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="es:emitDetailedExchanges"/>
		<a><xsl:attribute name="name">dataset<xsl:value-of select="ancestor::es:dataset/@number"/>allocation</xsl:attribute></a>
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

	<xsl:template match="es:timePeriod">
		<xsl:call-template name="es:emitLeafChilds">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">timePeriod</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="es:geography">
		<xsl:call-template name="es:emitLeaf">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">geography</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="es:representativeness">
		<xsl:call-template name="es:emitLeaf">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">representativeness</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="es:source">
		<xsl:call-template name="es:emitLeafRepetitiveChilds">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">source</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="elementTitle"><xsl:call-template name="emitString"><xsl:with-param name="name">pageNumbers</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="appendValue">true</xsl:with-param>
		</xsl:call-template>
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

	<xsl:template match="es:dataGeneratorAndPublication">
		<xsl:call-template name="es:emitLeafRepetitiveChilds">	
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">dataGeneratorAndPublication</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="elementTitle"><xsl:call-template name="emitString"><xsl:with-param name="name">pageNumbers</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="es:person">
		<xsl:call-template name="es:emitLeaf">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">person</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="appendValue">true</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="es:referenceFunction">
		<xsl:call-template name="es:emitLeafChilds">	
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">referenceFunction</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="elementTitle"><xsl:call-template name="emitString"><xsl:with-param name="name">synonyms</xsl:with-param></xsl:call-template></xsl:with-param>
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
				<td nowrap="nowrap" style="text-align:right;">
					<xsl:attribute name="class">
						<xsl:value-of select="$ttlClass"/>
					</xsl:attribute>
					<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>dataset</xsl:attribute><xsl:call-template name="es:emitCompleteDatasetDesignation"><xsl:with-param name="ancestorRequired">true</xsl:with-param></xsl:call-template></a>
				</td>
			</tr>
		</table>
	</xsl:template>

	<xsl:template name="es:emitExchangeName">
		<xsl:param name="exchangeID"/>

		<xsl:choose>
			<xsl:when test="$lang='ja'">
				<xsl:value-of select="ancestor::es:flowData/es:exchange[@number = $exchangeID]/@localName"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="ancestor::es:flowData/es:exchange[@number = $exchangeID]/@name"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="es:emitExchanges">
		<table width="100%">
			<tr>
				<td class="ttc3" width="40"><xsl:call-template name="emitString"><xsl:with-param name="name">number</xsl:with-param></xsl:call-template></td>
				<td class="ttc3"><xsl:call-template name="emitString"><xsl:with-param name="name">name</xsl:with-param></xsl:call-template></td>
				<td class="ttc3" width="120"><xsl:call-template name="emitString"><xsl:with-param name="name">meanValue</xsl:with-param></xsl:call-template></td>
				<td class="ttc3" width="50"><xsl:call-template name="emitString"><xsl:with-param name="name">unit</xsl:with-param></xsl:call-template></td>
			</tr>

			<xsl:for-each select="es:exchange">
				<xsl:sort data-type="text" order="ascending" select="@category"/>
				<xsl:sort data-type="text" order="ascending" select="@subCategory"/>
				<xsl:sort data-type="text" order="ascending" select="@name"/>
					
				<xsl:call-template name="es:emitExchange"/>
			</xsl:for-each>
		</table>
	</xsl:template>

	<xsl:template name="es:emitExchange">
		<xsl:variable name="prevCategory">
			<xsl:call-template name="es:prevCat">
				<xsl:with-param name="pos"><xsl:value-of select="position() - 1"/></xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:variable name="prevSubCategory">
			<xsl:call-template name="es:prevCat">
				<xsl:with-param name="pos"><xsl:value-of select="position() - 1"/></xsl:with-param>
				<xsl:with-param name="subcategory">true</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:if test="(@category != '') and ((position() = 1) or (@category != $prevCategory) or (@subCategory != $prevSubCategory))">
			<tr>
				<td class="ttl4" colspan="8">
					<xsl:choose>
						<xsl:when test="$lang='ja'">
							<xsl:value-of select="@localCategory"/><xsl:if test="@localSubCategory">/<xsl:value-of select="@localSubCategory"/></xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="@category"/><xsl:if test="@subCategory">/<xsl:value-of select="@subCategory"/></xsl:if>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>

		<tr>
			<td class="ttc4">
				<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>exchange<xsl:value-of select="@number"/></xsl:attribute><xsl:value-of select="@number"/></a>
			</td>
			<xsl:choose>
				<xsl:when test="$lang='ja'">
					<td class="ttl4"><xsl:value-of select="@localName"/></td>
				</xsl:when>
				<xsl:otherwise>
					<td class="ttl4"><xsl:value-of select="@name"/></td>
				</xsl:otherwise>
			</xsl:choose>
			<td class="ttr4"><xsl:value-of select="@meanValue"/></td>
			<td class="ttl4"><xsl:value-of select="../../es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>/<xsl:value-of select="@unit"/></td>
		</tr>
	</xsl:template>
	
	<xsl:template name="es:prevCat">
		<xsl:param name="pos"/>
		<xsl:param name="subcategory">false</xsl:param>
		
		<xsl:if test="$pos &gt; 0">
			<xsl:for-each select="ancestor::es:flowData/es:exchange">
				<xsl:sort data-type="text" order="ascending" select="@category"/>
				<xsl:sort data-type="text" order="ascending" select="@subCategory"/>
				<xsl:sort data-type="text" order="ascending" select="@name"/>
					
				<xsl:if test="position() = $pos">
					<xsl:if test="$subcategory = 'false'">
						<xsl:value-of select="@category"/>
					</xsl:if>
					<xsl:if test="$subcategory = 'true'">
						<xsl:value-of select="@subCategory"/>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>

	<xsl:template name="es:emitDetailedExchanges">
		<xsl:for-each select="es:exchange">
			<xsl:sort data-type="text" order="ascending" select="@category"/>
			<xsl:sort data-type="text" order="ascending" select="@subCategory"/>
			<xsl:sort data-type="text" order="ascending" select="@name"/>
					
			<xsl:call-template name="es:emitDetailedExchange"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="es:emitDetailedExchange">
		<xsl:call-template name="es:emitAnchor">
			<xsl:with-param name="appendValue">true</xsl:with-param>
		</xsl:call-template>
		
		<table width="100%">
			<tr>
				<td class="ttl4" colspan="3">
					<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>exchange</xsl:attribute><xsl:value-of select="@name"/> (<xsl:value-of select="@number"/>)</a>
				</td>
				<td class="ttr4">[<xsl:value-of select="@unit"/>]</td>
			</tr>
			<tr>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">localName</xsl:with-param></xsl:call-template></td>
				<td class="ttl4"><xsl:value-of select="@localName"/></td>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">meanValue</xsl:with-param></xsl:call-template></td>
				<td class="ttr4" width="20%"><xsl:value-of select="@meanValue"/></td>
			</tr>
			<tr>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">localCategory</xsl:with-param></xsl:call-template></td>
				<td class="ttl4"><xsl:value-of select="@localCategory"/></td>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">referenceToSource</xsl:with-param></xsl:call-template></td>
				<td class="ttr4" width="20%"><xsl:value-of select="@referenceToSource"/></td>
			</tr>
			<tr>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">localSubcategory</xsl:with-param></xsl:call-template></td>
				<td class="ttl4"><xsl:value-of select="@localSubCategory"/></td>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">pageNumbers</xsl:with-param></xsl:call-template></td>
				<td class="ttr4" width="20%"><xsl:value-of select="@pageNumbers"/></td>
			</tr>
			<tr>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">generalComment</xsl:with-param></xsl:call-template></td>
				<td class="ttl4" colspan="3"><xsl:value-of select="@generalComment"/></td>
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
		
		<xsl:if test="$ancestorRequired = 'true'">
			<xsl:choose>
				<xsl:when test="$lang = 'ja'">
					 <xsl:if test="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@localSubCategory != ''"><xsl:value-of 	select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@localSubCategory"/>, </xsl:if><xsl:value-of 	select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@localName"/>
				</xsl:when>
				<xsl:otherwise>
					 <xsl:if test="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@subCategory != ''"><xsl:value-of 	select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@subCategory"/>, </xsl:if><xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/	@name"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	
		<xsl:if test="$ancestorRequired = 'false'">
			<xsl:choose>
				<xsl:when test="$lang = 'ja'">
					<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@localCategory"/>, <xsl:if test="es:metaInformation/es:processInformation/es:referenceFunction/@localSubCategory != ''">	<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@localSubCategory"/>, </xsl:if><xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@localName"/>, <xsl:value-of select="es:metaInformation/es:processInformation/es:geography/@location"/>, [<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@category"/>, <xsl:if test="es:metaInformation/es:processInformation/es:referenceFunction/@subCategory != ''"><xsl:value-of 	select="es:metaInformation/es:processInformation/es:referenceFunction/@subCategory"/>, </xsl:if><xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@name"/>, <xsl:value-of 	select="es:metaInformation/es:processInformation/es:geography/@location"/>, [<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>