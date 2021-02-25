{
"config": {
        "name": "Diseasecard",
        "description": "Diseasecard: rare genetic diseases research portal",
        "keyprefix":"diseasecard",
        "version": "4.5",
        "ontology": "https://bioinformatics.ua.pt/coeus/ontology/",
        "setup": "setup_valid.rdf",
        "sdb":"sdb.ttl",
        "predicates":"predicates.csv",
        "built": false,
        "debug": true,
        "apikey":"coeus|uavr",
        "environment":"default",
        "wizard":true,
        "dc_url":"http://containerbackend:8080/diseasecard/startup",
        "sourceFilesLocation":"/usr/local/tomcat/datasets"
},
"prefixes" : {
        "coeus": "http://bioinformatics.ua.pt/coeus/resource/",
        "diseasecard": "http://bioinformatics.ua.pt/diseasecard/resource/",
        "owl2xml":"http://www.w3.org/2006/12/owl2-xml#",
        "xsd": "http://www.w3.org/2001/XMLSchema#",
        "rdfs": "http://www.w3.org/2000/01/rdf-schema#",
        "owl": "http://www.w3.org/2002/07/owl#",
        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
        "dc": "http://purl.org/dc/elements/1.1/",
        "np": "http://www.nanopub.org/nschema#",
        "prov": "http://www.w3.org/ns/prov", 
        "dcterms": "http://purl.org/dc/terms/",
        "d2r":"http://sites.wiwiss.fu-berlin.de/suhl/bizer/d2r-server/config.rdf#"
    }
}