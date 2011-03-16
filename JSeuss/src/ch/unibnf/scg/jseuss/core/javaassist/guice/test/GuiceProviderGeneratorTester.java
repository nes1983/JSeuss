package ch.unibnf.scg.jseuss.core.javaassist.guice.test;

import static org.junit.Assert.*;
import generated.guice.SpellCheckerInterface;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.unibe.niko.FrenchSpellChecker;
import ch.unibnf.scg.jseuss.core.javaassist.guice.ClassDetails;
import ch.unibnf.scg.jseuss.core.javaassist.guice.GuiceProviderGenerator;

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
			ClassDetails theProvider = GuiceProviderGenerator
					.generate(newVarClass, interfaceClass,
							"generated.guice.SpellCheckerProvider", false);
			assertTrue(theProvider != null);
		} else {
			fail("please initialize interfaceClass and newVarClass in the Setup() method.");
		}
	}

}
