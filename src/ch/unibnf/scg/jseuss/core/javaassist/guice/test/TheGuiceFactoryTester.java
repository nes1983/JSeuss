package ch.unibnf.scg.jseuss.core.javaassist.guice.test;

import generated.guice.SpellCheckerBinder;
import generated.guice.SpellCheckerInterface;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import ch.unibnf.scg.jseuss.core.javaassist.guice.GuiceBinderGenerator;
import ch.unibnf.scg.jseuss.core.javaassist.guice.GuiceProviderGenerator;
import ch.unibnf.scg.jseuss.core.javaassist.guice.JSeuss_TheGuiceFactory;
import ch.unibnf.scg.jseuss.core.javaassist.guice.JavaInterfaceGenerator;
import ch.unibnf.scg.sample.emailservice.EmailSender;
import ch.unibnf.scg.sample.spellCheck.FrenchSpellChecker;
import ch.unibnf.scg.sample.spellCheck.GermanSpellChecker;

public class TheGuiceFactoryTester {

	private Class<?> containerClass;
	private Class<?> localVarClass;
	private Class<?> interfaceClass;
	private Class<?> binderClass;

	@Before
	public void setUp() throws Exception {
		containerClass = EmailSender.class;
		localVarClass = GermanSpellChecker.class;
		interfaceClass = SpellCheckerInterface.class;
		binderClass = SpellCheckerBinder.class;
	}

	@Test
	public void testConvertClassToGuice() {
		if (interfaceClass != null && binderClass != null) {
			try {
				boolean done = JSeuss_TheGuiceFactory.toGuice(
						containerClass, localVarClass, interfaceClass,
						binderClass, false);
				assertTrue(done);
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (CannotCompileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			fail("Please initialize interfaceClass and binderClass in Setup() method.");
		}
	}

	@After
	public void tearDown() throws Exception {
	}

}
