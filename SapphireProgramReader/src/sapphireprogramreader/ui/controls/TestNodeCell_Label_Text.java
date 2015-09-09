/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Util.Test.TestItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 *
 * @author ghfan
 */
public class TestNodeCell_Label_Text extends Region {
    private Label label;
    private TextField text;

    public TestNodeCell_Label_Text(TestItem testItem, String labelName) {
        setMinHeight(24);
        setPrefSize(200, 24);
        
        label= new Label();
        if (labelName!=null)
            label.setText(labelName);
        else
            label.setText(testItem.getName());        
        text= new TextField();
        if(testItem.getAttriName()!=null){
            if(testItem.getAttriName().toLowerCase().equals("name"))
                text.setText(testItem.getExpression());
            else
                //sigref="Leak_Vddio33_Alt"
                text.setText(testItem.getAttriName() +  "=\""  +  testItem.getExpression() + "\"");
        }
        else
            text.setText(testItem.getExpression());
        getChildren().addAll(label, text);
    }
    public TestNodeCell_Label_Text(String labelName, String textIn) {
        setMinHeight(24);
        setPrefSize(200, 24);
        
        label= new Label();
        if (labelName!=null)
            label.setText(labelName);
        else
            label.setText("Empty");  
        
        text= new TextField();
        if(textIn!=null){
            text.setText(textIn);
        }
        else
            text.setText(textIn);
        getChildren().addAll(label, text);
    }
    @Override
    protected void layoutChildren() {
        if (getWidth()>480)
            label.resize(120, getHeight());
        else
            label.resize(getWidth()/4.0, getHeight());
        
        label.setLayoutX(0);
        label.setLayoutY(0);
        text.resize(getWidth()-label.getWidth(), getHeight());
        text.setLayoutX(label.getWidth());
        
    }
    
    public void update(String _value){
         this.text.setText(_value);
        this.text.setStyle("-fx-text-fill:green");
    }
    
}
