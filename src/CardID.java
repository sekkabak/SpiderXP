public class CardID {
    /**
     * 1 - spade
     * 2 - heart
     * 3 - club
     * 4 - diamond
     */
    private final int suit;
    /**
     * Ace = 1
     * 2 = 2
     * ...
     * 10 = 10
     * Jack = 11
     * Queen = 12
     * King = 13
     */
    private final int rank;

    public CardID(int suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardID)) return false;
        CardID id = (CardID) o;
        return this.suit == id.suit && this.rank == id.rank;
    }

    @Override
    public int hashCode() {
        return rank * 10 + suit;
    }

    public int getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }
}