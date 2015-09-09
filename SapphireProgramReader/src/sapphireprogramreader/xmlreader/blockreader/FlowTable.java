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
public class FlowTable {
    private String flowName=null;
    private String equationsRef=null;
//    private List<TestNode> testNodes = new ArrayList<>();
//    private List<FlowNode> flowNodes = new ArrayList<>();
    private String fileName=null;
    private List<BaseNode> nodes = new ArrayList<>();
    
    private StartNode startNode = new StartNode();
    private List<ExitNode> exitNodes = new ArrayList<>();
    private List<DeviceNode> deviceNodes= new ArrayList<>();
    private int nodeCnt=0;
    private boolean isUsed=false;
    
    public void FlowTable(){}
    
    public void printFlowTable(){
        
        
        System.out.println("<Flow name=\"" + flowName +"\">");
        
        if ((equationsRef!=null) && (equationsRef!=""))
        
            System.out.println("      <EquationsRef>" + equationsRef + "</EquationsRef>"); 
        
        // print start node
        startNode.printStartNode();

        // print test node or flow node
        
        for(BaseNode baseNode: nodes){
            if (baseNode.getNodeType().equalsIgnoreCase("test"))
                baseNode.printTestNode();
            else if (baseNode.getNodeType().equalsIgnoreCase("flow"))
                baseNode.printFlowNode();
        }
       
        for(ExitNode exitNode: exitNodes){
            exitNode.printExitNode();
        }
        
        System.out.println("</Flow>");
    }
    public void printFlowTable(PrintWriter printWriter){
        printWriter.println("<Flow name=\"" + flowName +"\">");
        
        if ((equationsRef!=null) && (equationsRef!=""))
        
            printWriter.println("    <EquationsRef>" + equationsRef + "</EquationsRef>"); 
        
        // print start node
        startNode.printStartNodeInFlow(printWriter);

        // print test node or flow node
        
        for(BaseNode baseNode: nodes){
            for(String line: baseNode.getText()){
                printWriter.println("    "+ line);
            }
        }
       
        for(ExitNode exitNode: exitNodes){
            exitNode.printExitNode(printWriter);
        }
        
        printWriter.println("</Flow>");
    
    }    
    public List<BaseNode> getNodes() {
        return nodes;
    }

    public void addNodes(BaseNode node) {
        this.nodes.add(node);
        node.setMotherEquationsRef(equationsRef);
    }

    public List<ExitNode> getExitNodes() {
        return exitNodes;
    }

    public void addExitNodes(ExitNode exitNode) {
        this.exitNodes.add(exitNode);
    }
    
    public void addDeviceNodes(DeviceNode deviceNode){
        this.deviceNodes.add(deviceNode);
    }

    public StartNode getStartNode() {
        return startNode;
    }

    public void setStartNode(StartNode startNode) {
        this.startNode = startNode;
    }
    
    public DeviceNode getDeviceNodes() {
        if(this.deviceNodes.size()==1)
            return deviceNodes.get(0);
        else
            return null;
    }

    public String getEquationsRef() {
        return equationsRef;
    }

    public void setEquationsRef(String equationsRef) {
        this.equationsRef = equationsRef;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }
    
    public boolean getIsUsed(){
        return this.isUsed;
    }
    

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
            
    public int getNodeCnt() {
        return nodeCnt;
    }

    public void setNodeCnt(int nodeCnt) {
        this.nodeCnt = nodeCnt;
    }

    public List<String> getTextLine(){
        List<String> textLines = new ArrayList();
        return textLines;
    } 

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
    
    public boolean search(String content){
        if(this.flowName.equals(content)||((this.equationsRef!=null)&&(this.equationsRef.equals(content)))){
                return true;
        }
        else return false;
            
    }
    
}