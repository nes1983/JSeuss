package ch.unibnf.scg.jseuss.core.javaassist.guice.test;

import static org.junit.Assert.*;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import usecase.GermanSpellChecker;

import ch.unibnf.scg.jseuss.core.javaassist.guice.JavaInterfaceGenerator;

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
	public void testGenerateJavaInterface() throws CannotCompileException, NotFoundException, IOException {
		if (localVarClass != null) {
			Class<?> theInterface = JavaInterfaceGenerator
					.generate(localVarClass,
							"generated.guice.SpellCheckerInterface");
			assertTrue(theInterface != null);
		} else {
			fail("Please initialize localVarClass in the Setup() method.");
		}
	}
}
