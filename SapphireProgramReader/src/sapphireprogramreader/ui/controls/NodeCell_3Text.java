/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.ui.controls;

import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 *
 * @author ghfan
 */
public class NodeCell_3Text extends Region{
    private TextField TextField1= new TextField();
    private TextField TextField2= new TextField();
    private TextField TextField3= new TextField();

    public NodeCell_3Text(String str1, String str2, String str3) {
        
        TextField1.setText(str1);
        TextField2.setText(str2);
        TextField3.setText(str3);
        TextField1.setEditable(false);
        TextField2.setEditable(false);
        TextField3.setEditable(false);
        getChildren().addAll(TextField1,TextField2,TextField3);
    }
    @Override
    protected void layoutChildren(){
        TextField1.resize(getWidth()/3, getHeight());
        TextField2.resize(getWidth()/3, getHeight());
        TextField3.resize(getWidth()/3, getHeight());
        
        TextField1.setLayoutX(0);
        TextField2.setLayoutX(getWidth()/3);
        TextField3.setLayoutX(getWidth()*2/3);
        
    }
    public void update(String _value){
        TextField2.setText(_value);
        TextField2.setStyle("-fx-text-fill:green");
    }

    public String getTextField3() {
        return TextField1.getText();
    }
    
    
}
