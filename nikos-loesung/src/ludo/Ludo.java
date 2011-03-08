package ludo;

import java.io.PrintStream;
import java.util.List;

/**
 * Runs a ludo game.
 * <P>
 * Uses two little languages to setup the board, see the static methods in the
 * class body.
 * 
 * @author Adrian Kuhn, 2008-2009
 * @author Niko Schwarz, 2010 
 * 
 */
public class Ludo extends Game {

	Ludo(PrintStream out, List<IPlayer> players, IDie die,
			List<StartSquare> starts, SquareGrid grid
			) {
		super(die, out, players);
		this.starts = starts;
		this.grid = grid;
	}

	private final List<StartSquare> starts;
	private final SquareGrid grid;


	public List<StartSquare> getStarts() {
		return starts;
	}

	public SquareGrid getGrid() {
		return grid;
	}

	public StartSquare getStartSquare(Color color) {
		return starts.get(color.ordinal());
	}
	
	@Deprecated
	public void nextTurn(int steps) {
		this.getCurrentPlayer().move(steps);
		if (steps == 6)
			return; // play again!
		this.switchPlayer();
	}

	@Override
	public void playNextTurn() {
		int steps = getDie().roll();
		nextTurn(steps);
	}

	@Override
	public void startGame() {
		super.startGame();
	}

	@Override
	public String toString() {
		return grid.toString();
	}

}
