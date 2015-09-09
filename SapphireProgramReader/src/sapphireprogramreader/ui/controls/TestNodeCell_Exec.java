/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Util.Test.TestItem;
import Util.XMLRead;
import static Util.XMLRead.upaPath;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 *
 * @author Administrator
 */
public class TestNodeCell_Exec extends Region {
    private TextField textBox_1;
    private Button openButton;
    private Label label;
    final String fileName;

    public TestNodeCell_Exec(TestItem testItem) {
        
        openButton = new Button();
        openButton.setText("...");

        String openFile = XMLRead.upaPath  +"\\"+ testItem.getExpression().replace('.', '\\') + ".java";
        openFile = openFile.replace('/', '\\');
        this.fileName=openFile;
        File sourceFile= new File(this.fileName);
        
        if(!sourceFile.exists()){
                openButton.setDisable(true);
        }
        
        setId("SearchBox");
        getStyleClass().add("search-box");
        setMinHeight(24);
        setPrefSize(200, 24);
        setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
 
        label= new Label();
        label.setText(testItem.getName());

        textBox_1 = new TextField();
        textBox_1.setText(testItem.getExpression());
        textBox_1.setEditable(false);

        getChildren().addAll(label,textBox_1, openButton);
        
        openButton.setOnAction(new EventHandler<ActionEvent>() {                
            @Override
            public void handle(ActionEvent actionEvent) {
//                    textBox.setText("");
                textBox_1.requestFocus();
                XMLRead.editBat(fileName);
                try {
                    XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestNodeCell_Exec.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    @Override
    protected void layoutChildren() {
        if(getWidth()>480)
            label.resize(120, getHeight());
        else
            label.resize(getWidth()/4.0, getHeight());
        label.setLayoutX(0);
        label.setLayoutY(0);
        textBox_1.resize(getWidth()-label.getWidth(), getHeight());
        textBox_1.setLayoutX(label.getWidth());
        textBox_1.setLayoutY(0);
        
        openButton.resizeRelocate(getWidth() - 28, 2, 28, 20);
    }
    
    
}
