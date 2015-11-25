/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader.blockreader;

import java.util.List;
import org.dom4j.Element;

/**
 *
 * @author ghfan
 */
public class FlowContext{
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
        public boolean search(String context){
            if(this.name.equals(context))
                return true;
            else{
                return (this.byPass.search(context));
            }
        
        }
        
        public boolean containsSearch(String context){
            if(this.name.toLowerCase().contains(context))
                return true;
            else{
                return (this.byPass.containsSearch(context));
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
        public class Bypass{
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
            
            public boolean search(String context){
                if(this.equationRef.equals(context))
                    return true;
                else if(this.variableName.equals(context))
                    return true;
                else
                    return false;
                  
            }
            
            public boolean containsSearch(String context){
                if(this.equationRef.toLowerCase().contains(context))
                    return true;
                else if(this.variableName.toLowerCase().contains(context))
                    return true;
                else
                    return false;
                  
            }
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
