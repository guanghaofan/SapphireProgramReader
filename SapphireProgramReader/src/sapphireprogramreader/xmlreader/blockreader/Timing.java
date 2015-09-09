/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader.blockreader;

import sapphireprogramreader.ui.controls.NodeCell_3Text;
import sapphireprogramreader.ui.controls.TestNodeCell_Label;
import sapphireprogramreader.ui.controls.TestNodeCell_Label_Text;
import sapphireprogramreader.ui.controls.VariableLabelNodeCell;
//import Util.Equation.equationNode;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TreeItem;
import org.dom4j.Element;
import sapphireprogramreader.xmlreader.XMLRead;
import sapphireprogramreader.xmlreader.blockreader.Equation.equationNode;

/**
 *
 * @author Administrator
 */
public class Timing {
    
//    <Timing name="Timing_Standard">
//	<TimingDomain name="Main">
//		<Period name="Period1">		<Value>RefClkPeriod</Value></Period>
//
//		<SignalRef name="AllClkinH">
//			<Waveform name="dr2F"><Time>Clkin_drF1</Time>		<Event>D/U[0]</Event>	<Time>Clkin_drF2</Time>		<Event>D</Event></Waveform>
//			<Waveform name="dr2Z"><Time>Clkin_drF1</Time>		<Event>Z</Event>		<Time>Clkin_drF2</Time>		<Event>NOP</Event></Waveform>
//		</SignalRef>
    
    private String name=null;
    private List<TimingDomain> timingDomain= new ArrayList<>();
    private String fileName=null;
    private List<TreeItem> domainRoot= new ArrayList<>();
    private boolean isReady=false;
    
    
    

    public Timing(Element element, String fileName) {
        this.fileName=fileName;
        this.name=element.attributeValue("name");
        List<Element> nodes= element.elements();
        if(!nodes.isEmpty()){
            for(Element node: nodes){
                switch (node.getName()) {
                    case "TimingDomain":
                        this.timingDomain.add(new TimingDomain(node));
                        break;
                    default:
                        System.out.println("UnSupported Timing Type " + node.getName());
                }
            }
        }
    }
    
    public void buildTree(TreeItem root){
        for(TimingDomain domain: this.timingDomain){
             TreeItem domainRoot= new TreeItem(new TestNodeCell_Label_Text("TimingDomain", domain.getName()));
             this.domainRoot.add(domainRoot);
             
             root.getChildren().add(domainRoot);
             domain.buildTree(domainRoot);
        }
        isReady=true;
    }

    public void getRootItem(TreeItem root) {
        if(isReady){
            int i=0;
            for(TimingDomain domain: this.timingDomain){
                setExpanded(this.domainRoot.get(i));
                root.getChildren().add(this.domainRoot.get(i));
                i++;
                System.out.println("Re-add timing");
            }
        }
        else{
            buildTree(root);
            System.out.println("Build timing");
            
        }
        
    }
    public void setExpanded(TreeItem treeItem){
        if(treeItem.isExpanded()) treeItem.setExpanded(false);
//        System.out.println(treeItem.getValue().toString() + " is set un expanded");
        for(int i=0; i!= treeItem.getChildren().size();i++){
            TreeItem item= (TreeItem) treeItem.getChildren().get(i);
//            if(item.isExpanded()){ 
//                item.setExpanded(false);
//                System.out.println(item.getValue().toString() + " is set un expanded");
//            }
            setExpanded(item);
        }
    }
    public List<TimingDomain> getTimingDomain() {
        return timingDomain;
    }
    
    public void print(){
        System.out.println("<Timing name=\""+ this.name + "\">");
        if(!this.timingDomain.isEmpty()){
            for(TimingDomain domain: this.timingDomain){
                domain.print();
            }
        }
        System.out.println("</Timing>");
    }
    public void print(PrintWriter printWriter){
        printWriter.println("<Timing name=\""+ this.name + "\">");
        if(!this.timingDomain.isEmpty()){
            for(TimingDomain domain: this.timingDomain){
                domain.print(printWriter);
            }
        }
        printWriter.println("</Timing>");
    }
    public boolean search(String content){
        System.out.println("Start to search Timing in " + this.name);
        boolean isFound=false;
        if (this.name.equals(content))
            return true;
        else{
            for(TimingDomain domain: this.timingDomain){
                if(domain!=null&&domain.search(content)){
                    isFound=true;
                    break;
                }
            }
            return isFound;
        }
    }
    public String getName() {
        return name;
    }
        

    public String getFileName() {
        return fileName;
    }
    
    public String printVariable(String name, String value){
//            <OverCurrentDelay>VddCrCpu_OverCurrDly</OverCurrentDelay>
                return ("<"+ name + ">" + value + "</" + name + ">");
            }
    
    public void updateTiming(){
        for(TimingDomain domain:this.timingDomain){
            domain.update();
        }
        
    
    }
    public class SignalRef{
        private String name=null;
        private final List<WaveForm> waveForms = new ArrayList<>(); 
        private String strobeMode=null;
        private String signalDelay=null;

        public SignalRef(Element element) {
            if(element!=null){
                this.name=element.attributeValue("name").trim();
                List<Element> nodes = element.elements();
                if(!nodes.isEmpty()){
                    for(Element node:nodes){
                        if(node.getName().equals("Waveform"))
                            this.waveForms.add(new WaveForm(node));
                        else if(node.getName().equals("SignalDelay")){
                            this.strobeMode=node.getText().trim();
                        }
                        else if(node.getName().equals("StrobeMode")){
                            this.signalDelay=node.getText().trim();
                        }
                        else{
                            System.out.println("UnSuppoprted type " + node.getName()+ " in Timing SigRef " + this.name);
                        }
                    }
                }
            }
        }
        
        public void udpate(){
            for(WaveForm waveForm: this.waveForms){
                waveForm.update();
            }
        
        }
        

        public String getStrobeMode() {
            return strobeMode;
        }

        public String getSignalDelay() {
            return signalDelay;
        }
        public String getName() {
            return name;
        }

        public List<WaveForm> getWaveForms() {
            return waveForms;
        }
        public void print(){
            System.out.println("<SignalRef name=\""+ this.name+ "\">");
            //<StrobeMode>Window</StrobeMode>
            if(this.strobeMode!=null)
                System.out.println("<StrobeMode>"+ this.strobeMode+ "</StrobeMode>");
            if(this.signalDelay!=null)
                System.out.println("<SignalDelay>"+ this.signalDelay+ "</SignalDelay>");
            for(WaveForm waveform: this.waveForms){
                waveform.print();
            }
            System.out.println("</SignalRef>");
        }
        public void print(PrintWriter printWriter){
            printWriter.println("        <SignalRef name=\""+ this.name+ "\">");
            if(this.strobeMode!=null)
                printWriter.println("<StrobeMode>"+ this.strobeMode+ "</StrobeMode>");
            if(this.signalDelay!=null)
                printWriter.println("<SignalDelay>"+ this.signalDelay+ "</SignalDelay>");
            for(WaveForm waveform: this.waveForms){
                waveform.print(printWriter);
            }
            printWriter.println("        </SignalRef>");
        }
        public boolean search(String content){
            System.out.println("Start to search in SignalRef "+ this.name);
            boolean isFound=false;
            if(this.name.equals(content)||(this.strobeMode!=null && this.strobeMode.equals(content))||(this.signalDelay!=null && this.signalDelay.equals(content))){
                return true;
            }
            else{
                for(WaveForm waveForm: this.waveForms){
                    if (waveForm!=null && waveForm.search(content)){
                        isFound=true;
                        break;
                    }
                }
                return isFound;
            }
            
        }
        
//        <SignalRef name="AllClkinL">
//                <Waveform name="dr2F"><Time>Clkin_drF1</Time>		<Event>D/U[0]</Event>	<Time>Clkin_drF2</Time>		<Event>U</Event></Waveform>
//                <Waveform name="dr2Z"><Time>Clkin_drF1</Time>		<Event>Z</Event>		<Time>Clkin_drF2</Time>		<Event>NOP</Event></Waveform>
//        </SignalRef>
        
            
    }
    public class WaveForm {
            private String name=null;
            private String time1=null;
            private String value1=null;
            private String event1=null;
            private String time2=null;
            private String value2=null;
            private String event2=null;
            private String time3=null;
            private String event3=null;
            private String value3=null;
             private String time4=null;
            private String event4=null;
            private String value4=null;
            private final List<NodeCell_3Text> nodeCell= new ArrayList<>();

            public WaveForm(Element element) {
                if(element!=null){
                    this.name=element.attributeValue("name");
                    List<Element> nodes= element.elements();
                    if(!nodes.isEmpty()){
                        for(Element node: nodes){
                            switch( node.getName()){
                                case "Time":
                                    if(this.time1==null) this.time1= node.getText().trim();
                                    else if(this.time2==null) this.time2=node.getText().trim();
                                    else if(this.time3==null) this.time3=node.getText().trim();
                                    else if(this.time4==null) this.time4=node.getText().trim();
                                    break;
                                case "Event":
                                    if(this.event1==null) this.event1= node.getText().trim();
                                    else if(this.event2==null) this.event2=node.getText().trim();
                                    else if(this.event3==null) this.event3=node.getText().trim();
                                    else if(this.event4==null) this.event4=node.getText().trim();
                                    break;           
                            }
                        }
                    }
                }  
            }
            
            public void update(){
                for(NodeCell_3Text _nodeCell: this.nodeCell){
                    
//                    if(_nodeCell!=null)
//                        System.out.println("Empty nodecell");
                    if(XMLRead.evaluationOn){
                        equationNode eqnNode= XMLRead.variables.get(_nodeCell.getTextField3());
                        if(eqnNode!=null){
                            _nodeCell.update(eqnNode.getValue());
                        }
                    }
                    
                }
                
            }
            
            public void buildWaveformTree(TreeItem root){
                if(this.time1!=null||this.event1!=null){
                    NodeCell_3Text node= new NodeCell_3Text(this.time1, this.time1, this.event1);
                    this.nodeCell.add(node);
                    
                    root.getChildren().add(new TreeItem(node));
//                    
//                    if(node==null)
//                        System.out.println("build waveform 888888888888");
                    
                    
                    
                    
//                     root.getChildren().add( new NodeCell_3Text(this.time1, this.time1, this.event1));
//                 
                }
                if(this.time2!=null||this.event2!=null){
                    NodeCell_3Text node= new NodeCell_3Text(this.time2, this.time2, this.event2);
                    this.nodeCell.add(node);
                    root.getChildren().add(new TreeItem(node));
                    
//                    root.getChildren().add( new NodeCell_3Text(this.time2, this.time2, this.event2));
                }
                    
                if(this.time3!=null||this.event3!=null){
                    NodeCell_3Text node= new NodeCell_3Text(this.time3, this.time3, this.event3);
                    this.nodeCell.add(node);
                    root.getChildren().add(new TreeItem(node));
                    
//                    root.getChildren().add( new NodeCell_3Text(this.time3, this.time3, this.event3));
                }
                    
                if(this.time4!=null||this.event4!=null){
                    NodeCell_3Text node= new NodeCell_3Text(this.time4, this.time4, this.event4);
                    this.nodeCell.add(node);
                    root.getChildren().add(new TreeItem(node));
//                    
                    root.getChildren().add( new NodeCell_3Text(this.time4, this.time4, this.event4));
                }
                   
                
            }

            public String getValue1() {
                return value1;
            }

            public String getValue2() {
                return value2;
            }

            public void setValue3(String value3) {
                this.value3 = value3;
            }

            public void setValue2(String value2) {
                this.value2 = value2;
            }

            public void setValue1(String value1) {
                this.value1 = value1;
            }

            public String getValue3() {
                return value3;
            }
            public String getTime3() {
                return time3;
            }

            public String getTime2() {
                return time2;
            }

            public String getTime1() {
                return time1;
            }

            public String getEvent3() {
                return event3;
            }

            public String getEvent2() {
                return event2;
            }

            public String getEvent1() {
                return event1;
            }
            

        public String getValue4() {
            return value4;
        }

        public String getTime4() {
            return time4;
        }

        public String getEvent4() {
            return event4;
        }

            public String getName() {
                return name;
            }
            public void print(){
                System.out.println(toString());
            }
            public void print(PrintWriter printWriter){
                printWriter.println(toString());
            }
            public boolean search(String content){
                System.out.println("Start to search in WaveForm "+ this.name);
                if (this.name!=null&&this.name.equals(content))
                    return true;
                else if((this.time1!=null && this.time1.equals(content))|| this.event1!=null&& this.event1.equals(content))
                    return true;
                else if((this.time2!=null && this.time2.equals(content))|| this.event2!=null&& this.event2.equals(content))
                    return true;
                else if((this.time3!=null && this.time3.equals(content))|| this.event3!=null&& this.event3.equals(content))
                    return true;
                else if((this.time4!=null && this.time4.equals(content))|| this.event4!=null&& this.event4.equals(content))
                    return true;
                else
                    return false;     
                
            }
           

            @Override
            public String toString() {
                //<Waveform name="dr2Z"><Time>Clkin_drF1</Time>		<Event>Z</Event>		<Time>Clkin_drF2</Time>		<Event>NOP</Event></Waveform>
                String waveForm="<            Waveform name=\"" + this.name+ "\">" ;
                
                if(this.time1!=null) waveForm+= printVariable("Time" ,this.time1);
                if(this.event1!=null) waveForm+= printVariable("Event" ,this.event1);
                if(this.time2!=null) waveForm+= printVariable("Time" ,this.time2);
                if(this.event2!=null) waveForm+= printVariable("Event" ,this.event2);
                if(this.time3!=null) waveForm+= printVariable("Time" ,this.time3);
                if(this.event3!=null) waveForm+= printVariable("Event" ,this.event3);
                if(this.time4!=null) waveForm+= printVariable("Time" ,this.time4);
                if(this.event4!=null) waveForm+= printVariable("Event" ,this.event4);
                waveForm+="</Waveform>";
                return waveForm;
            }
        }       
    
    public class Period{
        private String name=null;
        private String value=null;
        private String expression =null;
        TreeItem treeItem= new TreeItem();

        public Period(Element element) {
            this.name=element.attributeValue("name").trim();
            this.expression=element.elementText("Value").trim();
            this.value=element.elementText("Value").trim();
        }
        public void print(){
            System.out.println(toString());
        }
        public void print(PrintWriter printWriter){
            printWriter.println(toString());
        }
        public boolean search(String content){
            System.out.println("Start to search in Period "+ this.name);
            if(this.name.equals(content)||this.expression.equals(content))
                return true;
            else
                return false;
        }
        public TreeItem getTreeItem(){
          
            this.treeItem = new TreeItem(new TestNodeCell_Label_Text(this.getExpression(),this.getValue()));
           
            return this.treeItem;
        }
        public void udpate(){
            if (this.treeItem!=null){
                TestNodeCell_Label_Text  nodeCell =(TestNodeCell_Label_Text) this.treeItem.getValue();
                if(nodeCell!=null){
                    System.out.println("updating Period");
                    
//                    if(XMLRead.evaluationOn){
                        equationNode eqnNode= XMLRead.variables.get(this.expression);
                        if(eqnNode!=null){
                            nodeCell.update(eqnNode.getValue());
                        }
//                    }
                    
                }
//                else{
//                    System.out.println(" NodeCell == Null");
//                }
            }
//            else{
//                System.out.println(" Period == Null");
//            }
        }
        
        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
        public void setValue(String value) {
            this.value = value;
        }

        String getExpression() {
            return expression;
        }

        @Override
        public String toString() {
            return ("        <Period name=\""+ this.name + "\"> <Value>"+ this.expression+ "</Value></Period>");
        }   
    }
    
    public class TimingDomain{
        private String name=null;
        private List<Period> periods= new ArrayList<>();
//        private List<TreeItem> periodRoots = new ArrayList<>();
        private List<SignalRef> signalRefs= new ArrayList<>();

        public TimingDomain(Element element) {
            this.name=element.attributeValue("name").trim();
            List<Element> nodes= element.elements();
            
            for(Element node: nodes){
                switch (node.getName()) {
                    case "Period":
                        this.periods.add(new Period(node));
                        break;
                    case "SignalRef":
                        this.signalRefs.add(new SignalRef(node));
                        break;
                    default:
                        System.out.println("Unsupported Type Timing " + node.getName());
                }
            }
        }
        
        public void buildTree(TreeItem root){
            for(Period period: this.periods){
                TreeItem periodRoot;
                periodRoot= new TreeItem(new TestNodeCell_Label_Text("Period",period.getName()));
//                periodRoots.add(periodRoot);
                
                root.getChildren().add(periodRoot);
//                periodRoot.getChildren().add(new TreeItem(new TestNodeCell_Label_Text(period.getValue(),period.getExpression())));
                periodRoot.getChildren().add(period.getTreeItem());
            }
            
            for(SignalRef signal: this.signalRefs){
                TreeItem signalRoot= new TreeItem(new TestNodeCell_Label(signal.name));
                if(signal.strobeMode!=null)
                    signalRoot.getChildren().add(new TreeItem(new TestNodeCell_Label_Text("StrobeMode",signal.strobeMode)));
                if(signal.signalDelay!=null)
                    signalRoot.getChildren().add(new TreeItem(new TestNodeCell_Label_Text("SignalDelay",signal.signalDelay)));
                
                for(WaveForm waveform: signal.waveForms){
                    TreeItem waveformRoot= new TreeItem(new TestNodeCell_Label_Text("Waveform", waveform.getName()));
                    waveformRoot.getChildren().add(new TreeItem(new VariableLabelNodeCell("Time","Value","Event")));
                    if(waveform.time1!=null) waveform.buildWaveformTree(waveformRoot);
                    signalRoot.getChildren().add(waveformRoot);
                }
                root.getChildren().add(signalRoot);
                
            }
        
        }
        
        public void print(){
            if(this.name!=null)
                System.out.println("<TimingDomain name=\"" + this.name+ "\">");
            if(! this.periods.isEmpty()){
                for(Period period: this.periods){
                    period.print();
                }
            }
            if(!this.signalRefs.isEmpty()){
                for(SignalRef sig: this.signalRefs){
                    sig.print();
                }
            }
        }
        public void print(PrintWriter printWriter){
            if(this.name!=null)
                printWriter.println("    <TimingDomain name=\"" + this.name+ "\">");
            if(! this.periods.isEmpty()){
                for(Period period: this.periods){
                    period.print(printWriter);
                }
            }
            if(!this.signalRefs.isEmpty()){
                for(SignalRef sig: this.signalRefs){
                    sig.print(printWriter);
                }
            }
        }
        public boolean search(String content){
            System.out.println("Start to search in Timing Domain "+ this.name);
            boolean isFound=false;
            if(this.name.equals(content))
                return true;
            else{
                for(Period period: this.periods){
                    if(period!=null&&period.search(content)){
                        isFound=true;
                        break;
                    }
                }
                if(isFound)
                    return true;
                else{
                    for(SignalRef sig :this.signalRefs){
                        if(sig!=null&& sig.search(content)){
                            isFound=true;
                            break;
                        }
                    }
                    return isFound;
                }
            } 
        }
        
        public void update(){
            
            for(Period _period:this.periods){
                System.out.println("start to update period");
                _period.udpate();
            }
            for(SignalRef signal: this.signalRefs){
                signal.udpate();
            }
        }
        
        public String getName() {
            return name;
        }

        public List<Period> getPeriods() {
            return periods;
        }

        public List<SignalRef> getSignalRefs() {
            return signalRefs;
        }  
    }
}
