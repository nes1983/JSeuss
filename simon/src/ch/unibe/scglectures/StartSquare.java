package ch.unibe.scglectures;

/**
 * Stones must roll a 5 to leave this square.
 *<P> 
 * @author Adrian Kuhn, 2007
 *
 */
public class StartSquare extends Square implements IStartSquare {

    public static final int FIRST_STEP = 5;

    @Override
    public boolean hasNext(int steps, Color color) {
        assert invariantOfLinks();
        return steps == FIRST_STEP;
    }

    @Override
    public ISquare next(int steps, Color color) {
        assert invariantOfLinks();
        assert steps == FIRST_STEP;
        return next();
    }

}
