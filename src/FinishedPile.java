import java.awt.*;

/**
 * Klasa obsługująca kupkę do której wpadają złożone już karty
 * @see CardsPile
 */
public class FinishedPile extends CardsPile {
    protected final int pileOffset = 50;

    public FinishedPile(int windowWidth, int windowHeight) {
        super(25, windowHeight - Card.cardHeight - 75);
    }

    @Override
    public void paintPile(Graphics g) {
        int offset = 0;
        for (Card card : pile) {
            g.drawImage(card.getImage(), this.x + offset, this.y, null);
            offset += pileOffset;
        }
    }

    public boolean isAllDone() {
        return this.pile.size() == 8;
    }
}
