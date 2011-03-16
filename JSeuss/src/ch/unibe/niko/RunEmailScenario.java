package ch.unibe.niko;

import static org.junit.Assert.assertTrue;
import generated.guice.IFrenchSpellChecker;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;


public class RunEmailScenario {
	
	@Test
	public void sendGermanEmail() {
		Injector i = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IFrenchSpellChecker.class).to(GermanSpellChecker.class);
			}
		});
		
		NikoGo ng = i.getInstance(NikoGo.class);
		Email email = ng.go();
		assertTrue(email.language.equalsIgnoreCase("german"));
		
	}
}
