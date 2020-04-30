import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    protected Game game;

    public GamePanel(Game game) {
        super();
        this.game = game;

        CustomMouseAdapter mouseAdapter = new CustomMouseAdapter(this.game, this);
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
//        super.setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (CardsPile pile : game.allPiles) {
            pile.paintPile(g);
        }
        game.drawPile.paintPile(g);
        game.dragPile.paintPile(g);
    }
}
