<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body>
  <h2>Statistical Data on URL: <xsl:value-of select="StatisticalData/@harvesterUrl"/></h2>
  Number of documents: <xsl:value-of select="StatisticalData/DocumentCount"/><br /><br /><br />
  <em><h3>Graphical overviews:</h3></em>
  <img src="licenses.png" width="300" height="250" /><br /><br /><br />
  <img src="dates.png" width="500" height="500" /><br /><br /><br />
  <img src="formats.png" width="500" height="500" />
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>