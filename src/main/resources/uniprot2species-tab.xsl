<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:up="http://uniprot.org/uniprot">
	<xsl:output method="text"/>
	<xsl:template match="/">
		<xsl:apply-templates select="//up:uniprot/up:entry"/>
	</xsl:template>
	<xsl:template match="up:entry">
		<xsl:for-each select="./up:accession">
			<xsl:apply-templates/>
			<xsl:if test="not(position() = last())">,</xsl:if>
		</xsl:for-each>
		<xsl:text>&#x9;</xsl:text>
		<xsl:for-each select="./up:name">
			<xsl:apply-templates/>
			<xsl:if test="not(position() = last())">,</xsl:if>
		</xsl:for-each>
		<xsl:text>&#x9;</xsl:text>
		<xsl:value-of select="./up:organism/up:name[@type='scientific']"/>
		<xsl:text>&#xA;</xsl:text>
	</xsl:template>
</xsl:stylesheet>
