package ludo.test;

import static ludo.Color.GREEN;
import static ludo.Color.RED;
import static org.junit.Assert.assertEquals;
import ludo.BranchSquare;
import ludo.LudoModule;
import ludo.Square;
import ludo.SquareBuilder;
import ludo.Stone;
import ludo.StoneFactory;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class BranchSquareTest {

    private Square board;
    private Stone red;
    private Stone green;
    private final Injector injector = Guice.createInjector(new LudoModule());
    private final StoneFactory stoneFactory = injector.getInstance(StoneFactory.class);

    @Before
    public void makeBoard() {
        board = injector.getInstance(SquareBuilder.class).squares(3)
                .branch(RED).squares(1).goal().endOfBranch()
                .squares(99).getFirst();
    }
    
    @Test 
    public void shouldTakeTheBranch() {
        red = stoneFactory.create(board, RED);
        red.move(5);
        BranchSquare branch = (BranchSquare) board.next().next().next();
        assertEquals(red.location(), branch.branch().next());
    }

    @Test 
    public void shouldNotTakeBranchUsingCanMoveMethod() {
        green = stoneFactory.create(board, GREEN);
        assertEquals(true, green.canMove(5));
        // NOTE the branch is shorter then six fields
        assertEquals(true, green.canMove(6));
    }
    
    @Test 
    public void shouldTakeBranchUsingCanMoveMethod() {
        red = stoneFactory.create(board, RED);
        assertEquals(true, red.canMove(5));
        // NOTE the branch is shorter then six fields
        assertEquals(false, red.canMove(6)); // false!
    }
    
}
