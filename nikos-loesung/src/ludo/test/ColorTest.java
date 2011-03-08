package ludo.test;

import static ludo.Color.BLUE;
import static ludo.Color.GREEN;
import static ludo.Color.RED;
import static ludo.Color.YELLOW;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ColorTest {

    @Test
    public void shouldCycleThroughColors() {
        assertEquals(GREEN, RED.next());
        assertEquals(YELLOW, GREEN.next());
        assertEquals(BLUE, YELLOW.next());
        assertEquals(RED, BLUE.next());
    }

}
