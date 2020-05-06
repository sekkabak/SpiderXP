import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Game extends JFrame {
    public static final int windowWidth = 1150;
    public static final int windowHeight = 700;

    private static final int pilesSpacing = 40;

    private static final Color boardColor = new Color(0x10782b);
    public static final BufferedImage backImage = Helper.loadImage("back1.png");

    private final GamePanel panel;

    // all cards available in game
    // <suit, rank>
    public HashMap<CardID, Card> cards = new HashMap<>();

    public List<Card> gameCards = new ArrayList<>();
    public List<Card> gameCardsCopy = new ArrayList<>();

    public List<CardsPile> allPiles = new ArrayList<>();
    public DrawPile drawPile = new DrawPile(Game.windowWidth, Game.windowHeight);
    public CardsPile dragPile = new CardsPile(0, 0);
    public FinishedPile finishedPile = new FinishedPile(Game.windowWidth, Game.windowHeight);

    public Stack<GameState> gameStateStack = new Stack<GameState>();
    public GameState actualState = null;

    public long moves = 0;
    public long points = 0;

    public int difficulty = 5; // 2,3,5

    private Actions actions;

    private JMenuBar menuBar = new JMenuBar();
    private JMenu menu = new JMenu("Game");
    JMenuItem restartGame;
    JMenuItem undo;

    // TODO rozdzielić losowość

    // TODO za wcześnie pojawia się victory

    // TODO
//    long start = System.nanoTime();
//    long elapsedTime = System.nanoTime() - start;
//    System.out.println("Czas: " + (elapsedTime * 0.000000001) + "s");

    public Game() {
        generateCards();

        setMinimumSize(new Dimension(windowWidth, windowHeight));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setResizable(false);

        actions = new Actions(this);

        setMenu();
        setJMenuBar(menuBar);

        panel = new GamePanel(this);
        panel.setBackground(boardColor);

        setContentPane(panel);
        generatePiles();

        // TODO DEBUG
        Card card;
        CardsPile debug1 = allPiles.get(0);
//        for (int i = 13; i > 1; i -= 1) {
//            card = (Card) cards.get(new CardID(1, i)).clone();
//            debug1.addCard(card.getUnlockedAndVisible());
//        }
//        for (int i = 13; i > 1; i -= 1) {
//            card = (Card) cards.get(new CardID(1, i)).clone();
//            debug1.addCard(card.getUnlockedAndVisible());
//        }
//        for (int i = 11; i > 5; i -= 1) {
//            card = (Card) cards.get(new CardID(1, i)).clone();
//            debug1.addCard(card.getUnlockedAndVisible());
//        }
        card = (Card) cards.get(new CardID(1, 13)).clone();
        debug1.addCard(card.getUnlockedAndVisible());
        for (int i = 13; i > 1; i -= 1) {
            card = (Card) cards.get(new CardID(1, i)).clone();
            debug1.addCard(card.getUnlockedAndVisible());
        }
        CardsPile debug2 = allPiles.get(1);
        card = (Card) cards.get(new CardID(1, 1)).clone();
        debug2.addCard(card.getUnlockedAndVisible());
        CardsPile debug3 = allPiles.get(2);
        card = (Card) cards.get(new CardID(2, 2)).clone();
        debug3.addCard(card.getUnlockedAndVisible());

        setVisible(true);
        openDifficultyDialog();
    }

    public void openDifficultyDialog() {
        actions.difficultyDialog.changeDifficulty();
    }

    /**
     * Configures menubar for game
     */
    private void setMenu() {
        JMenuItem newGame = new JMenuItem("New Game", KeyEvent.VK_F2);
        restartGame = new JMenuItem("Restart This Game");
        undo = new JMenuItem("Undo");
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

        undo.setAction(actions.undo);
        undo.setEnabled(false);
        undo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        deal.setAction(actions.deal);

        tip.setEnabled(false);

        difficulty.setMnemonic(KeyEvent.VK_F3);
        actions.difficulty.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F3);
        difficulty.setAction(actions.difficulty);
        difficulty.setAccelerator(KeyStroke.getKeyStroke("F3"));

        statistics.setEnabled(false);
        options.setEnabled(false);
        save.setEnabled(false);
        openSave.setEnabled(false);

        gitHub.setAction(actions.gitHub);
        exit.setAction(actions.exit);

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

    /**
     * Generates all cards for future coping etc
     */
    private void generateCards() {
        // i card type
        // j card color
        // k deck number
        for (int i = 1, j = 1, k = 0; k < 8; i++) {
            if (i == 14) {
                j++;
                if (j == 5)
                    j = 1;
                k++;
                i = 1;
            }
            CardID id = new CardID(j, i);
            Card card = new Card(id, Helper.loadImage(id.hashCode() + ".png"), Game.backImage);
            cards.put(id, card);
        }
    }

    /**
     * Resets actual game and starts new
     */
    public void newGame() {
        resetMoves();

        // clears board
        clearPiles();

        // generates cards for game
        generateGameCards();

        // give cards for drawPile
        generateDrawPile();

        // deal rest cards
        dealTheCards();

        clearHistory();
        saveState();

        // repaint board
        panel.repaint();
    }

    /**
     * Resets actual game and starts new
     */
    public void restartGame() {
        if (moves == 0 && drawPile.size() == 52)
            return;

        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to restart this game from the beginning?", "Restart",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        resetMoves();

        // clears board
        clearPiles();

        // restore previous cards generation
        gameCards = new ArrayList<>(gameCardsCopy);

        // give cards for drawPile
        generateDrawPile();

        // hide and lock all remaining cards
        for (Card card : gameCards) {
            card.hide();
            card.lock();
        }

        // deal rest cards
        dealTheCards();

        clearHistory();
        saveState();

        // repaint board
        panel.repaint();
    }

    /**
     * Clears piles used in actual game
     */
    private void clearPiles() {
        gameCards.clear();
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
    private void generateGameCards() {
        // i card type
        // j card color
        // k deck number
        gameCards = new ArrayList<>();
        for (int i = 1, j = 1, k = 0; k < 8; i++) {
            Card card = (Card) cards.get(new CardID(j, i)).clone();
            gameCards.add(card);
            if (i == 13) {
                j++;
                if (j == difficulty)
                    j = 1;
                k++;
                i = 0;
            }
        }
        Collections.shuffle(gameCards);
        gameCardsCopy = new ArrayList<>(gameCards);
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
        int limit = gameCards.size();
        int allPilesSize = allPiles.size();
        // adds each card to his pile and also removes from generated cards pile
        for (int i = limit - 1; i >= 0; i--) {
            if (i < 10) {
                allPiles.get(i % allPilesSize).addCard(gameCards.remove(i).getUnlockedAndVisible());
            } else {
                allPiles.get(i % allPilesSize).addCard(gameCards.remove(i));
            }
        }
    }

    /**
     * Generates pile of cards for drawing from first 50 cards
     */
    private void generateDrawPile() {
        for (int i = 0; i < 50; i++) {
            drawPile.addCard(gameCards.remove(i));
        }
    }

    /**
     * Check piles for need to reveal top card
     */
    public void checkPiles() {
        for (CardsPile cardsPile : allPiles) {
            cardsPile.fixPile();
        }
        panel.repaint();
    }

    /**
     * Deals i pile from DrawPile
     */
    public void makeADeal() {
        if (drawPile.pile.isEmpty())
            return;

        for (CardsPile pile : allPiles) {
            if (pile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "U can't deal with empty slots");
                return;
            }
        }

        for (CardsPile pile : allPiles) {
            pile.addCard(drawPile.pile.remove(0).getUnlockedAndVisible());
        }

        restartGame.setEnabled(true);
        saveState();
        panel.repaint();
    }

    /**
     * Prints end of game dialog
     */
    public void victory() {
        JOptionPane.showMessageDialog(this, "Victory!");
    }

    public void resetMoves() {
        moves = 0;
        points = 500;
        restartGame.setEnabled(false);
    }

    public void makeMove() {
        if (moves == 0)
            restartGame.setEnabled(true);

        moves++;
        if (points > 0)
            points--;

        saveState();
    }

    public void saveState() {
        if(actualState != null) {
            gameStateStack.push(actualState);
            undo.setEnabled(true);
        }

        actualState = new GameState(allPiles, drawPile, finishedPile, moves, points);
    }

    public void clearHistory() {
        gameStateStack.clear();
        actualState = null;
        undo.setEnabled(false);
    }

    public void undo() {
        actualState = null;
        GameState state = gameStateStack.pop();
        allPiles = state.getAllPiles();
        drawPile = state.getDrawPile();
        finishedPile = state.getFinishedPile();
        moves = state.getMoves();
        points = state.getPoints();

        restartGame.setEnabled(moves != 0 || drawPile.size() != 52);
        undo.setEnabled(!gameStateStack.isEmpty());
        saveState();
        panel.repaint();
    }
}
