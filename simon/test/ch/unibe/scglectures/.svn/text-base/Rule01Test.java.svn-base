package ch.unibe.scglectures;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Rule01Test {

    @Test
    public void makeGame() {
        Game game = new Ludo();
        assertEquals(false, game.hasEnoughPlayers());
    }

    @Test
    public void onePlayer() {
        Game game = new Ludo();
        game.addPlayer();
        assertEquals(false, game.hasEnoughPlayers());
    }
    
    @Test
    public void twoPlayer() {
        Game game = new Ludo();
        game.addPlayer();
        game.addPlayer();
        assertEquals(true, game.hasEnoughPlayers());
    }
 
    @Test(expected=AssertionError.class)
    public void fivePlayer() {
        Game game = new Ludo();
        game.addPlayer();
        game.addPlayer();
        game.addPlayer();
        game.addPlayer();
        // next fails
        game.addPlayer();
    }
    
}
