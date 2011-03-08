package ludo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Factory that creates players for a board game.
 * @author Niko Schwarz, 2010 
 * @see IPlayerFactory
 */
public class PlayerFactory implements IPlayerFactory {

	private final Provider<LinkedList<Stone>> stonesListProvider;

	@Inject
	PlayerFactory(Provider<LinkedList<Stone>> stonesListProvider) {
		this.stonesListProvider = checkNotNull(stonesListProvider);
	}
	
	@Override
	public Player create(Square startSquare) {
		LinkedList<Stone> stones = stonesListProvider.get();
		for (Stone each: startSquare.getOccupants()) {
			stones.add(each);
		}
		return new Player(stones);
	}

}
