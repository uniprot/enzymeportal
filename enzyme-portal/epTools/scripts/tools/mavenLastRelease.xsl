<?xml version="1.0" encoding="UTF-8"?>
<!-- Processes a maven-metadata.xml file
    to extract the last released version -->
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="text"/>
    <xsl:template match="/">
        <xsl:apply-templates select="//metadata/versioning/release"/>
    </xsl:template>
    <xsl:template match="release">
        <xsl:value-of select="."/>
    </xsl:template>
</xsl:stylesheet>