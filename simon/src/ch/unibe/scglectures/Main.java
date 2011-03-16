package ch.unibe.scglectures;

import java.util.Collection;
import java.util.LinkedList;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryProvider;

public class Main {

    public static void main(String... args) {
		Injector injector = Guice.createInjector(
			new AbstractModule() {
				public void configure() {
					//Simple Bindings
					bind(IDice.class).to(Dice.class);
					bind(IStone.class).to(Stone.class);
					bind(IGoalSquare.class).to(GoalSquare.class);
					
					// Providers
					bind(IPlayer.class).toProvider(PlayerProvider.class);
					bind(IBranchSquare.class).toProvider(BranchSquareProvider.class);
					bind(ISquareBuilder.class).toProvider(SquareBuilderProvider.class);
					bind(ISquareGrid.class).toProvider(SquareGridProvider.class);
					bind(ITurtle.class).toProvider(TurtleProvider.class);
					bind(IStone.class).toProvider(StoneProvider.class);
					bind(IStartSquare.class).toProvider(StartSquareProvider.class);
					bind(StringBuilder.class).toProvider(StringBuilderProvider.class);
					bind(new TypeLiteral<Collection<IStone>>() {}).to(new TypeLiteral<LinkedList<IStone>>() {});
					bind(IBranchSquareFactory.class).toProvider(
						    FactoryProvider.newFactory(IBranchSquare.class, BranchSquare.class));
					bind(List<IPlayer>.class).toProvider(ListPlayerProvider.class);
					bind(List<IStartSquare>.class).toProvider(ListStartSquareProvider.class);
			}
		});
		IGame game = injector.getInstance(Ludo.class);
		game.addPlayer();
		game.addPlayer();
		game.setOutput(System.out);
		game.run();
    }
}
