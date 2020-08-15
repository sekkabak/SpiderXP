import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Klasa obsługująca mysz w grze
 */
public class CustomMouseAdapter extends MouseAdapter {
    Game game;
    GamePanel gamePanel;

    int lastX, lastY;
    private boolean drawed;
    private CardsPile returnPile;

    public CustomMouseAdapter(Game game, GamePanel gamePanel) {
        super();
        this.game = game;
        this.gamePanel = gamePanel;
    }

    /**
     * Sprawdza wszystkie warunki dla naciśnięcia myszy
     * i na tej podstawie wykonuje odpowiednie wywołania
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(game.gameBlocked) return;

        super.mousePressed(e);
        int x = e.getX();
        int y = e.getY();

        // check for hint request in score area
        if (x > (Game.windowWidth-200)/2 && y > Game.windowHeight-200 && x < (Game.windowWidth-200)/2 + 200 && y < Game.windowHeight-100) {
            game.tip();
            return;
        }

        // check for draw request in draw area
        if (x > Game.windowWidth - 175 && y > Game.windowHeight - 175 && x < Game.windowWidth - 25 && y < Game.windowHeight - 45) {
            game.makeADeal();
            gamePanel.repaint();
            return;
        }

        // check for card selection
        for (CardsPile pile : game.allPiles) {
            List<Card> selected = pile.getCardsFormCords(x, y);
            if (selected != null) {
                for (Card card : selected) {
                    game.dragPile.addCard(card);
                }
                returnPile = pile;
                break;
            }
        }

        game.dragPile.translate(x - lastX - (Card.cardWidth / 2), y - lastY - 10);
        lastX = x - (Card.cardWidth / 2);
        lastY = y - 10;

        if(!game.dragPile.isEmpty()) {
            game.playSound(1);
        }
        gamePanel.repaint();
    }

    /**
     * Sprawdza czy jakiś przedmiot został poprawnie upuszczony
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(game.gameBlocked) return;

        super.mouseReleased(e);
        if (drawed) {
            drawed = false;
            gamePanel.repaint();
            return;
        }

        // dont handle empty hand
        if (game.dragPile.size() == 0)
            return;

        int x = e.getX();
        int y = e.getY();

        game.playSound(2);

        // check for good moves
        for (CardsPile pile : game.allPiles) {
            int pileHeight;
            if (pile.isEmpty()) {
                pileHeight = pile.y + Card.cardHeight;
            } else {
                pileHeight = pile.getLastCard().y + Card.cardHeight;
            }

            // if cursor was on pile
            if (x > pile.x && x < (pile.x + Card.cardWidth) && y > pile.y && y < pileHeight) {
                Card a = pile.getLastCard();                // top card from pile
                Card b = game.dragPile.pile.get(0);         // lower card from hand
                // check is cards has proper order
                if (a == null || a.getRank() == (b.getRank() + 1)) {
                    // move cards from hand to new pile
                    int limit = game.dragPile.size();
                    for (int i = 0; i < limit; i++) {
                        pile.addCard(game.dragPile.removeFirstCard());
                    }
                    int suit = pile.checkIfDeckIsComplete();
                    if (suit != -1) {
                        game.points+=100;
                        Card card = (Card) game.cards.get(new CardID(suit, 13)).clone();
                        game.finishedPile.addCard(card.getUnlockedAndVisible());
                    }
                    if(game.finishedPile.isAllDone())
                    {
                        gamePanel.repaint();
                        game.victory();
                    }

                    // check piles for fliping needed or unlocking
                    game.checkPiles();
                    game.makeMove();
                    gamePanel.repaint();
                    return;
                }
            }
        }

        // return cards to previous pile
        int limit = game.dragPile.size();
        for (int i = 0; i < limit; i++) {
            returnPile.addCard(game.dragPile.removeFirstCard());
        }
        gamePanel.repaint();
    }

    /**
     * Przemieszcze kupkę kart jeśli taka jest w ręce
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if(game.gameBlocked) return;

        if (drawed)
            return;

        if (!game.dragPile.isEmpty()) {
            int x = e.getX() - (Card.cardWidth / 2);
            int y = e.getY() - 10;
            game.dragPile.translate(x - lastX, y - lastY);
            lastX = x;
            lastY = y;

            gamePanel.repaint();
        }
    }
}
