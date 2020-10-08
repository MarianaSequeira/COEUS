/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.coeus.api.plugins;

import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.api.API;
import pt.ua.bioinformatics.coeus.api.PrefixFactory;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.coeus.data.Predicate;
import pt.ua.bioinformatics.coeus.domain.Resource;
import pt.ua.bioinformatics.diseasecard.newdomain.HGNC;

/**
 *
 * @author mfs98
 */
public class HGNCPlugin {
    
    private final List<HGNC> hgncs;
    private final HashMap<String, com.hp.hpl.jena.rdf.model.Resource> hgncRes;
    private final HashMap<String, com.hp.hpl.jena.rdf.model.Resource> uniprotRes;
//    private final HashMap<String, String> omims;
    private final Resource res;
    private final API api;

    
    public HGNCPlugin(Resource res) 
    {
        this.hgncs = new ArrayList<>();
        this.hgncRes = new HashMap<>();
        this.uniprotRes = new HashMap<>();
//        this.omims = omims;
        this.res = res;
        this.api = Boot.getAPI();
    }
    
    
    public void itemize()
    {
        if(this.load())
        {
            this.triplifyInformation();
        }
    }
    
    
    private boolean load()
    {
        boolean sucess = false;
        File file = new File("/home/mariana/Documents/BOLSA/Extra/hgnc_data");
        BufferedReader in;
        try 
        {
            in = new BufferedReader(new FileReader(file));
            CSVReader reader = new CSVReader(in, '\t');
            
            /*
            HGNC ID | Approved symbol | Chromosome | RefSeq IDs	| Enzyme IDs | NCBI Gene ID | Ensembl gene ID | OMIM ID	| UniProt ID | Pubmed IDs
            */
            
            List<String[]> rows = reader.readAll();
            
            // Removing rows that doesn't contains OMIM ID
            // rows.removeIf( s -> s[7].equals(""));
            
            List<String> oIDs;
            String[] info;
            
            for (int i=1; i < rows.size(); i++) 
            {
                info = rows.get(i);
                oIDs = Arrays.asList(info[7].split(", "));
                
                HGNC hgnc = new HGNC(info[0]);
                hgnc.setApprovedSymbol(info[1]);
                hgnc.setChromosomes(info[2]);
                hgnc.setRefseqID(info[3]);
                hgnc.setEnsembl(info[4]);
                hgnc.setEnzymeID(info[5]);
                hgnc.setNcbi(info[6]);
                hgnc.setOmims(oIDs);
                hgnc.setUniprot(info[8]);
                hgnc.setPubmedIDs(Arrays.asList(info[9].split(", ")));

                this.hgncs.add(hgnc);
            }
            sucess = true;
        } 
        catch (IOException ex) 
        {
            if (Config.isDebug()) 
            {
                System.out.println("[COEUS][HGNC] Unable to load data from HGNC");
                Logger.getLogger(OMIMPlugin.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        System.out.println("\tLoading process finished.");
        return sucess;
    }
    
   
    
    /*
        For each OMIM in DB creates all the associations with HGNC, Uniprot, etc.
    */
    private void triplifyInformation()
    {
        try
        {
<<<<<<< HEAD
            for (Map.Entry<String, String[]> entry : OMIMPlugin.getOMIMs().entrySet()) {
                String omimID = entry.getKey();
                String geneNames = entry.getValue()[0];
                String chromosomalLocation = entry.getValue()[1];
=======
            for (Map.Entry<String, String> entry : OMIMPlugin.getOMIMs().entrySet()) {
                String omimID = entry.getKey();
                String chromosomalLocation = entry.getValue();
>>>>>>> f60e615c4bb997073019a6527f72a6c05a2ca036

                com.hp.hpl.jena.rdf.model.Resource omim_item = api.getResource(PrefixFactory.getURIForPrefix(Config.getKeyPrefix()) + "omim_" + omimID);

                List<HGNC> relatedHGNC = this.findHGNCByOMIM(omimID);

                if (relatedHGNC.isEmpty())
                {
<<<<<<< HEAD
                    relatedHGNC = this.findHGNCByApprovedSymbol(geneNames);
                }
                if (relatedHGNC.isEmpty())
                {
=======
>>>>>>> f60e615c4bb997073019a6527f72a6c05a2ca036
                    relatedHGNC = this.findHGNCByChromosomalLocation(chromosomalLocation);
                }

                for(HGNC hgnc : relatedHGNC)
                {
                    com.hp.hpl.jena.rdf.model.Resource hgnci = null; 
                    com.hp.hpl.jena.rdf.model.Resource uniproti = null;

                    if (!hgnc.getLoaded())
                    {
<<<<<<< HEAD
                        hgnci = this.itemizeResource(hgnc.getHgncID().split(":")[1], hgnc.getApprovedSymbol(), "hgnc_", "concept_HGNC");

                        // Itemize the uniprot value and all the resources that extens uniprof (Enzyme, Ensembl, etc.)
=======
                        hgnci = this.itemizeResource(hgnc.getHgncID(), hgnc.getApprovedSymbol(), "hgnc_", "concept_HGNC");

>>>>>>> f60e615c4bb997073019a6527f72a6c05a2ca036
                        if(!hgnc.getUniprot().equals("")) 
                        {
                            uniproti = this.itemizeResource(hgnc.getUniprot(), hgnc.getUniprot(), "uniprot_", "concept_UniProt" );
                            this.uniprotRes.put(hgnc.getUniprot(), uniproti);
<<<<<<< HEAD
                            
                            this.itemizeUniprotExtensions(hgnc, uniproti);
=======
>>>>>>> f60e615c4bb997073019a6527f72a6c05a2ca036
                        }

                        // Avoids itemize the same resource, over and over again.
                        hgnc.setLoaded(true);
                        this.hgncRes.put(hgnc.getHgncID(), hgnci);
                    }
                    else
                    {
                        hgnci = this.hgncRes.get(hgnc.getHgncID());
                        if(!hgnc.getUniprot().equals("")) uniproti = this.uniprotRes.get(hgnc.getUniprot());
                    }


                    /*
                        Begins process of association
                    */
                    this.associateItems(hgnci, omim_item);
                    if(!hgnc.getUniprot().equals(""))  this.associateItems(uniproti, omim_item);
                }
            }
        }
        catch(Exception ex)
        {
            if (Config.isDebug()) 
            {
                System.out.println("[COEUS][OMIM] Unable to triplify gene information");
                Logger.getLogger(OMIMPlugin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
   
    /*
        Itemize items that have associations between them
    */
    private com.hp.hpl.jena.rdf.model.Resource itemizeResource(String ID, String name, String o, String concept)
    {
        com.hp.hpl.jena.rdf.model.Resource item = null;
        try 
        {
            item = api.createResource(PrefixFactory.getURIForPrefix(Config.getKeyPrefix()) + o + ID);
            com.hp.hpl.jena.rdf.model.Resource obj = api.createResource(PrefixFactory.getURIForPrefix(Config.getKeyPrefix()) + "Item");
            
            api.addStatement(item, Predicate.get("rdf:type"), obj);

            api.addStatement(item, Predicate.get("rdfs:label"), o + name);
            api.addStatement(item, Predicate.get("dc:title"), name.toUpperCase());
        
            com.hp.hpl.jena.rdf.model.Resource con = api.getResource(PrefixFactory.getURIForPrefix(Config.getKeyPrefix()) + concept);
            
            api.addStatement(item, Predicate.get("coeus:hasConcept"), con);
            api.addStatement(con, Predicate.get("coeus:isConceptOf"), item);
            
        } 
        catch (Exception ex) 
        {
            if (Config.isDebug()) 
            {
                System.out.println("[COEUS][HGNC] Unable to triplify gene information");
                Logger.getLogger(OMIMPlugin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
    }
    
    
    /*
        Allows the association between items.
    */
    private void associateItems(com.hp.hpl.jena.rdf.model.Resource item, com.hp.hpl.jena.rdf.model.Resource parent)
    {
        try {
            api.addStatement(item, Predicate.get("coeus:isAssociatedTo"), parent);
            api.addStatement(parent, Predicate.get("coeus:isAssociatedTo"), item);
        } catch (Exception ex) {
            Logger.getLogger(HGNCPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    /*
<<<<<<< HEAD
        Itemize the resources related with uniprot and stablish all existing associations.
    */
    private void itemizeUniprotExtensions(HGNC hgnc, com.hp.hpl.jena.rdf.model.Resource uniprot)
    {
        if (!hgnc.getEnzymeID().equals(""))
        {
            com.hp.hpl.jena.rdf.model.Resource enzymei = this.itemizeResource(hgnc.getEnzymeID(), hgnc.getEnzymeID(), "enzyme_", "concept_ENZYME" );
            this.associateItems(enzymei, uniprot);
        }
        if (!hgnc.getEnsembl().equals(""))
        {
            com.hp.hpl.jena.rdf.model.Resource ensembli = this.itemizeResource(hgnc.getEnsembl(), hgnc.getEnsembl(), "ensembl_", "concept_Ensembl" );
            this.associateItems(ensembli, uniprot);
        }
    }
    
    
    /*
=======
>>>>>>> f60e615c4bb997073019a6527f72a6c05a2ca036
        Returns list of all HGNC objects that contains the OMIM ID specified.
    */
    private List<HGNC> findHGNCByOMIM(String omimID)
    {
        List<HGNC> result = new ArrayList<>();
        
        for(HGNC hgnc : this.hgncs)
        {
            if(hgnc.getOmims().contains(omimID))
            {
                result.add(hgnc);
            }
        }
        return result;
    }
    
    /*
        Returns list of all HGNC objects that contains a gene with the ChromosomalLocation specified.
    */
<<<<<<< HEAD
    private List<HGNC> findHGNCByApprovedSymbol(String geneNames)
    {
        List<HGNC> result = new ArrayList<>();
        
        for(HGNC hgnc : this.hgncs)
        {
            for (String gene : geneNames.replace("[","").replace("]","").split(", "))
            {
                if(hgnc.getApprovedSymbol().equals(gene)) result.add(hgnc);
                break;
            }
        }
        return result;
    }
    
=======
>>>>>>> f60e615c4bb997073019a6527f72a6c05a2ca036
    private List<HGNC> findHGNCByChromosomalLocation(String chromosomalLocation)
    {
        List<HGNC> result = new ArrayList<>();
        
        for(HGNC hgnc : this.hgncs)
        {
<<<<<<< HEAD
            if(hgnc.getChromosomes().equals(chromosomalLocation))
=======
            if(hgnc.getChromosomes().contains(chromosomalLocation))
>>>>>>> f60e615c4bb997073019a6527f72a6c05a2ca036
            {
                result.add(hgnc);
            }
        }
        return result;
    }
}
