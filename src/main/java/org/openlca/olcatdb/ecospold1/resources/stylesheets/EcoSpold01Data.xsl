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
	xmlns:es="http://www.EcoInvent.org/EcoSpold01"
	xmlns:ues="http://www.umberto.de/EcoSpold01Extensions">
	
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
			<xsl:if test="ancestor-or-self::es:dataset/ues:transitionIcon">
				<tr><td><xsl:call-template name="emitString"><xsl:with-param name="name">transitionIcon</xsl:with-param></xsl:call-template></td></tr>
			</xsl:if>
			<xsl:if test="ancestor-or-self::es:dataset/ues:transitionSpecification">
				<tr><td><xsl:call-template name="emitString"><xsl:with-param name="name">transitionSpecification</xsl:with-param></xsl:call-template></td></tr>
			</xsl:if>
			<xsl:if test="ancestor-or-self::es:dataset/ues:transitionParameters">
				<tr><td><xsl:call-template name="emitString"><xsl:with-param name="name">transitionParameters</xsl:with-param></xsl:call-template></td></tr>
			</xsl:if>
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
			<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>metaInformationEx</xsl:attribute><xsl:call-template name="emitString"><xsl:with-param name="name">metaInformation</xsl:with-param></xsl:call-template></a>
			<xsl:if test="count(es:allocation) &gt; 0">
				&#160;&#160;-&#160;&#160;<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>allocation</xsl:attribute><xsl:call-template name="emitString"><xsl:with-param name="name">allocations</xsl:with-param></xsl:call-template></a>
			</xsl:if>
		</div>
		<br/>

		<xsl:call-template name="es:emitTitle">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">flowData</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="ttlClass">ttl1</xsl:with-param>
		</xsl:call-template>

		<a><xsl:attribute name="name">dataset<xsl:value-of select="ancestor::es:dataset/@number"/>exchange</xsl:attribute></a>

		<xsl:call-template name="es:emitTitle">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">exchanges</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="ttlClass">ttl2</xsl:with-param>
		</xsl:call-template>

		<xsl:if test="count(es:exchange[es:inputGroup = 4]) != 0">
			<xsl:call-template name="es:emitTitle">
				<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">fromNature</xsl:with-param></xsl:call-template></xsl:with-param>
				<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
			</xsl:call-template>
			
			<xsl:call-template name="es:emitExchanges">
				<xsl:with-param name="inputCat">4</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if test="count(es:exchange[es:inputGroup = 5]) != 0">
			<xsl:call-template name="es:emitTitle">
				<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">fromTechnosphere</xsl:with-param></xsl:call-template></xsl:with-param>
				<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
			</xsl:call-template>

			<xsl:call-template name="es:emitExchanges">
				<xsl:with-param name="inputCat">5</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="count(es:exchange[es:outputGroup = 0]) != 0">
			<xsl:call-template name="es:emitTitle">
				<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">referenceProduct</xsl:with-param></xsl:call-template></xsl:with-param>
				<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
			</xsl:call-template>

			<xsl:call-template name="es:emitExchanges">
				<xsl:with-param name="outputCat">0</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if test="count(es:exchange[es:outputGroup = 2]) != 0">
			<xsl:call-template name="es:emitTitle">
				<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">allocatedBy-product</xsl:with-param></xsl:call-template></xsl:with-param>
				<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
			</xsl:call-template>

			<xsl:call-template name="es:emitExchanges">
				<xsl:with-param name="outputCat">2</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="count(es:exchange[es:outputGroup = 4]) != 0">
			<xsl:call-template name="es:emitTitle">
				<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">toNature</xsl:with-param></xsl:call-template></xsl:with-param>
				<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
			</xsl:call-template>

			<xsl:call-template name="es:emitExchanges">
				<xsl:with-param name="outputCat">4</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="(count(es:exchange[es:outputGroup]) = 0) and (count(es:exchange[es:inputGroup]) = 0)">
			<xsl:call-template name="es:emitExchanges"/>
		</xsl:if>

		<xsl:call-template name="es:emitTitle">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">detailedExchanges</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="ttlClass">ttl2</xsl:with-param>
		</xsl:call-template>

		<xsl:if test="count(es:exchange[es:inputGroup = 4]) != 0">
			<xsl:call-template name="es:emitTitle">
				<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">fromNature</xsl:with-param></xsl:call-template></xsl:with-param>
				<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
			</xsl:call-template>
			
			<xsl:call-template name="es:emitDetailedExchanges">
				<xsl:with-param name="inputCat">4</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if test="count(es:exchange[es:inputGroup = 5]) != 0">
			<xsl:call-template name="es:emitTitle">
				<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">fromTechnosphere</xsl:with-param></xsl:call-template></xsl:with-param>
				<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
			</xsl:call-template>

			<xsl:call-template name="es:emitDetailedExchanges">
				<xsl:with-param name="inputCat">5</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="count(es:exchange[es:outputGroup = 0]) != 0">
			<xsl:call-template name="es:emitTitle">
				<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">referenceProduct</xsl:with-param></xsl:call-template></xsl:with-param>
				<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
			</xsl:call-template>

			<xsl:call-template name="es:emitDetailedExchanges">
				<xsl:with-param name="outputCat">0</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if test="count(es:exchange[es:outputGroup = 2]) != 0">
			<xsl:call-template name="es:emitTitle">
				<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">allocatedBy-product</xsl:with-param></xsl:call-template></xsl:with-param>
				<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
			</xsl:call-template>

			<xsl:call-template name="es:emitDetailedExchanges">
				<xsl:with-param name="outputCat">2</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="count(es:exchange[es:outputGroup = 4]) != 0">
			<xsl:call-template name="es:emitTitle">
				<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">toNature</xsl:with-param></xsl:call-template></xsl:with-param>
				<xsl:with-param name="ttlClass">ttl3</xsl:with-param>
			</xsl:call-template>

			<xsl:call-template name="es:emitDetailedExchanges">
				<xsl:with-param name="outputCat">4</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="(count(es:exchange[es:outputGroup]) = 0) and (count(es:exchange[es:inputGroup]) = 0)">
			<xsl:call-template name="es:emitDetailedExchanges"/>
		</xsl:if>

		<a><xsl:attribute name="name">dataset<xsl:value-of select="ancestor::es:dataset/@number"/>allocation</xsl:attribute></a>

		<xsl:if test="es:allocation">
			<br/>
			<div align="center">
				<a href="#toc"><xsl:call-template name="emitString"><xsl:with-param name="name">tableOfContent</xsl:with-param></xsl:call-template></a>&#160;&#160;-&#160;&#160;
				<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>metaInformationEx		</xsl:attribute><xsl:call-template name="emitString"><xsl:with-param name="name">metaInformation</xsl:with-param></xsl:call-template></a>&#160;&#160;-&#160;&#160;
				<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>flowData</xsl:attribute><xsl:call-template name="emitString"><xsl:with-param name="name">exchanges</xsl:with-param></xsl:call-template></a>
			</div>
			<br/>

			<xsl:call-template name="es:emitTitle">
				<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">allocations</xsl:with-param></xsl:call-template></xsl:with-param>
				<xsl:with-param name="ttlClass">ttl2</xsl:with-param>
			</xsl:call-template>

			<table width="100%">
				<tr>
					<td class="ttc3"><xsl:call-template name="emitString"><xsl:with-param name="name">referenceToCoproduct</xsl:with-param></xsl:call-template></td>
					<td class="ttc3"><xsl:call-template name="emitString"><xsl:with-param name="name">allocationMethod</xsl:with-param></xsl:call-template></td>
					<td class="ttc3"><xsl:call-template name="emitString"><xsl:with-param name="name">fraction</xsl:with-param></xsl:call-template></td>
					<td class="ttc3"><xsl:call-template name="emitString"><xsl:with-param name="name">explanations</xsl:with-param></xsl:call-template></td>
					<td class="ttc3"><xsl:call-template name="emitString"><xsl:with-param name="name">referenceToInputOoutput</xsl:with-param></xsl:call-template></td>
				</tr>
			
				<xsl:for-each select="es:allocation">
					<tr>
						<td class="ttl4" nowrap="nowrap">
							<xsl:call-template name="es:emitExchangeName">
								<xsl:with-param name="exchangeID">
									<xsl:value-of select="@referenceToCoProduct"/>
								</xsl:with-param>
							</xsl:call-template>&#160;&#160;<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>exchange<xsl:value-of select="@referenceToCoProduct"/></xsl:attribute>(<xsl:value-of select="@referenceToCoProduct"/>)</a>
						</td>
						<td class="ttc4"><xsl:value-of select="@allocationMethod"/></td>
						<td class="ttc4"><xsl:value-of select="@fraction"/></td>
						<td class="ttc4"><xsl:value-of select="@explanations"/></td>
						<td class="ttl4">
							<xsl:call-template name="es:emitExchangeName">
								<xsl:with-param name="exchangeID">
									<xsl:value-of select="es:referenceToInputOutput"/>
								</xsl:with-param>
							</xsl:call-template>&#160;&#160;<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>exchange<xsl:value-of select="es:referenceToInputOutput"/></xsl:attribute>(<xsl:value-of select="es:referenceToInputOutput"/>)</a>
						</td>
					</tr>
					
					<xsl:if test="count(es:referenceToInputOutput) &gt; 1">
						<xsl:for-each select="es:referenceToInputOutput">
							<xsl:if test="position() &gt; 1">
								<tr>
									<td class="ttc4"/>
									<td class="ttc4"/>
									<td class="ttc4"/>
									<td class="ttc4"/>
									<td class="ttl4">
										<xsl:call-template name="es:emitExchangeName">
											<xsl:with-param name="exchangeID">
												<xsl:value-of select="."/>
											</xsl:with-param>
										</xsl:call-template>&#160;&#160;<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>exchange<xsl:value-of select="."/></xsl:attribute>(<xsl:value-of select="."/>)</a>
									</td>
								</tr>
							</xsl:if>
						</xsl:for-each>
					</xsl:if>
				</xsl:for-each>
			</table>
		</xsl:if>
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

	<xsl:template match="es:technology">
		<xsl:call-template name="es:emitLeaf">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">technology</xsl:with-param></xsl:call-template></xsl:with-param>
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
					<a><xsl:attribute name="href">#dataset<xsl:value-of select="ancestor::es:dataset/@number"/>dataset</xsl:attribute><xsl:call-template name="emitCompleteAncestorDatasetDesignation"></xsl:call-template></a>
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
		<xsl:param name="inputCat" select="-1"/>
		<xsl:param name="outputCat" select="-1"/>
		
		<xsl:variable name="cntLocation">
			<xsl:value-of select="count(es:exchange[es:outputGroup = $outputCat]/@location) + count(es:exchange[es:inputGroup = $inputCat]/@infrastructureProcess)"/>
		</xsl:variable>

		<table width="100%">
			<tr>
				<td class="ttc3" width="40"><xsl:call-template name="emitString"><xsl:with-param name="name">number</xsl:with-param></xsl:call-template></td>
				<td class="ttc3"><xsl:call-template name="emitString"><xsl:with-param name="name">name</xsl:with-param></xsl:call-template></td>
				<xsl:if test="$cntLocation &gt; 0">
					<td class="ttc3" width="80"><xsl:call-template name="emitString"><xsl:with-param name="name">location</xsl:with-param></xsl:call-template></td>
					<td class="ttc3" width="40"><xsl:call-template name="emitString"><xsl:with-param name="name">infra</xsl:with-param></xsl:call-template></td>
				</xsl:if>
				<td class="ttc3" width="120"><xsl:call-template name="emitString"><xsl:with-param name="name">meanValue</xsl:with-param></xsl:call-template></td>
				<td class="ttc3" width="40"><xsl:call-template name="emitString"><xsl:with-param name="name">unit</xsl:with-param></xsl:call-template></td>
				<td class="ttc3" width="120"><xsl:call-template name="emitString"><xsl:with-param name="name">uncertaintyType</xsl:with-param></xsl:call-template></td>
				<td class="ttc3" width="40"><xsl:call-template name="emitString"><xsl:with-param name="name">standardDeviation95</xsl:with-param></xsl:call-template></td>
			</tr>

			<xsl:choose>
				<xsl:when test="$inputCat != -1">
					<xsl:for-each select="es:exchange[es:inputGroup = $inputCat]">
						<xsl:sort data-type="text" order="ascending" select="@category"/>
						<xsl:sort data-type="text" order="ascending" select="@subCategory"/>
						<xsl:sort data-type="text" order="ascending" select="@name"/>
						
						<xsl:call-template name="es:emitExchange">
							<xsl:with-param name="inputCat"><xsl:value-of select="$inputCat"/></xsl:with-param>
							<xsl:with-param name="cntLocation"><xsl:value-of select="$cntLocation"/></xsl:with-param>
						</xsl:call-template>
					</xsl:for-each>
				</xsl:when>

				<xsl:when test="$outputCat != -1">
					<xsl:for-each select="es:exchange[es:outputGroup = $outputCat]">
						<xsl:sort data-type="text" order="ascending" select="@category"/>
						<xsl:sort data-type="text" order="ascending" select="@subCategory"/>
						<xsl:sort data-type="text" order="ascending" select="@name"/>
						
						<xsl:call-template name="es:emitExchange">
							<xsl:with-param name="outputCat"><xsl:value-of select="$outputCat"/></xsl:with-param>
							<xsl:with-param name="cntLocation"><xsl:value-of select="$cntLocation"/></xsl:with-param>
						</xsl:call-template>
					</xsl:for-each>
				</xsl:when>

				<xsl:when test="($inputCat = -1) and ($outputCat = -1)">
					<xsl:for-each select="es:exchange[not(es:inputGroup) and not(es:outputGroup)]">
						<xsl:sort data-type="text" order="ascending" select="@category"/>
						<xsl:sort data-type="text" order="ascending" select="@subCategory"/>
						<xsl:sort data-type="text" order="ascending" select="@name"/>
						
						<xsl:call-template name="es:emitExchange">
							<xsl:with-param name="cntLocation"><xsl:value-of select="$cntLocation"/></xsl:with-param>
						</xsl:call-template>
					</xsl:for-each>
				</xsl:when>
			</xsl:choose>	
		</table>
	</xsl:template>

	<xsl:template name="es:emitExchange">
		<xsl:param name="inputCat" select="-1"/>
		<xsl:param name="outputCat" select="-1"/>
		<xsl:param name="cntLocation" select="1"/>

		<xsl:variable name="prevCategory">
			<xsl:call-template name="prevCat">
				<xsl:with-param name="inputCat"><xsl:value-of select="$inputCat"/></xsl:with-param>
				<xsl:with-param name="outputCat"><xsl:value-of select="$outputCat"/></xsl:with-param>
				<xsl:with-param name="pos"><xsl:value-of select="position() - 1"/></xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:variable name="prevSubCategory">
			<xsl:call-template name="prevCat">
				<xsl:with-param name="inputCat"><xsl:value-of select="$inputCat"/></xsl:with-param>
				<xsl:with-param name="outputCat"><xsl:value-of select="$outputCat"/></xsl:with-param>
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
			<xsl:if test="$cntLocation &gt; 0">
				<td class="ttc4"><xsl:value-of select="@location"/></td>
				<td class="ttc4">
					<xsl:choose>
						<xsl:when test="@infrastructureProcess = 'true'"><xsl:call-template name="emitString"><xsl:with-param name="name">true</xsl:with-param></xsl:call-template></xsl:when>
						<xsl:otherwise><xsl:call-template name="emitString"><xsl:with-param name="name">false</xsl:with-param></xsl:call-template></xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			<td class="ttr4"><xsl:value-of select="@meanValue"/></td>
			<td class="ttl4"><xsl:value-of select="@unit"/></td>
			<td class="ttc4">
				<xsl:choose>
					<xsl:when test="@uncertaintyType=0"><xsl:call-template name="emitString"><xsl:with-param name="name">uncertaintyTypeUndefined</xsl:with-param></xsl:call-template></xsl:when>
					<xsl:when test="@uncertaintyType=1"><xsl:call-template name="emitString"><xsl:with-param name="name">uncertaintyTypeLognormal</xsl:with-param></xsl:call-template></xsl:when>
					<xsl:when test="@uncertaintyType=2"><xsl:call-template name="emitString"><xsl:with-param name="name">uncertaintyTypeNormal</xsl:with-param></xsl:call-template></xsl:when>
					<xsl:when test="@uncertaintyType=3"><xsl:call-template name="emitString"><xsl:with-param name="name">uncertaintyTypeTriang</xsl:with-param></xsl:call-template></xsl:when>
					<xsl:when test="@uncertaintyType=4"><xsl:call-template name="emitString"><xsl:with-param name="name">uncertaintyTypeUniform</xsl:with-param></xsl:call-template></xsl:when>
				</xsl:choose>
			</td>
			<td class="ttc4"><xsl:value-of select="@standardDeviation95"/></td>
		</tr>
	</xsl:template>
	
	<xsl:template name="prevCat">
		<xsl:param name="inputCat" select="-1"/>
		<xsl:param name="outputCat" select="-1"/>
		<xsl:param name="pos"/>
		<xsl:param name="subcategory">false</xsl:param>
		
		<xsl:if test="$pos &gt; 0">
		<xsl:choose>
			<xsl:when test="$inputCat != -1">
				<xsl:for-each select="ancestor::es:flowData/es:exchange[es:inputGroup = $inputCat]">
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
			</xsl:when>

			<xsl:when test="$outputCat != -1">
				<xsl:for-each select="ancestor::es:flowData/es:exchange[es:outputGroup = $outputCat]">
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
			</xsl:when>
		</xsl:choose>	
		</xsl:if>
	</xsl:template>

	<xsl:template name="es:emitDetailedExchanges">
		<xsl:param name="inputCat" select="-1"/>
		<xsl:param name="outputCat" select="-1"/>

		<xsl:choose>
			<xsl:when test="($inputCat != -1) and ($inputCat != 4)">
				<xsl:for-each select="es:exchange[es:inputGroup = $inputCat]">
					<xsl:sort data-type="text" order="ascending" select="@category"/>
					<xsl:sort data-type="text" order="ascending" select="@subCategory"/>
					<xsl:sort data-type="text" order="ascending" select="@name"/>
					
					<xsl:call-template name="es:emitDetailedExchange"/>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="$inputCat = 4">
				<xsl:for-each select="es:exchange[es:inputGroup = $inputCat]">
					<xsl:call-template name="es:emitDetailedExchange"/>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="($outputCat != -1) and ($outputCat != 4)">
				<xsl:for-each select="es:exchange[es:outputGroup = $outputCat]">
					<xsl:sort data-type="text" order="ascending" select="@category"/>
					<xsl:sort data-type="text" order="ascending" select="@subCategory"/>
					<xsl:sort data-type="text" order="ascending" select="@name"/>
					
					<xsl:call-template name="es:emitDetailedExchange"/>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="$outputCat = 4">
				<xsl:for-each select="es:exchange[es:outputGroup = $outputCat]">
					<xsl:call-template name="es:emitDetailedExchange"/>
				</xsl:for-each>
			</xsl:when>

			<xsl:when test="($inputCat = -1) and ($outputCat = -1)">
				<xsl:for-each select="es:exchange[not(es:inputGroup) and not(es:outputGroup)]">
					<xsl:call-template name="es:emitDetailedExchange"/>
				</xsl:for-each>
			</xsl:when>
		</xsl:choose>	
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
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">minValue</xsl:with-param></xsl:call-template></td>
				<td class="ttr4" width="20%"><xsl:value-of select="@minValue"/></td>
			</tr>
			<tr>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">localSubcategory</xsl:with-param></xsl:call-template></td>
				<td class="ttl4"><xsl:value-of select="@localSubCategory"/></td>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">maxValue</xsl:with-param></xsl:call-template></td>
				<td class="ttr4" width="20%"><xsl:value-of select="@maxValue"/></td>
			</tr>
			<tr>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">CASNumber</xsl:with-param></xsl:call-template></td>
				<td class="ttl4"><xsl:value-of select="@CASNumber"/></td>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">mostlikelyValue</xsl:with-param></xsl:call-template></td>
				<td class="ttr4" width="20%"><xsl:value-of select="@mostLikelyValue"/></td>
			</tr>
			<tr>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">formula</xsl:with-param></xsl:call-template></td>
				<td class="ttl4"><xsl:value-of select="@formula"/></td>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">referenceToSource</xsl:with-param></xsl:call-template></td>
				<td class="ttr4" width="20%"><xsl:value-of select="@referenceToSource"/></td>
			</tr>
			<tr>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">generalComment</xsl:with-param></xsl:call-template></td>
				<td class="ttl4"><xsl:value-of select="@generalComment"/></td>
				<td class="ttl3nb" width="20%"><xsl:call-template name="emitString"><xsl:with-param name="name">pageNumbers</xsl:with-param></xsl:call-template></td>
				<td class="ttr4" width="20%"><xsl:value-of select="@pageNumbers"/></td>
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
		<xsl:variable name="infra">
			<xsl:if test="es:metaInformation/es:processInformation/es:referenceFunction/@infrastructureProcess = 'yes'"><xsl:call-template name="emitString"><xsl:with-param name="name">datasetContainsInfrastructureData</xsl:with-param></xsl:call-template></xsl:if>
		</xsl:variable>

		<xsl:choose>
			<xsl:when test="$lang = 'ja'">
				<xsl:choose>
					<xsl:when test="es:metaInformation/es:processInformation/es:dataSetInformation/@impactAssessmentResult = 'false'">
						<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@localName"/>, <xsl:value-of select="es:metaInformation/es:processInformation/es:geography/@location"/>, [<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]<xsl:value-of select="$infra"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@localName"/>, <xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@localCategory"/>, <xsl:value-of 	select="es:metaInformation/es:processInformation/es:referenceFunction/@localSubCategory"/>, [<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="es:metaInformation/es:processInformation/es:dataSetInformation/@impactAssessmentResult = 'false'">
						<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@name"/>, <xsl:value-of select="es:metaInformation/es:processInformation/es:geography/@location"/>, [<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]<xsl:value-of select="$infra"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@name"/>, <xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@category"/>, <xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@subCategory"/>, [<xsl:value-of select="es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="emitCompleteAncestorDatasetDesignation">
		<xsl:variable name="infra">
			<xsl:if test="es:metaInformation/es:processInformation/es:referenceFunction/@infrastructureProcess = 'yes'"><xsl:call-template name="emitString"><xsl:with-param name="name">datasetContainsInfrastructureData</xsl:with-param></xsl:call-template></xsl:if>
		</xsl:variable>

		<xsl:choose>
			<xsl:when test="$lang = 'ja'">
				<xsl:choose>
					<xsl:when test="ancestor::es:dataset/es:metaInformation/es:processInformation/es:dataSetInformation/@impactAssessmentResult = 'false'">
					<xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@localName"/>, <xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:geography/@location"/>, [<xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]<xsl:value-of select="$infra"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@localName"/>, <xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@localCategory"/>, <xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@localSubCategory"/>, [<xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="ancestor::es:dataset/es:metaInformation/es:processInformation/es:dataSetInformation/@impactAssessmentResult = 'false'">
					<xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@name"/>, <xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:geography/@location"/>, [<xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]<xsl:value-of select="$infra"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@name"/>, <xsl:value-of select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@category"/>, <xsl:value-of 	select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@subCategory"/>, [<xsl:value-of 		select="ancestor::es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@unit"/>]
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="ues:transitionSpecification">
		<xsl:call-template name="es:emitAnchor"/>
		<br/>
		<div align="center">
			<a href="#toc"><xsl:call-template name="emitString"><xsl:with-param name="name">tableOfContent</xsl:with-param></xsl:call-template></a>
		</div>
		<br/>

		<xsl:call-template name="es:emitTitle">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">transitionSpecificationTitle</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="ttlClass">ttl2</xsl:with-param>
		</xsl:call-template>

		<table width="100%">
			<tbody>
				<xsl:for-each select="ues:line">
					<tr>
						<td class="ttl4"><xsl:value-of select="."/></td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="ues:transitionIcon"/>
	
	<xsl:template match="ues:transitionParameters">
		<xsl:call-template name="es:emitAnchor"/>
		<br/>
		<div align="center">
			<a href="#toc"><xsl:call-template name="emitString"><xsl:with-param name="name">tableOfContent</xsl:with-param></xsl:call-template></a>
		</div>
		<br/>

		<xsl:call-template name="es:emitTitle">
			<xsl:with-param name="title"><xsl:call-template name="emitString"><xsl:with-param name="name">transitionParametersTitle</xsl:with-param></xsl:call-template></xsl:with-param>
			<xsl:with-param name="ttlClass">ttl2</xsl:with-param>
		</xsl:call-template>

		<table width="100%">
			<tbody>
				<tr>
					<td class="ttl3"><xsl:call-template name="emitString"><xsl:with-param name="name">variable</xsl:with-param></xsl:call-template></td>
					<td class="ttl3"><xsl:call-template name="emitString"><xsl:with-param name="name">name</xsl:with-param></xsl:call-template></td>
					<td class="ttl3"><xsl:call-template name="emitString"><xsl:with-param name="name">quantity</xsl:with-param></xsl:call-template></td>
					<td class="ttl3"><xsl:call-template name="emitString"><xsl:with-param name="name">unit</xsl:with-param></xsl:call-template></td>
				</tr>
				<xsl:for-each select="ues:transitionParameter">
					<xsl:sort select="@var"/>
					<tr>
						<td class="ttl4"><xsl:value-of select="@var"/></td>
						<td class="ttl4"><xsl:value-of select="@name"/></td>
						<td class="ttl4"><xsl:value-of select="@quantity"/></td>
						<td class="ttl4"><xsl:value-of select="@unit"/></td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>
</xsl:stylesheet>

