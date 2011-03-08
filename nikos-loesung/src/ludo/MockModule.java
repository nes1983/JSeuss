package ludo;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;

/**
 * Module that binds IDie to MockDie.
 * @author Niko Schwarz, 2010 
 * 
 */
public class MockModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IDie.class).to(MockDie.class);
	}
	
	public static Module ludoMockModule() {
		return Modules.override(new LudoModule()).with(new MockModule());
	}
}
