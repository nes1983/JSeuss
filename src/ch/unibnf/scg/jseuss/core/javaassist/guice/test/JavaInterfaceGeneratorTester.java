package ch.unibnf.scg.jseuss.core.javaassist.guice.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.unibnf.scg.jseuss.core.javaassist.guice.JavaInterfaceGenerator;
import ch.unibnf.scg.sample.spellCheck.GermanSpellChecker;

public class JavaInterfaceGeneratorTester {
	Class<?> localVarClass;

	@Before
	public void setUp() throws Exception {
		localVarClass = GermanSpellChecker.class;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateJavaInterface() {
		if (localVarClass != null) {
			Class<?> theInterface = JavaInterfaceGenerator
					.generateJavaInterface(localVarClass,
							"generated.guice.SpellCheckerInterface", false);
			assertTrue(theInterface != null);
		} else {
			fail("Please initialize localVarClass in the Setup() method.");
		}
	}
}
