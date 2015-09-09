/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sapphireprogramreader.ui.controls;

import sapphireprogramreader.xmlreader.blockreader.Equation;
import sapphireprogramreader.xmlreader.blockreader.Equation.Group;
import sapphireprogramreader.xmlreader.blockreader.Equation.equationNode;
import sapphireprogramreader.xmlreader.XMLRead;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author ghfan
 */
public class EquationsTreeNode {
    private TreeItem equationsTreeItem;

    public EquationsTreeNode(Equation equation) {
        this.equationsTreeItem = new TreeItem();
    }
    public void buildEquationTable(Equation equation, XMLRead xmlReader, TreeItem item){
//        if (this.firstRun){
//            equationsTreeItem.setValue(new equationNodeCell(equation.getName()));
//            this.firstRun=false;
//        }
//        else{    
            item.setValue(new EquationNodeCell(equation.getName(), equation.getFileName()));
//        }
        if(!equation.isGroupIsSet()) {};
        for(Group group:equation.getGroupList()){
            if(group.getGroupType()==0){
//                TreeItem variablesItem= new TreeItem("Variables");
//                variablesItem.getChildren().add(new TreeItem(getVariableTable(group)));
//                item.getChildren().add(variablesItem);
                // change to new cell
                TreeItem variablesItem= new TreeItem("*Variables");
                variablesItem.getChildren().add(new TreeItem(new VariableLabelNodeCell()));
                for(equationNode node : group.getNodes()){
                    TreeItem variableItem= new TreeItem(new VariableNodeCell(node));
                    variablesItem.getChildren().add(variableItem);
                }
                item.getChildren().add(variablesItem);
            }
            else if(group.getGroupType()==1){
                for(equationNode node: group.getNodes()){
//                    if(node.getType()==1){
//                        TreeItem equationItem = new TreeItem();
//                        equationItem.setValue(new equationNodeCell(node.getName()));
//                        equationsTreeItem.getChildren().add(equationItem);
//                        buildEquationTable(xmlReader.equations.get(node.getName()), xmlReader);
//                        
//                    }
//                    else{
//                        System.out.println("something wrong in this node" + node.getName() +" , this is not a equationsRef");
//                    }
                    if(node.getType()==1){
                        TreeItem subItem= new TreeItem();
                        item.getChildren().add(subItem);
                        if(xmlReader.equations.get(node.getName())!=null){
                            buildEquationTable(xmlReader.equations.get(node.getName()), xmlReader,subItem);
                                 
                        }
                        else{
                            System.out.println("Equation  "+ node.getName() +"  doesn't exist");
                            
                        }
                        
                    }
                    else{
                       
                        System.out.println("something wrong in this node" + node.getName() +" , this is not a equationsRef");
                    }
                }
            
            }
        }
        
    }

    public TableView getVariableTable(Group group){
        TableView<Variable> tableView= new TableView();
        
        TableColumn firstNameCol = new TableColumn("Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Variable, String>("name"));
        
        TableColumn secondNameCol = new TableColumn("Value");
        secondNameCol.setMinWidth(100);
        secondNameCol.setCellValueFactory(new PropertyValueFactory<Variable, String>("value"));
        
        TableColumn thirdNameCol = new TableColumn("Expression");
        thirdNameCol.setMinWidth(100);
        thirdNameCol.setCellValueFactory(new PropertyValueFactory<Variable, String>("expression"));
        
        ObservableList<Variable> data = FXCollections.observableArrayList();
        for(equationNode node: group.getNodes()){
            data.add(new Variable(node));
        }
        tableView.setItems(data);
        tableView.getColumns().addAll(firstNameCol,secondNameCol,thirdNameCol);
        
        return tableView;
    }

    public TreeItem getEquationsTreeItem() {
        return equationsTreeItem;
    }

//    private final ObservableList<variables> data = FXCollections.observableArrayList(
//        new Person("Jacob", "Smith", "jacob.smith@example.com"),
//        new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
//        new Person("Ethan", "Williams", "ethan.williams@example.com"),
//        new Person("Emma", "Jones", "emma.jones@example.com"),
//        new Person("Michael", "Brown", "michael.brown@example.com")
    
    public static class Variable {
        private final SimpleStringProperty name;
        private final SimpleStringProperty value;
        private final SimpleStringProperty expression;
        
        private Variable(equationNode node) {
            this.name = new SimpleStringProperty(node.getName());
            this.value = new SimpleStringProperty(node.getValue());
            this.expression = new SimpleStringProperty(node.getValue());
//            System.out.println(name.get());
//            System.out.println(value.get());
//            System.out.println(expression.get());
        }
        
        public String getName() {
            
//            System.out.println("get name " + name.get());
            return name.get();
        }
        public void setName(String fName) {
//            System.out.println("Set name " + name.get());
            name.set(fName);
        }
        
        public String getValue() {
//            System.out.println("get value " + value.get());
            return value.get();
        }
        public void setValue(String fName) {
//            System.out.println("Set value " + value.get());
            value.set(fName);
        }
        
        public String getExpression() {
//            System.out.println("Get expression " + expression.get());
            return expression.get();
        }
        public void setExpression(String fName) {
//            System.out.println("Set expression " + expression.get());
            expression.set(fName);
        }
    }
}