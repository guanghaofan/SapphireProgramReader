/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;
import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class FXML_Label_TextController extends HBox {
    @FXML private TextField textField;

    public FXML_Label_TextController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML_Label_Text.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set(value);
    }

    public StringProperty textProperty() {
        return textField.textProperty();
    }

    @FXML
    protected void doSomething() {
        System.out.println("The button was clicked!");
    }
}
