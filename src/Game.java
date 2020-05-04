import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Game extends JFrame implements ActionListener {
    public static final int windowWidth = 1150;
    public static final int windowHeight = 700;

    private static final int pilesSpacing = 40;

    private static final Color boardColor = new Color(0x10782b);
    final private BufferedImage backImage = Helper.loadImage("back1.png");

    private final GamePanel panel;

    // all cards available in game
    // <suit, rank>
    public HashMap<CardID, Card> cards = new HashMap<>();

    public List<Card> allCards = new ArrayList<>();
    private List<Card> allCardsCopy;
    public List<CardsPile> allPiles = new ArrayList<>();
    public DrawPile drawPile = new DrawPile(Game.windowWidth, Game.windowHeight);
    public CardsPile dragPile = new CardsPile(0, 0);
    public FinishedPile finishedPile = new FinishedPile(Game.windowWidth, Game.windowHeight);

    public long points = 0;
    public int difficulty = 5; // 2,3,5

    private Actions actions;

    private JMenuBar menuBar = new JMenuBar();
    private JMenu menu = new JMenu("Game");

    // TODO zrobić generowanie każdej możliwej karty na samym początku a potem tylko je kopiować

    public Game() {
        setMinimumSize(new Dimension(windowWidth, windowHeight));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setResizable(false);

        actions = new Actions(this);

        setMenu();
        setJMenuBar(menuBar);

        panel = new GamePanel(this);
        panel.setBackground(boardColor);

        setContentPane(panel);
        generatePiles();

//        newGame();

        // TODO DEBUG
//        CardsPile debug1 = allPiles.get(0);
//        debug1.addCard(new Card(12, true));
//        for (int i = 131; i > 11; i -= 10) {
//            debug1.addCard(new Card(i, false));
//        }
//        CardsPile debug2 = allPiles.get(1);
//        debug2.addCard(new Card(11, false));
//        CardsPile debug3 = allPiles.get(2);
//        debug3.addCard(new Card(22, false));
    }

    /**
     * Configures menubar for game
     */
    private void setMenu() {
        JMenuItem newGame = new JMenuItem("New Game", KeyEvent.VK_F2);
        JMenuItem restartGame = new JMenuItem("Restart This Game");
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem deal = new JMenuItem("Deal Next Row");
        JMenuItem tip = new JMenuItem("Show An Available Move");
        JMenuItem difficulty = new JMenuItem("Difficulty...");
        JMenuItem statistics = new JMenuItem("Statistics...");
        JMenuItem options = new JMenuItem("Options...");
        JMenuItem save = new JMenuItem("Save This Game");
        JMenuItem openSave = new JMenuItem("Open Last Saved Game");
        JMenuItem gitHub = new JMenuItem("GitHub");
        JMenuItem exit = new JMenuItem("Exit");

        newGame.setMnemonic(KeyEvent.VK_F2);
        actions.newGame.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F2);
        newGame.setAction(actions.newGame);
        newGame.setAccelerator(KeyStroke.getKeyStroke("F2"));

        restartGame.setAction(actions.restartGame);

        difficulty.setMnemonic(KeyEvent.VK_F3);
        actions.difficulty.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F3);
        difficulty.setAction(actions.difficulty);
        difficulty.setAccelerator(KeyStroke.getKeyStroke("F3"));


//        newGame.addActionListener(this);
//        restartGame.addActionListener(this);
//        undo.addActionListener(this);
//        deal.addActionListener(this);
//        tip.addActionListener(this);
//        difficulty.addActionListener(this);
//        statistics.addActionListener(this);
//        options.addActionListener(this);
//        save.addActionListener(this);
//        openSave.addActionListener(this);
//        exit.addActionListener(this);

        menu.add(newGame);
        menu.add(restartGame);
        menu.addSeparator();
        menu.add(undo);
        menu.add(deal);
        menu.add(tip);
        menu.addSeparator();
        menu.add(difficulty);
        menu.add(statistics);
        menu.add(options);
        menu.addSeparator();
        menu.add(save);
        menu.add(openSave);
        menu.addSeparator();
        menu.add(gitHub);
        menu.addSeparator();
        menu.add(exit);

        menuBar.add(menu);
    }

    private void generateGameCards() {
        long start = System.nanoTime();

        // i card type
        // j card color
        // k deck number
        for (int i = 1, j = 1, k = 0; k < 8; i++) {
            if (i == 14) {
                j++;
                if (j == difficulty)
                    j = 1;
                k++;
                i = 1;
            }
            CardID id = new CardID(j, i);
            Card card = new Card();
            cards.put(, )
        }
        Collections.shuffle(allCards);
        allCardsCopy = new ArrayList<>(allCards);

        long elapsedTime = System.nanoTime() - start;
        System.out.println("Czas: " + (elapsedTime * 0.000000001) + "s");
    }

    /**
     * Resets actual game and starts new
     */
    public void newGame() {
        // clears board
        clearPiles();

        // generates cards for game
        generateDeck();

        // give cards for drawPile
        generateDrawPile();

        // deal rest cards
        dealTheCards();

        // repaint board
        panel.repaint();
    }

    /**
     * Resets actual game and starts new
     */
    public void restartGame() {
        // TODO dodać dialog z potwierdzeniem czy chcesz jeszcze raz zagrać to rozdanie
        // clears board
        clearPiles();

        // restore previous cards generation
        allCards = new ArrayList<>(allCardsCopy);

        // give cards for drawPile
        generateDrawPile();

        // hide and lock all remaining cards
        for (Card card : allCards) {
//            card.hide();
            card.lock();
        }

        // deal rest cards
        dealTheCards();

        // repaint board
        panel.repaint();
    }

    /**
     * Clears ALL piles and deletes all cards
     */
    private void clearPiles() {
        allCards.clear();
        drawPile.pile.clear();
        dragPile.pile.clear();
        finishedPile.pile.clear();
        for (CardsPile cardsPile : allPiles) {
            cardsPile.pile.clear();
        }
    }

    /**
     * Generates all cards used in game
     */
    private void generateDeck() {
        allCards = new ArrayList<>();
        // i card type
        // j card color
        // k deck number
        for (int i = 1, j = 1, k = 0; k < 8; i++) {
            if (i == 14) {
                j++;
                if (j == difficulty)
                    j = 1;
                k++;
                i = 1;
            }
            // TODO optymalizacja
//            long start = System.nanoTime();
            {
                allCards.add(new Card(Integer.parseInt(i + "" + j), false));
            }
//            long elapsedTime = System.nanoTime() - start;
//            System.out.println("Czas: " + (elapsedTime * 0.000000001) + "s");
        }
        Collections.shuffle(allCards);
        allCardsCopy = new ArrayList<>(allCards);
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
     * Check piles for need to reveal top card
     */
    public void checkPiles() {
        for (CardsPile cardsPile : allPiles) {
            cardsPile.fixPile();
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

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO

    }
}
