package ch.unibe.scglectures;

import org.junit.Test;

public class AssertionEnabledTest {

	@Test(expected = AssertionError.class)
	public void assertionsEnabled() {
		assert false;
	}

}
