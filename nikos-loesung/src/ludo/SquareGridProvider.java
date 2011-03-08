package ludo;

import static ludo.SquareGrid.Heading.DOWN;
import static ludo.SquareGrid.Heading.LEFT;
import static ludo.SquareGrid.Heading.RIGHT;
import static ludo.SquareGrid.Heading.UP;

import java.util.Iterator;
import java.util.List;

import ludo.SquareGrid.Heading;
import ludo.SquareGrid.Turtle;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provider for the SquareGrid.
 * @author Niko Schwarz, 2010
 * @see SquareGrid
 */
public class SquareGridProvider implements Provider<SquareGrid>{

	private final int GRID_SIZE = 15;
	private final List<StartSquare> startSquares;
	
	@Inject
	SquareGridProvider(@StartSquares List<StartSquare> startSquares) {
		this.startSquares = startSquares;
	}

	@Override
	public SquareGrid get() {
		SquareGrid grid = new SquareGrid(new Square[GRID_SIZE][GRID_SIZE]);
		populateGrid(grid, startSquares.iterator());
		return grid;
	}
	
	public void populateGrid(SquareGrid grid, Iterator<StartSquare> starts) {
		quarter(makeTurtle(grid, 5, 1, DOWN, starts.next()));
		quarter(makeTurtle(grid, 1, 9, LEFT, starts.next()));
		quarter(makeTurtle(grid, 9, 13, UP, starts.next()));
		quarter(makeTurtle(grid, 13, 5, RIGHT, starts.next()));
	}
	
	public static Turtle makeTurtle(SquareGrid grid, int row, int column, Heading heading, Square next) {
		return  grid.new Turtle(row, column, heading, next);
	}

	private static void quarter(Turtle turtle) {
		turtle.move(1).turnLeft().move(5).turnLeft().skip().move(5).turnRight()
				.move(1).branchHere().move(2)
				// branch
				.gotoBranch().turnRight().skip().move(5).skip().move(1);
	}

}
