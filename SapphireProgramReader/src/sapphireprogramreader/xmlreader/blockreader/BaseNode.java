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
public class BaseNode {
    /*
      <Node name="LeakageV33_High">
      <Type>Test</Type>
      <SyncPoint>On</SyncPoint>
      <EquationsRef>SetupLeakage</EquationsRef>
      <StrategyRef>BySite</StrategyRef>
      <TestRef>LeakageV33_High</TestRef>
      <GoTo result="PASS">    <NodeRef>LeakageV33_Low</NodeRef><Decision>Pass</Decision></GoTo>
      <GoTo result="FAIL">    <NodeRef>LeakageV33_Low</NodeRef><Decision>Fail</Decision></GoTo>
      <GoTo result="OVERRIDE"><NodeRef>LeakageV33_Low</NodeRef><Decision>Fail</Decision></GoTo>
      </Node>
    */
    
     /*
    <Node name="PSUPPLYSHORTS">
       <Type>Flow</Type>
       <SyncPoint>On</SyncPoint>
       <EquationsRef>Equations_TestSetups_Func_PMax_Hi</EquationsRef>
       <FlowRef>PSUPPLYSHORTS</FlowRef>
       <GoTo result="CONTINUE"><NodeRef>PINCONUT</NodeRef><Decision>Fail</Decision></GoTo>
       <GoTo result="OVERRIDE"><NodeRef>PINCTINUITY_ALL</NodeRef><Decision>Pass</Decision></GoTo>
       <GoTo result="BINOUT">  <NodeRef>BINOONTINUITY_ALL</NodeRef><Decision>Otherwise</Decision></GoTo>
    </Node>
    */
    
    private String nodeName=null;
    private String nodeType=null;
    private String syncPoint=null;
    private String equationsRef=null;
    private String strategyRef=null;
    private String tfRef=null;
    private String binningRef=null;
    private String xmlFileName=null;
    
    private String motherEquationsRef=null;
    private String flowName=null;
    
    private int flowNo=0;
    private List<String> textLine= new ArrayList();
    private int nodeNo =1;
    private int baseNodeNo=0;
    private List<GoToResult> gotoResult=new ArrayList<>();
    
    private boolean testIsReady=false;
    
    public BaseNode(){}
    public BaseNode(String nodeName, String nodeType, String syncPoint,String equationsRef, String strategyRef, String testRef, String binningRef, List<GoToResult> result, String flowName, String xmlFileName, int nodeNumber, int flowNo, int baseNodeNum){
        this.nodeName=nodeName;
        this.nodeType=nodeType;
        this.syncPoint=syncPoint;
        this.equationsRef=equationsRef;
        this.strategyRef=strategyRef;
        this.tfRef=testRef;
        this.binningRef=binningRef;
        this.nodeNo=nodeNumber;
        this.gotoResult=result;
        this.flowName=flowName;
        this.xmlFileName=xmlFileName;
        this.flowNo=flowNo;
        this.baseNodeNo=baseNodeNum;
//        getLine();
    }
    public void printTestNode(){
        System.out.println("  <Node name=\"" + nodeName + "\">");
        System.out.println("      <Type>Test</Type>");
        if (syncPoint!=null) System.out.println("      <SyncPoint>" + syncPoint + "</SyncPoint>");
        if (equationsRef!=null) System.out.println("      <EquationsRef>" + equationsRef+ "</EquationsRef>");
        if (strategyRef!=null) System.out.println("      <StrategyRef>" + strategyRef+"</StrategyRef>");
        
        System.out.println("      <TestRef>"+ tfRef + "</TestRef>");
        if(this.binningRef!=null) System.out.println("      <BinningRef>" + this.binningRef+"</BinningRef>");
        for(GoToResult result:gotoResult){
            result.printGoToResult();
        }
        System.out.println("  </Node>");
    }
    public void printFlowNode(){
        System.out.println("  <Node name=\""+ nodeName + "\">");
        System.out.println("      " + "<Type>Flow</Type>");
        if (syncPoint!= null) System.out.println("      " + "<SyncPoint>" + syncPoint+ "</SyncPoint>");
        if (equationsRef!=null) System.out.println("      "+ "<EquationsRef>" + equationsRef + "</EquationsRef>");
        System.out.println("      "+ "<FlowRef>" + tfRef +"</FlowRef>");
        if(this.binningRef!=null) System.out.println("      <BinningRef>" + this.binningRef+"</BinningRef>");
        for(GoToResult result:gotoResult ){
            result.printGoToResult();
        }
        System.out.println("  </Node>");    
    }
    public void printTestNode(PrintWriter printWriter){
        printWriter.println("<Node name=\"" + nodeName + "\">");
        if (this.nodeType.equals("Flow"))
            printWriter.println("    <Type>Flow</Type>");
        else
            printWriter.println("    <Type>Test</Type>");
        if (syncPoint!=null) printWriter.println("    <SyncPoint>" + syncPoint + "</SyncPoint>");
        if (equationsRef!=null) printWriter.println("    <EquationsRef>" + equationsRef+ "</EquationsRef>");
        if (strategyRef!=null) printWriter.println("    <StrategyRef>" + strategyRef+"</StrategyRef>");
        if (this.nodeType.equals("Flow"))
            printWriter.println("    <FlowRef>"+ tfRef + "</FlowRef>");
        else
            printWriter.println("    <TestRef>"+ tfRef + "</TestRef>");
        if(this.binningRef!=null) printWriter.println("    <BinningRef>" + this.binningRef+"</BinningRef>");
        for(GoToResult result:gotoResult){
            result.printGoToResult(printWriter);
        }
        printWriter.println("</Node>");
    }
//    
    private void getLine(){
        textLine.add("<Node name=\""+ nodeName + "\">");
        if (this.nodeType.equalsIgnoreCase("flow")) textLine.add("    " + "<Type>Flow</Type>");
        else 
            textLine.add("    " + "<Type>Test</Type>");

        if (syncPoint!= null) textLine.add("    " + "<SyncPoint>" + syncPoint+ "</SyncPoint>");
        if (equationsRef!=null) textLine.add("    "+ "<EquationsRef>" + equationsRef + "</EquationsRef>");
        if (strategyRef!=null) textLine.add("    <StrategyRef>" + strategyRef+"</StrategyRef>");
         
        if (this.nodeType.equalsIgnoreCase("flow")) textLine.add("    "+ "<FlowRef>" + tfRef +"</FlowRef>");
        else
            textLine.add("    "+ "<TestRef>" + tfRef +"</TestRef>");
        if(this.binningRef!=null)
            textLine.add("    <BinningRef>" + this.binningRef+"</BinningRef>");
        for(GoToResult result:gotoResult ){
            textLine.add("    <GoTo result=\"" + result.getReuslt() + "\">    <NodeRef>" + result.getNodeRef() + "</NodeRef><Decision>" + result.getReuslt()+ "</Decision></GoTo>");
        }
        textLine.add("</Node>");   
    }
    public List<String> getText(){
        return this.textLine;
    }
    private void setName(String name){
        this.nodeName=name;
    }
    
    

    public int getBaseNodeNo() {
        return baseNodeNo;
    }
    public String getBinningRef() {
        return binningRef;
    }
    public String getName(){
        return this.nodeName;
    }
    private void setNodeType(String type){
        this.nodeName=type;
    }
    public String getNodeType(){
        return this.nodeType;
    }
    private void setsynsPoint(String syncPoint){
        this.syncPoint=syncPoint;
    }
    private String getSyncPoint(){
        return this.syncPoint;
    }
    private void setEquationsRef(String ref){
        this.equationsRef=ref;
    }
    public String getEquationsRef(){
        if(this.motherEquationsRef==null) return this.equationsRef;
        else{
            if (this.equationsRef!=null)
                return (this.motherEquationsRef+','+ this.equationsRef);  // motherEquationsRef is just the equationRef inside this flow        
            else 
                return this.motherEquationsRef;
        }
    }
    private void setStrategyRef(String ref){
        this.strategyRef=ref;
    }
    private String getStrategyRef(){
        return this.strategyRef;
    } 
    private void setTestFlowRef(String ref){
        this.tfRef=ref;
    }
    public String getTestFlowRef(){
        return this.tfRef;
    }
    
    private void setFlowName(String name){
        this.flowName=name;
    }
    public String getFlowName(){
        return this.flowName;
    }
    

    public void setTestIsReady(boolean testIsReady) {
        this.testIsReady = testIsReady;
    }

    public boolean isTestIsReady() {
        return testIsReady;
    }

    public void setMotherEquationsRef(String ref){
        this.motherEquationsRef=ref;
    }
    public String getMotherEquationsRef(){
        return this.motherEquationsRef;
    }
     
    public void setNodeNo(int number){
        this.nodeNo=number;
    }
    public int getNodeNo(){
        return this.nodeNo;
    }
    
    public void addGoToResult(GoToResult result){
        this.gotoResult.add(result);
    }
    public List<GoToResult> getGoToResult(){
        return this.gotoResult;
    }

    public void setFlowNo(int flowNo) {
        this.flowNo = flowNo;
    }
 
    public int getFlowNo() {
        return flowNo;
    }

    public String getXmlFileName() {
        return xmlFileName;
    }
    
    public boolean search(String content){
        if(this.nodeName.equals(content)||(this.tfRef.equals(content))||(this.equationsRef!=null&&this.equationsRef.equals(content))||(this.binningRef!=null&& this.binningRef.equals(content)))
            return true;
        else{
            boolean isFound=false;
            for(GoToResult result: this.gotoResult){
                if(result.search(content)){
                    isFound=true;
                    break;
                }
            }
            return isFound;
        }
        
    }
   
}
