package ch.unibnf.scg.jseuss.core.javaassist.guice.test;

import static org.junit.Assert.*;
import generated.guice.SpellCheckerInterface;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.unibnf.scg.jseuss.core.javaassist.guice.GuiceProviderGenerator;
import ch.unibnf.scg.sample.spellCheck.FrenchSpellChecker;

public class GuiceProviderGeneratorTester {
	
	Class<?> interfaceClass;
	Class<?> newVarClass;

	@Before
	public void setUp() throws Exception {
		interfaceClass = SpellCheckerInterface.class;
		newVarClass = FrenchSpellChecker.class;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateGuiceProvider() {
		if (newVarClass != null && interfaceClass != null) {
			Class<?> theProvider = GuiceProviderGenerator
					.generateGuiceProvider(newVarClass, interfaceClass,
							"generated.guice.SpellCheckerProvider", false);
			assertTrue(theProvider != null);
		} else {
			fail("please initialize interfaceClass and newVarClass in the Setup() method.");
		}
	}

}
