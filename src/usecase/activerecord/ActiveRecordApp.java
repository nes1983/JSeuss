package usecase.activerecord;

import generated.guice.usecase.activerecord.IDatabase;
import usecase.activerecord.mocks.DatabaseMock;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ActiveRecordApp {
	public static void main(String[] args) {
		Injector i = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabase.class).to(DatabaseMock.class);
			}
		});
		
		Person person = i.getInstance(Person.class);
		person.setName("Muster");
		person.setPrename("Hans");
		person.setAge(41);
		person.save();
		
	}
}