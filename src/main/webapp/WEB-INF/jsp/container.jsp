<html xmlns="http://www.w3.org/1999/xhtml" lang="eng"><!-- InstanceBegin template="/Templates/new_template_no_menus.dwt" codeOutsideHTMLIsLocked="false" -->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="description" content="The European Bioinformatics Institute" />
        <meta name="author" content="EBI Web Team" />
        <meta http-equiv="Content-Language" content="en-GB" />
        <meta http-equiv="Window-target" content="_top" />
        <meta name="no-email-collection" content="http://www.unspam.com/noemailcollection/" />
        <meta name="generator" content="Dreamweaver 8" />
        <title>Enzyme Portal</title>
        <link rel="stylesheet"  href="http://www.ebi.ac.uk/inc/css/contents.css"     type="text/css" />
        <link rel="stylesheet"  href="http://www.ebi.ac.uk/inc/css/userstyles.css"   type="text/css" />
        <script  src="http://www.ebi.ac.uk/inc/js/contents.js" type="text/javascript"></script>
        <link rel="SHORTCUT ICON" href="http://www.ebi.ac.uk/bookmark.ico" />
        <link rel="stylesheet"  href="http://www.ebi.ac.uk/inc/css/sidebars.css"   type="text/css" />
    </head>
    <body onLoad="if(navigator.userAgent.indexOf('MSIE') != -1) {document.getElementById('head').allowTransparency = true;}">
        <div class="headerdiv" id="headerdiv" style="position:absolute; z-index: 1;">
            <iframe src="http://www.ebi.ac.uk/inc/head.html" name="head" id="head" frameborder="0" marginwidth="0px" marginheight="0px" scrolling="no"  width="100%" style="position:absolute; z-index: 1; height: 800px;">[Your user agent does not support frames or is currently configured not to display iframes.</iframe>
        </div>
        <div class="contents" id="contents">
            <div class="breadcrumbs">
                <a href="http://www.ebi.ac.uk/" class="firstbreadcrumb">EBI</a>
                <a href="http://www.ebi.ac.uk/Databases/">Databases</a>
                <a href="http://www.ebi.ac.uk/Databases/">Enzymes</a>
            </div>
            <div style="width: 100%; margin-top: 0px;">
                <jsp:include page='<%= request.getParameter("content")%>' />
            </div>
            <div class="footerdiv" id="footerdiv"  style="z-index:2;">
                <iframe src="http://www.ebi.ac.uk/inc/foot.html" name="foot" frameborder="0" marginwidth="0px" marginheight="0px" scrolling="no"  height="22px" width="100%"  style="z-index:2;">[Your user agent does not support frames or is currently configured not to display iframes.]</iframe>
            </div>
        </div>
    </body>
</html>