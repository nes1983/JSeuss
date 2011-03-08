package ludo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

/** Each player knows its stones. Players are created by the game.
 *<P>
 * @see {@link Game#addPlayer()}
 *<P>
 * @author Adrian Kuhn, 2007
 * @author Niko Schwarz, 2010 
 *
 */
public class Player implements IPlayer {

    private final Collection<Stone> stones;
    
    /*
     * Created by PlayerFactory
     */
    Player(Collection<Stone> stones) {
    		this.stones = checkNotNull(stones);
        assert invariant();
    }
    
    private boolean invariant() {
        // TODO check that all stones are of the same color
        return stones != null && !stones.isEmpty();
    }

    /* (non-Javadoc)
	 * @see ludo.IPlayer#isWinner()
	 */
    public boolean isWinner() {
        for (Stone each: stones) if (!each.atGoal()) return false;
        return true;
    }
    
    /* (non-Javadoc)
	 * @see ludo.IPlayer#move(int)
	 */
    public void move(int dice) {
        // TODO implement better strategy
        for (Stone stone: stones()) {
            if (stone.canMove(dice)) {
                stone.move(dice);
                return;
            }
        }
    }
    
    /* (non-Javadoc)
	 * @see ludo.IPlayer#stones()
	 */
    public Iterable<Stone> stones() {
        return stones;
    }
    
    /* (non-Javadoc)
	 * @see ludo.IPlayer#toString()
	 */
    @Override
    public String toString() {
        return "Player " + stones.iterator().next().getColor();
    }

	@Override
	public Stone anyStone() {
		return stones.iterator().next();
	}
    
}
