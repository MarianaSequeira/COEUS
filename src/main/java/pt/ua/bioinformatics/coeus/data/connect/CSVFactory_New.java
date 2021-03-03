/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.coeus.data.connect;

import com.hp.hpl.jena.rdf.model.Statement;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pt.ua.bioinformatics.coeus.api.API;
import pt.ua.bioinformatics.coeus.api.ItemFactory;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.coeus.data.Predicate;
import pt.ua.bioinformatics.coeus.data.Triplify;
import pt.ua.bioinformatics.coeus.domain.InheritedResource;
import pt.ua.bioinformatics.coeus.domain.Resource;

/**
 *
 * @author mariana
 */
public class CSVFactory_New implements ResourceFactory { 
    
    private boolean hasError = false;
    private Triplify rdfizer = null;
    private Resource res;

    public CSVFactory_New(Resource res) {
        this.res = res;
    }
    
    private List<String[]> readEndpoint() throws IOException {
        
        CsvParserSettings settings = new CsvParserSettings();
        settings.detectFormatAutomatically();
        CsvParser parser = new CsvParser(settings);
        try {
            // Read Local File
            if (this.res.getEndpoint().contains("sourceFilesLocation"))
            {
                return parser.parseAll(new File("/usr/local/tomcat/datasets/malacards_diseasecards_dump.csv"));
            }
            // Read Online File
            else 
            {
                URL u = new URL(res.getEndpoint());
                return parser.parseAll(new InputStreamReader(u.openStream()));
            }
        } catch (MalformedURLException ex) {
            saveError(ex);
            Logger.getLogger(CSVFactory_New.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("[COEUS][CSVFactory] Impossible to read the file.");
        }
        return null;
    }
    
    
    
    @Override
    public void read() {
        
        try {
            List<String[]> rows = readEndpoint();
            
            HashMap<String, String> extensions;
            if (res.getExtension().equals("")) extensions = res.getExtended();
            else                               extensions = res.getExtended(res.getExtension());
            
            InheritedResource resKey = (InheritedResource) res.getHasKey();
            //System.out.println(resKey.toString());
            
            for ( String itemRaw : extensions.keySet() )
            {
                String item = ItemFactory.getTokenFromItem(itemRaw);
                //System.out.println("ItemRaw: " + itemRaw + "| Item: " + item);
                
                for (String[] r : rows) 
                {
                    //System.out.println(Arrays.toString(r));
                    //System.out.println(resKey.getRegex());
                    
                    if ( r[Integer.parseInt(resKey.getRegex())].equals(item) ) 
                    {
                        //System.out.println("EXTENSIONS.GET(itemRaw) = " + extensions.get(itemRaw));
                        rdfizer = new Triplify(res, extensions.get(itemRaw));
                        
                        //System.out.println("RES.GETLOADSFROM() = " + res.getLoadsFrom().toString());
                        
                        for (Object o : res.getLoadsFrom()) {
                            InheritedResource c = (InheritedResource) o;
                            String[] tmp = c.getProperty().split("\\|");
                            for (String inside : tmp) {
                                int col = Integer.parseInt(c.getQuery());
                                //System.out.println("INSIDE: " + inside + " | R(COL): " + r[col]);
                                rdfizer.add(inside, r[col]);
                            }
                        }
                        rdfizer.itemize(r[Integer.parseInt(resKey.getQuery())]);
                        
                        break;
                    }
                    
                }
            }
            
            
            
        } catch (IOException ex) {
            saveError(ex);
            Logger.getLogger(CSVFactory_New.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("[COEUS][CSVFactory] Impossible to read the information.");
        }
        
        
    }

    
    
    
    @Override
    public boolean save() {
        boolean success = false;
        try {
            //only change built property if there are no errors
            if (hasError == false) {
                API api = Boot.getAPI();
                com.hp.hpl.jena.rdf.model.Resource resource = api.getResource(this.res.getUri());
                Statement statementToRemove = api.getModel().createLiteralStatement(resource, Predicate.get("coeus:built"), false);
                api.removeStatement(statementToRemove);
                api.addStatement(resource, Predicate.get("coeus:built"), true);
            }
            success = true;
            if (Config.isDebug()) { 
                System.out.println("[COEUS][API] Saved resource " + res.getUri());
            }
        } catch (Exception ex) {
            if (Config.isDebug()) { saveError(ex);
                System.out.println("[COEUS][API] Unable to save resource " + res.getUri());
                Logger.getLogger(CSVFactory_New.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return success;
    }
    
    // TODO: Validar comportamento
    private void saveError(Exception ex) {
        try {
            API api = Boot.getAPI();
            com.hp.hpl.jena.rdf.model.Resource resource = api.getResource(this.res.getUri());
            Statement statement=api.getModel().createLiteralStatement(resource, Predicate.get("dc:coverage"), "ERROR: "+ex.getMessage()+". For more information, please see the application server log.");
            api.addStatement(statement);
            hasError = true;

            if (Config.isDebug()) { 
                System.out.println("[COEUS][API] Saved error on resource " + res.getUri());
            }
        } catch (Exception e) {
            if (Config.isDebug()) { 
                System.out.println("[COEUS][API] Unable to save error on resource " + res.getUri());
                Logger.getLogger(XMLFactory.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
}
