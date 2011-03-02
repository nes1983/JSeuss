package ch.unibe.scglectures;

import static ch.unibe.scglectures.Color.RED;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Rule05Test {

    private Square board;
    private Stone stone;

    @Before
    public void makeBoard() {
        board = new SquareBuilder().startHere(RED).squares(99).getStartSquares().get(RED.ordinal());
        stone = board.occupants().iterator().next();
    }
    
    @Test
    public void checkMove() {
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
