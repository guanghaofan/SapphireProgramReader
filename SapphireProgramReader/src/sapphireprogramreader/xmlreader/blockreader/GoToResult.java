/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.PrintWriter;

/**
 *
 * @author ghfan
 */
public class GoToResult {
    /*
     <GoTo result="PASS">    <NodeRef>CONTINUE</NodeRef><Decision>Pass</Decision></GoTo>
    */
    private String result =null;
    private String nodeRef =null;
    private String decision =null;
    
    public GoToResult(String result, String nodeRef, String decision){
        this.result=result;
        this.nodeRef=nodeRef;
        this.decision=decision;
  
    }
    public void printGoToResult(PrintWriter printWriter){
        printWriter.println("    <GoTo result=\"" + result + "\">    <NodeRef>" + nodeRef + "</NodeRef><Decision>" + decision+ "</Decision></GoTo>");
    }
    public void printGoToResultInFlow(PrintWriter printWriter){
        printWriter.println("        <GoTo result=\"" + result + "\">    <NodeRef>" + nodeRef + "</NodeRef><Decision>" + decision+ "</Decision></GoTo>");
    }
    public void printGoToResult(){
        System.out.println("      <GoTo result=\"" + result + "\">    <NodeRef>" + nodeRef + "</NodeRef><Decision>" + decision+ "</Decision></GoTo>");
    }
    public void setDecision(String decision) {
        this.decision = decision;
    }
    public String getDecision() {
        return decision;
    }
    public void setReuslt(String reuslt) {
        this.result = reuslt;
    }
    public String getNodeRef() {
        return nodeRef;
    }
    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }
    public String getReuslt() {
        return result;
    }
    public boolean search(String content){
        if(this.nodeRef!=null && this.nodeRef.equals(content))
            return true;
        else
            return false;
        
    }
}
