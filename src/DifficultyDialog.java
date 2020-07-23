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
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));


        easy = new JRadioButton("Easy: One Suit", game.difficulty == 2);
        medium = new JRadioButton("Medium: Two Suit", game.difficulty == 3);
        hard = new JRadioButton("Hard: Four Suits", game.difficulty == 5);

        ButtonGroup buttonsGroup = new ButtonGroup();
        buttonsGroup.add(easy);
        buttonsGroup.add(medium);
        buttonsGroup.add(hard);

        JLabel diffLabel = new JLabel();
        diffLabel.setIcon(new ImageIcon(Helper.loadImage("difficulty.png")));
        diffLabel.setBackground(Color.RED);
        panel.add(diffLabel);

        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new GridLayout(3, 1));
        radioButtonPanel.add(easy);
        radioButtonPanel.add(medium);
        radioButtonPanel.add(hard);
        panel.add(radioButtonPanel);

        easy.addActionListener(x -> setDifficulty(2));
        medium.addActionListener(x -> setDifficulty(3));
        hard.addActionListener(x -> setDifficulty(5));

        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Select the game difficulty level that you want"));

        int result = JOptionPane.showOptionDialog(null, panel, "Choose difficulty", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, null, null);
        if(result == 0) {
            game.newGame(localDifficulty);
        }
    }

    private void setDifficulty(int difficulty) {
        this.localDifficulty = difficulty;
    }
}
