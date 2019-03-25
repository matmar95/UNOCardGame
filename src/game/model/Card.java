package game.model;

import game.controller.GameController;
import game.controller.GameUIController;
import game.network.NetworkManager;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.rmi.RemoteException;


public class Card extends Parent implements Serializable {

    private int idCard;
    private int number;
    private Color color;
    private Type type;
    private String imgPath;


    public Card(int number, Color color, Type type) {
        //Card info
        this.number = number;
        this.color = color;
        this.type = type;
        //Image Path
        this.imgPath = "image_assets/card/" + type;
        if(type == Type.NONE) {
            this.imgPath += "_" + color + "_" + number;
        } else if(type == Type.SKIP || type == Type.INVERT || type == Type.DRAW2){
            this.imgPath += "_" + color;
        }
        this.imgPath += ".png";
    }

    public int getIdCard() {
        return idCard;
    }

    public void setIdCard(int idCard) {
        this.idCard = idCard;
    }

    public int getNumber() {
        return number;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Type getType() {
        return type;
    }

    public String getImgPath() {
        return imgPath;
    }

    public Rectangle getGraphic(double resize, Card cartina){
        Rectangle rect = new Rectangle();
        rect.setFill(new ImagePattern(new Image(imgPath)));
        rect.setWidth(240*resize);
        rect.setHeight(360*resize);
        rect.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    if(cartina.getType() == Type.DRAW4COLORCHANGE || cartina.getType() == Type.COLORCHANGE ){
                        if (new GameController().isMyTurn(NetworkManager.getInstance().getMyNode())) {
                            if(new GameController().isValidCard(NetworkManager.getInstance().getMyNode(),cartina)) {
                                GameUIController.getInstance().setGridColorChooserVisible();
                                GameUIController.getInstance().setBlackCard(cartina);
                            } else {
                                GameUIController.getInstance().showDialog("You can't play this card!");
                            }
                        } else {
                            GameUIController.getInstance().showDialog("It's not your turn yet!");
                        }
                    } else {
                        new GameController().playCard(NetworkManager.getInstance().getMyNode(), cartina);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return rect;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            return idCard == ((Card) obj).getIdCard();
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Card("
                + "id="
                + idCard
                + ", number="
                + number
                + ", color="
                + color
                + ", type="
                + type
                + ", imagePath='"
                + imgPath
                + '\''
                + ')';
    }
}
