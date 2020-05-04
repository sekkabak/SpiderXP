import javax.swing.*;
import java.awt.event.ActionEvent;

public class Actions {
    final Game game;
    DifficultyDialog difficultyDialog;

    public Actions(Game game) {
        this.game = game;
        difficultyDialog = new DifficultyDialog(this.game);
    }

    Action newGame = new AbstractAction("newGame") {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.newGame();
        }
    };

    Action restartGame = new AbstractAction("restartGame") {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.restartGame();
        }
    };

    Action difficulty = new AbstractAction("difficulty") {
        @Override
        public void actionPerformed(ActionEvent e) {
            difficultyDialog.changeDifficulty();
        }
    };
}
