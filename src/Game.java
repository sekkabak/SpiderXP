import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends JFrame {
    public static final int windowWidth = 1150;
    public static final int windowHeight = 700;

    private static final int pilesSpacing = 40;

    private static final Color boardColor = new Color(0x10782b);

    private final GamePanel panel;

    public List<Card> allCards = new ArrayList<>();
    public List<CardsPile> allPiles = new ArrayList<>();
    public DrawPile drawPile = new DrawPile(Game.windowWidth, Game.windowHeight);
    public CardsPile dragPile = new CardsPile(0, 0);
    public FinishedPile finishedPile = new FinishedPile(Game.windowWidth, Game.windowHeight);

    public long points = 0;
    public int difficulty = 1; // 1,2,3

    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Menu");

    // TODO bugs
    // TODO przy zdjęcie talii do kupki złożonych karta poniżej nie odkrywa się
    // TODO przy wybraniu karty i złapaniu jej pokazuje się karta z pod spodu
    // TODO przy złożeniu całej talii czasami nie zostaje ona wrzucona do kupki złożonych

    
    public Game() {
        setMinimumSize(new Dimension(windowWidth, windowHeight));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setResizable(false);

        setJMenuBar(menuBar);
        setMenu();

        panel = new GamePanel(this);
        panel.setBackground(boardColor);

        setContentPane(panel);

        generateDeck();
        generatePiles();

        generateDrawPile();
        dealTheCards();

        // TODO DEBUG
//        CardsPile debug1 = allPiles.get(0);
//        for (int i = 131; i > 11; i -= 10) {
//            debug1.addCard(new Card(i, false));
//        }
//        CardsPile debug2 = allPiles.get(1);
//        debug2.addCard(new Card(11, false));

    }

    /**
     * Configures menubar for game
     */
    private void setMenu() {
        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem restartGame = new JMenuItem("Restart This Game");
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem deal = new JMenuItem("Deal Next Row");
        JMenuItem tip = new JMenuItem("Show An Available Move");
        JMenuItem difficulty = new JMenuItem("Difficulty...");
        JMenuItem statistics = new JMenuItem("Statistics...");
        JMenuItem options = new JMenuItem("Options...");
        JMenuItem save = new JMenuItem("Save This Game");
        JMenuItem openSave = new JMenuItem("Open Last Saved Game");
        JMenuItem exit = new JMenuItem("Exit");


    }

    /**
     * Generates all cards used in game
     */
    private void generateDeck() {
        // TODO dodać poziomy trudności
        int[] difficulty = {2, 4};
        switch (this.difficulty) {
            default:
            case 1: // easy
                difficulty[0] = 8;
                difficulty[1] = 1;
                break;
            case 2: // medium
                difficulty[0] = 8;
                difficulty[1] = 1;
                break;
            case 3: // hard
                break;
        }


        allCards = new ArrayList<>();
        for (int k = 0; k < difficulty[0]; k++) {
            for (int i = 1; i <= difficulty[1]; i++) {
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
            } else {
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

    /**
     * Deals i pile from DrawPile
     */
    public void makeADeal() {
        for (CardsPile pile : allPiles) {
            if (pile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "U can't deal with empty slots");
                return;
            }
        }

        if (!drawPile.pile.isEmpty()) {
            for (CardsPile pile : allPiles) {
                pile.addCard(drawPile.pile.remove(0));
            }
        }
    }

    /**
     * Prints end of game dialog
     */
    public void victory() {
        JOptionPane.showMessageDialog(this, "Victory!");
    }
}
