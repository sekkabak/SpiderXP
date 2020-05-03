import java.awt.*;

public class FinishedPile extends CardsPile {
    protected final int pileOffset = 50;

    public FinishedPile(int windowWidth, int windowHeight) {
        super(-20, windowHeight - 50);
    }

    @Override
    public void paintPile(Graphics g) {
        int offset = 0;
        for (Card card : pile) {
            g.drawImage(card.getImage(), this.x + offset + Card.cardWidth, this.y - Card.cardHeight, null);
            offset += pileOffset;
        }
    }

    public boolean isAllDone() {
        return this.pile.size() == 8;
    }
}
