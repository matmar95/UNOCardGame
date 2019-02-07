package game.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;

public class Controller {

    @FXML
    TextField userField;
    @FXML
    TextField ipField;
    @FXML
    TextField portField;
    @FXML
    public void initialize(){
    }

    public void buttonStartPress(ActionEvent event){
        Button b = (Button)event.getSource();
        String username = null;
        if (!userField.getText().isEmpty()){
            username = userField.getText().trim();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Username", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        String ip = ipField.getText().trim();
        String port = portField.getText().trim();

        System.out.println(username + " " + ip +  " " + port );

    }
}
