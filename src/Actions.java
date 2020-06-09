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
}
