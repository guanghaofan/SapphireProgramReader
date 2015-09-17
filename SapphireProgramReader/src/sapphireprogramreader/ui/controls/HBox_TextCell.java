/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.ui.controls;

import sapphireprogramreader.xmlreader.XMLRead;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 *
 * @author ghfan
 */
public class HBox_TextCell extends HBox{
    private String fileName=null;
    private TextField text= new TextField();

    public HBox_TextCell(File file) {
        
        
        this.fileName=file.getAbsolutePath();
        this.text.setText(file.getName());
        text.setEditable(false);
        text.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent t) {
                if(t.getClickCount()==2){
                    if(XMLRead.notePadPath!=null && XMLRead.notePadPath!="" ){
                        if(new File(fileName).exists()){
                            XMLRead.editBat(fileName);
                            try {
                                XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                            } catch (InterruptedException ex) {
                                Logger.getLogger(HBox_TextCell.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }   
                    }
                
                }
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        getChildren().add(text);
    }

    @Override
    protected void layoutChildren() {
        this.text.resize(getWidth(), getHeight());
    }
    
    
    

}
