package ludo;

import com.google.inject.Inject;
import com.google.inject.internal.Nullable;

/**
 * Factory for stones in a ludo game that requires the starting square that the
 * square should stand on. 
 * 
 * @author Niko Schwarz, 2010
 * 
 */
public class StoneFactory {

	@Inject
	StoneFactory() {}
	
	public Stone create(Square square, @Nullable Color color) {
		Stone s = new Stone(square, color);
		square.add(s);
		return s;
	}

	/**
	 * Answers a newly created stone that gets put on <code>square</square>.
	 * @param square the square that the new stone is put on.
	 * @return the newly created stone.
	 */
	public Stone create(Square square) {
		return this.create(square, null);
	}
}
