package ludo;

import java.util.LinkedList;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Start square on a ludo board game. Stones must roll a 5 to leave this square.
 *<P> 
 * @author Adrian Kuhn, 2007
 * @author Niko Schwarz, 2010
 *
 */
public class StartSquare extends Square {

	private final int FIRST_STEP ;

	@Inject
    StartSquare(LinkedList<Stone> occupants, @Named("FIRST_STEP") int FIRST_STEP) {
		super(occupants);
		this.FIRST_STEP = FIRST_STEP;
	}


    @Override
    public boolean hasNext(int steps, Color color) {
        assert invariantOfLinks();
        return steps == FIRST_STEP;
    }

    @Override
    public Square next(int steps, Color color) {
        assert invariantOfLinks();
        return next();
    }

}
