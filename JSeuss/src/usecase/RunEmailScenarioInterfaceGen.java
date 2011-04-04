package usecase;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.unibnf.scg.jseuss.core.javaassist.guice.JavaInterfaceGenerator;

public class RunEmailScenarioInterfaceGen {
	Class<?> localVarClass;

	@Before
	public void setUp() throws Exception {
		localVarClass = FrenchSpellChecker.class;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateJavaInterface() throws CannotCompileException, NotFoundException, IOException {
		if (localVarClass != null) {
			Class<?> theInterface = JavaInterfaceGenerator
					.generate(localVarClass,
							"generated.guice.usecase.I" + localVarClass.getSimpleName());
			assertTrue(theInterface != null);
		} else {
			fail("Please initialize localVarClass in the Setup() method.");
		}
	}
}
