/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.ui.controls;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
/**
 *
 * @author Administrator
 */
public class OpenFile extends Region{
    private TextField textBox;
    private Button openButton;
//    private String fileName;
//    private String fileName=null;
        public String getText(){
            return textBox.getText();
        }
        public OpenFile() {
//     
            setId("SearchBox");
            getStyleClass().add("search-box");
            setMinHeight(25);
            setPrefSize(200, 25);
            setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            textBox = new TextField();
//            textBox.setPromptText(name);
//            textBox.setText(name);
            textBox.setEditable(false);
            openButton = new Button();
            openButton.setText("...");
            
//            if(_fiName==null || (!new File(fileName).exists()))
//                openButton.setDisable(true);
            getChildren().addAll(textBox, openButton);
            openButton.setOnAction(new EventHandler<ActionEvent>() {                
                @Override
                public void handle(ActionEvent actionEvent) {
//                    textBox.setText("");
                    textBox.requestFocus();
                    FileChooser FileDirectory = new FileChooser();
                    FileDirectory.setTitle("Open Your Ignored File");
                    File file= FileDirectory.showOpenDialog(null);
                    if (file!=null){
                        textBox.setText(file.getAbsolutePath());
                    }              
                }
            });
        }

        @Override
        protected void layoutChildren() {
            textBox.resize(getWidth(), getHeight());
            openButton.resizeRelocate(getWidth() - 18, 2, 18, 20);
        }

    @Override
    public String toString() {
        return ("Equation Node Name " + textBox.getText());
    }
//    public void update(){
//        
//            this.textBox.setTooltip(new Tooltip("Ignore this Repeat Equation "+ this.textBox.getText()));
//            this.textBox.setStyle("-fx-text-fill:red");
//        
//    }
        
    
}
