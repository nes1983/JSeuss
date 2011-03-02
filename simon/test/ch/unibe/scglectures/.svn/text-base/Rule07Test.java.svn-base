package ch.unibe.scglectures;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Rule07Test {

    private Square board;
    private Stone stone;

    @Before
    public void makeBoard() {
        board = new SquareBuilder().squares(3).goal().getFirst();
        stone = board.makeStone(null);
    }
    
    @Test
    public void checkMove() {
        assertEquals(true, stone.canMove(0));
        assertEquals(true, stone.canMove(1));
        assertEquals(true, stone.canMove(2));
        assertEquals(true, stone.canMove(3));
        // beyond goal
        assertEquals(false, stone.canMove(4));
        assertEquals(false, stone.canMove(5));
        assertEquals(false, stone.canMove(6));
     }
    
    
}
