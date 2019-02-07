package game.controller;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Controller {
    @FXML
    Label Label;
    @FXML
    ImageView ImageLogo;

    @FXML
    public void initialize(){
        System.out.println("GameController initializing");
        Label.setText("Porco dio");

        File file2 = new File("image_assets/card/icon.png");
        Image image2 = new Image(file2.toURI().toString());
        ImageLogo.setImage(image2);

    }
}
