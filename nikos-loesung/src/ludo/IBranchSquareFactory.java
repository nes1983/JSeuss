package ludo;

/**
 * Factory that creates {@link BranchSquare}s.
 * @author Niko Schwarz, 2010 
 * 
 */

public interface IBranchSquareFactory {
	public BranchSquare create(Color color);
}
