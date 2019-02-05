package game.model;

import java.io.Serializable;

public class Card implements Serializable {

    private int id;
    private int number;
    private CardColor color;
    private CardType type;
    private String imgPath;

    public Card(int number, CardColor color, CardType type) {
        this.number = number;
        this.color = color;
        this.type = type;
        this.imgPath = "image_assets/card/" + type;
        if(type == CardType.NONE) {
            this.imgPath += "_" + color + "_" + number;
        } else if(type == CardType.SKIP || type == CardType.INVERT || type == CardType.DRAW2){
            this.imgPath += "_" + color;
        }
        this.imgPath += ".png";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public CardType getType() {
        return type;
    }

    public String getImgPath() {
        return imgPath;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            return id == ((Card) obj).getId();
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Card("
                + "id="
                + id
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
