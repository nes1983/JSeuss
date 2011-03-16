/**
 * 
 */
package ch.unibnf.scg.jseuss.core.javaassist.generic.test;

import static org.junit.Assert.*;

import generated.guice.SpellCheckerInterface;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.junit.Before;
import org.junit.Test;

import ch.unibe.niko.EmailSender;
import ch.unibe.niko.FrenchSpellChecker;
import ch.unibe.niko.GermanSpellChecker;
import ch.unibnf.scg.jseuss.core.javaassist.generic.JSeuss_Javaassist;

/**
 * @author TGDMOAH1
 * 
 */
public class JSeussUtilsTest {

	private Class<?> containerClass;
	private Class<?> localVarClass;
	private Class<?> newVarClass;
	private Class<?> factoryClass;
	private Class<?> interfaceClass;

	@Before
	public void setup() {
		containerClass = EmailSender.class;
		localVarClass = GermanSpellChecker.class;
		newVarClass = FrenchSpellChecker.class;
//		factoryClass = FrenchSpellCheckerFactory.class;
//		interfaceClass = SpellCheckerInterface.class;
	}
	
	@Test
	public void testFactorizeJavaassistToGuice_Jar() {
		boolean done = false;
		try {
			done = JSeuss_Javaassist.factorizeToGuice(containerClass,
					localVarClass, interfaceClass, true);
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
	public void testFactorizeLocalVariable_Jar() {
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
