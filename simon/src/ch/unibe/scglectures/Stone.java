package ch.unibe.scglectures;

import com.google.inject.Inject;

/** Stones move from one square to the next.
 *<P> 
 * @see {@link Square#makeStone(Color)}
 *<P> 
 * @author Adrian Kuhn, 2007
 *
 */
public class Stone implements IStone {

    private Color color;
    private ISquare location, start;
    
    @Inject
    public Stone(ISquare start, Color color) {
        this.location = this.start = start;
        this.color = color;
    }
    
    public Stone() {
	}
	@Override
	public void setStartAndColor(ISquare start, Color color) {
		this.location = this.start = start;
        this.color = color;
	}

	public Color getColor() {
    	return this.color;
    }
    
    public boolean atGoal() {
        return location instanceof GoalSquare;
    }

    public boolean atStart() {
        return location instanceof StartSquare;
    }

    public boolean canMove(int steps) {
        return location.hasNext(steps, this.color);
    }
    
    public ISquare location() {
        return location;
    }

    public ISquare lookahead(int steps) {
        return location.next(steps, this.color);
    }    
    
    public void move(int steps) {
        assert canMove(steps);
        location.advance(this, location = lookahead(steps));
        assert location.contains(this);
    }
    
    public void restart() {
        location.advance(this, location = start);
        assert location.contains(this);
    }
    
}
