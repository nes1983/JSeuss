package ludo.test;

import static ludo.Color.GREEN;
import static ludo.Color.RED;
import static ludo.Square.NONE;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;

import ludo.LudoModule;
import ludo.NoStartSquaresModule;
import ludo.Square;
import ludo.SquareBuilder;
import ludo.StartSquare;
import ludo.StartSquares;
import ludo.Stone;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;
import com.google.inject.util.Providers;

/** Rule 8: When a piece enters a square that is occupied by another player's
piece, the piece of the other player returns to the initial square.
 * Rule 9: If however, the entered square is occupied by two pieces of another
 * player's pieces, then the piece that entered the square must return!
 * 
 * @author akuhn
 *
 */
public class Rule08And09Test {

    private Stone red1, red2, green;
    private Square board, redStart, greenStart;
    private Integer  FIRST_STEP;

    @Before
    public void makeBoard() {
    		Injector inj = Guice.createInjector(Modules.override(new LudoModule()).with(new NoStartSquaresModule()));
        SquareBuilder builder = inj.getInstance(SquareBuilder.class);
        FIRST_STEP = inj.getInstance(Key.get(Integer.class, Names.named("FIRST_STEP")));
        builder.squares(1)
                .startHere(RED)
                .startHere(GREEN)
                .squares(11)
                .goal();
        redStart = builder.getStartOf(RED);
        Iterator<Stone> redStones = redStart.getOccupants().iterator();
        red1 = redStones.next(); 
        red2 = redStones.next(); 
        greenStart = builder.getStartOf(GREEN);
        green = greenStart.anyStone(); 
        board = builder.getFirst();
    }
    
    @Test
    public void solitaryPieceShouldRestartWhenCaptured() {
        green.move(FIRST_STEP);
        green.move(5);
        assertEquals(5, board.indexOf(green));
        red1.move(FIRST_STEP);
        red1.move(2);
        assertEquals(2, board.indexOf(red1));
        red1.move(3);
        // assert green is back at start
        assertEquals(greenStart, green.location()); 
        assertEquals(NONE, board.indexOf(green));
        assertEquals(5, board.indexOf(red1));
    }
    
    @Test
    public void restartedPieceShouldMoveAgain() {
    	// NOTE this example is motivated by an actual bug of previous years!
        green.move(FIRST_STEP);
        green.move(5);
        assertEquals(5, board.indexOf(green));
        green.restart();
        assertEquals(greenStart, green.location()); 
        assertEquals(NONE, board.indexOf(green));
        green.move(FIRST_STEP);
        green.move(5);
        assertEquals(5, board.indexOf(green));
    }

    @Test
    public void twoPiecesShouldNotRestartWhenCapturedRatherTheCapturingPieceMustRestart() {
        red1.move(FIRST_STEP);
        red1.move(5);
        assertEquals(5, board.indexOf(red1));
        red2.move(FIRST_STEP);
        red2.move(5);
        assertEquals(5, board.indexOf(red2));
        green.move(FIRST_STEP);
        green.move(2);
        assertEquals(2, board.indexOf(green));
        green.move(3);
        // assert green is back at start
        assertEquals(greenStart, green.location()); 
        assertEquals(NONE, board.indexOf(green));
        assertEquals(5, board.indexOf(red1));
        assertEquals(5, board.indexOf(red2));
    }
    
    @Test
    public void onGoalSquareShouldNotApplyRule09() {
        red1.move(FIRST_STEP);
        red1.move(12);
        assertEquals(true, red1.atGoal());
        red2.move(FIRST_STEP);
        red2.move(12);
        assertEquals(true, red2.atGoal());
        green.move(FIRST_STEP);
        green.move(12);
        assertEquals(true, green.atGoal());
        assertEquals(true, red1.atGoal());
        assertEquals(true, red2.atGoal());
    }

    @Test
    public void onGoalSquareShouldNotApplyRule08() {
        red1.move(FIRST_STEP);
        red1.move(12);
        assertEquals(true, red1.atGoal());
        green.move(FIRST_STEP);
        green.move(12);
        assertEquals(true, green.atGoal());
        assertEquals(true, red1.atGoal());
    }
    
    
}
