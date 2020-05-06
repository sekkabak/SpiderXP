import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CardsPile implements Cloneable {
    protected List<Card> pile;
    protected final int pileOffset = 7;
    public int x, y;

    public CardsPile(int x, int y) {
        this.x = x;
        this.y = y;
        this.pile = new ArrayList<>();
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
            if (x.getSuit() == next.getSuit() && (x.getRank() + 1) == next.getRank() && !next.isFaceDown()) {
                next.unlock();
            } else {
                next.lock();
            }
        }
    }

    public void paintPile(Graphics g) {
        int offset = 0;
        int faceDownOffset = 22 - (pile.size() * 2) / 5;
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
        return this.pile.remove(this.pile.size() - 1);
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
     * Fixes pile when
     * - Top card is locked and hidden
     */
    public void fixPile() {
        Card topCard = getLastCard();
        if (topCard != null) {
            removeCard();   // removes card to check
            addCard(topCard.getUnlockedAndVisible());
        }
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

                List<Card> tmpPile = new ArrayList<>();
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
     * @return suit id or -1 when deck is not full
     */
    public int checkIfDeckIsComplete() {
        // minimum length for deck
        int pileSize = size();
        if (pileSize < 13)
            return -1;

        int i;
        int prevSuit = -1;
        int sequenceScore = 13;
        for (i = 0; i < pileSize; i++) {
            Card card = pile.get(i);

            // different color
            if(card.getSuit() != prevSuit) {
                prevSuit = card.getSuit();
                sequenceScore = 13;
            }

            // card in proper sequence
            if (card.getRank() == sequenceScore) {
                sequenceScore--;
            }
            // begin new sequence with king
            else if(card.getRank() == 13) {
                sequenceScore = 12;
            }
            // reset sequence
            else {
                sequenceScore = 13;
            }

            // sequence is complete
            if(sequenceScore == 0)
                break;
        }

        // remove cards from pile
        if(sequenceScore == 0) {
            for (int j = 13; j > 0; j--, i--) {
                pile.remove(i);
            }

            return prevSuit;
        }

        return -1;
    }

    @Override
    public CardsPile clone() {
        try {
            CardsPile clone = (CardsPile) super.clone();
            List<Card> clonedPile = new ArrayList<>();
            for (Card card : clone.pile) {
                Card x = (Card) card.clone();
                clonedPile.add(x);
            }

            clone.pile = clonedPile;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
