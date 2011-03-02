package ch.unibe.scglectures;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Rule06Test {

    @Test
    public void repeatOnSix() {
        Ludo game = new Ludo();
        Player red = game.addPlayer();
        Player green = game.addPlayer();
        game.startGame();
        
        assertEquals(red, game.currentPlayer());
        game.nextTurn(1);
        assertEquals(green, game.currentPlayer());
        game.nextTurn(2);
        assertEquals(red, game.currentPlayer());
        game.nextTurn(3);
        assertEquals(green, game.currentPlayer());
        game.nextTurn(4);
        assertEquals(red, game.currentPlayer());
        game.nextTurn(5);
        assertEquals(green, game.currentPlayer());
        game.nextTurn(6);
        assertEquals(green, game.currentPlayer()); // repeat green player!
        game.nextTurn(6);
        assertEquals(green, game.currentPlayer()); // repeat green player!
        game.nextTurn(6);
        assertEquals(green, game.currentPlayer()); // repeat green player!
    }
    
}
