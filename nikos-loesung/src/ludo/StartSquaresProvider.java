package ludo;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provider for start squares in a ludo game. 
 * @author Niko Schwarz, 2010
 *
 */
public class StartSquaresProvider implements Provider<List<StartSquare>> {
	
	private final SquareBuilder squareBuilder;

	@Inject
	StartSquaresProvider(SquareBuilder squareBuilder) {
		this.squareBuilder =  squareBuilder;
	}
	

	@Override
	public List<StartSquare> get() {
		for (Color color : Color.values()) {
			squareBuilder.branch(color).squares(5).goal().endOfBranch()
			// branch
					.squares(2).startHere(color).squares(10);
		}
		return squareBuilder.closeRing().getStartSquares();
	}

}
