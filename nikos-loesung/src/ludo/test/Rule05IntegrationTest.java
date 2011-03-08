package ludo.test;

import static org.junit.Assert.assertEquals;
import ludo.Game;
import ludo.LudoFactory;
import ludo.LudoModule;
import ludo.MockModule;
import ludo.Square;
import ludo.StoneFactory;
import static org.junit.Assert.*;
import org.junit.Test;
import ludo.*;
import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;
import org.junit.runner.RunWith;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * A player needs to roll a 5 to be allowed to move a piece into play.
 * 
 */
@RunWith(JExample.class)
public class Rule05IntegrationTest {
	private final Injector injector = Guice.createInjector(MockModule
			.ludoMockModule());

	@Test
	public Game game() {
		LudoFactory f = injector.getInstance(LudoFactory.class);
		Game g = f.create(2);
		g.startGame();
		return g;
	}

	@Given("#game")
	public void shouldMoveIntoPlayWithFiveOnly(Game game) {
		Square out1 = game.getCurrentPlayer().anyStone().location().next();
		assertFalse(occupied(out1));

		playOneGameStep(game, 1);
		assertEquals(false, occupied(out1));

		Square out2 = game.getCurrentPlayer().anyStone().location().next();

		playOneGameStep(game, 2);
		assertEquals(false, occupied(out2));
		playOneGameStep(game, 3);
		assertEquals(false, occupied(out1));
		playOneGameStep(game, 4);
		assertEquals(false, occupied(out2));
		playOneGameStep(game, 5);
		assertEquals(true, occupied(out1));// strike!
		playOneGameStep(game, 6);
		assertEquals(false, occupied(out2));

	}

	private boolean occupied(Square out) {
		return !out.getOccupants().isEmpty();
	}

	private static void playOneGameStep(Game game, int steps) {
		game.getDie().setNextRoll(steps);
		game.playNextTurn();
	}

}
