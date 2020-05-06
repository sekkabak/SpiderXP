import java.awt.*;
import java.awt.image.BufferedImage;

public class Card extends Rectangle {
    public CardID id;

    public static final int cardWidth = 71;
    public static final int cardHeight = 96;

    final private BufferedImage image;
    final private BufferedImage backImage;
    private boolean isFaceDown;
    private boolean locked;

    public Card(CardID id, BufferedImage image, BufferedImage backImage) {
        this.width = Card.cardWidth;
        this.height = Card.cardHeight;

        this.id = id;
        this.image = image;
        this.backImage = backImage;
        this.isFaceDown = true;
        this.locked = true;
    }

    public BufferedImage getImage() {
        return isFaceDown ? backImage : image;
    }

    public boolean isFaceDown() {
        return isFaceDown;
    }
    public boolean isLocked() {
        return locked;
    }

    public void lock() {
        locked = true;
    }
    public void unlock() {
        locked = false;
    }

    public void hide() {
        isFaceDown = true;
    }
    public void show() {
        isFaceDown = false;
    }

    public int getSuit() {
        return id.getSuit();
    }
    public int getRank() {
        return id.getRank();
    }

    public Card getUnlockedAndVisible() {
        locked = false;
        isFaceDown = false;
        return this;
    }
}
