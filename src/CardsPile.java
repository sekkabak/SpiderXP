import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
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

    // TODO to jest potrzebne?
    public List<Card> getPile() {
        return pile;
    }

    public void addCard(Card card) {
        lockAll();
        card.unlock();
        pile.add(card);

        int pileSize = pile.size();

        // Check right order
        for (int i = pileSize - 1; i > 0; i--) {
            Card x = pile.get(i);

            // skip rest of locked cards
            if (x.isLocked())
                continue;

            Card next = pile.get(i - 1);
            if (x.getSuit() == next.getSuit() && (x.getCardValue() + 1) == next.getCardValue() && !next.isFaceDown()) {
                next.unlock();
            } else {
                next.lock();
            }
        }
    }

    public void paintPile(Graphics g) {
        // TODO zrobić elastyczny offset
        int offset = 0;
        int faceDownOffset = 22;
        boolean lastFaceDown = true;
        for (Card card : pile) {
            card.x = this.x;
            card.y = this.y + offset;
            if (!lastFaceDown) {
                card.y += faceDownOffset;
                offset += this.pileOffset + faceDownOffset;
            } else {
                offset += this.pileOffset;
            }
            g.drawImage(card.getImage(), card.x, card.y, null);

            lastFaceDown = card.isFaceDown();
        }
    }

    public boolean isEmpty() {
        return this.pile.isEmpty();
    }

    public Card removeFirstCard() {
        return pile.remove(0);
    }

    public Card removeCard(int index) {
        if (index == this.pile.size() - 1)
            return removeCard();

        return this.pile.remove(index);
    }

    public Card removeCard() {
        Card card = this.pile.remove(this.pile.size() - 1);
        if (!this.pile.isEmpty()) {
            // by removing and adding I prevent blocking segments
            Card newTop = this.pile.remove(this.pile.size() - 1);
            if (newTop.isFaceDown())
                newTop.flip();
            addCard(newTop);
        }
        return card;
    }

    private void lockAll() {
        for (Card card : pile) {
            card.lock();
        }
    }

    public void translate(int x, int y) {
        this.x += x;
        this.y += y;
        this.pile.forEach(a -> a.translate(x, y));
    }

    public int size() {
        return pile.size();
    }

    public Card getLastCard() {
        if (pile.isEmpty())
            return null;

        return pile.get(pile.size() - 1);
    }

    /**
     * Returns first visible card on given coordinates
     * Removes card from picked pile
     *
     * @param x x in game
     * @param y y in game
     * @return returns List of card that was taken from pile
     */
    public List<Card> getCardsFormCords(int x, int y) {
        int pileSize = pile.size();
        for (int i = pileSize - 1; i >= 0; i--) {
            Card card = pile.get(i);
            if (!card.isLocked() && card.contains(x, y)) {
                // We take all cards to oposite direction then loop

                List<Card> tmpPile = new ArrayList<Card>();
                // Just one cart from beginning
                if (i == pileSize - 1) {
                    tmpPile.add(card);
                    removeCard(i);
                }
                // More cards will be taken
                else {
                    tmpPile.add(card);
                    removeCard(i);

                    for (int k = i; k != pileSize - 1; k++) {
                        tmpPile.add(removeCard(i));
                    }
                }
                return tmpPile;
            }
        }
        return null;
    }

    /**
     * Checks if deck was complited in this pile then remove it and returns suit id
     *
     * @return suit id
     */
    public int checkIfDeckIsComplete() {
        int completePoints = 0;     // 13 is full
        int actualSuit = pile.get(0).getSuit();
        int actualCardValue = 13;
        int limit = pile.size();
        for (int i = 0; i < limit; i++) {
            Card card = pile.get(i);
            if (card.getCardValue() == actualCardValue && card.getSuit() == actualSuit) {
                completePoints++;
                actualCardValue--;
            } else {
                completePoints = 0;
                actualCardValue = 13;
            }

            if (completePoints == 13) {
                while(true) {
                    card = pile.remove(i);
                    if(card.getCardValue() == 13)
                        return actualSuit;
                    i--;
                }
            }
        }

        return -1;
    }
}
