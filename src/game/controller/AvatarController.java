package game.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


public class AvatarController {

    @FXML
    Label nameAvatar;
    @FXML
    Label cards;
    @FXML
    Rectangle imgAvatar;
    @FXML
    Rectangle imgCard;
    @FXML
    VBox vBoxAvatar;

    private final BackgroundFill focusBackground = new BackgroundFill(Color.rgb(0,0,0,0.6), new CornerRadii(5),new Insets(0));

    @FXML
    public void initialize(){

    }

    public void setNameAvatar(String nameAvatar){
        this.nameAvatar.setText(nameAvatar);
    }

    public void setImgAvatar(String avatarImgPath, boolean round){
        imgAvatar.setFill(new ImagePattern(new Image("/image_assets/avatars/" + avatarImgPath + ".png")));
        if (round) {
            vBoxAvatar.setBackground(new Background(focusBackground));
            vBoxAvatar.setEffect(new DropShadow(+30, 0d, +2d, Color.YELLOWGREEN));
        }
    }

    public void setCards(int numcards){
        this.cards.setText(Integer.toString(numcards));
        imgCard.setFill(new ImagePattern(new Image("/image_assets/card/UNO_Card.png")));
    }
}
