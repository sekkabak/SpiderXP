import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Klasa posiadająca obsługiwane przez klase akcje i metody
 * wywoływane przez użytkownika
 */
public class Actions {
    final Game game;
    DifficultyDialog difficultyDialog;

    public Actions(Game game) {
        this.game = game;
        difficultyDialog = new DifficultyDialog(this.game);
    }

    /**
     * Otwiera nową grę
     */
    Action newGame = new AbstractAction("New Game") {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.newGame();
        }
    };

    /**
     * Resetuje aktulanie rozgrywaną gre
     * (ustawia wszystkie karty jak na początku)
     */
    Action restartGame = new AbstractAction("Restart This Game") {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.restartGame();
        }
    };

    /**
     * Cofa do poprzedniego stanu gry
     * (sprzed wykonania przed użytkownika ruchu)
     */
    Action undo = new AbstractAction("Undo") {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.undo();
        }
    };

    /**
     * Akcja rozdania talii z sterty przygotowanej do rozdań
     */
    Action deal = new AbstractAction("Deal Next Row") {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeADeal();
        }
    };

    /**
     * Wyświetlenie okna z wyborem poziomu trudności
     *
     * @see DifficultyDialog
     */
    Action difficulty = new AbstractAction("Difficulty...") {
        @Override
        public void actionPerformed(ActionEvent e) {
            difficultyDialog.changeDifficulty();
        }
    };

    /**
     * Otwiera okno domyślnej przeglądarki z włączoną stroną repozytorium
     * projektu SpiderXP
     */
    Action gitHub = new AbstractAction("GitHub") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/sekkabak/SpiderXP"));
                } catch (IOException | URISyntaxException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    };

    /**
     * Wyłącza aplikacje
     */
    Action exit = new AbstractAction("Exit") {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

    /**
     * Wyświetla zasady gry
     */
    Action rules = new AbstractAction("Rules") {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(game, "<html><h4><strong>The Pack</strong></h4>" +
                    "<p>Two standard 52-card packs are used.</p>" +
                    "<h4><strong>Object of the Game</strong></h4>" +
                    "<p>The goal is to assemble 13 cards of a suit, in ascending sequence from ace through king, on top of a pile.</p>" +
                    "<p>Whenever a full suit of 13 cards is so assembled, it is lifted off and " +
                    "discarded from the game. The game is won if all eight suits are played out.</p>" +
                    "<h4><strong>The Deal</strong></h4>" +
                    "<p>Ten piles of five cards each are dealt by rows. The first four cards of each " +
                    "pile are dealt face down, the top cards face up.</p>" +
                    "<h4><strong>The Play</strong></h4>" +
                    "<p>The top card of a pile may be moved, together with all face-up cards below it that " +
                    "follow in ascending suit and sequence.</p>" +
                    "<p>A sequence of available cards may be broken at any point by leaving some cards behind.</p>" +
                    "<p>Example: If a pile from top down shows 4, 5, 6, 7, either the first one, two, or three cards " +
                    "may be moved as a unit, but the 7 may not be moved until the covering three cards are removed.</p>" +
                    "<p>When all face-up cards on a pile are removed, the next card below is turned face up and becomes available.</p>" +
                    "<p>A movable unit of cards may be placed either in a space or on a card of the next-higher rank " +
                    "to the bottom card of the unit, regardless of color or suit.</p>" +
                    "<p>Example: If the bottom card of a unit is the J, it may be moved onto any one of the four queens.</p>" +
                    "<p>A king can be moved only onto a space. Alternatively, the spaces may be filled with any movable unit.</p>" +
                    "<p>When all possible or desired moves come to a standstill, the player deals another row of ten cards face up.</p>" +
                    "<p>However, before such a deal may be made, all spaces must be filled. The final deal consists of only four cards, " +
                    "which are placed on the first four piles.</p></html>", "Rules", JOptionPane.INFORMATION_MESSAGE);
        }
    };

    /**
     * Wyświetla about
     */
    Action about = new AbstractAction("About Spider...") {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(game, "<html>" +
                    "<h3>Remake of classic WindowsXP Spider Solitaire<br>" +
                    "Created by: Cezary Bąk</h3>" +
                    "</html>", "About", JOptionPane.INFORMATION_MESSAGE);
        }
    };

    /**
     * Zapisuje aktualną grę
     */
    Action save = new AbstractAction("Save This Game") {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.saveActualGame();
        }
    };

    /**
     * Wczytuje ostatnio zapisaną grę
     */
    Action openSave = new AbstractAction("Open Last Saved Game") {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.openSavedGame();
        }
    };

}
