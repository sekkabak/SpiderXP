import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * Klasa dziedzicząca z JPanel spersonalizowana dla tej gry
 * dodaje mouse listenera oraz rysowanie wszystkich obiektów
 */
public class GamePanel extends JPanel {
    protected Game game;
    private final BufferedImage backgroundImage;

    private final List<AnimatedCard> toAnimateCards = new ArrayList<>();
    private final List<AnimatedCard> animatedCards = new ArrayList<>();

    public GamePanel(Game game) {
        super();
        this.game = game;

        backgroundImage = Helper.loadImage("FELT.bmp");
        CustomMouseAdapter mouseAdapter = new CustomMouseAdapter(this.game, this);
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);
        drawScoreBox(g);

        paintPilesHolders(g);

        for (CardsPile pile : game.allPiles) {
            pile.paintPile(g);
        }
        game.drawPile.paintPile(g);
        game.finishedPile.paintPile(g);
        game.dragPile.paintPile(g);

        drawAnimatedCards(g);
    }

    /**
     * Rysuje kwadraty na pustych miejscach kupek
     */
    private void paintPilesHolders(Graphics g) {
        g.setColor(Color.gray);
        for (int i = 0; i < 10; i++) {
            g.drawRect(i * (Card.cardWidth + 40) + 42, 7, 65, 90);
        }
    }

    private void drawScoreBox(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect((Game.windowWidth - 200) / 2, Game.windowHeight - 200, 200, 100);
        g.setColor(new Color(0x075c1d));
        g.fillRect((Game.windowWidth - 200) / 2 + 1, Game.windowHeight - 200 + 1, 199, 99);
        g.setColor(Color.WHITE);
        g.setFont(new Font("default", Font.BOLD, 12));
        g.drawString("Score: " + game.points, (Game.windowWidth - 100) / 2, Game.windowHeight - 150);
        g.drawString("Moves: " + game.moves, (Game.windowWidth - 100) / 2, Game.windowHeight - 135);
    }

    private void drawAnimatedCards(Graphics g) {
        List<AnimatedCard> toRemove = new ArrayList<>();

        for (AnimatedCard card : animatedCards) {
            if (card.move()) {
                toRemove.add(card);
            }
            g.drawImage(card.card.getImage(), card.card.x, card.card.y, null);
        }

        // usunięcie kart które dotarły na miejsce
        for (AnimatedCard card : toRemove) {
            animatedCards.remove(card);
        }
        toRemove.clear();
    }

    private void drawBackground(Graphics g) {
        for (int x = 0; x < (Game.windowWidth / backgroundImage.getWidth()) + 1; x++) {
            for (int y = 0; y < (Game.windowHeight / backgroundImage.getHeight()) + 1; y++) {
                g.drawImage(backgroundImage, x * backgroundImage.getWidth(), y * backgroundImage.getHeight(), null);
            }
        }
    }

    public void animateCardMoving(Card card, CardsPile from, CardsPile to) {
        card.setLocation(from.x, from.y);
        toAnimateCards.add(new AnimatedCard(card, to));
    }

    public void fireAnimation() {
        final int[] interval = {0};
        Timer timer = new Timer();

        class Repaint extends TimerTask {
            public void run() {
                if (interval[0] == 60 && !toAnimateCards.isEmpty()) {
                    game.playSound(0);
                    animatedCards.add(toAnimateCards.remove(0));
                    interval[0] = 0;
                }

                if (animatedCards.isEmpty()) {
                    timer.cancel();
                    timer.purge();
                    game.endAnimation();
                }

                repaint();
                interval[0]++;
            }
        }

        // uruchomienie sekwencji
        game.playSound(0);
        animatedCards.add(toAnimateCards.remove(0));
        timer.schedule(new Repaint(), 0, 2);
        game.blockGame();
    }

    static class AnimatedCard {
        public Card card;
        CardsPile to;

        int destX;
        int destY;
        int cardSpeed = 10;

        AnimatedCard(Card card, CardsPile to) {
            this.card = card;
            this.to = to;

            Card last = to.getFirstUnlocked();
            if(last != null) {
                destX = last.x;
                destY = last.y + to.pileOffset;
            } else {
                destX = to.x;
                destY = to.y;
            }
        }

        /**
         * @return zwraca true jeśli karta dotarła na miejsce
         */
        public boolean move() {
            // jeśli karta osiągnęła cel to umieszczamy ją na kupce
            if (card.x == destX && card.y == destY) {
                if(to.getClass() != FinishedPile.class) {
                    to.addCard(card);
                }
                return true;
            }

            // sprawdzenie czy karta znajduje się już aproksymacyjnie na miejscu
            if (Math.abs(card.x - destX) < cardSpeed) {
                card.x = destX;
            }
            if (Math.abs(card.y - destY) < cardSpeed) {
                card.y = destY;
            }

            // obliczenie wektora ruchu
            int[] vector = {(card.x - destX), (card.y - destY)};

            // poruszanie kartą
            card.x -= vector[0] / cardSpeed;
            card.y -= vector[1] / cardSpeed;

            return false;
        }
    }
}
