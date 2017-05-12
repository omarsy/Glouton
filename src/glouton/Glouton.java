/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glouton;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Omar
 */
public class Glouton extends Application {
   public static Stage stage;
   public static Parent root;
   public static Scene scene;
   public static Parametre parametre;
   
    @Override
    public void start(Stage stage) throws Exception {
        parametre = Parametre.load();
       ResourceBundle bundle = ResourceBundle.getBundle("glouton.messages", new Locale(parametre.lang));
       root = FXMLLoader.load(getClass().getResource("accueil.fxml"),bundle);
        
        scene = new Scene(root);
        Glouton.stage =stage;
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
