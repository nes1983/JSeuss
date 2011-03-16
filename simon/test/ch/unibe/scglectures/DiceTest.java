package ch.unibe.scglectures;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DiceTest {

    private IDice dice = new Dice();
    
    @Test
    public void rollsAtLeastOne() {
        for (int n = 0; n < 10000; n++) 
            assertTrue(1 <= dice.roll());
    }

    @Test
    public void rollsAtMostSix() {
        for (int n = 0; n < 10000; n++) 
            assertTrue(dice.roll() <= 6);
    }
    
    
}
