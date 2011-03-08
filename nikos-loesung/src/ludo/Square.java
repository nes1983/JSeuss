package ludo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.google.inject.Inject;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Cell in a linked list, holding zero or more stones.
 *<P> 
 * Stones move from one square to the next, depending on the square's type and the stone's color.
 * Most of the game logic is implemented in this class and its subclasses.
 * The movement of stones uses <i>design by contract</i> and <i>recursion</i>. 
 * Whenever a stone wants to move, it must first ask its current square whether it can move,
 * see {@link #hasNext(int, Color)}.
 * If and only if the answer is <tt>true</tt> the stone may actually advance on the board,
 * see {@link #next(int, Color)}.
 * Both requests are forwarded from stone to stone using recursion, a technique similar to induction in mathematics.
 * Therefore each request includes the number of remaining steps: 
 * if no steps remains the receiving stone handles the request, 
 * else the number of remaining steps is decreases and request passed on to the next stone. 
 *<P>
 * To create squares, please use the {@link SquareBuilder} class. 
 *<P>
 * @author Adrian Kuhn, 2007-2009 
 * @author Niko Schwarz, 2010 
 */
public class Square {

	public static final int NONE = -1;
	
    private Square next;
	private Collection<Stone> occupants;

	/** Creates a new square, but does <em>not</em> establish the invariant!
     * The link to the next square remains unset.
     * Please call <tt>#add</tt> to establish the invariant.
     * 
     * @see #add(Square)
     */
	@Inject
	Square(LinkedList<Stone> occupants) {
	    this.occupants = checkNotNull(occupants);
	}	
	
    /** Links this square to the next square. Establishes the invariant.
	 * 
	 * @throws AssertionError when called more than once.
	 * 
	 */
	protected Square add(Square next) {
	    assert this.next == null; 
	    return this.next = next;
	}
	
	/** Removes stone from this square and puts stone on given target square.
	 *<P>
	 * If the target square contains a single opposing stone, the opposing stone is sent back to its start.
	 * If the target square contains two or more opposing stones, the given stone is
	 * (instead of being put on the target square) sent back to start
	 *<P> 
     * @throws AssertionError if the link invariant is not established. 
     * @throws AssertionError if the given stone is not on this square. 
	 */
    public void advance(Stone stone, Square target) {
        assert invariantOfLinks();
        if (this == target) return;
        assert target != null;
        assert occupants.contains(stone);
        occupants.remove(stone);
        target.put(stone);
        assert invariantOfOccupants();
    }
	
	/** Returns any occupant (or fails).
     * 
     * @throws NoSuchElementException if there are no stones on this square.
     */
    public Stone anyStone() {
        return this.getOccupants().iterator().next();
    }
	
	/** Returns the next square.
	 *<P>
	 * This class disregards the given color, but subclass {@link BranchSquare} does not! 
	 * 
	 */
    protected Square chooseNext(Color color) {
        return next();
    }
	
	public boolean contains(Stone stone) {
        return occupants.contains(stone);
    }

	/** Returns the number of occupants that are <em>not</em> of same color as given stone.
     * 
     */
    public int countOpponents(Stone stone) {
    	// NOTE assumes that all stones are of the same color, 
    	// as is asserted in #invariantOfOccupants.
        if (occupants.isEmpty()) return NONE;
        if (anyStone().getColor() == stone.getColor()) return NONE;
        return occupants.size();
    }
    
    /** Checks if a stone can move given number of squares.
     * The result may depend on the stone's color.
	 *<P>
	 * Implements <em>design by contract</em>.
	 * Clients must call this method to check if a call to {@link #next(int,Color)} is valid.
	 *<P> 
     * @throws AssertionError if the link invariant is not established. 
	 */
	public boolean hasNext(int steps, Color color) {
        assert invariantOfLinks();
		if (steps == 0) return true;
		return chooseNext(color).hasNext(steps - 1, color);
	}
	
    /** Counts the number of steps until given stone is reached.
     *<P>  
     * @return {@link Square#NONE} if given stone is not found.
     */
    public int indexOf(Stone stone) {
        if (this.contains(stone)) return 0;
        int index = chooseNext(stone.getColor()).indexOf(stone);
        return index == NONE ? NONE : index + 1;
    }
    
    protected boolean invariantOfLinks() {
        return next != null;
    }
    
    protected boolean invariantOfOccupants() {
        if (occupants.isEmpty()) return true;
        Color sameColor = anyStone().getColor();
        for (Stone each: occupants) if (each.getColor() != sameColor) return false;
        for (Stone each: occupants) if (each.location() != this) return false;
        return true;
    }

    public void add(Stone stone) {
    		occupants.add(stone);
        assert invariantOfOccupants();
    }

    public Square next() {
	    return next;
	}
    
    /** Returns the target square of a stone that moves given number of squares.
	 * The result may depend on the stone's color.
     *<P>
     * Implements <em>design by contract</em>.
     * Clients must call {@link #hasNext(int,Color)} to check if a call to this methods is valid.
     *<P> 
     * @throws AssertionError if the link invariant is not established. 
     * @throws AssertionError if the stone cannot move (ie contract violation). 
     */
	public Square next(int steps, Color color) {
	    assert invariantOfLinks();
	    if (steps == 0) return this;
	    return chooseNext(color).next(steps - 1, color);
	}
    
    public Collection<Stone> getOccupants() {
        return occupants;
    }

    private void put(Stone stone) {
        int opponents = countOpponents(stone);
        if (opponents == 1) anyStone().restart();
        occupants.add(stone);
        if (opponents > 1) stone.restart();
        assert invariantOfOccupants();
    }
}
