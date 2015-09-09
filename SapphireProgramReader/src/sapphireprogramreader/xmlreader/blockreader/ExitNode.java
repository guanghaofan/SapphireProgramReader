/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ghfan
 */
public class ExitNode {
    /*
    <Node name="CONTINUE">
       <Type>Exit</Type>
       <Decision>Pass</Decision>
    </Node> 
     */
    private String nodeName=null;
    private String nodeType=null;
    private String decision =null;
    private String flowName=null; // this is to record the flow name
    private int nodeNo=0;
    public int flowNo=0;
    private List<String> textLine = new ArrayList<>();
    
    public ExitNode(String nodeName, String nodeType, String decision, String flowName, int nodeNo, int flowNo){
        this.nodeName=nodeName;
        this.nodeType=nodeType;
        this.decision=decision;
        this.flowName=flowName;
        this.flowNo=flowNo;
        this.nodeNo=nodeNo;
        getLine();
    }
    public void printExitNode(){
        System.out.println("  <Node name=\"" + nodeName + "\">");
        System.out.println("      <Type>" + nodeType + "</Type>");
        System.out.println("      <Decision>" + decision + "</Decision>");
        System.out.println("  </Node>");
        
    }
    public void printExitNode(PrintWriter printWriter){
        printWriter.println("    <Node name=\"" + nodeName + "\">");
        printWriter.println("        <Type>" + nodeType + "</Type>");
        printWriter.println("        <Decision>" + decision + "</Decision>");
        printWriter.println("    </Node>");
        
    }
    
    private void getLine(){
       
        textLine.add("<Node name=\"" + nodeName + "\">");
        textLine.add("      <Type>" + nodeType + "</Type>");
        textLine.add("      <Decision>" + decision + "</Decision>");
        textLine.add("</Node>");
                                
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
 
    public int getNodeNo() {
        return nodeNo;
    }

    public void setNodeNo(int nodenum) {
        this.nodeNo = nodenum;
    }

    public int getFlowNo() {
        return flowNo;
    }
    public List<String> getText(){
        return this.textLine;
    }

}
