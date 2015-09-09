/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.ui.controls;

import sapphireprogramreader.xmlreader.blockreader.Test;
import sapphireprogramreader.xmlreader.blockreader.Test.TestItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 *
 * @author ghfan
 */
public class TestNodeCell_Label extends Region{
    private Label label;
//    private TextField text;

    public TestNodeCell_Label(TestItem testItem) {
        label= new Label();
        label.setText(testItem.getName());       
      
        getChildren().addAll(label);
    }
    public TestNodeCell_Label(String name) {
        label= new Label();
        label.setText(name);       
      
        getChildren().addAll(label);
    }
    
    @Override
    protected void layoutChildren() {
        label.resize(getWidth(), getHeight());
        label.setLayoutX(0);
        label.setLayoutY(0);    
    }
}
