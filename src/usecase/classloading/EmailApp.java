package usecase.classloading;

import generated.guice.usecase.classloading.IEmail;
import generated.guice.usecase.classloading.IFrenchSpellChecker;
import usecase.classloading.EmailSender;
import usecase.classloading.mocks.EmailMock;
import usecase.classloading.mocks.FrenchSpellCheckerMock;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class EmailApp {
	public static void main(String[] args) {
		Injector i = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IEmail.class).to(EmailMock.class);
				bind(IFrenchSpellChecker.class).to(FrenchSpellCheckerMock.class);
			}
		});
		
		EmailSender es = i.getInstance(EmailSender.class);
		es.run();
		System.out.println("Language: " + es.getEmail().language );
	}
}
