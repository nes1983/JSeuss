package ch.unibe.scglectures;

import static ch.unibe.scglectures.Color.GREEN;
import static ch.unibe.scglectures.Color.RED;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class BranchSquareTest {

    private Square board;
    private Stone red;
    private Stone green;

    @Before
    public void makeBoard() {
        board = new SquareBuilder().squares(3)
                .branch(RED).squares(1).goal().endOfBranch()
                .squares(99).getFirst();
    }
    
    @Test 
    public void moveRed() {
        red = board.makeStone(RED);
        red.move(5);
        BranchSquare branch = (BranchSquare) board.next().next().next();
        assertEquals(red.location(), branch.branch().next());
    }

    @Test 
    public void moveGreen() {
        green = board.makeStone(GREEN);
        green.move(5);
        BranchSquare branch = (BranchSquare) board.next().next().next();
        assertEquals(green.location(), branch.next().next());
    }

    @Test 
    public void checkGreenMove() {
        green = board.makeStone(GREEN);
        assertEquals(true, green.canMove(5));
        assertEquals(true, green.canMove(6));
    }
    
    @Test 
    public void checkRedMove() {
        red = board.makeStone(RED);
        assertEquals(true, red.canMove(5));
        assertEquals(false, red.canMove(6)); // false!
    }
    
}
