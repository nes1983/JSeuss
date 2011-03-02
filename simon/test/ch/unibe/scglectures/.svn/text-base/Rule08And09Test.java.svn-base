package ch.unibe.scglectures;

import static ch.unibe.scglectures.Color.GREEN;
import static ch.unibe.scglectures.Color.RED;
import static ch.unibe.scglectures.Square.NONE;
import static ch.unibe.scglectures.StartSquare.FIRST_STEP;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class Rule08And09Test {

    private Stone red1, red2, green;
    private Square board, redStart, greenStart;

    @Before
    public void makeBoard() {
        SquareBuilder builder = new SquareBuilder();
        builder.squares(1)
                .startHere(RED)
                .startHere(GREEN)
                .squares(11)
                .goal();
        redStart = builder.getStartOf(RED);
        Iterator<Stone> redStones = redStart.occupants().iterator();
        red1 = redStones.next(); 
        red2 = redStones.next(); 
        greenStart = builder.getStartOf(GREEN);
        green = greenStart.anyStone(); 
        board = builder.getFirst();
    }
    
    @Test
    public void rule08() {
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
    public void moveBeyondRestart() {
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
    public void rule09() {
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
    public void rule09DoesNotApplyInGoal() {
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
    public void rule08DoesNotApplyInGoal() {
        red1.move(FIRST_STEP);
        red1.move(12);
        assertEquals(true, red1.atGoal());
        green.move(FIRST_STEP);
        green.move(12);
        assertEquals(true, green.atGoal());
        assertEquals(true, red1.atGoal());
    }
    
    
}
