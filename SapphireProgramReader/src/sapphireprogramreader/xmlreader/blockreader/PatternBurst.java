/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader.blockreader;

import sapphireprogramreader.ui.controls.PatternRefCell;
import sapphireprogramreader.ui.controls.TestNodeCell_Label_2Text_Button;
import sapphireprogramreader.ui.controls.TestNodeCell_Label_Text;
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
public class PatternBurst {
    private String name=null;
    private String composite =null;
    private String executionMode=null;
    private String compareRef=null;
    private String fileName=null;
    private int patternNo=-1;
    private List<PatListItem> patternList= new ArrayList<>();
    private boolean used=false;
   
//    private boolean treeIsReady=false;

//    public PatternBurst(String name, String composite, String executionMode, String compareRef, String fileName) {
//        this.name=name;
//        this.composite=composite;
//        this.executionMode=executionMode;
//        this.compareRef=compareRef;
//        this.fileName= fileName;
//    }
    public void addNode(Element node){ 
        if(node.getName().equalsIgnoreCase("Composite")){
            this.composite=node.getText();
        } 
        else if(node.getName().equalsIgnoreCase("ExecutionMode")){
            this.executionMode=node.getText();
        }
        else if(node.getName().equalsIgnoreCase("CompareRef")){
            this.compareRef=node.getText();
        }
        else if(node.getName().equalsIgnoreCase("PatListItem")){
            this.patternList.add(new PatListItem(node));
        } 
    }
    
    public PatternBurst(Element element, String fileName, int patNo){
        this.fileName=fileName;
        this.patternNo=patNo;
        
        List<Attribute> attrs = element.attributes();  
        if (attrs != null && attrs.size() > 0) {    
            for (Attribute attr : attrs) {   
                this.name = attr.getValue();
            }
        }
        List<Element> nodes =element.elements();
        for(Element node: nodes){    
            if(node.getName().equalsIgnoreCase("Composite")){
                this.composite=node.getText();
            } 
            else if(node.getName().equalsIgnoreCase("ExecutionMode")){
                this.executionMode=node.getText();
            }
            else if(node.getName().equalsIgnoreCase("CompareRef")){
                this.compareRef=node.getText();
            }
            else if(node.getName().equalsIgnoreCase("PatListItem")){
                PatListItem patListItem= new PatListItem(node);
                this.patternList.add(patListItem);
                if(patListItem.getType().equals("PatternRef")){
//                    if(XMLRead.patRefTreeItems.get(patListItem.getName())!=null){
//                        System.out.println("Repeat PataternRef " + XMLRead.patRefTreeItems.get(patListItem.getName()).toString());
//                        System.out.println(patListItem.toString());
//                    }
//                    XMLRead.patRefTreeItems.put(patListItem.getName(), new TreeItem((new PatternRefCell(patListItem)))); // this is effective for all the products, memory saved a lot.
//                    patListItem.setTreeItem(new TreeItem(new PatternRefCell(patListItem))); // add 20M memory for Styx
                    
                    
                }
            } 
        }
    }
    
    public TreeItem getCompositeTreeItem(){
        if(this.composite!=null){
            TreeItem root=XMLRead.compositeTreeItems.get(this.composite);
            if(root!=null)
                return root;
            else{
                root= new TreeItem( new TestNodeCell_Label_Text("Composite",this.composite));
                XMLRead.compositeTreeItems.put(this.composite,root );
                return root;
            }
        }
        else return null;
    }
    public TreeItem getCompareRefTreeItem(){
        if(this.compareRef!=null){
            TreeItem root= XMLRead.compareTreeItems.get(this.compareRef);
            if (root!=null)
                return root;
            else{
                root= new TreeItem( new TestNodeCell_Label_Text("CompareRef",this.compareRef));
                XMLRead.compareTreeItems .put(this.compareRef, root);
                return root;
            }
        }
        else return null;
    }

    public TreeItem getPatRefTreeItem(PatListItem item){
        patRefTreeItem root= XMLRead.patRefTreeItems.get(item.getName());
        System.out.println();
        System.out.println("<------------Start patRefTreeItem " + item.getName());
        if((root!=null) &&(root.getItem().equals(item))){
                
                if(XMLRead.currentPatRefTree.contains(item.getName())){
                    System.out.println("Clone existing patRefTreeItem " + item.getName());
                    TreeItem temp = root.clone();
                    System.out.println("<--------------End patRefTreeItem " + item.getName());
                    return temp;
                }
                else{
                    System.out.println("Re-use existing patRefTreeItem " + item.getName());
                    XMLRead.currentPatRefTree.add(item.getName());
                    System.out.println("<--------------End patRefTreeItem " + item.getName());
                    return root;
                }
        }

        else{
            patRefTreeItem treeItem= new patRefTreeItem(item);
            XMLRead.patRefTreeItems.put(item.getName(), treeItem);
            XMLRead.currentPatRefTree.add(item.getName());
            System.out.println("build patRefTreeItem " + item.getName());
            System.out.println("<--------------End patRefTreeItem " + item.getName());
            return treeItem;
        }
        
    }
//    public void setTreeIsReady(boolean treeIsReady) {
//        this.treeIsReady = treeIsReady;
//    }

//    public boolean isTreeIsReady() {
//        return treeIsReady;
//    }
    
    public boolean search(String content){
        if (this.name.equals(content)||( this.compareRef!=null &&this.compareRef.equals(content))||(this.composite!=null&&this.composite.equals(content)))
            return true;
        else{
            boolean isFind=false;
            for(PatListItem item: this.getPatternList()){
                if(item.getName().equals(content)||(item.getEnabled()!=null && item.getEnabled().equals(content))){
                    isFind=true;
                    break;
                }
            }
            return isFind;
        }
    }
    public boolean containsSearch(String content){
        if (this.name.toLowerCase().contains(content)||( this.compareRef!=null &&this.compareRef.toLowerCase().contains(content))||(this.composite!=null&&this.composite.toLowerCase().contains(content)))
            return true;
        else{
            boolean isFind=false;
            for(PatListItem item: this.getPatternList()){
                if(item.getName().toLowerCase().contains(content)||(item.getEnabled()!=null && item.getEnabled().toLowerCase().contains(content))){
                    isFind=true;
                    break;
                }
            }
            return isFind;
        }
    }
    public TreeItem getChildren( TreeItem root){

        
//        System.out.println("Pattern Burst is clear "  + this.name);
        root.setExpanded(true);

        if(this.composite!=null)
            root.getChildren().add(getCompositeTreeItem());
        if(this.compareRef!=null)
            root.getChildren().add(getCompareRefTreeItem());
        for(PatListItem item: this.patternList){
            if (item.getType().equals("PatternRef")){
                root.getChildren().add(getPatRefTreeItem(item));

//                    FXML_Label_TextController c= new FXML_Label_TextController();
//                    c.setText("this is a custom control");
//                    TreeItem b = new TreeItem( new TextField("ssss"));
//                    root.getChildren().add(b);
//                    root.getChildren().add(b);

            }
            else{
                PatternBurst patternBurst= XMLRead.patternBursts.get(item.getName());
                if(patternBurst!=null)
                    root.getChildren().add(patternBurst.getRoot(false));
            }
        }
        XMLRead.currentPatternBurst.add(this.name);
        return root;
        
    }
    public TreeItem getRoot(boolean isFirst){
        TreeItem root= buildRoot();
        
        root.getChildren().clear();
        System.out.println("Pattern Burst is clear "  + this.name);
        root.setExpanded(false);

        if(this.composite!=null && isFirst)
            root.getChildren().add(getCompositeTreeItem());
        if(this.compareRef!=null && isFirst)
            root.getChildren().add(getCompareRefTreeItem());
        for(PatListItem item: this.patternList){
            if (item.getType().equals("PatternRef")){
                root.getChildren().add(getPatRefTreeItem(item));

//                    FXML_Label_TextController c= new FXML_Label_TextController();
//                    c.setText("this is a custom control");
//                    TreeItem b = new TreeItem( new TextField("ssss"));
//                    root.getChildren().add(b);
//                    root.getChildren().add(b);

            }
            else{
                PatternBurst patternBurst= XMLRead.patternBursts.get(item.getName());
                if(patternBurst!=null)
                    root.getChildren().add(patternBurst.getRoot(false));
            }
        }
        XMLRead.currentPatternBurst.add(this.name);
        return root;
        
    }
    public TreeItem rebuildRoot(){
        
        TreeItem root;
        
        root= new TreeItem(new TestNodeCell_Label_2Text_Button(this));
        XMLRead.patBurstRootItems.put(this.name, root);
        return  root;
        
    }
    public TreeItem buildRoot(){
        TreeItem root;
        root= XMLRead.patBurstRootItems.get(this.name);
        
        if(root==null){
            root= new TreeItem(new TestNodeCell_Label_2Text_Button(this));
            XMLRead.patBurstRootItems.put(this.name, root);
        }
        boolean isUsed=false;
        
        for(String patBurstName: XMLRead.currentPatternBurst){
            if(patBurstName.equals(this.name)){
                isUsed=true;
                break;
            }
        }
        if(isUsed){
            System.out.println("Repeat PatternBurst  " + this.name  + "  need to Re-build");
            root= rebuildRoot();
        }
        else{}
        return root;
    }
    public int getPatternNo() {
        return patternNo;
    }

    public List<PatListItem> getPatternList() {
        return patternList;
    }

    public String getComposite() {
        return composite;
    }

    public String getCompareRef() {
        return compareRef;
    }

    public String getName() {
        return name;
    }
    

    public String getFileName() {
        return fileName;
    }
    
    public void print(){
        System.out.println(this.nameToString());
        if (this.composite!=null) System.out.println(this.compositeToString());
        if (this.executionMode!=null) System.out.println(this.executionModeToString());
        if (this.compareRef!=null) System.out.println(this.compareRefToString());
        for(PatListItem item: patternList){
            item.print();
        }
        System.out.println("</PatternBurst>");
        
    }

    public String nameToString(){
        return "<PatternBurst name=\"" + this.name + "\">";
    }
    public String compositeToString(){
        if(this.composite!=null) 
            return "    <Composite>" + this.composite + "</Composite>";
        else return null;
    }
    public String executionModeToString(){
        if (this.executionMode!=null) 
            return "    <ExecutionMode>" + this.executionMode + "</ExecutionMode>";
        else return null;
    }
    public String compareRefToString(){
        if (this.compareRef!=null) 
            return "    <CompareRef>" + this.compareRef + "</CompareRef>";
        else return null;
    }
    public class patRefTreeItem extends TreeItem{
    
        private PatListItem item;

        public patRefTreeItem(PatListItem item) {
            if(item.getType().equals("PatternRef")){
                super.setValue(new PatternRefCell(item));
                this.item=item;
            }
        }

        public PatListItem getItem() {
            return item;
        }
        public TreeItem clone(){
            return new TreeItem(new PatternRefCell(item));
        }
        
    }
    
    public void print(PrintWriter printWriter){
                    
        printWriter.println(this.nameToString());
        if (this.composite!=null) printWriter.println(this.compositeToString());
        if (this.executionMode!=null) printWriter.println(this.executionModeToString());
        if (this.compareRef!=null) printWriter.println(this.compareRefToString());
        for(PatListItem item: patternList){
            item.print(printWriter);
        }
        printWriter.println("</PatternBurst>");
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
    
}
