package game.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;


public class AvatarController {

    @FXML
    Label nameAvatar;
    @FXML
    Label cards;
    @FXML
    Circle imgAvatar;

    @FXML
    public void initialize(){

    }

    public void setNameAvatar(String nameAvatar){
        this.nameAvatar.setText(nameAvatar);
    }

    public void setImgAvatar(String avatarImgPath){
        imgAvatar.setFill(new ImagePattern(new Image("/image_assets/avatars/" + avatarImgPath + ".png")));
        //imgAvatar.setEffect(new DropShadow(+30, 0d, +2d, Color.DARKSEAGREEN));

    }

    public void setCards(int numcards){
        this.cards.setText(Integer.toString(numcards));
    }
}
