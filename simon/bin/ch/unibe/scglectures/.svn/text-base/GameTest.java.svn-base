package ch.unibe.scglectures;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

@RunWith(JExample.class)
public class GameTest {

    @Test
    public Game emptyGame() {
        Game game = new Ludo();
        assertEquals(false, game.hasEnoughPlayers());
        assertEquals(false, game.isRunning());
        return game;
    }

    @Test
    @Given("#emptyGame")
    public Game withOnePlayer(Game game) {
        assertEquals(false, game.hasEnoughPlayers());
        game.addPlayer();
        assertEquals(false, game.hasEnoughPlayers());
        return game;
    }
    
    @Test
    @Given("#withOnePlayer")
    public Game withTwoPlayers(Game game) {
        assertEquals(false, game.hasEnoughPlayers());
        game.addPlayer();
        assertEquals(true, game.hasEnoughPlayers());
        return game;
    }
    
    @Test
    @Given("#withTwoPlayers")
    public Game withFourPlayers(Game game) {
        game.addPlayer();
        game.addPlayer();
        return game;
    }
    
    @Test(expected=AssertionError.class)
    @Given("#withFourPlayers")
    public void failWithFivePlayers(Game game) {
        game.addPlayer();
    }
 
    @Test
    @Given("#withTwoPlayers")
    public Game runningGame(Game game) {
        assertEquals(false, game.isRunning());
        game.startGame();
        assertEquals(true, game.isRunning());
        return game;
    }
    
    @Test(expected=AssertionError.class)
    @Given("#runningGame")
    public void cannotJoinRunningGame(Game game) {
        game.addPlayer();
    }
    
}
