/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Control.TestNodeCell_Label;
import Control.TestNodeCell_Label_2Text_Button;
import Control.VariableLabelNodeCell;
import Control.VariableNodeCell;
import Util.Equation.equationNode;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TreeItem;
import org.dom4j.Element;

/**
 *
 * @author Administrator
 */
public class Levels {
//    <blocks>
//    <DCLevels name="Levels_Standard">
//	<LoadboardRef name="OpenAllRelays" />
//	<!-- POWER -->
//	<DPS sigref="S_VDDCR_CPU">
//		<VForce>VddCrCpu</VForce>
//		<IClampHi>VddCrCpu_IClamp</IClampHi>
//		<IClampMode>Clamp</IClampMode>
//		<OverCurrentDelay>VddCrCpu_OverCurrDly</OverCurrentDelay>
//	</DPS>
//        <DCL sigref="TriggerPins">
//                    <Init>Lo</Init>
//                    <VIH>2.000</VIH>
//                    <VIL>0.000</VIL>
//                    <VClampHi>2.300</VClampHi>
//                    <VClampLo>-0.300</VClampLo>
//                    <TermVRef>0.000</TermVRef>
//            </DCL>
//    </DCLevels>
//    </blocks>
    
    private String name=null;
    private String LoadboardRef=null;
    final private List<DPS> dps = new ArrayList<>();
    final private List<DCL> dcl= new ArrayList<>();
    final private List<Connection> connection= new ArrayList<>();
    private String fileName=null;
    private boolean isReady=false;
    private TreeItem dpsRoot= new TreeItem();
    private TreeItem dclRoot= new TreeItem(); 
    private TreeItem connectionRoot= new TreeItem();
    private TreeItem loadboardRoot= new TreeItem();
//    private TreeItem root= new TreeItem();

    
    
    public Levels(String name, String fileName) {
        this.name=name;
        this.fileName=fileName;
    }
    
    public Levels(Element element, String fileName){
        
        this.fileName=fileName;
        this.name=element.attributeValue("name");
        List<Element> nodes= element.elements();
        
        for(Element node: nodes){
            if (node.getName().equals("DPS")){
                this.dps.add(new DPS(node));
            }
            else if(node.getName().equals("DCL"))
                this.dcl.add(new DCL(node));
            else if(node.getName().equals("LoadboardRef")){
//                System.out.println("this LoadBoardRef is " + node.attributeValue("name"));
                this.LoadboardRef=node.attributeValue("name");
            }
            else if(node.getName().equals("Connection")){
                this.connection.add(new Connection(node));
                
            }
            else{
                System.out.println("UnSupported Type in Levels  " + this.name + " found " + node.getName());
            }
            
        }    
        
       
    }
    public DPS buildDPS(Element element){
        return new DPS(element);
    }
    public DCL buildDCL(Element element){
        return new DCL(element);
    }
    public void printVariable(String name, String value){
//            <OverCurrentDelay>VddCrCpu_OverCurrDly</OverCurrentDelay>
        if(value!=null)
            System.out.print("<"+ name + ">" + value + "</" + name + ">");
    }
    
    public boolean search(String content){
        boolean isFound=false;
        if(this.name.equals(content)||(this.LoadboardRef!=null && this.LoadboardRef.equals(content)))
            return true;
        else{
            for(DPS dps: this.dps){
                if(dps!=null&&dps.search(content)){
                    isFound=true;
                    break;
                }
            }
            if(isFound)
                return true;
            else{
                for(DCL dcl: this.dcl){
                    if(dcl!=null&&dcl.search(content)){
                        isFound=true;
                        break;
                    }
                }
                if(isFound)
                    return true;
                else{
                    for(Connection _connection:this.connection){
                        if(_connection.search(content)){
                            isFound=true;
                            break;
                        }
                    }
                    return isFound;
                }
            }
        }
        
    }
    public String getFileName() {
        return fileName;
    }
    public void print(){
        //<DCLevels name="Levels_Standard">
        System.out.println("<DCLevels name=\"" + this.name + "\">");
        if(this.LoadboardRef!=null) 
            //<LoadboardRef name="OpenAllRelays" />
            System.out.println("    <LoadboardRef name=\"" + this.LoadboardRef + "\" />");
        for(DPS _dps: this.dps){
            _dps.print();
        }
        for(DCL _dcl: this.dcl){
            _dcl.print();
        }
        for(Connection _connection: this.connection){
            _connection.print();
        }
        
        System.out.println("</DCLevels>");
    }
    public void print(PrintWriter printWriter){
        //<DCLevels name="Levels_Standard">
        printWriter.println("<DCLevels name=\"" + this.name + "\">");
        if(this.LoadboardRef!=null) 
            //<LoadboardRef name="OpenAllRelays" />
            printWriter.println("    <LoadboardRef name=\"" + this.LoadboardRef + "\" />");
        for(DPS _dps: this.dps){
            _dps.print(printWriter);
        }
        for(DCL _dcl: this.dcl){
            _dcl.print(printWriter);
        }
        for(Connection _connection: this.connection){
            _connection.print(printWriter);
        }
        
        printWriter.println("</DCLevels>");
    }
    
    public String getLoadboardRef() {
        return LoadboardRef;
    }
    
    

    public void buildTree(TreeItem root) {
//        this.print();
//        System.out.println("start to build levels tree " + this.name);
//       TreeItem root= XMLRead.timing_level.get(this.name);
       if(this.LoadboardRef!=null){ 
           System.out.println("This LoadBoardRef is " + this.LoadboardRef);
           loadboardRoot= new TreeItem(new TestNodeCell_Label_2Text_Button(XMLRead.loadBoards.get(this.LoadboardRef), this.LoadboardRef,null));
       }
       if(!this.dps.isEmpty()) 
           dpsRoot= new TreeItem(new TestNodeCell_Label("DPS"));
       if(!this.dcl.isEmpty()) 
           dclRoot= new TreeItem(new TestNodeCell_Label("DCL"));
       if(!this.connection.isEmpty())
           connectionRoot= new TreeItem(new TestNodeCell_Label("Connection"));
       
       for(DPS _dps: this.dps){
            dpsRoot.getChildren().add(_dps.getRoot());
//            System.out.println("build dps item " + _dps.sigRef);
       }
       for(DCL _dcl: this.dcl){
           dclRoot.getChildren().add(_dcl.getRoot());
       }
       for(Connection _connection: this.connection){
           connectionRoot.getChildren().add(_connection.getRoot());
       }
       if(this.LoadboardRef!=null) 
           root.getChildren().add(loadboardRoot);
       
       if(!this.dps.isEmpty()){
           root.getChildren().add(dpsRoot);
//           System.out.println("add dps tree");
       }
       if(!this.dcl.isEmpty())
           root.getChildren().add(dclRoot);
       if(!this.connection.isEmpty())
           root.getChildren().add(connectionRoot);
       isReady=true;
//       return root;
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
    
    public void getRootItem(TreeItem root){
        if(isReady){
            if(this.LoadboardRef!=null)
                root.getChildren().add(this.loadboardRoot);
            if(!this.dps.isEmpty())
                root.getChildren().add(this.dpsRoot);
            if(!this.dcl.isEmpty())
                root.getChildren().add(this.dclRoot);
            if(!this.connection.isEmpty())
                root.getChildren().add(this.connectionRoot);
            setExpanded(root);
        }
        else{
            buildTree(root);
        }
    }

    public List<DPS> getDps() {
        return dps;
    }

    public List<DCL> getDcl() {
        return dcl;
    }
    
    public List<Connection> getConnection() {
        return connection;
    }
    
    public void setLoadboardRef(String LoadboardRef) {
        this.LoadboardRef = LoadboardRef;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void updateLevels(){
        for(DPS _dps: this.dps){
            _dps.udpate();
        }
        for(DCL _dcl: this.dcl){
            _dcl.update();
        }
    
    }
    
    public class DPS{
//      <DPS sigref="S_VDDCR_CPU">
//		<VForce>VddCrCpu</VForce>
//		<IClampHi>VddCrCpu_IClamp</IClampHi>
//		<IClampMode>Clamp</IClampMode>
//		<OverCurrentDelay>VddCrCpu_OverCurrDly</OverCurrentDelay>
//	</DPS>
        
        private String sigRef=null;
        private List<Item> items= new ArrayList<>();
        


        public DPS(Element element) {
            
            this.sigRef= element.attributeValue("sigref");
            
            
            List<Element> nodes= element.elements();
            for(Element node: nodes){
                    this.items.add(new Item(node));
                }
            
        }
        public boolean search(String content){
//            System.out.println("Start Search DPs in " + this.sigRef);
            boolean isFound=false;    
            for(Item item: this.items){
                if(item!=null&&item.search(content)){
                    isFound=true;
                    break;
                            
                }
                
            }
            return isFound;
            
        } 

        public TreeItem getRoot() {
            TreeItem root= new TreeItem( new TestNodeCell_Label(this.sigRef));
            root.getChildren().add(new TreeItem(new VariableLabelNodeCell()));
             for(Item item: this.items){
                 root.getChildren().add(item.getTreeItem());
                }
            return root;
        }
        
       
        
        public List<Item> getItems() {
            return items;
        }
        public void print(){
            System.out.println("    <DPS sigref=\"" + this.sigRef + "\">");
            for(Item item: this.items){
                item.print();
            }
            System.out.println("    </DPS>");
        }
        public void print(PrintWriter printWriter){
            printWriter.println("    <DPS sigref=\"" + this.sigRef + "\">");
            for(Item item: this.items){
                item.print(printWriter);
            }
            printWriter.println("    </DPS>");
        }
        
        public void udpate(){
            for(Item item: this.items){
                item.update();
                
            }
        }
   }

    public class DCL{
        
        private String sigRef=null;
        private List<Item> items= new ArrayList<>();
                
        public DCL(Element element) {
            
            this.sigRef= element.attributeValue("sigref");
            List<Element> nodes= element.elements();
            for(Element node: nodes){
                this.items.add(new Item(node));
            }
        }
        //<DCL sigref="TriggerPins">
//                    <Init>Lo</Init>
//                    <VIH>2.000</VIH>
//                    <VIL>0.000</VIL>
//                    <VClampHi>2.300</VClampHi>
//                    <VClampLo>-0.300</VClampLo>
//                    <TermVRef>0.000</TermVRef>
//            </DCL>
        public boolean search(String content){
            boolean isFound=false;    
            for(Item item: this.items){
//                System.out.println("Start Search DCL in " + this.sigRef);
                if(item!=null && item.search(content)){
                    isFound=true;
                    break;
                            
                }
                
            }
            return isFound;
            
        }
        
        public void update(){
            for(Item item: this.items){
                item.update();
            }
        }

        //<DCL sigref="TriggerPins">
//                    <Init>Lo</Init>
//                    <VIH>2.000</VIH>
//                    <VIL>0.000</VIL>
//                    <VClampHi>2.300</VClampHi>
//                    <VClampLo>-0.300</VClampLo>
//                    <TermVRef>0.000</TermVRef>
//            </DCL>
       
        public void print(){
            System.out.println("    <DCL sigref=\""+ this.sigRef + "\">");
            for(Item item: this.items){
                item.print();
            }
            System.out.println("    </DCL>");   
        }
        public void print(PrintWriter printWriter){
             printWriter.println("    <DCL sigref=\""+ this.sigRef + "\">");
            for(Item item: this.items){
                item.print(printWriter);
            }
             printWriter.println("    </DCL>");   
        }
        
        public TreeItem getRoot() {
            TreeItem root= new TreeItem( new TestNodeCell_Label(this.sigRef));
            root.getChildren().add(new TreeItem(new VariableLabelNodeCell()));
                    
             for(Item item: this.items){
                 root.getChildren().add(item.getTreeItem());
                }
            return root;
        }

        public List<Item> getItems() {
            return items;
        }
        
        
    }
    public class Connection{
        
        private String sigRef=null;
        private List<Item> items= new ArrayList<>();
                
        public Connection(Element element) {
            
            this.sigRef= element.attributeValue("sigref");
            List<Element> nodes= element.elements();
            for(Element node: nodes){
                this.items.add(new Item(node));
            }
        }
        //<DCL sigref="TriggerPins">
//                    <Init>Lo</Init>
//                    <VIH>2.000</VIH>
//                    <VIL>0.000</VIL>
//                    <VClampHi>2.300</VClampHi>
//                    <VClampLo>-0.300</VClampLo>
//                    <TermVRef>0.000</TermVRef>
//            </DCL>
        public boolean search(String content){
            boolean isFound=false;    
            for(Item item: this.items){
//                System.out.println("Start Search DCL in " + this.sigRef);
                if(item!=null && item.search(content)){
                    isFound=true;
                    break;
                            
                }
                
            }
            return isFound;
            
        }

        //<DCL sigref="TriggerPins">
//                    <Init>Lo</Init>
//                    <VIH>2.000</VIH>
//                    <VIL>0.000</VIL>
//                    <VClampHi>2.300</VClampHi>
//                    <VClampLo>-0.300</VClampLo>
//                    <TermVRef>0.000</TermVRef>
//            </DCL>
       
        public void print(){
            System.out.println("    <Connection sigref=\""+ this.sigRef + "\">");
            for(Item item: this.items){
                item.print();
            }
            System.out.println("    </Connection>");   
        }
        public void print(PrintWriter printWriter){
             printWriter.println("    <Connection sigref=\""+ this.sigRef + "\">");
            for(Item item: this.items){
                item.print(printWriter);
            }
             printWriter.println("    </Connection>");   
        }
        
        public TreeItem getRoot() {
            TreeItem root= new TreeItem( new TestNodeCell_Label(this.sigRef));
            root.getChildren().add(new TreeItem(new VariableLabelNodeCell()));
                    
             for(Item item: this.items){
                 root.getChildren().add(item.getTreeItem());
                }
            return root;
        }

        public List<Item> getItems() {
            return items;
        }
        
        
    }
    public class Item{
        private String name=null;
        private String value=null;
        private String expression=null;
        private TreeItem treeItem = new TreeItem();

        public Item(Element node) {
            if(node!=null){
                this.name=node.getName();
                this.value=node.getText().trim();
                this.expression=node.getText().trim();
            }
        }
        public void print(){
            System.out.print("        <"+ name + ">" + expression + "</" + name + ">");
        }
        public void print(PrintWriter printWriter){
            printWriter.print("        <"+ name + ">" + expression + "</" + name + ">");
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getExpression() {
            return expression;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
        
        public TreeItem getTreeItem(){
            
           treeItem = new TreeItem( new VariableNodeCell(this.name, this.value, this.expression));
            return treeItem;
        }
        
        public void update(){
            if(this.treeItem!=null){
                VariableNodeCell variableNodeCell=(VariableNodeCell)this.treeItem.getValue();
                equationNode eqnNode=XMLRead.variables.get(this.expression);
                if(eqnNode!=null)
                    variableNodeCell.updateLevelValue(eqnNode.getValue());
            }
           
        }
        
        public boolean search(String content){
            if(this.name.equals(content)||this.expression.equals(content))
                return true;
            else
                return false;
        }
                
    }
   
    
}
