<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Enzyme Portal</title>
        <link rel="stylesheet"  href="http://www.ebi.ac.uk/inc/css/contents.css"     type="text/css" />
        <link media="screen" href="resources/lib/spineconcept/css/960gs-fluid/grid.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/identification.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />
        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="resources/lib/spineconcept/javascript/identification.js" type="text/javascript"></script>
        <link href="resources/css/search.css" type="text/css" rel="stylesheet" />
        <script src="resources/javascript/search.js" type="text/javascript"></script>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <div class="contents">
            <div class="page container_12">
                <jsp:include page="subHeader.jsp"/>
            </div>
            <div class="grid_12 content">

	<h2>About the Enzyme Portal</h2>
	<p>
		The Enzyme Portal is for people who are interested in
		finding out about the biology of enzymes and proteins with enzymatic
		activity.
	</p>

	<p>
		The Enzyme Portal integrates publicly available
		information about enzymes, such as small-molecule chemistry,
		biochemical pathways and drug compounds. It provides a concise
		summary of information from:
		<ul>
			<li><a href="http://www.uniprot.org/help/uniprotkb">UniProt
						knowledge base</a></li>
			<li><a href="http://www.pdbe.org">Protein Data Bank in
						Europe</a></li>
			<li>Rhea, a database
					of enzyme-catalyzed reactions</li>
			<li><a href="http://www.reactome.org">Reactome</a>, a database
					of biochemical pathways</li>
			<li>IntEnz, a resource with enzyme nomenclature information</li>
			<li><a href="http://www.ebi.ac.uk/chebi">ChEBI</a> and
					<a href="https://www.ebi.ac.uk/chembl/">ChEMBL</a>,
					which contain information about small molecule chemistry and
					bioactivity</li>
			<li><a href="http://www.ebi.ac.uk/thornton-srv/databases/CoFactor/">Cofactor</a>
					and <a href="http://www.ebi.ac.uk/thornton-srv/databases/MACiE/">Macie</a>
					for highly detailed, curated information about cofactors and reaction
					mechanisms.</li>
		</ul>
		The Enzyme Portal brings together lots of diverse
		information about enzymes and displays it in an organised overview.
		It covers a large number of species including the key model
		organisms, and provides a simple way to compare orthologues.
	</p>
	<p>
		Give it a try and 
		<a href="http://www.ebi.ac.uk/support/index.php?query=Enzyme+portal&referrer=http://www.ebi.ac.uk/enzymeportal/">let
		us know</a> what you think!
	</p>

	<h2>The Enzyme Portal Team</h2>
	<p>
		The Enzyme Portal is designed and developed at the
			EMBL-European Bioinformatics Institute (<a
			href="http://www.ebi.ac.uk/information">EMBL-EBI</a>)
			in the UK. Part of the <a
			href="http://www.embl.org">European Molecular Biology Laboratory</a>,
			EMBL-EBI is the hub of excellence for bioinformatics in Europe. We
			provide freely available life science data and <a
			href="http://www.ebi.ac.uk">services</a>, and
			perform basic <a href="http://www.ebi.ac.uk/research">research</a>
			in computational biology.
	</p>

	<p>
		The Enzyme Portal was created by the <a
		href="http://www.ebi.ac.uk/steinbeck/">Cheminformatics and Metabolism
		Team</a> at EMBL-EBI, which is led by
		<a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=922">Christoph
		Steinbeck</a>. Some of the key contributors are:
		<ul>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=265">Paula
			de Matos</a>, Project Coordinator and User Experience
			Analyst</li>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=690">Rafael
			Alcántara</a>, Hong Cao and
			<a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=1518">Joseph
			Onwubiko</a>, Software Developers</li>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=1195">Jenny
			Cham</a>, User Experience Consultant</li>
		</ul>
	</p>

	<p>
		We also have the invaluable support of 
	<ul>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=467">Jules
			Jacobsen</a> from the UniProt Team</li>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=290">Bijay
			Jassal</a>&nbsp;from the Reactome Team</li>
		<li>Gemma Holiday and Julia Fischer, previously from the Thornton Team</li>
		<li>Syed Asad Rahman from the Thornton Team</li>
		<li>Mickael Goujon and Francis Rowland from the External Services Team</li>
		<li>James Malone and Helen Parkinson</li>
	</ul>
	</p>
	
	
	
            </div>
            
            <jsp:include page="footer.jsp"/>
        </div>
    </body>
</html>