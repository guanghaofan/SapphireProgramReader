/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Util.XMLRead;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;

/**
 *
 * @author Administrator
 */
public class EquationNodeCell extends Region{
    private TextField textBox;
    private Button openButton;
    final String fileName;
//    private String fileName=null;
        public String getText(){
            return textBox.getText();
        }
        public EquationNodeCell(String name, String _fiName) {
            this.fileName=_fiName;
            setId("SearchBox");
            getStyleClass().add("search-box");
            setMinHeight(24);
            setPrefSize(200, 24);
            setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            textBox = new TextField();
            textBox.setPromptText(name);
            textBox.setText(name);
            textBox.setEditable(false);
            openButton = new Button();
            openButton.setText("...");
            
            if(_fiName==null || (!new File(fileName).exists()))
                openButton.setDisable(true);
            getChildren().addAll(textBox, openButton);
            openButton.setOnAction(new EventHandler<ActionEvent>() {                
                @Override
                public void handle(ActionEvent actionEvent) {
//                    textBox.setText("");
                    textBox.requestFocus();
                    if(XMLRead.notePadPath.toLowerCase().contains("gvim")){
                        try {
                            XMLRead.editBat(fileName,textBox.getText());
                            System.out.println("Equation file name is " + fileName);
                            XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
//                            XMLRead.runBat(new File("config/openXML.cmd").getAbsolutePath());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EquationNodeCell.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else{

                        XMLRead.editBat(fileName);
                        try {
                            XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EquationNodeCell.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
//            textBox.textProperty().addListener(new ChangeListener<String>() {
//                @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                    clearButton.setVisible(textBox.getText().length() != 0);
//                }
//            });
//            textBox.setOnKeyReleased(new EventHandler<KeyEvent>(){
//
//                @Override
//                public void handle(KeyEvent t) {
// 
//                }
//            });
        }

        @Override
        protected void layoutChildren() {
            textBox.resize(getWidth(), getHeight());
            openButton.resizeRelocate(getWidth() - 28, 2, 28, 20);
        }

    @Override
    public String toString() {
        return ("Equation Node Name " + textBox.getText());
    }
    public void update(){
        
            this.textBox.setTooltip(new Tooltip("Ignore this Repeat Equation "+ this.textBox.getText()));
            this.textBox.setStyle("-fx-text-fill:red");
        
    }
        
    
}
