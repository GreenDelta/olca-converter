<?xml version="1.0" encoding="UTF-8"?>

	<!--
		The contents of this file are subject to the EcoSpold Public License
		Version 1.0 (the "License"); you may not use this file except in
		compliance with the License. You may obtain a copy of the License at
		http://www.ecoinvent.ch. Software distributed under the License is
		distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
		express or implied. See the License for the specific language
		governing rights and limitations under the License. The Original Code
		consists of the EcoSpold data format and EcoSpold Access. The Original
		Code was created by the ecoinvent Centre, Switzerland (Swiss Centre
		for Life Cycle Inventories) and ifu Hamburg GmbH, Germany. Portions
		created by the ecoinvent Centre and ifu Hamburg GmbH are Copyright (C)
		ecoinvent Centre. All Rights Reserved.
	-->

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:es="http://www.EcoInvent.org/EcoSpold02">

	<xsl:output method="html" media-type="text/html" version="1.0"
		encoding="UTF-8" omit-xml-declaration="no" indent="no" />

	<xsl:template match="/">
	
		<xsl:param name="activity">
			<xsl:value-of select="es:ecoSpold/es:activityDataset/es:activityDescription/es:activity"></xsl:value-of>
		</xsl:param>
	
		<html>
			<head>
				<title>
					<xsl:value-of
						select="activity/es:activityName[@xml:lang='en']" />
				</title>
				<style type="text/css">
					a:link{color:000080;
					text-decoration:none;}
					a:visited{color:000080;
					text-decoration:none;}
					a:hover{color:red;
					text-decoration:underline;}
					body {background-color:#C0FFC0;
					color:#000000;
					font-family:arial, verdana, sans-serif;
					font-size:12pt;}
					table {background:#C0FFC0;
					height:20px;
					color:#000000;}
					td { background:#C0FFC0;
					font-size:9pt;
					text-align:left;
					color:#000000;}
					td.ttl {background:#008000;
					font-size:16pt;
					height:22;
					text-align:left;
					color:#FFFFFF;}
					td.ttl1 {background:#00C000;
					font-size:14pt;
					font-weight: bold;
					height:22;
					text-align:left;
					color:#000000;}
					td.ttl2 {background:#40FF40;
					font-size:12pt;
					font-style: italic;
					height:22;
					text-align:left;
					color:#000000;}
					td.ttl3 {background:#80FF80;
					font-size:10pt;
					font-weight: bold;
					height:22;
					text-align:left;}
					td.ttl3nb {background:#80FF80;
					font-size:10pt;
					height:22;
					text-align:left;}
					td.ttc3 {background:#80FF80;
					font-size:10pt;
					height:22;
					padding-left: 5;
					padding-right: 5;
					text-align:center;}
					td.ttl4 {background:#FFFFFF;
					font-size:10pt;
					height:22;
					padding-left: 5;
					padding-right: 5;
					text-align:left;}
					td.ttc4 {background:#FFFFFF;
					font-size:10pt;
					height:22;
					padding-left: 5;
					padding-right: 5;
					text-align:center;}
					td.ttr4 {background:#FFFFFF;
					font-size:10pt;
					height:22;
					padding-left: 5;
					padding-right: 5;
					text-align:right;}
				</style>
			</head>
			<body>
				
				<!-- activity description -->
				<p>
				<table width="100%" >
					<tr valign="top">
						<td class="ttl1" colspan="2">Activity description</td>
					</tr>
					<tr valign="top">
						<td class="ttl3" width="25%">ID</td>
						<td class="ttl4" width="75%">
							<xsl:value-of select="//es:activity/@id" />
						</td>
					</tr>
					<tr valign="top">
						<td class="ttl3">Name ID</td>
						<td class="ttl4">
							<xsl:value-of select="//es:activity/@activityNameId" />
						</td>
					</tr>
					<tr valign="top">
						<td class="ttl3">Parent ID</td>
						<td class="ttl4">
							<xsl:value-of select="//es:activity/@parentActivityId" />
						</td>
					</tr>
					<tr valign="top">
						<td class="ttl3">Inheritance depth</td>
						<td class="ttl4">
							<xsl:value-of select="//es:activity/@inheritanceDepth" />
						</td>
					</tr>
					<tr valign="top">
						<!-- (1 = Unit process; 2 = System terminated) -->
						<td class="ttl3">Type</td>
						<td class="ttl4">
							<xsl:value-of select="//es:activity/@type" />
						</td>
					</tr>
					<tr valign="top">
						<!-- (0 = ordinary activity (default), 1 = market activity, 2 = IO/residual activity, 3 = import activity, 4 = production mix, 5 = supply mix) -->
						<td class="ttl3">Special activity type</td>
						<td class="ttl4">
							<xsl:value-of select="//es:activity/@specialActivityType" />
						</td>
					</tr>
					<tr valign="top">
						<!-- (0 = Undefined (default); 1 = Net values; 2 = Gross values) -->
						<td class="ttl3">Energy values</td>
						<td class="ttl4">
							<xsl:value-of select="//es:activity/@energyValues" />
						</td>
					</tr>
					<tr valign="top">
						<td class="ttl3">Master allocation property ID</td>
						<td class="ttl4">
							<xsl:value-of select="//es:activity/@masterAllocationPropertyId" />
						</td>
					</tr>
					<tr valign="top">
						<td class="ttl3">Data set icon</td>
						<td class="ttl4">
							<xsl:if test="//es:activity/@datasetIcon">
								<xsl:element name="img">
									<xsl:attribute name="src">
										<xsl:value-of select="//es:activity/@datasetIcon" />
									</xsl:attribute>
								</xsl:element>						
							</xsl:if>
						</td>
					</tr>
					<tr valign="top">
						<td class="ttl3">Name(s)</td>
						<td class="ttl4">
							<xsl:for-each select="//es:activity/es:activityName">
								<xsl:value-of select="@xml:lang" /> - <xsl:value-of select="." />
								<br/> 
							</xsl:for-each>							
						</td>
					</tr>
					<tr valign="top">
						<td class="ttl3">Synonyms</td>
						<td class="ttl4">
							<xsl:for-each select="//es:activity/es:synonym">
								<xsl:value-of select="@xml:lang" /> - <xsl:value-of select="." />
								<br/> 
							</xsl:for-each>							
						</td>
					</tr>
					
				</table>
				</p>
				
				<p>
				<table width="100%" >
					<tr valign="top">
						<td class="ttl1" colspan="2">Geography</td>
					</tr>	
					
				</table>
				</p>
				
			</body>
		</html>
	</xsl:template>


</xsl:stylesheet>
