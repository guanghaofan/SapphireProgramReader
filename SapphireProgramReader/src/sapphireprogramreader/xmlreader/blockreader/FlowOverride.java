/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader.blockreader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 *
 * @author ghfan
 */
public class FlowOverride {
    /*
    <FlowOverride name="FlowOverrideDefault">
		<Enable>TRUE</Enable>
		<AllSites>

      <!--  CPU SPEEDBIN Flow Bypass Control :
            Will Override the running of a Flow if the OverRideCPUBinX variable has been set to 1
            The OverRideCPUBinX variable will be set to 1 when the CPU Speed for the associated
            Bin has been set to 0 (see Equations_TestSetup_SPEEDBIN.xml in COMMON_PKG\Equations.xml)
      -->
      <FlowContext name="FT;Run_Flow^SPEEDBIN|SPEEDBIN^SPEEDBIN_CPU|SPEEDBIN_CPU^SPEEDBIN_CPU_BIN1|SPEEDBIN_CPU_BIN1">
        <Mode>TestAndBinning</Mode>
        <Bypass>
          <Switch>On</Switch>
          <EquationRef>SPEEDBIN_CPU_BIN1</EquationRef>
          <VariableName>OverRideCPUBin1</VariableName>
        </Bypass>
        <Override>
          <Switch>OnBypass</Switch>
          <EquationRef>SPEEDBIN_CPU_BIN1</EquationRef>
          <VariableName>NextNode</VariableName>
        </Override>
      </FlowContext>
      */
    
    private String name=null;
    private boolean enable=false;
    private String fileName=null;
    private List<FlowContext> flowContext= new ArrayList<>(); 
    
    

    public FlowOverride(Element element, String fileName) {
        this.fileName=fileName;
        this.name=element.attributeValue("name");
                
        List<Node> enableNodes =  element.selectNodes("//blocks/FlowOverride/Enable");
        if(!enableNodes.isEmpty())
            if(enableNodes.get(0).getText().equals("TRUE"))
                this.enable=true;
        
        List<Element> nodes= element.selectNodes("//blocks/FlowOverride//FlowContext");
        
        for(Element node:nodes){
            this.flowContext.add(new FlowContext(node));
        
        }
        
    }
    
    public boolean contains(String _flowContext){
        boolean notContains=true;
        for(FlowContext flowContext: this.flowContext){
            if(_flowContext.equals(flowContext.name)){
                notContains=false;
//                System.out.println("this node " + flowContext.name  + " is override");
                break;
            }
                
        }
        return(!notContains);       
    }
    public void print(){
      System.out.println("<FlowOverride name=\"  "+ this.name + "\">");
      if(this.enable)
          System.out.println("    " + "TRUE");
      else
          System.out.println("    " + "FALSE");
      
      for(FlowContext flowContext: this.flowContext){
          flowContext.print();
      }
      
      System.out.println("</FlowOverride>");
      
        
    }
   

    public boolean isEnable() {
        return enable;
    }

    public void setFlowContext(List<FlowContext> flowContext) {
        this.flowContext = flowContext;
    }

    public String getName() {
        return name;
    }

    public List<FlowContext> getFlowContext() {
        return flowContext;
    }

    public String getFileName() {
        return fileName;
    }
    

    class FlowContext{
        /*
            <FlowContext name="FT;Run_Flow^SPEEDBIN|SPEEDBIN^SPEEDBIN_CPU|SPEEDBIN_CPU^SPEEDBIN_CPU_BIN1|SPEEDBIN_CPU_BIN1">
            <Mode>TestAndBinning</Mode>
            <Bypass>
              <Switch>On</Switch>
              <EquationRef>SPEEDBIN_CPU_BIN1</EquationRef>
              <VariableName>OverRideCPUBin1</VariableName>
            </Bypass>
            <Override>
              <Switch>OnBypass</Switch>
              <EquationRef>SPEEDBIN_CPU_BIN1</EquationRef>
              <VariableName>NextNode</VariableName>
            </Override>
          </FlowContext>
        */
        private String name=null;
        private Bypass byPass;
        

        public FlowContext(Element element) {
            this.name=element.attributeValue("name");
            List<Element> nodes= element.elements();
            for(Element node:nodes){
                if(node.getName().equals("Bypass")){
                    this.byPass= new Bypass(node);
                    break;
                }
            }
            
        }
        
        public void print(){
            System.out.println("    <FlowContext name=\"" + this.name +"\">" );
            this.byPass.print();
            System.out.println("    </FlowContext>");
        }
        

        public String getName() {
            return name;
        }

        public Bypass getByPass() {
            return byPass;
        }
        class Bypass{
            private boolean switchOn=false;
            private String equationRef=null;
            private String variableName=null;

            public Bypass(Element element) {
                
                List<Element> nodes= element.elements();
                for(Element node: nodes){
                    if(node.getName().equals("Switch") && node.getText().equals("On"))
                        this.switchOn=true;
                    else if(node.getName().equals("EquationRef")){
                        this.equationRef=node.getText();
                    }
                    else if(node.getName().equals("VariableName")){
                        this.variableName=node.getText();
                    }
                    
                }   
            }
            /*
            <Bypass>
              <Switch>On</Switch>
              <EquationRef>SPEEDBIN_CPU_BIN1</EquationRef>
              <VariableName>OverRideCPUBin1</VariableName>
            </Bypass>
            */
            public void print(){
                System.out.println("    <Bypass>");
                if(this.switchOn)
                    System.out.println("        <Switch>On</Switch>");
                else
                    System.out.println("        <Switch>Off</Switch>");
                System.out.println("        <EquationRef>"+ this.equationRef + "</EquationRef>");
                System.out.println("        <VariableName>"+ this.variableName + "</VariableName>");
                System.out.println("    </Bypass>");
                                        
            }

            public void setEquationRef(String equationRef) {
                this.equationRef = equationRef;
            }

            public void setSwitchOn(boolean switchOn) {
                this.switchOn = switchOn;
            }

            public void setVariableName(String variableName) {
                this.variableName = variableName;
            }

            public String getEquationRef() {
                return equationRef;
            }

            public String getVariableName() {
                return variableName;
            }

            public boolean isSwitchOn() {
                return switchOn;
            }
        }

        
    }
   
    
}
