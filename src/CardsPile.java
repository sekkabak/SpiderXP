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

    public boolean isEmpty() {
        return this.pile.isEmpty();
    }

    public Card getCard(int index) {
        return this.pile.get(index);
    }

    public Card removeCard(int index) {
        return this.pile.remove(index);
    }

    public Card removeCard() {
        return this.pile.remove(this.pile.size() - 1);
    }

    public void translate(int x, int y) {
        this.x += x;
        this.y += y;
        this.pile.forEach(a -> a.translate(x, y));
    }
}
