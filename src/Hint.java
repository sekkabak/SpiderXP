import java.util.concurrent.Semaphore;

public class Hint {
    private final Game game;
    private final Card card;
    private final CardsPile pile;
    private final int index;
    private CardsPile emptyPile = null;
    private Semaphore semaphore = null;

    private final int animationSpeed = 300;

    public Hint(Game game, Card card, CardsPile pile, int index, Semaphore semaphore) {
        this.game = game;
        this.card = card;
        this.pile = pile;
        this.index = index;
        this.semaphore = semaphore;

        invertPile(pile, index);
        game.repaint();
        invertSecondCard();
    }

    public Hint(Game game, Card card, CardsPile pile, int index, Semaphore semaphore, CardsPile emptyPile) {
        this(game, card, pile, index, semaphore);
        this.emptyPile = emptyPile;
    }

    private void invertSecondCard() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        invertPile(pile, index);
                        card.invert();
                        if(emptyPile != null)
                            emptyPile.addCard(card);

                        game.repaint();
                        invertBackSecondCard();
                    }
                },
                animationSpeed
        );
    }

    private void invertBackSecondCard() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        card.invert();
                        if(emptyPile != null)
                            emptyPile.removeCard();

                        semaphore.release();
                        game.repaint();
                    }
                },
                animationSpeed
        );
    }

    private void invertPile(CardsPile pile, int index) {
        for (int i = index; i < pile.size(); i++) {
            pile.pile.get(i).invert();
        }
    }
}
