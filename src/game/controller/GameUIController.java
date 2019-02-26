package game.controller;

import game.model.Card;
import game.model.Color;
import game.model.StatusRegistry;
import game.model.Type;
import game.network.NetworkManager;
import game.network.PlayerNode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import utils.Logger;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.rmi.RemoteException;
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

    private static GameUIController instance;

    public static GameUIController getInstance() {
        return instance;
    }

    private ArrayList<Rectangle> hand = new ArrayList<>();
    private ArrayList<String> avatars = new ArrayList<>();
    @FXML
    public void initialize(){
        instance=this;
        leftArrow.setFill(new ImagePattern(new Image("/image_assets/arrows/CLOCK_LEFT.png")));
        rightArrow.setFill(new ImagePattern(new Image("/image_assets/arrows/CLOCK_RIGHT.png")));
        updateGUI();
        hBoxCard.setSpacing(10);
        hBoxCard.setPadding(new Insets(10));
        scrollPane.setPannable(true);
        rectangleDeck.setFill(new ImagePattern(new Image("/image_assets/card/UNO_Card.png")));
        avatars = StatusRegistry.getInstance().getAvatars();
        int numAvatar = 1;
        for (PlayerNode player : StatusRegistry.getInstance().getPlayers()){
            player.setAvatar(avatars.get(numAvatar));
            numAvatar++;
        }
    }

    public void showDialog(String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.CLOSE);
        alert.setTitle("");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.CLOSE) {
            alert.close();
        }
    }

    public void updateGUI(){
        Platform.runLater(()->{
            hand.clear();
            for (Card card: StatusRegistry.getInstance().getPlayerHand(NetworkManager.getInstance().getMyNode())){

                hand.add(card.getGraphic(0.25, card));
            }
            hBoxCard.getChildren().clear();
            hBoxCard.getChildren().addAll(hand);
            rectangleGraveyard.setFill(new ImagePattern(new Image(StatusRegistry.getInstance().getGraveyard().get(StatusRegistry.getInstance().getGraveyard().size()-1).getImgPath())));
            avatars = StatusRegistry.getInstance().getAvatars();
            avatarBox.getChildren().clear();
            int index = 0;
            for (PlayerNode player : StatusRegistry.getInstance().getPlayers()){
                try {
                    FXMLLoader avatar = new FXMLLoader(getClass().getResource("/game/view/Avatar.fxml"));
                    VBox vboxAvatar = avatar.load();
                    AvatarController avatarController = avatar.getController();
                    if (player == NetworkManager.getInstance().getMyNode()){
                        avatarController.setNameAvatar("You");
                    }else{
                        avatarController.setNameAvatar(player.getUsername());
                    }
                    avatarController.setCards(StatusRegistry.getInstance().getPlayerHand(player).size());
                    if (StatusRegistry.getInstance().getCurrentPlayerIndex() == index){
                        avatarController.setImgAvatar(player.getAvatar(),true);
                    }else{
                        avatarController.setImgAvatar(player.getAvatar(),false);
                    }
                    avatarBox.getChildren().add(vboxAvatar);
                    index++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void drawCardAction(javafx.scene.input.MouseEvent mouseEvent) throws RemoteException {
        (new GameController()).drawCard(NetworkManager.getInstance().getMyNode());
    }
}


