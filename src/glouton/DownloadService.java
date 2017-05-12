/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glouton;

import javafx.concurrent.Service;
import javafx.concurrent.Task;




/**
 *
 * @author Omar
 */
public class DownloadService extends Service<Boolean>{
    FXMLDocumentController fxml;
    URI uri;
    public DownloadService(FXMLDocumentController fxml)
    {
        this.fxml = fxml;
        uri = new URI();
        uri.addObserver(fxml);
    }
    

    @Override
    protected Task<Boolean> createTask() {
       return new Task<Boolean>(){

     @Override
     protected Boolean call() throws Exception {
        // Sauvegarder le fichier ici.
        
        
        
       if( uri.download(fxml.textTelecharger.getText(), fxml.labelPath.getText()+ "\\", fxml.checkImageEtVideo.isSelected(), fxml.checkRec.isSelected()))
       {
        fxml.parametre.uris.add(uri);
        fxml.parametre.save();
        fxml.refreshtree();
       
            return true;
       }
       else
       {
          
       }
        return false;
          
      }
    };
  } //To change body of generated methods, choose Tools | Templates.
    }

    
    

