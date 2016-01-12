<?xml version="1.0" encoding="UTF-8"?>
<!-- Processes a maven pom.xml file to extract groupId and artifactId -->
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="text"/>
    <xsl:template match="/">
        <xsl:apply-templates select="*[local-name() = 'project']"/>
    </xsl:template>
    <xsl:template match="*[local-name() = 'project']">
        <xsl:value-of select="*[local-name() = 'groupId']"/>
        <xsl:text>:</xsl:text>
        <xsl:value-of select="*[local-name() = 'artifactId']"/>
    </xsl:template>
</xsl:stylesheet>