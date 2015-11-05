/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader.blockreader;

import sapphireprogramreader.ui.controls.TestNodeCell_Exec;
import sapphireprogramreader.ui.controls.TestNodeCell_Label;
import sapphireprogramreader.ui.controls.TestNodeCell_Label_2Text_Button;
import sapphireprogramreader.ui.controls.TestNodeCell_Label_Text;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TreeItem;
import org.dom4j.Attribute;
import org.dom4j.Element;
import sapphireprogramreader.xmlreader.XMLRead;

/**
 *
 * @author ghfan
 */
public class Test {
    
    private TestItem root;
    private String fileName=null;
    private TreeItem temp;
    private TreeItem temp1;
    public boolean treeIsReady=false;
    private TreeItem rootItem;
    private String patternBurst=null;
    private String levels=null;
    private String timing=null;
    private String loadBoard=null;
    private TreeItem levelRoot=new TreeItem();
    private TreeItem timingRoot= new TreeItem();
    private TreeItem loadBoardRoot=new TreeItem();
    private List<String> equationRef=new ArrayList();
    private String execName=null;
    private String sourceFile=null;
//    private boolean isUsed=false;
    private boolean udpateDone=false;
    private boolean used=false;
    private int testNo=0;
    private List<DCMeasurement> measurement= new ArrayList();
    private boolean findMeasure=false;
    private List<String> variables= new ArrayList<>();


    

    public Test(Element element, String fileName, int testNo) {
        root= new TestItem(element,0);
//        System.out.println(root.getExpression());
        this.fileName=fileName;
        this.testNo=testNo;
    }
    

    public TreeItem getRootItem() {
        if (treeIsReady){
            System.out.println("Test Tree " + this.root.expression + " is already existing");  
            if(this.patternBurst!=null){
                TreeItem item  = XMLRead.patBurstRootItems.get(this.patternBurst);
                if (item!=null){
                    item.getChildren().clear();
                }
                else{
                    System.out.println("Error empty PatternBurst TreeItem " + this.patternBurst);
                }
                
            }
            if(this.levels!=null){
//                TreeItem item  = XMLRead.timing_level.get(this.levels);
                if (this.levelRoot!=null){
                    this.levelRoot.getChildren().clear();
                }
                else{
                    System.out.println("Error empty Levels TreeItem " + this.levels);
                }
            
            }
            if(this.timing!=null){
//                TreeItem item  = XMLRead.timing_level.get(this.timing);
                if (this.timingRoot!=null){
                    this.timingRoot.getChildren().clear();
                    System.out.println("Clear Timing TreeItem " + this.timing);
                }
                else{
                    System.out.println("Error empty Timing TreeItem " + this.timing);
                }
            }
   
            setExpanded(rootItem);
//            System.out.println("Test "+ this.root.getExpression() + " is set unexpanded");
            return rootItem;
        }
        else{
            getTreeItem();
            return rootItem;
        }   
    }
    
    public void print(PrintWriter printWriter){
        root.print(printWriter);
    }
    
    public boolean search(String content){
        return root.search(content);
    }
    
    

    public List<String> getEquationRef() {
            return equationRef;
       
    }
    

    public TestItem getRoot() {
        return root;
    }
    public int getTestNo() {
        return testNo;
    }
    public String getPatternBurst() {
        return patternBurst;
    }
    

    public boolean isTreeIsReady() {
        return treeIsReady;
    }
    
    // Scan and Bist test need to update the evaluated values from its equation ref variables
    public void updateTestTree(){
        String[] label_text=null;
        String label=null;
        String text=null;
        System.out.println("test exec name is " + this.execName);
        if(!this.udpateDone){
            if(this.execName.endsWith("/Scan")||this.execName.endsWith(".Scan")){
                for(int i=0; i!= rootItem.getChildren().size();i++){

                    TreeItem item= (TreeItem) rootItem.getChildren().get(i);
                                        System.out.println("getValue().toString() equals " +item.getValue().toString());
    //                    System.out.println("item.toString() equals " +item.toString());

                    if(!item.isLeaf() && item.getValue().toString().equals("Param")){   

                        for(int j=0; j!=item.getChildren().size();j++){

                            TreeItem scanItem=(TreeItem) item.getChildren().get(j);
    //                        System.out.println("Scanitem.toString() equals " +scanItem.toString());
    //                        System.out.println("Scanitem.getValue().toString() equals " +scanItem.getValue().toString());
                            TestNodeCell_Label_Text labelCell=(TestNodeCell_Label_Text) scanItem.getValue();
                            label_text=scanItem.getValue().toString().split(":");
                            System.out.println("label_text is " + label_text[0] + label_text[1]);

                            if(label_text.length==2){                           
                                label= label_text[0];
                                text=label_text[1];
                                if(!text.contains("'")){
                                    System.out.println("label is " + label);
                                    switch (label){
                                        case "DownComponentOnFail":
                                            updateLabelCell(labelCell,text);
                                            break;
                                        case "ScanMode":
                                            updateLabelCell(labelCell,text);
                                            break;
                                        case "Iterations":
                                            updateLabelCell(labelCell,text);
                                            break;
                                        case "Vdd":
                                            updateLabelCell(labelCell,text);
                                            break;
                                        case "Fmax":
                                            updateLabelCell(labelCell,text);
                                            break;
                                        default:
                                    }
                                }   
                            }

                        }
    //                   
    //                    System.out.println("getValue().toString() equals " +item.getValue().toString());
    //                    System.out.println("item.toString() equals " +item.toString());
                    }
                }

            }
            else if(this.execName.endsWith("/Bist")||this.execName.endsWith(".Bist")){
                for(int i=0; i!= rootItem.getChildren().size();i++){

                    TreeItem item= (TreeItem) rootItem.getChildren().get(i);
                                        System.out.println("getValue().toString() equals " +item.getValue().toString());
    //                    System.out.println("item.toString() equals " +item.toString());

                    if(!item.isLeaf() && item.getValue().toString().equals("TestParameters")){
                        TestNodeCell_Label_Text labelCell;

                        for(int j=0; j!=item.getChildren().size();j++){

                            TreeItem scanItem=(TreeItem) item.getChildren().get(j);
                            if(!scanItem.isLeaf())
                                continue;
    //                        System.out.println("Scanitem.toString() equals " +scanItem.toString());
    //                        System.out.println("Scanitem.getValue().toString() equals " +scanItem.getValue().toString());

                            label= scanItem.getValue().toString() ;
                            if(label.contains(":")){
                                System.out.println("label is " + label);
                                label_text=scanItem.getValue().toString().split(":");

                                if(label_text.length==2){                           
                                    label= label_text[0];
                                    text=label_text[1];
                                    if(!text.contains("'")){                      
                                        switch (label){
                                            case "VddNB":
                                                labelCell=(TestNodeCell_Label_Text) scanItem.getValue();
                                                updateLabelCell(labelCell,labelCell.getText());
                                                break;
                                            case "VddCore":
                                                labelCell=(TestNodeCell_Label_Text) scanItem.getValue();
                                                updateLabelCell(labelCell,labelCell.getText());
                                                break;
                                            case "FmaxCore":
                                                labelCell=(TestNodeCell_Label_Text) scanItem.getValue();
                                                updateLabelCell(labelCell,labelCell.getText());
                                                break;
                                            case "DownComponentOnFail":
                                                labelCell=(TestNodeCell_Label_Text) scanItem.getValue();
                                                updateLabelCell(labelCell,labelCell.getText());
                                                break;
                                            default:
                                        }
                                    }
                                }
                            }

                        }
    //                   
    //                    System.out.println("getValue().toString() equals " +item.getValue().toString());
    //                    System.out.println("item.toString() equals " +item.toString());
                    }
                }
            }
        }
        this.udpateDone=true;
    }
    private void updateLabelCell(TestNodeCell_Label_Text labelCell, String text){
        if(XMLRead.variables.containsKey(text)){
//            System.out.println("variables contains this key " + text);
//            System.out.println("update this variable " + text + "equals " + XMLRead.variables.get(text).getValue());
            if(XMLRead.variables.get(text).isIsEvaluated() || XMLRead.variables.get(text).isIsValid()){
                
                labelCell.update(labelCell.getText() +  " == " + XMLRead.variables.get(text).getValue());
            }
        }
    
    }
    public TreeItem getTreeItem(){
        
        this.rootItem = new TreeItem(new TestNodeCell_Label_2Text_Button(root, this.fileName, this.root.expression)); 
        
        for(TestItem testItem: root.subItems){
            TreeItem child =buildTreeItem(testItem);
            if(child!=null) rootItem.getChildren().add(child);
        }
        treeIsReady=true;
//        updateTestTree();
        return rootItem;
    }
    public void setExpanded(TreeItem treeItem){
        if(treeItem.isExpanded()) treeItem.setExpanded(false);
//        System.out.println(treeItem.getValue().toString() + " is set un expanded");
        for(int i=0; i!= treeItem.getChildren().size();i++){
            TreeItem item= (TreeItem) treeItem.getChildren().get(i);
//            if(item.isExpanded()){ 
//                item.setExpanded(false);
//                System.out.println(item.getValue().toString() + " is set un expanded");
//            }
            if(item!=null)setExpanded(item);
        }
    }
    public TreeItem buildTreeItem(TestItem testItem){
        System.out.println("start to build " + testItem.getName() + testItem.getExpression() + " tree");
        TreeItem testRoot;
        if(testItem.isLeaf){
            //<Exec>com/amd/sapphire/client/test_template/xctrl/dc/std/SimpleDCTest</Exec>
            //<VForce>1.8</VForce>
            //<IRange>1mA</IRange>
//            String openFile=null;
            if(testItem.getName().equals("Exec")){
                testRoot= new TreeItem(new TestNodeCell_Exec(testItem));
            }
            else{
                String testItemName=testItem.getName();
               
                if(testItemName.toLowerCase().contains("timing")&&XMLRead.timing.get(testItem.expression)!=null ){
                    TestNodeCell_Label_2Text_Button timingNode=new TestNodeCell_Label_2Text_Button(testItem,null,this.root.expression);
                    timingNode.updateTips();
                    testRoot= new TreeItem(timingNode);
                    this.timingRoot=testRoot;
                }
                else if(testItemName.toLowerCase().contains("level")&&XMLRead.levels.get(testItem.expression)!=null){
                    TestNodeCell_Label_2Text_Button levelNode= new TestNodeCell_Label_2Text_Button(testItem,null,this.root.expression);
                    levelNode.updateTips();
                    testRoot= new TreeItem(levelNode);
                    this.levelRoot=testRoot; 
                }
                else if(testItemName.toLowerCase().contains("patternburst")&&XMLRead.patternBursts.get(testItem.expression)!=null){
//                    openFile=XMLRead.patternBursts.get(testItem.expression).getFileName();
//                    testRoot= new TreeItem(new TestNodeCell_Label_2Text_Button(testItem,null));
                    XMLRead.currentPatternBurst.clear();
                    XMLRead.currentPatRefTree.clear();
//                    testRoot= XMLRead.patternBursts.get(testItem.expression).getRoot(true);
                      testRoot= XMLRead.patternBursts.get(testItem.expression).buildRoot();
                      TestNodeCell_Label_2Text_Button nodeCell =(TestNodeCell_Label_2Text_Button) testRoot.getValue();
                      nodeCell.updateTips();
                      
//                      XMLRead.currentPatternBurstTreeItem=testItem.expression;
                }
                else if(testItemName.toLowerCase().contains("loadboard")&&XMLRead.loadBoards.get(testItem.expression)!=null){
                    testRoot= new TreeItem(new TestNodeCell_Label_2Text_Button(XMLRead.loadBoards.get(testItem.expression),testItem.expression,this.root.expression));
                    this.loadBoardRoot=testRoot;
                }
                else if(testItemName.toLowerCase().contains("description")&&XMLRead.testDescription.get(testItem.expression)!=null){

                    testRoot= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.name, testItem.expression,XMLRead.testDescription.get(testItem.expression).getFileName() ));
                }
                else if(testItemName.toLowerCase().contains("resultspec")&&XMLRead.resultSpecs.get(testItem.expression)!=null){
//                    System.out.println(testItemName);

                    testRoot= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.name, testItem.expression,XMLRead.resultSpecs.get(testItem.expression).getFileName()));
                }
                else if(testItemName.toLowerCase().contains("vectorresult")&&XMLRead.vectorResult.get(testItem.expression)!=null){
//                    System.out.println(testItemName);

                    testRoot= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.name, testItem.expression,XMLRead.vectorResult.get(testItem.expression).getFileName()));
                }
                else if(testItemName.toLowerCase().contains("softset")&&XMLRead.softSet.get(testItem.expression)!=null){
//                    System.out.println(testItemName);

                    testRoot= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.name, testItem.expression,XMLRead.softSet.get(testItem.expression).getFileName()));
                }
                else if(testItemName.toLowerCase().contains("softset")&&XMLRead.softSetGroup.get(testItem.expression)!=null){
                   
                    testRoot= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.name, testItem.expression,XMLRead.softSetGroup.get(testItem.expression).getFileName()));
                }
                
                else if((testItemName.toLowerCase().contains("dcsequ")||testItemName.toLowerCase().contains("dcpatt"))&&XMLRead.DCs.get(testItem.expression)!=null){
//                    System.out.println(testItemName);

                    testRoot= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.name, testItem.expression,XMLRead.DCs.get(testItem.expression).getFileName()));
                }
                else if((testItemName.contains("TestRef"))&&XMLRead.newTests.get(testItem.expression)!=null){
//                    System.out.println(testItemName);

                    testRoot= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.name, testItem.expression,XMLRead.newTests.get(testItem.expression).getFileName()));
                }
                else if((testItemName.contains("AxisList"))&&XMLRead.AxisList.get(testItem.expression)!=null){
//                    System.out.println(testItemName);

                    testRoot= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.name, testItem.expression,XMLRead.AxisList.get(testItem.expression).getFileName()));
                }
                
                else{
                    if(testItem.expression.contains(".")){
                        String[] equationVariable =null;
                        equationVariable =testItem.expression.split("\\.");
                        String tempEuqationName=equationVariable[0];
                        
                        String variableName=null;
                     
                        if(XMLRead.equations.containsKey(tempEuqationName) && equationVariable.length==2){
                            variableName= equationVariable[1];
                            testRoot = new TreeItem(new TestNodeCell_Label_2Text_Button(testItem.name, testItem.expression,XMLRead.equations.get(tempEuqationName).getFileName(),variableName));
                            if(equationRef.isEmpty())
                                equationRef.add(equationVariable[0]);
                            else if(!equationRef.contains(equationVariable[0]))
                                equationRef.add(equationVariable[0]);
                            
                            if(!variables.contains(variableName))
                                variables.add(variableName);
                        }
                        else
                            testRoot= new TreeItem(new TestNodeCell_Label_Text(testItem,null));
                    }
                    else    
                        testRoot= new TreeItem(new TestNodeCell_Label_Text(testItem,null));
                }
  
            }
        }
        else{
            //<TestParameters> Label
            //<CurrentMeasurement name="Leakage Low V18_P Pins"> Label+ text
            //<Measurement sigref="LeakageV18_P"> Label + text
            //<Param name="GenericBlock"><Value>TestVar</Value></Param> first should be Label, and then Label + Text
            
            if(testItem.attriName==null){
                // this if for RingOsc test MacroConfig 
                if(testItem.name.equals("MacroConfig")){
                    if(!testItem.previousItem.equals("MacroConfig")){
                        // this is the first Param, only add one Param Label TreeItem
                        testRoot= new TreeItem(new TestNodeCell_Label(testItem)); 
                        temp1=testRoot;              
                        for(TestItem subItem: testItem.subItems){
                            TreeItem item=new TreeItem( new TestNodeCell_Label_Text(subItem,null));
                            testRoot.getChildren().add(item);      
                        }
                    }
                    else{ 
//                        System.out.println("Repeat ");
                        // this is the N Param, do nothing, the rootTree should be the first one
                       for(TestItem subItem: testItem.subItems){
                            if(temp1!=null){ 
                                temp1.getChildren().add(new TreeItem( new TestNodeCell_Label_Text(subItem,null)));    
                            }
                            else
                                System.out.println("Error Temp is none");
                       }   
                       return null;
                    }
                }
                else{
                    testRoot= new TreeItem(new TestNodeCell_Label(testItem));

                    for(TestItem subItem: testItem.subItems){
                        testRoot.getChildren().add(buildTreeItem(subItem));
                        
                    }
                }  
            }
            else{
                if(testItem.name.equals("Param")){
                    if(!testItem.previousItem.equals("Param")){
                        // this is the first Param, only add one Param Label TreeItem
                        testRoot= new TreeItem(new TestNodeCell_Label(testItem)); 
                        temp=testRoot;              
                        for(TestItem subItem: testItem.subItems){
                            String testItemName=testItem.expression;
               
                            if(testItemName.toLowerCase().contains("timing")&&XMLRead.timing.get(subItem.expression)!=null ){
                                TestNodeCell_Label_2Text_Button nodeCell= new TestNodeCell_Label_2Text_Button(subItem,null,this.root.expression);
                                    TreeItem item= new TreeItem(nodeCell);
                                    nodeCell.updateTips();
                                    this.timingRoot=item;
                                    this.timing=subItem.expression;
                                    if(temp!=null) temp.getChildren().add(item);
                            }
                            else if(testItemName.toLowerCase().contains("level")&&XMLRead.levels.get(subItem.expression)!=null){
                                TestNodeCell_Label_2Text_Button nodeCell= new TestNodeCell_Label_2Text_Button(subItem,null,this.root.expression);
                                    TreeItem item= new TreeItem(nodeCell);
                                    nodeCell.updateTips();
                                    this.levelRoot=item; 
                                    this.levels=subItem.expression;
                                    if(temp!=null) temp.getChildren().add(item);
                            }
                            else if(testItemName.toLowerCase().contains("patternburst")&&XMLRead.patternBursts.get(subItem.expression)!=null){
                //                    openFile=XMLRead.patternBursts.get(testItem.expression).getFileName();
                //                    testRoot= new TreeItem(new TestNodeCell_Label_2Text_Button(testItem,null));
                                
                                XMLRead.currentPatternBurst.clear();
                                XMLRead.currentPatRefTree.clear();
                                this.patternBurst=subItem.expression;
                //                    testRoot= XMLRead.patternBursts.get(testItem.expression).getRoot(true);
                                  TreeItem item= XMLRead.patternBursts.get(subItem.expression).buildRoot();
                                  TestNodeCell_Label_2Text_Button nodeCell =(TestNodeCell_Label_2Text_Button) item.getValue();
                                  nodeCell.updateTips();
                                  
                                  if(temp!=null) temp.getChildren().add(item);
                //                      XMLRead.currentPatternBurstTreeItem=testItem.expression;
                            }
                            else if(testItemName.toLowerCase().contains("loadboard")&&XMLRead.loadBoards.get(subItem.expression)!=null){
                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button(XMLRead.loadBoards.get(subItem.expression),subItem.expression,this.root.expression));
                                if(temp!=null) temp.getChildren().add(item);
                                this.loadBoardRoot=item;
                                this.loadBoard=subItem.expression;
                            }
                            else if(testItemName.toLowerCase().contains("description")&&XMLRead.testDescription.get(subItem.expression)!=null){

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.testDescription.get(subItem.expression).getFileName() ));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if(testItemName.toLowerCase().contains("resultspec")&&XMLRead.resultSpecs.get(subItem.expression)!=null){
                //                    System.out.println(testItemName);

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.resultSpecs.get(subItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if(testItemName.toLowerCase().contains("vectorresult")&&XMLRead.vectorResult.get(subItem.expression)!=null){
                //                    System.out.println(testItemName);

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.vectorResult.get(subItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if(testItemName.toLowerCase().contains("softset")&&XMLRead.softSet.get(subItem.expression)!=null){
                //                    System.out.println(testItemName);

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.softSet.get(subItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if(testItemName.toLowerCase().contains("softset")&&XMLRead.softSetGroup.get(testItem.expression)!=null){
                        
                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.name, testItem.expression,XMLRead.softSetGroup.get(testItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if((testItemName.toLowerCase().contains("dcseq")||testItemName.toLowerCase().contains("dcpatter"))&&XMLRead.DCs.get(subItem.expression)!=null){
                //                    System.out.println(testItemName);

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.DCs.get(subItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if((testItemName.contains("TestRef"))&&XMLRead.newTests.get(subItem.expression)!=null){
                //                    System.out.println(testItemName);

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.newTests.get(subItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if((testItemName.contains("AxisList"))&&XMLRead.AxisList.get(subItem.expression)!=null){
                //                    System.out.println(testItemName);

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.AxisList.get(subItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else{
                                // the reason we always need to define a TreeItem here is due to it's a Tree and we can not re-use it and point it to a new Tree
                                TreeItem item;
                                if(subItem.expression.contains(".")){
                                    
                                    String[] equationVariable =null;
                                    equationVariable =testItem.expression.split("\\.");
                                    String tempEuqationName=equationVariable[0];

                                    String variableName=null;

                                    if(XMLRead.equations.containsKey(tempEuqationName) && equationVariable.length==2){
                                        variableName= equationVariable[1];
                                        item = new TreeItem(new TestNodeCell_Label_2Text_Button(testItem.expression, subItem.expression,XMLRead.equations.get(tempEuqationName).getFileName(),variableName));
                                        if(equationRef.isEmpty())
                                            equationRef.add(equationVariable[0]);
                                        else if(!equationRef.contains(equationVariable[0]))
                                            equationRef.add(equationVariable[0]);
                                         if(!variables.contains(variableName))
                                            variables.add(variableName);
                                    }
                                    else
                                        item= new TreeItem( new TestNodeCell_Label_Text(subItem,testItem.expression));
                                }
                                else
                                    item= new TreeItem( new TestNodeCell_Label_Text(subItem,testItem.expression));
                                
                                
                                
                                if(temp!=null) temp.getChildren().add(item);
                            }
                 
                          
                        }
                    }
                    else{ 
                        // this is the N Param, do nothing, the rootTree should be the first one
                       for(TestItem subItem: testItem.subItems){
                            String testItemName=testItem.expression;
               
                            if(testItemName.toLowerCase().contains("timing")&&XMLRead.timing.get(subItem.expression)!=null ){
                                TestNodeCell_Label_2Text_Button nodeCell =new TestNodeCell_Label_2Text_Button(subItem,null,this.root.expression);
                                    TreeItem item= new TreeItem(nodeCell);
                                    nodeCell.updateTips();
                                    this.timingRoot=item;
                                    this.timing=subItem.expression;
                                    if(temp!=null) temp.getChildren().add(item);
                            }
                            else if(testItemName.toLowerCase().contains("level")&&XMLRead.levels.get(subItem.expression)!=null){
                                TestNodeCell_Label_2Text_Button nodeCell= new TestNodeCell_Label_2Text_Button(subItem,null,this.root.expression);
                                    TreeItem item= new TreeItem(nodeCell);
                                    nodeCell.updateTips();
                                    this.levelRoot=item; 
                                    this.levels=subItem.expression;
                                    if(temp!=null) temp.getChildren().add(item);
                            }
                            else if(testItemName.toLowerCase().contains("patternburst")&&XMLRead.patternBursts.get(subItem.expression)!=null){
                //                    openFile=XMLRead.patternBursts.get(testItem.expression).getFileName();
                //                    testRoot= new TreeItem(new TestNodeCell_Label_2Text_Button(testItem,null));
                                XMLRead.currentPatternBurst.clear();
                                XMLRead.currentPatRefTree.clear();
                                this.patternBurst=subItem.expression;
                //                    testRoot= XMLRead.patternBursts.get(testItem.expression).getRoot(true);
                                  TreeItem item= XMLRead.patternBursts.get(subItem.expression).buildRoot();
                                  TestNodeCell_Label_2Text_Button nodeCell =(TestNodeCell_Label_2Text_Button) item.getValue();
                                  nodeCell.updateTips();
                                  if(temp!=null) temp.getChildren().add(item);
                //                      XMLRead.currentPatternBurstTreeItem=testItem.expression;
                            }
                            else if(testItemName.toLowerCase().contains("loadboard")&&XMLRead.loadBoards.get(subItem.expression)!=null){
                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button(XMLRead.loadBoards.get(subItem.expression),subItem.expression,this.root.expression));
                                if(temp!=null) temp.getChildren().add(item);
                                this.loadBoardRoot=item;
                                this.loadBoard=subItem.expression;
                            }
                            else if(testItemName.toLowerCase().contains("description")&&XMLRead.testDescription.get(subItem.expression)!=null){

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.testDescription.get(subItem.expression).getFileName() ));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if(testItemName.toLowerCase().contains("resultspec")&&XMLRead.resultSpecs.get(subItem.expression)!=null){
                //                    System.out.println(testItemName);

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.resultSpecs.get(subItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if(testItemName.toLowerCase().contains("vectorresult")&&XMLRead.vectorResult.get(subItem.expression)!=null){
                //                    System.out.println(testItemName);

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.vectorResult.get(subItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if(testItemName.toLowerCase().contains("softset")&&XMLRead.softSet.get(subItem.expression)!=null){
//                                    System.out.println(testItemName);

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.softSet.get(subItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if(testItemName.toLowerCase().contains("softset")&&XMLRead.softSetGroup.get(testItem.expression)!=null){
         
                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.name, testItem.expression,XMLRead.softSetGroup.get(testItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if((testItemName.toLowerCase().contains("dcseq")||testItemName.toLowerCase().contains("dcpatt"))&&XMLRead.DCs.get(subItem.expression)!=null){
                //                    System.out.println(testItemName);

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.DCs.get(subItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if((testItemName.contains("TestRef"))&&XMLRead.newTests.get(subItem.expression)!=null){
                //                    System.out.println(testItemName);

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.newTests.get(subItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else if((testItemName.contains("AxisList"))&&XMLRead.AxisList.get(subItem.expression)!=null){
                //                    System.out.println(testItemName);

                                TreeItem item= new TreeItem(new TestNodeCell_Label_2Text_Button( testItem.expression, subItem.expression,XMLRead.AxisList.get(subItem.expression).getFileName()));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                            else{
                                TreeItem item;
                                if(subItem.expression.contains(".")){
                                    
                                    String[] equationVariable =null;
                                    equationVariable =testItem.expression.split("\\.");
                                    String tempEuqationName=equationVariable[0];

                                    String variableName=null;

                                    if(XMLRead.equations.containsKey(tempEuqationName) && equationVariable.length==2){
                                        variableName= equationVariable[1];
                                        item = new TreeItem(new TestNodeCell_Label_2Text_Button(testItem.expression, subItem.expression,XMLRead.equations.get(tempEuqationName).getFileName(),variableName));
                                        if(equationRef.isEmpty())
                                            equationRef.add(equationVariable[0]);
                                        else if(!equationRef.contains(equationVariable[0]))
                                            equationRef.add(equationVariable[0]);
                                         if(!variables.contains(variableName))
                                            variables.add(variableName);
                                    }
                                    else
                                        item= new TreeItem( new TestNodeCell_Label_Text(subItem,testItem.expression));
                                }
                                else
                                    item= new TreeItem( new TestNodeCell_Label_Text(subItem,testItem.expression));
                                if(temp!=null) temp.getChildren().add(item);
                            }
                       }   
                       return null;
                    }
                }
                else{               
                    testRoot= new TreeItem(new TestNodeCell_Label_Text(testItem,null));
                    for(TestItem subItem: testItem.subItems){
                        testRoot.getChildren().add(buildTreeItem(subItem));    
                    }
                }
            }
        }

        return testRoot;
    }
    

//    public void setIsUsed(boolean isUsed) {
//        this.isUsed = isUsed;
//    }
//    
//
//    public boolean isIsUsed() {
//        return isUsed;
//    }
    public String getSourceFile() {
        System.out.println("Exec name "+ this.execName);
        sourceFile = XMLRead.upaPath  +"\\"+  this.execName.replace('.', '\\') + ".java";
        sourceFile = sourceFile.replace('/', '\\');

        File tempFile = new File(sourceFile);
//            System.out.println(fileName);
        if (tempFile.exists()) {
            sourceFile = tempFile.getAbsolutePath();
                System.out.println("File exist...." + tempFile.getAbsolutePath());
        }
        else{
            sourceFile = "K:\\Xtos"  +"\\"+  this.execName.replace('.', '\\') + ".java";
            sourceFile = sourceFile.replace('/', '\\');
            
            tempFile= new File(sourceFile);
            if(!tempFile.exists())
                sourceFile=null;
        }
        return sourceFile;
    }

    public String getFileName() {
        return fileName;
    }

    public String getExecName() {
        return execName;
    }
    public TreeItem getLevelRoot() {
        return levelRoot;
    }

    public TreeItem getTimingRoot() {
        return timingRoot;
    }
    public void print(){
        root.print();
    }
    public class TestItem{
        private String name=null;
        private boolean isLeaf=true;
        private String attriName=null;
        private String expression=null;
        private List<TestItem> subItems= new ArrayList<>();;
        private int level=0;
        private String space="";
        private String previousItem="";
        private String parentName=null;
//        private Item motherItem;

        public TestItem() {
        }
        public TestItem(Element element, int _level) {
          
            this.level=_level;
            this.name=element.getName();
//            System.out.println("node name is "+ this.name);
            List<Element>nodes= element.elements();
            
            
            if(nodes.isEmpty()){
                this.isLeaf=true;
                this.expression=element.getText();
                if (_level==1&& this.name=="Exec") execName=this.expression;
                
                if(name.toLowerCase().contains("patternburst")/*&&XMLRead.patternBursts.get(expression)!=null*/){
                    patternBurst=this.expression;
                }
                else if(name.toLowerCase().contains("level")/*&&XMLRead.levels.get(expression)!=null*/){
                    levels=this.expression;
                }
                else if(name.toLowerCase().contains("timing")/*&&XMLRead.timing.get(expression)!=null*/){
                    timing=this.expression;
                }
                else if(name.toLowerCase().contains("loadboard")/*&&XMLRead.timing.get(expression)!=null*/){
                    loadBoard=this.expression;
                }
                else if(name.toLowerCase().contains("equation")/*&&XMLRead.timing.get(expression)!=null*/){
                    if(equationRef.isEmpty())
                        equationRef.add(this.expression);
                    else if(!equationRef.contains(this.expression))
                        equationRef.add(this.expression);
//                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            
                }
                
            }
            else{
                this.isLeaf=false;
                if(element.attributeCount()!=0){
                    List<Attribute> attrs = element.attributes();
                    this.expression=attrs.get(0).getValue();
                    this.attriName=attrs.get(0).getName();
                }

                String temp="";
                for(Element node: nodes){
                    TestItem item= new TestItem(node, this.level+1);
                    item.previousItem=temp;
                    item.parentName=this.expression;
                    temp=item.getName();
                    this.subItems.add(item);
                    
                }
            }
            
        }
        
        

        public String getParentName() {
            return parentName;
        }

        public List<TestItem> getSubItems() {
            return subItems;
        }
        public void print(){
            
//            <Test name="ThermdcLeak_High">
//               <Exec>com/amd/sapphire/client/test_template/xctrl/dc/std/SimpleDCTest</Exec>
//               <TestParameters>
//                  <Levels>LevelsThermalDiode</Levels>
//                  <OpenPinsBefore>BP_THERMDA</OpenPinsBefore>
//                  <DebugResultTrace>FAIL</DebugResultTrace>
//                  <CurrentMeasurement name="LeakageThermDCHigh">
//                     <Device>PMU</Device>
//                     <Type>Static</Type>
//                     <!--ResultMode>DIGITIZE</ResultMode-->
//                     <ResultSpec>ThermdcLeak_High_RS</ResultSpec>
//                     <Measurement sigref="BP_THERMDC">
//                        <VForce>1.5V</VForce>
//                        <IRange>10uA</IRange>
//                        <VConnect>0</VConnect>
//                        <SetupDelay>5ms</SetupDelay>
//                        <TestDescription>LeakageDCHigh</TestDescription>
//                        <PinLogMode>ALL</PinLogMode>
//                     </Measurement>
//                  </CurrentMeasurement>
//                  <FunctionalTestDescription>LeakageDCHigh</FunctionalTestDescription>
//               </TestParameters>
//            </Test>
            if(!this.isLeaf){
                if (this.expression!=null){
                    System.out.println(getSpace() + "<" + this.name + " "+ this.attriName +"=\"" + this.expression + "\">");
                    for(TestItem item: this.subItems){
                        item.print();
                    }
                    System.out.println(getSpace()+"</"+ this.name +">");
                }
                else{
                    System.out.println(getSpace()+"<"+ this.name +">");
                    for(TestItem item: this.subItems){
                        item.print();
                    }
                    System.out.println(getSpace()+"</"+ this.name +">");
                }
            }
            else
                System.out.println(getSpace()+"<"+ this.name + ">"+ this.expression + "</" + this.name +">");
        }

        public String getSpace() {
            if (this.level==0)
                return space;
            else if(this.level==1)
                return "    ";
            else if(this.level==2)
                return "        ";
            else if(this.level==3)
                return "            ";
            else if(this.level==4)
                return "                ";
            else if(this.level==5)
                return "                   ";
            else
                return "";
        }

        public String getName() {
            return name;
        }

        public String getExpression() {
            return expression;
        }

        public String getAttriName() {
            return attriName;
        }
 
        public boolean search(String content){
                if(this.isLeaf){
//                    System.out.println(this.name);
                    if (this.expression!=null && this.expression.equals(content))
                        return true;
                    else
                        return false;
                }
                else{
                    if (this.expression!=null && this.expression.equals(content))
                        return true;
                    boolean isFind=false;
                    for(TestItem item: this.subItems){
                        if(item.search(content)){
                            isFind=true;
                            break;
                        }
                    }
                    if(isFind)
                        return true;
                    else
                        return false;
                }
        }
        
        

        public boolean isIsLeaf() {
            return isLeaf;
        }
        
        public void print(PrintWriter printWriter){
            if(!this.isLeaf){
                if (this.expression!=null){
                    printWriter.println(getSpace() + "<" + this.name + " "+ this.attriName +"=\"" + this.expression + "\">");
                    for(TestItem item: this.subItems){
                        item.print(printWriter);
                    }
                    printWriter.println(getSpace()+"</"+ this.name +">");
                }
                else{
                    printWriter.println(getSpace()+"<"+ this.name +">");
                    for(TestItem item: this.subItems){
                        item.print(printWriter);
                    }
                    printWriter.println(getSpace()+"</"+ this.name +">");
                }
            }
            else
                printWriter.println(getSpace()+"<"+ this.name + ">"+ this.expression + "</" + this.name +">");
        
        }
    }
    public void getMeasurement(TestItem testItem){
        
       
        for(TestItem item: testItem.getSubItems()){
            if(item.name.equals("VoltageMeasurement")||item.name.equals("CurrentMeasurement")){
                measurement.add( new DCMeasurement(item));
                this.findMeasure =true;
            }
            if((!item.isIsLeaf())&&(!this.findMeasure)){
                getMeasurement(item);  
            }
        }
    }
    public class DCMeasurement{
        private String measurementName;
        private int measurementType;
        private final ArrayList<signalMeasure> measureSignals= new ArrayList();

        public DCMeasurement(TestItem item) {
            if(item.getName().equals("VoltageMeasurement"))
                this.measurementType=0;
            else if(item.getName().equals("CurrentMeasurement"))
                this.measurementType=1;
            this.measurementName=item.expression;
            
            for(TestItem subItem: item.getSubItems()){
                if(subItem.getName().equals("Measurement"))
                    
                this.measureSignals.add(new signalMeasure(subItem));
            }
        }

        public ArrayList<signalMeasure> getMeasureSignals() {
            return measureSignals;
        }

        public String getMeasurementName() {
            return measurementName;
        }

        public int getMeasurementType() {
            return measurementType;
        }
        public class signalMeasure{
            private String signal;
            private String pinLogMode;

            public signalMeasure(TestItem item) {
                this.signal= item.getExpression();
                for(TestItem logMode: item.getSubItems()){
                    if(logMode.name.equals("PinLogMode"))
                        this.pinLogMode=logMode.expression;
                }
            }

            public String getPinLogMode() {
                return pinLogMode;
            }

            public String getSignal() {
                return signal;
            }
            
            
           
        
        }
        
        
    
    }

    public List<DCMeasurement> getDCMeasurement() {
        if (!this.findMeasure)
            getMeasurement(this.root);
        return measurement;
    }
    
    
    public void getVariableMeasurement(TestItem testItem, ArrayList<String> sigRefs, boolean isFound){
        for(TestItem item: testItem.getSubItems()){
            if(item.name.equals("Measurement")){
                sigRefs.add(item.getExpression());
                isFound=true;
            }
            if((!item.isIsLeaf())&&(!isFound)){
                getVariableMeasurement(item,sigRefs, false);  
            }
        }
    }
    public String getBinTable(TestItem testItem){
        TestItem  tempItem = null;
        for(TestItem item: testItem.getSubItems()){
            if(item.expression.equals("BinTable")){
                tempItem=item;
                break;
            }
        }
        if(tempItem!=null)
            return tempItem.subItems.get(0).expression;
        else
            return null;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isUsed() {
        return used;
    }

    public List<String> getVariables() {
        return variables;
    }
    
    
    
    
    
}
