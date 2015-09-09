/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.xmlreader.blockreader;
import org.dom4j.Element;
/**
 *
 * @author ghfan
 */
public class ActionList {
    private String actionName=null;
    private String type=null;
    private String flowRef=null;

    public ActionList(Element root){
        this.actionName=root.attributeValue("name");
        this.type=root.element("Type").getText();
        
        this.flowRef=root.elementText("FlowRef");
    }

    public String getFlowRef() {
        return flowRef;
    }

    public String getType() {
        return type;
    }

    public String getActionName() {
        return actionName;
    }

    public void printAction(){ 
    }
    
}
