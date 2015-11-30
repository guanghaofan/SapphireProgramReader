/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader.blockreader;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

/**
 *
 * @author ghfan
 */
public class DeviceNode {
    private String name=null;
    private String nodeType= "Device";
    private String binningRef=null;
    private int nodeNum=-1;
    private String flowName=null;
   
    public int flowNo=0;
    private List<GoToResult> gotoResult=new ArrayList<>();
    private boolean used=false;

    public DeviceNode(Element element, String _flowName, int _nodeNum, int _flowNo) {
        name=element.attributeValue("name");
        this.nodeNum=_nodeNum;
        this.flowName=_flowName;
        this.flowNo=_flowNo;
        List<Element>nodes= element.elements();
            if(!nodes.isEmpty()){
                for(Element node: nodes){
                    if(node.getName().equals("BinningRef")) 
                        this.binningRef=node.getText();
                    else if(node.getName().equals("GoTo"))
                        gotoResult.add(new GoToResult(node.attributeValue("result"),node.elementText("NodeRef"),node.elementText("Decision")));
                }
            }
    }
    public void print(){
        //System.out.println("");
        System.out.println("  <Node name=\"" + name + "\">");
        System.out.println("      <Type>Device</Type>");
        System.out.println("      <BinningRef>" + this.binningRef+  "</BinningRef>");
        for(GoToResult gotoResult: this.gotoResult){
            gotoResult.printGoToResult();
        }
        System.out.println("    </Node>");
    }
    
    public void printWriter(PrintWriter printWriter, String space){
        if(space==null){
        //System.out.println("");
            printWriter.println("<Node name=\"" + name + "\">");
            printWriter.println("  <Type>Device</Type>");
            printWriter.println("  <BinningRef>" + this.binningRef+  "</BinningRef>");
            for(GoToResult gotoResult: this.gotoResult){
                gotoResult.printGoToResultInFlow(printWriter);
            }
            printWriter.println("</Node>");
        }
        else{
            printWriter.println("  <Node name=\"" + name + "\">");
            printWriter.println("    <Type>Device</Type>");
            printWriter.println("    <BinningRef>" + this.binningRef+  "</BinningRef>");
            for(GoToResult gotoResult: this.gotoResult){
                gotoResult.printGoToResultInFlow(printWriter);
            }
            printWriter.println("  </Node>");
        
        }
        
        
    }

    public String getFlowName() {
        return flowName;
    }

    public int getFlowNo() {
        return flowNo;
    }

    public String getBinningRef() {
        return binningRef;
    }

    public String getName() {
        return name;
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public String getNodeType() {
        return nodeType;
    }

    public List<GoToResult> getGotoResult() {
        return gotoResult;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
    
    public boolean search(String content){
        if(this.name.equals(content))
            return true;
        else if(this.nodeType.equals(content))
            return true;
        else if(this.binningRef.equals(content))
            return true;
        else{
            boolean find=false;
            for(GoToResult result: this.gotoResult){
                if(result.search(content)){
                    find =true;
                    break;
                }
                    
            }
            return find;
        }
    }
    
    public boolean containsSearch(String content){
        if(this.name.toLowerCase().contains(content))
            return true;
        else if(this.nodeType.toLowerCase().contains(content))
            return true;
        else if(this.binningRef.toLowerCase().contains(content))
            return true;
        else{
            boolean find=false;
            for(GoToResult result: this.gotoResult){
                if(result.containsSearch(content)){
                    find =true;
                    break;
                }
                    
            }
            return find;
        }
    }
    
    
}
