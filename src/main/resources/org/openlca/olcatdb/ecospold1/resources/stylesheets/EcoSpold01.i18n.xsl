<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:param name="lang">en</xsl:param>

	<xsl:variable name="i18n" select="document('EcoSpold01.i18n.xml')/elements"/>

	<xsl:template name="emitString">
		<xsl:param name="name"/>
		
		<xsl:if test="$i18n/element[@name = $name]/translation[@xml:lang=$lang]">
			<xsl:value-of select="$i18n/element[@name = $name]/translation[@xml:lang=$lang]"/>
		</xsl:if>
		
		<xsl:if test="not($i18n/element[@name = $name]/translation[@xml:lang=$lang])">i18n error: element <xsl:value-of select="$name"/> unknown!</xsl:if>
	</xsl:template>
</xsl:stylesheet>
