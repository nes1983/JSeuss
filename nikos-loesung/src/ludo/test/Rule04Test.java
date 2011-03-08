package ludo.test;

import static org.junit.Assert.assertEquals;
import ludo.IPlayer;
import ludo.IPlayerFactory;
import ludo.LudoModule;
import ludo.Square;
import ludo.SquareBuilder;
import ludo.Stone;
import ludo.StoneFactory;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/** A player wins when all of his pieces are on the goal square. 
 * 
 */
public class Rule04Test {

    private Square board;
    private Stone s1, s2, s3;
    private IPlayer player;
    private final Injector injector = Guice.createInjector(new LudoModule());
    private final StoneFactory stoneFactory = injector.getInstance(StoneFactory.class);

    @Before
    public void makeBoard() {
        board = injector.getInstance(SquareBuilder.class).squares(5).goal().getFirst();
        s1 = stoneFactory.create(board);
        s2 = stoneFactory.create(board);
        s3 = stoneFactory.create(board); 
        Injector injector = Guice.createInjector(new LudoModule());
        player = injector.getInstance(IPlayerFactory.class).create(board);
    }
    
    @Test
    public void shouldWinWhenAllPiecesAreOnTheGoal() {
        assertEquals(false, player.isWinner());
        s1.move(5);
        assertEquals(false, player.isWinner());
        s2.move(5);
        assertEquals(false, player.isWinner());
        s3.move(5);
        assertEquals(true, player.isWinner());
     }
    
    
}
