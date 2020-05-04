import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Class for card object
 * <p>
 * Rank in number reprezentation:
 * First number
 * Ace = 1
 * 2 = 2
 * ...
 * 10 = 10
 * Jack = 11
 * Queen = 12
 * King = 13
 * Second number (suit)
 * 1 - club
 * 2 - spade
 * 3 - heart
 * 4 - diamond
 */
public class Card extends Rectangle {
    public int rank;

    public static final int cardWidth = 71;
    public static final int cardHeight = 96;

    final private BufferedImage image;
    final private BufferedImage backImage;
    private boolean isFaceDown;
    private boolean locked;

    public Card(int rank, boolean isFaceDown) {
        this.width = Card.cardWidth;
        this.height = Card.cardHeight;

        this.rank = rank;
        this.image = loadImageByRank(rank);
        this.backImage = loadImage("back1.png");
        this.isFaceDown = isFaceDown;
        this.locked = true;
    }

    private BufferedImage loadImageByRank(int rank) {
        return loadImage(rank + ".png");
    }



    public BufferedImage getImage() {
        return isFaceDown ? backImage : image;
    }

    public boolean isFaceDown() {
        return isFaceDown;
    }

    public void flip() {
        isFaceDown = !isFaceDown;
    }

    public int getRank() {
        return rank;
    }

    public int getCardValue() {
        return getRank() / 10;
    }

    public int getSuit() {
        return getRank() - (getCardValue() * 10);
    }

    public void unlock() {
        locked = false;
    }

    public void lock() {
        locked = true;
    }

    public boolean isLocked() {
        return locked;
    }

    public void hide() {
        isFaceDown = true;
    }
}
