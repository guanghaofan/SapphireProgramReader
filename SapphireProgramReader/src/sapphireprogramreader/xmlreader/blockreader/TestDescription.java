/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader.blockreader;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Element;
import java.io.PrintWriter;

/**
 *
 * @author ghfan
 */
public class TestDescription {
    private Item root;
    private String fileName=null;

    
    
    public TestDescription(Element element, String fileName) {
        root= new Item(element,0);
        this.fileName=fileName;
    }
    public boolean search(String content){
        return root.search(content);
    }
    

    public Item getRoot() {
        return root;
    }
    public void print(){
        root.print();
    }
    
    public void print(PrintWriter printWriter){
        root.print(printWriter);
    }

    public String getFileName() {
        return fileName;
    }
    
    public class Item{
        private String name=null;
        private boolean isLeaf=true;
        private String attriName=null;
        private String expression=null;
        private List<Item> subItems= new ArrayList<>();;
        private int level=0;
        private String space="";
        private String previousItem="";
//        private Item motherItem;

        public Item() {
        }
        public Item(Element element, int _level) {
            this.level=_level;
            this.name=element.getName();
//            System.out.println("node name is "+ this.name);
            List<Element>nodes= element.elements();
            if(nodes.isEmpty()){
                this.isLeaf=true;
                this.expression=element.getText();
//                if(name.toLowerCase().contains("patternburst")/*&&XMLRead.patternBursts.get(expression)!=null*/){
//                    patternBurst=this.expression;
//                }
//                else if(name.toLowerCase().contains("level")/*&&XMLRead.levels.get(expression)!=null*/){
//                    levels=this.expression;
//                }
//                else if(name.toLowerCase().contains("timing")/*&&XMLRead.timing.get(expression)!=null*/){
//                    timing=this.expression;
//                }
//                else if(name.toLowerCase().contains("loadboard")/*&&XMLRead.timing.get(expression)!=null*/){
//                    loadBoard=this.expression;
//                }
                
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
                    Item item= new Item(node, this.level+1);
                    item.previousItem=temp;
                    temp=item.getName();
                    this.subItems.add(item);   
                }
            }
            
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
                    for(Item item: this.subItems){
                        if(item!=null&&item.search(content)){
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
                    for(Item item: this.subItems){
                        item.print();
                    }
                    
                    System.out.println(getSpace()+"</"+ this.name +">");
                }
                else{
                    System.out.println(getSpace()+"<"+ this.name +">");
                    for(Item item: this.subItems){
                        item.print();
                    }
                    System.out.println(getSpace()+"</"+ this.name +">");
                }
            }
            else
                System.out.println(getSpace()+"<"+ this.name + ">"+ this.expression + "</" + this.name +">");
        }
        
        public void print(PrintWriter printWriter){
            if(!this.isLeaf){
                if (this.expression!=null){
                    printWriter.println(getSpace() + "<" + this.name + " "+ this.attriName +"=\"" + this.expression + "\">");
                    for(Item item: this.subItems){
                        item.print(printWriter);
                    }
                    
                    printWriter.println(getSpace()+"</"+ this.name +">");
                }
                else{
                    printWriter.println(getSpace()+"<"+ this.name +">");
                    for(Item item: this.subItems){
                        item.print(printWriter);
                    }
                    printWriter.println(getSpace()+"</"+ this.name +">");
                }
            }
            else
                printWriter.println(getSpace()+"<"+ this.name + ">"+ this.expression + "</" + this.name +">");
            
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
 
        
    }
}
