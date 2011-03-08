package ludo;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Factory that creates the ludo board game.
 * @author Niko Schwarz, 2010 
 *
 */
public class LudoFactory {

	private final Provider<IDie> die;
	private final IPlayerFactory playerFactory;
	private final Provider<SquareGrid> squareGrid;
	private final @StartSquares Provider<List<StartSquare>> startSquares;
	private final Provider<PrintStream> out;
	private final Provider<LinkedList<IPlayer>> players;
	
	@Inject
	LudoFactory(Provider<IDie> die, IPlayerFactory playerFactory,
			Provider<SquareGrid> squareGrid,
			@StartSquares Provider<List<StartSquare>> startSquares,
			Provider<PrintStream> out, Provider<LinkedList<IPlayer>> players) {
		super();
		this.die = die;
		this.playerFactory = playerFactory;
		this.squareGrid = squareGrid;
		this.startSquares = startSquares;
		this.out = out;
		this.players = players;
	}

	/**
	 * Creates the ludo board game.
	 * @param numberOfPlayers the number of players that will be added to the game.
	 * @return the freshly created ludo board game.
	 */
	public Ludo create(int numberOfPlayers) {
		List<StartSquare> ss = startSquares.get();
		Iterator<StartSquare> freeStarts = ss.iterator();
		LinkedList<IPlayer> ps = players.get();
		Ludo ret = new Ludo(out.get(), ps, die.get(), ss, squareGrid.get());
		for (int i = 0; i < numberOfPlayers; i++) {
			ps.add(makePlayer(freeStarts));
		}
		return ret;
	}

	private IPlayer makePlayer(Iterator<StartSquare> freeStarts) {
		assert freeStarts.hasNext();
		return playerFactory.create(freeStarts.next());
	}
}
