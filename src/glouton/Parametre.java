/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glouton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Omar
 */
public class Parametre {
    final static String FILENAME="parametres.txt";
    String path = ""; 
    String lang ="fr";
    ArrayList<URI> uris = new ArrayList<URI>();
    public static Parametre load()
    {
        Parametre p = new Parametre();
        p.path = (String) System.getProperties().get("user.dir") ;
        try{
			InputStream ips=new FileInputStream(FILENAME); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
                        p.lang = br.readLine();
                        p.path = br.readLine();
			while ((ligne=br.readLine())!=null && ligne.length()>2)
                        {
				p.uris.add(URI.load(ligne));
				
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
        
        return p;
    }
    
    public  void save()
    {
        try {
			FileWriter fw = new FileWriter (FILENAME);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
			Iterator<URI> lis = uris.iterator();
                        String value = "";
                        value += lang + "\n";
                        value += path+ "\n";
                        while(lis.hasNext())
                            value += lis.next().path +"\n";
                        fichierSortie.println (value); 
			fichierSortie.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}		
    }


}
    

