/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.ui.controls;

import sapphireprogramreader.xmlreader.blockreader.Levels;
import sapphireprogramreader.xmlreader.blockreader.PatternBurst;
import sapphireprogramreader.xmlreader.blockreader.Test.TestItem;
import sapphireprogramreader.xmlreader.blockreader.GenericBlock;
import sapphireprogramreader.xmlreader.blockreader.Timing;
import sapphireprogramreader.xmlreader.XMLRead;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 *
 * @author Administrator
 */
public class TestNodeCell_Label_2Text_Button extends Region{
    private TextField textBox_1;
    private TextField textBox_2;
    private Button openButton;
    private Label label;
    final String fileName;
    private String testName=null;
    final String equationName;

    public TestNodeCell_Label_2Text_Button(TestItem testItem,String openFile,final String testName) {
        equationName=null;
        openButton = new Button();
        this.testName=testName;
        String temp="";
        if (openFile!=null)
            this.fileName=openFile;
        else{
            String itemName=testItem.getName().toLowerCase();
            if (itemName.contains("pattern")||(testItem.getParentName()!=null&&testItem.getParentName().toLowerCase().contains("pattern"))){
                PatternBurst pattern= XMLRead.patternBursts.get(testItem.getExpression());
                if(pattern!=null){
                    fileName=pattern.getFileName();
                }
                else{
                    fileName="";
                    openButton.setDisable(true);
                }
                
            }
            else if(itemName.contains("timing")||(testItem.getParentName()!=null&&testItem.getParentName().toLowerCase().contains("timing"))){
                Timing timing= XMLRead.timing.get(testItem.getExpression());
                if(timing!=null){
                    this.fileName=timing.getFileName();
                }
                else{
                    fileName="";
                    openButton.setDisable(true);
                }
                
            }
            else if (itemName.contains("level")||(testItem.getParentName()!=null&&testItem.getParentName().toLowerCase().contains("level"))){
                Levels levels= XMLRead.levels.get(testItem.getExpression());
                if(levels!=null){
                    this.fileName=levels.getFileName();
                }
                else{
                    fileName="";
                    openButton.setDisable(true);
                }
                
            }
            else{
                this.fileName="";
                openButton.setDisable(true);
            }
        }
        if(fileName==null)
            openButton.setDisable(true);
        else if(!fileName.equals("")){
            File file=new File(this.fileName);
            if(!file.exists()){
                openButton.setDisable(true);
            }
            else 
                temp=file.getName();
        }
        
        
        setId("SearchBox");
        getStyleClass().add("search-box");
        setMinHeight(24);
        setPrefSize(200, 24);
        setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        
        
//        
        
        label= new Label();
        label.setText(testItem.getName());
        
        textBox_1 = new TextField();
        textBox_1.setText(testItem.getExpression());
        
        textBox_2 = new TextField();
        textBox_2.setText(temp);

        textBox_1.setEditable(false);
        textBox_2.setEditable(false);
        
        
        
        openButton.setText("...");
        getChildren().addAll(label,textBox_1,textBox_2, openButton);
        
        textBox_1.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent t) {
                Timing timing= XMLRead.timing.get(textBox_1.getText());
                Levels level= XMLRead.levels.get(textBox_1.getText());
                if (timing!=null){
                    if(t.getClickCount()==2 &&XMLRead.newTests.get(testName).getTimingRoot().isLeaf()){
                        System.out.println("Double Clinck on "+ textBox_1.getText());
//                        Timing timing= XMLRead.timing.get(textBox_1.getText());
//                        if(timing!=null){
                            XMLRead.timing.get(textBox_1.getText()).getRootItem(XMLRead.newTests.get(testName).getTimingRoot());
                            if(XMLRead.evaluationOn){
                                XMLRead.timing.get(textBox_1.getText()).updateTiming();
                                System.out.println("Updating timing");
//                                
                            }
//                        }
//                        else{
//                            System.out.println("No Timing for " + textBox_1.getText());
//                        }


                    }
                   // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                else if( level!=null){
                    if(t.getClickCount()==2 &&XMLRead.newTests.get(testName).getLevelRoot().isLeaf()){
                        System.out.println("Double Clinck on "+ textBox_1.getText());
                        XMLRead.levels.get(textBox_1.getText()).getRootItem(XMLRead.newTests.get(testName).getLevelRoot());
                        if(XMLRead.evaluationOn)
                            XMLRead.levels.get(textBox_1.getText()).updateLevels();
                    }
                    
                }
                else{
                    System.out.println("No Timing and Levels found on  "+ textBox_1.getText());
                }
            }
        });
        
        
        
        openButton.setOnAction(new EventHandler<ActionEvent>() {                
            @Override
            public void handle(ActionEvent actionEvent) {
//                    textBox.setText("");
                textBox_2.requestFocus();
                if(XMLRead.notePadPath.toLowerCase().contains("gvim")){
                    XMLRead.editBat(fileName,textBox_1.getText());
                    try {
                        XMLRead.runBat(XMLRead.openXMLFile);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestNodeCell_Label_2Text_Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                else{
                
                    XMLRead.editBat(fileName);
                    try {
                        XMLRead.runBat(XMLRead.openXMLFile);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestNodeCell_Label_2Text_Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });
    }
    public TestNodeCell_Label_2Text_Button(PatternBurst pattern) {
        equationName=null;
 
        if(pattern!=null){
            fileName=pattern.getFileName();
        }
        else{
            fileName="";
            openButton.setDisable(true);
        }
        setId("SearchBox");
        getStyleClass().add("search-box");
        setMinHeight(24);
        setPrefSize(200, 24);
        setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        
        
        
        label= new Label();
        label.setText("PatternBurst");
        
        textBox_1 = new TextField();
        textBox_1.setText(pattern.getName());
        textBox_1.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent t) {
                if(t.getClickCount()==2){
                    System.out.println("Double Clinck on "+ textBox_1.getText());
          
                        TreeItem root=XMLRead.patBurstRootItems.get(textBox_1.getText());
                        
                        if(root!=null && root.isLeaf()){
                            root=XMLRead.patternBursts.get(textBox_1.getText()).getChildren(root);
                        }
                        else{
                            System.out.println("Error this PatternBurstTreeItem " + textBox_1.getText() + " doesn't exist");
                        }
         
                }
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        textBox_2 = new TextField();
        
        File file=new File(pattern.getFileName());
        
       

        textBox_1.setEditable(false);
        textBox_2.setEditable(true);
        
        
        openButton = new Button();
        openButton.setText("...");
        
        
        if(file.exists()){
            
        }
        else{
            openButton.setDisable(true);
        }
        textBox_2.setText(file.getName());
      
        getChildren().addAll(label,textBox_1,textBox_2, openButton);
        
        openButton.setOnAction(new EventHandler<ActionEvent>() {                
            @Override
            public void handle(ActionEvent actionEvent) {
//                    textBox.setText("");
                textBox_2.requestFocus();
                if(XMLRead.notePadPath.toLowerCase().contains("gvim")){
                    XMLRead.editBat(fileName,textBox_1.getText());
                    try {
                        XMLRead.runBat(XMLRead.openXMLFile);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestNodeCell_Label_2Text_Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                {
                    XMLRead.editBat(fileName);
                    try {
                        XMLRead.runBat(XMLRead.openXMLFile);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestNodeCell_Label_2Text_Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        
    }
    public TestNodeCell_Label_2Text_Button(GenericBlock loadboardRef, String name, String _testName) {
        equationName=null;
        System.out.println("LB Ref is " + name);
        
        testName= _testName;
        openButton = new Button();
        openButton.setText("...");
        setId("SearchBox");
        getStyleClass().add("search-box");
        setMinHeight(24);
        setPrefSize(200, 24);
        setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        
        label= new Label();
        label.setText("LoadboardRef");
        textBox_1 = new TextField();
        textBox_2 = new TextField();
        textBox_1.setText(name);
        
        
        if(loadboardRef!=null){
            fileName=loadboardRef.getFileName();
            File file=new File(loadboardRef.getFileName());
            if(!file.exists()){
                openButton.setDisable(true);
            }
            
            textBox_2.setText(file.getName());
            
        }
        else{
            fileName="";
            textBox_2.setText(" ");
            openButton.setDisable(true);
        } 
        
//        textBox_1.setOnMouseClicked(new EventHandler<MouseEvent>(){
//
//            @Override
//            public void handle(MouseEvent t) {
//                if(t.getClickCount()==2){
//                    System.out.println("Double Clinck on "+ XMLRead.currentPatternBurstTreeItem);
//                    Loadboard loadboard= XMLRead.loadBoards.get(textBox_1.getText());
//                 
//                    if (loadboard!=null){
//                        if(t.getClickCount()==2 &&XMLRead.newTests.get(testName).getTimingRoot().isLeaf()){
//                            System.out.println("Double Clinck on "+ textBox_1.getText());
//    //                        Timing timing= XMLRead.timing.get(textBox_1.getText());
//    //                        if(timing!=null){
//                                XMLRead.timing.get(textBox_1.getText()).getRootItem(XMLRead.newTests.get(testName).getTimingRoot());
//    //                        }
//    //                        else{
//    //                            System.out.println("No Timing for " + textBox_1.getText());
//    //                        }
//
//
//                        }
//                       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                    }
//                }
//               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
        
        textBox_1.setEditable(false);
        textBox_2.setEditable(false);
      
        getChildren().addAll(label,textBox_1,textBox_2, openButton);
        
        openButton.setOnAction(new EventHandler<ActionEvent>() {                
            @Override
            public void handle(ActionEvent actionEvent) {
//                    textBox.setText("");
                textBox_2.requestFocus();
                if(XMLRead.notePadPath.toLowerCase().contains("gvim")){
                    XMLRead.editBat(fileName,textBox_1.getText());
                    try {
                        XMLRead.runBat(XMLRead.openXMLFile);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestNodeCell_Label_2Text_Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    XMLRead.editBat(fileName);
                    try {
                        XMLRead.runBat(XMLRead.openXMLFile);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestNodeCell_Label_2Text_Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
        
        textBox_1.resize(getWidth()/2-label.getWidth()/2, getHeight());
        textBox_1.setLayoutX(label.getWidth());
        textBox_1.setLayoutY(0);

        textBox_2.resize(getWidth()/2-label.getWidth()/2, getHeight());
        textBox_2.setLayoutX(label.getWidth()+ textBox_1.getWidth());
        textBox_2.setLayoutY(0);
        
        openButton.resizeRelocate(getWidth() - 28, 2, 28, 20);
    }
   
    public TestNodeCell_Label_2Text_Button(String labelName, String text, String _fileName) {
        String temp=null;
        equationName=null;
        if(_fileName!=null){
            
            File file= new File(_fileName);
            if (!file.exists()){
                openButton.setDisable(true);
                this.fileName=" ";
            }
            else{
                this.fileName=_fileName;
                temp=file.getName();
            }
        }
        else{
            this.fileName=" ";
            openButton.setDisable(true);
        }
        
        
        
        setId("SearchBox");
        getStyleClass().add("search-box");
        setMinHeight(24);
        setPrefSize(200, 24);
        setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        
        
        
        label= new Label();
        label.setText(labelName);
        
        textBox_1 = new TextField();
        textBox_1.setText(text);
       
        textBox_2 = new TextField();
        textBox_2.setText(temp);

        textBox_1.setEditable(false);
        textBox_2.setEditable(false);
        
        
        openButton = new Button();
        openButton.setText("...");

        getChildren().addAll(label,textBox_1,textBox_2, openButton);
        
        openButton.setOnAction(new EventHandler<ActionEvent>() {                
            @Override
            public void handle(ActionEvent actionEvent) {
//                    textBox.setText("");
                textBox_2.requestFocus();
                if(XMLRead.notePadPath.toLowerCase().contains("gvim")){  
                        XMLRead.editBat(fileName,textBox_1.getText());
                    try {
                        XMLRead.runBat(XMLRead.openXMLFile);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestNodeCell_Label_2Text_Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    XMLRead.editBat(fileName);
                    try {
                        XMLRead.runBat(XMLRead.openXMLFile);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestNodeCell_Label_2Text_Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
    public void updateTips(){
        textBox_1.setStyle("-fx-text-fill:blue");
        this.textBox_1.setTooltip(new Tooltip("Double Click to Expand the Tree"));
        
    }
    public TestNodeCell_Label_2Text_Button(String labelName, String text, String _fileName, String _equationName) {
        if(_equationName!=null)
            this.equationName=_equationName;
        else
            this.equationName=null;
        String temp=null;
        if(_fileName!=null){
            
            File file= new File(_fileName);
            if (!file.exists()){
                openButton.setDisable(true);
                this.fileName=" ";
            }
            else{
                this.fileName=_fileName;
                temp=file.getName();
            }
        }
        else{
            this.fileName=" ";
            openButton.setDisable(true);
        }
        
        
        
        setId("SearchBox");
        getStyleClass().add("search-box");
        setMinHeight(24);
        setPrefSize(200, 24);
        setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        
        
        
        label= new Label();
        label.setText(labelName);
        
        textBox_1 = new TextField();
        textBox_1.setText(text);
       
        textBox_2 = new TextField();
        textBox_2.setText(temp);

        textBox_1.setEditable(false);
        textBox_2.setEditable(false);
        
        
        openButton = new Button();
        openButton.setText("...");

        getChildren().addAll(label,textBox_1,textBox_2, openButton);
        
        openButton.setOnAction(new EventHandler<ActionEvent>() {                
            @Override
            public void handle(ActionEvent actionEvent) {
//                    textBox.setText("");
                textBox_2.requestFocus();
                if(XMLRead.notePadPath.toLowerCase().contains("gvim")){
                
                     XMLRead.editBat(fileName,equationName );
                 
                    try {
                        XMLRead.runBat(XMLRead.openXMLFile);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestNodeCell_Label_2Text_Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    XMLRead.editBat(fileName);
                    try {
                        XMLRead.runBat(XMLRead.openXMLFile);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestNodeCell_Label_2Text_Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    @Override
    public String toString() {
        return this.label.getText(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
