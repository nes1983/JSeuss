package ch.unibe.scglectures;

import java.util.Collection;
import java.util.LinkedList;

/** Each player knows its stones. Players are created by the game.
 *<P>
 * @see {@link Game#addPlayer()}
 *<P>
 * @author Adrian Kuhn, 2007
 *
 */
public class Player {

    private Collection<Stone> stones;
    
    public Player(Square start) {
        this.stones = new LinkedList<Stone>();
        for (Stone each: start.occupants()) stones.add(each);
        assert invariant();
    }
    
    private boolean invariant() {
        // TODO check that all stones are of the same color
        return stones != null && !stones.isEmpty();
    }

    public boolean isWinner() {
        for (Stone each: stones) if (!each.atGoal()) return false;
        return true;
    }
    
    public void move(int dice) {
        // TODO implement better strategy
        for (Stone stone: stones()) {
            if (stone.canMove(dice)) {
                stone.move(dice);
                return;
            }
        }
    }
    
    public Iterable<Stone> stones() {
        return stones;
    }
    
    @Override
    public String toString() {
        return "Player " + stones.iterator().next().color;
    }
    
}
