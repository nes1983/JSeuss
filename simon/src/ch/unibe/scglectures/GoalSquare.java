package ch.unibe.scglectures;

/**
 * Stones cannot move further, must roll exact number to reach this square.
 *<P> 
 * @author Adrian Kuhn, 2007 
 *
 */
public class GoalSquare extends Square implements IGoalSquare {

    /** Creates a new square and establishes the invariant.
     * 
     */
    public GoalSquare() {
        super();
    }
    
    @Override
    public int countOpponents(IStone stone) {
        return NONE;
    }

    @Override
    public boolean hasNext(int steps, Color color) {
        assert invariantOfLinks();
        return steps == 0;
    }
    
    @Override
    public int indexOf(IStone stone) {
        return this.contains(stone) ? 0 : NONE;
    }

    @Override
	public boolean invariantOfLinks() {
        return true;
    }

    @Override
	public boolean invariantOfOccupants() {
        for (IStone each: occupants()) if (each.location() != this) return false;
        return true;
    }

    @Override
    public ISquare next(int steps, Color color) {
        assert invariantOfLinks();
        assert steps == 0;
        return this;
    }
    
}
