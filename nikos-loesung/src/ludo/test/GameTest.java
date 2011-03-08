package ludo.test;

import static org.junit.Assert.assertEquals;
import ludo.Game;
import ludo.LudoFactory;
import ludo.LudoModule;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

@RunWith(JExample.class)
public class GameTest {
	final Injector injector = Guice.createInjector(new LudoModule());
    
	@Test
	public LudoFactory ludoFactory() {
		return injector.getInstance(LudoFactory.class);
	}
	
	@Given("#ludoFactory")
    public Game emptyGame(LudoFactory factory) {
        Game game = factory.create(0);
        assertEquals(false, game.hasEnoughPlayers());
        assertEquals(false, game.isRunning());
        return game;
    }

    @Test
    @Given("#ludoFactory")
    public Game shouldAcceptFirstPlayer(LudoFactory factory) {
        Game game = factory.create(1); 
    		assertEquals(false, game.hasEnoughPlayers());
    		game = factory.create(2);  
    		assertEquals(true, game.hasEnoughPlayers());
        return game;
    }
    
    @Test
    @Given("#ludoFactory")
    public Game shouldAcceptSecondPlayer(LudoFactory factory) {
        Game game = factory.create(3); 
        assertEquals(true, game.hasEnoughPlayers());
        return game;
    }
    
    @Test
    @Given("#ludoFactory")
    public Game shouldAcceptUptoFourPlayers(LudoFactory factory) {
        Game game = factory.create(4); 
        assertEquals(true, game.hasEnoughPlayers());
        return game;
    }
    
 
    @Test
    @Given("#shouldAcceptSecondPlayer")
    public Game shouldRunGame(Game game) {
        assertEquals(false, game.isRunning());
        game.startGame();
        assertEquals(true, game.isRunning());
        return game;
    }   
}
