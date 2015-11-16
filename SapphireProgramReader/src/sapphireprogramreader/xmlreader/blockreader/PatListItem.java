/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader.blockreader;

import java.io.PrintWriter;
import java.util.List;
import org.dom4j.Element;

/**
 *
 * @author ghfan
 */
public class PatListItem {
    private String name=null;
    private String type=null;
    private String enabled=null;
    private String loaded =null; 
//    private TreeItem treeItem;
   
    public PatListItem(Element element) {
        List<Element> nodes =element.elements();
        if(!nodes.isEmpty()){
            for(Element node: nodes){    
                if(node.getName().equalsIgnoreCase("PatternRef")){
                    this.name=node.getText();
                    this.type="PatternRef";
                } 
                else if(node.getName().equalsIgnoreCase("PatternBurstRef")){
                    this.name=node.getText();
                    this.type="PatternBurstRef";
                }
                else if(node.getName().equalsIgnoreCase("Enabled")){
                    this.enabled=node.getText();
                }
                else if(node.getName().equalsIgnoreCase("Load")){
                    this.loaded= node.getText();
                } 
            }
        }   
        
        
    }

    public String getEnabled() {
        return enabled;
    }

    public String getLoaded() {
        return loaded;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
    
    public void print(){
        System.out.println("        " + toString());
    }
    public void print(PrintWriter printWriter){
        printWriter.println("    " + toString());
    }

    @Override
    public String toString() {
        
//        <PatListItem><PatternRef>mbist_StartBist_SMS_L2_node11</PatternRef> <Enabled>DIE_0_CU_2</Enabled> </PatListItem>
//	<PatListItem><PatternBurstRef>JTAG_WAIT_50K</PatternBurstRef> <Enabled>1</Enabled> </PatListItem>
//        <PatListItem><PatternRef>ScanDelay_CPU_SX_A0_SP1_ATLAS-Intest1_PR152469_022614_TK_MC-GPIO_PLL_Std1</PatternRef><Enabled>1</Enabled><Load>true</Load></PatListItem>
        String str="<PatListItem>";
        if (this.type.equals("PatternRef")) 
            str+="<PatternRef>"+ this.name + "</PatternRef>";
        else 
            str+="<PatternBurstRef>"+ this.name + "</PatternBurstRef>";
        if (this.enabled!=null) str+="<Enabled>" + this.enabled+ "</Enabled>";
        if (this.loaded!=null) str+="<Load>"+ this.loaded +"</Load>";
        str+="</PatListItem>";
        return str; //To change body of generated methods, choose Tools | Templates.
    }

    public boolean equals(PatListItem item) {
        if(this.enabled==null){
            if(this.name.equals(item.getName())&&item.getEnabled()==null){
                return true;
            }
            else{
                return false; //To change body of generated methods, choose Tools | Templates.
            }
        }
        else{
            if(item.getEnabled()!=null && item.getEnabled().equals(this.enabled)&& item.getName().equals(this.name))
                return true;
            else
                return false;
                        
        
        }
    }
    
    

}
