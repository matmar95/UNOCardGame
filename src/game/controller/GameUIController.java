package game.controller;

import game.model.Card;
import game.model.Color;
import game.model.StatusRegistry;
import game.model.Type;
import game.network.NetworkManager;
import game.network.PlayerNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class GameUIController {

    private Logger LOG = new Logger(GameUIController.class);
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

    private ArrayList<Rectangle> hand = new ArrayList<>();
    private ArrayList<String> avatars = new ArrayList<>();
    @FXML
    public void initialize(){
        leftArrow.setFill(new ImagePattern(new Image("/image_assets/arrows/CLOCK_LEFT.png")));
        rightArrow.setFill(new ImagePattern(new Image("/image_assets/arrows/CLOCK_RIGHT.png")));
        /*ArrayList<Rectangle> hand = new ArrayList<>();
       for (int i=0; i<10; i++){
           Card newCard = new Card(i, Color.RED, Type.NONE);
           hand.add(newCard.getGraphic(0.25));
           Card newCard2 = new Card(i, Color.BLUE, Type.NONE);
           hand.add(newCard2.getGraphic(0.25));
        }*/
        //hBoxCard.getChildren().addAll(hand);
        updateGUI();
        hBoxCard.setSpacing(10);
        hBoxCard.setPadding(new Insets(10));
        scrollPane.setPannable(true);
        rectangleDeck.setFill(new ImagePattern(new Image("/image_assets/card/UNO_Card.png")));


       /* for (int i=11; i<=20; i++) {
            try {
                FXMLLoader avatar = new FXMLLoader(getClass().getResource("/game/view/Avatar.fxml"));
                VBox vboxAvatar = avatar.load();
                AvatarController avatarController = avatar.getController();
                avatarController.setNameAvatar("ahahahaha"+i);
                avatarController.setCards(i);
                avatarController.setImgAvatar("avatar"+i);
                avatarBox.getChildren().add(vboxAvatar);
                //avatarBox.setSpacing(4);
                //avatarBox.setPadding(new Insets(2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void updateGUI(){
        //System.out.println(StatusRegistry.getInstance().getPlayerHand(NetworkManager.getInstance().getMyNode())+ "AAAA");
        for (Card card: StatusRegistry.getInstance().getPlayerHand(NetworkManager.getInstance().getMyNode())){
            hand.add(card.getGraphic(0.25));
        }
        hBoxCard.getChildren().addAll(hand);
        rectangleGraveyard.setFill(new ImagePattern(new Image(StatusRegistry.getInstance().getGraveyard().get(StatusRegistry.getInstance().getGraveyard().size()-1).getImgPath())));
        avatars = StatusRegistry.getInstance().getAvatars();
        int numAvatar = 1;
        for (PlayerNode player : StatusRegistry.getInstance().getPlayers()){
            try {
                FXMLLoader avatar = new FXMLLoader(getClass().getResource("/game/view/Avatar.fxml"));
                VBox vboxAvatar = avatar.load();
                AvatarController avatarController = avatar.getController();
                avatarController.setNameAvatar(player.getUsername());
                avatarController.setCards(StatusRegistry.getInstance().getPlayerHand(player).size());
                avatarController.setImgAvatar(avatars.get(numAvatar));
                avatarBox.getChildren().add(vboxAvatar);
                numAvatar++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}


