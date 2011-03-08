package ludo.test;

import static org.junit.Assert.assertEquals;

import javax.swing.text.html.HTMLDocument.Iterator;

import ludo.Game;
import ludo.IPlayer;
import ludo.Ludo;
import ludo.LudoFactory;
import ludo.LudoModule;
import ludo.MockModule;
import ludo.Player;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
/** When a player rolls a 6, he may move his piece and then roll again.
 * 
 */
public class Rule06Test {
	
	Injector injector = Guice.createInjector(MockModule.ludoMockModule());

    @Test
    public void shouldRepeatTurnWhenSixRolled() {
    		LudoFactory factory = injector.getInstance(LudoFactory.class);
    		Ludo game = factory.create(2);
    		java.util.Iterator<IPlayer> iterator = game.getPlayers().iterator();
        IPlayer red = iterator.next();
        IPlayer green = iterator.next();
        game.startGame();
        
        assertEquals(red, game.getCurrentPlayer());
        playOneGameStep(game, 1);
        assertEquals(green, game.getCurrentPlayer());
        playOneGameStep(game, 2);
        assertEquals(red, game.getCurrentPlayer());
        playOneGameStep(game, 3);
        assertEquals(green, game.getCurrentPlayer());
        playOneGameStep(game, 4);
        assertEquals(red, game.getCurrentPlayer());
        playOneGameStep(game, 5);
        assertEquals(green, game.getCurrentPlayer());
        playOneGameStep(game, 6);
        assertEquals(green, game.getCurrentPlayer()); // repeat green player!
        playOneGameStep(game, 6);
        assertEquals(green, game.getCurrentPlayer()); // repeat green player!
        playOneGameStep(game, 6);
        assertEquals(green, game.getCurrentPlayer()); // repeat green player!
    }
    
	private static void playOneGameStep(Game game, int steps) {
		game.getDie().setNextRoll(steps);
		game.playNextTurn();
	}
    
}
