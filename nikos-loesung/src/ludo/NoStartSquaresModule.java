package ludo;

import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Providers;

/**
 * Guice module that switches off the generation of start squares by binding them to null. 
 * This speeds up the instantiation in unit tests.
 * @author Niko Schwarz, 2010 
 *
 */
public class NoStartSquaresModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<List<StartSquare>>() {})
			.annotatedWith(StartSquares.class).toProvider(
				(Provider<? extends List<StartSquare>>) Providers.of(null));
	}

}
