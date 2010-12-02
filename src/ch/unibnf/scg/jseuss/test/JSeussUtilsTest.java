/**
 * 
 */
package ch.unibnf.scg.jseuss.test;

import static org.junit.Assert.*;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.unibnf.scg.jseuss.core.JSeuss_Javaassist;
import ch.unibnf.scg.sample.emailservice.EmailSender;
import ch.unibnf.scg.sample.factory.FrenchSpellCheckerFactory;
import ch.unibnf.scg.sample.spellCheck.FrenchSpellChecker;
import ch.unibnf.scg.sample.spellCheck.GermanSpellChecker;

/**
 * @author TGDMOAH1
 * 
 */
public class JSeussUtilsTest {

	@Test
	public void testFactorizeLocalVariable_Jar() {
		Class containerClass = EmailSender.class;
		Class localVarClass = GermanSpellChecker.class;
		Class newVarClass = FrenchSpellChecker.class;
		Class factoryClass = FrenchSpellCheckerFactory.class;

		String factoryMethod = "createInstance";
		boolean done = false;
		try {
			done = JSeuss_Javaassist.factorizeLocalVariable(containerClass,
					localVarClass, newVarClass, factoryClass, factoryMethod,
					true);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(done);
	}

	@Test
	public void testGenerateJavaFactoryClass_CreateJar() {
		// // Class<?>[] toFactory = new Class[] { FrenchSpellChecker.class,
		// // GermanSpellChecker.class };
		// Class<?>[] toFactory = new Class[] { FrenchSpellChecker.class };
		// Class<?> returnClass = SpellCheckerInterface.class;
		// String factoryName =
		// "generated.ch.unibnf.seminars.scg.dif.sample.SpellCheckerFactory";
		//
		// boolean generated = JSeussUtils.generateJavaFactory(toFactory,
		// returnClass, factoryName, true);
		// assertTrue("generated=false", generated);
	}

	@Test
	public void testGenerateJavaInterface_CreateJar() {
		// boolean generated = JSeuss_Javaassist
		// .generateJavaInterface(
		// GermanSpellChecker.class,
		// "generated.ch.unibnf.SpellCheckerInterface",
		// true, true);
		// assertTrue("generated=false", generated);
	}

	@Test
	public void testGenerateJavaInterface_NoCreateJar() {
		// boolean generated = JSeussUtils
		// .generateJavaInterface(
		// GermanSpellChecker.class,
		// "generated.ch.unibnf.seminars.scg.dif.sample.SpellCheckerInterface",
		// true, false);
		// assertTrue("generated=false", generated);
	}
}
