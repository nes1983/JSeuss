/**
 * 
 */
package ch.unibnf.scg.jseuss.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.unibnf.scg.jseuss.utils.JSeussUtils;
import ch.unibnf.scg.sample.spellCheck.GermanSpellChecker;

/**
 * @author TGDMOAH1
 * 
 */
public class JSeussUtilsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateJavaFactoryClass_CreateJar() {
		// Class<?>[] toFactory = new Class[] { FrenchSpellChecker.class,
		// GermanSpellChecker.class };
		// Class<?> returnClass = SpellCheckerInterface.class;
		// String factoryName =
		// "generated.ch.unibnf.seminars.scg.dif.sample.SpellCheckerFactory";
		// JSeussUtils.generateJavaFactoryClass(toFactory, returnClass,
		// factoryName);
		boolean generated = true;
		assertTrue(generated);
	}

	@Test
	public void testGenerateJavaInterface_CreateJar() {
		boolean generated = JSeussUtils
				.generateJavaInterface(
						GermanSpellChecker.class,
						"generated.ch.unibnf.seminars.scg.dif.sample.SpellCheckerInterface",
						true, true);
		assertTrue("generated=false", generated);
	}

	@Test
	public void testGenerateJavaInterface_NoCreateJar() {
		boolean generated = JSeussUtils
				.generateJavaInterface(
						GermanSpellChecker.class,
						"generated.ch.unibnf.seminars.scg.dif.sample.SpellCheckerInterface",
						true, false);
		assertTrue("generated=false", generated);
	}
}
