/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader.blockreader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ghfan
 */
public class MyNode {
    
    String nodeName=null;
    String nodeType=null;
    String equatioonsRef=null;
    String flowortestRef=null;
    String flowContext=null;
    String motherFlowContext=null;
    String motherEquationsRef=null;
    
    int nodeNumber =1;
    public List<MyNode> nodeList= new ArrayList<>();
    public MyNode(String nodeName, String nodeType, String equationsRef, String flowortestRef, int nodeNumber){
        this.nodeName=nodeName;
        this.nodeType=nodeType;
        this.equatioonsRef=equationsRef;
        this.flowortestRef=flowortestRef;
        this.nodeNumber=nodeNumber;
    }
    public void setName(String name){
        this.nodeName=name;
    }
    public String getName(){
        return this.nodeName;
    }
    public void setNodeType(String type){
        this.nodeName=type;
    }
    public String getNodeType(){
        return this.nodeType;
    }
    public void setEquationsRef(String ref){
        this.equatioonsRef=ref;
    }
    public String getEquationsRef(){
        return this.equatioonsRef;
    }
    public void setFlowOrTestRef(String ref){
        this.flowortestRef=ref;
    }
    public String getFlowOrTestRef(){
        return this.flowortestRef;
    }
    public void setFlowContext(String flowContext){
        this.flowContext=flowContext;
    }
    private String getFlowContext(){
        return this.flowContext;
    }
    private String getMotherFlowContext(){
        return this.motherFlowContext;
    }
    
    
    private void setNodeNumber(int number){
        this.nodeNumber=number;
    }
    private int getNodeNumber(){
        return this.nodeNumber;
    }
    private MyNode addSubNode(String nodeName, String nodeType, String equationsRef, String flowortestRef, int nodeNumber){
        MyNode subNode = new MyNode(nodeName,nodeType, equationsRef,flowortestRef,nodeNumber);
        this.nodeList.add(subNode);
        subNode.motherFlowContext=this.flowContext;
        subNode.motherEquationsRef=this.equatioonsRef;
        
        subNode.equatioonsRef="";
        return subNode;
    }
    public List getSubNodes(){
        return this.nodeList;
    }
    
    
}
