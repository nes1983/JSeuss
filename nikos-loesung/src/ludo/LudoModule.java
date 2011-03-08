package ludo;

import java.io.PrintStream;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryProvider;
import com.google.inject.name.Names;

/**
 * Module that holds the default guice bindings for the ludo game.
 * @author Niko Schwarz, 2010 
 *
 */
public class LudoModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IDie.class).to(Die.class);
		bind(SquareBuilder.class).toProvider(SquareBuilderProvider.class);
		bind(IPlayerFactory.class).to(PlayerFactory.class);
		bindConstant().annotatedWith(Names.named("FIRST_STEP")).to(5);
		bindConstant().annotatedWith(Names.named("NUMBER_OF_STONES_ON_STARTFIELD")).to(4);
		bind(new TypeLiteral<List<StartSquare>>() {}).annotatedWith(StartSquares.class)
						.toProvider(StartSquaresProvider.class).in(Singleton.class);
		bind(SquareGrid.class).toProvider(SquareGridProvider.class);
		bind(PrintStream.class).toInstance(System.out);
		bind(IBranchSquareFactory.class).toProvider(
		FactoryProvider.newFactory(IBranchSquareFactory.class, BranchSquare.class));
	}	
}
