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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.drawRect((Game.windowWidth-200)/2, Game.windowHeight-200,200,100);
        g.setColor(new Color(0x075c1d));
        g.fillRect((Game.windowWidth-200)/2+1, Game.windowHeight-200+1,199,99);
        g.setColor(Color.WHITE);
        g.drawString("Score: "+ game.points, (Game.windowWidth-100)/2, Game.windowHeight-150);
        g.drawString("Moves: "+ game.moves, (Game.windowWidth-100)/2, Game.windowHeight-135);

        paintPilesHolders(g);

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
