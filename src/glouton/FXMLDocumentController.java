/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glouton;



import java.awt.AWTException;
import javafx.scene.control.CheckBox;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author Omar
 */
public class FXMLDocumentController implements Initializable,Observer {
    Parametre parametre;
    public URI uriclick ;
    @FXML
    Button telechargerButton;
    
    @FXML
    public Label label;
    @FXML
    public TextField textTelecharger;
    @FXML
    public VBox vb ;
    @FXML
    public Label labeltelechargementerreur ;
    @FXML
    public Label labeltelechargementreussi ;
    @FXML
    public WebView webview;
    @FXML
    public Label labelPath;
    @FXML
    public CheckBox checkRec;
    @FXML
    public CheckBox checkImageEtVideo;
    @FXML
    public TreeView treeview;
    @FXML
    public ListView listArborescence;
    @FXML
    public ListView listImagesVideos;
    @FXML
    public Label labelNomSelect;
    @FXML
    public TabPane tabPane;
    @FXML
    public Tab tabVisualiser;
    @FXML
    public Button buttonVisualiserRac;
    @FXML
    public TextField textVisualiser;
    @FXML
    public Button buttonVisualiserInTab;
    @FXML
    public ProgressBar progressWebView;
    @FXML
    ResourceBundle rb;
    @FXML
    ComboBox choicelangue;
    @FXML
    ListView listUrl;
    ObservableList<String> items =    FXCollections.observableArrayList();
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    @FXML
    private void clickTelecharger(ActionEvent event) throws InterruptedException, IOException {
     
        
        System.out.println("You clicked me!");
    this.telechargerButton.setDisable(true);
       DownloadService service = new DownloadService(this);
        service.start();
        service.setOnFailed((WorkerStateEvent event1) -> {
            this.telechargerButton.setDisable(false); 
            items.removeAll(items);
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Tous les fichiers n'ont pas pu etre telecharge");

            alert.showAndWait();;
        });
        service.setOnSucceeded((WorkerStateEvent event1) -> {
            this.telechargerButton.setDisable(false);
            items.removeAll(items);
            if(service.valueProperty().get())
            {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("telechargement réussi");
                
                alert.showAndWait();
            }
            else
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Erreur lors du telechargement!\n Veuillez verifiez votre url ou votre Connexion");
                
                alert.showAndWait();
            }  
        });
        
      
        //rb = ResourceBundle.getBundle("javafxapplication4.messages", new Locale("en"));
    }
    
    @FXML
    private void supprimerPage(ActionEvent event)
    {
        if(this.uriclick != null)
        {
            try {
                File rep = new File (this.uriclick.path);
                this.recursifDelete(rep);
                this.parametre.uris.remove(this.uriclick);
                this.listArborescence.setItems(null);
                this.listImagesVideos.setItems(null);
                this.labelNomSelect.setText("");
                this.uriclick = null;
                this.parametre.save();
                this.refreshtree();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        
    }
    @FXML
    private void clickParcourir(ActionEvent event)
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();
           File selectedDirectory = directoryChooser.showDialog(Glouton.stage);
            if (selectedDirectory != null) {
                labelPath.setText(selectedDirectory.getAbsolutePath());
                parametre.path = selectedDirectory.getAbsolutePath();
                parametre.save();
            }
    
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
       listUrl.setItems(items);
      
       
      webview.getEngine().getLoadWorker().progressProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
        if(newValue.intValue() == 1)
            progressWebView.visibleProperty().set(false);
        else
        {
            progressWebView.visibleProperty().set(true);
             progressWebView.setProgress(newValue.doubleValue());
        }
      });
      webview.getEngine().load("file:///C:/Users/Omar/Documents/ceri.univ-avignon.fr/index.html");
      parametre = Glouton.parametre;
        
      
      labelPath.setText(parametre.path);
      this.rb = rb;
      switch(parametre.lang)
      {
          case "fr": {choicelangue.getSelectionModel().select(0);
          break;
          }
          case "en":{
              choicelangue.getSelectionModel().select(1);
              break;
          }
          case "ar":{
              choicelangue.getSelectionModel().select(2);
              break;
          }
      }
  
choicelangue.getSelectionModel().selectedItemProperty().addListener(new ChangeLangueListener(this));
      refreshtree();
    }    
    
    public void refreshtree()
    {
       treeview.setRoot(null);
        TreeItem<URI> rootItem = new TreeItem<> ();
        rootItem.setExpanded(true);
        Iterator<URI> lis = parametre.uris.iterator();
        
        while(lis.hasNext())
        {
            TreeItem<URI> item = new TreeItem<> (lis.next());            
            rootItem.getChildren().add(item);
        }
       treeview.setRoot(rootItem);
       treeview.getSelectionModel().selectedItemProperty()
            .addListener(new ItemClickListener(this));
    }
    @FXML
    private void clickbuttonVisualiserInTab()
    {
        System.out.println(textVisualiser.getText());
       webview.getEngine().load(textVisualiser.getText());
    }
    @FXML
    private void clickbuttonVisualiserRac() throws AWTException
    {
        System.out.println(this.uriclick);
        if(this.uriclick != null)
        {
            textVisualiser.setText("file:///"+this.uriclick.base.replace("\\", "/"));
            tabPane.selectionModelProperty().getValue().selectNext();
            buttonVisualiserInTab.fire();
        }
        else
        {
            
        }
    }
    
    
    public  void recursifDelete(File path) throws IOException 
    { 
        if (!path.exists()) throw new IOException( 
        "File not found '" + path.getAbsolutePath() + "'"); 
        if (path.isDirectory()) 
        { 
            File[] children = path.listFiles(); 
            for (int i=0; children != null && i<children.length; i++) 
                recursifDelete(children[i]); 
            if (!path.delete()) 
                throw new IOException( "No delete path '" + path.getAbsolutePath() + "'"); 
        } 
        else 
            if (!path.delete())
                throw new IOException( "No delete file '" + path.getAbsolutePath() + "'"); 
    }

    @Override
    public void update(Observable o, Object arg) {
      System.out.println("hhhhhhhhhhhh");
        items.add(arg.toString()); 
    }
    
}
