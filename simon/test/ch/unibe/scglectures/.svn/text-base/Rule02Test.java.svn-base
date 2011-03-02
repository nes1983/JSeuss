package ch.unibe.scglectures;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.unibe.util.Length;

public class Rule02Test {

    @Test
    public void redPlayerHasFourStonesAtStart() {
        Game game = new Ludo();
        Player red = game.addPlayer();
        assertEquals(4, Length.of(red.stones()));
        for (Stone each: red.stones()) {
            assertEquals(Color.RED, each.color);
            assertEquals(true, each.atStart());
        }
    }

    @Test
    public void greenPlayerHasFourStonesAtStart() {
        Game game = new Ludo();
        game.addPlayer();
        Player green = game.addPlayer();
        assertEquals(4, Length.of(green.stones()));
        for (Stone each: green.stones()) {
            assertEquals(Color.GREEN, each.color);
            assertEquals(true, each.atStart());
        }
    }
    
    @Test
    public void redStartHasFourStones() {
        Ludo game = new Ludo();
        Square red = game.getStartSquare(Color.RED);
        assertEquals(4, Length.of(red.occupants()));
        for (Stone each: red.occupants()) {
            assertEquals(Color.RED, each.color);
            assertEquals(true, each.atStart());
        }
    }

    @Test
    public void greenStartHasFourStones() {
        Ludo game = new Ludo();
        Square red = game.getStartSquare(Color.GREEN);
        assertEquals(4, Length.of(red.occupants()));
        for (Stone each: red.occupants()) {
            assertEquals(Color.GREEN, each.color);
            assertEquals(true, each.atStart());
        }
    }
    
    
}
