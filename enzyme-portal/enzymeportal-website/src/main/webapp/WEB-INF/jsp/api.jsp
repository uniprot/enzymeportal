<%-- 
    Document   : api
    Created on : 21-Sep-2020, 11:47:11
    Author     : joseph
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="epfn" uri="/WEB-INF/epTagLibray.tld" %>

<!DOCTYPE html>
<html>
       <c:set var="pageTitle" value="API"/>
    <head>
        <meta charset="utf-8">
        <title>${pageTitle} &lt; Enzyme Portal &lt; EMBL-EBI</title>

        <meta name="description" content="EMBL-EBI"><!-- Describe what this page is about -->
        <meta name="HandheldFriendly" content="true" />
        <meta name="MobileOptimized" content="width" />
        <meta name="theme-color" content="#70BDBD"> <!-- Android Chrome mobile browser tab color -->
        <meta name="description" content="EMBL-EBI Enzyme Portal">
        <meta name="keywords" content="enzyme database, bioinformatics, europe, institute">
        <meta name="author" content="EMBL-EBI, Enzyme Portal Team">
        <meta name="viewport" content="width=device-width,initial-scale=1">
        <meta name="google-site-verification" content="b4iTMZj90Y1Kw06d48P6nU9PC-NCHebICZsox09NHPM" /> <!-- for google webtool verification (sitemap) --> 


        <!-- If you have custom icon, replace these as appropriate.
              You can generate them at realfavicongenerator.net -->
        <link rel="icon" type="image/x-icon" href="https://www.ebi.ac.uk/web_guidelines/EBI-Framework/v1.1/images/logos/EMBL-EBI/favicons/favicon.ico" />
        <link rel="icon" type="image/png" href="https://www.ebi.ac.uk/web_guidelines/EBI-Framework/v1.1/images/logos/EMBL-EBI/favicons/favicon-32x32.png" />
        <link rel="icon" type="image/png" sizes="192×192" href="https://www.ebi.ac.uk/web_guidelines/EBI-Framework/v1.1/images/logos/EMBL-EBI/favicons/android-chrome-192x192.png" /> <!-- Android (192px) -->
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="https://www.ebi.ac.uk/web_guidelines/EBI-Framework/v1.1/images/logos/EMBL-EBI/favicons/apple-icon-114x114.png"> <!-- For iPhone 4 Retina display (114px) -->
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="https://www.ebi.ac.uk/web_guidelines/EBI-Framework/v1.1/images/logos/EMBL-EBI/favicons/apple-icon-72x72.png"> <!-- For iPad (72px) -->
        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="https://www.ebi.ac.uk/web_guidelines/EBI-Framework/v1.1/images/logos/EMBL-EBI/favicons/apple-icon-144x144.png"> <!-- For iPad retinat (144px) -->
        <link rel="apple-touch-icon-precomposed" href="https://www.ebi.ac.uk/web_guidelines/EBI-Framework/v1.1/images/logos/EMBL-EBI/favicons/apple-icon-57x57.png"> <!-- For iPhone (57px) -->
        <link rel="mask-icon" href="https://www.ebi.ac.uk/web_guidelines/EBI-Framework/v1.1/images/logos/EMBL-EBI/favicons/safari-pinned-tab.svg" color="#ffffff"> <!-- Safari icon for pinned tab -->
        <meta name="msapplication-TileColor" content="#2b5797"> <!-- MS Icons -->
        <meta name="msapplication-TileImage" content="https://www.ebi.ac.uk/web_guidelines/EBI-Framework/v1.1/images/logos/EMBL-EBI/favicons/mstile-144x144.png">

        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/api/swagger-ui.css" >

        <!-- START New CSS for Foundation -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/foundation/6.2.4/foundation.min.css" type="text/css" media="all">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/motion-ui/1.1.1/motion-ui.min.css" />
        <link rel="stylesheet" href="https://www.ebi.ac.uk/web_guidelines/EBI-Framework/v1.1/css/ebi-global.css" type="text/css" media="all">
        <link rel="stylesheet" href="https://www.ebi.ac.uk/web_guidelines/EBI-Icon-fonts/v1.1/fonts.css" type="text/css" media="all">
        <!-- END New CSS for Foundation -->

        <link rel="stylesheet" href="https://www.ebi.ac.uk/web_guidelines/EBI-Framework/v1.1/css/theme-embl-petrol.css" type="text/css" media="all">



        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/search.css" type="text/css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/enzyme-portal-colours.css" type="text/css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/skins/default/skin2.css" type="text/css" />

        <!--    <link rel="stylesheet" href="https://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />-->
        <style type="text/css">
            /* You have the option of setting a maximum width for your page, and making sure everything is centered */
            /* body { max-width: 1600px; margin: 0 auto; } */
        </style>


        <script src="https://www.ebi.ac.uk/web_guidelines/EBI-Framework/v1.1/libraries/modernizr/modernizr.custom.49274.js"></script>

        <!-- This is the global include file for jQuery -->
        <script src="//code.jquery.com/jquery-1.11.2.min.js"></script>

        <script src="${pageContext.request.contextPath}/resources/javascript/jquery.easy-autocomplete.min.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/easy-autocomplete.min.css" type="text/css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/easy-autocomplete.themes.min.css" type="text/css" />


        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/enzyme.css" type="text/css" />


        <style>
            html
            {
                box-sizing: border-box;
                overflow: -moz-scrollbars-vertical;
                overflow-y: scroll;
            }

            *,
            *:before,
            *:after
            {
                box-sizing: inherit;
            }

            body
            {
                margin:0;
                background: #fafafa;
            }
            code, kbd {
                font-family: Consolas,Liberation Mono,Courier,monospace;
                color: #fefefe; 
                background-color: rgba(0,0,0,0.075);
                font-weight: 700;
                border: 0px #cacaca;
            }
            .topbar{
                display: none;
            }
            .swagger-ui section.models {
                margin: 30px 0;
                border: 1px solid rgba(59,65,81,.3);
                border-radius: 4px;
                display: none;
            }
            .swagger-ui .opblock.opblock-get .opblock-summary-method {
                background: #408c8c;
            }
            .swagger-ui .opblock.opblock-get .tab-header .tab-item.active h4 span:after {
                background: #207a7a;
            }
            .info{
                display: none;  
            }
            .scheme-container{
                display: none;    
            }
        </style>

    </head>
    <body class="level2">

        <%@include file="skipto.jspf" %>

        <div id="wrapper">

            <%@include file="header.jspf" %>

            <div id="content" role="main" class="clearfix">


                <section>
                    <p>The Enzyme Portal REST API can be used to programmatically retrieve enzymes and related biological data.</p>
                    <p>All API access is over HTTPS and accessed from <strong class="microlight" style="background: #cacaca"> https://www.ebi.ac.uk/enzymeportal/rest</strong>  All data is sent and received as JSON.</p>
                    <p>All resources may have one or more <strong class="microlight">*_links</strong> properties linking to other resources. These links are meant to provide explicit URLs so that API clients don't need to construct URLs on their own while traversing linked resources.</p>
                    <span>The Enzyme Portal API provides access to a variety of different enzymes resources including the following:</span>
                    <ul>

                        <li>Reactions</li>
                        <li>Pathways</li>
                        <li>Protein function</li>
                        <li>Diseases</li>
                        <li>Metabolites</li>
                        <li>Cofactors</li>

                    </ul>
                </section>
                <p>If you have any problems, questions or requests, please contact <a href="mailto:enzyme-portal-project@ebi.ac.uk">Enzyme Portal Support.</a> </p>
                <h3>Available Endpoints </h3> <hr/>
                <div id="swagger-ui"></div>


            </div>

            <%@include file="footer.jspf" %>

            <script src="${pageContext.request.contextPath}/resources/api/swagger-ui-bundle.js" charset="UTF-8"></script>
            <script src="${pageContext.request.contextPath}/resources/api/swagger-ui-standalone-preset.js" charset="UTF-8"></script>
            <script>
                window.onload = function () {
                    // Begin Swagger UI call region
                    const ui = SwaggerUIBundle({
                        url: "${apiUrl}/v3/api-docs",
                        dom_id: '#swagger-ui',
                        deepLinking: true,
                        presets: [
                            SwaggerUIBundle.presets.apis,
                            SwaggerUIStandalonePreset
                        ],
                        plugins: [
                            SwaggerUIBundle.plugins.DownloadUrl
                        ],
                        layout: "StandaloneLayout"
                    });
                    // End Swagger UI call region

                    window.ui = ui;
                };
            </script>

        </div>
    </body>
</html>

