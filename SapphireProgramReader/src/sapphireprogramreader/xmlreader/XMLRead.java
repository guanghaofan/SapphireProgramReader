/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader;

import sapphireprogramreader.ui.controls.EquationNodeCell;
import sapphireprogramreader.xmlreader.blockreader.Equation.Group;
import sapphireprogramreader.xmlreader.blockreader.Equation.equationNode;
import sapphireprogramreader.xmlreader.blockreader.PatternBurst.patRefTreeItem;

import sapphireprogramreader.com.udojava.evalex.Expression;
import sapphireprogramreader.xmlreader.blockreader.Test;
import sapphireprogramreader.xmlreader.blockreader.Equation;
import sapphireprogramreader.xmlreader.blockreader.Timing;
import sapphireprogramreader.xmlreader.blockreader.Levels;
import sapphireprogramreader.xmlreader.blockreader.GenericBlock;
import sapphireprogramreader.xmlreader.blockreader.FlowTable;
import sapphireprogramreader.xmlreader.blockreader.PatternBurst;
import sapphireprogramreader.xmlreader.blockreader.ActionList;
import sapphireprogramreader.xmlreader.blockreader.StartNode;
import sapphireprogramreader.xmlreader.blockreader.DeviceNode;
import sapphireprogramreader.xmlreader.blockreader.ExitNode;
import sapphireprogramreader.xmlreader.blockreader.GoToResult;
import sapphireprogramreader.xmlreader.blockreader.BaseNode;
//import com.sun.javafx.fxml.expression.Expression;
import java.io.FileFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.dom4j.Attribute;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import sapphireprogramreader.xmlreader.blockreader.FlowContext;
import sapphireprogramreader.xmlreader.blockreader.FlowContext.Bypass;
import sapphireprogramreader.xmlreader.blockreader.FlowOverride;

/**
 *
 * @author ghfan
 */
public class XMLRead {

    public static List<File> xmlFileList = new ArrayList<>();
    public String TestProgramFile = "";
    public List<ActionList> actionList = new ArrayList<>();
    public GenericBlock action;;
    public List<FlowTable> flowTables = new ArrayList<>();
    public List<TreeItem> actionTree = new ArrayList<>();
    public List<String> flowContextList = new ArrayList();
//    public List<testElement> tests = new ArrayList();
    
    public static HashMap<String,Test> newTests = new HashMap();
    public static HashMap<String, Equation> equations = new HashMap();
    public int treeNodeIndex = 0;
    public static HashMap<String, PatternBurst> patternBursts = new HashMap();
    public int patNo = 0;
    public ObservableList<String> searchResult = FXCollections.observableArrayList();
    
    public HashMap<String, TreeItem> equationRootNodes = new HashMap();
    public HashMap<String, TreeItem> repeatEquationRootNodes = new HashMap();
    public List<String> currentEquation = new ArrayList<>();
    public List<File> skipFiles= new ArrayList<>();
    public boolean getActionList=false;
    
    public List<String> upaConfig= new ArrayList<>();
    public static String upaPath="";
    public static String notePadPath = "";
    public List<String> folderNameFilter= new ArrayList<>(); 
    
    public boolean reReadTest=false;
    public boolean reReadFlow=false;
    public List<String> recentFileList = new ArrayList<>();
    public static ObservableList<String> searchFilesList = FXCollections.observableArrayList();
    
    public String programName=null;
    
    
    public static HashMap<String,Levels> levels= new HashMap();
    public static HashMap<String, Timing> timing = new HashMap();
    public static HashMap<String, GenericBlock> loadBoards= new HashMap();
    
    public static HashMap<String,patRefTreeItem> patRefTreeItems= new HashMap();
    public static List<String> currentPatRefTree= new ArrayList<>();
    // this is to store all the repeat pattern refs but with different parameters
    public static List<String> currentPatternBurst = new ArrayList<>();
     
    public static HashMap<String,TreeItem> patBurstRootItems= new HashMap();
    public static HashMap<String, TreeItem> compareTreeItems = new HashMap();
    public static HashMap<String, TreeItem> compositeTreeItems = new HashMap();
    
    public static String currentPatternBurstTreeItem =null;
    
    public static HashMap<String, TreeItem> timing_level= new HashMap();
    
    public static HashMap<String, GenericBlock> testDescription= new HashMap();
    public static HashMap<String, GenericBlock> resultSpecs = new HashMap<>();
    public static HashMap<String, GenericBlock> compares= new HashMap<>();
    public static HashMap<String, GenericBlock> DCs= new HashMap<>();
    public static HashMap<String, GenericBlock> vectorResult= new HashMap<>();
    public static HashMap<String, GenericBlock> softSet= new HashMap<>();
    public static HashMap<String, GenericBlock> softSetGroup= new HashMap<>();
    public HashMap<String, FlowOverride> flowOverrides= new HashMap<>();
    public static HashMap<String, GenericBlock> AxisList = new HashMap<>();
    
    public List<String> skipFileFilter = new ArrayList<>();
    public List<String> inValidFiles= new ArrayList<>();
    public List<String> newInValidFiles= new ArrayList<>();
    public List<Test> binningTest= new ArrayList<>();
    
    public static HashMap<String, equationNode> variables= new HashMap();
    
    public static List<File> unusedFileList = new ArrayList<>();
    
    public static boolean evaluationOn=false; 
    public static boolean expandTestTree=true;

    public String configFile=System.getProperty("user.home") + "\\SapphireProgramReader\\config\\Config.xml";
    public static String openXMLFile=System.getProperty("user.home") + "\\SapphireProgramReader\\config\\openXML.bat";
//    public String propertyFile;
//    public String signalsFile;
//    public String signalGroupFile;
//    public static LinkedHashMap<String,Signal> sigs = new LinkedHashMap<String,Signal>();
//    public static LinkedHashMap<String,SignalGroup> allSignalGroups = new LinkedHashMap<String,SignalGroup>();
//    public boolean firstAudit=true;
//    
//    private BinTableReader binReader = new BinTableReader();
//    private BinTableAuditor binAuditor = new BinTableAuditor();
    
    
    public XMLRead() {
    }

    public void init() {
//        evaluationOn=false; 
        programName=null;
        xmlFileList.clear();
        TestProgramFile = null;
        actionList.clear();
        action=null;
        flowTables.clear();
        actionTree.clear();
        flowContextList.clear();
//        tests.clear();
        upaPath = "";
        treeNodeIndex = 0;
        searchResult.clear();
        patternBursts.clear();
        patNo = 0;
        equations.clear();
        equationRootNodes.clear();
        currentEquation.clear();
        skipFiles.clear();
        reReadTest=false;
        reReadFlow=false;
//        upaConfig.clear();
//        notePadPath="";
//        folderNameFilter.clear();
        levels.clear();
        timing.clear();
        loadBoards.clear();
        patRefTreeItems.clear();
        currentPatRefTree.clear();
        currentPatternBurst.clear();
        patBurstRootItems.clear();
        compareTreeItems.clear();
        compositeTreeItems.clear();
        currentPatternBurstTreeItem=null;
        timing_level.clear();
        testDescription.clear();
        resultSpecs.clear();
        DCs.clear();
        vectorResult.clear();
        this.newInValidFiles.clear();
        this.flowOverrides.clear();
        searchFilesList.clear();
        unusedFileList.clear();
        
//        this.propertyFile=null;
//        this.signalsFile=null;
//        this.signalGroupFile=null;
//        this.firstAudit=true;
    }

    public void printFlowContextList() {
        for (String flowContext : flowContextList) {
            System.out.println(flowContext);
        }
    }

    public void printActionList() {
        /*
         <Action name="StartDevice">
         <Type>StartDevice</Type>
         <ThreadModel>Single</ThreadModel>
         <FlowRef>Flow_START_DEVICE</FlowRef>
         </Action>
         */
//        for (ActionList list : actionList) {
//            list.printAction();
//        }
        action.print();
    }

    public void printXMLFileList() {
        for (File xmlFile : xmlFileList) {
            System.out.println(xmlFile.getAbsolutePath());
        }
    }

    public void printFlowTables() {
        for (FlowTable flowTable : flowTables) {
            flowTable.printFlowTable();
        }
    }

    public void printPatternBurst() {
        for (PatternBurst pat : XMLRead.patternBursts.values()) {
            pat.print();
        }
    }

    public void printEquations() {
        for (Equation equation : equations.values()) {
            equation.print();
        }
    }
    
    public void printTiming(){
        for(Timing timing: XMLRead.timing.values()){
            timing.print();
        }
    }
    
    public void printLevel(){
        for(Levels level: XMLRead.levels.values()){
            level.print();
        }
    }
    
    public void printLoadboards(){
        for(GenericBlock lb: this.loadBoards.values()){
            lb.print();
        }
    }
  
    public void printTests(){
        for(Test test: this.newTests.values()){
            test.print();
        }
    }
    
    public void printSoftSet(){
        for(GenericBlock testDescription: XMLRead.softSet.values()){
            testDescription.print();
        }
    }
    public void printSoftSetGroup(){
        for(GenericBlock testDescription: XMLRead.softSetGroup.values()){
            testDescription.print();
        }
    }
    
    public void printTestDescription(){
        for(GenericBlock testDescription: XMLRead.testDescription.values()){
            testDescription.print();
        }
    }
    public void printResultSpec(){
        for(GenericBlock result: XMLRead.resultSpecs.values()){
            result.print();
        }
    }
    public void printCompare(){
        for(GenericBlock result: XMLRead.compares.values()){
            result.print();
        }
    }
    public void printDCs(){
        for(GenericBlock result: XMLRead.DCs.values()){
            result.print();
        }
    }
    public void printVectorResult(){
        for(GenericBlock result:this.vectorResult.values()){
            result.print();
        }
    }
    public void printFlowOverride(){
        for(FlowOverride flowOverride: this.flowOverrides.values()){
            flowOverride.print();
        }
            
    }
    
    /**
     *
     * @param file
     * @return
     */
    private boolean readActionList(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        List<Element> nodes = document.selectNodes("//blocks/TestProgram/Action");
        if (nodes.isEmpty()){
            this.skipFiles.add(file);
            document = null;
            return false;
        }
        else{
            String actionName = "";
            for (Element element : nodes) {
                List<Attribute> attrs = element.attributes();
                if (attrs != null && attrs.size() > 0) {
                    for (Attribute attr : attrs) {
                        //System.out.println("Attrs Value " + attr.getValue() + " "); 
                        actionName = attr.getValue();
                    }
                }
    //            Element typeNode = element.element("Type");
    //            System.out.println(typeNode.getText());
    //            
    //            Element flowRefNode = element.element("FlowRef");
    //            System.out.println(flowRefNode.getText());
                actionList.add(new ActionList(element));
            }
        //printActionList();
            getActionList=true;
            document = null;
            return true;
        }
    }
    private boolean readActionList(Document document) {
        List<Element> nodes = document.selectNodes("//blocks/TestProgram/Action");
        if (nodes.isEmpty()){
//            this.skipFiles.add(file);
            return false;
        }
        else{
            String actionName = "";
            for (Element element : nodes) {

                List<Attribute> attrs = element.attributes();
                if (attrs != null && attrs.size() > 0) {
                    for (Attribute attr : attrs) {
                        //System.out.println("Attrs Value " + attr.getValue() + " "); 
                        actionName = attr.getValue();

                    }
                }
    //            Element typeNode = element.element("Type");
    //            System.out.println(typeNode.getText());
    //            
    //            Element flowRefNode = element.element("FlowRef");
    //            System.out.println(flowRefNode.getText());
                actionList.add(new ActionList(element));
            }
        //printActionList();
            document = null;
            return true;
        }
    }
    private void readAction(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        final String fileName=file.getAbsolutePath();
        
        reader.addHandler("/blocks/TestProgram",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Action " + testName);    
                action= new GenericBlock(row, fileName);
                // prune the tree
                row.detach();
                getActionList=true;
            }
        });
        reader.addHandler( "/blocks/TestProgram/Action",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read action " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                
//                System.out.println("End to read Action " + row.attributeValue("name"));    
                actionList.add(new ActionList(row));
                // prune the tree
//                row.detach();
               
            }
        });
 
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     *
     * @param file
     * @return
     */
    private boolean readFlowTables(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        final String fileName=file.getAbsolutePath();
        int equCnt= this.equations.size();
        int testCnt= XMLRead.newTests.size();
        int flowCnt=this.flowTables.size();
        int flowOverrideCnt= this.flowOverrides.size();
        
        reader.addHandler( "/blocks/Flow",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Flow " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Flow " + flowName);    
                readSubFlow(flowName, row, fileName);
                // prune the tree
                row.detach();
            }
        }
    );
        
        reader.addHandler( "/blocks/Test",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
                System.out.println("Bad Rule, Test " + testName +" is found in a flow file " + fileName);    
           
//                tests.add(new testElement(row, fileName, null));
                newTests.put(testName,new Test(row, fileName, newTests.size()));
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler( "/blocks/Equations",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String flowName=path.getCurrent().attributeValue("name");
                   
                Equation equation = new Equation(row, fileName);
                equations.put(equation.getName(), equation);
                System.out.println("Bad Rule, Equation " + equation.getName() + " is found in flow file " + fileName); 
                // prune the tree
                row.detach();
            }
        });  
        
    

        reader.addHandler( "/blocks/FlowOverride", new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
                FlowOverride  flowOverride= new FlowOverride(row, fileName);
                flowOverrides.put(row.attributeValue("name"),flowOverride);
                if(!flowOverride.isEnable())
                    flowOverrides.remove(row.attributeValue("name"));
                row.detach();

            }
        });

        Document document = null;
        try {
            document = reader.read(file);
             if (equCnt== this.equations.size() && testCnt== XMLRead.newTests.size() && flowCnt==this.flowTables.size() && flowOverrideCnt== this.flowOverrides.size()){
                this.newInValidFiles.add(file.getAbsolutePath());
                
                 System.out.println("Invalid XML file "+ fileName);
             }
            return true;
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
       
        
    }
     private boolean readFlowTables(Document document, String fileName) {
        List<Element> nodes = document.selectNodes("//blocks/Flow");
        if (nodes.isEmpty()) {
//            System.out.println(file.getAbsolutePath() + " is not a Flow file");
//            this.skipFiles.add(file);
            return false;
        } else {

            String flowName = "";
            for (Element element : nodes) {
                flowName = "";
                List<Attribute> attrs = element.attributes();
                if (attrs != null && attrs.size() > 0) {
                    for (Attribute attr : attrs) {
                        //System.out.println("Attrs Value " + attr.getValue() + " "); 
                        flowName += attr.getValue();
                        //System.out.println("<Flow name=\""+ flowName +"\">");
                    }
                }
                readSubFlow(flowName, element, fileName);
            }
            document = null;
            return true;
        }
    }

    private void readSubFlow(String flowName, Element e, String fileName) {

        FlowTable flowTable = new FlowTable();
        flowTable.setFlowName(flowName);
        flowTable.setFileName(fileName);

        List<Element> newNodes = e.elements();
        String nodeName = "";
        //System.out.println("*********************sub flow start ****************************************");

        int nodeNum = 0;
        for (Element element : newNodes) {
            nodeName = "";
            if (element.getName().equalsIgnoreCase("EquationsRef")) {
                flowTable.setEquationsRef(element.getText());
            } else {
                List<Attribute> attrs = element.attributes();
                if (attrs != null && attrs.size() > 0) {
                    for (Attribute attr : attrs) {
                        //System.out.println("Attrs Value " + attr.getValue() + " "); 
                        nodeName = attr.getValue();
//                        System.out.print(nodeName + "    ");
                    }

                }// end if
                String nodeType = element.element("Type").getText();
//                System.out.println("Node Type is   " + nodeType);
                if (nodeType.equalsIgnoreCase("entry")) {
                    flowTable.setStartNode(new StartNode(nodeName, nodeType, readGoToResult(element), flowName, nodeNum++, flowTables.size()));
                } else if ((nodeType.equalsIgnoreCase("test")) || (nodeType.equalsIgnoreCase("flow"))) {
                    flowTable.addNodes(readNode(element, nodeName, nodeType, flowName, fileName, nodeNum++, flowTables.size(),flowTable.getNodes().size()));
                } else if (nodeType.equalsIgnoreCase("exit")) {
                    String decision = element.element("Decision").getText();
                    flowTable.addExitNodes(new ExitNode(nodeName, nodeType, decision, flowName, nodeNum++, flowTables.size()));
                }
                else if(nodeType.equals("Device")){
                    flowTable.addDeviceNodes(new DeviceNode(element, flowName, nodeNum++, flowTables.size()));
                }
            }//end if
            //System.out.println("Node Number is    "   + nodeNum);

        }
//        System.out.println("Node Number is    "   + nodeNum);
        flowTable.setNodeCnt(nodeNum);
//        System.out.println("*********************sub flow end ****************************************");
        flowTables.add(flowTable);
//        else additionalTables.add(flowTable);
    }

    private BaseNode readNode(Element element, String nodeName, String nodeType, String flowName, String fileName, int nodeNum, int flowNo, int baseNodeNo) {

        /*
         <Node name="DDR3_DLLLockRead_667_D3AByte2_VddioNom">
         <Type>Test</Type>
         <SyncPoint>On</SyncPoint>
         <EquationsRef>Setup_LockRead_667_VddioNom</EquationsRef>
         <StrategyRef>BySite</StrategyRef>
         <TestRef>DDR3_DLLLockRead_667_D3AByte2</TestRef>
         <GoTo result="PASS">   <NodeRef>DDR3_DLLLockRead_2133_D3Dbyte_TxLow_VddioNom</NodeRef>   <Decision>Pass</Decision></GoTo>
         <GoTo result="FAIL">   <NodeRef>DDR3_DLLLockRead_2133_D3Dbyte_TxLow_VddioNom</NodeRef>   <Decision>Fail</Decision></GoTo>
         <GoTo result="OVERRIDE"><NodeRef>DDR3_DLLLockRead_2133_D3Dbyte_TxLow_VddioNom</NodeRef>   <Decision>Fail</Decision></GoTo>
         </Node> 
         */

        List<Element> elements = element.elements();
        String syncPoint = null;
        String equationsRef = null;
        String strategyRef = null;
        String tfRef = null;
        String binningRef=null;


        for (Element subElement : elements) {
            if ("SyncPoint".equals(subElement.getName())) {
                syncPoint = subElement.getText();
            } else if ("EquationsRef".equals(subElement.getName())) {
                equationsRef = subElement.getText();
            } else if ("StrategyRef".equals(subElement.getName())) {
                strategyRef = subElement.getText();
            } else if (("TestRef".equals(subElement.getName())) || ("FlowRef".equals(subElement.getName()))) {
                tfRef = subElement.getText();
            }
            else if("BinningRef".equals(subElement.getName())){
                binningRef=subElement.getText();
            }
        }

        return (new BaseNode(nodeName, nodeType, syncPoint, equationsRef, strategyRef, tfRef, binningRef,readGoToResult(element), flowName, fileName, nodeNum, flowNo,baseNodeNo));
    }

    private List<GoToResult> readGoToResult(Element element) {
        /*
         <Node name="START">
         <Type>Entry</Type>
         <GoTo result="PASS"><NodeRef>DDR3_CompCodeRead</NodeRef>   <Decision>Pass</Decision></GoTo>
         </Node>  
         */
        List<GoToResult> nodeResult = new ArrayList();
        //List<Element> goToResults = element.selectNodes("//blocks/Flow/Node/GoTo");

        List<Element> goToResults = element.elements("GoTo");

        String result = "";
        for (Element goToResult : goToResults) {
            List<Attribute> attrs = goToResult.attributes();
            result = "";
            if (attrs != null && attrs.size() > 0) {
                for (Attribute attr : attrs) {
                    //System.out.println("Attrs Value " + attr.getValue() + " "); 
                    result += attr.getValue();
                    //System.out.print(result);
                }

            }// end if
            String nodeRef = goToResult.element("NodeRef").getText();
            String decision = goToResult.element("Decision").getText();
            nodeResult.add(new GoToResult(result, nodeRef, decision));
        }
        return nodeResult;
    }
    FileFilter fileFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            //if the file extension is .xml return true, else false
            if (file.getName().endsWith("")) {
                return true;
            }
            return false;
        }
    };

    public void treeWalk(Document document) {
        //treeWalk( document.getRootElement() );
    }

    private boolean treeWalk(Element element, String content) {
//        loopCnt+=1;
        List<Attribute> attrs = element.attributes();
        if (attrs != null && attrs.size() > 0) {
            for (Attribute attr : attrs) {
//                    System.out.println("loop count is  " + loopCnt);
//                    System.out.println("Attrs Value " + attr.getValue() + " ");
                if ((attr.getValue().equals(content)) || ((attr.getValue().equals(content)))) {
                    return true;
                }
            }
        } else {
//                System.out.println("loop count is  " + loopCnt);
//                if(element.getText()!=null) System.out.println("NodeText " + element.getText());
            if ((element.getText().equals(content)) || ((element.getText().equals(content)))) {
                return true;
            }
        }
        for (Object subElement : element.elements()) {
            if (treeWalk((Element) subElement, content)) {
                return true;
            }
        }
        return false;
    }

    public Document parse(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        return document;
    }

    private void readNode(Element root, String content) {
        if (root == null) {
            return;
        }
        List<Attribute> attrs = root.attributes();
        if (attrs != null && attrs.size() > 0) {
            for (Attribute attr : attrs) {
                //System.err.println("Attrs Value " + attr.getValue() + " ");
                if (attr.getValue().equalsIgnoreCase(content)) {
                }
            }
        } else {
            //System.out.println("NodeText " + root.getText());
        }

    }

    public void LoadFile(File inFile, TreeItem root) throws DocumentException {
        File[] fileList = inFile.listFiles(fileFilter);
       
        for (File subFile : fileList) {
            if (subFile.isDirectory()) {
                boolean notTarget = false;
                for (String filter : folderNameFilter) {
                    if (subFile.getName().toLowerCase().contains(filter)) {
                        notTarget = true;
                        System.out.println("Skipped  " + subFile.getName() + " due to " + filter);
                        break;
                    }
                }
                if (!notTarget) {
//                    TreeItem item= new TreeItem(subFile.getName());
                    TreeItem item= new TreeItem(subFile.getName());
                    root.getChildren().add(item);
                    LoadFile(subFile, item);
                }
            } else {
//                root.getChildren().add( new TreeItem( new HBox_TextCell(subFile)));
                root.getChildren().add( new TreeItem(subFile.getName()));
//                if(subFile.getName().equalsIgnoreCase("tp.properties"))
//                    this.propertyFile=subFile.getAbsolutePath();
                String fileName = subFile.getName().toLowerCase();
                String path = subFile.getAbsolutePath();
           
                String parentName=subFile.getParentFile().getName();
                String fullName=subFile.getAbsolutePath();
                FileInputStream fileInStream = null;
                long fileSize=0;
                try {
                    fileInStream = new FileInputStream(subFile);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    fileSize= fileInStream.available();
                } catch (IOException ex) {
                    fileSize=800000;
                    Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if (fileName.endsWith("xml")&& fileSize<800000){                
//                    System.out.println(fileName);
                    
//                    if(fileName.contains("resultspec_default"))
//                        System.out.println("");
                    if(!this.inValidFiles.contains(fullName)){
//                        System.out.println("valid xml file " + fileName);
                        xmlFileList.add(subFile);
    //                    if(!((fileName.contains("timing"))||(fileName.contains("level"))||(fileName.contains("bistinfo")))){
    //                        System.out.println("In Loop" + fileName);
    //                        
    //                        read_Action_Flow_Test_Equation_PatternBurst_Timing_Level(subFile);
    //                    }

                        if (fileName.contains("equation")){
    //                        System.out.println("start Reading Equations File " + fileName);
                            readEquation(subFile);
                        } else if (fileName.contains("flow_")/*||(subFile.getParent().contains("flow"))*/) {
    //                        System.out.println("start Reading Flow Table File " + fileName);
                            readFlowTables(subFile);
                        } else if (fileName.contains("description")) {
                            readTestDescription(subFile);
    ////                       
//                            System.out.println("start Reading GenericBlock File " + fileName);
                        } else if (fileName.contains("timing")) {
                            readTiming(subFile);
//                            System.out.println("start Reading Timing File " + fileName);
                        } else if (fileName.contains("level")) {
                            readLevels(subFile);
//                            System.out.println("start Reading Level File " + fileName);
                        } else if (fileName.contains("test_")) {
                            readTest(subFile);
    //                        System.out.println("start Reading Test File " + fileName);
                        } else if (fileName.contains("testprogram")||fileName.contains("test_program")) {
                            readAction(subFile);
                            if(getActionList)
                                TestProgramFile += subFile.getAbsolutePath();
//                            if (readActionList(subFile) == true) {
//                            }
    //                        System.out.println("start Reading TestProgram File " + fileName);
                        } else if (fileName.contains("dcpattern")) {
    //                      
//                            System.out.println("start Reading DC Pattern File " + fileName);
                        } else if (fileName.contains("pattern")) {
                            readPatternBurst(subFile);
    //                        System.out.println("start Reading Pattern Burst File " + fileName);
                        } else if (parentName.contains("equation")) {
    //                        System.out.println("start Reading Equations File " + fileName);
                            readEquation(subFile);
                        } else if (fileName.contains("flow")||(parentName.contains("flow"))) {
    //                        System.out.println("start Reading Flow Table File " + fileName);
                            readFlowTables(subFile);
                        
                        } else if (fileName.contains("test") && (!fileName.contains("descr"))) {
                            readTest(subFile);
    //                        System.out.println("start Reading Test File " + fileName);
                        }  else if (fileName.contains("relay")) {
////                            System.out.println("start Reading LoadBoard File " + fileName);
                            readLoadboard(subFile);
                        } else if(fileName.contains("result")&&(! fileName.contains("vector"))){
//                            System.out.println("start Reading ResultSpec File " + fileName);
                            readResultSpec(subFile);
                        } else if(fileName.contains("result")&&(fileName.contains("vect"))){
//                            System.out.println("start Reading VectorResult File " + fileName);
                            readVectorResult(subFile);
                        }
                        else if(fileName.contains("compare")){
//                            System.out.println("start Reading Compare File " + fileName);
                            readCompare(subFile);
                        } else if(fileName.contains("dcsequence")||fileName.contains("dcpattern")){
//                            System.out.println("start Reading DC sequence/control/pattern File " + fileName);
                            readDCs(subFile);
                        }
                        else if(fileName.contains("binning.xml")){
                            readTest(subFile);
                        }
                        else if(fileName.contains("softset")||fileName.contains("soft_set")){
                            readSoftSet(subFile);
                        }
                        else if(fileName.contains("axislist")){
                            readAxisList(subFile);
                        }
//                        else if(fileName.contains("signal")){
//                            if(fileName.contains("group")){
//                                this.signalGroupFile=fullName;
//                                
//                            }
//                            else{
//                                this.signalsFile=fullName;
//                                
//                            }
//                        
//                        }
                        else {
                            System.out.println("SKIP this file " + path);
                            skipFiles.add(subFile);
    //                        if(subFile.getTotalSpace()>1000000){
    //                            System.out.println(fileName +"  is Test Number file");
    //                        }
                        }
                    }
                    else{
                        unusedFileList.add(subFile);
                        System.out.println("Skip " + path + " due to File Filter ");
                    }
                    
                } else {
                    unusedFileList.add(subFile);
//                    System.out.println("None XML file or Big size GT 800K");
                    if(fileSize>800000 &&fileName.endsWith("xml"))
                        System.out.println("Big XML file, SKIP this file " + path);
                    
                }
            }
        }
       
    }
    public void LoadFile(File inFile) throws DocumentException {
        File[] fileList = inFile.listFiles(fileFilter);
        for (File subFile : fileList) {
            if (subFile.isDirectory()) {
                boolean notTarget = false;
                for (String filter : folderNameFilter) {
                    if (subFile.getName().toLowerCase().contains(filter)) {
                        notTarget = true;
                        System.out.println("Skipped  " + subFile.getName());
                        break;
                    }
                }
                if (!notTarget) {
                   
                    
                    LoadFile(subFile);
                }
            } else {
                
                String fileName = subFile.getName().toLowerCase();
                String path = subFile.getAbsolutePath();
           
                String parentName=subFile.getParentFile().getName();
                String fullName=subFile.getAbsolutePath();
                FileInputStream fileInStream = null;
                long fileSize=0;
                try {
                    fileInStream = new FileInputStream(subFile);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    fileSize= fileInStream.available();
                } catch (IOException ex) {
                    Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if (fileName.endsWith("xml")&& fileSize<800000){                
//                    System.out.println(fileName);
                    if(!this.inValidFiles.contains(fullName)){
                        xmlFileList.add(subFile);
    //                    if(!((fileName.contains("timing"))||(fileName.contains("level"))||(fileName.contains("bistinfo")))){
    //                        System.out.println("In Loop" + fileName);
    //                        
    //                        read_Action_Flow_Test_Equation_PatternBurst_Timing_Level(subFile);
    //                    }

                        if (fileName.contains("equation")){
    //                        System.out.println("start Reading Equations File " + fileName);
                            readEquation(subFile);
                        } else if (fileName.contains("flow_")/*||(subFile.getParent().contains("flow"))*/) {
    //                        System.out.println("start Reading Flow Table File " + fileName);
                            readFlowTables(subFile);
                        } else if (fileName.contains("description")) {
                            readTestDescription(subFile);
    ////                       
//                            System.out.println("start Reading GenericBlock File " + fileName);
                        } else if (fileName.contains("timing")) {
                            readTiming(subFile);
//                            System.out.println("start Reading Timing File " + fileName);
                        } else if (fileName.contains("level")) {
                            readLevels(subFile);
//                            System.out.println("start Reading Level File " + fileName);
                        } else if (fileName.contains("test_")) {
                            readTest(subFile);
    //                        System.out.println("start Reading Test File " + fileName);
                        } else if (fileName.contains("testprogram")) {
                            TestProgramFile += subFile.getAbsolutePath();
                            if (readActionList(subFile) == true) {
                            }
    //                        System.out.println("start Reading TestProgram File " + fileName);
                        } else if (fileName.contains("dcpattern")) {
    //                      
                            System.out.println("start Reading DC Pattern File " + fileName);
                        } else if (fileName.contains("pattern")) {
                            readPatternBurst(subFile);
    //                        System.out.println("start Reading Pattern Burst File " + fileName);
                        } else if (parentName.contains("equation")) {
    //                        System.out.println("start Reading Equations File " + fileName);
                            readEquation(subFile);
                        } else if (fileName.contains("flow")||(parentName.contains("flow"))) {
    //                        System.out.println("start Reading Flow Table File " + fileName);
                            readFlowTables(subFile);
                        } else if (fileName.contains("test") && (!fileName.contains("descr"))) {
                            readTest(subFile);
    //                        System.out.println("start Reading Test File " + fileName);
                        } 
    //                    else if (parentName.contains("binningdoc")) {
    ////                        System.out.println("start Reading Equations File " + fileName);
    ////                        readEquation(subFile);
    //                    } 
                        else if (fileName.contains("relay")) {
                            System.out.println("start Reading LoadBoard File " + fileName);
                            readLoadboard(subFile);
                        }

                        else {
                            System.out.println("SKIP this file " + path);
                            skipFiles.add(subFile);
    //                        if(subFile.getTotalSpace()>1000000){
    //                            System.out.println(fileName +"  is Test Number file");
    //                        }
                        }
                    }
                    
                } else {
                    if(fileSize>800000 &&fileName.endsWith("xml"))
                        System.out.println("Big XML file, SKIP this file " + path);
                    
                }
            }
        }
        
    }
    
    /*
    private void read_Action_Flow_Test_Equation_PatternBurst_Timing_Level(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        final String fileName = file.getAbsolutePath();
        //For Test Handler
        reader.addHandler( "/blocks/Test",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
           
                tests.add(new testElement(row, fileName, null));
                // prune the tree
                row.detach();
            }
        });
        //For Flow Tables Handler
        reader.addHandler( "/blocks/Flow",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read Flow " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Flow " + flowName);    
                readSubFlow(flowName, row, fileName);
                // prune the tree
                row.detach();
            }
        });
        //For Equations Handler
        reader.addHandler( "/blocks/Equations",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
                Equation equation = new Equation(row, fileName);
                equations.put(equation.getName(), equation);
                // prune the tree
                row.detach();
            }
        });        
        // For Pattern Burst Handler
        reader.addHandler( "/blocks/PatternBurst",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
                PatternBurst burst = new PatternBurst(row, fileName, patNo++);
                patternBursts.put(burst.getName(), burst);
                row.detach();
            }
        });
        //For FlowOverride Handler
        reader.addHandler( "/blocks/FlowOverride/FlowContext",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read FlowOverride FlowContext " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        });
        //For SoftSet Handler
         reader.addHandler( "/blocks/SoftSetGroup",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read SoftSetGroup " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        });  
            reader.addHandler( "/blocks/SoftSet",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read SoftSet " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        });  
        // For DCDCSequence Handler    
        reader.addHandler( "/blocks/DCSequence",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read DCSequence " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        });  
        
        // For DCDCSequenceControl Handler
        reader.addHandler( "/blocks/DCSequenceControl",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read DCSequenceControl " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        });  
        // For GenericBlock Handler
        reader.addHandler( "/blocks/TestDescription",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read TestDescription " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        });   
         // For VectorResult Handler
        reader.addHandler( "/blocks/VectorResult",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read VectorResult " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        });
         // For Triggers Handler
        reader.addHandler( "/blocks/Triggers",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read Triggers " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        });  
        // For ResultSpec Handler
        reader.addHandler( "/blocks/ResultSpec",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read ResultSpec " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        }); 
        
        // For Compare Handler
        reader.addHandler( "/blocks/Compare",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read Compare " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        }); 
        
        // For Binning Handler
        reader.addHandler( "/blocks/Binning",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read Binning " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        }); 
        
        // For Binning Handler
        reader.addHandler( "/blocks/BinMap",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read BinMap " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        }); 
        
         // For SignalGroups/Group Handler
        reader.addHandler( "/blocks/SignalGroups/Group",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
                System.out.println("Start to read SignalGroups Group " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
             
                row.detach();
            }
        }); 
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */

    private boolean readPatternBurst(Document document, String fileName) {
        List<Element> nodes = document.selectNodes("//blocks/PatternBurst");
        if (nodes.isEmpty()) {
//            System.out.println(file.getAbsolutePath() + " is not a PatternBurst file");
//            this.skipFiles.add(file);
            return false;
        }

        else{
            String patBurstName = "";
            int i = 0;
            for (Element element : nodes) {
                PatternBurst burst = new PatternBurst(element, fileName, patNo++);
                XMLRead.patternBursts.put(burst.getName(), burst);
            }
            document = null;
            return true;
        }
    }
    private void readPatternBurst(File file) {

        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        int patternCnt= XMLRead.patternBursts.size();
        final String fileName = file.getAbsolutePath();
        reader.addHandler( "/blocks/PatternBurst",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
                PatternBurst burst = new PatternBurst(row, fileName, patNo++);
                patternBursts.put(burst.getName(), burst);
                row.detach();
                
            }
        });  
//        reader.addHandler( "/blocks/PatternBurst",new ElementHandler() {
//            @Override
//            public void onStart(ElementPath path) {
////                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
//            }
//            @Override
//            public void onEnd(ElementPath path) {
//                // process a ROW element
//                Element row = path.getCurrent();
////                Element rowSet = row.getParent();
////                Document document = row.getDocument();
////                String flowName=path.getCurrent().attributeValue("name");
////                System.out.println("End to read Equation " + flowName);    
//                PatternBurst burst = new PatternBurst(row, fileName, patNo++);
//                patternBursts.put(burst.getName(), burst);
//                row.detach();
//                
//            }
//        });
//        
//        reader.addHandler( "/blocks/PatternBurst/ExecutionMode",new ElementHandler() {
//            @Override
//            public void onStart(ElementPath path) {
////                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
//            }
//            @Override
//            public void onEnd(ElementPath path) {
//                // process a ROW element
//                Element row = path.getCurrent();
////                Element rowSet = row.getParent();
////                Document document = row.getDocument();
////                String flowName=path.getCurrent().attributeValue("name");
////                System.out.println("End to read Equation " + flowName);    
//                PatternBurst burst = new PatternBurst(row, fileName, patNo++);
//                patternBursts.put(burst.getName(), burst);
//                row.detach();
//                
//            }
//        }); 
//        reader.addHandler( "/blocks/PatternBurst/Composite",new ElementHandler() {
//            @Override
//            public void onStart(ElementPath path) {
////                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
//            }
//            @Override
//            public void onEnd(ElementPath path) {
//                // process a ROW element
//                Element row = path.getCurrent();
////                Element rowSet = row.getParent();
////                Document document = row.getDocument();
////                String flowName=path.getCurrent().attributeValue("name");
////                System.out.println("End to read Equation " + flowName);    
//                PatternBurst burst = new PatternBurst(row, fileName, patNo++);
//                patternBursts.put(burst.getName(), burst);
//                row.detach();
//                
//            }
//        });
//        
//        reader.addHandler( "/blocks/PatternBurst/ExecutionMode",new ElementHandler() {
//            @Override
//            public void onStart(ElementPath path) {
////                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
//            }
//            @Override
//            public void onEnd(ElementPath path) {
//                // process a ROW element
//                Element row = path.getCurrent();
////                Element rowSet = row.getParent();
////                Document document = row.getDocument();
////                String flowName=path.getCurrent().attributeValue("name");
////                System.out.println("End to read Equation " + flowName);    
//                PatternBurst burst = new PatternBurst(row, fileName, patNo++);
//                patternBursts.put(burst.getName(), burst);
//                row.detach();
//                
//            }
//        }); 
//        
//        reader.addHandler( "/blocks/PatternBurst/CompareRef",new ElementHandler() {
//            @Override
//            public void onStart(ElementPath path) {
////                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
//            }
//            @Override
//            public void onEnd(ElementPath path) {
//                // process a ROW element
//                Element row = path.getCurrent();
////                Element rowSet = row.getParent();
////                Document document = row.getDocument();
////                String flowName=path.getCurrent().attributeValue("name");
////                System.out.println("End to read Equation " + flowName);    
//                PatternBurst burst = new PatternBurst(row, fileName, patNo++);
//                patternBursts.put(burst.getName(), burst);
//                row.detach();
//                
//            }
//        });
//        
//        
//        reader.addHandler( "/blocks/PatternBurst/PatListItem",new ElementHandler() {
//            @Override
//            public void onStart(ElementPath path) {
////                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
//            }
//            @Override
//            public void onEnd(ElementPath path) {
//                // process a ROW element
//                Element row = path.getCurrent();
////                Element rowSet = row.getParent();
////                Document document = row.getDocument();
////                String flowName=path.getCurrent().attributeValue("name");
////                System.out.println("End to read Equation " + flowName);    
//                PatternBurst burst = new PatternBurst(row, fileName, patNo++);
//                patternBursts.put(burst.getName(), burst);
//                row.detach();
//                
//            }
//        });
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(patternCnt==XMLRead.patternBursts.size()){
            System.out.println("Invalid XML file "+ fileName);
           
            this.newInValidFiles.add(file.getAbsolutePath());
        }
    }

    private void readTest(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        final String fileName=file.getAbsolutePath();
        int equCnt= this.equations.size();
        int testCnt= XMLRead.newTests.size();
        int flowCnt=this.flowTables.size();
        int resultSpecCnt= XMLRead.resultSpecs.size();
        int testDescriptionCnt=XMLRead.testDescription.size();
        int binningTestCnt= this.binningTest.size();
        int patternCnt=this.patternBursts.size();
        int softSetCnt= softSet.size();
        int softSetGroupCnt= softSetGroup.size();
        int AxisListCnt=AxisList.size();

        
        
        reader.addHandler( "/blocks/Test",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
           
//                tests.add(new testElement(row, fileName, null));
                newTests.put(testName,new Test(row, fileName, newTests.size()));
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler( "/blocks/Binning",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Binning Test " + testName);    
           
//                tests.add(new testElement(row, fileName, null));
                binningTest.add(new Test(row, fileName, binningTest.size()));
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler( "/blocks/Flow",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Flow " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String flowName=path.getCurrent().attributeValue("name");
                System.out.println("Bad Rule, flow " + flowName + " is found in test file " + fileName);    
                readSubFlow(flowName, row, fileName);
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler( "/blocks/Equations",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String flowName=path.getCurrent().attributeValue("name");
                 
                Equation equation = new Equation(row, fileName);
                equations.put(equation.getName(), equation);
                System.out.println("Bad Rule, Equation " + equation.getName() + " is found in a test file "+ fileName);   
                // prune the tree
                row.detach();
            }
        });    
        reader.addHandler("/blocks/ResultSpec",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                resultSpecs.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler("/blocks/TestDescription",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                testDescription.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler( "/blocks/PatternBurst",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
                PatternBurst burst = new PatternBurst(row, fileName, patNo++);
                patternBursts.put(burst.getName(), burst);
                row.detach();
                
            }
        });
        
        reader.addHandler("/blocks/SoftSet",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                softSet.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler("/blocks/SoftSetGroup",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String testName=path.getCurrent().attributeValue("name");
                String testName= path.getCurrent().attribute(0).getText();
//                System.out.println("End to read Test " + testName);    
                softSetGroup.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });

        reader.addHandler("/blocks/AxisList",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                AxisList.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            System.out.println("Error during read File " + fileName);
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (binningTestCnt== this.binningTest.size() && equCnt== this.equations.size() && testCnt== XMLRead.newTests.size() && flowCnt==this.flowTables.size() && resultSpecCnt== XMLRead.resultSpecs.size()&&testDescriptionCnt==XMLRead.testDescription.size()&&XMLRead.patternBursts.size()==patternCnt&&softSetCnt==softSet.size()&&softSetGroupCnt==softSetGroup.size()&&AxisListCnt==AxisList.size()){
            System.out.println("Invalid XML file "+ fileName);
            this.newInValidFiles.add(file.getAbsolutePath());
        }
//        List<Element> patNodes = document.selectNodes("//blocks/Test/TestParameters/PatternBurst");
//        List<String> test_Pattern = new ArrayList();
//        if (!patNodes.isEmpty()) {
//            for (Element node : patNodes) {
////                System.out.println(node.getParent().getParent().attribute(0));
////                System.out.println(node.getText());
//                test_Pattern.add(node.getParent().getParent().attribute(0).getValue() + "," + node.getText());
//
//
//            }
//        }
//
//        List<Element> nodes = document.selectNodes("//blocks/Test");
//        if (nodes.isEmpty()) {
//            System.out.println(file.getAbsolutePath() + " is not a Test file");
//            this.skipFiles.add(file);
//            document = null;
//        }
//        else{
//            int i = 0;
//            for (Element element : nodes) {
//                tests.add(new testElement(element, file.getAbsolutePath(), test_Pattern));
//            }
//            document = null;
//        }
    }
    
    private void readTestDescription(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        final String fileName=file.getAbsolutePath();
        int equCnt= this.equations.size();
        int testCnt= XMLRead.newTests.size();
        int flowCnt=XMLRead.testDescription.size();
        
        reader.addHandler("/blocks/TestDescription",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                testDescription.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        
        reader.addHandler( "/blocks/Test",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
                System.out.println("End Test is found in Test Description file " + testName);    
           
//                tests.add(new testElement(row, fileName, null));
                newTests.put(testName,new Test(row, fileName, newTests.size()));
                // prune the tree
                row.detach();
            }
        });

        reader.addHandler( "/blocks/Equations",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String flowName=path.getCurrent().attributeValue("name");
                 
                Equation equation = new Equation(row, fileName);
                equations.put(equation.getName(), equation);
                System.out.println("Bad Rule, Equation" + equation.getName() + " is found in a test description file "+ fileName);   
                // prune the tree
                row.detach();
            }
        });    
        
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (equCnt== this.equations.size() && testCnt== XMLRead.newTests.size() && flowCnt==XMLRead.testDescription.size()){
            System.out.println("Invalid XML file "+ fileName);
            this.newInValidFiles.add(file.getAbsolutePath());
        
        }
    }
    
    private void readResultSpec(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        final String fileName=file.getAbsolutePath();
        int equCnt= this.equations.size();
        int testCnt= XMLRead.newTests.size();
        int testDescriptionCnt=XMLRead.testDescription.size();
        int resultSpecCnt=XMLRead.resultSpecs.size();
        
        reader.addHandler("/blocks/TestDescription",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                testDescription.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler("/blocks/ResultSpec",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                resultSpecs.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        
        reader.addHandler( "/blocks/Test",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
                System.out.println("End Test is found in Test Description file " + testName);    
           
//                tests.add(new testElement(row, fileName, null));
                newTests.put(testName,new Test(row, fileName, newTests.size()));
                // prune the tree
                row.detach();
            }
        });

        reader.addHandler( "/blocks/Equations",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String flowName=path.getCurrent().attributeValue("name");
                 
                Equation equation = new Equation(row, fileName);
                equations.put(equation.getName(), equation);
                System.out.println("Bad Rule, Equation" + equation.getName() + " is found in a test description file "+ fileName);   
                // prune the tree
                row.detach();
            }
        });    
        
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (equCnt== this.equations.size() && testCnt== XMLRead.newTests.size() && testDescriptionCnt==XMLRead.testDescription.size() &&resultSpecCnt==XMLRead.resultSpecs.size()){
            System.out.println("Invalid XML file "+ fileName);
            this.newInValidFiles.add(file.getAbsolutePath());
        
        }
    }
    private void readVectorResult(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        final String fileName=file.getAbsolutePath();
    
        int resultSpecCnt=vectorResult.size();
        
        reader.addHandler("/blocks/VectorResult",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                vectorResult.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (resultSpecCnt==vectorResult.size()){
            System.out.println("Invalid XML file "+ fileName);
            this.newInValidFiles.add(file.getAbsolutePath());
        
        }
    }
    private void readSoftSet(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        final String fileName=file.getAbsolutePath();
    
        int softSetCnt=softSet.size();
        int softSetGroupCnt=softSetGroup.size();
        int equationCne=equations.size();
        
        reader.addHandler("/blocks/SoftSet",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                softSet.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler("/blocks/SoftSetGroup",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
//                String testName=path.getCurrent().attributeValue("name");
                String testName= path.getCurrent().attribute(0).getText();
//                System.out.println("End to read Test " + testName);    
                softSetGroup.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler( "/blocks/Equations",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String flowName=path.getCurrent().attributeValue("name");
                 
                Equation equation = new Equation(row, fileName);
                equations.put(equation.getName(), equation);
                System.out.println("Bad Rule, Equation" + equation.getName() + " is found in a test description file "+ fileName);   
                // prune the tree
                row.detach();
            }
        });    
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (softSetCnt==softSet.size()&&softSetGroupCnt==softSetGroup.size()&&equationCne==equations.size()){
            System.out.println("Invalid XML file "+ fileName);
            this.newInValidFiles.add(file.getAbsolutePath());
        
        }
    }
    private void readAxisList(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        final String fileName=file.getAbsolutePath();
        int AxisListCnt=AxisList.size();

        
        reader.addHandler("/blocks/AxisList",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                AxisList.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
  
        
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (AxisListCnt== this.AxisList.size()){
            System.out.println("Invalid XML file "+ fileName);
            this.newInValidFiles.add(file.getAbsolutePath());
        
        }
    }
    private void readCompare(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        final String fileName=file.getAbsolutePath();
        int compareCnt= compares.size();

        
        reader.addHandler("/blocks/Compare",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                compares.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
  
        
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (compareCnt== this.compares.size()){
            System.out.println("Invalid XML file "+ fileName);
            this.newInValidFiles.add(file.getAbsolutePath());
        
        }
    }
    private void readDCs(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        final String fileName=file.getAbsolutePath();
        int DCCnt= DCs.size();

        
        reader.addHandler("/blocks/DCSequenceControl",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                DCs.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler("/blocks/DCPattern",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                DCs.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler("/blocks/DCSequence",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Test " + testName);    
                DCs.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
  
        
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (DCCnt== DCs.size()){
            System.out.println("Invalid XML file "+ fileName);
            this.newInValidFiles.add(file.getAbsolutePath());
        
        }
    }
    /*
    private boolean readTest(Document document, String fileName) {
        List<Element> patNodes = document.selectNodes("//blocks/Test/TestParameters/PatternBurst");
        List<String> test_Pattern = new ArrayList();
        if (!patNodes.isEmpty()) {
            for (Element node : patNodes) {
//                System.out.println(node.getParent().getParent().attribute(0));
//                System.out.println(node.getText());
                test_Pattern.add(node.getParent().getParent().attribute(0).getValue() + "," + node.getText());


            }
        }

        List<Element> nodes = document.selectNodes("//blocks/Test");
        if (nodes.isEmpty()) {
//            System.out.println(file.getAbsolutePath() + " is not a Test file");
//            this.skipFiles.add(file);
            return false;
        }
        else{
            int i = 0;
            for (Element element : nodes) {
                tests.add(new testElement(element,fileName, test_Pattern));
            }
            document = null;
            return true;
        }
    }
    */

    private void readEquation(File file) {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        final String fileName = file.getAbsolutePath();
        final String isValid=null;;
        int equCnt= this.equations.size();
        int testCnt= XMLRead.newTests.size();
        int flowCnt=this.flowTables.size();
        int testDescriptionCnt= XMLRead.testDescription.size();
        
        reader.addHandler( "/blocks/Equations",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
              
//                System.out.println("Start to read Equation " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String flowName=path.getCurrent().attributeValue("name");
//                System.out.println("End to read Equation " + flowName);    
                Equation equation = new Equation(row, fileName);
                equations.put(equation.getName(), equation);
                // prune the tree
                row.detach();
            }
        });    
        reader.addHandler( "/blocks/Test",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
                System.out.println("Bad Rule, test " + testName + " is found in a Equation file " + fileName);    
           
//                tests.add(new testElement(row, fileName, null));
                newTests.put(testName,new Test(row, fileName, newTests.size()));
                // prune the tree
                row.detach();
            }
        });
        
        reader.addHandler( "/blocks/Flow",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Flow " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String flowName=path.getCurrent().attributeValue("name");
                System.out.println("Bad Rule, Flow " + flowName + "  is found in a Equation file "+ fileName);    
                readSubFlow(flowName, row, fileName);
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler("/blocks/TestDescription",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Test " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
//                Element rowSet = row.getParent();
//                Document document = row.getDocument();
                String testName=path.getCurrent().attributeValue("name");
                System.out.println("Bad Rule, TestDescription is found in Equation file " + testName);    
                testDescription.put(testName,new GenericBlock(row, fileName));
                // prune the tree
                row.detach();
            }
        });
        reader.addHandler( "/blocks/Config/Param",new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                System.out.println("Start to read Flow " + path.getCurrent().attributeValue("name")); 
            }
            @Override
            public void onEnd(ElementPath path) {
                Element row = path.getCurrent();
                row.detach();
            }
        });
        
        try {
            document = reader.read(file);
            
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (equCnt== this.equations.size() && testCnt== XMLRead.newTests.size() && flowCnt==this.flowTables.size() &&testDescriptionCnt==XMLRead.testDescription.size()){
           
            this.newInValidFiles.add(file.getAbsolutePath());
         
            System.out.println("Invalid XML file " + fileName);
        }
    }
    private boolean readEquation(Document document, String fileName) {
       
        List<Element> nodes = document.selectNodes("//blocks/Equations");
        if (nodes.isEmpty()) {
//            System.out.println(file.getAbsolutePath() + " is not a Equation file");
//            this.skipFiles.add(file);
            document = null;
            return false;
        }
        else{
            for (Element node : nodes) {
                Equation equation = new Equation(node,fileName);
                this.equations.put(equation.getName(), equation);
//                this.rootNodes.put(equation.getName(), equation.getRoot());
//                System.out.println("load equaitons " + equation.getName());
            }
            document = null;
            return true;
        }
        
    }
 
    public void readTiming(File file){
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        final String fileName=file.getAbsolutePath();
        int timingCnt=XMLRead.timing.size();
        reader.addHandler( "/blocks/Timing", new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
           
            }
            @Override
            public void onEnd(ElementPath path) {
                Element row=path.getCurrent();
                timing.put(row.attributeValue("name"),new Timing(row, fileName));
            }
        });
        Document document = null;
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(timingCnt==XMLRead.timing.size()){
            System.out.println("Invalid XML file " + fileName);
            this.newInValidFiles.add(file.getAbsolutePath());
        }
     }
    public void readLevels(File file){
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        final String fileName=file.getAbsolutePath();
        
        int levelsCnt= XMLRead.levels.size();
    

        reader.addHandler( "/blocks/DCLevels", new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
//                Element row = path.getCurrent();
//                levels.put(row.attributeValue("name"),new Levels(row, fileName));
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
                levels.put(row.attributeValue("name"),new Levels(row, fileName));
//                Element row = path.getCurrent();
                row.detach();
            }
        });

        Document document = null;
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(levelsCnt== XMLRead.levels.size()){
            System.out.println("Invalid XML file "+ fileName);
            this.newInValidFiles.add(file.getAbsolutePath());
        }
    }
    public void readLoadboard(File file){
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        final String fileName=file.getAbsolutePath();
        int loadBoardCnt= XMLRead.loadBoards.size();
        reader.addHandler("/blocks/Loadboard", new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
           
            }
            @Override
            public void onEnd(ElementPath path) {
                Element row=path.getCurrent();
                loadBoards.put(row.attributeValue("name"),new GenericBlock(row, fileName));
                row.detach();
            }
        });
        
        reader.addHandler( "/blocks/TokenValMap", new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
           
            }
            @Override
            public void onEnd(ElementPath path) {
                Element row=path.getCurrent();
                row.detach();
//                loadBoards.add(new Loadboard(row));
            }
        });
        Document document = null;
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(loadBoardCnt== XMLRead.loadBoards.size()){
            System.out.println("Invalid XML file "+ fileName);
            this.newInValidFiles.add(file.getAbsolutePath());
        }
    }
    
    public void readFlowOrerrides(File file){
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        final String fileName=file.getAbsolutePath();
        
        int flowOverrideCnt= this.flowOverrides.size();
    

        reader.addHandler( "/blocks/FlowOverride", new ElementHandler() {
            @Override
            public void onStart(ElementPath path) {
            }
            @Override
            public void onEnd(ElementPath path) {
                // process a ROW element
                Element row = path.getCurrent();
                flowOverrides.put(row.attributeValue("name"),new FlowOverride(row, fileName));
//                Element row = path.getCurrent();
                row.detach();
            }
        });

        Document document = null;
        
        try {
            document = reader.read(file);
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(flowOverrideCnt== this.flowOverrides.size()){
            System.out.println("Invalid XML file "+ fileName);
            this.newInValidFiles.add(file.getAbsolutePath());
        }
    }
    
    public void printFileList(File inFile) {
        File[] fileList = inFile.listFiles(fileFilter);
        for (File subFile : fileList) {
            if (subFile.isDirectory()) {
                System.out.println("Folder Name " + subFile.getName());
                printFileList(subFile);
            } else {
                System.out.println(subFile.getName());
            }

        }
    }

    private void addTableTree(TreeItem root, String flowName, String motherFlowName, String motherEquationsRef, String actionType) {
        
        boolean isFind = false;
        for (FlowTable flowTable : flowTables) {
            if (flowTable.getFlowName().equals(flowName)) {
//                flowTable.setIsUsed(true);
                isFind = true;
                TreeNode startNode;
                startNode = new TreeNode(flowTable.getStartNode(), motherFlowName);
                root.getChildren().add(startNode);
                startNode.setNodeIndex(treeNodeIndex++);
//                if (flowTable.getEquationsRef() != null) {
//                }
//                TreeNode deviceNode;
                if(actionType!=null&& actionType.equals("StartDevice")&& flowTable.getDeviceNodes()!=null){
                        TreeNode deviceNode= new TreeNode(flowTable.getDeviceNodes(),motherFlowName);
                        root.getChildren().add(deviceNode);
//                        deviceNode.setNodeIndex(treeNodeIndex++);
                        int testNo=0;
                        for(Test test: this.binningTest){ 
//                            System.out.println("BinningTest " + test.getRoot().getExpression() + " " + testNo);
                            if (test.getRoot().getName().equals("Binning")&&test.getRoot().getExpression().equals(flowTable.getDeviceNodes().getBinningRef())){
                                deviceNode.setNodeIndex(testNo);
                                break;
                            }
                            testNo++;        
                        }
                        
                }
                else if(actionType!=null&& actionType.equals("EndDevice")&& flowTable.getDeviceNodes()!=null){
                        TreeNode deviceNode= new TreeNode(flowTable.getDeviceNodes(),motherFlowName);
                        root.getChildren().add(deviceNode);
//                        deviceNode.setNodeIndex(treeNodeIndex++);
                        int testNo=0;
                        for(Test test: this.binningTest){ 
//                            System.out.println("BinningTest " + test.getRoot().getExpression() + " " + testNo);
                            if (test.getRoot().getName().equals("Binning")&&test.getRoot().getExpression().equals(flowTable.getDeviceNodes().getBinningRef())){
                                deviceNode.setNodeIndex(testNo);
                                break;
                            }
                            testNo++;        
                        }
                }
                
                
                
                for (BaseNode node : flowTable.getNodes()) {

                    TreeNode item = new TreeNode(node, motherFlowName, motherEquationsRef);

                    root.getChildren().add(item);
                    item.setNodeIndex(treeNodeIndex++);

                    if (node.getNodeType().equalsIgnoreCase("flow")) {
                        addTableTree(item, node.getTestFlowRef(), item.getFlowContext(), item.getEquationsRefs(),null);
//                        if (node.getTestFlowRef().equals("RefPreInit"))
//                            System.out.println("RefPreInit "+ node.getXmlFileName());
//                        item.mySetGraphic(rootIcon);
                    } 
                    else if (node.getNodeType().equalsIgnoreCase("test")) {
                        // here to add the test which is defined in the TestRef in the TestNode
                        if (!node.isTestIsReady()) {
                            
                            Test test= XMLRead.newTests.get(node.getTestFlowRef());
                            if(test!=null){
                                node.setTestIsReady(true);
                                item.setNodeIndex(test.getTestNo());
//                                if(!test.isIsUsed())
//                                    test.setIsUsed(true);
                            }
                            else{
                                System.out.println("Error This Test is not found:  " + node.getTestFlowRef());
                            
                            }
                        }
                        
                    }// this is the end of is test
                }
                for (ExitNode node : flowTable.getExitNodes()) {
                    TreeNode exitNode = new TreeNode(node, motherFlowName);
                    root.getChildren().add(exitNode);
                    exitNode.setNodeIndex(treeNodeIndex++);
                }
                break;
            }
        }
//        if ((!isFind)&&(!reReadFlow)) {
        if (!isFind) {
            System.out.println("Error no Flow Table for FlowRef " + flowName);
//            reReadFlow();
//            reReadFlow=true;
//            addTableTree(root, flowName, motherFlowName, motherEquationsRef);
        }
    }

    private TreeItem buildFlowTree(ActionList action) {
        TreeItem<String> root = new TreeItem(action.getFlowRef());
        String actionType= action.getType();
        treeNodeIndex = 0;
        addTableTree(root, action.getFlowRef(), action.getActionName() + ";", null, actionType);
        return root;
    }

    public void buildActionTree() {
//        buildAllEquationsTree();
        if (this.actionList.isEmpty())
            System.out.println("Empty Action");
        for (ActionList _action: this.actionList) {
            System.out.println("Build Action " + _action.getActionName());
            actionTree.add(buildFlowTree(_action));
        }
    }

    public TreeItem buildEquationTree(Equation equation) {
        if(equation!=null){
//            System.out.println("Equation " + equation.getName() +" in File " + equation.getFileName());
            try{
//                if(!currentEquation.contains(equation.getName()))
                TreeItem root= this.equationRootNodes.get(equation.getName());
                if (root==null){
                    root = new TreeItem(new EquationNodeCell(equation.getName(),equation.getFileName()));
                    this.equationRootNodes.put(equation.getName(), root);
                    currentEquation.add(equation.getName());
                    for (int i = 0; i != equation.getTreeNodes().size(); i++) {
                        if (equation.getGroupList().get(i).getGroupType() == 0) {
                            equation.getTreeNodes().get(i).setExpanded(false);
                            root.getChildren().add(equation.getTreeNodes().get(i));
                        } else {
                            if(this.equations.get(equation.getGroupList().get(i).getNodes().get(0).getName())!=null){
                                root.getChildren().add(buildEquationTree(this.equations.get(equation.getGroupList().get(i).getNodes().get(0).getName())));
                            }
                            else{
                                this.equations.put(equation.getGroupList().get(i).getNodes().get(0).getName(), new Equation(equation.getGroupList().get(i).getNodes().get(0).getName())); // this is a Empty Equation
                                root.getChildren().add(buildEquationTree(this.equations.get(equation.getGroupList().get(i).getNodes().get(0).getName())));
                                System.out.println("Error in buildEquationTree, Equation " + equation.getGroupList().get(i).getNodes().get(0).getName() +" doesn't exist");
                            }
                        }
                    }
                }
                else{
                    if(!currentEquation.contains(equation.getName())){
                        currentEquation.add(equation.getName());
                        if (!root.isLeaf()) root.getChildren().clear();
                        for (int i = 0; i != equation.getTreeNodes().size(); i++) {
                            if (equation.getGroupList().get(i).getGroupType() == 0) {
                                equation.getTreeNodes().get(i).setExpanded(false);
                                root.getChildren().add(equation.getTreeNodes().get(i));
                            } else {
                                if(this.equations.get(equation.getGroupList().get(i).getNodes().get(0).getName())!=null){
                                    root.getChildren().add(buildEquationTree(this.equations.get(equation.getGroupList().get(i).getNodes().get(0).getName())));
                                }
                                else{
                                    this.equations.put(equation.getGroupList().get(i).getNodes().get(0).getName(), new Equation(equation.getGroupList().get(i).getNodes().get(0).getName()));
                                    root.getChildren().add(buildEquationTree(this.equations.get(equation.getGroupList().get(i).getNodes().get(0).getName())));

                                    System.out.println("Error in buildEquationTree, Equation " + equation.getGroupList().get(i).getNodes().get(0).getName() +" doesn't exist");
        //                            return null;
                                }
                            }
                        }

                        root.setExpanded(false);
                    }
                    else{
                        if(!currentEquation.contains(equation.getName()+"_N0")){
                            root=this.repeatEquationRootNodes.get(equation.getName()+"_N0");
                            if(root!=null){
                                currentEquation.add(equation.getName()+"_N0");
                            }
                            // this is to save memory, ignore the repeat Equations
                            else{
                                EquationNodeCell equationNodeCell=new EquationNodeCell(equation.getName(),equation.getFileName());
                                equationNodeCell.update();
                                root = new TreeItem(equationNodeCell);
                                this.equationRootNodes.put(equation.getName()+"_N0", root);
                                currentEquation.add(equation.getName()+"_N0");
                            }
                        }
                        else if(!currentEquation.contains(equation.getName()+"_N1")){
                             root=this.repeatEquationRootNodes.get(equation.getName()+"_N1");
                            if(root!=null){
                                currentEquation.add(equation.getName()+"_N1");
                            }
                            else{
                                EquationNodeCell equationNodeCell=new EquationNodeCell(equation.getName(),equation.getFileName());
                                equationNodeCell.update();
                                
                                root = new TreeItem(equationNodeCell);
                                this.equationRootNodes.put(equation.getName()+"_N1", root);
                                currentEquation.add(equation.getName()+"_N1");
                            }
                        }
                        else if(!currentEquation.contains(equation.getName()+"_N2")){
                             root=this.repeatEquationRootNodes.get(equation.getName()+"_N2");
                            if(root!=null){
                                currentEquation.add(equation.getName()+"_N2");
                            }
                            else{
                                EquationNodeCell equationNodeCell=new EquationNodeCell(equation.getName(),equation.getFileName());
                                equationNodeCell.update();
                                
                                root = new TreeItem(equationNodeCell);
                                this.equationRootNodes.put(equation.getName()+"_N2", root);
                                currentEquation.add(equation.getName()+"_N2");
                            }
                        }
                        else if(!currentEquation.contains(equation.getName()+"_N3")){
                             root=this.repeatEquationRootNodes.get(equation.getName()+"_N3");
                            if(root!=null){
                                currentEquation.add(equation.getName()+"_N3");
                            }
                            else{
                                EquationNodeCell equationNodeCell=new EquationNodeCell(equation.getName(),equation.getFileName());
                                equationNodeCell.update();
                                
                                root = new TreeItem(equationNodeCell);
                                this.equationRootNodes.put(equation.getName()+"_N3", root);
                                currentEquation.add(equation.getName()+"_N3");
                            }
                        }
                        else if(!currentEquation.contains(equation.getName()+"_N4")){
                             root=this.repeatEquationRootNodes.get(equation.getName()+"_N4");
                            if(root!=null){
                                currentEquation.add(equation.getName()+"_N4");
                            }
                            else{
                                EquationNodeCell equationNodeCell=new EquationNodeCell(equation.getName(),equation.getFileName());
                                equationNodeCell.update();
                                
                                root = new TreeItem(equationNodeCell);
                                this.equationRootNodes.put(equation.getName()+"_N4", root);
                                currentEquation.add(equation.getName()+"_N4");
                            }
                        }
                        else if(!currentEquation.contains(equation.getName()+"_N5")){
                             root=this.repeatEquationRootNodes.get(equation.getName()+"_N5");
                            if(root!=null){
                                currentEquation.add(equation.getName()+"_N5");
                            }
                            else{
                                EquationNodeCell equationNodeCell=new EquationNodeCell(equation.getName(),equation.getFileName());
                                equationNodeCell.update();
                                
                                root = new TreeItem(equationNodeCell);
                                this.equationRootNodes.put(equation.getName()+"_N5", root);
                                currentEquation.add(equation.getName()+"_N5");
                            }
                        }
                        else if(!currentEquation.contains(equation.getName()+"_N6")){
                             root=this.repeatEquationRootNodes.get(equation.getName()+"_N6");
                            if(root!=null){
                                currentEquation.add(equation.getName()+"_N6");
                            }
                            else{
                                EquationNodeCell equationNodeCell=new EquationNodeCell(equation.getName(),equation.getFileName());
                                equationNodeCell.update();
                                
                                root = new TreeItem(equationNodeCell);
                                this.equationRootNodes.put(equation.getName()+"_N6", root);
                                currentEquation.add(equation.getName()+"_N6");
                            }
                        }
                        else if(!currentEquation.contains(equation.getName()+"_N7")){
                             root=this.repeatEquationRootNodes.get(equation.getName()+"_N7");
                            if(root!=null){
                                currentEquation.add(equation.getName()+"_N7");
                            }
                            else{
                                EquationNodeCell equationNodeCell=new EquationNodeCell(equation.getName(),equation.getFileName());
                                equationNodeCell.update();
                                
                                root = new TreeItem(equationNodeCell);
                                this.equationRootNodes.put(equation.getName()+"_N7", root);
                                currentEquation.add(equation.getName()+"_N7");
                            }
                        }
                        else if(!currentEquation.contains(equation.getName()+"_N8")){
                             root=this.repeatEquationRootNodes.get(equation.getName()+"_N8");
                            if(root!=null){
                                currentEquation.add(equation.getName()+"_N8");
                            }
                            else{
                                EquationNodeCell equationNodeCell=new EquationNodeCell(equation.getName(),equation.getFileName());
                                equationNodeCell.update();
                                
                                root = new TreeItem(equationNodeCell);
                                this.equationRootNodes.put(equation.getName()+"_N8", root);
                                currentEquation.add(equation.getName()+"_N8");
                            }
                        }
                        else if(!currentEquation.contains(equation.getName()+"_N9")){
                             root=this.repeatEquationRootNodes.get(equation.getName()+"_N9");
                            if(root!=null){
                                currentEquation.add(equation.getName()+"_N9");
                            }
                            else{
                                EquationNodeCell equationNodeCell=new EquationNodeCell(equation.getName(),equation.getFileName());
                                equationNodeCell.update();
                                
                                root = new TreeItem(equationNodeCell);
                                this.equationRootNodes.put(equation.getName()+"_N9", root);
                                currentEquation.add(equation.getName()+"_N9");
                            }
                        }
//                        if(root.isLeaf()){
//                            for (int i = 0; i != equation.getTreeNodes().size(); i++) {
//                                if (equation.getGroupList().get(i).getGroupType() == 0) {
//                                    equation.getRepeatTreeNodes().get(i).setExpanded(false);
//                                    // this is to save memory to ignore repeat Equation
////                                    root.getChildren().add(equation.getRepeatTreeNodes().get(i));
//                                } 
//                                else {
//                                    if(this.equations.get(equation.getGroupList().get(i).getNodes().get(0).getName())!=null){
//                                        root.getChildren().add(buildEquationTree(this.equations.get(equation.getGroupList().get(i).getNodes().get(0).getName())));
//                                    }
//                                    else{
//                                        this.equations.put(equation.getGroupList().get(i).getNodes().get(0).getName(), new Equation(equation.getGroupList().get(i).getNodes().get(0).getName()));
//                                        root.getChildren().add(buildEquationTree(this.equations.get(equation.getGroupList().get(i).getNodes().get(0).getName())));
//                                        System.out.println("Error in buildEquationTree, Equation " + equation.getGroupList().get(i).getNodes().get(0).getName() +" doesn't exist");
//                                        
//                                    }
//                                }
//                            }
//                        }

                    }
                    root.setExpanded(false);
                }

                
                return root;
            }catch(Exception e){
                System.out.println(e.getStackTrace().toString());
                return null;
            }
            
        }
        else {
            System.out.println("Error in buildEquationTree, Equation doesn't exist");
            return null;
        }
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
    
    public void clearRoot(TreeItem root){
        if(!this.repeatEquationRootNodes.containsValue(root)){
            ObservableList children= root.getChildren();
            if (children!=null){
                root.getChildren().clear();
                for(int i=0; i!= children.size();i++){
                    TreeItem item= (TreeItem) children.get(i);
                    if(!item.isLeaf())
                        clearRoot(item);
                }

            }
        }
            
        
        
    }
//    public void buildAllEquationsTree() {
//        for (Equation equation : this.equations.values()) {
//            System.out.println(equation.getName());
////            if (equation.getName().equals("SetupDefault")) {
////                System.out.println("get it");
////            }
//            for (Group group : equation.getGroupList()) {
//                if (group.getGroupType() != 0) {
//                    this.rootNodes.get(equation.getName()).getChildren().add(this.rootNodes.get(group.getName()));
//                } else {
//                    this.rootNodes.get(equation.getName()).getChildren().add(group.getVariableRootNode());
//                }
//            }
//        }
//    }

    public final class TreeNode extends TreeItem {

        private int nodeIndex = -1; // this is for test element index for this node
        private int flowNo = 0;
        private int nodeNo = 0;
        private String nodeType = "";
        private String flowContext = "";
        private String equationsRef = null;
        private String testFlowRef=null;
        //private List<String> nodeText=new ArrayList<>();
        final private List<String> validEquaitons = new ArrayList<>();
        final private List<equationNode> overRideEqnNodes= new ArrayList<>();
        private boolean overRide=false;
        private String overRideString = null;
        private String flowOverrideFile=null;
        private String overRideEqu=null;
        private boolean used=false;

        public TreeNode(StartNode node, String motherFlowContext) {
            setGraphic(new ImageView(new Image(getClass().getResourceAsStream("start.gif"))));
            this.flowNo = node.getFlowNo();
            this.nodeNo = node.getNodeNo();
            super.setValue(node.getNodeName());
            
            this.nodeType = node.getNodeType();
            if (!motherFlowContext.contains("^")) {
                this.flowContext = motherFlowContext + node.getFlowName()+ "^" + node.getNodeName();
            }else{
                this.flowContext = motherFlowContext + "|" +  node.getFlowName()+ "^" + node.getNodeName();
            }
            used=true;
        }
        public TreeNode(DeviceNode node, String motherFlowContext) {
            setGraphic(new ImageView(new Image(getClass().getResourceAsStream("device.gif"))));
            this.flowNo = node.getFlowNo();
            this.nodeNo = node.getNodeNum();
            super.setValue(node.getName());
            
            this.nodeType = node.getNodeType();
            if (!motherFlowContext.contains("^")) {
                this.flowContext = motherFlowContext + node.getFlowName()+ "^" + node.getName();
            }else{
                this.flowContext = motherFlowContext + "^" + node.getName();
            }
            used=node.isUsed();
        }

        public TreeNode(BaseNode node, String motherFlowContext, String motherEquationsRef) {
            this.flowNo = node.getFlowNo();
            this.nodeNo = node.getNodeNo(); // Node No start from Start node
            super.setValue(node.getName());
            this.nodeType = node.getNodeType();
            this.testFlowRef=node.getTestFlowRef();

            if (motherFlowContext.contains("^")) {
                this.flowContext = motherFlowContext + "|" + node.getFlowName() + "^" + node.getName();
            } else {
                this.flowContext = motherFlowContext + node.getFlowName() + "^" + node.getName();
            }
            if (motherEquationsRef != null) {
                if (node.getEquationsRef() != null) {
                    this.equationsRef = motherEquationsRef + "," + node.getEquationsRef();
                } else {
                    this.equationsRef = motherEquationsRef;
                }
            } else {
                this.equationsRef = node.getEquationsRef();
            } 
//            if (this.nodeType.equals("test"))
//                this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("config/run_test.gif"))));
             
            
                if(this.nodeType.equals("Test")){
                    
                    //skip this equationRefs in the test
//                    Test test=newTests.get(this.testFlowRef);
//                    if(test!=null){
//                        if(test.getEquationRef()!=null)       
//                             this.equationsRef=this.equationsRef+"," +newTests.get(this.testFlowRef).getEquationRef();
//                    }
//                    System.out.println(node.getBaseNode().getName() + "  set graphic as test");
                    setGraphic(new ImageView(new Image(getClass().getResourceAsStream("run_test.gif"))));}

                else if(this.nodeType.equals("Flow")){
//                    System.out.println(node.getBaseNode().getName() + "  set graphic as flow");
                    setGraphic(new ImageView(new Image(getClass().getResourceAsStream("testflow_tm.png"))));
                }
                
                for(FlowOverride flowOverride:flowOverrides.values() ){
                    FlowContext temp =flowOverride.contains(this.flowContext);
                    if(temp!=null){
                        this.overRide=true;
                        Bypass bypass= temp.getByPass();
                        this.overRideString= temp.getByPass().getEquationRef()+"."+ temp.getByPass().getVariableName();
                        overRideEqu=temp.getByPass().getEquationRef();
                        this.flowOverrideFile=flowOverride.getFileName();
                        break;
                    }
                }
                    
           checkEquationRefs();
                   
           if(this.nodeType.equals("Test")){
                Test test=newTests.get(this.testFlowRef);
                if(test!=null && test.isUsed()){
                    for(String equ: this.validEquaitons){
                        if(XMLRead.equations.containsKey(equ)){
                            XMLRead.equations.get(equ).setUseFul(true);
                        }
                    }
                    for(String equ: test.getEquationRef()){
                        if(XMLRead.equations.containsKey(equ)){
                            XMLRead.equations.get(equ).setUseFul(true);
                        }
                    }
                    test.checkEquInTestDescription();
                    
                    // here to add the equation which is used in flow override control
                    if(this.overRideEqu!=null){
                        Equation equ = XMLRead.equations.get(overRideEqu);
                        if(equ!=null)
                            equ.setUseFul(true);
                    }

                }
                
           }
           used=node.isUsed();
        }

        public TreeNode(ExitNode node, String motherFlowContext) {
            this.flowNo = node.getFlowNo();
            this.nodeNo = node.getNodeNo();
            super.setValue(node.getNodeName());
            this.nodeType = node.getNodeType();
            if (!motherFlowContext.contains("^")) {
                this.flowContext = motherFlowContext + node.getFlowName()+"^" + node.getNodeName();
            }
            else{
                this.flowContext = motherFlowContext + "|"+node.getFlowName() +  "^" + node.getNodeName();
            }
            
            if(node.getDecision().equals("Pass")){
                setGraphic(new ImageView(new Image(getClass().getResourceAsStream("goodBin.gif"))));
            }
            else
                setGraphic(new ImageView(new Image(getClass().getResourceAsStream("badBin.gif"))));
            used=true;
        }
        

        public boolean isUsed() {
            return used;
        }
 
        
        public String getFlowOverrideFile() {
            return flowOverrideFile;
        }
      

        public String getOverRideString() {
            return overRideString;
        }
        

        public boolean isOverRide() {
            return overRide;
        }

        public String getTestFlowRef() {
            return testFlowRef;
        }

        public int getNodeNo() {
            return nodeNo;
        }

        public int getFlowNo() {
            return flowNo;
        }

        public String getNodeType() {
            return nodeType;
        }

        public BaseNode getBaseNode() {
            if(flowTables.get(this.flowNo).getDeviceNodes()!=null)
                return flowTables.get(this.flowNo).getNodes().get(this.nodeNo - 2);
            else
                return flowTables.get(this.flowNo).getNodes().get(this.nodeNo - 1);
        }
        
        public DeviceNode getDeviceNode(){
            return flowTables.get(this.flowNo).getDeviceNodes();
        }
        public String getDeviceNodeFile(){
            return flowTables.get(this.flowNo).getFileName();
        }

        public StartNode getStartNode() {
            return flowTables.get(this.flowNo).getStartNode();
        }

        public ExitNode getExitNode() {
            int startFromNo = flowTables.get(this.flowNo).getNodeCnt() - flowTables.get(this.flowNo).getExitNodes().size();
//            System.out.println("Start From is" + startFromNo);
            return flowTables.get(this.flowNo).getExitNodes().get(this.nodeNo - startFromNo);
        }

        public String getFlowContext() {
            return flowContext;
        }

        public void setFlowContext(String flowContext) {
            this.flowContext = flowContext;
        }

        public String getEquationsRefs() {
            return this.equationsRef;
        }

        public int getNodeIndex() {
            return nodeIndex;
        }

        public void setNodeIndex(int nodeIndex) {
            this.nodeIndex = nodeIndex;
//            System.out.println("nodeIndex is " + nodeIndex);
        }

        private void mySetGraphic(javafx.scene.Node node) {
            super.setGraphic(node);
        }

//        public void getEquationTree() {
////            if(this.equationsRef=="") {
////                return null;
////            }
//            if (this.equationsRef != null) {
//                String[] refs = this.equationsRef.split(",");
//                if (refs.length > 1) {
//                    if (rootNodes.get(this.equationsRef) == null) {
//                        rootNodes.put(this.equationsRef, new TreeItem(new EquationNodeCell(this.equationsRef, null )));
//                        for (int i = 0; i != refs.length; i++) {
//                            rootNodes.get(this.equationsRef).getChildren().add(rootNodes.get(refs[i]));
//                        }
//                    }
//                }
//            }
//        }
        public void checkEquationRefs(){
            if (this.equationsRef != null) {
                String[] refs = this.equationsRef.split(",");
                for(int i=0;i!=refs.length;i++){
                    if(!this.validEquaitons.contains(refs[i]))
                        this.validEquaitons.add(refs[i]);
                    checkEquation(refs[i]);
                }
                    
            }

        }
        public void checkEquation(String equationName){
            Equation equation=equations.get(equationName);
            if(equation!=null){
                if(!equation.getIsUsed()){
                    equation.setIsUsed(true);
                    for(Group group:equation.getGroupList() ){
                        if(group.getGroupType()==1){
                            String eqnName =group.getNodes().get(0).getName();
                            if(!this.validEquaitons.contains(eqnName))
                                this.validEquaitons.add(eqnName);
                            checkEquation(eqnName);
                        }
                        else{
                            if(XMLRead.evaluationOn){
                                for(equationNode eqnNode:group.getNodes() ){
                                    if(eqnNode!=null && eqnNode.getType()==0){
                                  
                                        if(eqnNode.isValidVariable()){
                                            eqnNode.setIsValid(true);
//                                            System.out.print(" valid number");
                                        }
                                        else{
//                                            System.out.print(" Invalid number");
//                                            System.out.println("Split " + eqnNode.getExpression());
                                            eqnNode.splitVariables();
                                        }
                                

                                    }
                                }
                            }
                            
                        }
                    }
                }
                else{
                    for(Group group:equation.getGroupList() ){
                        if(group.getGroupType()==1){
                            String eqnName =group.getNodes().get(0).getName();
                            if(!this.validEquaitons.contains(eqnName))
                                this.validEquaitons.add(eqnName);
                            checkEquation(eqnName);
                        }
                    }
                }
                // here to add valid equation...
            }
            
        }
        public void refreshVariables(){
            variables.clear();
            this.overRideEqnNodes.clear();// add on 5/21/2012
            if (this.equationsRef != null) {
                String[] refs = this.equationsRef.split(",");
                for(String eqnName:refs){
                    addVariables(eqnName);
                }
                    
            }
        }
        private void addVariables(String eqnName){
            Equation equation=equations.get(eqnName);
            if(equation!=null){
                for(Group group:equation.getGroupList() ){
                    if(group.getGroupType()==0){
                        for(equationNode eqnNode: group.getNodes()){
                            if (eqnNode.isIsOverride())
                                eqnNode.recoverValue();
                            
                            if(variables.containsKey(eqnNode.getName())){
                                this.overRideEqnNodes.add(variables.get(eqnNode.getName()));
                            }
                            variables.put(eqnNode.getName(), eqnNode);
                            if(!eqnNode.isIsValid()){
                                eqnNode.setIsEvaluated(false);
                                eqnNode.setCanEvaluated(true);
                                eqnNode.setWaitEvaluated(false);
                                eqnNode.setValue(eqnNode.getExpression());
                            }
                        }
                    }
                    else{
                        addVariables(group.getNodes().get(0).getName());
                    }
                }
            }
        }
        public String toNumber(String expression){
            if(expression.contains("E")){
                String[] result=expression.split("E");
                if(result.length!=2)
                    return null;
                else{
                    String temp;
                    temp= addZero(result[1]);
                    if(result[1].contains("-")){
                        if(temp!=null)
                            return "(" + result[0] + "/"+ temp +")";
                        else 
                            return null;
                    }
                    else
                        if(temp!=null){
                            return "(" + result[0] + "*"+ temp +")";
                        }
                        else
                            return null;
                    
                }
            }
            if(expression.contains("e")){
                String[] result=expression.split("e");
                if(result.length!=2)
                    return null;
                else{
                    String temp;
                    temp= addZero(result[1]);
                    if(result[1].contains("-")){
                        if(temp!=null)
                            return "(" + result[0] + "/"+ addZero(result[1]) +")";
                        else
                            return null;
                    }
                    else{
                        if(temp!=null)
                            return "(" + result[0] + "*"+ addZero(result[1]) +")";
                        else
                            return null;
                    }
                    
                    
                }
            }
            else
                return null;
        }
        private String addZero(String expression){
            String number=expression;
            if(number.contains("-")||number.contains("+")){
                number=number.substring(1);
            }
            switch(number){
                case "0":
                    return "1";
                case "1":
                    return "10";
                case "2":
                    return "100";
                case "3":
                    return "1000";
                case "4":
                    return "10000";
                case "5":
                    return "100000";
                case "6":
                    return "1000000";
                case "7":
                    return "10000000";    
                case "8":
                    return "100000000";    
                case "9":
                    return "1000000000";   
                case "10":
                    return "10000000000";   
                case "11":
                    return "100000000000";   
                case "12":
                    return "1000000000000";
                default:     
                    return null;
            }
        }
        public String checkUnit(String _expression){
            switch (_expression.toLowerCase()){
                case "mhz":
                    _expression="*1000000";
                    return _expression;
                case "khz":
                    _expression="*1000";
                    return _expression;
                case "ghz":
                    _expression="*1000000000";
                    return _expression;
                case "hz":
                    _expression=_expression.substring(0, _expression.length()-2);
                    return _expression;    
                case "ms":
                    _expression="/1000";
                    return _expression;
                case "us":
                    _expression="/1000000";
                    return _expression;
                case "ns":
                    _expression="/1000000000";
                    return _expression;    
                case "s":
                    _expression=_expression.substring(0, _expression.length()-1);
                    return _expression;
                case "ps":
                    _expression="/1000000000000";
                    return _expression;
                case "fs":
                    _expression="/1000000000000000";
                    return _expression;    
                case "mv":
                    _expression="/1000";
                    return _expression;
                case "uv":
                    _expression="/1000000";
                    return _expression;
                case "v":
                    _expression=_expression.substring(0, _expression.length()-1);
                    return _expression;    
                case "ma":
                    _expression="/1000";
                    return _expression;
                case "ua":
                    _expression="/1000000";
                    return _expression;
                case "a":
                    _expression=_expression.substring(0, _expression.length()-1);
                    return _expression; 
                case "na":
                    _expression="/1000000000";
                    return _expression;     
                case "nf":
                    _expression="/1000000000";
                    return _expression;    
                case "uf":
                    _expression="/1000000";
                    return _expression;
                case "pf":
                    _expression="/1000000000000";
                    return _expression;
                case "ohm":
                    _expression=_expression.substring(0, _expression.length()-3);
                    return _expression;
                case "kohm":
                    _expression="*1000";
                    return _expression;     
                default:
                    return null;
            }
        
        }
        
        public void equationEvaluate(){    
            for(String eqnName: this.validEquaitons){
//                if((eqnName.toLowerCase().contains("ac"))||(eqnName.toLowerCase().contains("timin"))||(eqnName.toLowerCase().contains("level"))&&(eqnName.toLowerCase().contains("equation"))){
                if(eqnName!=null){
                    // this is a AC Equation Setup
                    Equation equation=equations.get(eqnName);
                    if (equation!=null){
                        for(Group group:equation.getGroupList() ){
                            if(group.getGroupType()==0){
                                for(equationNode eqnNode: group.getNodes()){
                                    // if this eqnNode is valid, it means it dosen't need to evaluate this variable since it's number
                                    if(!eqnNode.isIsValid()){
                                        String variableValue=null;
                                        
//                                        Expression expression = new Expression(eqnNode.getExpression().replaceAll("!", "NOT"));
                                        String _expression=eqnNode.getExpression();
                                        if(eqnNode.getExpression().startsWith("-")){
                                            _expression="0" + eqnNode.getExpression();
                                        }
                                        Expression expression = new Expression(_expression);
                                        
//                                        System.out.println("***************** " + eqnNode.getName() + " = " + eqnNode.getExpression());
//                                        eqnNode.setCanEvaluated(true);
//                                        if(eqnNode.getName().equals("MdioDataA_tF1_Offset"))
//                                            System.out.println("stop here");
                                        
                                        for(String variabelName: eqnNode.getSubVariables()){
//                                            if(variabelName.equals("MdioDataA_tF1_Offset"))
//                                                System.out.println("stop here");
                                            if(!eqnNode.isCanEvaluated())
                                                break;
                                            if(!variables.containsKey(variabelName)){
                                                eqnNode.setCanEvaluated(false);
//                                                System.out.println(variabelName +" is not found");
                                                
    //                                            eqnNode.setIsEvaluated(true);
                                                break;
                                            }
                                            else{
                                                equationNode variableNode=variables.get(variabelName);
                                                if(variableNode!=null){
                                                    if((variableNode.isIsValid()||variableNode.isIsEvaluated())&&(variableNode.isCanEvaluated())){
    //                                                    System.out.println("Good variable "+ variabelName + " = " + variableNode.getValue());
                                                         //"1E8" this type is not supported now
    //                                                    variableNode.setValue("5E+8");
                                                        if(variableNode.getValue().toUpperCase().contains("E")){
                                                            String _toNumber=toNumber(variableNode.getValue());
                                                            if(_toNumber!=null){
                                                                expression.and(variabelName, _toNumber);
    //                                                            System.out.println("add expression " +variabelName + " " + toNumber(variableNode.getValue()));
                                                            }
                                                            else{
                                                                eqnNode.setCanEvaluated(false);
                                                                break;
                                                            }
                                                        }
                                                        else if((variableNode.getValue().charAt(variableNode.getValue().length()-1))<'0'||(variableNode.getValue().charAt(variableNode.getValue().length()-1)>'9')){
                                                            // a number with unit
    //                                                        System.out.println("Unit Number " +  variableNode.getName() + "  " + variableNode.getValue());
                                                            for(int i= variableNode.getValue().length()-1;i>=0;i--){
                                                                if(variableNode.getValue().charAt(i)>='0'&&variableNode.getValue().charAt(i)<='9')
                                                                {
                                                                    variableValue=variableNode.getValue().substring(0, i+1)+checkUnit(variableNode.getValue().substring(i+1));
                                                                    if(variableValue!=null){
                                                                        expression.and(variabelName,variableValue);
    //                                                                    System.out.println("add expression " +variabelName +" " + variableNode.getValue().substring(0, i+1)+checkUnit(variableNode.getValue().substring(i+1)));
                                                                    }
                                                                    else{
                                                                        eqnNode.setCanEvaluated(false);
                                                                    }
                                                                    break;
                                                                }

                                                            }

                                                        }
                                                        else{
                                                            if(variableNode.getValue()!=null){
                                                                expression.and(variabelName, variableNode.getValue());
    //                                                            System.out.println("add expression " +variabelName +" " + variableNode.getValue());
                                                            }
                                                            else{
                                                                eqnNode.setCanEvaluated(false);
                                                                break;

                                                            }
                                                        }


                                                    }
                                                    else{
                                                        eqnNode.setWaitEvaluated(true);
    //                                                    System.out.println("Bad variable "+ variabelName + " = " + variables.get(variabelName).getValue());
                                                        break;
                                                    }
                                                }
                                                else{
                                                    System.out.println("Invalid variable "+ variabelName);
                                                }
                                            }
                                        }
                                        if(eqnNode.isCanEvaluated()&&!eqnNode.isWaitEvaluated()){
//                                            if(eqnNode.getExpression().equals("-VClampOffset"))
//                                                System.out.println(_expression);
                                                    
//                                            System.out.println("Start to evaluate " + _expression);
//                                            eqnNode.setValue(expression.eval().toString());
//                                            if (eqnNode.getName().equals("ForceVref"))
//                                                System.out.println("get ForceVref");
                                            try{
                                                String temp= expression.eval().toString();
                                                eqnNode.setValue(temp);
                                                eqnNode.setIsEvaluated(true);
                                                eqnNode.updateValue(null);
                                                
                                            }
                                            catch(Exception e){
//                                                System.out.println(e.getMessage());
                                                eqnNode.setIsEvaluated(true);
                                            }
                                            
                                        }
//                                        System.out.println("***************************************************** end "); 
                                    }
                                }
                            }
                        }
                    }
                }
            }
            boolean goodDone=true;
            int loopNo=0;
            while(goodDone &&(loopNo<10)){
                goodDone=false;
                System.out.println("start Evaluating for the "+ loopNo++ +  " time");
                for(String eqnName: this.validEquaitons){
    //                if((eqnName.toLowerCase().contains("ac"))||(eqnName.toLowerCase().contains("timin"))||(eqnName.toLowerCase().contains("level"))&&(eqnName.toLowerCase().contains("equation"))){
                    if(eqnName!=null){
                        // this is a AC Equation Setup
                        Equation equation=equations.get(eqnName);
                        if (equation!=null){
                                                    // re-try the second time to check if any excapes
                            for(Group group:equation.getGroupList() ){
                                if(group.getGroupType()==0){
                                    for(equationNode eqnNode: group.getNodes()){
                                        String variabelValue=null;
                                        // if this eqnNode is valid, it means it dosen't need to evaluate this variable since it's number
                                        if(!eqnNode.isIsValid() && (!eqnNode.isIsEvaluated()) &&eqnNode.isCanEvaluated()){
                                            String _expression=eqnNode.getExpression();
                                            if(eqnNode.getExpression().startsWith("-")){
                                                _expression="0" + eqnNode.getExpression();
                                            }
                                            Expression expression = new Expression(_expression);

//                                            System.out.println(" ***************** " + eqnNode.getName() + " = " + eqnNode.getExpression());

                                            for(String variabelName: eqnNode.getSubVariables()){
                                                equationNode variableNode=variables.get(variabelName);
                                                if(variableNode!=null){
                                                
                                                    if((variableNode.isIsValid()||variableNode.isIsEvaluated())&&(variableNode.isCanEvaluated())){
                                                        if(!variables.containsKey(variabelName)){
                                                            eqnNode.setCanEvaluated(false);
    //                                                        System.out.println(variabelName +" is not found");

                //                                            eqnNode.setIsEvaluated(true);
                                                            break;
                                                        }
                                                         //"1E8" this type is not supported now

                                                        if(variableNode.getValue().contains("E")){
    //                                                        expression.and(variabelName,  toNumber(variableNode.getValue()));
                                                            String _toNumber=toNumber(variableNode.getValue());
                                                            if(_toNumber!=null){
                                                                expression.and(variabelName, _toNumber);
    //                                                            System.out.println("add expression " +variabelName + " " + toNumber(variableNode.getValue()) );
                                                            }
                                                            else{
                                                                eqnNode.setCanEvaluated(false);
                                                                break;

                                                            }

                                                        }
                                                        else if((variableNode.getValue().charAt(variableNode.getValue().length()-1))<'0'||(variableNode.getValue().charAt(variableNode.getValue().length()-1)>'9')){
                                                            // a number with unit
    //                                                        System.out.println("Unit Number " +  variableNode.getName() + "  " + variableNode.getValue());
                                                            for(int i= variableNode.getValue().length()-1;i>=0;i--){
                                                                if(variableNode.getValue().charAt(i)>='0'&&variableNode.getValue().charAt(i)<='9')
                                                                {
                                                                    variabelValue=variableNode.getValue().substring(0, i)+checkUnit(variableNode.getValue().substring(i+1));
                                                                    if(variabelValue!=null){
                                                                        expression.and(variabelName,variabelValue);
                                                                    }
                                                                    else{
                                                                        eqnNode.setCanEvaluated(false);
                                                                    }
                                                                    break;
                                                                }
                                                            }

                                                        }
                                                        else{
                                                            if(variableNode.getValue()!=null){
                                                                expression.and(variabelName,variableNode.getValue());
                                                            }
                                                            else{
                                                                eqnNode.setCanEvaluated(false);
                                                                break;
                                                            }

                                                        }


    //                                                    System.out.println("Good variable "+ variabelName + " = " + variableNode.getValue());
                                                        eqnNode.setWaitEvaluated(false);
                                                    }
                                                    else{
                                                        eqnNode.setWaitEvaluated(true);
    //                                                    System.out.println("Bad variable "+ variabelName + " = " + variableNode.getValue());
                                                        break;
                                                    }
                                                }
                                                else
                                                    System.out.println("Invalid variable " + variabelName);

                                            }
                                            if(eqnNode.isCanEvaluated()&&(!eqnNode.isWaitEvaluated())){
                                                goodDone=true;
//                                                System.out.println("Start to evaluate " + eqnNode.getName());

                                                try{
                                                    String temp= expression.eval().toString();
                                                    eqnNode.setValue(temp);
                                                    eqnNode.setIsEvaluated(true);
                                                    eqnNode.updateValue(null);

                                                }
                                                catch(Exception e){
//                                                    System.out.println(e.getMessage());
                                                    eqnNode.setIsEvaluated(true);
                                                }
                                            }
//                                            System.out.println("***************************************************** end "); 
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // here to update the variables which are overrided by the other variabels
            
            for(equationNode eqnNode: this.overRideEqnNodes){
                if(variables.containsKey(eqnNode.getName())){
                    equationNode newEqnNode=variables.get(eqnNode.getName());
                    if(newEqnNode.isIsValid()||newEqnNode.isIsEvaluated()){
                        eqnNode.updateOverRideValue(newEqnNode.getValue(),newEqnNode.getEquationName());
                    }
                }
            }
        }

        public List<String> getValidEquaitons() {
            return validEquaitons;
        }
        
    }
    

    public class testElement {

        private Element element;
        private String testFile;
        private String testName;
        private final String execName;
        String sourceFile = "";
        private String patternName = "";
        private int patternNo = -1;
        private boolean isUsed=false;

        public testElement(Element e, String file, List<String> test_pat_list) {
            this.element = e;
            this.testFile = file;

            List<Attribute> attrs = e.attributes();
            if (attrs != null && attrs.size() > 0) {
                for (Attribute attr : attrs) {
                    this.testName = attr.getValue();
                }
            }
            this.execName = e.element("Exec").getText();
//            System.out.println("Exec is " + this.execName);
//            String temp=this.execName;
                
            String fileName = upaPath  +"\\"+  this.execName.replace('.', '\\') + ".java";
            fileName = fileName.replace('/', '\\');
//            System.out.println("Exec is " + this.execName);
            
//            System.out.println(this.testName);
            File tempFile = new File(fileName);
//            System.out.println(fileName);
            if (tempFile.exists()) {
                this.sourceFile = tempFile.getAbsolutePath();
//                System.out.println("File exist...." + tempFile.getAbsolutePath());
                
            } else {
                fileName="K:\\Xtos" +"\\"+  this.execName.replace('.', '\\') + ".java";
                fileName = fileName.replace('/', '\\');
                tempFile= new File(fileName);
                this.sourceFile = tempFile.getAbsolutePath();
                if(tempFile.exists())
                    this.sourceFile=null;
//                System.out.println("file doesnt exit " + fileName);
            }
            if(test_pat_list!=null){
                for (String test_pat : test_pat_list) {
    //                System.out.println(test_pat);
                    if (test_pat.startsWith(this.testName + ",")) {
                        this.patternName = test_pat.split(",")[1];
    //                    System.out.println(this.patternName);
                    }
                }
            }
        }

        public String getPatternName() {
            return patternName;
        }

        public String getSourceFile() {
            return sourceFile;
        }

        public String getTestFile() {
            return testFile;
        }

        public Element getElement() {
            return element;
        }

        public String getTestName() {
            return testName;
        }

        public String getExecName() {
            return this.execName;
        }

        public void setPatternNo(int patternNo) {
            this.patternNo = patternNo;
        }

        public int getPatternNo() {
            return patternNo;
        }

        public boolean isIsUsed() {
            return isUsed;
        }
        
    }
    public void getUPA(File file){
        int i=0;
        if(checkUPA(file, i))
            System.out.println("Valid UPA PATH " + this.upaPath);
    }

    private boolean checkUPA(File file, int loopNo) {
        if(file!=null){
        String currPath = file.getAbsolutePath();
        boolean isFind=false;
        for(String upa:this.upaConfig){
            File tempFile = new File(currPath + "\\" + upa);
            if (tempFile.exists()) {
                this.upaPath = tempFile.getAbsolutePath();
                isFind=true;
            }
        }
        if(!isFind){
            if (loopNo < 2) {
                loopNo++;
                return checkUPA((file.getParentFile()), loopNo);
            }
            return false;  
        }
        else 
            return true;
        }
        else
            return false;
    }

    private void setNotePadPP() {
        String xmlEditor=getGvimPath();
        if(xmlEditor!=null)
            XMLRead.notePadPath=xmlEditor;
        else
        {
            File editor= new File("C:\\Windows\\System32\\notepad.exe");
            if(editor.exists()){
                XMLRead.notePadPath=editor.getAbsolutePath();
            }
        
        }
    }

    public void startSearch(String content, boolean depthSearch) {
        searchResult.clear();
        File folder= new File("config");
        if(!folder.exists()||(folder.exists()&&folder.isFile())){
            folder.mkdir();
        }
        
        System.out.println("Starts to search " + content);
        String fileName="config/search_";
        PrintWriter printWriter =null;
        
        if(depthSearch){
            for(int i=0; i!= content.length();i++){
                char chr=content.charAt(i);
                if((chr>='0' && chr<='9')||(chr>='a' && chr<='z')||(chr>='A' && chr<='Z')||(chr=='_')){
                    fileName+=chr;
                }

            }
            fileName+= "_" + Long.toString(System.currentTimeMillis());
            fileName+=".xml";

            File oneFile= new File(fileName);
//            oneFile.deleteOnExit();

            if ( printWriter == null) {
                try {
                    printWriter = new PrintWriter(new FileWriter(fileName, false),false);
                    printWriter.println("<Search name=\"" +content + "\">");
                    fileName=oneFile.getAbsolutePath();
                } catch (IOException e) { 
                    System.out.println ("ERROR: Unable to open file");
                    printWriter=null;
                }       
            }
        }

//        for (testElement element : tests) {
//            boolean isFind = false;
//            if (treeWalk(element.getElement(), content)) {
//                addFile(element.getTestFile());
//                if(depthSearch){
//                    writeContext(printWriter,element,"test");
//                }
//            }
//        }
        
        for(Test test: XMLRead.newTests.values()){
            if(searchResult.contains(test.getFileName()) && (!depthSearch))
                continue;
            if(test.search(content)){
                addFile(test.getFileName());
                if(depthSearch){
                    test.print(printWriter);
                }
            }
            else if(test.getEquationRef().contains(content) || test.getVariables().contains(content)){
                addFile(test.getFileName());
                if(depthSearch){
                    test.print(printWriter);
                }
            }
            
                
        }
        System.out.println("Test Search Done");
        
        for (FlowTable table : flowTables) {
            if(searchResult.contains(table.getFileName()) && (!depthSearch))
                continue;
//            System.out.println("flow Name " + table.getFlowName());
            boolean isFound = false;
            if(table.search(content)){
                if(depthSearch) {
                    table.printFlowTable(printWriter);
                }
                addFile(table.getFileName());
                continue;
            }
            else{
                if(table.getStartNode().search(content)){
                    addFile(table.getFileName());
                    if(depthSearch) {
                         table.getStartNode().printStartNode(printWriter);
                    }
                    else
                        continue;
                }
               
                for (BaseNode node : table.getNodes()) {
                    if(node.search(content)){  
                        isFound=true;
                        if(depthSearch) {
                            node.printTestNode(printWriter);
                        }
                        else
                            break;
                    }
                        
                }
                if(isFound)
                    addFile(table.getFileName());
            }
        }
        System.out.println("Flow Search Done");
        
        
        for (Iterator<PatternBurst> it = patternBursts.values().iterator(); it.hasNext();) {
            PatternBurst pattern = it.next();
            if(searchResult.contains(pattern.getFileName()) && (!depthSearch))
                continue;
            
            if(pattern.search(content)){
                addFile(pattern.getFileName());
                if(depthSearch)
                    pattern.print(printWriter);
            }
        }
        System.out.println("Pattern Burst search Done");
//        System.out.println("Total Equation count " + this.equations.values().size());
        // start to search equations
//        int i=0;
        for (Equation equation : this.equations.values()) {
            if(searchResult.contains(equation.getFileName()) && (!depthSearch))
                continue;
            
            if(equation.getName().equals(content)){
                addFile(equation.getFileName());
                if(depthSearch) writeContext(printWriter,equation,"equation");
                continue; 
                     
            }
//            if(i==3631){
//                i=3631;
//            }
            boolean isFind=false;
            System.out.println("Start to search in " + equation.getName() );
//            if(equation.getName().equals("OBD"))
//                isFind=false;
            if(equation.getGroupList()!=null){
                for (Group group : equation.getGroupList()) {
                    if(group.getNodes()!=null){
                        for (equationNode node : group.getNodes()) {
                            if (node.getName().equals(content)) {
                                addFile(equation.getFileName());
                                if(depthSearch) writeContext(printWriter,equation,"equation");
                                isFind=true;
                                break;
                            }
                            if (node.getType() == 0) { // this ia a variable
                                if (node.getExpression().equals(content)) {
                                    addFile(equation.getFileName());
                                    if(depthSearch) writeContext(printWriter,equation,"equation");
                                    isFind=true;
                                    break;
                                }
                            }
                        }
                    }
                    if(isFind) break;
                }
            }
        }
        System.out.println("Equations search Done");
        
        // start to search test description
        for(GenericBlock _testDescription: XMLRead.testDescription.values()){
            if(searchResult.contains(_testDescription.getFileName()) && (!depthSearch))
                continue;
            if (_testDescription.search(content)){
                addFile(_testDescription.getFileName());
                if(depthSearch){
                    _testDescription.print(printWriter);
                }
            }
        }
        System.out.println("TestDescription search Done");
        
        // start to search test result spec
        for(GenericBlock _testDescription: XMLRead.resultSpecs.values()){
            if(searchResult.contains(_testDescription.getFileName()) && (!depthSearch))
                continue;
            if (_testDescription.search(content)){
                addFile(_testDescription.getFileName());
                if(depthSearch){
                    _testDescription.print(printWriter);
                }
            }
        }
        System.out.println("ResultSpec search Done");
        
        
        // start to search compareSpec
        for(GenericBlock _testDescription: XMLRead.compares.values()){
            if(searchResult.contains(_testDescription.getFileName()) && (!depthSearch))
                continue;
            if (_testDescription.search(content)){
                addFile(_testDescription.getFileName());
                if(depthSearch){
                    _testDescription.print(printWriter);
                }
            }
        }
        System.out.println("Compares search Done");
        
         // start to search DCs
        for(GenericBlock _testDescription: XMLRead.DCs.values()){
            if(searchResult.contains(_testDescription.getFileName()) && (!depthSearch))
                continue;
            if (_testDescription.search(content)){
                addFile(_testDescription.getFileName());
                if(depthSearch){
                    _testDescription.print(printWriter);
                }
            }
        }
        System.out.println("DC sequence/control/pattern search Done");

        // start to seach TestProgram

        if (action.search(content)){
            addFile(action.getFileName());
            if(depthSearch){
                action.print(printWriter);
            }
        }
        System.out.println("Action search Done");
        for (GenericBlock _vectorResult :vectorResult.values()) {
            if(searchResult.contains(_vectorResult.getFileName()) && (!depthSearch))
                continue;
            if(_vectorResult.search(content)){
                addFile(_vectorResult.getFileName());
                if(depthSearch){
                    _vectorResult.print(printWriter);
                }
            }
        }
        System.out.println("Vector Result search Done");
        //starts to search LoadBoardRef
        for (GenericBlock _loadBoardRef :XMLRead.loadBoards.values()) {
            if(searchResult.contains(_loadBoardRef.getFileName()) && (!depthSearch))
                continue;
            if(_loadBoardRef.search(content)){
                addFile(_loadBoardRef.getFileName());
                if(depthSearch){
                    _loadBoardRef.print(printWriter);
                }
            }
        }
        
        System.out.println("SoftSet search Done");
        //starts to search LoadBoardRef
        for (GenericBlock _softSet :XMLRead.softSet.values()) {
            if(searchResult.contains(_softSet.getFileName()) && (!depthSearch))
                continue;
            if(_softSet.search(content)){
                addFile(_softSet.getFileName());
                if(depthSearch){
                    _softSet.print(printWriter);
                }
            }
        }
        
        for (GenericBlock _softSet :XMLRead.softSetGroup.values()) {
            if(searchResult.contains(_softSet.getFileName()) && (!depthSearch))
                continue;
            if(_softSet.search(content)){
                addFile(_softSet.getFileName());
                if(depthSearch){
                    _softSet.print(printWriter);
                }
            }
        }
        for (GenericBlock axisList :XMLRead.AxisList.values()) {
            if(searchResult.contains(axisList.getFileName()) && (!depthSearch))
                continue;
            if(axisList.search(content)){
                addFile(axisList.getFileName());
                if(depthSearch){
                    axisList.print(printWriter);
                }
            }
        }
        
        System.out.println("LoadBoards search Done");
        // start to search levels
        for(Levels _level: levels.values()){
            if(searchResult.contains(_level.getFileName()) && (!depthSearch))
                continue;
            System.out.println("start search Level in " + _level.getName());
            if(_level.search(content)){
                addFile(_level.getFileName());
                if(depthSearch)
                    _level.print(printWriter);
            }
        
        }
        System.out.println("Levels search Done");
        // starts to search timing
        for(Timing _timing: XMLRead.timing.values()){
            
            if(searchResult.contains(_timing.getFileName()) && (!depthSearch))
                continue;
            
            if(_timing.search(content)){
                addFile(_timing.getFileName());
                if(depthSearch)
                    _timing.print(printWriter);
            }
                
        }
        System.out.println("Timing search Done");
        
        for(FlowOverride flowOverride: this.flowOverrides.values()){
            if(searchResult.contains(flowOverride.getFileName()))
                continue;
            if(flowOverride.search(content)){
                addFile(flowOverride.getFileName());
            }
        }
        
        
        if(depthSearch && fileName!=""){
            searchResult.add(fileName);
        }
        if(printWriter!=null) {
            printWriter.println("</Search>");
            printWriter.close();
            printWriter= null;
        } 
        
        if(depthSearch&&searchResult.size()==1)
            searchResult.clear();

    }

    private void addFile(String name) {
        boolean isSame = false;
        if(name!=null){
            for (String file : searchResult) {
                if (file!=null && file.equalsIgnoreCase(name)) {
                    isSame = true;
                    break;
                }
            }
            if (!isSame) {
                searchResult.add(name);
            }
        }
    }

    private void reReadFlow() {
        for (File file : xmlFileList) {
            boolean isSame=false;
//            System.out.print("flowTable size is  " + flowTables.size());
            String fileName = file.getName().toLowerCase();
            for (FlowTable table : flowTables) {
                if (table.getFileName().equalsIgnoreCase(file.getAbsolutePath())) {
                    isSame=true;
                    break;
                }
            }
            if(!isSame){
                boolean isSkip=false;
                for(String filter: this.skipFileFilter){
                    if(fileName.contains(filter)){
                        isSkip=true;
                        break;
                    }
                }
                if(!isSkip)
                { 
                    if(!(file.getParent().toLowerCase().contains("bitmap")||file.getParent().toLowerCase().contains("config"))){
                        System.out.println("Re-Read flow file " + file.getAbsolutePath());
                        readFlowTables(file);
                    }
                }
                else{
                    System.out.println("Filter Skipped " + fileName );
                }
            }
        }
    }
    /*
    private void reReadTest() {
        for (File file : xmlFileList) {
            boolean isSame=false;
            String fileName = file.getName().toLowerCase();
            for (testElement test : tests) {
                if (test.testFile.equalsIgnoreCase(file.getAbsolutePath())) {
                    isSame=true;
                    break;
                }
            }
            if(!isSame){
                
                boolean isSkip=false;
                for(String filter: this.skipFileFilter){
                    if(fileName.contains(filter)){
                        isSkip=true;
                        break;
                    }
                }
                if(!isSkip)
                { 
                        if(!(file.getParent().toLowerCase().contains("bitmap")||file.getParent().toLowerCase().contains("config"))){
                        readTest(file);
                    System.out.println("Re-Read test file " + file.getAbsolutePath());
                    }
                }
                else{
                    System.out.println("Filter Skipped " + fileName );
                }
            }
        }
    }
    */
    public void reReadAll() {
        for (File file : this.skipFiles) {
//            System.out.println(file.getAbsolutePath());
            boolean isSame=false;
            String fileName = file.getName().toLowerCase();
            for (Equation equation : this.equations.values()) {
                if (equation.getFileName().equalsIgnoreCase(file.getAbsolutePath())) {
                    isSame=true;
//                    System.out.println("Skip Equation " + fileName);
                    break;
                }
            }
            if(!isSame){
                boolean isSkip=false;
                for(String filter: this.skipFileFilter){
                    if(fileName.contains(filter)){
                        isSkip=true;
                        break;
                    }
                }
                if(!isSkip)
                { 
                        if(!(file.getParentFile().getName().toLowerCase().contains("bitmap")||file.getParentFile().getName().toLowerCase().contains("config"))){
                        readEquation(file);
                        System.out.println("Re-Read Equation file " + file.getAbsolutePath());
                    }
                }
                else{
//                    System.out.println("Filter Skipped " + fileName );
                }
            } 
        }
    }
    public boolean readConfig(){
        System.out.println(System.getProperty("user.home"));
//         List<String> upaConfig= new ArrayList<>();
//         static String notePadPath = "";
//         List<String> folderNameFilter= new ArrayList<>(); 
        System.out.println("Start to read config file");
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document = null;
        try {
            File file= new File(System.getProperty("user.home")+ "\\SapphireProgramReader\\config\\Config.xml");
            if(!file.exists()){
                try {
                    System.out.println(file.getAbsolutePath());
                    File folder= new File(System.getProperty("user.home") + "\\SapphireProgramReader");
                    if(!folder.exists()||(folder.exists() && folder.isFile()))
                        folder.mkdir();
                    folder= new File(System.getProperty("user.home") + "\\SapphireProgramReader\\config");
                    if(!folder.exists()||(folder.exists() && folder.isFile()))
                        folder.mkdir();
                    file.createNewFile();
                    System.out.println("Creating Config File");
                    writeConfigFile(file);
                } catch (IOException ex) {
                    Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            if(file.exists()){
                System.out.println(file.getAbsolutePath());
                document = reader.read(file);
            }
        } catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        List<Element> element = document.selectNodes("//Configs/Config");
        if (element.isEmpty()){
            document = null;
            return false;
        }
        else{            
            for(Element node : element){
                List<Attribute> attrs =node.attributes();
                List<Element> variables= node.elements();
                String tmp=attrs.get(0).getValue();
                switch (attrs.get(0).getValue()) {
                    case "UPA":
                        for(Element variable: variables){
                            this.upaConfig.add(variable.getText().trim());
                        }
                        break;
                    case "Editor":
                        for(Element variable: variables){
                            XMLRead.notePadPath=variable.getText().trim();
                            File file= new File(XMLRead.notePadPath.replace('\\', '/'));
                            if(!file.exists()){
                                XMLRead.notePadPath="";
                                setNotePadPP();
                            }
                        }
                        break;
                    case "EquationEvaluation":
                        if(!variables.isEmpty() && variables.size()==1){
                            
                            if(variables.get(0).getText().trim().toLowerCase().equals("on"))
                                XMLRead.evaluationOn=true;
                            else
                            XMLRead.evaluationOn=false;
                        }
                        else
                            XMLRead.evaluationOn=false;
                        break;
                    case "ExpandTestTree":
                        if(!variables.isEmpty() && variables.size()==1){
                            
                            if(variables.get(0).getText().trim().toLowerCase().equals("on"))
                                XMLRead.expandTestTree=true;
                            else
                            XMLRead.expandTestTree=false;
                        }
                        break;
                        
                    case "FolderFilter":
                        for(Element variable: variables){
                            this.folderNameFilter.add(variable.getText().trim());
                        }
                        break;
                    case "SkipFileFilter":
                        for(Element variable: variables){
                            this.skipFileFilter.add(variable.getText().trim());
                        }
                        break;
                    case "SkipFiles":
                        for(Element variable: variables){
                            this.inValidFiles.add(variable.getText().trim());
                            
                        }
                        break;
                            
                }
            }
            return true;
        }
        
    }
    public void readRecentFile(boolean isFirst, String path){
        OutputFormat format= OutputFormat.createPrettyPrint();
        format.setIndent(true);
        format.setIndentSize(2);
        File file= new File(System.getProperty("user.home") + "\\SapphireProgramReader\\config\\RecentFileList.xml");
        if(file.exists()){
            System.out.println("Start to read RecentFileList.xml");
            SAXReader reader = new SAXReader();
            reader.setValidation(false);
            Document document = null;
            try {
                document = reader.read(file);
            } catch (DocumentException ex) {
                Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<Element> element = document.selectNodes("//File/List");
            if (element.isEmpty()){
                if(isFirst){
                    document = null;
                    reader=null;
                }
                else{
                    Element root= document.getRootElement();
                    Element node = root.addElement("List");
                    node.addText(path);
                    try {
                                XMLWriter writer = new XMLWriter(new FileWriter(file.getAbsolutePath()), format);
                                writer.write(document);
                           
                                writer.close();
                            } catch (IOException ex) {
                                Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
                            }
                }
            }
            else{
                for(Element node : element){
                    if(isFirst){
                        if (!this.recentFileList.contains(node.getText().trim()))
                            this.recentFileList.add(node.getText().trim());
                    }
                    else{
                        if(!this.recentFileList.contains(path)){
                            Element root= document.getRootElement();
                            Element newNode = root.addElement("List");
                            newNode.addText(path);
                            try {
                                XMLWriter writer = new XMLWriter(new FileWriter(file.getAbsolutePath()), format);
                                writer.write(document);
                                writer.close();
                            } catch (IOException ex) {
                                Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                        }
                        
                    }
                }
            }
        }
        else{
            try {
                file.createNewFile();
                System.out.println("Creating new RecentFileList "+ file.getAbsolutePath());
                XMLWriter writer = new XMLWriter(new FileWriter(file.getAbsolutePath()), format);
                Document document = DocumentHelper.createDocument();
                Element root = document.addElement( "File" );
                root.addText("");

                
                writer.write(document);
                writer.close();
                
            } catch (IOException ex) {
                Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
            }    
        }
        if(!isFirst)
            writeInValidFile();
    }
    public static void editBat( String fileName){
        
        File _file=new File(System.getProperty("user.home") + "\\SapphireProgramReader\\config\\openXML.bat");
        if (!_file.exists()){
            try {
                _file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String file= _file.getAbsolutePath();
        
        
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
            printWriter.print(notePadPath);
            printWriter.print("\"");
            printWriter.print(" \"");
            printWriter.print(fileName);
            printWriter.print("\"");
            printWriter.close();
            printWriter= null;
        }
        
    }
    public static void editBat( String fileName, String content){
        
        File _file=new File(System.getProperty("user.home") + "\\SapphireProgramReader\\config\\openXML.bat");
        if (!_file.exists()){
            try {
                _file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String file= _file.getAbsolutePath();
        
        
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
            printWriter.print(notePadPath);
            printWriter.print("\"");
            if(notePadPath.toLowerCase().contains("gvim")&& content!=null){
                
                //-c /\V\CINSTALLATION
                String s="^";
                if(content.contains(s))
                    content= content.replaceAll("\\^", "\"" + "^" + "\"");
                s="|";
                if(content.contains(s))
                    content= content.replaceAll("\\|", "\"" + "|" + "\"");
                s="%";
                if(content.contains(s)){
                    int i = content.split("%").length;
                    content=content.split("%")[i-1];
                }
                printWriter.print(" -c /\\V\\C" + content);
            }
            printWriter.print(" \"");
            printWriter.print(fileName);
            printWriter.print("\"");
            printWriter.close();
            printWriter= null;
        }
        
    }
    public PrintWriter FileLog(String content){
        
        File _file=new File(System.getProperty("user.home") + "\\SapphireProgramReader\\config\\Log.txt");
        if (!_file.exists()){
            try {
                _file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String file= _file.getAbsolutePath();
        
        
        PrintWriter printWriter =null;

        if ( printWriter == null) {
            try {
                printWriter = new PrintWriter(new FileWriter(file, false),true);             
            } catch (IOException e) { 
                System.out.println ("ERROR: Unable to open file");
                printWriter=null;
            }       
        }
        return printWriter;
//        if ( printWriter != null) {
//            printWriter.print("\"");
//            printWriter.close();
//            printWriter= null;
//        }
        
    }
    public static void runBat(String batName) throws InterruptedException {
        try {
            Process ps = Runtime.getRuntime().exec(batName);
//            InputStream in = ps.getInputStream();
//            int c;
//            while ((c = in.read()) != -1) {
//                System.out.print(c);
//            }
//            in.close();
//            ps.waitFor();

        } catch (IOException ioe) {
//            // TODO Auto-generated catch block
            ioe.printStackTrace();
        }
//        System.out.println("child thread done");
     }
    private void writeContext(PrintWriter printWriter, Object object, String type){
        if (printWriter!=null){
            printWriter.println("");
            switch (type) {
                case "test":
                    Test test=(Test)object;
                    test.print(printWriter);
                    break;
                case "flow":
                    BaseNode flow= (BaseNode)(object);
                    for(String line: flow.getText()){
                          //System.out.println(line);
                          printWriter.println(line);
                    }
                    break;
                case "equation":
                    Equation equation= (Equation)(object);
                    printWriter.println("<Equations name=\"" + equation.getName() + "\">");
                    for(Group group: equation.getGroupList()){
                        for(equationNode node: group.getNodes()){
                            if(node.getType()==0){
    //            //                <Variable name="NextNode"><Value>'OVERRIDE'</Value></Variable>
                                printWriter.println("    <Variable name=\"" + node.getName()+ "\"><Value>" + node.getValue() + "</Value></Variable>");
                            }
                            else{
    //            //                <EquationsRef name="FopsEQVars" />
                                printWriter.println("    <EquationsRef name=\"" + node.getName() +"\" />");
                            }
                        }
                    }
                    printWriter.println("</Equations>");
                    break;
                case "pattern":
                    PatternBurst pattern= (PatternBurst)(object);
                    printWriter.println(pattern.nameToString());
                    //                            testFlowArea.appendText("\n");
                    printWriter.println(pattern.compositeToString());
                    //                            testFlowArea.appendText("\n");
                    if ((pattern.executionModeToString())!=null){
                        printWriter.println(pattern.executionModeToString());
    //                                testFlowArea.appendText("\n");
                    }
                    if((pattern.compareRefToString())!=null){
                        printWriter.println(pattern.compareRefToString());
    //                                testFlowArea.appendText("\n");
                    }
                    printWriter.println("</PatternBurst>");
                    break;
                case "TestDescription":
                    GenericBlock _testDescription =(GenericBlock) object;
                    _testDescription.print(printWriter);
                    break;
            }
        }
    }
    public void writeInValidFile(){
        OutputFormat format= OutputFormat.createPrettyPrint();
        format.setIndent(true);
        format.setIndentSize(2);
        
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document document=null;
        File file= new File(System.getProperty("user.home") + "\\SapphireProgramReader\\config\\Config.xml");
        try {
                if(file.exists()){
                    document = reader.read(file);
                }
        } 
        catch (DocumentException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Element> element = document.selectNodes("//Configs/Config");
        for(Element nodes: element){ 
            if (nodes.attributeValue("name").equals("SkipFiles")){
//                System.out.println("get iot");
                Element root= document.getRootElement();
                for(String name: this.newInValidFiles){
                    if(!this.inValidFiles.contains(name)){
                        Element newNode =  nodes.addElement("Variable");
                        newNode.addText(name);
                        System.out.println("Invalid File is " + name);
                    }
                    else{
                        System.out.println("Repeat Filter Found" + name);
                    }
                }
                try {
                    XMLWriter writer = new XMLWriter(new FileWriter(file.getAbsolutePath()), format);
                    writer.write(document);
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
                
    }
    private void writeConfigFile(File file){
        System.out.println("writeConfigFile for the first time");
        OutputFormat format= OutputFormat.createPrettyPrint();
        format.setIndent(true);
        format.setIndentSize(2);
        XMLWriter writer;
        try {
            writer = new XMLWriter(new FileWriter(file.getAbsolutePath()), format);
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement( "Configs" );
//            root.addText("");
            
            // This is for UPA 
            
            Element UPARoot= root.addElement("Config");
            UPARoot.addComment("UPA is to set the UPA/Custom Package Source Code path");
            UPARoot.addAttribute("name", "UPA");
            
            
            Element node=UPARoot.addElement("Variable");
            node.setText("upa_package\\\\sapphire_client\\\\src\\\\main\\\\java");
            node.addComment("This is for Client1.X path, you can comment this one if your program is not using Client2.X");
            
            
            
            node = UPARoot.addElement("Variable");
            node.addComment("This is for Client2.X path, you can comment this one if your program is not using Client1.X");
            node.setText("sapphire_client\\\\lib");
            
            
            node= UPARoot.addElement("Variable");
            node.addComment("This is for Custom Package path, you can comment this one if your program is not using Custom Package");
            node.setText("custom_packages\\\\src");
            
            
            Element EditorRoot= root.addElement("Config");
            EditorRoot.addAttribute("name", "Editor");
           
            node= EditorRoot.addElement("Variable");
            node.addComment("This is to set the editor which will be using to open files");
            node.addComment("You can use notepadd++ or gvim as the editor");
            String gvimPath= getGvimPath();
            
            File editor;
            // verify if vim is installed
            if(gvimPath!=null){
                node.setText(gvimPath);
            }
            else{
                editor= new File("C:\\Windows\\System32\\notepad.exe");
                if(editor.exists()){
                    node.setText("C:\\Windows\\System32\\notepad.exe");
                }
            }
            
            Element EquationEvaluationRoot= root.addElement("Config");
            EquationEvaluationRoot.addAttribute("name", "EquationEvaluation");
            node= EquationEvaluationRoot.addElement("Variable");
            node.addComment("This variable is to disable(off) or enable(on) Equation Evalution feature");
            node.setText("on");
            
            Element ExpandTestTreeRoot= root.addElement("Config");
            ExpandTestTreeRoot.addAttribute("name", "ExpandTestTreeRoot");
            node= ExpandTestTreeRoot.addElement("Variable");
            node.addComment("This variable is to disable(off) or enable(on) expand test tree");
            node.setText("on");
            
                
            Element FolderFilterRoot= root.addElement("Config");
             FolderFilterRoot.addComment("This is to set the Folder filters, all the folders would be ignored during loading xml files");
            FolderFilterRoot.addAttribute("name", "FolderFilter");
//            <Variable>upa_package,sapphire_client,lib,nucleu,custom_package,pof,svn,cvs,git,build,tool,java,groovy,d6432,blockdefinitions</Variable>
            node= FolderFilterRoot.addElement("Variable");
            node.setText("upa_package");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("sapphire_client");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("lib");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("nucleu");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("custom_package");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("pof");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("svn");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("cvs");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("git");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("build");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("tool");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("java");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("groovy");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("d6432");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("blockdefinitio");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("calibration");
            
            node= FolderFilterRoot.addElement("Variable");
            node.setText("neutralformat");
            
            
            // this if for SkipFileFilter
             Element SkipFileFilterRoot= root.addElement("Config");
             SkipFileFilterRoot.addComment("Please don't edit this settings and it will get modified automatically during program loading");
             SkipFileFilterRoot.addAttribute("name", "SkipFileFilter");
             
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("flow");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("test");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("equation");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("level");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("pattern");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("signal");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("loadboard");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("result");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("timi");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("name");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("binn");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("sequ");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("trig");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("axislist");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("block_de");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("fcm");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("desription");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("softset");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("strategy");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("bistinfo");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("bitmap");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("fuseconfig");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("kdftest");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("fuseencod");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("componenthash");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("paramref");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("ulsdse");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("device.xml");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("testcode");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("recipe");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("xtos_init");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("scm");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("pinmeta");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("programconfig_");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("dib");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("relay");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("amdlb");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("insertion_");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("jitter");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("regist");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("encod");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("field");
             node= SkipFileFilterRoot.addElement("Variable");
             node.setText("compare");
         
             Element SkipFilesRoot= root.addElement("Config");
             SkipFilesRoot.addAttribute("name", "SkipFiles");
             SkipFilesRoot.setText("");
            
            
            writer.write(document);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(XMLRead.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
    }
    private String getGvimPath(){
        File editor= new File("C:/Program Files/Vim");
        String fileName=null;
        if (editor.exists()){
            
            for(File file: editor.listFiles()){
                editor= new File(file.getAbsolutePath() + "\\gvim.exe");
                if(editor.exists()){
                    fileName=editor.getAbsolutePath();
                    break;
                }   
            }
            return fileName;           
        }
        else{
            editor= new File("C:/Program Files (x86)/Vim");
            if (editor.exists()){

                for(File file: editor.listFiles()){
                    editor= new File(file.getAbsolutePath() + "\\gvim.exe");
                    if(editor.exists()){
                        fileName=editor.getAbsolutePath();
                        break;
                    }   
                }
                return fileName;           
            }
            else{
                editor = new File("C:\\Program Files (x86)\\Notepad++\\notepad++.exe");
                if (editor.exists()) {
                     return editor.getAbsolutePath();
                } else {
                    editor = new File("C:\\Program Files\\Notepad++\notepad++.exe");
                    if (editor.exists()) {
                        return editor.getAbsolutePath();
                    }
                    else
                        return null;
                }
            
            
            }
            
        
        }   
    }
    
    public static boolean xmlFileSearch(String content){
//        System.out.println("start search ....");
        searchFilesList.clear();
        for(File file: xmlFileList){
//            System.out.println("here is a step");
            if(file.getName().toLowerCase().contains(content.toLowerCase())){
                searchFilesList.add(file.getAbsolutePath());
//                System.out.println(file.getAbsolutePath());
            }
        }
        for(File file: unusedFileList){
//            System.out.println("here is a step");
            if(file.getName().toLowerCase().contains(content.toLowerCase())){
                searchFilesList.add(file.getAbsolutePath());
//                System.out.println(file.getAbsolutePath());
            }
        }
            return true;
    }
   
    
}
