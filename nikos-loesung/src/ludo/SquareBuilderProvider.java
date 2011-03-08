package ludo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provider for the SquareBuilder
 * 
 * @author Niko Schwarz, 2010
 * @see SquareBuilder
 */
public class SquareBuilderProvider implements Provider<SquareBuilder> {

	private final Square origin;
	private final GoalSquare goalSquare;
	private final Provider<Square> squareProvider;
	private final StartSquareFactory startSquareFactory;
	private final IBranchSquareFactory branchSquareFactory;
	private final Provider<LinkedList<StartSquare>> startsProvider;

	@Inject
	SquareBuilderProvider(Square origin, GoalSquare goalSquare,
			Provider<Square> squareProvider,
			StartSquareFactory startSquareFactory,
			IBranchSquareFactory branchSquareFactory,
			Provider<LinkedList<StartSquare>> startsProvier) {
		this.origin = checkNotNull(origin);
		this.goalSquare = checkNotNull(goalSquare);
		this.squareProvider = checkNotNull(squareProvider);
		this.startSquareFactory = checkNotNull(startSquareFactory);
		this.branchSquareFactory = checkNotNull(branchSquareFactory);
		this.startsProvider = startsProvier;
	}

	@Override
	public SquareBuilder get() {
		List<StartSquare> startSquares = makeStartSquares();
		return new SquareBuilder(origin, goalSquare, startSquares,
				squareProvider, branchSquareFactory);

	}

	private List<StartSquare> makeStartSquares() {
		List<StartSquare> starts = startsProvider.get();
		for (Color color : Color.values()) {
			StartSquare start = startSquareFactory.create(color);
			starts.add(start);
		}

		return starts;
	}

}
