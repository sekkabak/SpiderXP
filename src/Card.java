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
 * Second number
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
    private boolean locked = true;

    public Card(int rank, boolean isFaceDown) {
        this.rank = rank;
        this.image = loadImageByRank(rank);
        this.backImage = loadImage("back1.png");
        this.isFaceDown = isFaceDown;
    }

    private BufferedImage loadImageByRank(int rank) {
        return loadImage(rank + ".png");
    }

    private BufferedImage loadImage(String fileName) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(getClass().getResourceAsStream("/images/" + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return img;
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

    public void unlock() {
        locked = !locked;
    }

    public boolean isClicked(int x, int y) {
        return (!locked && x > this.x && x < (this.x + Card.cardWidth)) && (y > this.y && y < (this.y + Card.cardHeight));
    }
}
