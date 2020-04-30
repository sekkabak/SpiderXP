import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CardsPile {
    protected final List<Card> pile;

    // przesunięcie kart na stosie
    protected final int pileOffset = 7;
    public int x, y;


    public CardsPile(int x, int y) {
        this.x = x;
        this.y = y;
        this.pile = new ArrayList<Card>();
    }

    public List<Card> getPile() {
        return pile;
    }

    public void addCard(Card card) {
        pile.add(card);
    }

    public void paintPile(Graphics g) {
        int offset = 0;
        for (Card card : pile) {
            g.drawImage(card.getImage(), this.x, this.y + offset, null);
            offset += this.pileOffset;
        }
    }
}
