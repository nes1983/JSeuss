package ludo.test;

import static org.junit.Assert.assertTrue;
import ludo.Die;
import ludo.IDie;

import org.junit.Test;

public class DiceTest {

    private IDie dice = new Die();
    
    @Test
    public void shouldRollAtLeastOne() {
        for (int n = 0; n < 10000; n++) 
            assertTrue(1 <= dice.roll());
    }

    @Test
    public void shouldRollAtMostSix() {
        for (int n = 0; n < 10000; n++) 
            assertTrue(dice.roll() <= 6);
    }
    
    
}
