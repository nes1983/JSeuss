package ludo.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import ludo.Game;
import ludo.LudoFactory;
import ludo.MockModule;
import ludo.Square;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Each player rolls and then moves his piece clockwise along the squares.
 * 
 * 
 */
@RunWith(JExample.class)
public class Rule03IntegrationTest {
	private final Injector injector = Guice.createInjector(MockModule
			.ludoMockModule());
	
	@Test
	public Game game() {
		LudoFactory f = injector.getInstance(LudoFactory.class);
		Game g = f.create(2);
		g.startGame();
		return g;
	}
	
	@Given("game")
	public void shouldMoveStoneByOneTwoAndThreeFields(Game game) {
		Square p1Square = game.getCurrentPlayer().anyStone().location();
		assertFalse(p1Square.getOccupants().isEmpty());

		// First step
		assertTrue(p1Square.next().getOccupants().isEmpty());
		playOneGameStep(game, 5);
		assertFalse(p1Square.next().getOccupants().isEmpty());
		p1Square = p1Square.next(); // update p1Square

		Square p2Square = game.getCurrentPlayer().anyStone().location();
		assertFalse(p2Square.getOccupants().isEmpty());

		// Second player, first step
		assertTrue(p2Square.next(5, null).getOccupants().isEmpty());
		playOneGameStep(game, 5);
		assertFalse(p2Square.next(5, null).getOccupants().isEmpty());

		// Second player, second step
		assertTrue(p1Square.next(3, null).getOccupants().isEmpty());
		playOneGameStep(game, 3);
		assertFalse(p1Square.next(3, null).getOccupants().isEmpty());
	}

	private static void playOneGameStep(Game game, int steps) {
		game.getDie().setNextRoll(steps);
		game.playNextTurn();
	}
}
