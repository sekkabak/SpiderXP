import java.awt.*;

public class DrawPile extends CardsPile {
    protected final int pileOffset = 12;

    public DrawPile(int windowWidth, int windowHeight) {
        super(windowWidth - 45, windowHeight - 70);
    }

    @Override
    public void paintPile(Graphics g) {
        int offset = 0;
        int limit = pile.size() / 10;
        for (int i = 0; i < limit; i++) {
            g.drawImage(Game.backImage, this.x - offset - Card.cardWidth, this.y - Card.cardHeight, null);
            offset += pileOffset;
        }
    }
}
