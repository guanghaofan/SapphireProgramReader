/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader;


import sapphireprogramreader.xmlreader.blockreader.ActionList;
import sapphireprogramreader.xmlreader.blockreader.Equation;
import sapphireprogramreader.xmlreader.blockreader.GoToResult;
import sapphireprogramreader.xmlreader.blockreader.Test;
import sapphireprogramreader.xmlreader.blockreader.Test.TestItem;
import java.io.File;
import java.io.FileFilter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import sapphireprogramreader.xmlreader.XMLRead;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.dom4j.DocumentException;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import sapphireprogramreader.xmlreader.XMLRead.TreeNode;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;




/**
 *
 * @author ghfan
 */
public class SapphireProgramReader extends Application {
    
    public File ProjectPath=null;
    public XMLRead xmlReader = new XMLRead();
    final static ProgressIndicator searchProgressIndicator= new ProgressIndicator(-1.0);
//    final static ProgressIndicator binningAuditProgressIndicator = new ProgressIndicator(-1.0);
    final static SimpleBooleanProperty startSearch=new SimpleBooleanProperty();
    
    FileFilter FileFilter;
    public static  SimpleBooleanProperty prorgamLoaded=new SimpleBooleanProperty();
    boolean firstSearch=true;
    final ListView fileListView= new ListView<>();
    final Label flowContextLabel = new Label("FlowContext");
    final TextField flowContextField= new TextField("");
    final TextField programNameField = new TextField();
    final TreeView actionTree= new TreeView();

    final Label nodeTypeLabel = new Label("NodeType");
    final TextField nodeTypeField = new TextField();
    TableView<Result> myGoToTable= new TableView<>();
    ObservableList<Result> resultData= FXCollections.observableArrayList();
    public String previousFlow="";
    final TreeView flowTree= new TreeView();
    
//    final TextArea testFlowArea = new TextArea();
//    final TextArea testArea = new TextArea();
    public static String searchContent="";
    public static boolean deepSearch=false;
    
    final TitledPane equationPane= new TitledPane();
    final TitledPane testContextPane = new TitledPane();
    final TreeView equationTree = new TreeView();
    final TreeView testContextTree = new TreeView();
    
    public boolean test=false;
    final TextField actionLabel= new TextField();
    public TreeItem programExplorerRoot= new TreeItem();
    public TreeView programExplorerView= new TreeView();
    
    public MenuItem openItem = new MenuItem("Open");
    public Menu recentItem = new Menu("Recent Files");
//    final ComboBox actionBox= new ComboBox();
//    public static int actionIndex=-1;
//    public static String ignoredFileName=null;
    
    
    public Service searchTask = new Service() {
        @Override
        protected Task createTask() {
            return new Task(){
                @Override
                protected Object call() throws Exception {
                    xmlReader.startSearch(searchContent,deepSearch);
                    return null;
                }
            };
        }
    };
    
//    public Service binningAuditTask = new Service() {
//        @Override
//        protected Task createTask() {
//            return new Task(){
//                @Override
//                protected Object call() throws Exception {
//                    boolean result = xmlReader.auditBinTables(xmlReader.actionTree.get(actionIndex), actionIndex,ignoredFileName );
//                    if(result)
//                        System.out.println("Failed to Run BinningTableAudit");
//                    return null;
//                }
//            };
//        }
//    };
    
//    public Service loadTask = new Service() {
//        @Override
//        protected Task createTask() {
//            return new Task(){
//                @Override
//                protected Object call() throws Exception {
//                    binningAuditProgressIndicator.setVisible(true);
//                    return null;
//                }
//            };
//        }
//    };
    
    public SapphireProgramReader() {
        this.FileFilter = new FileFilter() {
      @Override
      public boolean accept(File file) {
          //if the file extension is .xml return true, else false
          if (file.getName().endsWith(".xml")) {
              return true;
          }
          return false;
      }
  };}
       
    @Override
    public void init(){
        System.out.println("Step into Init");
    }
    
    @Override
    @SuppressWarnings("empty-statement")
    public void start(Stage primaryStage) throws IOException {
        xmlReader.readConfig();
        xmlReader.readRecentFile(true, null);
        
        equationTree.setRoot(new TreeItem("Equations"));
        equationTree.setShowRoot(false);
        prorgamLoaded.setValue(false);// init state True  <Load> False-->True  </Load>
        
        
//        binningAuditProgressIndicator.setVisible(false);
        
        String searchBoxCss = SapphireProgramReader.class.getResource("SearchBox.css").toExternalForm();
        fileListView.setEditable(false);
        MenuItem openFile= new MenuItem("Open Selected File");
        fileListView.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent t) {
                if(t.getClickCount()==2){
//                    System.out.println("");
                    
                    if(XMLRead.notePadPath.toLowerCase().contains("gvim") && searchContent!=null && searchContent!=""){
                        XMLRead.editBat(fileListView.getSelectionModel().getSelectedItem().toString(),searchContent);
                        try {
                            XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else{
                        XMLRead.editBat(fileListView.getSelectionModel().getSelectedItem().toString());
                        try {
                            XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                
                }
//                else if(t.getButton().equals(MouseButton.SECONDARY)){
//                    System.out.println("Right clicked  on " +t.getSource());
//                }
//                System.out.println(fileListView.getSelectionModel().getSelectedItem().toString());
               
            }
        });
        
        openFile.setOnAction(new EventHandler() {

            @Override
            public void handle(Event t) {
                XMLRead.editBat(fileListView.getItems().get(fileListView.getSelectionModel().getSelectedIndex()).toString());
                try {
                    XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                } catch (InterruptedException ex) {
                    Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        ContextMenu searchContextMenu = new ContextMenu();
        searchContextMenu.getItems().add(openFile);
        
        
        //fileListView.setContextMenu(searchContextMenu);
        startSearch.set(false);
        startSearch.addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if(fileListView.getItems()!=null) fileListView.getItems().clear();
//               System.out.println("From "+ t + "  To " + t1);
               if(firstSearch){
//                   System.out.println("First Start Task");
                   fileListView.setItems(null);
                   searchTask.start();
                   firstSearch=false;
               }
               else{
                   fileListView.setItems(null);
//                   System.out.println("Restart  Task");
                   searchTask.restart();
               }
            }
        });
        searchTask.setOnFailed(new EventHandler<WorkerStateEvent>(){

            @Override
            public void handle(WorkerStateEvent t) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                System.out.println("Failed " + t.getSource().getValue());
                xmlReader.searchResult.clear();
            }
        });
        searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>(){

            @Override
            public void handle(WorkerStateEvent t) {
//                System.out.println("Done " + t.getSource().getValue());
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                fileListView.setItems(xmlReader.searchResult);
            }
        });

        
        flowTree.setEditable(true);
        //TreeView<String> treeView = new TreeView<String>(rootNode);
        flowTree.setEditable(false);
        flowTree.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
            @Override
            public TreeCell<String> call(TreeView<String> p) {
                return new TextFieldTreeCellImpl();
            }
        });
        programExplorerView.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
            @Override
            public TreeCell<String> call(TreeView<String> p) {
                return new TextFieldTreeCell();
            }
        });
        searchProgressIndicator.setPrefSize(100, 100);
        searchProgressIndicator.visibleProperty().bind(searchTask.runningProperty());
//        binningAuditProgressIndicator.setPrefSize(100, 100);
//        binningAuditProgressIndicator.visibleProperty().bind(binningAuditTask.runningProperty());
        
        final Accordion accordion = new Accordion();
        TitledPane titledPane= new TitledPane();
       
        
        
//        final SplitPane textSplitPane= new SplitPane();
        
        BorderPane root = new BorderPane();
        root.setPrefSize(800,400);
       
        HBox statusBar= new HBox();
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPrefSize(600,25);
        statusBar.autosize();
        
        
        statusBar.getChildren().add(programNameField);
        programNameField.setAlignment(Pos.CENTER_LEFT);
        programNameField.setPrefSize(600, 25);
        programNameField.setEditable(false);
        //programNameField.getStylesheets().add(searchBoxCss);
        
        
        statusBar.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {  
                programNameField.setPrefWidth(t1.doubleValue());
                actionLabel.setPrefWidth(t1.doubleValue());
            }
        });
        root.setBottom(statusBar);
        
        
        MenuBar menuBar = new MenuBar();
        menuBar.autosize();
        //menuBar.getStylesheets().add(searchBoxCss);
        
        
        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuView = new Menu("View");
        
        openItem.setOnAction(new EventHandler<ActionEvent>() {
    @Override public void handle(ActionEvent e) {
//        System.out.println("Opening Database Connection...");
        DirectoryChooser ProjectDirectory = new DirectoryChooser();
        ProjectDirectory.setTitle("Open Your Program Directory");
        ProjectPath = ProjectDirectory.showDialog(null);
        if (ProjectPath!=null && prorgamLoaded.getValue()==false){
//            binningAuditProgressIndicator.setVisible(true);
//            System.out.println("set visible true");
            loadProgram();
//            binningAuditProgressIndicator.setVisible(false);
//            System.out.println("set visible false");
        }     
    }
});
        
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem openConfigFile = new MenuItem("Open Config File");
        if(XMLRead.notePadPath==null)
            openConfigFile.setDisable(true);
        
        
        
        
        openConfigFile.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    File file= new File("config/Config.xml");
                    if(file.exists()){
                        String fileName = file.getAbsolutePath();
                        XMLRead.editBat(fileName);
                        System.out.println("Start to open config file");
                        try {
                            XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                
            }
        });
        
//        settings.setOnAction(new EventHandler<ActionEvent>(){
//
//            @Override
//            public void handle(ActionEvent t) {
//                Stage secondWindow=new Stage();
//                VBox vBox= new VBox();
//                HBox editor= new HBox();
//                HBox hBox= new HBox();
//                
//                Label label= new Label("EquationEvaluation");
//                
//                final ToggleGroup group= new ToggleGroup();
//                RadioButton on= new RadioButton("On");
//                on.setUserData("on");
//                RadioButton off= new RadioButton("Off");
//                off.setUserData("off");
//                on.setToggleGroup(group);
//                off.setToggleGroup(group);
//                
//                group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
//
//                    @Override
//                    public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
//                        if (group.getSelectedToggle() != null) {
//                            if (group.getSelectedToggle().getUserData().toString().equals("on")){
//                                            xmlReader.evaluationOn=true;
//                            }
//                            else{
//                                xmlReader.evaluationOn=false;
//                            
//                            }
//                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                        }
//                    }
//                });
//                
//                
//                vBox.getChildren().addAll(hBox, editor);
//                Label eLabel= new Label("Editor");
//                TextField eText= new TextField();
//                Button eButton= new Button("...");
//                
//                editor.getChildren().addAll(eLabel,eText,eButton);
//                
//                
//                
//                Scene scene=new Scene(vBox,350,100);
//                
//                
//                
//                
//                
//                hBox.setLayoutY(20);
//                hBox.setLayoutX(10);
////                
//                hBox.setMinWidth(350);
//                hBox.setMaxWidth(350);
////                
////                hBox.setMaxHeight(50);
////                hBox.setMinHeight(50);
//                
//               label.setPrefWidth(hBox.getWidth()*3/5);
//               on.setPrefWidth(hBox.getWidth()/5);
//               off.setPrefWidth(hBox.getWidth()/5);
//                
//                
//                
//                hBox.getChildren().addAll(label,on, off);
//                secondWindow.setTitle("Settings");
//                secondWindow.setScene(scene);secondWindow.show();
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
        
        // still need to check if the file is existing
        if(!xmlReader.recentFileList.isEmpty()){
            for(int i=0; i!= xmlReader.recentFileList.size();i++){
                if((new File(xmlReader.recentFileList.get(i)).exists())){
                    final MenuItem item = new MenuItem(xmlReader.recentFileList.get(i));
                    item.setOnAction(new EventHandler<ActionEvent>(){

                        @Override
                        public void handle(ActionEvent t) {
                            ProjectPath = new File(item.getText());
                            if (ProjectPath!=null && prorgamLoaded.getValue()==false){
                                loadProgram();
                            }   
                            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                    });  
                    recentItem.getItems().add(item);
                    
                }
            }
        }
        
        exitItem.setOnAction(new EventHandler<ActionEvent>() {
    @Override public void handle(ActionEvent e) {
        System.exit(0);
    }
});   
        
        menuFile.getItems().addAll(openItem,recentItem,openConfigFile,exitItem);
        
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        
        
        HBox labelHbox = new HBox();
//        labelHbox.setPadding(new Insets(2,2,2,2));
        
//        final Label flowLabel= new Label("flow");
        
        
        actionLabel.setPrefHeight(25);
        actionLabel.setEditable(false);
//        actionLabel.getStylesheets().add(searchBoxCss);
//        action.getStylesheets().add(searchBoxCss);
//        flowTree.getStylesheets().add(searchBoxCss);
        //actionLabel.setStyle("-fx-font-size:15; -fx-font-weight:bold;-fx-font-style;-fx-font;-fx-font-family; -fx-text-fill: black; fx-border-color: red; -fx-border-style; -fx-border-width:8; -fx-label-padding:2");
        
//        flowLabel.setPrefHeight(25);
        
//        actionLabel.setPrefWidth(160);
//        flowLabel.setPrefWidth(420);
        actionLabel.setPrefSize(600, 25);
        
        
        labelHbox.getChildren().addAll(actionLabel);
        
        VBox topVbox= new VBox();
        topVbox.getChildren().addAll(menuBar, labelHbox);
        
        root.setTop(topVbox);
        
        //***********************************************************************************
        SplitPane splitPane =new SplitPane();
        
        //******************************************************
        AnchorPane actionPane= new AnchorPane();
//        actionPane.setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);

        actionTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>(){

            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> ov, TreeItem<String> t, TreeItem<String> t1) {
                if(t1!=null){
                    int index = actionTree.getSelectionModel().getSelectedIndex();
//                    System.out.println("Index is  " + index );
//                    System.out.println(t1.getValue());
//                    flowTree.setRoot(xmlReader.actionTree.get(index));
                    flowTree.setRoot(xmlReader.actionTree.get(index));
                    flowTree.setShowRoot(false);
//                    xmlReader.auditBinTables(xmlReader.actionTree.get(index), index);
//                    testArea.clear();
//                    testFlowArea.clear();
                    flowContextField.clear();
                    nodeTypeField.clear();
                    actionLabel.clear();
                }
            }
        });
//       
        
//        
//        action.setLayoutX(actionPane.getLayoutX());
//        action.setLayoutY(actionPane.getLayoutY());
        final TabPane leftTabPane= new TabPane();
        leftTabPane.setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        
        
        
        Tab actionTab= new Tab("Action List");
        Tab explorerTab = new Tab("Program Explorer");
        actionTab.setClosable(false);
        explorerTab.setClosable(false);
        programExplorerView.setShowRoot(false);
    
        leftTabPane.getTabs().addAll(actionTab, explorerTab);
        final AnchorPane  actionAnchorPane = new AnchorPane();
        final AnchorPane  explorerAnchorPane = new AnchorPane();
        
        actionTab.setContent(actionAnchorPane);
        actionAnchorPane.getChildren().add(actionTree);
//        actionAnchorPane.setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
//        action.setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
//        programExplorerView.setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        explorerTab.setContent(explorerAnchorPane);
//        explorerAnchorPane.setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        explorerAnchorPane.getChildren().add(programExplorerView);
        actionTree.setLayoutX(1);
        programExplorerView.setLayoutX(1);
        actionTree.setLayoutY(1);
        programExplorerView.setLayoutY(1);
        actionPane.getChildren().add(leftTabPane);
        
        actionPane.widthProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
               
                leftTabPane.setPrefWidth(t1.doubleValue());
                actionAnchorPane.setPrefWidth(t1.doubleValue());
                explorerAnchorPane.setPrefWidth(t1.doubleValue());
                actionTree.setPrefWidth(t1.doubleValue()-2);
                programExplorerView.setPrefWidth(t1.doubleValue()-2);
               
            }
        });
        actionPane.heightProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                leftTabPane.setPrefHeight(t1.doubleValue());    
                actionAnchorPane.setPrefHeight(t1.doubleValue());
                explorerAnchorPane.setPrefHeight(t1.doubleValue());
                actionTree.setPrefHeight(t1.doubleValue()-33);
                actionTree.setMaxHeight(t1.doubleValue()-33);
                programExplorerView.setPrefHeight(t1.doubleValue()-33);
                programExplorerView.setMaxHeight(t1.doubleValue()-33);
            }
        });
        
        //*******************************************************
        AnchorPane mainPane = new AnchorPane();
        

        mainPane.heightProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
       
                 flowTree.setPrefHeight(t1.doubleValue());
                 
            }
        });
        mainPane.widthProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
              
                flowTree.setPrefWidth(t1.doubleValue());
            }
        });
        
        //mainPane.autosize();
        mainPane.getChildren().addAll(flowTree);//loadProgressIndicator);
//        binningAuditProgressIndicator.visibleProperty().bind(prorgamLoaded);
        
        flowTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeNode>(){

           /*
            @Override
            public void changed(ObservableValue<? extends TreeNode> ov, TreeNode t, TreeNode t1) {
                
//                Stage secondWindow=new Stage();
//                Scene scene=new Scene(new VBox(),300,275);
//                secondWindow.setTitle("secondWindow");
//                secondWindow.setScene(scene);secondWindow.show();
                
                if (t1!=null){
                    
//                    System.out.println(testFlowArea.getWidth()+"   "  + testFlowArea.getPrefWidth());
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    
                    //debug info
//                    System.out.println("FlowNo " + t1.getFlowNo());
//                    System.out.println("NodeNo "+ t1.getNodeNo());
//                    System.out.println(t1.toString());
                    
                    
//                    testArea.clear();
//                    testFlowArea.clear();
                    if (t1.getNodeType().equalsIgnoreCase("test")){
                        t1.getBaseNode().printTestNode();
                        for(String line: t1.getBaseNode().getText()){
                            //System.out.println(line);
                            testArea.appendText(line+"\n");
                        }
                        //testFlowArea.appendText(xmlReader.tests.get(t1.getNodeIndex()).getTestName());
                        testFlowArea.appendText(xmlReader.tests.get(t1.getNodeIndex()).getElement().asXML());
                        testFlowArea.appendText("\n");
//                        if(equationTree.getRoot().getChildren()!=null){                                                                                                                                                                                                                                                                                                                                                                                                        
//                                        equationTree.getRoot().getChildren().clear();
//                                    
//                                    }
//                        xmlReader.equationRootNodes.clear();
                        
                     
                        if(t1.getEquationsRefs()!=null){
                        
//                            if(t!=null && t.getEquationsRefs()!=null){
//                                setExpanded(xmlReader.equationRootNodes.get(t.getEquationsRefs()));
//                            }
                            System.out.println(t1.getEquationsRefs());
//                            //testFlowArea.appendText(t1.getEquationsRefs());
                            String[] equationsRefs =t1.getEquationsRefs().split(",");  
//                            if(equationsRefs.length==1){
//                                String oneRef= equationsRefs[0];         
//                                if(xmlReader.equations.get(oneRef) !=null){
//                                    TreeItem subRoot= xmlReader.equationRootNodes.get(oneRef);
//                                    if(subRoot!=null){
//                                        System.out.println("Skip Building Equation Tree ");
//                                        equationTree.setRoot(subRoot);
//                                        setExpanded(subRoot);
//                                    }
//                                    else{
//                                        equationTree.setRoot(xmlReader.buildEquationTree(xmlReader.equations.get(oneRef)));
//                                    }
//                                }
//                                equationTree.setShowRoot(true);                               
//                            }
//                            else{
//                                if(xmlReader.equationRootNodes.get(t1.getEquationsRefs())!=null){
//                                    System.out.println("Skip Building Equation Tree ");
//                                    equationTree.setRoot(xmlReader.equationRootNodes.get(t1.getEquationsRefs()));
//                                    equationTree.setShowRoot(false);
//                                    setExpanded(equationTree.getRoot());
//                                }
//                                else{
                                   
                                   
//                                    equationTree.setShowRoot(false);
                                   
//                                    for (String ref: equationsRefs){
//                                        if(ref!=null){
//        //                                        System.out.println(ref);
//        //                                    testFlowArea.appendText(ref);
//                                            if(xmlReader.equations.get(ref) !=null){        
//                                                equationTree.getRoot().getChildren().add(xmlReader.buildEquationTree(xmlReader.equations.get(ref)));
//                                            }
//                                            else{
//                                                System.out.println("This equation "+ ref +"   doesn't exist");
//                                            }
//                                        }
//                                    }
//                                }
//                            }
                            
//                            equationTree.getRoot().setExpanded(true);
                            if(equationsRefs.length==1){
                                equationTree.setRoot(xmlReader.rootNodes.get(equationsRefs[0]));
                                equationTree.setShowRoot(true);
                            }
                            else{
                                equationTree.setRoot(xmlReader.rootNodes.get(t1.getEquationsRefs()));
                                equationTree.setShowRoot(false);
                            }
                            
                        }
                        else{
                            
                        }
                        if(xmlReader.tests.get(t1.getNodeIndex()).getPatternName()!=""){
                            //        this.name=name;
                            //        this.composite=composite;
                            //        this.executionMode=executionMode;
                            //        this.compareRef=compareRef;
                            //        this.fileName= fileName;
                            testFlowArea.appendText(xmlReader.patternBursts.get(xmlReader.tests.get(t1.getNodeIndex()).getPatternName()).nameToString());
                            testFlowArea.appendText("\n");
                            testFlowArea.appendText(xmlReader.patternBursts.get(xmlReader.tests.get(t1.getNodeIndex()).getPatternName()).compositeToString());
                            testFlowArea.appendText("\n");
                            if ((xmlReader.patternBursts.get(xmlReader.tests.get(t1.getNodeIndex()).getPatternName()).executionModeToString())!=null){
                                testFlowArea.appendText(xmlReader.patternBursts.get(xmlReader.tests.get(t1.getNodeIndex()).getPatternName()).executionModeToString());
                                testFlowArea.appendText("\n");}
                            if((xmlReader.patternBursts.get(xmlReader.tests.get(t1.getNodeIndex()).getPatternName()).compareRefToString())!=null){
                                testFlowArea.appendText(xmlReader.patternBursts.get(xmlReader.tests.get(t1.getNodeIndex()).getPatternName()).compareRefToString());
                                testFlowArea.appendText("\n");
                            }
                            testFlowArea.appendText("</PatternBurst>");
                            
                   
                        }
                        testArea.setScrollTop(0);
                    }
                    else if(t1.getNodeType().equalsIgnoreCase("flow")){
                        t1.getBaseNode().printFlowNode();
                        for(String line: t1.getBaseNode().getText()){
                              //System.out.println(line);
                              testArea.appendText(line+"\n");
                        }
                        if(t1.getEquationsRefs()!=null){
                            //testFlowArea.appendText(t1.getEquationsRefs());
                            String[] equationsRefs =t1.getEquationsRefs().split(",");
                            for (String ref: equationsRefs){
                                testFlowArea.appendText(ref);
                                testFlowArea.appendText("\n");
                                
                            }
                        }
                    }
                    else if(t1.getNodeType().equalsIgnoreCase("entry")){
                        t1.getStartNode().printStartNode();
                        for(String line: t1.getStartNode().getText()){
                            //System.out.println(line);
                                testArea.appendText(line+"\n");
                        }
                    }
                    else if(t1.getNodeType().equalsIgnoreCase("exit")){
                        t1.getExitNode().printExitNode();
                        for(String line: t1.getExitNode().getText()){
                            //System.out.println(line);
                                testArea.appendText(line+"\n");
                              
                        }
                    }
                   
                    resetNodeInfo(t1);
                    testArea.setScrollTop(0);
                    //testArea.selectHome();
                    
                 }//end if
                
            }// func
            
            */
            
            //****************************************************************************************
            @Override
            public void changed(ObservableValue<? extends TreeNode> ov, TreeNode t, TreeNode t1) {
                
//                Stage secondWindow=new Stage();
//                Scene scene=new Scene(new VBox(),300,275);
//                secondWindow.setTitle("secondWindow");
//                secondWindow.setScene(scene);secondWindow.show();
                
                if(equationTree.getRoot().getChildren()!=null){                                                                                                                                                                                                                                                                                                                                                                                                        
//                                        equationTree.getRoot().getChildren().clear();
                        xmlReader.clearRoot(equationTree.getRoot());
                }
                  
                if (t1!=null){
                    actionLabel.setText(t1.getFlowContext());
                    
//                    System.out.println(testFlowArea.getWidth()+"   "  + testFlowArea.getPrefWidth());
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    
                    //debug info
//                    System.out.println("FlowNo " + t1.getFlowNo());
//                    System.out.println("NodeNo "+ t1.getNodeNo());
//                    System.out.println(t1.toString());
                    
//                    
//                    testArea.clear();
//                    testFlowArea.clear();
                    if (t1.getNodeType().equalsIgnoreCase("test")){
//                        Test test=XMLRead.newTests.get(t1.getBaseNode().getTestFlowRef());
                        Test test=XMLRead.newTests.get(t1.getTestFlowRef());
                        
                        if(test!=null){
                            if(test.getPatternBurst()!=null)
                                XMLRead.currentPatternBurstTreeItem=test.getPatternBurst();
//                            testContextTree.setRoot(test.getTreeItem());
                            testContextTree.setRoot(test.getRootItem());
                        }
                        else
                            System.out.println("No test for this Item " + t1.getBaseNode().getName());
//                        t1.getBaseNode().printTestNode();
//                        for(String line: t1.getBaseNode().getText()){
//                            //System.out.println(line);
//                            testArea.appendText(line+"\n");
//                        }
                        //testFlowArea.appendText(xmlReader.tests.get(t1.getNodeIndex()).getTestName());
                        
                        
//                        testFlowArea.appendText(xmlReader.tests.get(t1.getNodeIndex()).getElement().asXML());
//                        testFlowArea.appendText("\n");
//                        Test _test = XMLRead.newTests.get(t1.getBaseNode().getTestFlowRef());
//                        String pattternBurst;
//                        if(test!=null){
//                            printTestFlowArea(_test.getRoot());
//                            pattternBurst=XMLRead.newTests.get(t1.getBaseNode().getTestFlowRef()).getPatternBurst();
//                            if(pattternBurst!=null){
//           
//                                testFlowArea.appendText(XMLRead.patternBursts.get(pattternBurst).nameToString());
//                                testFlowArea.appendText("\n");
//                                testFlowArea.appendText(XMLRead.patternBursts.get(pattternBurst).compositeToString());
//                                testFlowArea.appendText("\n");
//                                if ((XMLRead.patternBursts.get(pattternBurst).executionModeToString())!=null){
//                                    testFlowArea.appendText(XMLRead.patternBursts.get(pattternBurst).executionModeToString());
//                                    testFlowArea.appendText("\n");}
//                                if((XMLRead.patternBursts.get(pattternBurst).compareRefToString())!=null){
//                                    testFlowArea.appendText(XMLRead.patternBursts.get(pattternBurst).compareRefToString());
//                                    testFlowArea.appendText("\n");
//                                }
//                                testFlowArea.appendText("</PatternBurst>");
//                            }
//                        }
                        
                        
                        
                        
                      
//                        xmlReader.equationRootNodes.clear();
                        if(t1.getEquationsRefs()!=null){
                        
//                            if(t!=null && t.getEquationsRefs()!=null){
//                                setExpanded(xmlReader.equationRootNodes.get(t.getEquationsRefs()));
//                            }
                            System.out.println("EquationRefs: "+ t1.getEquationsRefs());
//                            //testFlowArea.appendText(t1.getEquationsRefs());
                            String[] equationsRefs =t1.getEquationsRefs().split(",");  
//                            if(equationsRefs.length==1){
//                                String oneRef= equationsRefs[0];         
//                                if(xmlReader.equations.get(oneRef) !=null){
//                                    TreeItem subRoot= xmlReader.equationRootNodes.get(oneRef);
//                                    if(subRoot!=null){
//                                        System.out.println("Skip Building Equation Tree ");
//                                        equationTree.setRoot(subRoot);
//                                        setExpanded(subRoot);
//                                    }
//                                    else{
//                                        equationTree.setRoot(xmlReader.buildEquationTree(xmlReader.equations.get(oneRef)));
//                                    }
//                                }
//                                equationTree.setShowRoot(true);                               
//                            }
//                            else{
//                                if(xmlReader.equationRootNodes.get(t1.getEquationsRefs())!=null){
//                                    System.out.println("Skip Building Equation Tree ");
//                                    equationTree.setRoot(xmlReader.equationRootNodes.get(t1.getEquationsRefs()));
//                                    equationTree.setShowRoot(false);
//                                    setExpanded(equationTree.getRoot());
//                                }
//                                else{
                                   
                                   
//                                    equationTree.setShowRoot(false);
                                   xmlReader.currentEquation.clear();
                                    for (String ref: equationsRefs){
                                        if(ref!=null){
                                            System.out.println(ref);
        //                                    testFlowArea.appendText(ref);
                                            Equation equation=xmlReader.equations.get(ref);    
                                            if(equation !=null){
                                                TreeItem item=xmlReader.buildEquationTree(equation);
                                                if(item!=null){
                                                    equationTree.getRoot().getChildren().add(item);
                                                    xmlReader.setExpanded(item);
                                                }
                                                else
                                                {   System.out.println("Error, no TreeItem is build");
                                                    
                                                }
                                            }
                                            else{
                                                System.out.println("Error this equation "+ ref +"   doesn't exist");
                                            }
                                        }
                                    }
                                    if(XMLRead.evaluationOn){
                                        t1.refreshVariables();
                                        t1.equationEvaluate();
                                        System.out.println(t1.getValidEquaitons());
                                    }
                                    
                                    
//                                }
//                            }
                            
//                            equationTree.getRoot().setExpanded(true);
            
                            
                        }
                        else{
                            
                        }

//                        testArea.setScrollTop(0);
                    }
                    else if(t1.getNodeType().equalsIgnoreCase("flow")){
                        t1.getBaseNode().printFlowNode();
//                        for(String line: t1.getBaseNode().getText()){
//                              //System.out.println(line);
//                              testArea.appendText(line+"\n");
//                        }
//                        if(t1.getEquationsRefs()!=null){
//                            //testFlowArea.appendText(t1.getEquationsRefs());
//                            String[] equationsRefs =t1.getEquationsRefs().split(",");
//                            for (String ref: equationsRefs){
//                                testFlowArea.appendText(ref);
//                                testFlowArea.appendText("\n");  
//                            }
//                        }
                    }
                    else if(t1.getNodeType().equalsIgnoreCase("entry")){
                        t1.getStartNode().printStartNode();
//                        for(String line: t1.getStartNode().getText()){
//                            //System.out.println(line);
//                                testArea.appendText(line+"\n");
//                        }
                    }
                    else if(t1.getNodeType().equalsIgnoreCase("exit")){
                        t1.getExitNode().printExitNode();
//                        for(String line: t1.getExitNode().getText()){
//                            //System.out.println(line);
//                                testArea.appendText(line+"\n");   
//                        }
                    }
                    if (t1.getNodeType().equalsIgnoreCase("device") && t1.getNodeIndex()!=-1){
                        
                        Test test=xmlReader.binningTest.get(t1.getNodeIndex());
                        if(test!=null){
//                            if(test.getPatternBurst()!=null)
//                                XMLRead.currentPatternBurstTreeItem=test.getPatternBurst();
//                            testContextTree.setRoot(test.getTreeItem());
                            testContextTree.setRoot(test.getRootItem());
                        }
                        else
                            System.out.println("No Binning Test for this Item " + t1.getDeviceNode().getName());
                        if(t1.getEquationsRefs()!=null){
                        
//                            if(t!=null && t.getEquationsRefs()!=null){
//                                setExpanded(xmlReader.equationRootNodes.get(t.getEquationsRefs()));
//                            }
                            System.out.println("EquationRefs: "+ t1.getEquationsRefs());
//                            //testFlowArea.appendText(t1.getEquationsRefs());
                            String[] equationsRefs =t1.getEquationsRefs().split(",");  
//                            if(equationsRefs.length==1){
//                                String oneRef= equationsRefs[0];         
//                                if(xmlReader.equations.get(oneRef) !=null){
//                                    TreeItem subRoot= xmlReader.equationRootNodes.get(oneRef);
//                                    if(subRoot!=null){
//                                        System.out.println("Skip Building Equation Tree ");
//                                        equationTree.setRoot(subRoot);
//                                        setExpanded(subRoot);
//                                    }
//                                    else{
//                                        equationTree.setRoot(xmlReader.buildEquationTree(xmlReader.equations.get(oneRef)));
//                                    }
//                                }
//                                equationTree.setShowRoot(true);                               
//                            }
//                            else{
//                                if(xmlReader.equationRootNodes.get(t1.getEquationsRefs())!=null){
//                                    System.out.println("Skip Building Equation Tree ");
//                                    equationTree.setRoot(xmlReader.equationRootNodes.get(t1.getEquationsRefs()));
//                                    equationTree.setShowRoot(false);
//                                    setExpanded(equationTree.getRoot());
//                                }
//                                else{
                                   
                                   
//                                    equationTree.setShowRoot(false);
                                   xmlReader.currentEquation.clear();
                                    for (String ref: equationsRefs){
                                        if(ref!=null){
                                            System.out.println(ref);
        //                                    testFlowArea.appendText(ref);
                                            Equation equation=xmlReader.equations.get(ref);    
                                            if(equation !=null){
                                                TreeItem item=xmlReader.buildEquationTree(equation);
                                                if(item!=null){
                                                    equationTree.getRoot().getChildren().add(item);
                                                    xmlReader.setExpanded(item);
                                                }
                                                else
                                                {   System.out.println("Error, no TreeItem is build");
                                                    
                                                }
                                            }
                                            else{
                                                System.out.println("Error this equation "+ ref +"   doesn't exist");
                                            }
                                        }
                                    }
//                                }
//                            }
                            
//                            equationTree.getRoot().setExpanded(true);
            
                            
                        }
                        else{
                            
                        }

//                        testArea.setScrollTop(0);
                    }
                    resetNodeInfo(t1);
//                    testArea.setScrollTop(0);
                    //testArea.selectHome();
                    if(t1.getFlowContext()!=null)
                    actionLabel.setText(t1.getFlowContext());
                 }//end if
                
            }// func
            //****************************************************************************************           
        });       
        //*********************************************************
        AnchorPane infoPane = new AnchorPane();
        //infoPane.setPrefSize(100,348);
        //infoPane.autosize();
      
        final TabPane tabPane= new TabPane();
        
        
        //tabPane.setPrefSize(300,348);
        tabPane.autosize();
        tabPane.setTabMinWidth(0);
        final Tab tab1 = new Tab("Node Details");
        Tab tab2 = new Tab("Search");
        tab1.setClosable(false);
        tab2.setClosable(false);
//        Tab tab3 = new Tab("BinningTable Auditor");
//        tab3.setClosable(false);
        
//        AnchorPane binTablePane= new AnchorPane();
//        binTablePane.autosize();
        

        accordion.autosize();
        //accordion.setExpandedPane(myTitledPane("Test"));
        
        //accordion.getPanes().addAll(myTitledPane("NodeInfo"),myTitledPane("Test"));
        
        AnchorPane nodeInfoPane = new AnchorPane();
        
        final HBox flowContextBox= new HBox();
        HBox nodeTypeBox= new HBox();
        flowContextBox.setFillHeight(true);
        flowContextBox.setLayoutY(2);
        flowContextBox.setLayoutX(2);
        flowContextBox.setAlignment(Pos.CENTER);
        //flowContextBox.setPadding(new Insets(1,10,1,1));
        
        nodeTypeBox.setAlignment(Pos.CENTER);
        //labelBox.setSpacing(10);
        nodeTypeBox.setFillHeight(true);
        nodeTypeBox.setLayoutY(27);
        myGoToTable.setLayoutY(56);
        
        flowContextBox.getChildren().addAll(flowContextLabel, flowContextField);
        nodeTypeLabel.setAlignment(Pos.BOTTOM_RIGHT);
        //nodeTypeLabel.setPrefWidth(78);
        nodeTypeLabel.setMinWidth(78);
        //nodeTypeLabel.setMaxWidth(78);
        
        nodeTypeBox.getChildren().addAll(nodeTypeLabel, nodeTypeField);
        flowContextLabel.setAlignment(Pos.BOTTOM_RIGHT);
        //flowContextLabel.setPrefWidth(78);
        flowContextLabel.setMinWidth(78);
        
        nodeTypeField.setAlignment(Pos.CENTER_LEFT);
        flowContextField.setAlignment(Pos.CENTER_LEFT);
//        System.out.println("flowContext Box MaxWidth is "+flowContextBox.getMaxWidth());
        
        flowContextBox.widthProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                myGoToTable.setPrefWidth(t.doubleValue());
                flowContextField.setPrefWidth(t1.doubleValue());
                nodeTypeField.setPrefWidth(t1.doubleValue());
                
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        
        myGoToTable.setItems(resultData);
        TableColumn result = new TableColumn("Result");
        result.setCellValueFactory(new PropertyValueFactory<Result,String>("result"));

        final TableColumn nextNode = new TableColumn("NextNode");
        nextNode.setCellValueFactory(new PropertyValueFactory<Result,String>("nextNode"));
//        nextNode.setPrefWidth(80);
//        nextNode.setMinWidth(80);

        TableColumn decision = new TableColumn("Decision");
        decision.setCellValueFactory(new PropertyValueFactory<Result,String>("decision"));

        myGoToTable.getColumns().addAll(result, nextNode, decision);
        
        final VBox vBox= new VBox();
        vBox.setAlignment(Pos.CENTER);
        
        vBox.setPadding(new Insets(1,0,1,0));
        vBox.setMinWidth(0);
        vBox.getChildren().addAll(flowContextBox,nodeTypeBox,myGoToTable);
        nodeInfoPane.heightProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                myGoToTable.setPrefHeight(t1.doubleValue()-56.0);
              
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        nodeInfoPane.widthProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                myGoToTable.setPrefWidth(t1.doubleValue());
                vBox.setPrefWidth(t1.doubleValue());
                if(t1.doubleValue()>240){
                    nextNode.setPrefWidth(t1.doubleValue()-156.0);
                }
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        nodeInfoPane.getChildren().addAll(vBox);

        titledPane.setContent(nodeInfoPane);
        titledPane.setText("Node Information");

//        final TitledPane textTitledPane= new TitledPane();
//        AnchorPane textAnchorPane = new AnchorPane();

//        textTitledPane.setContent(textAnchorPane);
//        textTitledPane.setText("Test Flow");

//        textAnchorPane.heightProperty().addListener(new ChangeListener<Number>(){
//
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                //testFlowArea.setPrefHeight(t1.doubleValue()-5);
//               textSplitPane.setPrefHeight(t1.doubleValue());
//            }
//        });
//        textAnchorPane.widthProperty().addListener(new ChangeListener<Number>(){
//
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                vBox.setPrefWidth(t1.doubleValue());
//                textSplitPane.setPrefWidth(t1.doubleValue());
//            }
//        });

//        textSplitPane.setOrientation(Orientation.VERTICAL);
       
//        textSplitPane.setDividerPositions(0.5);  
        //final TextArea testArea = new TextArea();
//        testArea.setStyle("-fx-font-size: 15;");
        
        
//        textSplitPane.getItems().addAll(testArea,testFlowArea);
        
//        testFlowArea.setStyle("-fx-font-size: 15;");
       
//        textAnchorPane.getChildren().addAll(textSplitPane);
        equationPane.setText("Equations Context Display");
        
        equationPane.setContent(equationTree);
//        equationTree.setShowRoot(false);
        testContextPane.setText("Test Context Display");
        testContextPane.setContent(testContextTree);
                            
        
        accordion.setMinWidth(0);
        accordion.getPanes().addAll(titledPane, /*textTitledPane,*/ equationPane, testContextPane);
//        accordion.setExpandedPane(textTitledPane);
                
        AnchorPane extendPane= new AnchorPane();
        extendPane.autosize();
        
//        Group searchBox = new Group();
        
//        final VBox vbox = VBoxBuilder.create().build();
        final VBox vbox = new VBox();
        vbox.getStylesheets().add(searchBoxCss);
        //vbox.setPrefWidth(800);
        vbox.setMaxWidth(Control.USE_COMPUTED_SIZE);
        vbox.getChildren().add(new SearchBox());
//        searchBox.getChildren().add(vbox);
        
        //fileListView.setStyle("-fx-background-radius: 14.4;");
        fileListView.setLayoutY(25);
        extendPane.getChildren().addAll(vbox, fileListView,searchProgressIndicator);
        extendPane.widthProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                vbox.setPrefWidth(t1.doubleValue());
                fileListView.setPrefWidth(t1.doubleValue());
                searchProgressIndicator.setPrefWidth(t1.doubleValue()/4);
                searchProgressIndicator.setLayoutX(t1.doubleValue()*3.0/8.0 );
 
            }
        });
         extendPane.heightProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                fileListView.setPrefHeight(t1.doubleValue()-30);
                 searchProgressIndicator.setPrefHeight(t1.doubleValue()/4);
                searchProgressIndicator.setLayoutY(t1.doubleValue()*3.0/8.0);
            }
        });
        
        tab2.setContent(extendPane);
        tab1.setContent(accordion);
//        tab3.setContent(binTablePane);
        
//        HBox actionHBox= new HBox();
//        Label _actionLabel= new Label("Action");
//        _actionLabel.setPrefSize(75, 25);
////        _actionLabel.setLayoutX(5);
//        actionBox.setEditable(false);
//        
////        actionBox.setSelectionModel(SingleSelectionModel);
//        
//        actionHBox.getChildren().addAll(_actionLabel, actionBox);
//       
//
//        
//        
//        final HBox ignoreHBox= new HBox();
//        Label ignoreLabel= new Label("Ignored File");
//        ignoreLabel.setPrefSize(75, 25);
//        ignoreLabel.setLayoutX(5);
//        final Button ignoreButton= new Button();
//        ignoreButton.setAlignment(Pos.CENTER_RIGHT);
//        final TextField ignoreText= new TextField();
//        ignoreHBox.getChildren().addAll(ignoreLabel, ignoreText,ignoreButton);
//        final OpenFile openIgnoredFile= new OpenFile();
//        ignoreHBox.getChildren().addAll(ignoreLabel,openIgnoredFile );
//        
//        Button startAudit= new Button("Start");
//        
//        startAudit.setDefaultButton(true);
//        startAudit.setPrefWidth(100);
//        startAudit.setPrefHeight(25);
//        startAudit.setAlignment(Pos.CENTER);
        
//        startAudit.setOnAction(new EventHandler<ActionEvent>(){
//
//            @Override
//            public void handle(ActionEvent t) {
//                actionIndex=-1;
//                
//                if(actionBox.getValue()!=null && actionBox.getValue().toString().split(";")[1]!=null){               
//                    for(ActionList action :xmlReader.actionList){
//                        actionIndex=actionIndex+1;
//                        if(action.getActionName().equals(actionBox.getValue().toString().split(";")[0])){
//                            System.out.println("action is " + action.getFlowRef());
//                            break;
//                        }
//                    }
////                    String fileName=null;
//                    if(openIgnoredFile.getText()!="" && openIgnoredFile.getText()!=null){
//                        try {
//                            File file= new File(openIgnoredFile.getText());
//                            if(file.exists()){
//                                ignoredFileName=file.getCanonicalPath();
////                                binningAuditTask.start();
////                                xmlReader.auditBinTables(xmlReader.actionTree.get(actionIndex), actionIndex,ignoredFileName );
//                            }
//                            else
//                                ignoredFileName=null;
//                        } catch (IOException ex) {
//                            Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                    else
//                        ignoredFileName=null;
//                        
//                    if(xmlReader.firstAudit){
////                        xmlReader.auditBinTables(xmlReader.actionTree.get(actionIndex), actionIndex,ignoredFileName);
//                        // for fast debugging
//                        binningAuditTask.start();
//                    }
//                    else
//                        binningAuditTask.restart();
////                        xmlReader.auditBinTables(xmlReader.actionTree.get(actionIndex), actionIndex,ignoredFileName);
////                        System.out.println("Failed to launch Binning Manager");
//                    
//                
//                }
//            }
//        });
      
        
//        VBox binTableVBox = new VBox();
//        binTableVBox.setAlignment(Pos.CENTER);
//        
//        binTableVBox.getChildren().addAll(actionHBox, ignoreHBox,startAudit);
//        binTableVBox.setLayoutX(4);
//        binTablePane.getChildren().addAll(binTableVBox,binningAuditProgressIndicator);
//        
//        
//        binTablePane.widthProperty().addListener(new ChangeListener<Number>(){
//
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                actionBox.setPrefWidth(t1.doubleValue()-78);
////                ignoreText.setPrefWidth(t1.doubleValue()-78);
////                ignoreButton.setAlignment(Pos.CENTER_RIGHT);
//                openIgnoredFile.setPrefWidth(t1.doubleValue()-78);
//                binningAuditProgressIndicator.setPrefWidth(t1.doubleValue()/4);
//                binningAuditProgressIndicator.setLayoutX(t1.doubleValue()*3.0/8.0 );
//                
////                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
//        
//        binTablePane.heightProperty().addListener(new ChangeListener<Number>(){
//
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                binningAuditProgressIndicator.setPrefHeight(t1.doubleValue()/4);
//                binningAuditProgressIndicator.setLayoutY(t1.doubleValue()*3.0/8.0);
//            }
//        });
        
        
        tabPane.getTabs().addAll(tab1,tab2/*,tab3*/);
        infoPane.setMinWidth(0);
        infoPane.getChildren().add(tabPane);

        infoPane.widthProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                
                tabPane.setPrefWidth(t1.doubleValue());
                
                
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        infoPane.heightProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                tabPane.setPrefHeight(t1.doubleValue());
                
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        //******************************************************************
        
        
        
        splitPane.autosize();
        splitPane.getItems().addAll(actionPane, mainPane, infoPane);                                                                                                                                                                                                                                                                                                                                                                                                                                
        splitPane.setDividerPositions(0.3,0.6);
 
        //***************************************************************
        
        root.setCenter(splitPane);

        final Scene scene = new Scene(root);
        
        scene.setOnDragDetected(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent t) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                Dragboard board= scene.startDragAndDrop(TransferMode.ANY);
//                System.out.println("drag detected");
            }

 
        });
        scene.setOnDragOver(new EventHandler<DragEvent>(){

            @Override
            public void handle(DragEvent t) {
               if(t.getDragboard().hasFiles()){
                   t.acceptTransferModes(TransferMode.LINK);
               }
            }
        });
        scene.setOnDragDropped(new EventHandler<DragEvent>(){

            @Override
            public void handle(DragEvent t) {
                Dragboard board= t.getDragboard();
                if(board.hasFiles()){
//                    System.out.println("Files Dectected  " );
                    ProjectPath=board.getFiles().get(0);
//                      binningAuditProgressIndicator.setVisible(true);
                    if(prorgamLoaded.getValue()==false)
                        loadProgram();
//                      binningAuditProgressIndicator.setVisible(false);
                }
            }
        });
        primaryStage.setTitle("Sapphire Program Reader");
        
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.getIcons().add(new Image("file:config/ProgramReaderLogo.png",60.0,60.0,true,true));
//        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("ProgramReaderLogo.png")));
    }
    
//    @Override
//    public void stop() throws Exception{
//        File folder= new File("config");
//        if(folder.exists() && folder.isDirectory()){
//            File[] fileList = folder.listFiles();
//            for(File file: fileList){
//                if(file.getName().startsWith("search_"))
//                    try{
//                        file.delete();
//                    }catch(Exception e){}
//            }
//        }
//        
//        super.stop();
//    }
    
    public void printTestFlowArea(TestItem test){
            if(!test.isIsLeaf()){
                if (test.getExpression()!=null){
//                    testFlowArea.appendText(test.getSpace() + "<" + test.getName() + " "+ test.getAttriName() +"=\"" + test.getExpression() + "\">" + "\n"); //"\n"
                    for(Test.TestItem item: test.getSubItems()){
                        printTestFlowArea(item);
                    }
//                    testFlowArea.appendText(test.getSpace()+"</"+ test.getName() +">" + "\n");
                }
                else{
//                    testFlowArea.appendText(test.getSpace()+"<"+ test.getName() +">" +"\n");
                    for(Test.TestItem item: test.getSubItems()){
                        printTestFlowArea(item);
                    }
//                    testFlowArea.appendText(test.getSpace()+"</"+ test.getName() +">" +"\n");
                }
            }
            else{
//                testFlowArea.appendText(test.getSpace()+"<"+ test.getName() + ">"+ test.getExpression()+ "</" + test.getName() +">" +"\n");
            }
        
        }
    public void setExpanded(TreeItem treeItem){
        if(treeItem.isExpanded()) treeItem.setExpanded(false);
        for(int i=0; i!= treeItem.getChildren().size();i++){
            TreeItem item= (TreeItem) treeItem.getChildren().get(i);
            if(item.isExpanded()){ 
                item.setExpanded(false);
                System.out.println(item.getValue().toString() + " is set un expanded");
            }
            setExpanded(item);
        }
    }
    public void loadProgram(){
//        System.out.println(ProjectPath.getAbsolutePath());
        //clear test and flow node info  
        prorgamLoaded.setValue(false);
//        System.out.println("start to true");
        flowContextField.clear();
        nodeTypeField.clear();
        resultData.clear();
//        testArea.clear();
//        testFlowArea.clear();
        xmlReader.TestProgramFile="";
        
        programNameField.clear();
        if(fileListView.getItems()!=null) fileListView.getItems().clear();
        fileListView.setItems(null);
        if(flowTree.getRoot()!=null){
            if(flowTree.getRoot().getChildren()!=null){
                flowTree.getRoot().getChildren().clear();
            }
            flowTree.setRoot(null);
        }
            
        
        programNameField.setText(ProjectPath.getAbsolutePath());
//        firstSearch=true;
        
       
        try {
            xmlReader.init();
            xmlReader.getUPA(ProjectPath);
            xmlReader.programName=ProjectPath.getCanonicalPath();
            xmlReader.LoadFile(ProjectPath,programExplorerRoot);
//            xmlReader.LoadFile(ProjectPath);
            xmlReader.reReadAll();
            xmlReader.buildActionTree();
//            xmlReader.printPatternBurst();
//            xmlReader.printActionList();
//            xmlReader.printFlowTables();
//            xmlReader.printEquations();
//            xmlReader.printLevel();
//            xmlReader.printTiming();
//            xmlReader.printLoadboards();
//            xmlReader.printTests();
//            xmlReader.printTestDescription();
//            xmlReader.printResultSpec();
//            xmlReader.printSoftSet();
//            xmlReader.printSoftSetGroup();
//              xmlReader.printFlowOverride();
        } catch (DocumentException ex) {
            Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println(xmlReader.TestProgramFile);
        TreeItem<String> rootAction = new TreeItem<>("Actions");
        for(ActionList list: xmlReader.actionList){
            TreeItem<String>  item = new TreeItem<>(list.getActionName());
            rootAction.getChildren().add(item);               
        }
        actionTree.setRoot(rootAction);
        actionTree.setShowRoot(false);
        prorgamLoaded.setValue(true);
        openItem.setDisable(true);
        recentItem.setDisable(true);
        
        
        
        programExplorerView.setRoot(programExplorerRoot);

        xmlReader.readRecentFile(false, ProjectPath.getAbsolutePath());
        
//        for(ActionList action: xmlReader.actionList){
//            if(action.getType().equals("Device"))
//                actionBox.getItems().add(action.getActionName() + ";" + action.getFlowRef() );                
//        }
        
        
        
        List<String> skip= new ArrayList<>();
//        for(Equation equ: xmlReader.equations.values()){
//            
//            if(!equ.getIsUsed()){
////                System.out.println("This Equation " + equ.getName() + " is not used");
//                skip.add(equ.getName());
//            }
//        }
//        
//        for(String name: skip){
//            xmlReader.equations.remove(name);               
//        }
        
//        for(Test test: XMLRead.newTests.values()){
//            
//            if(!test.isIsUsed()){
////                System.out.println("This Equation " + equ.getName() + " is not used");
//                skip.add(test.getRoot().getName());
//            }
//        }
//        
//        for(String name: skip){
//            XMLRead.newTests.remove(name);               
//        }
        

        
//        for(int i=0; i!=xmlReader.flowTables.size();i++){
//            if(i<xmlReader.flowTables.size()){
//                if(!xmlReader.flowTables.get(i).getIsUsed()){
////                    System.out.println("This flow " + xmlReader.flowTables.get(i).getFlowName() + " is not used");
//                }
//            }
//            else
//                break;
//                 
//        }

        
        
        
    }
    public static class Result{
        private  SimpleStringProperty result;
        private  SimpleStringProperty nextNode;
        private  SimpleStringProperty decision;
        
        private Result(GoToResult goToResult){
            this.result= new SimpleStringProperty(goToResult.getReuslt());
            this.nextNode= new SimpleStringProperty(goToResult.getNodeRef());
            this.decision= new SimpleStringProperty(goToResult.getDecision());
        }
        public void setResult(String result){
            this.result.set(result);
        }

        public String getResult() {
            return this.result.get();
        }
        
        public void setNextNode(String node){
            this.nextNode.set(node);
        }
        public String getNextNode(){
            return this.nextNode.get();
        }
        public void setDecision(String decision){
            this.decision.set(decision);
        }
        public String getDecision(){
            return this.decision.get();
        }
        public void printResult(){
            System.out.println(this.getResult());
            System.out.println(this.getNextNode());
            System.out.println(this.getDecision());
        }

    }
    public void resetNodeInfo(TreeNode node){
        flowContextField.clear();
        flowContextField.setText(node.getFlowContext());
        
        nodeTypeField.clear();
        nodeTypeField.setText(node.getNodeType());
        updateGoToResultTable(node);
        
    }
    public void updateGoToResultTable(TreeNode node){
        
        // test or flow node  
        resultData.clear();
        if (node.getNodeType().equalsIgnoreCase("test")||node.getNodeType().equalsIgnoreCase("flow")){
             for( GoToResult result :node.getBaseNode().getGoToResult()){
                 resultData.add(new Result(result));
             }
        }
        else if (node.getNodeType().equalsIgnoreCase("entry")){
            for( GoToResult result :node.getStartNode().getResult()){
                 resultData.add(new Result(result));
             } 
        }
        else if(node.getNodeType().equalsIgnoreCase("device")){
            for( GoToResult result :node.getDeviceNode().getGotoResult()){
                 resultData.add(new Result(result));
             } 
        }
//        for(Result result:resultData){
//            result.printResult();
//        }
        
        
     
    } 
    /*
    public void editBat( String fileName){
        
        
        String file= new File("config/openXML.bat").getAbsolutePath();
        PrintWriter printWriter =null;

        if ( printWriter == null) {
            try {
                printWriter = new PrintWriter(new FileWriter(file, false),true);             
            } catch (IOException e) { 
                System.out.println ("ERROR: Unable to open file");
                printWriter=null;
            }       
        }
        if ( printWriter != null) {
            printWriter.print("\"");
            printWriter.print(xmlReader.notePadPath);
            printWriter.print("\"");
            printWriter.print(" \"");
            printWriter.print(fileName);
            printWriter.print("\"");
            printWriter.close();
        }
        
    }
    public void runBat(String batName) {
        try {
            Process ps = Runtime.getRuntime().exec(batName);
//            InputStream in = ps.getInputStream();
//            int c;
//            while ((c = in.read()) != -1) {
//                //System.out.print(c);
//            }
//            in.close();
//            ps.waitFor();

        } catch (IOException ioe) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
        }
//        System.out.println("child thread done");
     }
     */
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    private final class TextFieldTreeCellImpl extends TreeCell<String> {
        private boolean isUpdate=false;
//        private TextField textField;
        private final ContextMenu addMenu = new ContextMenu();
        final MenuItem openSource;
        private MenuItem copyFlowContext = new MenuItem("Copy FlowContext to Clipboard");
        final MenuItem printPatternBurst = new MenuItem("Print Pattern Burst");
        private MenuItem flowOverride =new MenuItem();
        
        
       
        public TextFieldTreeCellImpl() {
            super.setStyle("-fx-indent:25");
            openSource= new MenuItem();
            final MenuItem openTest = new MenuItem("Open Test File");
            
            
            super.setOnMouseClicked(new EventHandler<MouseEvent>(){

                        @Override
                        public void handle(MouseEvent t) {
                            if (t.getButton().equals(MouseButton.SECONDARY)){
//                                System.out.println("Right mouse click");
                                //int i = flowTree.getSelectionModel().getSelectedIndex();
                                //System.out.println(flowTree.getRow(getTreeItem()));
                                int nodeIndex= flowTree.getRow(getTreeItem());
                                flowTree.getSelectionModel().clearAndSelect(nodeIndex);
                                TreeNode node = (TreeNode) getTreeItem();
                                if (node!=null){

                                    if(node.isOverRide()){
                                        System.out.println("this node " + node.getFlowContext() + " is override during node update");
                                        setTextFill(Color.RED);
                                        flowOverride.setText("FlowOverride: " + node.getOverRideString() );
                                        flowOverride.setDisable(false);
                                    }
                                    else
                                    {
                                        flowOverride.setDisable(true);
                                        
                                        flowOverride.setText("");
                                        
                                    }
                                    if (node.getNodeType().equalsIgnoreCase("test")){
                                        if(node.getBaseNode().isTestIsReady()){
                                            openTest.setDisable(false);
                                            openSource.setText("Open_SoureFile: " + XMLRead.newTests.get(node.getTestFlowRef()).getExecName());
    //                                        System.out.println("click is " + xmlReader.tests.get(node.getNodeIndex()).getExecName());
                                            if(XMLRead.newTests.get(node.getTestFlowRef()).getSourceFile()==null){
                                                openSource.setDisable(true);
//                                                openSource.setText("");
                                            }
                                            else
                                                openSource.setDisable(false);
                                        }
                                        else{
                                            openTest.setDisable(true);
                                            openSource.setDisable(true);
                                        }
                                    }
                                    else{
                                        openTest.setDisable(true);
                                        openSource.setDisable(true);
                                        openSource.setText("");
                                    }
                                }
                                else{
                                    System.out.println("Error, No Tree Item");
                                }
                                
    //                            if (i!=nodeIndex)
    //                                System.out.println("i is  " + i +"            : Index is  " + nodeIndex);
                                
                            
                                
                            }
                            else if(t.getButton().equals(MouseButton.PRIMARY)){
                                int nodeIndex= flowTree.getRow(getTreeItem());
                                flowTree.getSelectionModel().clearAndSelect(nodeIndex);
//                                TreeNode node = (TreeNode) getTreeItem();
//                                System.out.println("Right click on " + node.getFlowContext());
                                
                                
                                
                            }
                        }
                    });
            MenuItem openFlow = new MenuItem("Open Flow File");
            
            
            copyFlowContext.setOnAction(new EventHandler() {
                @Override
                public void handle(Event t) {
                    TreeNode node = (TreeNode) getTreeItem();
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    ClipboardContent content = new ClipboardContent();
                    content.putString(node.getFlowContext());
                    clipboard.clear();
                    clipboard.setContent(content);
                }
            });
            openFlow.setOnAction(new EventHandler() {
                @Override
                public void handle(Event t) {
//                    System.out.println("on action item " + getTreeItem().getValue());
                    TreeNode node = (TreeNode) getTreeItem();
                    if(node.getNodeType().equalsIgnoreCase("flow")){
                            String fileName= node.getBaseNode().getXmlFileName();
//                            System.out.println("open flow file " + fileName);
//                            programNameField.setText("open flow file " + fileName);
                            if(XMLRead.notePadPath.toLowerCase().contains("gvim"))
                                XMLRead.editBat(fileName,node.getBaseNode().getName()); 
                            else
                                XMLRead.editBat(fileName);
                            String batFile=new File("config/openXML.bat").getAbsolutePath();
//                            XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                            try {
                            Process ps = Runtime.getRuntime().exec(batFile);
//                            programNameField.setText("open flow file " + fileName +" pass");
//                            InputStream in = ps.getInputStream();
//                            int c;
//                            while ((c = in.read()) != -1) {
//                                //System.out.print(c);
//                            }
//                            in.close();
//                                try {
//                                    ps.waitFor();
//                                } catch (InterruptedException ex) {
//                                    Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
//                                }

                        } catch (IOException e) {
//                            programNameField.setText("open flow file " + fileName +" failed" + e.toString() +" " +e.getStackTrace().toString() +" "+e.getMessage());
                        }
                            
                    }
                    else if(node.getNodeType().equalsIgnoreCase("test")){
                        String fileName= node.getBaseNode().getXmlFileName();
//                            System.out.println("open flow file " + fileName);
//                            programNameField.setText("open flow file " + fileName);
                            if(XMLRead.notePadPath.toLowerCase().contains("gvim"))
                                XMLRead.editBat(fileName,node.getBaseNode().getName()); 
                            else
                                XMLRead.editBat(fileName);
                            String batFile= new File("config/openXML.bat").getAbsolutePath();
//                            XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                            try {
                            Process ps = Runtime.getRuntime().exec(batFile);
//                            programNameField.setText("open flow file " + fileName +" pass");
//                            InputStream in = ps.getInputStream();
//                            int c;
//                            while ((c = in.read()) != -1) {
//                                //System.out.print(c);
//                            }
//                            in.close();
//                            ps.waitFor();

                        } catch (IOException e) {
//                            programNameField.setText("open flow file " + fileName +" failed" + e.toString() +" " +e.getStackTrace().toString() +" "+e.getMessage());
                        } 
//                            catch (InterruptedException ex) {
//                            Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                        
                    }
                }
            });
            openTest.setOnAction(new EventHandler() {
                @Override
                public void handle(Event t) {
//                    System.out.println("on action item " + getTreeItem().getValue());
                    TreeNode node = (TreeNode) getTreeItem();
                    if(node.getNodeType().equalsIgnoreCase("test")){
                            String fileName= XMLRead.newTests.get(node.getTestFlowRef()).getFileName();
//                            System.out.println(fileName);
                            if(XMLRead.notePadPath.toLowerCase().contains("gvim"))
                                XMLRead.editBat(fileName, node.getTestFlowRef());   
                            else
                                XMLRead.editBat(fileName);
                        try {
                            XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            openSource.setOnAction(new EventHandler() {
                @Override
                public void handle(Event t) {
                    TreeNode node = (TreeNode) getTreeItem();
                    if(node.getNodeType().equalsIgnoreCase("test")){
                        XMLRead.editBat(XMLRead.newTests.get(node.getTestFlowRef()).getSourceFile());   
                        try {
                            XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            flowOverride.setOnAction(new EventHandler() {

                @Override
                public void handle(Event t) {
                    TreeNode node = (TreeNode) getTreeItem();
                    if(node.getFlowOverrideFile()!=null){
                        
                        String equationFile=null;
                        if(XMLRead.equations.get(node.getOverRideString().split("\\.")[0])!=null)
                            equationFile= XMLRead.equations.get(node.getOverRideString().split("\\.")[0]).getFileName();
                        if(equationFile!=null){
                            if(XMLRead.notePadPath.toLowerCase().contains("gvim")){ 
//                                System.out.println(node.getOverRideString().split("\\.")[0]+"\""+ ">");
                                    XMLRead.editBat(equationFile,  node.getOverRideString().split("\\.")[0]);  
                            }
                            
                            else
                                XMLRead.editBat(equationFile);
                            try {
                                XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                            } catch (InterruptedException ex) {
                                Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
  
                    
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
                addMenu.getItems().addAll(openFlow, openTest, openSource,copyFlowContext,flowOverride);
      
        }

         
        public int getNodeIndex(){
            TreeNode node= (TreeNode) super.getTreeItem();
            return node.getNodeIndex();
        }

        @Override
        public void updateItem(String item, boolean empty) {
            
                
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } 
                else {
                    if (isEditing()) {} 
                    else {

                        setText(getString());
                        setGraphic(getTreeItem().getGraphic());
    //                    TreeNode node = (TreeNode) getTreeItem();
    //                    if(node.getNodeType().equals("test"))
    //                        setGraphic(new ImageView(new Image(getClass().getResourceAsStream("run_test.gif"))));
    //                    else if(node.getNodeType().equals("flow"))
    //                        setGraphic(new ImageView(new Image(getClass().getResourceAsStream("testflow_tm.png"))));
                        setContextMenu(addMenu);
                        TreeNode node = (TreeNode) getTreeItem();
//                        if(node!=null && node.getNodeType().equals("Test")){
                        
                            if(node.isOverRide()){
//                                System.out.println("this node " + node.getFlowContext() + " is override during node update");
                                setTextFill(Color.RED);
                            }
                            else{
                                setTextFill(Color.BLACK);
                            }
                        
                            
                    }
                
            }
        }

 
        private String getString() {
            return getItem() == null ? "" : getItem();
        }
     }
    
    private final class TextFieldTreeCell extends TreeCell<String> {
 

//        private ContextMenu addMenu = new ContextMenu();
//        final MenuItem openSource;
//        private MenuItem copyFlowContext = new MenuItem("Copy FlowContext to Clipboard");
//        
        

        
        public TextFieldTreeCell() {
            super.setEditable(false);
            super.setStyle("-fx-indent:25");
            super.setOnMouseClicked(new EventHandler<MouseEvent>(){

                        @Override
                        public void handle(MouseEvent t) {
                            if (t.getClickCount()==2){
//                                System.out.println("Right mouse click");
                                //int i = flowTree.getSelectionModel().getSelectedIndex();
                                //System.out.println(flowTree.getRow(getTreeItem()));
                                int nodeIndex= programExplorerView.getRow(getTreeItem());
//                                programExplorerView.getSelectionModel().clearAndSelect(nodeIndex);
                                TreeItem node = getTreeItem();
                                
//                                System.out.println("Double click on " + (node!=null?node.getValue():"nothing"));
                                if(node!=null&&node.isLeaf()){
//                                    String fileName=ProjectPath.getAbsolutePath();
                                    String fileName="";
                                    while((node!=null) && !node.toString().substring(18, node.toString().length()-2).equals("null")){
                                        String temp= node.toString().substring(18, node.toString().length()-2);
//                                        System.out.println("temp is "+ temp);
                                        if((temp!=null)&& (!temp.equals("null")))
                                            fileName="\\"+ temp+fileName;
//                                        System.out.println(node.getValue().toString());
//                                        System.out.println(node);
                                        node=node.getParent();
                                    }
                                    fileName=ProjectPath.getAbsolutePath()+fileName;
                                    System.out.println("Click on " + fileName);
                                    
                                    File file=new File(fileName);
                                    if(file.exists()){
                                        XMLRead.editBat(fileName);                          
                                        try {
                                            XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(SapphireProgramReader.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    
                                
                                }
//                                if (node!=null){
//                                    if (node.getNodeType().equalsIgnoreCase("test")){
//                                        if(node.getBaseNode().isTestIsReady()){
//                                            openTest.setDisable(false);
//                                            openSource.setText("Open_SoureFile: " + XMLRead.newTests.get(node.getTestFlowRef()).getExecName());
//    //                                        System.out.println("click is " + xmlReader.tests.get(node.getNodeIndex()).getExecName());
//                                            if(XMLRead.newTests.get(node.getTestFlowRef()).getSourceFile()==null){
//                                                openSource.setDisable(true);
//                                            }
//                                        }
//                                        else{
//                                            openTest.setDisable(true);
//                                            openSource.setDisable(true);
//                                        }
//                                    }
//                                    else{
//                                        openTest.setDisable(true);
//                                        openSource.setDisable(true);
//                                        openSource.setText("");
//                                    }
//                                }
//                                else{
//                                    System.out.println("Error, No Tree Item");
//                                }
                                
    //                            if (i!=nodeIndex)
    //                                System.out.println("i is  " + i +"            : Index is  " + nodeIndex);
                                
                            
                                
                            }
                        }
                    });
//            MenuItem openFlow = new MenuItem("Open Flow File");
            
            
//            copyFlowContext.setOnAction(new EventHandler() {
//                @Override
//                public void handle(Event t) {
//                    TreeNode node = (TreeNode) getTreeItem();
//                    Clipboard clipboard = Clipboard.getSystemClipboard();
//                    ClipboardContent content = new ClipboardContent();
//                    content.putString(node.getFlowContext());
//                    clipboard.clear();
//                    clipboard.setContent(content);
//                }
//            });
//            openFlow.setOnAction(new EventHandler() {
//                @Override
//                public void handle(Event t) {
////                    System.out.println("on action item " + getTreeItem().getValue());
//                    TreeNode node = (TreeNode) getTreeItem();
//                    if(node.getNodeType().equalsIgnoreCase("test")||node.getNodeType().equalsIgnoreCase("flow")){
//                            String fileName= node.getBaseNode().getXmlFileName();
//                            System.out.println("open flow file " + fileName);
//                            programNameField.setText("open flow file " + fileName);
//                            XMLRead.editBat(fileName); 
//                            String batFile=new File("config/openXML.bat").getAbsolutePath();
////                            XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
//                            try {
//                            Process ps = Runtime.getRuntime().exec(batFile);
////                            programNameField.setText("open flow file " + fileName +" pass");
//                //            InputStream in = ps.getInputStream();
//                //            int c;
//                //            while ((c = in.read()) != -1) {
//                //                //System.out.print(c);
//                //            }
//                //            in.close();
//                //            ps.waitFor();
//
//                        } catch (IOException e) {
////                            programNameField.setText("open flow file " + fileName +" failed" + e.toString() +" " +e.getStackTrace().toString() +" "+e.getMessage());
//                        }
//                            
//                    }
//                }
//            });
//            openTest.setOnAction(new EventHandler() {
//                @Override
//                public void handle(Event t) {
////                    System.out.println("on action item " + getTreeItem().getValue());
//                    TreeNode node = (TreeNode) getTreeItem();
//                    if(node.getNodeType().equalsIgnoreCase("test")||node.getNodeType().equalsIgnoreCase("flow")){
//                            String fileName= XMLRead.newTests.get(node.getTestFlowRef()).getFileName();
////                            System.out.println(fileName);
//                            XMLRead.editBat(fileName);                          
//                            XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
//                    }
//                }
//            });
//            openSource.setOnAction(new EventHandler() {
//                @Override
//                public void handle(Event t) {
//                    TreeNode node = (TreeNode) getTreeItem();
//                    if(node.getNodeType().equalsIgnoreCase("test")){
//                        XMLRead.editBat(XMLRead.newTests.get(node.getTestFlowRef()).getSourceFile());   
//                        XMLRead.runBat(new File("config/openXML.bat").getAbsolutePath());
//                    }
//                }
//            });
          
//                addMenu.getItems().addAll(openFlow, openTest, openSource,copyFlowContext);
      
        }

         
//        public int getNodeIndex(){
//            TreeItem node= super.getTreeItem();
//            return node.getNodeIndex();
//        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } 
            else {
                setText(getString());
//                    setText(super.getText());
                    
//                    setContextMenu(addMenu);
                   
                }
            }
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
    private static class SearchBox extends Region {

        private TextField textBox;
        private Button clearButton;
        private Button deepSearchButton;
        public String getText(){
            return textBox.getText();
        }
        public SearchBox() {
//            this.textBox.setTooltip(new Tooltip("Case sensitive and must be the full content of the element name, attribute or value "));
            setId("SearchBox");
            getStyleClass().add("search-box");
            setMinHeight(24);
            setPrefSize(200, 24);
            setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            textBox = new TextField();
            textBox.setPromptText("Search, Case sensitive");
            this.textBox.setTooltip(new Tooltip("Must be the full element attribute or value "));
            clearButton = new Button();
            clearButton.setVisible(false);
            clearButton.setId("clearButton");
            
            deepSearchButton= new Button();
            deepSearchButton.setText("Go");
            getChildren().addAll(textBox, clearButton,deepSearchButton);
            deepSearchButton.setOnAction(new EventHandler<ActionEvent>() {                
                @Override public void handle(ActionEvent actionEvent) {
                    searchContent=textBox.getText().trim();
                    if((searchContent!="")&&(prorgamLoaded.getValue())&&(textBox.getText().trim().length()!=0)){
                        deepSearch=true;
                        if (startSearch.getValue()){
                            startSearch.set(false);
                        }
                        else
                            startSearch.set(true);
                    }
                }
            });
            clearButton.setOnAction(new EventHandler<ActionEvent>() {                
                @Override public void handle(ActionEvent actionEvent) {
                    textBox.setText("");
                    textBox.requestFocus();
                }
            });
            textBox.textProperty().addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    clearButton.setVisible(textBox.getText().length() != 0);
                }
            });
            textBox.setOnKeyReleased(new EventHandler<KeyEvent>(){

                @Override
                public void handle(KeyEvent t) {
                    if((t.getCode().equals(KeyCode.ENTER))&&(prorgamLoaded.getValue())&&(textBox.getText().trim().length()!=0)){
                        searchContent=textBox.getText().trim();
                        deepSearch=false;
                        if (startSearch.getValue()){
                            
                            startSearch.set(false);
                        }
                        else
                            startSearch.set(true);
                    }
                }
            });
        }

        @Override
        protected void layoutChildren() {
            textBox.resize(getWidth()-50, getHeight());
            clearButton.resizeRelocate(getWidth() - 68, 6, 12, 13);//x-y-width-height
            deepSearchButton.resizeRelocate(getWidth() - 50, 0, 50, getHeight());
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Start main");
        launch(args);
        
    }

}
