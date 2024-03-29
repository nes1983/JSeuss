package ch.unibe.scglectures;

/** Stream of random numbers between 1 and 6.
 *<P> 
 * @author Adrian Kuhn, 2007
 *
 */
public class Dice implements IDice {

   private static final int FACES = 6;
   
   public int roll() {
       return (int) (Math.random() * FACES) + 1;
    }
    
    
}
