/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glouton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Omar
 */
public class URI extends Observable {
    String url;
    String path;
    String base;
    Date date;
    ArrayList<String> js = new ArrayList<>();
    ArrayList<String> css = new ArrayList<>();
    ArrayList<String> links = new ArrayList<>();
    ArrayList<String> imgs = new ArrayList<>();
    ArrayList<String> videos = new ArrayList<>();
    public  URL verifyUrl(String url) { 
        if (!(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"))) 
            return null;
        URL verifiedUrl = null; 
    try { 
        verifiedUrl = new URL(url); 
    } catch (MalformedURLException e) { 
        System.out.println(e.toString());
        return null; 
    }
return verifiedUrl;
}
 public  boolean download(String urlstr, String path,boolean imageetvideo,boolean recursive) throws IOException
{
        URL url =  verifyUrl(urlstr);
        
        if(url != null)
        {
         
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
         if(code == 200 || code == 301 || code == 302)
         {
          String data =  getStringPage(url);
          
          String clone;
          clone = (String) urlstr.replace("/","\\");
          clone = clone.replace("http:\\\\","");
          clone = clone.replace("https:\\\\","");
          clone = path + clone;
          System.out.println(clone);
          String [] rep;
            rep = clone.split("\\\\");
          String pa = "";
          String fin;
          for(int i = 0 ; i < (rep.length - 1); i++)
          {
             
              pa = pa + rep[i]+"\\";
          }
          System.out.println(urlstr.replace("http://","").replace("https://",""));
          if (rep[rep.length - 1].compareTo(urlstr.replace("http://","").replace("https://","").replace("/", "")) == 0)
          {
              pa += rep[rep.length - 1]+"\\";
              fin = "index.html";
          }
          else
              fin = rep[rep.length - 1];
          if(fin.indexOf(".html") ==-1)
              fin +=".html";
            this.url = urlstr;
            this.path = pa;
            this.base = pa+fin;
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            date = Calendar.getInstance().getTime();
             writeRep(pa,fin,data);
            js =downloadLinksInPage( url,path, data,"script", "src");
            links = downloadLinksInPage( url,path, data,"link", "href");
            if(imageetvideo)
            {
                imgs = downloadLinksInPage( url,path, data,"img", "src");
                videos = downloadLinksInPage( url, path,data,"video", "src");
            }
            if(recursive)
            {
               links = downloadLinksInPage( url,path, data,"a", "href");
            }
            save();
            return true;
         }
        }
        
        return false;
}
    
    public  String getStringPage(URL url) throws IOException
    {
        
        
         URLConnection urlConnection = url.openConnection();
         System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
         HttpURLConnection connection = null;
         if(urlConnection instanceof HttpURLConnection) {
            connection = (HttpURLConnection) urlConnection;
         }else {
            System.out.println("Please enter an HTTP URL.");
            return null;
         }
         System.out.print(url.toString());
         BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         String page = "";
         String current;
         
         while((current = in.readLine()) != null) {
           page += current+"\n";
          
         }
        return page;
    }
 public  ArrayList<String> downloadLinksInPage( URL pageUrl,String strpath, String pageContents,String prem, String sec) throws IOException{ 



// Compile link matching pattern. 
Pattern p = Pattern.compile("<"+prem+"\\s+"+sec+"\\s*=\\s*\"?(.*?)[\"|>]", Pattern.CASE_INSENSITIVE); 
Matcher m = p.matcher(pageContents);
// Create list of link matches. 
ArrayList<String> linkList = new ArrayList();
while (m.find()) { 

String link = m.group(1).trim();
// Skip empty links. 
if (link.length() < 1) 
{ 
continue; 

}

// Prefix absolute and relative URLs if necessary. 

if (link.indexOf("://") == -1) { // Handle absolute URLs. 

if (link.charAt(0) == '/') 
{ 
link = "http://" + pageUrl.getHost() + link; 
// Handle relative URLs. 
} 
else 
{
 String file = pageUrl.getFile(); 
 if (file.indexOf('/') == -1) 
 { 
 link = "http://" + pageUrl.getHost() + "/" + link; 
 } 
 else 
 {
  String path = file.substring(0, file.lastIndexOf('/') + 1);
   link = "http://" + pageUrl.getHost() + path + link; 
   } 
   }
   }
// Remove anchors from link. 
int index = link.indexOf('#'); 
if (index != -1) { 
link = link.substring(0, index);
}
// Remove leading "www" from URL's host if present. 
//link = removeWwwFromUrl(link);
// Verify link and skip if invalid. 
URL verifiedLink = verifyUrl(link); 
if (verifiedLink == null) {
 continue; 
 }
/* If specified, limit links to those having the same host as the start URL. */ 
if ( !pageUrl.getHost().toLowerCase().equals( verifiedLink.getHost().toLowerCase())) { 
continue; 
}
//System.out.println(link);
URL url =  verifyUrl(link);
if(url != null)
{
    String data =  getStringPage(url);
    String clone = (String) link.toString().replace("/","\\");
    clone = clone.replace("http:\\\\","");
    clone = clone.replace("https:\\\\","");
   clone = strpath + clone;
    String [] rep = clone.split("\\\\");
    String pa = "";
    System.out.println("f"+rep[0]);
    for(int i = 0 ; i < (rep.length - 1); i++)
        pa +=rep[i]+"\\";
    try{
    writeRep(pa,rep[rep.length - 1],data);
    linkList.add(pa+rep[rep.length - 1]);
    }
    catch (Exception e)
    {
        
    }
}

}
return linkList;
 }

 private void writeRep(String path, String name,String data ) throws IOException
 {
      if(!new File(path).exists())
        {
            // Créer le dossier avec tous ses parents
            new File(path).mkdirs();
            System.out.println("hh");
 
        }
      this.notifyObservers(path+name);
 
      FileWriter fstream = new FileWriter(path+name);
    BufferedWriter out = new BufferedWriter(fstream);
    out.write(data);
    out.close();
  }
 
 public void save()
 {
     try {
			FileWriter fw = new FileWriter (path+"uri.txt");
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
			String value ="";
                        value += url +"\n";
                        value +=path+"\n";
                        value +=base+"\n";
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        value += df.format(date)+"\n";
                        Iterator<String> lis = css.iterator();
                        while(lis.hasNext())
                            value +="css "+lis.next()+"\n";
                        
                        lis = js.iterator();
                        while(lis.hasNext())
                            value +="js "+lis.next()+"\n";
                        
                        lis = imgs.iterator();
                        while(lis.hasNext())
                            value +="img "+lis.next()+"\n";
                        
                        lis = videos.iterator();
                        while(lis.hasNext())
                            value +="video "+lis.next()+"\n";
                        
                        lis = links.iterator();
                        while(lis.hasNext())
                            value +="link "+lis.next()+"\n";
                        System.out.println(path);
                        fichierSortie.println(value); 
			fichierSortie.close(); 
		}
		catch (IOException e){
			System.out.println(e.toString());
		}		
	}
 
 public static URI load(String path )
{
    URI uri = new URI();
     try{
			InputStream ips=new FileInputStream(path+"uri.txt"); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
                        uri.url = br.readLine();
                        uri.path = br.readLine();
                        uri.base = br.readLine();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        uri.date = df.parse(br.readLine());
                        while ((ligne=br.readLine())!=null){
                            String[] val = ligne.split(" ");
                            if(val[0].compareTo("css") == 0)
                            {
                                uri.css.add(val[1]);
                            }else if(val[0].compareTo("js") == 0)
                            {
                                uri.js.add(val[1]);
                            }else if(val[0].compareTo("img") == 0)
                            {
                                uri.imgs.add(val[1]);
                            }else if(val[0].compareTo("video") == 0)
                            {
                                uri.videos.add(val[1]);
                            }else if(val[0].compareTo("link") == 0)
                            {
                                uri.links.add(val[1]);
                            }
				
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
    return uri;
}
    @Override
    public String toString()
{
    return url;
}
}

 