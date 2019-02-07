package game.controller;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;

public class Controller {

    @FXML
    AnchorPane root;

    @FXML
    public void initialize(){
        root.setStyle("-fx-background-size: 100% 100%;");
        root.setStyle("-fx-background-repeat: stretch;");



    }
}
