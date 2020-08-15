import java.awt.*;

/**
 * Klasa obsługująca kupkę z której dobieramy karty
 * @see CardsPile
 */
public class DrawPile extends CardsPile {
    protected final int pileOffset = 12;

    public DrawPile(int windowWidth, int windowHeight) {
        super(windowWidth - 45 - Card.cardWidth, windowHeight - 70 - Card.cardHeight);
    }

    @Override
    public void paintPile(Graphics g) {
        int offset = 0;
        int limit = pile.size() / 10;
        for (int i = 0; i < limit; i++) {
            g.drawImage(Game.backImage, this.x - offset, this.y, null);
            offset += pileOffset;
        }
    }
}
