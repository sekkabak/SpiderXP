import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.Math.abs;

public class CustomMouseAdapter extends MouseAdapter {
    Game game;
    GamePanel gamePanel;

    int lastX, lastY;

    public CustomMouseAdapter(Game game, GamePanel gamePanel) {
        super();
        this.game = game;
        this.gamePanel = gamePanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        int x = e.getX();
        int y = e.getY();

        // check for card selection
        for (CardsPile pile : game.allPiles) {
            for (int i = pile.pile.size(); i > 0; i--) {
                pile.getCard(i).contains(x, y);
            }
        }

        // check for draw request
        // TODO

        game.dragPile.translate(x - lastX, y - lastY);
        lastX = x;
        lastY = y;

        gamePanel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        game.dragPile.removeCard(0);
        gamePanel.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!game.dragPile.isEmpty()) {
            int x = e.getX();
            int y = e.getY();
            game.dragPile.translate(x - lastX, y - lastY);
            lastX = x;
            lastY = y;

            gamePanel.repaint();
        }
    }
}
