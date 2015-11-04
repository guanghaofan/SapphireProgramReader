/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader.blockreader;

import sapphireprogramreader.ui.controls.EquationNodeCell;
import sapphireprogramreader.ui.controls.VariableLabelNodeCell;
import sapphireprogramreader.ui.controls.VariableNodeCell;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TreeItem;
import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 *
 * @author ghfan
 */
public class Equation {
    
    private String name=null;
    private String fileName=null;
//    private HashMap<String, String> variables= new HashMap();
//    private List<String> equationsRefs = new ArrayList<>();
//    public List<String> order= new ArrayList<>();
//    private final HashMap<String, equationNode> equationNodes= new HashMap();
    private final List<Group> groupList= new ArrayList<>();
    private boolean groupIsSet=false;
    private final List<TreeItem> treeNodes =new ArrayList<>();
    private boolean treeNodeReady=false;
    private boolean isUsed=false;
    private boolean useFul=false;

    
   public Equation(String name){
       this.name=name;
       this.fileName=null;
       isUsed=true;
   } 

    public Equation(Element element, String fileName) {
//        <Equations name="SetupPSShorts">
//                <EquationsRef name="ParamSetupPSShortsUnpowered" />
//                <EquationsRef name="TestpointsContinuity" />
//                <EquationsRef name="TestpointsIClamp" />
//                <DefaultVariable name="Index">	<Value>0000</Value>		</DefaultVariable>
//        </Equations>
        
        boolean firstOne=true;
        int previousType=0;
        int loopNo=0;
        
        
        this.fileName=fileName;
        List<Attribute> attrs = element.attributes();  
        if (attrs != null && attrs.size() > 0) {    
            for (Attribute attr : attrs) {   
                this.name = attr.getValue();
            }
        }
        //for testing
//        if(this.name.equals("DCSetupLevelEquations"))
//            this.name="DCSetupLevelEquations";
        
//      System.out.println("Equation " + this.name );
        List<Element> nodes =element.elements();
        if (!nodes.isEmpty()){
            for(Element eqnNode: nodes){
//                if(eqnNode.attribute(0).getValue().equals("AMDSIVil"))
//                    this.name="DCSetupLevelEquations";
                equationNode node= new equationNode(eqnNode, this.name);

                if(firstOne){
                    if(node.type==0){ // this is a variable
                        Group firstGroup= new Group(0,0);
                        firstGroup.name="Variable";
                        firstGroup.nodes.add(node); 
                        groupList.add(firstGroup);
    //                    firstGroup.startIndex=0;
                        previousType=0;
                    }
                    else{ // this is a equationRef
                        Group firstGroup= new Group(1,0);
                        firstGroup.name=node.name;
                        firstGroup.nodes.add(node); 
    //                    firstGroup.startIndex=0;
                        groupList.add(firstGroup);
                        previousType=1;
                    }
                    firstOne=false;

                }
                else{ 
                     if(node.type==0&&previousType==0){ // this is a variable
                         // do nothing since the same type and continue loop until to the break, that is the change of type
                         this.groupList.get(this.groupList.size()-1).nodes.add(node);

                     }
                     else if(node.type==0&&previousType==1)  {// variables to equationsRef
                         this.groupList.get(this.groupList.size()-1).stopIndex=loopNo-1;
                         this.groupList.add(new Group(0,loopNo));
                         this.groupList.get(this.groupList.size()-1).nodes.add(node);
                         this.groupList.get(this.groupList.size()-1).name=node.name;

                         previousType=0;
                     }
                     else if(node.type==1){ // change from variable to equationsRef
                         this.groupList.get(this.groupList.size()-1).stopIndex=loopNo-1;
                         this.groupList.add(new Group(1,loopNo));
                         this.groupList.get(this.groupList.size()-1).nodes.add(node);
                         this.groupList.get(this.groupList.size()-1).name=node.name;

                         previousType=1;
                     }
                }
                loopNo++;   
            }
        }
        else{
            System.out.println("Empty Equation "+ this.name +" in " + this.fileName);
        }
    }
    /*
    public void setGroup(){
        //groupList.clear();// bug fix,, if the same Equation is called for multip times, then GroupList will be re-set lots of times.
                          // this also can be fixed by setGroup in construct function. 
        boolean firstOne=true;
        int previousType=0;
        int loopNo=0;
        for(equationNode node:this.equationNodes.values()){
             if(firstOne){
                if(node.type==0){ // this is a variable
                    Group firstGroup= new Group(0,0);
                    firstGroup.name="Variable";
                    firstGroup.nodes.add(node); 
                    groupList.add(firstGroup);
//                    firstGroup.startIndex=0;
                    previousType=0;
                }
                else{ // this is a equationRef
                    Group firstGroup= new Group(1,0);
                    firstGroup.name=node.name;
                    firstGroup.nodes.add(node); 
//                    firstGroup.startIndex=0;
                    groupList.add(firstGroup);
                    previousType=1;
                }
                firstOne=false;
                
            }
            else{ 
                 if(node.type==0&&previousType==0){ // this is a variable
                     // do nothing since the same type and continue loop until to the break, that is the change of type
                     this.groupList.get(this.groupList.size()-1).nodes.add(node);
                     
                 }
                 else if(node.type==0&&previousType==1)  {// variables to equationsRef
                     this.groupList.get(this.groupList.size()-1).stopIndex=loopNo-1;
                     this.groupList.add(new Group(0,loopNo));
                     this.groupList.get(this.groupList.size()-1).nodes.add(node);
                     this.groupList.get(this.groupList.size()-1).name=node.name;
                     
                     previousType=0;
                 }
                 else if(node.type==1){ // change from variable to equationsRef
                     this.groupList.get(this.groupList.size()-1).stopIndex=loopNo-1;
                     this.groupList.add(new Group(1,loopNo));
                     this.groupList.get(this.groupList.size()-1).nodes.add(node);
                     this.groupList.get(this.groupList.size()-1).name=node.name;
                     
                     previousType=1;
                 }
            }
//            node.print();
//            System.out.println("Group No is  " + (this.groupList.size()-1));
            loopNo++;
        }
        this.groupIsSet=true; 
    }
    */
    public void buildEquationTable(){ 
        if(!groupIsSet){}
        for(Group group:this.groupList){
            if(group.getGroupType()==0){
//                TreeItem variablesItem= new TreeItem("Variables");
//                variablesItem.getChildren().add(new TreeItem(getVariableTable(group)));
//                item.getChildren().add(variablesItem);
                // change to new cell
                TreeItem variablesItem= new TreeItem("*Variables");
                variablesItem.getChildren().add(new TreeItem(new VariableLabelNodeCell()));
                for(equationNode node : group.getNodes()){
                    
                    TreeItem variableItem= new TreeItem(new VariableNodeCell(node));
                    node.treeItem=variableItem;
                    variablesItem.getChildren().add(variableItem);
                }
//                treeNode.getChildren().add(variablesItem);
                treeNodes.add(variablesItem);
            }
            else if(group.getGroupType()==1){
                for(equationNode node: group.getNodes()){
                    if(node.getType()==1){
                        TreeItem subItem= new TreeItem(new EquationNodeCell(node.name, this.fileName));
//                        treeNode.getChildren().add(subItem);
                        treeNodes.add(subItem);
//                        if(xmlReader.equations.get(node.getName())!=null){
//                            buildEquationTable(xmlReader.equations.get(node.getName()), xmlReader,subItem);
//                                 
//                        }
//                        else{
//                            System.out.println("Equation  "+ node.getName() +"  doesn't exist");
//                            
//                        }
                        
                    }
                    else{
                        System.out.println("something wrong in this node" + node.getName() +" , this is not a equationsRef");
                    }
                }
            
            }
        }
        treeNodeReady=true;    
    }
    
    

    public boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
//    public TreeItem getRoot() {
//        return new TreeItem( new EquationNodeCell(this.name, this.fileName));
//    }
//    

    public List<TreeItem> getTreeNodes() {
        if(this.treeNodeReady)
            return treeNodes;
        else{
            buildEquationTable();
            return treeNodes;
        }
    }
    public List<TreeItem> getRepeatTreeNodes(){
        List<TreeItem> _treeNodes =new ArrayList<>();
        for(Group group:this.groupList){
            if(group.getGroupType()==0){
//                TreeItem variablesItem= new TreeItem("Variables");
//                variablesItem.getChildren().add(new TreeItem(getVariableTable(group)));
//                item.getChildren().add(variablesItem);
                // change to new cell
                TreeItem variablesItem= new TreeItem("*Variables");
                variablesItem.getChildren().add(new TreeItem(new VariableLabelNodeCell()));
                for(equationNode node : group.getNodes()){
                    TreeItem variableItem= new TreeItem(new VariableNodeCell(node));
                    variablesItem.getChildren().add(variableItem);
                }
//                treeNode.getChildren().add(variablesItem);
                _treeNodes.add(variablesItem);
            }
            else if(group.getGroupType()==1){
                for(equationNode node: group.getNodes()){
                    if(node.getType()==1){
                        TreeItem subItem= new TreeItem(new EquationNodeCell(node.name, this.fileName));
//                        treeNode.getChildren().add(subItem);
                        _treeNodes.add(subItem);
//                        if(xmlReader.equations.get(node.getName())!=null){
//                            buildEquationTable(xmlReader.equations.get(node.getName()), xmlReader,subItem);
//                                 
//                        }
//                        else{
//                            System.out.println("Equation  "+ node.getName() +"  doesn't exist");
//                            
//                        }
                        
                    }
                    else{
                        System.out.println("something wrong in this node" + node.getName() +" , this is not a equationsRef");
                    }
                }
            
            }
        }
        
        
        return _treeNodes;
    
    }
    public List<Group> getGroupList() {
//        if(!groupIsSet){};
        return groupList;
    }
    
    public void print(){
        System.out.println("<Equations name=\"" + this.name + "\">");
        
        for(Group group: this.groupList){
            for(equationNode node: group.nodes){
                node.print();
            }
            
        }
        
        System.out.println("</Equations>");
    }
    public String getName() {
        return name;
    }
    
    
    public boolean isGroupIsSet() {
        return groupIsSet;
    }
    

    public boolean isTreeNodeReady() {
        return treeNodeReady;
    }

    public String getFileName() {
        return fileName;
    }

//    public HashMap<String, equationNode> getEquationNodes() {
//        return equationNodes;
//    }

    public void setName(String name) {
        this.name = name;
    }
//<Equations name="Overrides">
//    <EquationsRef name="ComponentHash" />
//    <!--<EquationsRef name="MarketSegment" />-->
//    <EquationsRef name="FopsEQVars" />
//
//    <Variable name="NextNode"><Value>'OVERRIDE'</Value></Variable>
//    <Variable name="OverrideOn"><Value>1</Value></Variable>
//
//    <Variable name="SX1Bitmap0SampleResult"><Value>1</Value></Variable>
//    <Variable name="SX1Charz0SampleResult"><Value>1</Value></Variable>
//    <Variable name="SX1Charz1SampleResult"><Value>1</Value></Variable>
//    <Variable name="SX1Charz2SampleResult"><Value>1</Value></Variable>
//    <Variable name="SX1Charz3SampleResult"><Value>1</Value></Variable>
//    <Variable name="SX1ScanSFBSampleResult"><Value>1</Value></Variable>
//    <Variable name="PerTileVolumeSFB"><Value>1</Value></Variable>
//
//</Equations>
    
    public class equationNode{
        private String name=null;
        private int type =0;
        private String value=null;
        private String expression=null;
        private boolean isValid=false;
        private boolean isEvaluated=false;
        private TreeItem treeItem;
        private List<String> subVariables= new ArrayList<>();
        private boolean canEvaluated=true;
        private boolean waitEvaluated=false;
        private String equationName=null;
        private String originalExpression=null;
        private boolean isOverride=false;

        public equationNode(Element node, String _equationName) {
            if(_equationName!=null) 
                this.equationName=_equationName;
//            <DefaultVariable name="Index">	<Value>0000</Value>		</DefaultVariable>
            if(node.getName().equals("Variable")){
                this.name=node.attribute(0).getValue();
                this.type=0;
                this.value= node.element("Value").getText().trim();
                this.expression= this.value;
//                if(XMLRead.evaluationOn){
//                    if(isValidVariable()){
//                        isValid=true;
//                        System.out.print(" valid number");
//                    }
//                    else{
//                        System.out.print(" Invalid number");
//                        System.out.println("Split " + this.expression);
//                        splitVariables();
//                    }
//                }
            }
            else if(node.getName().equals("EquationsRef")){
                this.name=node.attribute(0).getValue();
                this.type=1;
            }
            else if(node.getName().contains("Variable")){
                this.name=node.attribute(0).getValue();
                this.type=0;
                this.value= node.element("Value").getText().trim();
                this.expression=this.value;
//                if(XMLRead.evaluationOn){
//                    if(isValidVariable())
//                        isValid=true;
//                    else
//                        splitVariables();
//                }
            }
            else{
                System.out.println("something wrong in this equation " + this.name);
            }
            
        }
        public String getEquationName(){
            return this.equationName;
        }
        public void updateValue(String colour){
            if(this.isEvaluated){
                if(this.treeItem!=null){
                    VariableNodeCell _variableNodeCell =(VariableNodeCell) this.treeItem.getValue();
                    _variableNodeCell.updateValue(this.value, colour,null);
                    if(this.originalExpression!=null)
                        _variableNodeCell.updateExpression(this.originalExpression);
                }
            }
                
        }
        public void updateOverRideValue(String _value, String equationName){
           
            if(this.treeItem!=null){
                VariableNodeCell _variableNodeCell =(VariableNodeCell) this.treeItem.getValue();
                _variableNodeCell.updateValue(_value, "red",  equationName);
                
                if(this.originalExpression!=null)
                    _variableNodeCell.updateExpression(this.originalExpression);
            }
            this.isOverride=true;

        }
        public void recoverValue(){
            if(this.treeItem!=null && this.isOverride){
                VariableNodeCell _variableNodeCell =(VariableNodeCell) this.treeItem.getValue();
                
                 _variableNodeCell.recoveryVlaue(this);
//                if(this.originalExpression!=null)
//                    _variableNodeCell.updateExpression(this.originalExpression);
            }
            this.isOverride=false;
        
        }
        
        public boolean isValidVariable(){
//            System.out.println("");
//            System.out.print("Checking " + this.name + " = " + this.expression);
            // pure number or number with Unit
            
//            if( this.expression=="0V")
//                this.expression="PmaxScanGPUFreqLo_2 * 1000000";
            char[] chars;
          
            chars=this.expression.toCharArray();
            
            if(this.expression.contains("!")||this.expression.contains("?")||this.expression.contains("'")||this.expression.contains("\"")){
                this.canEvaluated=false;
                return true;
            }
            else if(this.expression.contains("*")|| this.expression.contains("/"))
                return false;
            
            else if ((chars[0]<'0'||chars[0]>'9')&&(chars[0]!='-')&&(chars[0]!='e')&&(chars[0]!='E')){
                if(chars[0]!='\'')
                    return false;
                else 
                    return true;
            }
            else if(this.expression.toLowerCase().contains("e-")){
                String temp[]= this.expression.toLowerCase().split("e-");
                if((temp.length==2&&(temp[0].length()!=0 )&&temp[0].charAt(temp[0].length()-1)<='9'&&temp[0].charAt(temp[0].length()-1)>='0')&&(temp[1].charAt(0)<='9'&&temp[1].charAt(0)>='0')&&(!temp[1].contains("+"))&&(!temp[1].contains("-"))&&(!temp[1].contains("/"))&&(!temp[1].contains("*"))&&(!temp[0].contains("+"))&&(!temp[0].substring(1).contains("-"))&&(!temp[0].contains("/"))&&(!temp[0].contains("*"))){
              
                    return true;
                }
                else
                {
                    return false;
                }
                    
            }
            else if(this.expression.substring(1).contains("-"))
                return false;
            else if(this.expression.toLowerCase().contains("e+")){
                String temp[]= this.expression.toLowerCase().split("e\\+");
                if(temp.length==2&&(temp[0].length()!=0 )&&(temp[0].charAt(temp[0].length()-1)<='9'&&temp[0].charAt(temp[0].length()-1)>='0')&&(temp[1].charAt(0)<='9'&&temp[1].charAt(0)>='0')&&(!temp[1].contains("+"))&&(!temp[1].contains("-"))&&(!temp[1].contains("/"))&&(!temp[1].contains("*"))&&(!temp[0].contains("+"))&&(!temp[0].substring(1).contains("-"))&&(!temp[0].contains("/"))&&(!temp[0].contains("*"))){
            
                        return true;
                }
                else
                {
                        return false;
                }
            }
            else if(this.expression.substring(1).contains("+"))
                return false;
            else if(this.expression.toLowerCase().split("e").length==2){
                String temp[]= this.expression.toLowerCase().split("e");
                if((temp[0].length()!=0 )&&(temp[0].charAt(temp[0].length()-1)<='9'&&temp[0].charAt(temp[0].length()-1)>='0')&&(temp[1].charAt(0)<='9'&&temp[1].charAt(0)>='0')&&(!temp[1].contains("+"))&&(!temp[1].contains("-"))&&(!temp[1].contains("/"))&&(!temp[1].contains("*"))&&(!temp[0].substring(1).contains("+"))&&(!temp[0].substring(1).contains("-"))&&(!temp[0].contains("/"))&&(!temp[0].contains("*"))){
                     if(temp[0].length()>=1){
                        if(temp[0].charAt(temp[0].length()-1)<='9'&&temp[0].charAt(temp[0].length()-1)>='0')
                            return true;
                        else 
                            return false;
                    }
                    else 
                        return true;
                }
                else 
                    return false;
            }
            
            else{
                int i;
                boolean charIsFound=false;
                for(i=1;i!=chars.length;i++){
                    if ((chars[i]<'0'|| chars[i]>'9')){
                        charIsFound=true;
                        break;  
                    }
                }
                if(charIsFound){
                    if(chars[i]=='.'){
                        int j;    
                        boolean noChar=true;
                        for(j=i+1;j!=chars.length; j++){
                            if(chars[j]<'0'||chars[j]>'9'){
                                noChar=false;
                                break;
                            }
                            
                        }
                        
                        if(noChar)
                            return true;
                        else{
                            return checkUnit(this.expression.substring(j));
//                            switch (this.expression.substring(j).toLowerCase()){
//                            case "mhz":
//                                this.value=this.expression.substring(0, this.expression.length()-3)+"E6";
//                                return true;
//                            case "khz":
//                                this.value=this.expression.substring(0, this.expression.length()-3)+"E3";
//                                return true;
//                            case "ghz":
//                                this.value=this.expression.substring(0, this.expression.length()-3)+"E9";
//                                return true;
//                            case "hz":
//                                this.value=this.expression.substring(0, this.expression.length()-2);
//                                return true;    
//                            case "ms":
//                                this.value=this.expression.substring(0, this.expression.length()-2) + "E-3";
//                                return true;
//                            case "us":
//                                this.value=this.expression.substring(0, this.expression.length()-2)+"E-6";
//                                return true;
//                            case "ns":
//                                this.value=this.expression.substring(0, this.expression.length()-2)+"E-9";
//                                return true;    
//                            case "s":
//                                this.value=this.expression.substring(0, this.expression.length()-1);
//                                return true;
//                            case "ps":
//                                this.value=this.expression.substring(0, this.expression.length()-2) + "E-12";
//                                return true;
//                            case "fs":
//                                this.value=this.expression.substring(0, this.expression.length()-2) +"E-15";
//                                return true;    
//                            case "mv":
//                                this.value=this.expression.substring(0, this.expression.length()-2)+ "E-3";
//                                return true;
//                            case "uv":
//                                this.value=this.expression.substring(0, this.expression.length()-2)+ "E-6";
//                                return true;
//                            case "v":
//                                this.value=this.expression.substring(0, this.expression.length()-1);
//                                return true;    
//                            case "ma":
//                                this.value=this.expression.substring(0, this.expression.length()-2)+"E-3";
//                                return true;
//                            case "ua":
//                                this.value=this.expression.substring(0, this.expression.length()-2)+"E-6";
//                                return true;
//                            case "a":
//                                this.value=this.expression.substring(0, this.expression.length()-1);
//                                return true; 
//                            case "na":
//                                this.value=this.expression.substring(0, this.expression.length()-2)+"E-9";
//                                return true;    
//                            case "nf":
//                                this.value=this.expression.substring(0, this.expression.length()-2) +"E-9";
//                                return true;    
//                            case "uf":
//                                this.value=this.expression.substring(0, this.expression.length()-2)+"E-6";
//                                return true;
//                            case "pf":
//                                this.value=this.expression.substring(0, this.expression.length()-2)+"E-12";
//                                return true;
//                            default:
//                                return false;
//                            }
                        }
                    }
                    else
                    {   
                        return checkUnit(this.expression.substring(i));
                    }
                }
                else
                    return true;
            }
            
        }
        
        

        public boolean isIsOverride() {
            return isOverride;
        }
        public boolean checkUnit(String _expression){
            switch (_expression.toLowerCase()){
                case "mhz":
                    this.value=this.expression.substring(0, this.expression.length()-3)+"E6";
                    return true;
                case "khz":
                    this.value=this.expression.substring(0, this.expression.length()-3)+"E3";
                    return true;
                case "ghz":
                    this.value=this.expression.substring(0, this.expression.length()-3)+"E9";
                    return true;
                case "hz":
                    this.value=this.expression.substring(0, this.expression.length()-2);
                    return true;    
                case "ms":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-3";
                    return true;
                case "us":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-6";
                    return true;
                case "ns":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-9";
                    return true;    
                case "s":
                    this.value=this.expression.substring(0, this.expression.length()-1);
                    return true;
                case "ps":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-12";
                    return true;
                case "fs":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-15";
                    return true;    
                case "mv":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-3";
                    return true;
                case "uv":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-6";
                    return true;
                case "v":
                    this.value=this.expression.substring(0, this.expression.length()-1);
                    return true;    
                case "ma":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-3";
                    return true;
                case "ua":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-6";
                    return true;
                case "a":
                    this.value=this.expression.substring(0, this.expression.length()-1);
                    return true; 
                case "na":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-9";
                    return true;     
                case "nf":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-9";
                    return true;    
                case "uf":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-6";
                    return true;
                case "pf":
                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-12";
                    return true;    
                default:
                    return false;
            }
        
        }
        public boolean checkExpressionUnit(String name, int i ){
            String _expression= name.substring(i);
            switch (_expression.toLowerCase()){
                case "mhz":
                    if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "*1000000");
//                    this.value=this.expression.substring(0, this.expression.length()-3)+"E6";
                    return true;
                case "khz":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "*1000");
//                    this.value=this.expression.substring(0, this.expression.length()-3)+"E3";
                    return true;
                case "ghz":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "*1000000000");
//                    this.value=this.expression.substring(0, this.expression.length()-3)+"E9";
                    return true;
                case "hz":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i));
//                    this.value=this.expression.substring(0, this.expression.length()-2);
                    return true;    
                case "ms":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-3";
                    return true;
                case "us":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-6";
                    return true;
                case "ns":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000000000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-9";
                    return true;    
                case "s":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i));
//                    this.value=this.expression.substring(0, this.expression.length()-1);
                    return true;
                case "ps":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000000000000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-12";
                    return true;
                case "fs":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000000000000000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-15";
                    return true;    
                case "mv":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-3";
                    return true;
                case "uv":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-6";
                    return true;
                case "v":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i));
//                    this.value=this.expression.substring(0, this.expression.length()-1);
                    return true;    
                case "ma":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-3";
                    return true;
                case "ua":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-6";
                    return true;
                case "a":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i));
//                    this.value=this.expression.substring(0, this.expression.length()-1);
                    return true; 
                case "na":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000000000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-9";
                    return true;     
                case "nf":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000000000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-9";
                    return true;    
                case "uf":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-6";
                    return true;
                case "pf":
                     if(this.originalExpression==null)
                        this.originalExpression=this.expression;
                    this.expression=this.expression.replace(name, name.substring(0, i)+ "/1000000000000");
//                    this.value=this.expression.substring(0, this.expression.length()-2)+"E-12";
                    return true;    
                default:
                    return false;
            }
        
        }

        public boolean isWaitEvaluated() {
            return waitEvaluated;
        }

        public boolean isCanEvaluated() {
            return canEvaluated;
        }

        public void setWaitEvaluated(boolean waitEvaluated) {
            this.waitEvaluated = waitEvaluated;
        }

        public void setCanEvaluated(boolean canEvaluated) {
            this.canEvaluated = canEvaluated;
        }
        
        public int getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }  
        

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public void setValue(String value) {
            this.value = value;
        }
        

        public void setIsValid(boolean isValid) {
            this.isValid = isValid;
        }

        public void setIsEvaluated(boolean isEvaluated) {
            this.isEvaluated = isEvaluated;
        }

        public boolean isIsValid() {
            return isValid;
        }

        public boolean isIsEvaluated() {
            return isEvaluated;
        }
        
        public void print(){
            if(this.type==0){
//                <Variable name="NextNode"><Value>'OVERRIDE'</Value></Variable>
//                System.out.println("    <Variable name=\"" + this.name+ "\"><Value>" + this.value + "</Value></Variable>");
            }
            else{
//                <EquationsRef name="FopsEQVars" />
//                System.out.println("    <EquationsRef name=\"" + this.name +"\" />");
            }
        }
        public String toNumber(String expression){
            if(expression.contains("E")){
                String[] result=expression.split("E");
                if(result.length!=2)
                    return null;
                else{
                    String temp;
                    if(result[1].contains("-")){
                        return "(" + result[0] + "/"+ addZero(result[1]) +")";
                        
                    }
                    else
                        return "(" + result[0] + "*"+ addZero(result[1]) +")";
                    
                    
                }
            }
            if(expression.contains("e")){
                String[] result=expression.split("e");
                if(result.length!=2)
                    return null;
                else{
                    String temp;
                    if(result[1].contains("-")){
                        return "(" + result[0] + "/"+ addZero(result[1]) +")";
                        
                    }
                    else
                        return "(" + result[0] + "*"+ addZero(result[1]) +")";
                    
                    
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
        private boolean isNumber(String name){
//            this.expression="a-" +" b+ " +"897E4ms"  +"+200";
//            name="897E4ms";
          
//            name="5E-9ns";
            // 5E9
            boolean notNumber=false;
            boolean firstE=false;
            boolean firstEP=false;
            String temp=null;
            for(int i=0; i< name.length();i++){
                if((name.charAt(i)<'0'||name.charAt(i)>'9')&&name.charAt(i)!='.'){
                    if(firstE||firstEP){
                        notNumber=!checkExpressionUnit(name,i);
                        temp=name.substring(0,i);
                     
                        break;
                    }
                        
                    if(name.charAt(i)=='e'||name.charAt(i)=='E'){
                        if((name.length()>i+1)&&name.charAt(i+1)<='9'&&name.charAt(i+1)>='0'){
                            if((i>=1)&&(name.charAt(i-1)>='0')&&(name.charAt(i-1)<='9')){
                                firstE=true;
                                i=i+1;
                            }
                            else
                            {   
                                notNumber=true;
                                break;
                            }
                        }
                        else if((name.length()>i+2)&&((name.charAt(i+1)=='+')||(name.charAt(i+1)=='-')&& name.charAt(i+2)<='9'&& name.charAt(i+2)>='0')){
                            if((i>=1)&&(name.charAt(i-1)>='0')&&(name.charAt(i-1)<='9')){
                                firstEP=true;
                                i=i+2;
                            }
                            else
                            {
                                notNumber=true;
                                break;
                            }
                        }
                    }
                    else{
                        notNumber=!checkExpressionUnit(name,i);
                        break;
                    }
                }
            }
            
            if(!notNumber){
                if(firstE||firstEP){
                    if(temp==null){
                        if(this.originalExpression==null)
                            this.originalExpression=this.expression;
                        this.expression=this.expression.replace(name, toNumber(name));
                    }
                    else{
                        if(this.originalExpression==null)
                            this.originalExpression=this.expression;
                        this.expression=this.expression.replace(temp, toNumber(temp));
                    }
                    
                }
            }
            return(!notNumber);
        }
        public void splitVariables(){
//           this.expression="bypassclk_rise+bypassclk_pw + 598Mhz";
            if (!this.isValid){
                String result=null;
                String[] _result;
                result=this.expression;
                
                _result=this.expression.toLowerCase().split("(\\d)(e)(\\d)");
                if(((_result.length>=1)&&(!_result[0].equalsIgnoreCase(this.expression.toLowerCase())))||(_result.length==0))
                    System.out.println("this expression contains 5E5");
                else if((!this.expression.toLowerCase().contains("e-"))&&(!this.expression.toLowerCase().contains("e+"))){
                    //"+ - * / ? : , !, &,{,},[,](,)"
                    if(result.charAt(0)=='(')
                        result=result.substring(1);
                    if(result!=null){
                        result= result.replace('+', '@');
                        result= result.replace('-', '@');
                        result= result.replace('*', '@');
                        result= result.replace('/', '@');
                        result= result.replace('?', '@');
                        result= result.replace(',', '@');
                        result= result.replace('!', '@');
                        result= result.replace('&', '@');
                        result= result.replace('[', '@');
                        result= result.replace(']', '@');
                        result= result.replace('(', '@');
                        result= result.replace(')', '@');
                        result= result.replace('{', '@');
                        result= result.replace('}', '@');
                        result= result.replace('%', '@');
                        result= result.replace('^', '@');
                        _result= result.split("@"); 
                        //<Variable name="scan_output_strobe">    <Value>.25*scan_period+3.5ns</Value> </Variable> 
                        for(String variable: _result){
                            String temp=variable.trim();
//                            System.out.println("checking if " + temp +" is a number");
                            if(temp!=null&&(!isNumber(temp))){
//                                System.out.println(temp + " is not a number");
                                this.subVariables.add(variable.trim());
                            }
                            else{
//                                System.out.println(temp + " is a valid number");
                            }
                        }
                    }
                    
                }
                else{ 
                    _result=this.expression.toLowerCase().split("(\\d)(e)(-)(\\d)");
                    if(((_result.length>=1)&&(!_result[0].equalsIgnoreCase(this.expression.toLowerCase())))||(_result.length==0))
                        System.out.println("this expression contains 5E-5");
                    else{
                        _result=this.expression.toLowerCase().split("(\\d)(e)(\\+)(\\d)");
                        if((_result.length>=1&&(!_result[0].equalsIgnoreCase(this.expression.toLowerCase())))||(_result.length==0))
                            System.out.println("this expression contains 5E+5");
                        else{
                                                //"+ - * / ? : , !, &,{,},[,](,)"
                            if(result.charAt(0)=='(')
                                result=result.substring(1);
                            if(result!=null){
                                result= result.replace('+', '@');
                                result= result.replace('-', '@');
                                result= result.replace('*', '@');
                                result= result.replace('/', '@');
                                result= result.replace('?', '@');
                                result= result.replace(',', '@');
                                result= result.replace('!', '@');
                                result= result.replace('&', '@');
                                result= result.replace('[', '@');
                                result= result.replace(']', '@');
                                result= result.replace('(', '@');
                                result= result.replace(')', '@');
                                result= result.replace('{', '@');
                                result= result.replace('}', '@');
                                result= result.replace('%', '@');
                                result= result.replace('^', '@');
                                _result= result.split("@"); 
                                //<Variable name="scan_output_strobe">    <Value>.25*scan_period+3.5ns</Value> </Variable> 
                                for(String variable: _result){
                                    String temp=variable.trim();
//                                    System.out.println("checking if " + temp +" is a number");
                                    if(temp!=null&&(!isNumber(temp))){
//                                        System.out.println(temp + " is not a number");
                                        this.subVariables.add(variable.trim());
                                    }
                                    else{
//                                        System.out.println(temp + " is a valid number");
                                    }
                                }
                            }
                        }
                    }
                    
                }
            }
        }

        public List<String> getSubVariables() {
            return subVariables;
        }

        
    }
    public class Group{
        private int groupType=0;
        int startIndex=0;
        int stopIndex=0;
        private List<equationNode> nodes= new ArrayList<>();
        private String name=null;

        public Group(int type,int startIndex) {
            this.groupType=type;
            this.startIndex=startIndex;
        }

        public void setGroupType(int groupType) {
            this.groupType = groupType;
            
        }

        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        public void setStopIndex(int stopIndex) {
            this.stopIndex = stopIndex;
        }

        public int getGroupType() {
            return groupType;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public int getStopIndex() {
            return stopIndex;
        }

        public List<equationNode> getNodes() {
            return nodes;
        }
        
        public TreeItem getVariableRootNode(){
            if (groupType!=0) return null;
            else{
                TreeItem item= new TreeItem(new VariableLabelNodeCell());
                for(equationNode node : nodes){
                    item.getChildren().add(new TreeItem(new VariableNodeCell(node)));
                }
                return item;
            }
        }

        private String getName() {
            return name;
        }
        
        
        
        
    }

    public boolean isUseFul() {
        return useFul;
    }

    public void setUseFul(boolean useFul) {
        this.useFul = useFul;
    }
    
    
}
