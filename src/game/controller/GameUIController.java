package game.controller;

import game.model.Card;
import game.model.Color;
import game.model.StatusRegistry;
import game.model.Type;
import game.network.NetworkManager;
import game.network.PlayerNode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import utils.Logger;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;


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
    @FXML
    GridPane gridColorChoose;

    private static GameUIController instance;

    public static GameUIController getInstance() {
        return instance;
    }

    private ArrayList<Rectangle> hand = new ArrayList<>();
    private ArrayList<String> avatars = new ArrayList<>();
    private Card blackCard=null;

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

    public void setGridColorChooserVisible(){
        gridColorChoose.setVisible(true);
    }

    public void setBlackCard(Card card){
        this.blackCard = card;
    }

    public void colorChoose(ActionEvent event){
        String color = ((Button) event.getSource()).getId().replace("Choose","");
        LOG.info("Choose color: " + color.toUpperCase());
        switch (color){
            case "red":
                blackCard.setColor(Color.RED);
                break;
            case "blue":
                blackCard.setColor(Color.BLUE);
                break;
            case "green":
                blackCard.setColor(Color.GREEN);
                break;
            case "yellow":
                blackCard.setColor(Color.YELLOW);
                break;
        }
        try {
            new GameController().playCard(NetworkManager.getInstance().getMyNode(), blackCard);
        } catch (RemoteException e){
            e.printStackTrace();
        }
        blackCard = null;
        gridColorChoose.setVisible(false);
    }

    public void showDialog(String msg){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.CLOSE);
            alert.setTitle("");
            alert.show();
            if (alert.getResult() == ButtonType.CLOSE) {
                alert.close();
            }
        });
    }

    public void updateGUI(){
        Platform.runLater(()->{
            if (StatusRegistry.getInstance().getDirection()>0){
                leftArrow.setFill(new ImagePattern(new Image("/image_assets/arrows/CLOCK_LEFT.png")));
                rightArrow.setFill(new ImagePattern(new Image("/image_assets/arrows/CLOCK_RIGHT.png")));
            } else {
                leftArrow.setFill(new ImagePattern(new Image("/image_assets/arrows/ANTICLOCK_LEFT.png")));
                rightArrow.setFill(new ImagePattern(new Image("/image_assets/arrows/ANTICLOCK_RIGHT.png")));
            }
            hand.clear();
            for (Card card: StatusRegistry.getInstance().getPlayerHand(NetworkManager.getInstance().getMyNode())){

                hand.add(card.getGraphic(0.25, card));
            }
            hBoxCard.getChildren().clear();
            hBoxCard.getChildren().addAll(hand);
            Card topGraveyard = StatusRegistry.getInstance().getGraveyard().get(StatusRegistry.getInstance().getGraveyard().size()-1);
            rectangleGraveyard.setFill(new ImagePattern(new Image(topGraveyard.getImgPath())));
            if(topGraveyard.getType() == Type.COLORCHANGE ||topGraveyard.getType() == Type.DRAW4COLORCHANGE){
                String color = topGraveyard.getColor().toString().toLowerCase();
                if(color.equals("green")){
                    color = "rgba(0,255,0)";
                }
                LOG.info("COLOR:" + color);
                rectangleGraveyard.setStyle("-fx-effect: dropshadow(three-pass-box, " + color + ", 30, 0.7, 0, 0);");
            } else {
                rectangleGraveyard.setStyle("");
            }
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

    public void drawCardAction(MouseEvent mouseEvent) throws RemoteException {
        (new GameController()).drawCard(NetworkManager.getInstance().getMyNode());
    }

    public void callOneAction(ActionEvent event) throws RemoteException {
        if(StatusRegistry.getInstance().getCurrentPlayerIndex() == StatusRegistry.getInstance().getMyPlayerIndex()){
                (new GameController()).callOne(NetworkManager.getInstance().getMyNode());
            } else {
                showDialog("Non puoi chiamare UNO");
            }


    }

    public void showVictoryScreen(PlayerNode player) {
        Platform.runLater(()->{
            String winner = player.getUsername();
            if(player.getNetworkAddress().equals(NetworkManager.getInstance().getMyNode().getNetworkAddress())){
                winner = "YOU";
            }
            Alert victory = new Alert(Alert.AlertType.CONFIRMATION, "The winner is " + winner + "!",ButtonType.OK);
            victory.setTitle("Winner");
            victory.showAndWait();
            if (victory.getResult() == ButtonType.OK) {
                LOG.info(">>>>>>>> END MATCH <<<<<<<<");
                System.exit(0);
            }
        });
    }
}


