package usecase;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import org.junit.Test;

import ch.unibnf.scg.jseuss.core.javaassist.generic.JSeussJavaassist;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;


public class RunEmailScenario {
	
	@Test
	public void sendGermanEmail() {
		Injector i = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
//				bind(IFrenchSpellChecker.class).to(GermanSpellChecker.class);
			}
		});
		
		EmailSender es = i.getInstance(EmailSender.class);
//		assertNotNull(es.ifrenchspellcheckerProvider);
//		Email e = new Email();
//		
//		es.ifrenchspellcheckerProvider.get().check(e);
//		
//		es.sendEmail(e);
//		assertEquals("German", e.language );
//		
	}
	
	@Test
	public void testFactorizeJavaassistToGuice_Jar() throws NotFoundException, ClassNotFoundException, CannotCompileException, IOException {
		Class<EmailSender> containerClass = EmailSender.class;
		Class<GermanSpellChecker> localVarClass = GermanSpellChecker.class;
		Class<IFrenchSpellChecker> interfaceClass = IFrenchSpellChecker.class;

		CtClass emailSender = JSeussJavaassist.factorizeToGuice(containerClass, localVarClass, interfaceClass);
		JSeussJavaassist.writeCtClass(emailSender);
	}
}
