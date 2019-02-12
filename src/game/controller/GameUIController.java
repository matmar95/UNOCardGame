package game.controller;

import game.model.Card;
import game.model.Color;
import game.model.Type;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;


public class GameUIController {
    @FXML
    ScrollPane scrollPane;
    @FXML
    HBox hBoxCard;
    @FXML
    Rectangle rectangleDeck;
    @FXML
    Rectangle rectangleGraveyard;
    @FXML
    HBox avatarBox;
    @FXML
    Rectangle leftArrow;
    @FXML
    Rectangle rightArrow;


    @FXML
    public void initialize(){
        leftArrow.setFill(new ImagePattern(new Image("/image_assets/arrows/CLOCK_LEFT.png")));
        rightArrow.setFill(new ImagePattern(new Image("/image_assets/arrows/CLOCK_RIGHT.png")));

        ArrayList<Rectangle> hand = new ArrayList<>();
       for (int i=0; i<10; i++){
           Card newCard = new Card(i, Color.RED, Type.NONE);
           hand.add(newCard.getGraphic(0.25));
           Card newCard2 = new Card(i, Color.BLUE, Type.NONE);
           hand.add(newCard2.getGraphic(0.25));
        }
        hBoxCard.getChildren().addAll(hand);
        hBoxCard.setSpacing(10);
        hBoxCard.setPadding(new Insets(10));
        scrollPane.setPannable(true);
        rectangleDeck.setFill(new ImagePattern(new Image("/image_assets/card/UNO_Card.png")));
        rectangleGraveyard.setFill(new ImagePattern(new Image("/image_assets/card/NONE_BLUE_2.png")));

        for (int i=0; i<10; i++) {
            try {
                FXMLLoader avatar = new FXMLLoader(getClass().getResource("/game/view/Avatar.fxml"));
                VBox vboxAvatar = avatar.load();
                AvatarController avatarController = avatar.getController();
                avatarController.setNameAvatar("ahahahaha"+i);
                avatarController.setCards(i);
                avatarController.setImgAvatar("avatar1");
                avatarBox.getChildren().add(vboxAvatar);
                //avatarBox.setSpacing(4);
                //avatarBox.setPadding(new Insets(2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


