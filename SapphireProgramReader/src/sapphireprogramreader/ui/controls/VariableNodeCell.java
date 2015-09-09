/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Util.Equation.equationNode;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;

/**
 *
 * @author Administrator
 */
public class VariableNodeCell extends Region {
    private final TextField name = new TextField();
    private final TextField value= new TextField();
    private final TextField expression = new TextField();
//    private final Button openButton= new Button();
//        public String getText(){
//            return textBox.getText();
//        }
        public VariableNodeCell(equationNode node ) {
            
            setId("SearchBox");
            getStyleClass().add("search-box");
            setMinHeight(24);
            setPrefSize(200, 24);
            setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
         
            name.setText(node.getName());
            name.setEditable(false);
            
            
         
            value.setText(node.getValue());
            value.setEditable(false);
            
            if(node.isIsValid()){
                value.setStyle("-fx-text-fill:green");
                if(node.getName().equals("MdioDataA_tF1_Offset"))
                    System.out.println("stop here");
                
                
            }
            
            expression.setText(node.getExpression());
            expression.setEditable(false);
            

            
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
        public VariableNodeCell(String _name, String _value, String _expression ) {
            
            setId("SearchBox");
            getStyleClass().add("search-box");
            setMinHeight(24);
            setPrefSize(200, 24);
            setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
         
            name.setText(_name);
            name.setEditable(false);
            
         
            value.setText(_value);
            value.setEditable(false);
         
            expression.setText(_expression);
            expression.setEditable(false);

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
        public void updateValue(String _value,String colour, String equationName){
            if(equationName!=null)
                this.value.setTooltip(new Tooltip("It's overrided in " + equationName));
            
            else
                this.value.setTooltip(null);
                
            value.setText(_value);
            if (colour==null)
                value.setStyle("-fx-text-fill:blue");
            else
                value.setStyle("-fx-text-fill:red");
        }
        public void updateExpression(String original){
            expression.setStyle("-fx-text-fill:red");
            this.expression.setTooltip(new Tooltip("The Original Expression Is " + original));
            
        }
        
        public void updateLevelValue(String _value){
            value.setText(_value);
            value.setStyle("-fx-text-fill:green");
            this.value.setTooltip(null);
        }
        public void recoveryVlaue(equationNode node){
            
            //value.setEditable(false);
            
            if(node.isIsValid()){
                value.setText(node.getValue());
                value.setStyle("-fx-text-fill:green");
        
//                this.expression.setTooltip(null);
//                this.expression.setTooltip(null);
                this.value.setTooltip(null);
                    
//             
//                if(node.getName().equals("MdioDataA_tF1_Offset"))
//                    System.out.println("MdioDataA_tF1_Offset is reset to " + node.getValue());
                
                
            }
        }
    
}
