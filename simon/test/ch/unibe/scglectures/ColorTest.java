package ch.unibe.scglectures;

import static ch.unibe.scglectures.Color.BLUE;
import static ch.unibe.scglectures.Color.GREEN;
import static ch.unibe.scglectures.Color.RED;
import static ch.unibe.scglectures.Color.YELLOW;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ColorTest {

    @Test
    public void testNext() {
        assertEquals(GREEN, RED.next());
        assertEquals(YELLOW, GREEN.next());
        assertEquals(BLUE, YELLOW.next());
        assertEquals(RED, BLUE.next());
    }

}
