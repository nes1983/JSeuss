package ludo;

/**
 * Die that provides a random integer when rolled. A die has a number of faces,
 * starting from one up to some integer <i>n</i> and rolls integers from the
 * range [1,<i>n</i>] uniformly at random, which stands for the face that came
 * atop after rolling.
 * <p>
 * If you wish to test an application that uses an IDie, you can inject a
 * {@link MockDie} into your application that will deterministically return the
 * integer that you pass into {@link setNextRoll}.
 * 
 * 
 * @author Niko Schwarz, 2010 
 * 
 */
public interface IDie {

	/**
	 * Answers a random integer out of the range [1,<i>n</i>], where <i>n</i> is
	 * an integer that depends on the die.
	 * 
	 * @return the face that lay atop after rolling the die.
	 */
	public int roll();

	/**
	 * Ensures that the next returned value from {@link roll} will be
	 * <code>nextFaceToShow</code> if this IDie is a mock, otherwise throws an
	 * <code>UnsupportedOperationException</code>. For example, the following
	 * assertion will hold.
	 * 
	 * <pre>
	 * <code>
	 * IDie die = new MockDie();
	 * die.setNextRoll(3);
	 * assert die.roll() == 3;
	 * </code>
	 * </pre>
	 * 
	 * @see MockDie
	 * @param nextFaceToShow
	 *            the face that {@link roll} will return next.
	 * @throws UnsupportedOperationException
	 *             If this is is a random die and not a mock die.
	 */
	@ForTestingOnly
	public void setNextRoll(int nextFaceToShow)
			throws UnsupportedOperationException;

}