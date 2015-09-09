/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.ui.controls;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 *
 * @author Administrator
 */
public class VariableLabelNodeCell extends Region{
    private final Label name = new Label();
    private final Label value= new Label();
    private final Label expression = new Label();
//    private final Button openButton= new Button();
//        public String getText(){
//            return textBox.getText();
//        }
    public VariableLabelNodeCell() {

        setId("SearchBox");
        getStyleClass().add("search-box");
        setMinHeight(24);
        setPrefSize(200, 24);
        setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);

        name.setText("Name");
        value.setText("Value");
        expression.setText("Exprewssion");
//        openButton.setText("...");
        getChildren().addAll(name, value, expression);
//            openButton.setOnAction(new EventHandler<ActionEvent>() {                
//                public void handle(ActionEvent actionEvent) {
//                    textBox.setText("");
//                    textBox.requestFocus();
//                }
//            });
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
    public VariableLabelNodeCell(String str1, String str2, String str3) {

        setId("SearchBox");
        getStyleClass().add("search-box");
        setMinHeight(24);
        setPrefSize(200, 24);
        setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);

        name.setText(str1);
        value.setText(str2);
        expression.setText(str3);
//        openButton.setText("...");
        getChildren().addAll(name, value, expression);
//            openButton.setOnAction(new EventHandler<ActionEvent>() {                
//                public void handle(ActionEvent actionEvent) {
//                    textBox.setText("");
//                    textBox.requestFocus();
//                }
//            });
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
        name.resize(getWidth()/3.0, getHeight());
        name.setLayoutX(1);
        name.setLayoutY(1);
        value.resize(getWidth()/3.0, getHeight());
        value.setLayoutX(getWidth()/3.0);
        value.setLayoutY(1);

        expression.resize(getWidth()/3.0, getHeight());
        expression.setLayoutX(getWidth()*2/3.0);
        expression.setLayoutY(1);
//            openButton.resizeRelocate(getWidth() - 28, 2, 28, 20);
    }
}
