import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameState {
    private final List<CardsPile> allPiles = new ArrayList<>();
    private final DrawPile drawPile;
    private final FinishedPile finishedPile;

    private final long moves;
    private final long points;

    public GameState(List<CardsPile> allPiles,
                     DrawPile drawPile,
                     FinishedPile finishedPile,
                     long moves,
                     long points) {
        for (CardsPile pile : allPiles) {
            this.allPiles.add(pile.clone());
        }
        this.drawPile = (DrawPile) drawPile.clone();
        this.finishedPile = (FinishedPile) finishedPile.clone();
        this.moves = moves;
        this.points = points;
    }

    public List<CardsPile> getAllPiles() {
        return allPiles;
    }

    public DrawPile getDrawPile() {
        return drawPile;
    }

    public FinishedPile getFinishedPile() {
        return finishedPile;
    }

    public long getMoves() {
        return moves;
    }

    public long getPoints() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameState gameState = (GameState) o;
        return moves == gameState.moves &&
                points == gameState.points &&
                Objects.equals(allPiles, gameState.allPiles) &&
                Objects.equals(drawPile, gameState.drawPile) &&
                Objects.equals(finishedPile, gameState.finishedPile);
    }
}
