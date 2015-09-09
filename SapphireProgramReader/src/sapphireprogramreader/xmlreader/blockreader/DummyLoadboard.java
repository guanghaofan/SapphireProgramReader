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
 * @author Administrator
 */
public class DummyLoadboard {
//    <Loadboard name="ConnectRextHsio">
//        <PowerSupplies>
//		<PSU5V>'Enable'</PSU5V>
//		<PSU-5V>'Enable'</PSU-5V>
//		<PSU15V>'Enable'</PSU15V>
//		<PSU-15V>'Enable'</PSU-15V>
//		<PSU3.3V>'Enable'</PSU3.3V>
//	</PowerSupplies>
//	<UbitsPowerSelect>'+5V'</UbitsPowerSelect>
//	<Ubit sigref="UB_PCIE_ZVSS">	<State>'Enable'</State>	</Ubit>	<!--K1;  UB1-->
//	<Ubit sigref="UB_SATA_ZVSS">	<State>'Enable'</State>	</Ubit>	<!--K2;  UB2-->
//	<Ubit sigref="UB_10GKR_ZVSS">	<State>'Enable'</State>	</Ubit>	<!--K3;  UB3-->
//	<Ubit sigref="UB_SEAM_ZVSS">	<State>'Enable'</State>	</Ubit>	<!--K4;  UB4-->
//	<Ubit sigref="UB_MEMZVSS0">		<State>'Enable'</State>	</Ubit>	<!--K5;  UB5-->
//	<Ubit sigref="UB_MEMZVSS1">		<State>'Enable'</State>	</Ubit>	<!--K6;  UB6-->
//    </Loadboard>
    
    private String name=null;
    final private List<PowerSupply> powerSupplise = new ArrayList<>();
    private String uBitsPowerSelect=null;
    final private List<UBit> uBits= new ArrayList<>();
    private String fileName=null;
    

    public DummyLoadboard(String name, String fielName) {
        this.name=name;
        this.fileName=fileName;
    }
    
    public DummyLoadboard(Element element, String fileName){
        
        if(element!=null){
            this.name= element.attributeValue("name");
            this.fileName=fileName;
            List<Element> nodes= element.elements();
            for(Element node:nodes){
                if(node.getName().equals("Ubit")){
                    this.uBits.add(new UBit(node));
                }
                else if(node.getName().equals("PowerSupplies")){
                    List<Element> powers= node.elements();
                    for(Element power: powers){
                        this.powerSupplise.add(new PowerSupply(power));
                    }  
                }
                else if(node.getName().equals("UbitsPowerSelect")){
                    this.uBitsPowerSelect=node.getText();
                }
                else{
                    System.out.println("UnSupported LoadBoard Type "+ node.getName());
                }
            }
        }
    }
    public void setUbitsPowerSelect(String power){
        this.uBitsPowerSelect=power;
    }
    public void setPowerSupplise(Element element) {
        if(element!=null){
            List<Element> nodes= element.elements();
            for(Element node: nodes){
                this.powerSupplise.add(new PowerSupply(node));  
            }
        }
       
    }
    public void addUBit(Element element){
        this.uBits.add(new UBit(element));
    }

    public void print(){
        //<Loadboard name="ConnectRextHsio">
        System.out.println("<Loadboard name=\"" + this.name + "\">");
        if(!powerSupplise.isEmpty()){
            //<PowerSupplies>
            System.out.println("    <PowerSupplies>");
            
            for(PowerSupply powerSupply: this.powerSupplise){
                powerSupply.print();
            }
            System.out.println("    </PowerSupplies>");    
        }
        
        if(this.uBitsPowerSelect!=null){
            //<UbitsPowerSelect>'+5V'</UbitsPowerSelect>
            System.out.println("    <UbitsPowerSelect>" + this.uBitsPowerSelect + "</UbitsPowerSelect>");        
        }
        
        if(!this.uBits.isEmpty()){
            for(UBit uBit: this.uBits){
                uBit.print();
            }
        }
        System.out.println("</Loadboard>");
        
    }
    public String getuBitsPowerSelect() {
        return uBitsPowerSelect;
    }
    

    public String getFileName() {
        return fileName;
    }
    public List<UBit> getuBits() {
        return uBits;
    }

    public List<PowerSupply> getPowerSupplise() {
        return powerSupplise;
    }
    public String getName() {
        return name;
    }
    
    
    public class UBit{
        private String sigRef=null;
        private String state=null;

        public UBit(Element element) {
            if (element!=null){
                this.sigRef=element.attributeValue("sigref");
                this.state=element.elementText("State");
            } 
        }
        public void print(){
            //<Ubit sigref="UB_MEMZVSS1">		<State>'Enable'</State>	</Ubit>	<!--K6;  UB6-->
            System.out.println(toString());
        }
        public void print(PrintWriter printWriter){
            //<Ubit sigref="UB_MEMZVSS1">		<State>'Enable'</State>	</Ubit>	<!--K6;  UB6-->
            printWriter.println(toString());
        }
        @Override
        public String toString() {
            return ("    <Ubit sigref=\"" + this.sigRef + "\">" + "<State>" + this.state +"</State>	</Ubit>");
        }
        public String getState() {
            return state;
        }
        public String getSigRef() {
            return sigRef;
        }
    }
    
    public class PowerSupply{
        private String power=null;
        private String mode=null;

        public PowerSupply(Element element) {
            if(element!=null){
                this.power=element.getName();
                this.mode=element.getText();
            }
        }
        public void print(){
        //<PSU5V>'Enable'</PSU5V>
            System.out.println(toString());
            
        }
        public void print(PrintWriter printWriter){
        //<PSU5V>'Enable'</PSU5V>
            printWriter.println(toString());
            
        }
        @Override
        public String toString() {
            return ("        <" + this.power +">" + this.mode + "</" + this.power + ">");
        }

        public String getMode() {
            return mode;
        }

        public String getPower() {
            return power;
        }   
    }
    
    public boolean search(String content){
        if (this.name.equals(content))
            return true;
        else return false;
    }
}
