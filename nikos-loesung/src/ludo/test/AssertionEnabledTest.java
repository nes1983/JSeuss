package ludo.test;

import org.junit.Test;

public class AssertionEnabledTest {

	@Test(expected = AssertionError.class)
	public void assertionsShouldBeEnabled() {
		assert false;
	}

}
