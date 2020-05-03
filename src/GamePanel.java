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

        paintPilesHolders(g);

//        // TODO
//        g.setColor(Color.RED);
//        g.drawRect(Game.windowWidth-175,Game.windowHeight-150,151,106);

        for (CardsPile pile : game.allPiles) {
            pile.paintPile(g);
        }
        game.drawPile.paintPile(g);
        game.finishedPile.paintPile(g);
        game.dragPile.paintPile(g);
    }

    private void paintPilesHolders(Graphics g) {
        g.setColor(Color.gray);
        for (int i = 0; i < 10; i++) {
            g.drawRect(i*(Card.cardWidth+40)+42, 7, 65, 90);
        }
    }
}
