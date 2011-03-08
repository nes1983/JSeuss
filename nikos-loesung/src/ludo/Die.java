package ludo;

/**
 * Stream of random numbers between 1 and 6.
 *<P>
 * 
 * @author Adrian Kuhn, 2007
 * @author Niko Schwarz, 2010 
 * 
 */
public class Die implements IDie {

	private static final int FACES = 6;

	/**
	 * 
	 * 
	 * @see ludo.IDie#roll()
	 */
	public int roll() {
		return (int) (Math.random() * FACES) + 1;
	}

	@Override
	public void setNextRoll(int nextFaceToShow) {
		throw new UnsupportedOperationException();
	}

}
