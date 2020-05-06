import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Actions {
    final Game game;
    DifficultyDialog difficultyDialog;

    public Actions(Game game) {
        this.game = game;
        difficultyDialog = new DifficultyDialog(this.game);
    }

    Action newGame = new AbstractAction("New Game") {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.newGame();
        }
    };

    Action restartGame = new AbstractAction("Restart This Game") {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.restartGame();
        }
    };

    Action undo = new AbstractAction("Undo") {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.undo();
        }
    };

    Action deal = new AbstractAction("Deal Next Row") {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeADeal();
        }
    };

    Action difficulty = new AbstractAction("Difficulty...") {
        @Override
        public void actionPerformed(ActionEvent e) {
            difficultyDialog.changeDifficulty();
        }
    };

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

    Action exit = new AbstractAction("Exit") {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };
}
