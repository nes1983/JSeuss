package ch.unibe.scglectures;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Rule04Test {

    private Square board;
    private Stone s1, s2, s3;
    private Player player;

    @Before
    public void makeBoard() {
        board = new SquareBuilder().squares(3).goal().getFirst();
        s1 = board.makeStone(null);
        s2 = board.makeStone(null);
        s3 = board.makeStone(null); 
        player = new Player(board);
    }
    
    @Test
    public void checkMove() {
        assertEquals(false, player.isWinner());
        s1.move(3);
        assertEquals(false, player.isWinner());
        s2.move(3);
        assertEquals(false, player.isWinner());
        s3.move(3);
        assertEquals(true, player.isWinner());
     }
    
    
}
