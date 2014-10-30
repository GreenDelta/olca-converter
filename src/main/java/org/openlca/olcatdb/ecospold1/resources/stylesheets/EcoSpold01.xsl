<?xml version="1.0" encoding="UTF-8"?>
<!--
The contents of this file are subject to the EcoSpold Public License Version 1.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.ecoinvent.ch.
Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the specific language governing rights and limitations under the License.
The Original Code consists of the EcoSpold data format and EcoSpold Access.
The Original Code was created by the ecoinvent Centre, Switzerland (Swiss Centre for Life Cycle Inventories) and ifu Hamburg GmbH, Germany. Portions created by the ecoinvent Centre and ifu Hamburg GmbH are Copyright (C) ecoinvent Centre. All Rights Reserved.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:es="http://www.EcoInvent.org/EcoSpold01" xmlns:esi="http://www.EcoInvent.org/EcoSpold01Impact" xmlns:ese="http://www.EcoInvent.org/EcoSpold01Elementary" xmlns:ues="http://www.umberto.de/EcoSpold01Extensions" 
exclude-result-prefixes="es esi ese">
	<xsl:include href="EcoSpold01.i18n.xsl"/>
	<xsl:include href="EcoSpold01Data.xsl"/>
	<xsl:include href="EcoSpold01ImpactData.xsl"/>
	<xsl:include href="EcoSpold01ElementaryData.xsl"/>

	<xsl:output 
		method="html" 
		media-type="text/html" 
		version="1.0" 
		encoding="UTF-8" 
		omit-xml-declaration="no" 
		indent="no"/>

	<xsl:param name="body">html</xsl:param>
	<xsl:param name="mode">complete</xsl:param>

	<xsl:template match="ues:*"/>

	<xsl:template match="/">
		<xsl:if test="$body='html'">
			<html>
				<xsl:attribute name="lang"><xsl:value-of select="$lang"/></xsl:attribute>
				<head>
					<xsl:choose>
						<xsl:when test="es:ecoSpold">
							<title>
								<xsl:value-of select="es:ecoSpold/es:dataset/es:metaInformation/es:processInformation/es:referenceFunction/@name"/>
							</title>
						</xsl:when>
						<xsl:when test="ese:ecoSpold">
							<title>
								<xsl:value-of select="ese:ecoSpold/ese:dataset/ese:metaInformation/ese:processInformation/ese:referenceFunction/@name"/>
							</title>
						</xsl:when>
						<xsl:when test="esi:ecoSpold">
							<title>
								<xsl:value-of select="esi:ecoSpold/esi:dataset/esi:metaInformation/esi:processInformation/esi:referenceFunction/@name"/>
							</title>
						</xsl:when>
					</xsl:choose>
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
					td {	background:#C0FFC0;
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
					<table width="100%">
						<xsl:if test="count(es:ecoSpold/es:dataset) + count(ese:ecoSpold/ese:dataset) + count(esi:ecoSpold/esi:dataset) &gt; 1">
						<tr>
							<td valign="top">
								<a name="toc"/>
								<table width="100%">
									<xsl:choose>
										<xsl:when test="$mode = 'complete'">
											<xsl:choose>
												<xsl:when test="ese:ecoSpold">
													<tr>
														<td class="ttl" width="100%"><xsl:call-template name="emitString"><xsl:with-param name="name">dataset</xsl:with-param></xsl:call-template></td>
													</tr>
												</xsl:when>
												<xsl:otherwise>
													<tr>
														<td class="ttl" width="70%"><xsl:call-template name="emitString"><xsl:with-param name="name">dataset</xsl:with-param></xsl:call-template></td>
														<td class="ttl" width="30%" style="text-align:center;"><xsl:call-template name="emitString"><xsl:with-param name="name">flowData</xsl:with-param></xsl:call-template></td>
													</tr>
												</xsl:otherwise>
											</xsl:choose>
											<xsl:apply-templates select="es:ecoSpold/es:dataset" mode="TOC"/>
											<xsl:apply-templates select="esi:ecoSpold/esi:dataset" mode="TOC"/>
											<xsl:apply-templates select="ese:ecoSpold/ese:dataset" mode="TOC"/>
										</xsl:when>
										<xsl:when test="$mode = 'metaOnly'">
											<tr>
												<td class="ttl"><xsl:call-template name="emitString"><xsl:with-param name="name">dataset</xsl:with-param></xsl:call-template></td>
											</tr>
											<xsl:apply-templates select="es:ecoSpold/es:dataset" mode="TOCMeta"/>
											<xsl:apply-templates select="esi:ecoSpold/esi:dataset" mode="TOCMeta"/>
											<xsl:apply-templates select="ese:ecoSpold/ese:dataset" mode="TOC"/>
										</xsl:when>
									</xsl:choose>
								</table>
							</td>
						</tr>
						</xsl:if>
						<tr>
							<td valign="top">
								<xsl:if test="(count(es:ecoSpold/es:dataset) + count(ese:ecoSpold/ese:dataset) + count(esi:ecoSpold/esi:dataset)) &lt;= 1">
									<a name="toc"/>
								</xsl:if>
								<xsl:choose>
									<xsl:when test="$mode = 'complete'">
										<xsl:apply-templates select="es:ecoSpold/es:dataset" mode="complete"/>
										<xsl:apply-templates select="esi:ecoSpold/esi:dataset" mode="complete"/>
										<xsl:apply-templates select="ese:ecoSpold/ese:dataset" mode="complete"/>
									</xsl:when>
									<xsl:when test="$mode = 'metaOnly'">
										<xsl:apply-templates select="es:ecoSpold/es:dataset" mode="metaOnly"/>
										<xsl:apply-templates select="esi:ecoSpold/esi:dataset" mode="metaOnly"/>
										<xsl:apply-templates select="ese:ecoSpold/ese:dataset" mode="complete"/>
									</xsl:when>
								</xsl:choose>
							</td>
						</tr>
					</table>
				</body>
			</html>
		</xsl:if>
		<xsl:if test="$body='nohtml'">
			<table width="100%">
				<xsl:if test="count(es:ecoSpold/es:dataset) + count(ese:ecoSpold/ese:dataset) + count(esi:ecoSpold/esi:dataset) &gt; 1">
				<tr>
					<td valign="top">
						<a name="toc"/>
						<table width="100%">
							<xsl:choose>
								<xsl:when test="$mode = 'complete'">
									<xsl:choose>
										<xsl:when test="ese:ecoSpold">
											<tr>
												<td class="ttl" width="100%"><xsl:call-template name="emitString"><xsl:with-param name="name">dataset</xsl:with-param></xsl:call-template></td>
											</tr>
										</xsl:when>
										<xsl:otherwise>
											<tr>
												<td class="ttl" width="70%"><xsl:call-template name="emitString"><xsl:with-param name="name">dataset</xsl:with-param></xsl:call-template></td>
												<td class="ttl" width="30%" style="text-align:center;"><xsl:call-template name="emitString"><xsl:with-param name="name">flowData</xsl:with-param></xsl:call-template></td>
											</tr>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:apply-templates select="es:ecoSpold/es:dataset" mode="TOC"/>
									<xsl:apply-templates select="esi:ecoSpold/esi:dataset" mode="TOC"/>
									<xsl:apply-templates select="ese:ecoSpold/ese:dataset" mode="TOC"/>
								</xsl:when>
								<xsl:when test="$mode = 'metaOnly'">
									<tr>
										<td class="ttl"><xsl:call-template name="emitString"><xsl:with-param name="name">dataset</xsl:with-param></xsl:call-template></td>
									</tr>
									<xsl:apply-templates select="es:ecoSpold/es:dataset" mode="TOCMeta"/>
									<xsl:apply-templates select="esi:ecoSpold/esi:dataset" mode="TOCMeta"/>
									<xsl:apply-templates select="ese:ecoSpold/ese:dataset" mode="TOC"/>
								</xsl:when>
							</xsl:choose>
						</table>
					</td>
				</tr>
				</xsl:if>
				<tr>
					<td valign="top">
						<xsl:if test="(count(es:ecoSpold/es:dataset) + count(ese:ecoSpold/ese:dataset) + count(esi:ecoSpold/esi:dataset)) &lt;= 1">
							<a name="toc"/>
						</xsl:if>
						<xsl:choose>
							<xsl:when test="$mode = 'complete'">
								<xsl:apply-templates select="es:ecoSpold/es:dataset" mode="complete"/>
								<xsl:apply-templates select="esi:ecoSpold/esi:dataset" mode="complete"/>
								<xsl:apply-templates select="ese:ecoSpold/ese:dataset" mode="complete"/>
							</xsl:when>
							<xsl:when test="$mode = 'metaOnly'">
								<xsl:apply-templates select="es:ecoSpold/es:dataset" mode="metaOnly"/>
								<xsl:apply-templates select="esi:ecoSpold/esi:dataset" mode="metaOnly"/>
								<xsl:apply-templates select="ese:ecoSpold/ese:dataset" mode="complete"/>
							</xsl:when>
						</xsl:choose>
					</td>
				</tr>
			</table>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
