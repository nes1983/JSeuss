package ludo.test;

import static org.junit.Assert.assertEquals;
import ludo.Color;
import ludo.Game;
import ludo.IPlayer;
import ludo.Ludo;
import ludo.LudoFactory;
import ludo.LudoModule;
import ludo.Square;
import ludo.Stone;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;
import ch.unibe.util.Length;

/** Each player chooses a color and has 4 pieces in his square to hold a
 * player's pieces before they are allowed into play
 *
 */
@RunWith(JExample.class)
public class Rule02Test {
	private final Injector injector = Guice.createInjector(new LudoModule());
	
	@Test
	public LudoFactory ludoFactory() {
		return injector.getInstance(LudoFactory.class);
	}
	
	@Given("ludoFactory")
	public Ludo emptyGame(LudoFactory factory) { 
        Ludo game = factory.create(0);
        return game;
    }

	@Given("#ludoFactory")
    public void firstPlayerShouldStartWithFourRedPieces(LudoFactory factory) {
		Game game = factory.create(1);
        IPlayer red = game.getPlayers().get(0);
        assertEquals(4, Length.of(red.stones()));
        for (Stone each: red.stones()) {
            assertEquals(Color.RED, each.getColor());
            assertEquals(true, each.atStart());
        }
    }

	@Given("#ludoFactory")
    public void secondPlayerShouldStartWithFourGreenPieces(LudoFactory factory) {
        Game game = factory.create(2);
        IPlayer green = game.getPlayers().get(1);
        assertEquals(4, Length.of(green.stones()));
        for (Stone each: green.stones()) {
            assertEquals(Color.GREEN, each.getColor());
            assertEquals(true, each.atStart());
        }
    }
    
	@Given("#emptyGame")
    public void shouldHaveFourRedPiecesOnRedStart(Ludo game) {
        Square red = game.getStartSquare(Color.RED);
        assertEquals(4, Length.of(red.getOccupants()));
        for (Stone each: red.getOccupants()) {
            assertEquals(Color.RED, each.getColor());
            assertEquals(true, each.atStart());
        }
    }

	@Given("#emptyGame")
    public void shouldHaveFourGreenPiecedOnGreenStart(Ludo game) {
        Square red = game.getStartSquare(Color.GREEN);
        assertEquals(4, Length.of(red.getOccupants()));
        for (Stone each: red.getOccupants()) {
            assertEquals(Color.GREEN, each.getColor());
            assertEquals(true, each.atStart());
        }
    }
}
