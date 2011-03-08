package ludo.test;

import static org.junit.Assert.assertEquals;
import ludo.LudoModule;
import ludo.NoStartSquaresModule;
import ludo.Square;
import ludo.SquareBuilder;
import ludo.Stone;
import ludo.StoneFactory;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

/** In the home row, if a player rolls a number higher than he has squares left
 * to move, he may not move.
 *
 */
public class Rule07Test {

    private Square board;
    private Stone stone;
    private final Injector injector = Guice.createInjector(Modules.override(new LudoModule()).with(new NoStartSquaresModule()));

    private final StoneFactory stoneFactory = injector.getInstance(StoneFactory.class);

    @Before
    public void makeBoard() {
        board = injector.getInstance(SquareBuilder.class).squares(3).goal().getFirst();
        stone = stoneFactory.create(board);
    }
    
    @Test
    public void shouldReachGoalWithExactNumber() {
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
