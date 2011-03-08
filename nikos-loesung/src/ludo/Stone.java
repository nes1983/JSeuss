package ludo;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.internal.Nullable;

/**
 * Stone on a ludo game board. This stone moves from one square to the next
 * until it either reaches the goal or the game ends. Created by {@link StoneFactory}.
 * Stones know their color, but they don't need to have one.
 * 
 * @author Adrian Kuhn, 2007
 * @author Niko Schwarz, 2010
 * 
 */
public class Stone {

	private @Nullable final Color color;
	private final Square start;
	private Square location;

	/*
	 * @See StoneFactory
	 */
	Stone(Square start, @Nullable Color color) {
		this.location = this.start = checkNotNull(start);
		this.color = color;
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

	public Square location() {
		return location;
	}
	
	public Color getColor() {
		return color;
	}

	private Square lookahead(int steps) {
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
