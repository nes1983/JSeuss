package ludo;

import java.util.LinkedList;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.internal.Nullable;
import com.google.inject.name.Named;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Factory that creates start squares when given their color.
 * @author Niko Schwarz, 2010
 * @see StartSquare
 */
public class StartSquareFactory {
	
	private final Provider<LinkedList<Stone>> occupantsProvider;
	private final int FIRST_STEP;
	private final int NUMBER_OF_STONES_ON_STARTFIELD;
	private final StoneFactory stoneFactory;

	@Inject
	StartSquareFactory(
			Provider<LinkedList<Stone>> occupantsProvider, 
			@Named("FIRST_STEP") int FIRST_STEP,
			@Named("NUMBER_OF_STONES_ON_STARTFIELD") int NUMBER_OF_STONES_ON_STARTFIELD,
			StoneFactory stoneFactory) {
		this.occupantsProvider = checkNotNull(occupantsProvider);
		this.FIRST_STEP = FIRST_STEP;
		this.NUMBER_OF_STONES_ON_STARTFIELD = NUMBER_OF_STONES_ON_STARTFIELD;
		this.stoneFactory = checkNotNull(stoneFactory);
	}
	
	/**
	 * Creates the StartSquare.
	 * @param the color of the player that owns this field.
	 * @return the created start square.
	 */
	public StartSquare create(@Nullable Color color) {
		StartSquare ss = new StartSquare(occupantsProvider.get(), FIRST_STEP);
        for (int n = 0; n < NUMBER_OF_STONES_ON_STARTFIELD; n++) makeAndAddStone(ss, color);
		return ss;
	}
	
	private void makeAndAddStone(StartSquare ss, @Nullable Color color) {
        assert ss.getOccupants().isEmpty() || color == null || ss.anyStone().getColor().equals(color);
        stoneFactory.create(ss, color); //Adds the stone to ss.
    }
 }
