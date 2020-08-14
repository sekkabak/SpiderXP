import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Klasa obiektu karta, używanego do rysowania na stole oraz
 * sprawdzania przez parametr id mechanik gry
 */
public class Card extends Rectangle {
    /**
     * Zawiera kod karty czyli jej kolor oraz wartość
     *
     * @see CardID
     */
    public CardID id;

    public static final int cardWidth = 71;
    public static final int cardHeight = 96;

    /**
     * Przednie zdjęcie karty
     */
    transient private BufferedImage image;
    transient private BufferedImage invertedImage;

    /**
     * Tylne zdjęcie karty
     */
    transient private BufferedImage backImage;

    private boolean isInverted = false;
    private boolean isFaceDown;
    private boolean locked;

    /**
     * Stworzenie karty w grze
     *
     * @param id        wartość karty oraz jej kolor
     * @param image     obiekt obrazka danej karty
     * @param backImage obiekt obrazka tyłu karty
     */
    public Card(CardID id, BufferedImage image, BufferedImage backImage) {
        this.width = Card.cardWidth;
        this.height = Card.cardHeight;

        this.id = id;
        this.image = image;
        this.invertedImage = invertImage(image);
        this.backImage = backImage;
        this.isFaceDown = true;
        this.locked = true;
    }

    /**
     * @return zwraca aktualnie wykorzystywany obrazek karty
     * jeśli jest niewidoczna dla gracza zwraca tylni obrazek
     */
    public BufferedImage getImage() {
        if (isInverted)
            return invertedImage;
        else if (isFaceDown)
            return backImage;

        return image;
    }

    /**
     * @return zwraca odblokowaną, pokazaną kartę
     */
    public Card getUnlockedAndVisible() {
        locked = false;
        isFaceDown = false;
        return this;
    }

    public boolean isFaceDown() {
        return isFaceDown;
    }

    public boolean isLocked() {
        return locked;
    }

    public void invert() { isInverted = !isInverted; }

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

    public void recreateImages() {
        image = Helper.loadImage(id.hashCode() + ".png");
        invertedImage = invertImage(image);
        backImage = Game.backImage;
    }

    private BufferedImage invertImage(BufferedImage image) {
        // deep copy image
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        BufferedImage inputFile = new BufferedImage(cm, raster, isAlphaPremultiplied, null);

        // invert image
        for (int x = 0; x < inputFile.getWidth(); x++) {
            for (int y = 0; y < inputFile.getHeight(); y++) {
                int rgba = inputFile.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                        255 - col.getGreen(),
                        255 - col.getBlue());
                inputFile.setRGB(x, y, col.getRGB());
            }
        }
        return inputFile;
    }
}
