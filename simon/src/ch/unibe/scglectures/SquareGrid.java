package ch.unibe.scglectures;

import com.google.inject.Inject;
import com.google.inject.Provider;

import ch.unibe.util.ILength;
import ch.unibe.util.Length;

/**
 * Prints the Ludo board as a grid of squares.
 *<P> 
 * This class implements another domain specific language (DSL).
 * There is a turtle that can be scripted to put squares on the grid.
 * The turtle knows the sequence of squares and can do three things:
 * <ul>
 * <li>It can turn 90 degrees.</li>
 * <li>It can advance one cell.</li>
 * <li>It can put the next square on the current cell, and advance one cell.</li>
 * </ul>
 *<P> 
 * Actually, the turtle can do two more things:
 * <ul>
 * <li>It can memorize the begin of a branch.</li>
 * <li>It can go back to that position.</li>
 * </ul>
 *<P> 
 * The code in Ludo puts the turtle four times on the grid,
 * and executes the same sequence of operations four times.
 * Once for each quarter of the game board.
 *<pre>
 * turtle
 *     .move(1)
 *     .turnLeft()
 *     .move(5)
 *     .turnLeft()
 *     .skip()
 *     .move(5)
 *     .turnRight()
 *     .move(1)
 *     .branchHere()
 *     .move(2)
 * .gotoBranch()
 *     .turnRight()
 *     .skip()
 *     .move(5)
 *     .skip()
 *     .move(1);
 *</pre> 
 *<P>
 * This little DSL is obviously inspired by the LOGO programming language. 
 *<P>
 * @see http://en.wikipedia.org/wiki/Logo_(programming_language)
 *<P> 
 * @author Adrian Kuhn, 2009
 * 
 */
public class SquareGrid implements ISquareGrid {
	
	 @Inject ILength length;
	
	// TODO was tun? Turtle braucht genau dieses grid auch
	private ISquare[][] grid;
	@Inject
	private Provider<ITurtle> turtleProvider;
	@Inject
	private Provider<StringBuilder> stringBuilderProvider;

	public SquareGrid(int rows, int columns) {
		// TODO new provider
		this.grid = new Square[rows][columns];
	}

	public SquareGrid() {
	}
	public void setRowsAndColumns(int rows, int columns) {
		// TODO new provider
		this.grid = new Square[rows][columns];
	}

	public ISquare at(int row, int column) {
		return grid[row][column];
	}

	public ITurtle getTurtle(int row, int column, Heading heading, ISquare next) {
		ITurtle t = turtleProvider.get();
		t.setRowAndColumnAndHeadingAndNext(row, column, heading, next);
		return t;
	}
	
	@Override
	public String toString() {
		StringBuilder buf = stringBuilderProvider.get();;
		for (int row = 0; row < grid.length; row++) printRow(grid[row], buf);
		return buf.toString();
	}

	public void printRow(ISquare[] row, StringBuilder buf) {
		for (int column = 0; column < row.length; column++) printCell(row[column], buf);
		buf.append('\n');
	}

	public void printCell(ISquare square, StringBuilder buf) {
		if (square == null) { buf.append("    "); return; }
		// TODO static access
		int size = length.of(square.occupants());
		buf.append("[");
		buf.append(size > 0 ? square.anyStone().getColor().toString().charAt(0) : ' ');
		buf.append(size > 1 ? (char) ('0' + size) : ' ');
		buf.append(']');
	}

	public enum Heading {

		LEFT(0, -1), DOWN(1, 0), RIGHT(0, 1), UP(-1, 0);

		public final int columns;
		public final int rows;

		private Heading(int rows, int columns) {
			this.rows = rows;
			this.columns = columns;
		}

		public Heading left() {
			return values()[(this.ordinal() + 1) % values().length];
		}

		public Heading right() {
			return values()[(this.ordinal() + values().length - 1) % values().length];
		}

	}

}
