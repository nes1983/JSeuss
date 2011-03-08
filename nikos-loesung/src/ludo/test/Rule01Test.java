package ludo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import ludo.Game;
import ludo.Ludo;
import ludo.LudoFactory;
import ludo.LudoModule;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

import com.google.inject.Guice;
import com.google.inject.Injector;

/** Rule 1: 2 to 4 players can play.
 *
 */
@RunWith(JExample.class)
public class Rule01Test {

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

    @Given("#emptyGame")
    public void zeroPlayersShouldNotBeEnough(Game game) {
        assertEquals(false, game.hasEnoughPlayers());
    }

    @Given("#ludoFactory")
    public void onePlayerShouldNotBeEnough(LudoFactory factory) {
    		Game game = factory.create(1);
        assertEquals(false, game.hasEnoughPlayers());
    }
    
    @Given("#ludoFactory")
    public void twoPlayersShouldBeEnough(LudoFactory factory) {
    		Game game = factory.create(2);
        assertEquals(true, game.hasEnoughPlayers());
    }
 
    @Test(expected=AssertionError.class)
    @Given("#ludoFactory")
    public void fivePlayersShouldBeTooMuch(LudoFactory factory ) {
    		factory.create(5);
    }
    
}
