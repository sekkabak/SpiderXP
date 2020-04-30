import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends JFrame {
    private static final int windowWidth = 1150;
    private static final int windowHeight = 700;

    private static final int pilesSpacing = 40;

    private static final Color boardColor = new Color(0x10782b);

    private JFrame frame;
    private GamePanel panel;

    public List<Card> allCards = new ArrayList<>();
    public List<CardsPile> allPiles = new ArrayList<>();
    public DrawPile drawPile = new DrawPile(Game.windowWidth, Game.windowHeight);
    public CardsPile dragPile = new CardsPile(0, 0);

    public long points = 0;
    public int difficulty = 3; // 1,2,3

    public Game() {
        setMinimumSize(new Dimension(windowWidth, windowHeight));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //        frame.setResizable(false);

        panel = new GamePanel(this);
        panel.setBackground(boardColor);

        setContentPane(panel);

        generateDeck();
        generatePiles();

        generateDrawPile();
        dealTheCards();

    }

    /**
     * Generates all cards used in game
     */
    private void generateDeck() {
        // TODO dodać poziomy trudności
        allCards = new ArrayList<>();
        for (int k = 0; k < 2; k++) {
            for (int i = 1; i <= 4; i++) {
                for (int j = 1; j <= 13; j++) {
                    allCards.add(new Card(Integer.parseInt(j + "" + i), false));
                }
            }
        }
        Collections.shuffle(allCards);
    }

    /**
     * Generates main 10 piles for game
     */
    private void generatePiles() {
        int offsetInterval = Card.cardWidth + pilesSpacing;
        allPiles = new ArrayList<>();
        for (int i = 0, offset = pilesSpacing; i < 10; i++, offset += offsetInterval) {
            allPiles.add(new CardsPile(offset, 5));
        }
    }

    /**
     * Fills main 10 piles from rest of the shuffled cards
     */
    private void dealTheCards() {
        int limit = allCards.size();
        // adds each card to his pile and also removes from generated cards pile
        for (int i = limit - 1; i >= 0; i--) {
            Card card = allCards.remove(i);
            if (allCards.size() >= 10) {
                card.flip();
                card.unlock();
            }
            allPiles.get(i % allPiles.size()).addCard(card);
        }
    }

    /**
     * Generates pile of cards for drawing from first 50 cards
     */
    private void generateDrawPile() {
        for (int i = 0; i < 50; i++) {
            drawPile.addCard(allCards.remove(i));
        }
    }
}
