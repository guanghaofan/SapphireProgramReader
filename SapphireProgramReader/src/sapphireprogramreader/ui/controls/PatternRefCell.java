/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.ui.controls;

import sapphireprogramreader.xmlreader.blockreader.PatListItem;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 *
 * @author ghfan
 */
public class PatternRefCell extends Region {

    private Label name;
    private Label enabled;
    private Label load;
    
    private TextField nameText;
    private TextField enabledText;
    private TextField loadText;
    
    public PatternRefCell(PatListItem patList)  {
        System.out.println(patList.getName());
        
        setId("SearchBox");
        getStyleClass().add("search-box");
        setMinHeight(24);
        setPrefSize(200, 24);
        
        setMinWidth(200);

        setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        
        name=new Label("PatternRef");
        enabled= new Label("Enabled");
        load = new Label("Load");
        
      
        enabled.setAlignment(Pos.BASELINE_RIGHT);
        load.setAlignment(Pos.BASELINE_RIGHT);
        
        if(patList!=null){
            nameText= new TextField(patList.getName());
            
            if(patList.getEnabled()!=null)
                enabledText= new TextField(patList.getEnabled());
            else
                enabledText= new TextField("");
            if(patList.getLoaded()!=null){
                loadText = new TextField(patList.getLoaded());
            }
            else
                loadText = new TextField("");
        }
        else
        {
             nameText= new TextField("");
             enabledText= new TextField("");
             loadText = new TextField("");
            System.out.println("Error, null patListItem in build PatList TreeItem");
            
        }
//        loadText.setMaxWidth(40);
       
        
//        name.setMaxWidth(40);
        name.setPrefSize(40, 24);
        
//        enabled.setMaxWidth(40);
        enabled.setPrefSize(40,24);
        
//        load.setMaxWidth(40);
        load.setPrefSize(40,24);
        
        nameText.setEditable(false);
        loadText.setEditable(false);
        enabledText.setEditable(false);
        getChildren().addAll(name, nameText,enabled,enabledText, load, loadText);
        
    }
    @Override
    protected void layoutChildren() {
        name.resize(getWidth()/4, getHeight());
        name.setLayoutX(0);
        name.setLayoutY(0);
        if (enabledText.getText().equals("")){
//            System.out.println("sssssss");
            if(getWidth()>480){
                name.resize(120, getHeight());
                nameText.resize(getWidth()-name.getWidth(), getHeight());
                nameText.setLayoutX(name.getWidth());
            }
                 
            else{
                nameText.resize(getWidth()-name.getWidth(), getHeight());
                nameText.setLayoutX(name.getWidth());
            }
        }
        else if(!enabledText.getText().equals("")){
            if(getWidth()>480){
                name.resize(120, getHeight());
                nameText.resize(getWidth()/2-120 ,getHeight());
                nameText.setLayoutX(120);
                
                enabled.resize(120, getHeight());
                enabled.setLayoutX(getWidth()/2);
                
                enabledText.resize(getWidth()/2-120 ,getHeight());
                enabledText.setLayoutX(getWidth()/2+120);
                
            }
            else{
                nameText.resize(getWidth()/4,getHeight());
                nameText.setLayoutX(name.getWidth());

                enabled.setLayoutX(getWidth()/2 );
                enabledText.resize(getWidth()/4,getHeight());
                enabledText.setLayoutX(getWidth()*3/4);
                enabled.resize(getWidth()/4, getHeight());
            }
        }
//        else if((enabledText.getText().equals(""))&& (!loadText.getText().equals(""))){
//            if(getWidth()>480){
//                name.resize(120,getHeight());
//                nameText.resize(getWidth()-120 ,getHeight());
//                nameText.setLayoutX(name.getWidth());
//
////                load.resize(60, getHeight());
////                load.setLayoutX(getWidth()-120);
////                loadText.resize(60, getHeight());
////                loadText.setLayoutX(getWidth()-60);
//            }
//            else{
//                nameText.resize(getWidth()-name.getWidth(),getHeight());
//                nameText.setLayoutX(name.getWidth());
//
////                enabled.setLayoutX(getWidth()*3/4);
////                enabled.resize(getWidth()/8, getHeight());
////                enabledText.resize(getWidth()/8,getHeight());
////                enabledText.setLayoutX(getWidth()*7/8);  
//            }
//            
//        }
//        else if((!enabledText.getText().equals(""))&& (!loadText.getText().equals(""))){
////            System.out.println("name is " + name.getText());
//            nameText.resize(getWidth()*7/32,getHeight());
//            nameText.setLayoutX(name.getWidth());
//            
//            enabled.setLayoutX(getWidth()*7/16);
//            enabled.resize(getWidth()*7/32, getHeight());
//            enabledText.resize(getWidth()*7/32,getHeight());
//            enabledText.setLayoutX(getWidth()*21/32);
//            
////            load.setLayoutX(getWidth()*7/8);
////            loadText.setLayoutX(getWidth()*15/16);
////            load.resize(getWidth()/16, getHeight());
////            loadText.resize(getWidth()/16, getHeight());
//        }
    }
    
    
}
