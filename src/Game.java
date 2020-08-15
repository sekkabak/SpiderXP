import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.prefs.Preferences;

/**
 * Główny obiekt zarządzający grą oraz przetrzymujący dane
 */
public class Game extends JFrame {
    public static final String gameSavePath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/SpiderXPsave.dat";

    public static final int windowWidth = 1150;
    public static final int windowHeight = 700;

    private static final int pilesSpacing = 40;

    private static final Color boardColor = new Color(0x10782b);
    public static final BufferedImage backImage = Helper.loadImage("back1.png");

    private final GamePanel panel;

    // wszystkie karty dostępne w grze
    // <suit, rank>
    public HashMap<CardID, Card> cards = new HashMap<>();

    public List<Card> gameCards = new ArrayList<>();
    public List<Card> gameCardsCopy = new ArrayList<>();

    public List<CardsPile> allPiles = new ArrayList<>();
    public DrawPile drawPile = new DrawPile(Game.windowWidth, Game.windowHeight);
    public CardsPile dragPile = new CardsPile(0, 0);
    public FinishedPile finishedPile = new FinishedPile(Game.windowWidth, Game.windowHeight);

    public Stack<GameState> gameStateStack = new Stack<>();
    public GameState actualState = null;

    public boolean gameBlocked = false;

    public long moves = 0;
    public long points = 0;

    public int difficulty = 5; // 2,3,5

    boolean colorAssurance = true;
    private final int[] tipIndex = {0, 0};
    private final Semaphore tipQueueSemaphore = new Semaphore(1);

    private final String[] sounds = {
            "cardDraw",
            "cardPick",
            "cardDrop",
            "hint",
            "noHint",
            "win"
    };

    private final Actions actions;

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu menu = new JMenu("Game");
    private final JMenu help = new JMenu("Help");
    private JMenuItem restartGame;
    private JMenuItem undo;

    private final Preferences preferences = Preferences.userRoot().node(getName());
    public final Statistics statistics = new Statistics(preferences);

    public Game() {
        generateCards();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // nie ustawiono
        }

        setTitle("Spider solitaire");
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
//        Helper.createDebugPiles(this);

        setVisible(true);
        openDifficultyDialog();
    }

    public void openDifficultyDialog() {
        actions.difficultyDialog.changeDifficulty();
    }

    /**
     * Konfiguruje menubar gry
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
        tip.setAction(actions.tip);
        difficulty.setMnemonic(KeyEvent.VK_F3);
        actions.difficulty.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F3);
        difficulty.setAction(actions.difficulty);
        difficulty.setAccelerator(KeyStroke.getKeyStroke("F3"));

        // TODO
        options.setEnabled(false);

        statistics.setAction(actions.statistics);
        save.setAction(actions.save);
        openSave.setAction(actions.openSave);
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

        JMenuItem rules = new JMenuItem("Rules");
        rules.setAction(actions.rules);
        JMenuItem about = new JMenuItem("About Spider...");
        about.setAction(actions.about);
        help.add(rules);
        help.addSeparator();
        help.add(about);

        menuBar.add(menu);
        menuBar.add(help);
    }

    /**
     * Generuje wszystkie możliwe karty do późniejszego klonowania
     * co zmniejsza znacząco czas otwierania nowej gry oraz samej aplikacji
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
     * Czyści stół oraz zaczyna nową grę
     */
    public void newGame(int difficulty) {
        // jeśli wykonano ruch to dodaj grę jako przegraną
        if (moves > 0) {
            statistics.addLose(this.difficulty);
            statistics.submitScore(this.difficulty, points);
        }

        // zmienia poziom trudności gry i resetuje ruchy
        this.difficulty = difficulty;
        resetMoves();

        // czyści stół(wszystkie kupki)
        clearPiles();

        // generuje karty do aktualnej gry
        generateGameCards();

        // wtasowuje karty do talii z której będą dobierane
        generateDrawPile();

        // rozdaje resztę kart
        dealTheCards();

        clearHistory();
        saveState();

        // przemalowuje stół
        panel.repaint();
    }

    /**
     * Resetuje aktualną grę i zaczyna od początku(to samo rozłożenie)
     */
    public void restartGame() {
        if (moves == 0 && drawPile.size() == 52)
            return;

        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to restart this game from the beginning?", "Restart",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        // dodaje przegraną do statystyk
        statistics.addLose(difficulty);
        statistics.submitScore(difficulty, points);

        // resetuje historie cofania
        resetMoves();

        // czyści stół(wszystkie kupki)
        clearPiles();

        // przywraca poprzednią talie kart
        gameCards = new ArrayList<>(gameCardsCopy);

        // wtasowuje karty do talii z której będą dobierane
        generateDrawPile();

        // chowa i blokuje wszystkie pozostałe karty
        for (Card card : gameCards) {
            card.hide();
            card.lock();
        }

        // rozdaje resztę kart
        dealTheCards();

        clearHistory();
        saveState();

        // przemalowuje stół
        panel.repaint();
    }

    /**
     * Czyści wszystkie kupki kart
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
     * Kopiuje z wygenerowanych wcześniej obiektów kart
     * te które będą używane w grze
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
     * Tworzy 10 kupek kart na stole gry
     */
    private void generatePiles() {
        int offsetInterval = Card.cardWidth + pilesSpacing;
        allPiles = new ArrayList<>();
        for (int i = 0, offset = pilesSpacing; i < 10; i++, offset += offsetInterval) {
            allPiles.add(new CardsPile(offset, 5));
        }
    }

    /**
     * Zapełnia główne 10 kupek z reszty przetasowanych kart
     */
    private void dealTheCards() {
        int limit = gameCards.size();
        int allPilesSize = allPiles.size();
        // adds each card to his pile and also removes from generated cards pile
        for (int i = limit - 1; i >= 0; i--) {
            CardsPile pile = allPiles.get(i % allPilesSize);
            Card card = gameCards.remove(i);
            if (i < 10) {
                pile.addCard(card.getUnlockedAndVisible());
            } else {
                pile.addCard(card);
            }
//            card.x = pile.x;
//            card.y = pile.y;
        }
    }

    /**
     * Wtasowuje 50 pierwszch przetasowanych kart do kupek
     * z których będą dobierane karty
     */
    private void generateDrawPile() {
        for (int i = 0; i < 50; i++) {
            drawPile.addCard(gameCards.remove(i));
        }
    }

    /**
     * Sprawdza kupki czy jest jakaś zakryta karta na szczycie kupki
     */
    public void checkPiles() {
        for (CardsPile cardsPile : allPiles) {
            cardsPile.fixPile();
        }
        panel.repaint();
    }

    /**
     * Rozdaje karty z kupki do dobierania, zdejmuje z kupki
     * dobierania 'jedną' kartę oraz rozdaje po jednej karcie na każdę kupkę gracza
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
            panel.animateCardMoving(drawPile.pile.remove(0).getUnlockedAndVisible(), drawPile, pile);
        }
        panel.fireAnimation();
    }

    public void endAnimation() {
        unlockGame();
        restartGame.setEnabled(true);
        saveState();
        panel.repaint();
    }

    /**
     * Wyświetla powiadomienie o tym że gracz wygrał
     */
    public void victory() {
        statistics.addWin(difficulty);
        statistics.submitScore(difficulty, points);
        playSound(5);
        JOptionPane.showMessageDialog(this, "Victory!");
    }

    /**
     * Resetuje licznik punktów oraz ruchów
     */
    public void resetMoves() {
        moves = 0;
        points = 500;
        restartGame.setEnabled(false);
    }

    /**
     * Zmienia punkty
     * Dodaje punkt ruchu
     * Odblokowuje resetowanie gry
     */
    public void makeMove() {
        if (moves == 0)
            restartGame.setEnabled(true);

        moves++;
        if (points > 0)
            points--;

        saveState();
        resetHintIndex();
    }

    /**
     * Zapisuje aktualny stan stołu gry do późniejszego cofania
     */
    public void saveState() {
        if (actualState != null) {
            gameStateStack.push(actualState);
            undo.setEnabled(true);
        }

        actualState = new GameState(allPiles, drawPile, finishedPile, moves, points);
    }

    /**
     * Usuwa historię gry
     */
    public void clearHistory() {
        gameStateStack.clear();
        actualState = null;
        resetHintIndex();
        undo.setEnabled(false);
    }

    /**
     * Przywraca ostatni zapisany stan gry
     * oraz usuwa go z stosu stanów
     */
    public void undo() {
        actualState = null;
        GameState state = gameStateStack.pop();
        allPiles = state.getAllPiles();
        drawPile = state.getDrawPile();
        finishedPile = state.getFinishedPile();
        moves = state.getMoves();
        points = state.getPoints();
        moves += 2;
        points -= 2;

        restartGame.setEnabled(moves != 0 || drawPile.size() != 52);
        undo.setEnabled(!gameStateStack.isEmpty());
        saveState();
        panel.repaint();
    }

    /**
     * Tworzy zapis gry do pliku
     */
    public void saveActualGame() {
        Object[] data = new Object[]{
                moves,
                points,
                difficulty,
                gameCardsCopy,
                allPiles,
                drawPile,
                dragPile,
                finishedPile
        };
        try {
            FileOutputStream fos = new FileOutputStream(gameSavePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Otwiera zapis gry z pliku
     */
    public void openSavedGame() {
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to discard the game you are currently playing, and load your previously saved game?", "Spider",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        clearHistory();

        try {
            FileInputStream fis = new FileInputStream(gameSavePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object[] data = (Object[]) ois.readObject();
            ois.close();
            moves = (long) data[0];
            points = (long) data[1];
            difficulty = (int) data[2];

            //noinspection unchecked
            gameCardsCopy = (List<Card>) data[3];
            for (Card card : gameCards) card.recreateImages();

            //noinspection unchecked
            allPiles = (List<CardsPile>) data[4];
            for (CardsPile pile : allPiles) recreateImagesInPile(pile);

            drawPile = (DrawPile) data[5];
            recreateImagesInPile(drawPile);

            dragPile = (CardsPile) data[6];
            recreateImagesInPile(dragPile);

            finishedPile = (FinishedPile) data[7];
            recreateImagesInPile(finishedPile);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        panel.repaint();
    }

    private void recreateImagesInPile(CardsPile pile) {
        for (Card card : pile.pile) {
            card.recreateImages();
        }
    }

    public void tip() {
        new Thread(() -> {
            try {
                tipQueueSemaphore.acquire();
                // jeśli podpowiedź w poprawnych kolorach się nie powiodła
                if (tipSearch(colorAssurance)) {
                    colorAssurance = !colorAssurance;
                    if (tipSearch(colorAssurance)) {
                        if (tipSearch(!colorAssurance)) {
                            playSound(4);
                            tipQueueSemaphore.release();
                            return;
                        }
                    }
                }

                playSound(3);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }

    private boolean tipSearch(boolean colorAssurance) {
        int i = tipIndex[0];
        int j = tipIndex[1];

        for (; i < allPiles.size(); i++) {
            CardsPile pile = allPiles.get(i);

            // ominięcie pustej kupki
            if (pile.size() == 0) continue;

            Card card = pile.getFirstUnlocked();
            if (card == null) {
                new Exception("Któraś z kupek jest błędna").printStackTrace();
                return true;
            }

            for (; j < allPiles.size(); j++) {
                CardsPile card2Pile = allPiles.get(j);

                // ominięcie tej samej kupki oraz pustej kupki
                if (j == i) continue;

                // jeśli kupka jest pusta to każda karta może zostać tam położona
                if (card2Pile.size() == 0) {
                    tipIndex[0] = i;
                    tipIndex[1] = ++j;

                    CardID id = new CardID(1, 1);
                    Card newCard = new Card(id, Game.backImage, Game.backImage);
                    newCard.hide();
                    new Hint(this, newCard, pile, pile.pile.indexOf(card), tipQueueSemaphore, card2Pile);
                    return false;
                }

                // sprawdzanie czy kupka się nadaje
                Card card2 = card2Pile.getLastCard();

                // sprawdzenie poprawności koloru
                if (colorAssurance && card2.getSuit() != card.getSuit()) {
                    continue;
                }
                // wyrzucenie identycznych kolorów kiedy są one nie sprawdzane
                else if (!colorAssurance && card2.getSuit() == card.getSuit()) {
                    continue;
                }

                // sprawdzenie poprawności ruchu
                if (card2.getRank() == (card.getRank() + 1)) {
                    tipIndex[0] = i;
                    tipIndex[1] = ++j;
                    new Hint(this, card2, pile, pile.pile.indexOf(card), tipQueueSemaphore);
                    return false;
                }
            }
            tipIndex[1] = j = 0;
        }
        tipIndex[0] = 0;
        return true;
    }

    private void resetHintIndex() {
        tipIndex[0] = 0;
        tipIndex[1] = 0;
        colorAssurance = true;
    }

    /**
     * <p>
     * Plays sounds<br>
     * <p>
     * 0 - "cardDraw"<br>
     * 1 - "cardPick"<br>
     * 2 - "cardDrop"<br>
     * 3 - "hint"<br>
     * 4 - "noHint"<br>
     * 5 - "win"<br>
     * </p>
     *
     * @param soundId int index in sounds array
     */
    public synchronized void playSound(int soundId) {
        if (soundId < 0 || soundId >= sounds.length) {
            new Exception("Dzwięk nie istnieje!").printStackTrace();
            return;
        }

        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        Game.class.getResourceAsStream("/sounds/" + sounds[soundId] + ".wav"));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }

    public void blockGame() {
        gameBlocked = true;
        menu.setEnabled(false);
        help.setEnabled(false);
    }

    public void unlockGame() {
        gameBlocked = false;
        menu.setEnabled(true);
        help.setEnabled(true);
    }
}
