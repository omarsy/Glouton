/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glouton;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import static glouton.Glouton.root;
import static glouton.Glouton.scene;

/**
 *
 * @author Omar
 */
public class ChangeLangueListener implements ChangeListener<Label>{
    
    FXMLDocumentController fxml;
    public ChangeLangueListener(FXMLDocumentController fxml)
    {
        this.fxml = fxml;
    }

    @Override
    public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {
        System.out.println(newValue.getText());
           ResourceBundle bundle = ResourceBundle.getBundle("glouton.messages", new Locale(newValue.getText()));
        try 
        {
             this.fxml.parametre.lang = newValue.getText();
            this.fxml.parametre.save();
            Glouton.root = FXMLLoader.load(getClass().getResource("accueil.fxml"),bundle);
            scene = new Scene(root);
            Glouton.stage.setScene(scene);
            Glouton.stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ChangeLangueListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }
    
}
