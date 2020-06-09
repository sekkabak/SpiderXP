import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Klasa obiektu karta, używanego do rysowania na stole oraz
 * sprawdzania przez parametr id mechanik gry
 */
public class Card extends Rectangle {
    /**
     * Zawiera kod karty czyli jej kolor oraz wartość
     * @see CardID
     */
    public CardID id;

    public static final int cardWidth = 71;
    public static final int cardHeight = 96;

    /**
     * Przednie zdjęcie karty
     */
    final private BufferedImage image;

    /**
     * Tylne zdjęcie karty
     */
    final private BufferedImage backImage;

    private boolean isFaceDown;
    private boolean locked;

    /**
     * Stworzenie karty w grze
     *
     * @param id wartość karty oraz jej kolor
     * @param image obiekt obrazka danej karty
     * @param backImage obiekt obrazka tyłu karty
     */
    public Card(CardID id, BufferedImage image, BufferedImage backImage) {
        this.width = Card.cardWidth;
        this.height = Card.cardHeight;

        this.id = id;
        this.image = image;
        this.backImage = backImage;
        this.isFaceDown = true;
        this.locked = true;
    }

    /**
     * @return zwraca aktualnie wykorzystywany obrazek karty
     * jeśli jest niewidoczna dla gracza zwraca tylni obrazek
     */
    public BufferedImage getImage() {
        return isFaceDown ? backImage : image;
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
}
