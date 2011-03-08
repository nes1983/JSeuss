package ludo.test;

import static ludo.Color.RED;
import static org.junit.Assert.assertEquals;
import ludo.LudoModule;
import ludo.Square;
import ludo.SquareBuilder;
import ludo.Stone;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;

/** A player needs to roll a 5 to be allowed to move a piece into play. 
 * 
 */
public class Rule05Test {

    private Square start;
    private Stone stone;

    
    
    @Before
    public void makeBoard() {
        start = Guice.createInjector(new LudoModule()).getInstance(SquareBuilder.class)
        	.startHere(RED)
        	.squares(99)
        	.getStartSquares()
        	.get(RED.ordinal());
        stone = start.getOccupants().iterator().next();
    }
    
    @Test
    public void shouldMoveIntoPlayWithFiveOnly() {
        assertEquals(true, stone.atStart());
        assertEquals(false, stone.canMove(0));
        assertEquals(false, stone.canMove(1));
        assertEquals(false, stone.canMove(2));
        assertEquals(false, stone.canMove(3));
        assertEquals(false, stone.canMove(4));
        assertEquals(true, stone.canMove(5)); // strike!
        assertEquals(false, stone.canMove(6));
     }
    
    
}
