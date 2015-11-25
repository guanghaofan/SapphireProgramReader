/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader.blockreader;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ghfan
 */
public final class StartNode {
    
    
    /*
    <Node name="START">
       <Type>Entry</Type>
       <GoTo result="PASS">    <NodeRef>SetVar%TestedUnits:+</NodeRef><Decision>Pass</Decision></GoTo>
    </Node>
     */
    private String nodeName=null;
    private String nodeType =null;
    public List<GoToResult> result =null;
    private String flowName=null;
    private int nodeNo=0;
    public int flowNo=0;
    private List<String> textLine= new ArrayList<>();
    
    public StartNode(){}
    public StartNode(String nodeName, String nodeType,List<GoToResult> result, String flowName, int nodeNum, int flowNo){
        this.nodeName=nodeName;
        this.nodeType=nodeType;
        this.result=result;
        this.flowName=flowName;
        this.nodeNo=nodeNum;
        this.flowNo=flowNo;
        getLine();
    }
    
    public void printStartNode(){
        System.out.println("  <Node name=\"" + nodeName + "\">");
        System.out.println("      <Type>" + nodeType + "</Type>");
        for(GoToResult nodeResult: result){
            nodeResult.printGoToResult();
        }
        System.out.println("  </Node>");
    }
    public void printStartNode(PrintWriter printWriter){
        printWriter.println("<Node name=\"" + nodeName + "\">");
        printWriter.println("    <Type>" + nodeType + "</Type>");
        for(GoToResult nodeResult: result){
            nodeResult.printGoToResult(printWriter);
        }
        printWriter.println("</Node>");
    }
    public void printStartNodeInFlow(PrintWriter printWriter){
        printWriter.println("<    Node name=\"" + nodeName + "\">");
        printWriter.println("        <Type>" + nodeType + "</Type>");
        for(GoToResult nodeResult: result){
            nodeResult.printGoToResultInFlow(printWriter);
        }
        printWriter.println("    </Node>");
    }
    
    private void getLine(){
      
        textLine.add("<Node name=\"" + nodeName + "\">");
        textLine.add("      <Type>" + nodeType + "</Type>");
        for(GoToResult nodeResult: result){
            //nodeResult.printGoToResult();
            textLine.add("      <GoTo result=\"" + nodeResult.getReuslt() + "\">    <NodeRef>" + nodeResult.getNodeRef() + "</NodeRef><Decision>" + nodeResult.getReuslt()+ "</Decision></GoTo>");
        } 
        textLine.add("</Node>");
    }
    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }


    public List<GoToResult> getResult() {
        return result;
    }

    public void setResult(List<GoToResult> result) {
        this.result = result;
    }
    

    public int getNodeNo() {
        return nodeNo;
    }

    public void setNodeNo(int nodeNum) {
        this.nodeNo = nodeNum;
    }


    public int getFlowNo() {
        return flowNo;
    }
    public List<String> getText(){
        return this.textLine;
    }
    public boolean search(String content){
        boolean isFound=false;
        for(GoToResult result: this.getResult()){
            if(result.search(content)){
                isFound=true;
                break;
            }
        }
        return isFound;
    }
    public boolean containsSearch(String content){
        boolean isFound=false;
        for(GoToResult result: this.getResult()){
            if(result.containsSearch(content)){
                isFound=true;
                break;
            }
        }
        return isFound;
    }
}
