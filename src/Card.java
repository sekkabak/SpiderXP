import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Card {
    public static final int cardWidth = 71;
    public static final int cardHeight = 96;

    final private BufferedImage image;
    final private BufferedImage backImage;
    private boolean isFaceDown;
    /**
     * First number
     *  Ace = 1
     *  2 = 2
     *  ...
     *  10 = 10
     *  Jack = 11
     *  Queen = 12
     *  King = 13
     * Second number
     *  1 - club
     *  2 - spade
     *  3 - heart
     *  4 - diamond
     */
    private int rank;

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
            img = ImageIO.read(getClass().getResourceAsStream("/images/"+fileName));
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
}
