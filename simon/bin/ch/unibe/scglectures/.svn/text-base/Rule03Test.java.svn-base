package ch.unibe.scglectures;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Rule03Test {

    private Square board;
    private Stone stone;

    @Before
    public void makeBoard() {
        board = new SquareBuilder().squares(99).getFirst();
        stone = board.makeStone(null);
    }
    
    @Test
    public void moveStone() {
        assertEquals(0, board.indexOf(stone));
        stone.move(1);
        assertEquals(1, board.indexOf(stone));
        stone.move(2);
        assertEquals(3, board.indexOf(stone));
        stone.move(3);
        assertEquals(6, board.indexOf(stone));
    }
    
}
