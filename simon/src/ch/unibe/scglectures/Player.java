package ch.unibe.scglectures;

import java.util.Collection;
import java.util.LinkedList;

import com.google.inject.Inject;
import com.google.inject.Provider;

/** Each player knows its stones. Players are created by the game.
 *<P>
 * @see {@link Game#addPlayer()}
 *<P>
 * @author Adrian Kuhn, 2007
 *
 */
public class Player implements IPlayer {

    private Collection<IStone> stones;
    @Inject
	private Provider<Collection<IStone>> linkedListStoneProvider;
    
    public Player(ISquare start) {
        this.stones = linkedListStoneProvider.get();
        for (IStone each: start.occupants()) stones.add(each);
        assert invariant();
    }
    
    public boolean invariant() {
        // TODO check that all stones are of the same color
        return stones != null && !stones.isEmpty();
    }

    public boolean isWinner() {
        for (IStone each: stones) if (!each.atGoal()) return false;
        return true;
    }
    
    public void move(int dice) {
        // TODO implement better strategy
        for (IStone stone: stones()) {
            if (stone.canMove(dice)) {
                stone.move(dice);
                return;
            }
        }
    }
    
    public Iterable<IStone> stones() {
        return stones;
    }
    
    @Override
    public String toString() {
        return "Player " + stones.iterator().next().getColor();
    }

	@Override
	public void setStartSquare(IStartSquare start) {
		this.stones = linkedListStoneProvider.get();
        for (IStone each: start.occupants()) stones.add(each);
        assert invariant();
	}
    
}
