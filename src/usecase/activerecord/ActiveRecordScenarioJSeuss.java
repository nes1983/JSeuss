package usecase.activerecord;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import generated.guice.usecase.activerecord.IDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.unibnf.scg.jseuss.testing.JSeuss;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

@RunWith(JSeuss.class)
public class ActiveRecordScenarioJSeuss {

	IDatabase mock = mock(IDatabase.class);
	@Test
	public void testUsecaseScenario() throws Throwable {
		Injector i = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabase.class).toInstance(mock);
			}
		});
		
		
		Person person = i.getInstance(Person.class);
		person.setName("Vogt");
		person.setPrename("Hans");
		person.setAge(41);
		person.save();
		
		verify(mock).save(person);

		
	}
	
}
