/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.coeus.common;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author sernadela
 */
public class Worker implements Runnable{
    String name;
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    
    public Worker(String name){
        this.name=name;
    }

    @Override
    public void run() {
        System.out.println("worker run call");
        // Boot.build();
        // Send a message to DC - to let it know that it can start its internal process
        try {
            
            // TODO: inserir caminho no ficheiro de config
            HttpPost request = new HttpPost(Config.getDc_url());
            httpClient.execute(request);
            
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String toString(){
        return name;
    }
    
}
