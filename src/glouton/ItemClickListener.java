/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glouton;
import java.util.Iterator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Omar
 */
public class ItemClickListener implements ChangeListener<TreeItem<URI>> {
    FXMLDocumentController fx;
    String label;
    public ItemClickListener(FXMLDocumentController fx)
    {
      this.fx = fx;
      label = fx.labelNomSelect.getText();
    }
                @Override
                public void changed(ObservableValue<? extends TreeItem<URI>> observable,TreeItem<URI> old_val, TreeItem<URI> new_val) {
                    TreeItem<URI> selectedItem = new_val;
                    URI uri = selectedItem.getValue() ;
                    fx.uriclick = uri;
                    if(uri != null)
                    {
                    System.out.print(fx.uriclick);
                        fx.labelNomSelect.setText(label+uri.toString());
                       ObservableList<String> items =    FXCollections.observableArrayList();
                   Iterator<String> lis = uri.links.iterator();
        
                    while(lis.hasNext())
                    {
                        items.add(lis.next().replace(uri.path,""));
                    } 
                    fx.listArborescence.setItems(items);
                    
                     items =    FXCollections.observableArrayList();
                    if(uri.imgs != null)
                    {
                        lis = uri.imgs.iterator();

                        while(lis.hasNext())
                        {
                            items.add(lis.next());
                        }
                    }
                    if(uri.videos != null)
                    {
                        lis = uri.videos.iterator();

                       while(lis.hasNext())
                       {
                           items.add(lis.next());
                       }      
                    }
                    fx.listImagesVideos.setItems(items);
                    
                    }
                    else
                    {
                        fx.labelNomSelect.setText(label);
                        fx.listImagesVideos.setItems(null);
                        fx.listArborescence.setItems(null);
                    }
                    System.out.println("Selected Text : " + fx.uriclick);
                    // do what ever you want
                }

    
}
