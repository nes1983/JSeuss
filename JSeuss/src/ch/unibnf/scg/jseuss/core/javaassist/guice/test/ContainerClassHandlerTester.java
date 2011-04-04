package ch.unibnf.scg.jseuss.core.javaassist.guice.test;

import generated.guice.SpellCheckerInterface;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import usecase.EmailSender;
import usecase.GermanSpellChecker;

import static org.junit.Assert.*;

import ch.unibnf.scg.jseuss.core.javaassist.guice.ClassDetails;
import ch.unibnf.scg.jseuss.core.javaassist.guice.GuiceBinderGenerator;
import ch.unibnf.scg.jseuss.core.javaassist.guice.GuiceProviderGenerator;
import ch.unibnf.scg.jseuss.core.javaassist.guice.ContainerClassHandler;
import ch.unibnf.scg.jseuss.core.javaassist.guice.JavaInterfaceGenerator;

public class ContainerClassHandlerTester {

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
	public void testMakeGuiceCompliant() {
		if (interfaceClass != null && binderClass != null) {
			ClassDetails generated = ContainerClassHandler.makeGuiceCompliant(
					containerClass, localVarClass, interfaceClass, binderClass,
					false);
			assertTrue(generated != null);

		} else {
			fail("Please initialize interfaceClass and binderClass in Setup() method.");
		}
	}

	@After
	public void tearDown() throws Exception {
	}

}
