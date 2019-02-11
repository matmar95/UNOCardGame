package game.controller;

import game.model.Card;
import game.model.Color;
import game.model.Type;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

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

    ImageView img = new ImageView("/image_assets/card/NONE_BLUE_0.png");
    ImageView img2 = new ImageView("/image_assets/card/NONE_BLUE_1.png");
    ImageView img3 = new ImageView("/image_assets/card/NONE_BLUE_2.png");

    @FXML
    public void initialize(){
        ArrayList<Rectangle> hand = new ArrayList<>();
       for (int i=0; i<10; i++){
           Card newCard = new Card(i, Color.RED, Type.NONE);
           hand.add(newCard.getGraphic(0.25));
           Card newCard2 = new Card(i, Color.BLUE, Type.NONE);
           hand.add(newCard2.getGraphic(0.25));
        }
        //Card newCard = new Card(1, Color.RED, Type.NONE);
        hBoxCard.getChildren().addAll(hand);
        //hBoxCard.getChildren().addAll(new Button("button1"), new Button("button2"), new Button("button3"));
        hBoxCard.setSpacing(10);
        hBoxCard.setPadding(new Insets(10));
        scrollPane.setPannable(true);
        rectangleDeck.setFill(new ImagePattern(new Image("/image_assets/card/UNO_Card.png")));
        rectangleGraveyard.setFill(new ImagePattern(new Image("/image_assets/card/NONE_BLUE_2.png")));

    }
}


