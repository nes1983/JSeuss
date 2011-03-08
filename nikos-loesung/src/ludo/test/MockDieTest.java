package ludo.test;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;
import static org.junit.Assert.*;
import com.google.inject.Guice;
import com.google.inject.Injector;

import ludo.*;

@RunWith(JExample.class)
public class MockDieTest {
	
	
	private final Injector injector = Guice.createInjector();
	
	@Test
	public MockDie newMockDie() {
		return injector.getInstance(MockDie.class);
	}
	
	@Given("#newMockDie")
	public void shouldReturnWhatWasSetBefore(MockDie die) {
		die.setNextRoll(4);
		assertEquals(die.roll(),4);
		die.setNextRoll(1);
		assertEquals(die.roll(),1);
	}
	
    @Test(expected=AssertionError.class)
	@Given("#newMockDie")
	public void shouldFailToRollUnlessSet(MockDie die) {
		die.roll();
	}
}
