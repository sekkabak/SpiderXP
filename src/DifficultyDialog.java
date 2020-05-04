import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DifficultyDialog implements ChangeListener {
    JRadioButton button1, button2, button3;
    Game game;

    public DifficultyDialog(Game game) {
        this.game = game;
    }

    public void changeDifficulty() {
        final JPanel p = new JPanel();
        button1 = new JRadioButton("Easy");
        button2 = new JRadioButton("Medium");
        button3 = new JRadioButton("Hard");

        if (game.difficulty == 2) {
            button1.setSelected(true);
        } else if (game.difficulty == 3) {
            button1.setSelected(true);
        } else if (game.difficulty == 5) {
            button1.setSelected(true);
        }

        p.add(button1);
        p.add(button2);
        p.add(button3);

        button1.addChangeListener(this);

        JOptionPane.showMessageDialog(null, p);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (button1.isSelected()) {
            game.difficulty = 2;
        } else if (button2.isSelected()) {
            game.difficulty = 3;
        } else if (button3.isSelected()) {
            game.difficulty = 5;
        }
    }
}
