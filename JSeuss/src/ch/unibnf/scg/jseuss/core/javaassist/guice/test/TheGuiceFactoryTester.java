package ch.unibnf.scg.jseuss.core.javaassist.guice.test;

import static org.junit.Assert.*;
import generated.guice.SpellCheckerInterface;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.unibnf.scg.jseuss.core.javaassist.guice.TheGuiceFactory;
import ch.unibnf.scg.sample.emailservice.EmailSender;
import ch.unibnf.scg.sample.spellCheck.FrenchSpellChecker;
import ch.unibnf.scg.sample.spellCheck.GermanSpellChecker;

public class TheGuiceFactoryTester {

	private TheGuiceFactory guiceFactory;

	@Before
	public void setUp() throws Exception {
		guiceFactory = new TheGuiceFactory("generated.myjseuss", false);
	}

	@After
	public void tearDown() throws Exception {
		guiceFactory = null;
	}

	@Test
	public void testMakeJavaInterface() {
		String interfaceName = "NewSpellCheckerInterface";
		boolean createJar = true;
		Class<?> i = guiceFactory.makeJavaInterface(GermanSpellChecker.class,
				interfaceName, createJar);
		if (i != null)
			System.out.println("generated: " + i.getName());
		assertTrue(i != null);
	}

	@Test
	public void testToGuiceCode() {
		Class<?> container = EmailSender.class;
		Class<?> target = GermanSpellChecker.class;
		Class<?> replacement = FrenchSpellChecker.class;
		Class<?> theInterface = SpellCheckerInterface.class;
		boolean done = guiceFactory.toGuiceCode(container, target, replacement,
				theInterface);
		assertTrue(done);
	}
}
