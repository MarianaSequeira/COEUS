<%@include file="/layout/taglib.jsp" %>
<div class="row">
    <div class="span2 bs-docs-sidebar pull-left">
        <ul id="nav_api" class="nav nav-list bs-docs-sidenav affix">
            <li><a href="#java">Java</a></li>

            <li><a href="#rest">REST</a></li>

            <li><a href="#sparql">SPARQL</a></li>

            <li><a href="#linkeddata">LinkedData</a></li>

            <li><a href="#javascript">Javascript</a></li>
        </ul>
    </div>

    <div class="span9 pull-right">
        <!-- API/JAVA -->

        <section id="java">
            <div class="page-header">
                <h1>Java</h1>
            </div>

            <h2>API</h2>

            <p>How are your Javadoc reading skills?<br>
                The Java documentation is pretty self-explanatory. For interacting with the knowledge base, use the <a title="Go to COEUS' API Javadoc" href="http://bioinformatics.ua.pt/coeus/javadoc/pt/ua/bioinformatics/coeus/api/API.html" target="_blank">API</a> class to add new triples and perform SPARQL queries directly.</p>

            <p><a class="btn right btn-info" href="../javadoc/" target="_blank" title="Go to COEUS' Javadoc">View Java Documentation</a></p>

            <div class="bs-docs-example">
                API call to retrieve data matching the given triple wildcards.
            </div>
            <pre class="prettyprint lang-java linenums">
/* Invoke getTriple(...); */
pt.ua.bioinformatics.api.API.getTriple(?coeus:uniprot_P51587?, ?p?, ?o?, ?xml?);                                </pre>

            <div class="bs-docs-example">
                API call to perform given SPARQL query, with results in desired format (encapsulated in String).
            </div>
            <pre class="prettyprint lang-java linenums">
/* Invoke select(...); */
pt.ua.bioinformatics.api.API.select("SELECT ...", "js", false);                                </pre>

            <h2>Factories</h2>

            <p>COEUS includes multiple factories to perform quick transformation between URIs, concepts, strings, prefixes, etc. We use these static methods throughout the entire framework, thus facilitating string-based data conversions.</p>

            <h3>Prefix Factory</h3>

            <p>The prefix factory is utility class for Prefix information and transformations. This class stores an internal prefix map, enabling quick composition or decomposition of full object URIs.</p>

            <div class="bs-docs-example">
                Get the URI for a given prefix.
            </div>
            <pre class="prettyprint lang-java linenums">
/* Invoke getURIForPrefix(...); */
pt.ua.bioinformatics.api.PrefixFactory.getURIForPrefix("rdfs");                                </pre>

            <div class="bs-docs-example">
                Encode a full URI into a prefixed string. For example, <span class="label label-info">http://bioinformatics.ua.pt/coeus/resource/Item</span> into <span class="label label-info">coeus:Item</span>. The opposite is also available with the decode() method.
            </div>
            <pre class="prettyprint lang-java linenums">
/* Invoke encode(...); */
pt.ua.bioinformatics.api.PrefixFactory.encode("http://bioinformatics.ua.pt/coeus/resource/Item");                                </pre>

            <h3>Item Factory</h3>

            <p>The item factory is a utility class for Item transformation tasks, such as getting an identifier from the full individual item label.</p>

            <div class="bs-docs-example">
                Retrieves the identifier token from a given item (both full URI or encoded string).
            </div>
            <pre class="prettyprint lang-java linenums">
/* Invoke getTokenFromItem...); */
pt.ua.bioinformatics.api.ItemFactory.getTokenFromItem("http://bioinformatics.ua.pt/coeus/resource/omim_143100");                                </pre>
        </section><!-- API/REST -->

        <section id="rest">
            <div class="page-header">
                <h1>REST</h1>
            </div>

            <p>To access all triples in COEUS Semantic Storage, you can combine subjects, objects or predicates wildcards to iteratively get data. The wildcards' usage is highlighted in the following diagram.</p>

            <ul class="thumbnails">
                <li class="span9"><a href="#" class="thumbnail pull-left"><img src="<c:url value="/assets/img/rest.png" />" alt="REST"></a></li>
            </ul>

            <p></p>

            <p>Some data output examples are:</p>

            <ul>
                <li><a href="../api/triple/coeus:hgnc_brca2/p/o" target="_blank">../api/triple/coeus:hgnc_brca2/p/o</a> gets all predicates (p) and objects (o) related to <strong>omim_114480</strong></li>

                <li><a href="../api/triple/coeus:hgnc_brca2/coeus:isAssociatedTo/obj/csv" target="_blank">../api/triple/coeus:hgnc_brca2/coeus:isAssociatedTo/obj/csv</a> gets all objects (obj) with a <em>coeus:isAssociatedTo</em> relationship to <strong>omim_114480</strong> (in CSV format)</li>
            </ul>

            <p></p>
        </section><!-- API/SPARQL -->

        <section id="sparql">
            <div class="page-header">
                <h1>SPARQL</h1>
            </div>

            <p>All data collected in a COEUS instance can be accessed through a SPARQL endpoint and taking advantage of SPARQL's advanced querying features.</p>

            <p>The endpoint default location is at <span class="label label-info">/sparql</span>.</p>

            <div class="bs-docs-example">
                Query to get specific object data, in this case retrieves all predicates and objects for the individual created from UniProt's P51587 entry.
            </div>
            <pre class="prettyprint lang-sql linenums">
PREFIX coeus: &lt;http://bioinformatics.ua.pt/coeus/resource/&gt;

SELECT ?p ?o {coeus:uniprot_P51587 ?p ?o}                                </pre>

            <h3>SPARQL Federation</h3>

            <p>With the SPARQL endpoint online, querying multiple distributed COEUS instances is a straightforward process. Moreover, additional knowledge bases with public SPARQL endpoints can also be put into the mix, providing an holistic perspective over a distributed knowledge network.</p>

            <div class="bs-docs-example">
                Query to retrieve data from multiple COEUS seeds and external knowledge bases.
            </div>
            <pre class="prettyprint lang-sql linenums">
PREFIX coeus: &lt;http://bioinformatics.ua.pt/coeus/resource/&gt;
PREFIX dc: &lt;http://purl.org/dc/elements/1.1/&gt;
PREFIX diseasome: &lt;http://www4.wiwiss.fu-berlin.de/diseasome/resource/diseasome/&gt;
PREFIX rdfs: &lt;http://www.w3.org/2000/01/rdf-schema#&gt;
PREFIX coeus: &lt;http://bioinformatics.ua.pt/coeus/&gt;

SELECT ?pdb ?mesh
WHERE{
    {
        SERVICE &lt;http://www4.wiwiss.fu-berlin.de/diseasome/sparql&gt;
        {
                &lt;http://www4.wiwiss.fu-berlin.de/diseasome/resource/genes/BRCA2&gt; rdfs:label ?label
        }
    }
    {
        SERVICE &lt;http://bioinformatics.ua.pt/coeus/sparql&gt;
        {
                _:gene dc:title ?label.
                _:gene coeus:isAssociatedTo ?uniprot
        }
    }
    {
        SERVICE &lt;http://bioinformatics.ua.pt/coeus/sparql&gt;
        {
                ?uniprot coeus:isAssociatedTo ?pdb.
                ?pdb coeus:hasConcept coeus:concept_PDB
        }
    }
}
}                                </pre>

            <p><a class="btn right btn-info" href="../sparqler/">Test it here</a></p>
        </section><!-- API/LINKEDDATA -->

        <section id="linkeddata">
            <div class="page-header">
                <h1>LinkedData</h1>
            </div>

            <p>COEUS publishes all data through LinkedData patterns &amp; guidelines by default. With <a href="http://www4.wiwiss.fu-berlin.de/pubby/" target="_blank">pubby</a> included in all COEUS seeds, data is easily available to external appications.<br>
                Using the <em>../resource/*</em> pattern, you can access object data in the web or RDF browsers.</p>

            <ul>
                <li><a href="../resource/uniprot_P51687">../resource/uniprot_P51687</a> HTML page for UniProt P51587</li>

                <li><a href="../data/entrezgene_5002?output=xml">../data/entrezgene_5002?output=xml</a> RDF content for EntrezGene 5002</li>
            </ul>
        </section><!-- API/JAVASCRIPT -->

        <section id="javascript">
            <div class="page-header">
                <h1>Javascript</h1>
            </div>

            <p>Easily perform SPARQL queries to your COEUS-generated endpoint with this new library.<br>
                Check the documentation and the library at <span class="label label-info">../assets/js/coeus.sparql.js</span></p>
        </section>
    </div>
</div>