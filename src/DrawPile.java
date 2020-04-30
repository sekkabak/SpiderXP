import java.awt.*;

public class DrawPile extends CardsPile {
    protected final int pileOffset = 12;

    public DrawPile(int windowWidth, int windowHeight) {
        super(windowWidth - 45, windowHeight - 50);
    }

    @Override
    public void paintPile(Graphics g) {
        int offset = 0;
        int limit = pile.size() / 10;
        Card pileTop = new Card(11, true);
        for (int i = 0; i < limit; i++) {
            g.drawImage(pileTop.getImage(), this.x - offset - Card.cardWidth, this.y - Card.cardHeight, null);
            offset += pileOffset;
        }
    }
}
