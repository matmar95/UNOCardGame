package game.model;

import javafx.scene.Parent;

import java.io.Serializable;

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
