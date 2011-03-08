package ludo;

import com.google.inject.internal.Nullable;

/**
 * Non-random die useful for testing.
 * @author Niko Schwarz, 2010 
 * @see IDie
 */
public class MockDie implements IDie {

	private @Nullable Integer nextFaceToShow;
	
	MockDie() {
		nextFaceToShow = null;
	}
	
	@Override
	public int roll() {
		assert nextFaceToShow != null;
		return nextFaceToShow;
	}

	@Override
	public void setNextRoll(int nextFaceToShow)
			throws UnsupportedOperationException {
		this.nextFaceToShow = nextFaceToShow;
	}
}
