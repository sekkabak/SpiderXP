import javax.swing.*;
import java.awt.*;

/**
 * Klasa okienka zmiany poziomu trudności
 */
public class DifficultyDialog {
    JRadioButton easy, medium, hard;
    Game game;
    int localDifficulty = 5;

    public DifficultyDialog(Game game) {
        this.game = game;
    }

    /**
     * Otwiera okno dialogu oraz przypisuje
     * dynamiczną zmianę wartości do przycisków
     */
    public void changeDifficulty() {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        easy = new JRadioButton("Easy", game.difficulty == 2);
        medium = new JRadioButton("Medium", game.difficulty == 3);
        hard = new JRadioButton("Hard", game.difficulty == 5);

        ButtonGroup buttonsGroup = new ButtonGroup();
        buttonsGroup.add(easy);
        buttonsGroup.add(medium);
        buttonsGroup.add(hard);

        panel.add(easy);
        panel.add(medium);
        panel.add(hard);

        easy.addActionListener(x -> setDifficulty(2));
        medium.addActionListener(x -> setDifficulty(3));
        hard.addActionListener(x -> setDifficulty(5));

        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Choose difficulty"));

        int result = JOptionPane.showOptionDialog(null, panel, "Choose difficulty", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if(result == 0) {
            game.difficulty = localDifficulty;
            game.newGame();
        }
    }

    private void setDifficulty(int difficulty) {
        this.localDifficulty = difficulty;
    }
}
