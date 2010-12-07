package ch.unibnf.scg.jseuss.core.javaassist.guice.test;

import static org.junit.Assert.*;
import generated.guice.SpellCheckerInterface;
import generated.guice.SpellCheckerProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.unibnf.scg.jseuss.core.javaassist.guice.GuiceBinderGenerator;

public class GuiceBinderGeneratorTester {

	Class<?> interfaceClass;
	Class<?> providerClass;

	@Before
	public void setUp() throws Exception {
		interfaceClass = SpellCheckerInterface.class;
		providerClass = SpellCheckerProvider.class;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateGuiceBinder() {
		if (interfaceClass != null && providerClass != null) {
			Class<?> theBinder = GuiceBinderGenerator.generateGuiceBinder(
					providerClass, interfaceClass,
					"generated.guice.SpellCheckerBinder", false);
			assertTrue(theBinder != null);
		} else {
			fail("Please initialize providerClass and interfaceClass in the Setup() method.");
		}
	}

}
